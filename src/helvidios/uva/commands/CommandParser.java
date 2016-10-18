package helvidios.uva.commands;

import helvidios.uva.commands.exceptions.CommandParseException;
import helvidios.uva.utils.*;

public class CommandParser{
  public static final KeyValueStore templateSettings = new TemplatePrefsSettings();
  public static final KeyValueStore editorPathSettings = new EditorPrefsSettings();
  public static final KeyValueStore userSettings = new UserPrefsSettings();
  public static final KeyValueStore browserSettings = new BrowserPrefsSettings();
  public static final KeyValueStore proxySettings = new ProxyPrefsSettings();
  public static final HttpClient httpClient = new HttpClient(proxySettings);
  public static final UhuntFacade uhunt = new UhuntFacade(httpClient);

  private static CommandChainBuilder chainBuilder = new CommandChainBuilder();
  static{
    HelpProvider helpProvider = new HelpProvider(){
      @Override
      public String getHelp(){
        StringBuilder sb = new StringBuilder();
        if(chainBuilder.getRoot() != null)
          chainBuilder.getRoot().buildHelp(sb);
        return sb.toString();
      }
    };
    chainBuilder
      .add(new ExitCommand())
      .add(new TemplateCommand(templateSettings))
      .add(new EditorCommand(editorPathSettings))
      .add(new EditCommand(editorPathSettings, templateSettings, uhunt))
      .add(new UserCommand(userSettings))
      .add(new ChangeUserCommand(userSettings, httpClient))
      .add(new BrowserCommand(browserSettings))
      .add(new ViewProblemCommand(browserSettings))
      .add(new DiscussCommand(browserSettings))
      .add(new UdebugCommand(browserSettings))
      .add(new StatCommand(uhunt))
      .add(new ProxyCommand(proxySettings))
      .add(new SubmitCommand())
      .add(new GetCurrentPathCommand())
      .add(new HelpCommand(helpProvider));
  }

  public static Command parse(String commandText) throws CommandParseException{
    if(chainBuilder.getRoot() != null)
      return chainBuilder.getRoot().parse(commandText);
    return null;
  }
}
