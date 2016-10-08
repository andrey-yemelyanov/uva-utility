package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class UdebugCommand extends Command {
  private KeyValueStore browserSettings;
  public UdebugCommand(KeyValueStore browserSettings){
    this.browserSettings = browserSettings;
    this.name = "udebug";
    this.usage = name + " {problemId}";
    this.description = "Launches a browser and loads UDebug for supplied problem id.";
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
    int id = Integer.parseInt(problemId);
    String url = String.format("https://www.udebug.com/UVa/%s", problemId);
    launchBrowser(browserSettings, url);
  }
}
