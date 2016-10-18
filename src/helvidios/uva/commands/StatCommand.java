package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class StatCommand extends Command {
  private UhuntFacade uhunt;
  public StatCommand(UhuntFacade uhunt){
    this.uhunt = uhunt;
    this.name = "stats";
    this.usage = name;
    this.description = String.format("Displays %d latest UVA submissions for current user.", uhunt.N_SUBS_TO_SHOW);
  }

  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    System.out.println();
    System.out.printf("Latest submissions for %s:\n", uc.getUser().getUsername().toUpperCase());
    System.out.printf(String.format("%-30s%-20s%-10s%-25s%-10s\n", "PROBLEM", "VERDICT", "RUNTIME", "DATE", "LANGUAGE"));
    System.out.println("-----------------------------------------------------------------------------------------------");
    for(String sub : uhunt.getLatestSubs(uc.getUser().getUsername())){
      System.out.println(sub);
    }
    System.out.println();
  }
}
