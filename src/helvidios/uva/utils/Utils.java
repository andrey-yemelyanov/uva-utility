package helvidios.uva.utils;

import helvidios.uva.commands.exceptions.*;
import java.util.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Console;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Utils{
  private static final Logger logger = Logger.getLogger(Utils.class);

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
    if(!browserSettings.containsKey("browser")) throw new CommandExecutionException(
      "Browser path is not defined. Use set-browser {browserPath}."
    );
    String path = browserSettings.get("browser");
    try{
      launchProcess(path, url);
    }catch(IOException ex){
      logger.error(String.format("Path=%s, url=%s\n%s\n", path, url, getStackTrace(ex)));
      throw new Exception(String.format("Unable to launch browser from path %s and url %s.", path, url));
    }
  }

  public static void launchEditor(KeyValueStore editorPathSettings, String filePath) throws Exception{
    if(!editorPathSettings.containsKey("editor")) throw new CommandExecutionException(
      "Editor path is not defined. Use set-editor {editorPath}."
    );
    String path = editorPathSettings.get("editor");
    try{
      launchProcess(path, filePath);
    }catch(IOException ex){
      logger.error(String.format("Path=%s, filePath=%s\n%s\n", path, filePath, getStackTrace(ex)));
      throw new Exception(String.format("Unable to launch editor from path %s and file path %s.", path, filePath));
    }
  }

  private static void launchProcess(String path, String args) throws IOException {
    System.out.printf("Launching %s %s...\n", path, args);
    Runtime.getRuntime().exec(new String[]{ path, args});
  }

  public static String getProblemUrl(String problemId){
    int id = Integer.parseInt(problemId);
    return String.format("https://uva.onlinejudge.org/external/%d/%d.pdf", id / 100, id);
  }

  public static String getProblemId(String filePath){
    return filePath.replaceAll("\\D+","");
  }
}
