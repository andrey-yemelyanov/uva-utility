package helvidios.uva.commands;

import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class BrowserCommand extends Command{
  private String browserPath;
  private KeyValueStore browserSettings;
  public BrowserCommand(KeyValueStore browserSettings){
    this.browserSettings = browserSettings;
    this.name = "browser";
    this.usage = name + " (set {browserPath} | get)";
    this.description = "Configures browser to be used when e.g. viewing problems or launching \n" +
                        "discussion board for a specific problem.";
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
      browserPath = sb.toString().trim().replace("\"", "");
    }
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    switch(action){
      case "set":
        browserSettings.store("browser", browserPath);
        break;
      case "get":
        System.out.print(browserSettings.list());
        break;
    }
  }
}
