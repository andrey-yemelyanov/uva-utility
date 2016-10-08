package helvidios.uva;

import helvidios.uva.utils.*;

public class UserContext{
  private User user;
  private UvaFacade uvaFacade;
  public void setUser(User user, HttpClient httpClient){
    this.user = user;
    this.uvaFacade = new UvaFacade(httpClient, this.user);
  }
  public User getUser(){
    return user;
  }

  public UvaFacade getUvaFacade(){
    return uvaFacade;
  }

  public UserContext(User user, HttpClient httpClient){
    setUser(user, httpClient);
  }
}
