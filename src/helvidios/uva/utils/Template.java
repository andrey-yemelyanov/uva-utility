package helvidios.uva.utils;

import static helvidios.uva.utils.FileUtils.*;

public class Template{
  private KeyValueStore templateSettings;
  public Template(KeyValueStore templateSettings){
      this.templateSettings = templateSettings;
  }

  public String apply(String lang, String problemId) throws Exception{
    if(templateSettings.containsKey(lang) && fileExists(templateSettings.get(lang))){
      String templateText = readFile(templateSettings.get(lang));
      return templateText.replaceAll("##problemId##", problemId);
    }
    return "";
  }
}
