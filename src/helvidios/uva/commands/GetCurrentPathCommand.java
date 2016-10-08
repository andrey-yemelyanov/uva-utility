package helvidios.uva.commands;

import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class GetCurrentPathCommand extends Command{
  public GetCurrentPathCommand(){
    this.name = "path";
    this.usage = name;
    this.description = "Echoes current working directory.";
  }

  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    System.out.println("Working Directory = " + System.getProperty("user.dir"));
  }
}
