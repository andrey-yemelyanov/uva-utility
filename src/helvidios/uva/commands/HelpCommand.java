package helvidios.uva.commands;

import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;
import org.apache.log4j.Logger;

public class HelpCommand extends Command{
  private HelpProvider helpProvider;
  private final Logger logger = Logger.getLogger(HelpCommand.class);
  public HelpCommand(HelpProvider helpProvider){
    this.helpProvider = helpProvider;
    this.name = "help";
    this.usage = name;
    this.description = "Prints available commands and their descriptions.";
  }

  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    String help = helpProvider.getHelp();
    logger.info(help);
    System.out.println(help);
  }
}
