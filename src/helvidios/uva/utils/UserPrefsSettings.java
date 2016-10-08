package helvidios.uva.utils;

import java.util.*;
import java.util.prefs.*;
import static helvidios.uva.utils.AES.*;
import static helvidios.uva.utils.Utils.*;

public class UserPrefsSettings extends PrefsSettings{
  private AES aes;
  public UserPrefsSettings() throws RuntimeException {
    prefsContext = "userSettings";
    try{
        aes = new AES();
    }catch(Exception ex){
        throw new RuntimeException(String.format("Unable to init User settings. Cause: %s", ex.getMessage()));
    }
  }

  @Override
  public void store(String key, String value){
    super.store(key, aes.encrypt(value));
  }

  @Override
  public String get(String key){
    return aes.decrypt(super.get(key));
  }

  @Override
  public String list() throws Exception{
    Preferences prefs = super.getPrefs();
    StringBuilder out = new StringBuilder();
    for(String key : prefs.keys()){
      out.append(String.format("%10s => %s\n", key, super.get(key)));
    }
    return out.toString();
  }
}
