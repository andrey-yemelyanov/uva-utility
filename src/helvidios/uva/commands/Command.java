package helvidios.uva.commands;
import helvidios.uva.commands.exceptions.*;
import helvidios.uva.UserContext;

public abstract class Command{
  protected Command successor;
  protected String usage;
  protected String name;
  protected String description;
  public void setSuccessor(Command successor){
    this.successor = successor;
  }
  public abstract Command get(String commandText) throws CommandParseException;
  public abstract void execute(UserContext uc) throws Exception;

  protected final void getHelp(StringBuilder sb){
    sb.append(String.format("%s\n%s\n----------", usage, description));
    sb.append("\n");
  }

  public void buildHelp(StringBuilder sb){
    getHelp(sb);
    if(successor != null) successor.buildHelp(sb);
  }

  protected void incorrectSyntax() throws CommandParseException{
    throw new CommandParseException(
      String.format("Incorrect syntax for '%s'. Usage: %s", name, usage)
    );
  }

  public Command parse(String commandText) throws CommandParseException{
    Command command = get(commandText);
    if(command != null) return command;
    else if(successor != null) return successor.parse(commandText);
    return null;
  }
}
