# JUva
A Java-based command-line utility for managing code submissions to [UVA Online Judge](https://uva.onlinejudge.org/). Written in Java, it can run on all platforms.

Credit for the idea belongs entirely to **lucastan** who was the first to implement a similar tool - [UVA NODE](https://github.com/lucastan/uva-node).

Installation
--------------------------

**Make sure you have JDK/JRE and Ant installed your computer.**

1. Clone [uva-utility](https://github.com/andrey-yemelyanov/uva-utility.git) repo.
2. Navigate to the project folder and run ```ant```. This will build JUva.
3. Navigate to the folder where the code for your UVA problems resides.
3. Launch the application by entering ```java -jar {pathToProject}/build/jar/JUva.jar```.

After launching the application you will be prompted to enter your UVA username and password. Note that the password is stored encrypted, only locally, on your computer. You will need to set up paths to your browser and editor. You might also have to specify proxy server.

Once you initialized JUva with your UVA username/password, JUva will remember those and use them next time you launch the application.

Features
------------------------------

JUva supports the following commands:
___
```
browser (set {browserPath} | get)
```
Configures a browser to be used when e.g. viewing problems or launching discussion board for a specific problem.
___
```
discuss {problemId}
```
Launches a browser and loads a discussion board for supplied problem id.
___
```
edit ({problemId} | {filePath})
```
Launches editor and loads a code file corresponding to problem id or file path. If the file does not exist, it will be created using a template (if any). Currently JUva allows working with the files that have extension: cpp, cpp11, java, c, py, pas.

**Note that JUva expects that all code files will contain a problem id in their name. E.g. _123_SearchingQuickly.java or 123.cpp. This is how JUva will be able to infer for which problem id you are submitting code to UVA.**
___
```
editor (set {editorPath} | get)
```
Configures editor to be used when opening problems for editing.
___
```
exit
```
Exits JUva.
___
```
help
```
Prints a list of available commands and their descriptions.
___
```
path
```
Echoes current working directory.
___
```
proxy (set {hostname port} | get | rm)
```
Configures proxy server to be used when connecting to UVA.
___
```
stats
```
Displays 3 latest UVA submissions for the current user.
___
```
submit ({problemId} | {filePath})
```
Submits a code file for specific problem id to UVA. If you supply only problem id, JUva will attempt to derive file name. If more than one file match problem id, you will be prompted to supply file name instead.
___
```
tpl (add {tplPath} | show | rm {lang})
```
Configures templates for new code files. E.g. 'template.cpp' identifies a template that will be applied when a new C++ file is created.

Template engine currently supports the following dynamic variables:

* **##problem_id##** - replaced with file name without extension.
* **##problem_url##** - replaced with the URL to the problem pdf file on UVA judge.
* **##problem_name##** - replaced with problem name.
* **##username##** - replaced with name of the user who created the file.

Here is an example Java template I have on my computer under the name JavaTemplate.java

```java
import java.util.*;
import static java.lang.Math.*;
import java.util.stream.*;

/*
Problem name: ##problem_name##
Problem url: ##problem_url##
Author: ##username##
*/
public class ##problem_id## {
  public static void main(String[] args){
    Scanner s = new Scanner(System.in);

  }
}
```

Suppose you I want to create a new Java file with code for problem [11957 - Checkers](https://uva.onlinejudge.org/external/119/11957.pdf). I will start by typing ```edit _11957.java```. JUva will then create a new file with that name and apply the above-mentioned template. Note how file name begins with the underscore character. This is because Java does not allow creating classes whose name begins with a numeric character. The following file content will be created:

```java
import java.util.*;
import static java.lang.Math.*;
import java.util.stream.*;

/*
Problem name: 11957 Checkers
Problem url: https://uva.onlinejudge.org/external/119/11957.pdf
Author: yemelyanov
*/
public class _11957 {
  public static void main(String[] args){
    Scanner s = new Scanner(System.in);

  }
}
```
___
```
udebug {problemId}
```
Launches a browser and loads UDebug for supplied problem id.
___
```
use {username}
```
Changes current user context (username and password) that will be used when communicating with UVA and UHunt.
___
```
users (add {username} | show | rm {username})
```
Manages UVA users. Users are saved as a pair (username->password). Password is stored only on your local machine using AES encryption.
___
```
view {problemId}
```
Launches a browser and loads a description for supplied problem id.
___
