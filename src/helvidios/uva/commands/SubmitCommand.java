package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import static helvidios.uva.utils.FileUtils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;
import java.util.*;
import org.apache.log4j.Logger;

public class SubmitCommand extends Command{
  private final Logger logger = Logger.getLogger(SubmitCommand.class);

  public SubmitCommand(){
    this.name = "submit";
    this.usage = name + " ({problemId} | {filePath})";
    this.description = "Submits a code file for specific problem id to UVA.";
  }

  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2) incorrectSyntax();
    problemId = tokens[1];
    return this;
  }

  private void submitFile(String filePath, UserContext uc) throws Exception {
    String lang = getFileExtension(filePath);
    if(!isSupportedLang(lang)) throw new Exception(
      String.format("Language extension '%s' is not supported. Supported extensions: %s.", lang, supportedLangs())
    );
    String problemId = getFileName(filePath).replaceAll("\\D+","");
    System.out.printf("Submitting file '%s' (problemId=%s, lang=%s)...\n", filePath, problemId, lang);
    uc.getUvaFacade().submitProblem(problemId, lang, preprocessCode(readFile(filePath), lang));
  }

  private String preprocessCode(String code, String lang){
    if(lang.equals("java")){ // in java, class name must be Main for UVA to compile it
      return code.replaceFirst("\\s*public\\s+class\\s+\\w+", "\npublic class Main");
    }
    return code;
  }

  private String problemId;
  public void execute(UserContext uc) throws Exception{
    if(hasFileExtension(problemId)){
      if(!fileExists(problemId)) throw new Exception(
        String.format("File %s does not exist.", problemId)
      );
      submitFile(problemId, uc);
    }else{ // derive file name from problem id
      List<String> files = listFiles(problemId, supportedLangs());
      if(files.isEmpty()) throw new CommandExecutionException(
        String.format("Unable to find file matching %s", problemId)
      );
      if(files.size() > 1) throw new CommandExecutionException(
        String.format("More than one file match problem id %s (%s). Supply file name with extension.", problemId, files.toString())
      );
      submitFile(files.get(0), uc);
    }
  }
}
