package helvidios.uva.utils;

import java.util.*;

public class CookieStore{
  private List<String> cookies;
  public List<String> getCookies(){
    return cookies;
  }
  public void setCookies(List<String> cookies){
    if(cookies == null) clearCookies();
    else this.cookies = cookies;
  }
  public CookieStore(){
    clearCookies();
  }
  public void clearCookies(){
    cookies = new ArrayList<String>();
  }
}
