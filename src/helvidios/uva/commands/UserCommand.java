package helvidios.uva.commands;

import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;
import static helvidios.uva.utils.Utils.*;

public class UserCommand extends Command{
  private KeyValueStore userSettings;
  public UserCommand(KeyValueStore userSettings){
    this.userSettings = userSettings;
    this.name = "users";
    this.usage = name + " (add {username} | show | rm {username})";
    this.description = "Manages UVA users. Users are saved as a pair (username->password).\n" +
                        "Password is stored using AES encryption.";
  }

  private String action;
  private String username;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2 || (!tokens[1].equals("add") && !tokens[1].equals("show") && !tokens[1].equals("rm"))) incorrectSyntax();
    if(tokens.length < 3 && (tokens[1].equals("add") || tokens[1].equals("rm"))) incorrectSyntax();
    action = tokens[1];
    if(action.equals("add") || action.equals("rm")) username = tokens[2];
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    switch(action){
      case "add":
        String password = readPassword("UVA password: ");
        String passwordRepeat = readPassword("Repeat UVA password: ");
        if(password == null || password.isEmpty() || !password.equals(passwordRepeat)){
          throw new Exception("Passwords do not match.");
        }
        userSettings.store(username, password);
        break;
      case "show":
        System.out.println(userSettings.list());
        break;
      case "rm":
        userSettings.remove(username);
        String latest = userSettings.get("latest");
        if(latest != null && latest.equals(username)) userSettings.remove("latest");
        break;
    }
  }
}
