package helvidios.uva.utils;

import java.util.*;
import java.util.prefs.*;

public abstract class PrefsSettings implements KeyValueStore{
  protected String prefsContext;

  protected Preferences getPrefs(){
    return Preferences.userNodeForPackage(
      helvidios.uva.commands.Command.class).node(prefsContext);
  }

  @Override
  public boolean containsKey(String key){
    String value = get(key);
    return value != null && !value.isEmpty();
  }

  @Override
  public void store(String key, String value){
    getPrefs().put(key, value);
  }

  @Override
  public void remove(String key){
    getPrefs().remove(key);
  }

  @Override
  public String get(String key){
    return getPrefs().get(key, "");
  }

  @Override
  public String list() throws Exception{
    Preferences prefs = getPrefs();
    StringBuilder out = new StringBuilder();
    for(String key : prefs.keys()){
      out.append(String.format("%10s => %s\n", key, prefs.get(key, "")));
    }
    return out.toString();
  }
}
