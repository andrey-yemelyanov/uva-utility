package helvidios.uva.utils;

import java.util.*;
import java.util.prefs.*;

public interface KeyValueStore{
  public void store(String key, String value);
  public void remove(String key);
  public String get(String key);
  public String list() throws Exception;
  public boolean containsKey(String key);
}
