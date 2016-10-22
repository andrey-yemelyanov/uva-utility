package helvidios.uva.utils;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.*;
import java.util.*;
import org.json.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class UhuntFacade{
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

  private final String baseUrl = "http://uhunt.felix-halim.net/api";
  public final int N_SUBS_TO_SHOW = 3;

  private HttpClient httpClient;
  public UhuntFacade(HttpClient httpClient){
    this.httpClient = httpClient;
  }

  public String getUserId(String username) throws Exception {
    String url = String.format("%s/uname2uid/%s", baseUrl, username);
    return httpClient.doGet(url);
  }

  public String getProblemName(String problemId) throws Exception{
    String url = String.format("%s/p/num/%s", baseUrl, problemId);
    JSONObject problem = new JSONObject(httpClient.doGet(url));
    return problem.getInt("num") + " " + problem.getString("title");
  }

  private String getProblemNameById(int id) throws Exception{
    String url = String.format("%s/p/id/%d", baseUrl, id);
    JSONObject problem = new JSONObject(httpClient.doGet(url));
    return problem.getInt("num") + " " + problem.getString("title");
  }

  public String getFullName(String username) throws Exception {
    String userId = getUserId(username);
    String url = String.format("%s/subs-user-last/%s/0", baseUrl, userId);
    JSONObject json = new JSONObject(httpClient.doGet(url));
    return json.getString("name");
  }

  public List<String> getLatestSubs(String username) throws Exception {
    String userId = getUserId(username);
    String url = String.format("%s/subs-user-last/%s/%d", baseUrl, userId, N_SUBS_TO_SHOW);
    JSONObject json = new JSONObject(httpClient.doGet(url));
    JSONArray subs = json.getJSONArray("subs");
    List<String> submissions = new ArrayList<>();
    for (int i = 0; i < subs.length(); i++){
      String problemName = ellipsis(getProblemNameById(subs.getJSONArray(i).getInt(1)), 25);
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
}
