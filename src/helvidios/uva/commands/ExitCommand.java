package helvidios.uva.commands;

import helvidios.uva.commands.exceptions.*;
import helvidios.uva.UserContext;

public class ExitCommand extends Command{
  public ExitCommand(){
    this.name = "exit";
    this.usage = name;
    this.description = "Exits UVA utility";
  }

  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    System.exit(0);
  }
}
