package helvidios.uva.commands;

import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;
import static  helvidios.uva.utils.FileUtils.*;

public class TemplateCommand extends Command{
  private KeyValueStore templateSettings;
  public TemplateCommand(KeyValueStore templateSettings){
    this.templateSettings = templateSettings;
    this.name = "tpl";
    this.usage = name + " (add {tplPath} | show | rm {lang})";
    this.description = "Configures templates for new code files. E.g. 'template.cpp' identifies a \n" +
                        "template that will be applied when a new C++ file is created.";
  }

  private String action;
  private String templatePath;
  private String lang;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2 || (!tokens[1].equals("add") && !tokens[1].equals("show") && !tokens[1].equals("rm"))) incorrectSyntax();
    if(tokens.length < 3 && (tokens[1].equals("add") || tokens[1].equals("rm"))) incorrectSyntax();
    action = tokens[1];
    if(action.equals("add")) templatePath = tokens[2];
    if(action.equals("rm")) lang = tokens[2];
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    switch(action){
      case "add":
        templateSettings.store(getFileExtension(templatePath), templatePath);
        break;
      case "show":
        System.out.print(templateSettings.list());
        break;
      case "rm":
        templateSettings.remove(lang);
        break;
    }
  }
}
