package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;

public class ProxyCommand extends Command {
  private KeyValueStore proxySettings;
  public ProxyCommand(KeyValueStore proxySettings){
    this.proxySettings = proxySettings;
    this.name = "proxy";
    this.usage = name + " (set {hostname port} | get | rm)";
    this.description = "Configures proxy server to be used when connecting to UVA.";
  }

  private String action;
  private String hostname;
  private String port;
  public Command get(String commandText) throws CommandParseException{
    String[] tokens = commandText.split("\\s+");
    if(!tokens[0].equals(name)) return null;
    if(tokens.length < 2) incorrectSyntax();
    if(!tokens[1].equals("add") && !tokens[1].equals("get") && !tokens[1].equals("rm")) incorrectSyntax();
    if(tokens[1].equals("add") && tokens.length < 4) incorrectSyntax();
    action = tokens[1];
    if(tokens[1].equals("add")){
      hostname = tokens[2];
      port = tokens[3];
    }
    return this;
  }

  public void execute(UserContext uc) throws Exception{
    switch(action){
      case "add":
        proxySettings.store("proxy", String.format("%s:%s", hostname, port));
        break;
      case "get":
        System.out.println(proxySettings.list());
        break;
      case "rm":
        proxySettings.remove("proxy");
        break;
    }
  }
}
