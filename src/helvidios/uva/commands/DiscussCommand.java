package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class DiscussCommand extends Command {
  private KeyValueStore browserSettings;
  public DiscussCommand(KeyValueStore browserSettings){
    this.browserSettings = browserSettings;
    this.name = "discuss";
    this.usage = name + " {problemId}";
    this.description = "Launches a browser and loads discussion board for supplied problem id.";
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
    String url = String.format(
      "https://uva.onlinejudge.org/board/search.php?keywords=%s", problemId);
    launchBrowser(browserSettings, url);
  }
}
