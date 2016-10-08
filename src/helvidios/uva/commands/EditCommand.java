package helvidios.uva.commands;

import helvidios.uva.*;
import helvidios.uva.utils.*;
import static helvidios.uva.utils.FileUtils.*;
import static helvidios.uva.utils.Utils.*;
import helvidios.uva.commands.exceptions.*;
import java.util.*;

public class EditCommand extends Command{
  private KeyValueStore editorPathSettings;
  private Template template;
  public EditCommand(KeyValueStore editorPathSettings, KeyValueStore templateSettings){
    this.editorPathSettings = editorPathSettings;
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

  public void execute(UserContext uc) throws Exception{
    if(hasFileExtension(problemId)){
      if(!fileExists(problemId)){
        String lang = getFileExtension(problemId);
        if(!isSupportedLang(lang)) throw new Exception(
          String.format("Language extension %s is not supported. Supported extensions: %s.", lang, supportedLangs())
        );
        // apply template (if any) to the file
        String contents = template.apply(lang, getFileName(problemId));
        writeFile(problemId, contents);
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
