package helvidios.uva.commands;

import helvidios.uva.*;
import helvidios.uva.utils.*;
import helvidios.uva.commands.exceptions.*;

public class EditorCommand extends Command{
  private String editorPath;
  private KeyValueStore editorPathSettings;
  public EditorCommand(KeyValueStore editorPathSettings){
    this.editorPathSettings = editorPathSettings;
    this.name = "editor";
    this.usage = name + " (set {editorPath} | get)";
    this.description = "Configures editor to be used when opening problems for editing.";
  }

  private String action;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2) incorrectSyntax();
    action = tokens[1];
    if(action.equals("set")){
      if(tokens.length < 3) incorrectSyntax();
      StringBuilder sb = new StringBuilder();
      for(int i = 2; i < tokens.length; i++){
        sb.append(tokens[i]); sb.append(" ");
      }
      editorPath = sb.toString().trim().replace("\"", "");
    }
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    switch(action){
      case "set":
        editorPathSettings.store("editor", editorPath);
        break;
      case "get":
        System.out.print(editorPathSettings.list());
        break;
    }
  }
}
