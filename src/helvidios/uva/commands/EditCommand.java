package helvidios.uva.commands;

import helvidios.uva.*;
import helvidios.uva.utils.*;
import static helvidios.uva.utils.FileUtils.*;
import static helvidios.uva.utils.Utils.*;
import helvidios.uva.commands.exceptions.*;
import java.util.*;
import org.apache.log4j.Logger;

public class EditCommand extends Command{
  private static final Logger logger = Logger.getLogger(EditCommand.class);

  private UhuntFacade uhunt;
  private KeyValueStore editorPathSettings;
  private Template template;
  public EditCommand(KeyValueStore editorPathSettings, KeyValueStore templateSettings, UhuntFacade uhunt){
    this.editorPathSettings = editorPathSettings;
    this.uhunt = uhunt;
    template = new Template(templateSettings);
    this.name = "edit";
    this.usage = name + " ({problemId} | {filePath})";
    this.description = "Launches editor and loads a code file corresponding to problem id or file path.";
  }

  private String problemId;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2) incorrectSyntax();
    problemId = tokens[1];
    return this;
  }

  private String applyTemplate(String lang, String filePath, String username) throws Exception {
    Map<Template.Variable, String> vars = new HashMap<>();
    vars.put(Template.Variable.USERNAME, getFullName(username));
    vars.put(Template.Variable.PROBLEM_ID, getFileName(filePath));
    vars.put(Template.Variable.PROBLEM_URL, getProblemUrl(getProblemId(filePath)));
    vars.put(Template.Variable.PROBLEM_NAME, getProblemName(getProblemId(filePath)));
    return template.apply(lang, vars);
  }

  private String getProblemName(String problemId){
    String name = "";
    try{
      name = uhunt.getProblemName(problemId);
    }catch(Exception ex){
      logger.error(String.format("Unable to get name for problemId %s. \n%s",
        problemId, getStackTrace(ex)));
    }
    return name;
  }

  private String getFullName(String username){
    String name = "";
    try{
      name = uhunt.getFullName(username);
    }catch(Exception ex){
      logger.error(String.format("Unable to get full name for user %s. \n%s",
        username, getStackTrace(ex)));
    }
    return name;
  }

  public void execute(UserContext uc) throws Exception{
    if(hasFileExtension(problemId)){
      if(!fileExists(problemId)){
        String lang = getFileExtension(problemId);
        if(!isSupportedLang(lang)) throw new Exception(
          String.format("Language extension '%s' is not supported. Supported extensions: %s.", lang, supportedLangs())
        );
        writeFile(problemId, applyTemplate(lang, problemId, uc.getUser().getUsername()));
      }
      launchEditor(editorPathSettings, problemId);
    }else{
        List<String> files = listFiles(problemId, supportedLangs());
        if(files.isEmpty()) throw new CommandExecutionException(
          String.format("Unable to find file matching %s", problemId)
        );
        if(files.size() > 1) throw new CommandExecutionException(
          String.format("More than one file match problem id %s. Supply file name with extension.", problemId)
        );
        launchEditor(editorPathSettings, files.get(0));
    }
  }
}
