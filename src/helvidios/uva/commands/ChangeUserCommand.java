package helvidios.uva.commands;

import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class ChangeUserCommand extends Command{
  private KeyValueStore userSettings;
  private HttpClient httpClient;
  public ChangeUserCommand(KeyValueStore userSettings, HttpClient httpClient){
    this.userSettings = userSettings;
    this.httpClient = httpClient;
    this.name = "use";
    this.usage = name + " {username}";
    this.description = "Changes current user context (username and password) that will be \n" +
                        "used when communicating with UVA and UHunt.";
  }

  private String username;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2) incorrectSyntax();
    username = tokens[1];
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    String user = userSettings.get(username);
    if(user == null || user.isEmpty()) throw new Exception(
      String.format("User %s does not exist. Use 'users add {username}' to add new user.", username)
    );
    userSettings.store("latest", username);
    uc.setUser(new User(username, userSettings.get(username)), httpClient);
  }
}
