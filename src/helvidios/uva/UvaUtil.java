package helvidios.uva;

import java.util.*;
import helvidios.uva.utils.*;
import helvidios.uva.commands.*;
import static helvidios.uva.utils.Utils.*;
import org.apache.log4j.Logger;

public class UvaUtil{
  private static final Logger logger = Logger.getLogger(UvaUtil.class);
  static UserContext getUserContext(Scanner s) throws Exception {
    KeyValueStore userSettings = CommandParser.userSettings;
    HttpClient httpClient = CommandParser.httpClient;
    if(userSettings.containsKey("latest")){
      return new UserContext(
        new User(userSettings.get("latest"), userSettings.get(userSettings.get("latest"))),
          httpClient);
    }

    System.out.print("UVA username: "); String username = s.nextLine().trim();
    if(username == null || username.isEmpty()){
      throw new Exception("Username is empty. Try again.");
    }

    String password;
    if(userSettings.containsKey(username)){
      password = userSettings.get(username);
    }else{
      password = readPassword("UVA password: ");
      String passwordRepeat = readPassword("Repeat UVA password: ");
      if(password == null || password.isEmpty() || !password.equals(passwordRepeat)){
        throw new Exception("Passwords do not match. Try again.");
      }
      userSettings.store(username, password);
    }
    userSettings.store("latest", username);
    return new UserContext(new User(username, password), httpClient);
  }

  public static void main(String[] args){
    System.out.println("Welcome to UVA utility!");
    System.out.println("'help' - list of available commands");
    System.out.println("'exit' - quit the program");
    UserContext uc = null;
    Scanner s = new Scanner(System.in);
    while(true){
      try{
        if(uc == null){
          uc = getUserContext(s);
        }else{
          System.out.printf("%s@uva> ", uc.getUser().getUsername());
          String input = s.nextLine().trim();
          if(input.isEmpty()) continue;
          Command command = CommandParser.parse(input);
          if(command == null) throw new Exception(
            String.format("Unknown command '%s'", input));
          command.execute(uc);
        }
      }catch(Exception ex){
        logger.error("Error: " + Utils.getStackTrace(ex));
        System.out.println("Error: " + ex.getMessage());
      }
    }
  }
}
