package helvidios.uva.commands;

public class CommandChainBuilder{
  private Command root;
  private Command last;
  public CommandChainBuilder add(Command command){
    if(root == null) {
      root = command;
      last = root;
    }
    else{
      last.setSuccessor(command);
      last = command;
    }
    return this;
  }

  public Command getRoot(){
      return root;
  }
}
