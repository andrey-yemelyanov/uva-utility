package helvidios.uva.utils;

import java.net.*;
import java.io.*;
import java.util.*;

public class HttpClient{

  private KeyValueStore proxySettings;
  public HttpClient(KeyValueStore proxySettings){
    this.proxySettings = proxySettings;
  }

  private void checkProxy() throws Exception{
    // set proxy if configured in proxySettings
    if(proxySettings.containsKey("proxy")){
      String proxyUrl = proxySettings.get("proxy");
      //System.out.printf("Using proxy '%s'\n", proxyUrl);
      String[] tokens = proxyUrl.split(":");
      if(tokens.length < 2) throw new Exception(
        String.format("Malformed proxy url '%s'. Expected format: hostname:port", proxyUrl)
      );
      if(!tokens[1].matches("\\d+")) throw new Exception(
        String.format("Proxy port number '%s' is not numeric.", tokens[1])
      );
      final String hostname = tokens[0]; final int port = Integer.parseInt(tokens[1]);
      ProxySelector.setDefault(new ProxySelector() {
        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            throw new RuntimeException("Proxy connect failed", ioe);
        }
        @Override
        public List<Proxy> select(URI uri) {
          return Arrays.asList(
              new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port)));
        }
      });
    }
  }

  private HttpURLConnection getHttpUrlConnection(String url, Map<String, String> headers, CookieStore cookieStore) throws Exception{
    HttpURLConnection conn = (HttpURLConnection) (new URL(url).openConnection());
    conn.setUseCaches(false);

    // add headers (if any)
    for(Map.Entry<String, String> header : headers.entrySet()){
      conn.setRequestProperty(header.getKey(), header.getValue());
    }

    // add cookies (if any)
    for (String cookie : cookieStore.getCookies()) {
      conn.addRequestProperty("Cookie", cookie);
    }

    return conn;
  }

  public void doPost(String url, String postParams) throws Exception{
    doPost(url, postParams, new HashMap<String, String>(), new CookieStore());
  }

  public Map<String,List<String>> doPost(String url, String postParams, Map<String, String> headers, CookieStore cookieStore) throws Exception{
    checkProxy();
    HttpURLConnection conn = getHttpUrlConnection(url, headers, cookieStore);
    conn.setDoOutput(true);
	  conn.setDoInput(true);
    conn.setInstanceFollowRedirects(false);
    conn.setRequestMethod("POST");
	  conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

    try(DataOutputStream out = new DataOutputStream(conn.getOutputStream())){
      out.writeBytes(postParams);
      out.flush();
    }

    // store response cookies
    cookieStore.setCookies(conn.getHeaderFields().get("Set-Cookie"));

    return conn.getHeaderFields();
  }

  public String doGet(String url) throws Exception{
    return doGet(url, new HashMap<String, String>(), new CookieStore());
  }

  public String doGet(String url, Map<String, String> headers, CookieStore cookieStore) throws Exception{
    BufferedReader in = null;
    try{
      checkProxy();
      HttpURLConnection conn = getHttpUrlConnection(url, headers, cookieStore);
      int responseCode = conn.getResponseCode();
      if(responseCode != HttpURLConnection.HTTP_OK) throw new Exception(
        String.format("Server returned HTTP status code: %d", responseCode)
      );
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String inputLine;
  		StringBuffer response = new StringBuffer();
  		while ((inputLine = in.readLine()) != null) {
  			response.append(inputLine);
  		}
      return response.toString();
    }finally{
      if(in != null) in.close();
    }
  }
}
