# JUva
A Java-based command-line utility for managing code submissions to [UVA Online Judge](https://uva.onlinejudge.org/). Written in Java, it can run on all platforms.

Credit for the idea belongs entirely to **lucastan** who was the first to implement a similar tool - [UVA NODE](https://github.com/lucastan/uva-node).

###Installation
**Make sure you have JDK/JRE and Ant installed your computer.**

1. Clone [uva-utility](https://github.com/andrey-yemelyanov/uva-utility.git) repo.
2. Navigate to the project folder and run ```ant```. This will build JUva.
3. Navigate to the folder where the code for your UVA problems resides.
3. Launch the application by entering ```java -jar {pathToProject}/build/jar/JUva.jar```.

After launching the application you will be prompted to enter your UVA username and password. Note that the password is stored encrypted, only locally, on your computer. You will need to set up paths to your browser and editor. You might also have to specify proxy server.

Once you initialized JUva with your UVA username/password, JUva will remember those and use them next time you launch the application.

###Features
JUva supports the following commands:

##### ```browser (set {browserPath} | get)```
Configures a browser to be used when e.g. viewing problems or launching discussion board for a specific problem.

##### ```discuss {problemId}```
Launches a browser and loads a discussion board for supplied problem id.

##### ```edit ({problemId} | {filePath})```
Launches editor and loads a code file corresponding to problem id or file path.

##### ```editor (set {editorPath} | get)```
Configures editor to be used when opening problems for editing.

##### ```exit```
Exits JUva.

##### ```help```
Prints a list of available commands and their descriptions.

##### ```path```
Echoes current working directory.

##### ```proxy (set {hostname port} | get | rm)```
Configures proxy server to be used when connecting to UVA.

##### ```stats```
Displays 3 latest UVA submissions for the current user.

##### ```submit ({problemId} | {filePath})```

Submits a code file for specific problem id to UVA.
##### ```tpl (add {tplPath} | show | rm {lang})```
Configures templates for new code files. E.g. 'template.cpp' identifies a template that will be applied when a new C++ file is created.

##### ```udebug {problemId}```
Launches a browser and loads UDebug for supplied problem id.

##### ```use {username}```
Changes current user context (username and password) that will be used when communicating with UVA and UHunt.

##### ```users (add {username} | show | rm {username})```
Manages UVA users. Users are saved as a pair (username->password). Password is stored only on your local machine using AES encryption.

##### ```view {problemId}```
Launches a browser and loads a description for supplied problem id.
