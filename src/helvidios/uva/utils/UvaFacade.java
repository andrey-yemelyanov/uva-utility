package helvidios.uva.utils;

import static helvidios.uva.utils.Utils.*;
import helvidios.uva.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URLEncoder;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.log4j.Logger;

public class UvaFacade{
  private final Logger logger = Logger.getLogger(UvaFacade.class);

  private final String host = "https://uva.onlinejudge.org";
  private final String loginUrl = host + "/index.php?option=com_comprofiler&task=login";
  private final String submitUrl = host + "/index.php?option=com_onlinejudge&Itemid=25";
  private final String postProblemUrl = host + "/index.php?option=com_onlinejudge&Itemid=25&page=save_submission";
  private final String separator = "----WebKitFormBoundaryolYTb1xsZ6D0BU3A";

  private CookieStore cookieStore;
  private HttpClient httpClient;
  private Map<String, String> headers;
  private User user;

  private void resetHeaders(){
    headers = new HashMap<String, String>();
    headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    headers.put("Accept-Language", "sv-SE,sv;q=0.8,en-US;q=0.6,en;q=0.4,ru;q=0.2");
    headers.put("Cache-Control", "no-cache");
    headers.put("Connection", "keep-alive");
    headers.put("Pragma", "no-cache");
    headers.put("User-Agent", "Mozilla/5.0");
    headers.put("Referer", host);
    headers.put("Content-Type", "application/x-www-form-urlencoded");
  }

  public UvaFacade(HttpClient httpClient, User user){
    cookieStore = new CookieStore();
    this.httpClient = httpClient;
    this.user = user;
    resetHeaders();
  }

  public void submitProblem(String problemId, String lang, String content) throws Exception{
    logger.info(String.format("User '%s' submits code for problemId=%s, lang=%s...\n%s", user.getUsername(), problemId, lang, content));
    login();
    String postParams = getPostParams(problemId, lang, content);
    headers.put("Content-Type", "multipart/form-data; boundary=" + separator);
    headers.put("Referer", submitUrl);
    Map<String,List<String>> responseHeaders = httpClient.doPost(postProblemUrl, postParams, headers, cookieStore);
    if(!responseHeaders.containsKey("Location")) throw new Exception(
      String.format("Unable to submit problem %s. 'Location' header is not present in response from the UVA server.", problemId)
    );
    //System.out.println(responseHeaders.get("Location"));
    String statusMsg = getStatusMsg(responseHeaders.get("Location").get(0));
    if(statusMsg == null) throw new Exception(
      String.format("Unable to extract status message from submission. 'Location' header: %s.", responseHeaders.get("Location").toString())
    );
    System.out.println(statusMsg);
  }

  private String getStatusMsg(String locationUrl) throws Exception {
    int i = locationUrl.indexOf("mosmsg");
    if(i < 0) return null;
    locationUrl = locationUrl.substring(i);
    return URLDecoder.decode(locationUrl.split("=")[1], "UTF-8");
  }

  private String getPostParams(String problemId, String lang, String code) throws Exception{
    StringBuilder sb = new StringBuilder();
    appendContent(sb, "problemid", "");
    appendContent(sb, "category", "");
    appendContent(sb, "localid", problemId);
    appendContent(sb, "language", Integer.toString(getLangIdByName(lang)));
    appendContent(sb, "code", code);
    appendFileContent(sb, "codeupl");
    sb.append("--" + separator + "--\n");
    return sb.toString();
  }

  private void appendFileContent(StringBuilder sb, String name){
    sb.append("--" + separator + "\n");
    sb.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"\"\n", name));
    sb.append("Content-Type: application/octet-stream\n");
    sb.append("\n\n");
  }

  private void appendContent(StringBuilder sb, String name, String content){
    sb.append("--" + separator + "\n");
    sb.append(String.format("Content-Disposition: form-data; name=\"%s\"\n", name));
    sb.append("\n");
    sb.append(content);
    sb.append("\n");
  }

  private boolean isLoggedIn() throws Exception {
    String submitPage = httpClient.doGet(submitUrl, headers, cookieStore);
    Document doc = Jsoup.parse(submitPage);
    Elements codeUplInput = doc.select("input[name='codeupl']");
    return codeUplInput != null && codeUplInput.size() > 0;
  }

  public void login() throws Exception{
    System.out.printf("Logging in user %s to UVA...\n", user.getUsername());
    resetHeaders();
    cookieStore.clearCookies();
    String loginPage = httpClient.doGet(host, headers, cookieStore);
    String loginParams = getLoginFormParams(loginPage);
    httpClient.doPost(loginUrl, loginParams, headers, cookieStore);
    if(!isLoggedIn()) throw new Exception(
      String.format("Invalid login for user '%s' on UVA. Make sure UVA username and password are valid.", user.getUsername())
    );
    System.out.println("Login successful!");
  }

  private String getLoginFormParams(String html) throws Exception{
    Document doc = Jsoup.parse(html);
    Element loginForm = doc.getElementById("mod_loginform");
    Elements inputElements = loginForm.getElementsByTag("input");
    List<String> paramList = new ArrayList<String>();
    for (Element inputElement : inputElements) {
		  String key = inputElement.attr("name");
		  String value = inputElement.attr("value");
	    if (key.equals("username"))
			  value = user.getUsername();
		  else if (key.equals("passwd"))
			  value = user.getPassword();
		  paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
	  }

    StringBuilder sb = new StringBuilder();
    for(String param : paramList){
      if(sb.length() == 0) sb.append(param);
      else sb.append("&" + param);
    }

    return sb.toString();
  }
}
