package helvidios.uva.commands;

import static helvidios.uva.utils.Utils.*;
import static helvidios.uva.utils.HttpClient.*;
import helvidios.uva.utils.*;
import helvidios.uva.*;
import helvidios.uva.commands.exceptions.*;
import org.json.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class StatCommand extends Command {
  private final int N_SUBS_TO_SHOW = 3;
  private HttpClient httpClient;
  public StatCommand(HttpClient httpClient){
    this.httpClient = httpClient;
    this.name = "stats";
    this.usage = name;
    this.description = String.format("Displays %d latest UVA submissions for current user.", N_SUBS_TO_SHOW);
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
    for(String sub : getLatestSubs(uc)){
      System.out.println(sub);
    }
    System.out.println();
  }

  private List<String> getLatestSubs(UserContext uc) throws Exception {
    String userId = getUserId(uc.getUser().getUsername());
    String url = String.format("http://uhunt.felix-halim.net/api/subs-user-last/%s/%d", userId, N_SUBS_TO_SHOW);
    JSONObject json = new JSONObject(httpClient.doGet(url));
    String username = json.getString("uname");
    JSONArray subs = json.getJSONArray("subs");
    List<String> submissions = new ArrayList<>();
    for (int i = 0; i < subs.length(); i++){
      String problemName = ellipsis(getProblemName(subs.getJSONArray(i).getInt(1)), 25);
      String verdict = v[subs.getJSONArray(i).getInt(2)];
      String runtime = String.format(Locale.US, "%.3f", subs.getJSONArray(i).getInt(3) / 1000.0);
      String submissionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(
        new Date(subs.getJSONArray(i).getLong(4) * 1000));
      String lang = getLangNameById(subs.getJSONArray(i).getInt(5));
      submissions.add(String.format("%-30s%-20s%-10s%-25s%-10s", problemName, verdict, runtime, submissionTime, lang));
    }
    Collections.reverse(submissions);
    return submissions;
  }

  private static String[] v = new String[91];
  static{
    v[0] = "Pending...";
    v[10] = "Submission error";
    v[15] = "Can't be judged";
    v[20] = "In queue";
    v[30] = "Compile error";
    v[35] = "Restricted function";
    v[40] = "Runtime error";
    v[45] = "Output limit";
    v[50] = "Time limit";
    v[60] = "Memory limit";
    v[70] = "Wrong answer";
    v[80] = "PresentationE";
    v[90] = "ACCEPTED";
  }

  private String getUserId(String username) throws Exception{
    String url = String.format("http://uhunt.felix-halim.net/api/uname2uid/%s", username);
    return httpClient.doGet(url);
  }

  private String getProblemName(int problemId) throws Exception{
    String url = String.format("http://uhunt.felix-halim.net/api/p/id/%d", problemId);
    JSONObject problem = new JSONObject(httpClient.doGet(url));
    return problem.getInt("num") + " " + problem.getString("title");
  }
}
