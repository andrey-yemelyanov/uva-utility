package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class ViewProblemCommand extends Command {
  private KeyValueStore browserSettings;
  public ViewProblemCommand(KeyValueStore browserSettings){
    this.browserSettings = browserSettings;
    this.name = "view";
    this.usage = name + " {problemId}";
    this.description = "Launches a browser and loads description for supplied problem id.";
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
    String url = String.format(
      "https://uva.onlinejudge.org/external/%d/%d.pdf", id / 100, id);
    launchBrowser(browserSettings, url);
  }
}
