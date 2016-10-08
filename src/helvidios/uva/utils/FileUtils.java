package helvidios.uva.utils;

import java.util.*;
import java.io.*;

public class FileUtils{

  public static String getFileExtension(String path) throws Exception{
    int i = path.lastIndexOf('.');
    if(i < 0) throw new Exception("Unable to extract extension from " + path);
    return path.substring(i + 1);
  }

  public static boolean hasFileExtension(String file){
    return file.lastIndexOf('.') >= 0;
  }

  public static String getFileName(String file){
    return file.split("\\.")[0];
  }

  public static boolean fileExists(String file){
    File f = new File(file);
    return f.exists() && !f.isDirectory();
  }

  public static void writeFile(String filePath, String contents) throws IOException{
    PrintWriter writer = null;
    try{
      writer = new PrintWriter(filePath, "UTF-8");
      writer.println(contents);
    }finally{
        if(writer != null) writer.close();
    }
  }

  public static String readFile(String filePath) throws IOException{
    BufferedReader reader = null;
    try{
      reader = new BufferedReader(new FileReader(filePath));
      StringBuilder sb = new StringBuilder();
      String line;
      while((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
      }
      return sb.toString();
    }finally{
      if(reader != null) reader.close();
    }
  }

  public static List<String> listFiles(String fileName, Set<String> allowedExts) throws Exception {
    List<String> files = listFiles(fileName);
    List<String> filtered = new ArrayList<>();
    for(String file : files){
      if(allowedExts.contains(getFileExtension(file))){
        filtered.add(file);
      }
    }
    return filtered;
  }

  public static List<String> listFiles(String fileName){
    List<String> files = new ArrayList<>();
    for (File file : new File(".").listFiles()) {
      if (file.isFile() && getFileName(file.getName()).contains(fileName)) {
          files.add(file.getName());
      }
    }
    return files;
  }
}
