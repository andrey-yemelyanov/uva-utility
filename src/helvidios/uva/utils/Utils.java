package helvidios.uva.utils;

import helvidios.uva.commands.exceptions.*;
import java.util.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Console;
import org.apache.log4j.Logger;

public class Utils{
  private static final Map<String, Integer> supportedLangs = new HashMap<String, Integer>();
  static{
    supportedLangs.put("c", 1);
    supportedLangs.put("java", 2);
    supportedLangs.put("cpp", 3);
    supportedLangs.put("pas", 4);
    supportedLangs.put("cpp11", 5);
    supportedLangs.put("py", 6);
  }

  public static Set<String> supportedLangs(){
    return new HashSet<String>(supportedLangs.keySet());
  }

  public static boolean isSupportedLang(String lang){
    return supportedLangs.containsKey(lang);
  }

  public static int getLangIdByName(String lang){
    return supportedLangs.get(lang);
  }

  public static String getLangNameById(int langId) throws Exception {
    for(Map.Entry<String, Integer> entry : supportedLangs.entrySet()){
      if(entry.getValue() == langId) return entry.getKey();
    }
    throw new Exception(
      String.format("Unknown language id %d.", langId)
    );
  }

  public static String ellipsis(final String text, int length){
    if(text.length() <= length) return text;
    return text.substring(0, length - 3) + "...";
  }

  public static String getStackTrace(Exception ex){
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  public static String readPassword(String prompt){
    Console console = System.console();
    char passwordArray[] = console.readPassword(prompt);
    return new String(passwordArray);
  }

  public static void launchBrowser(KeyValueStore browserSettings, String url) throws Exception{
    String browserPath = browserSettings.get("browser");
    if(!browserSettings.containsKey("browser")) throw new CommandExecutionException(
      "Browser path is not defined. Use set-browser {browserPath}."
    );
    System.out.printf("Launching browser %s %s...\n", browserPath, url);
    Runtime.getRuntime().exec(new String[]{browserPath, url});
  }

  public static void launchEditor(KeyValueStore editorPathSettings, String filePath) throws Exception{
    String editorPath = editorPathSettings.get("editor");
    if(editorPath == null || editorPath.isEmpty()) throw new CommandExecutionException(
      "Editor path is not defined. Use set-editor {editorPath}."
    );
    System.out.printf("Launching editor %s %s...\n", editorPath, filePath);
    Runtime.getRuntime().exec(new String[]{editorPath, filePath});
  }
}
