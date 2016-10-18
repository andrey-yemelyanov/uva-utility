package helvidios.uva.utils;

import java.util.Map;
import static helvidios.uva.utils.FileUtils.*;

public class Template{
  public enum Variable{
    PROBLEM_ID("problem_id"),
    PROBLEM_URL("problem_url"),
    PROBLEM_NAME("problem_name"),
    USERNAME("username");

    private String varName;
    Variable(String varName){
      this.varName = varName;
    }
    public String getVarName(){
      return String.format("##%s##", varName);
    }
  }
  private KeyValueStore templateSettings;
  public Template(KeyValueStore templateSettings){
      this.templateSettings = templateSettings;
  }

  public String apply(String lang, Map<Variable, String> variables) throws Exception{
    if(templateSettings.containsKey(lang) && fileExists(templateSettings.get(lang))){
      String templateText = readFile(templateSettings.get(lang));
      for(Variable var : variables.keySet()){
        templateText = templateText.replaceAll(var.getVarName(), variables.get(var));
      }
      return templateText;
    }
    return "";
  }
}
