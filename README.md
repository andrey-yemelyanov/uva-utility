# uva-utility
Command-line application utility for managing code submissions to UVA Online Judge.

UvaUtility supports following commands:

# exit
Exits UVA utility
----------
tpl (add {tplPath} | show | rm {lang})
Configures templates for new code files. E.g. 'template.cpp' identifies a
template that will be applied when a new C++ file is created.
----------
editor (set {editorPath} | get)
Configures editor to be used when opening problems for editing.
----------
edit ({problemId} | {filePath})
Launches editor and loads a code file corresponding to problem id or file path.
----------
users (add {username} | show | rm {username})
Manages UVA users. Users are saved as a pair (username->password).
Password is stored using AES encryption.
----------
use {username}
Changes current user context (username and password) that will be
used when communicating with UVA and UHunt.
----------
browser (set {browserPath} | get)
Configures browser to be used when e.g. viewing problems or launching
discussion board for a specific problem.
----------
view {problemId}
Launches a browser and loads description for supplied problem id.
----------
discuss {problemId}
Launches a browser and loads discussion board for supplied problem id.
----------
udebug {problemId}
Launches a browser and loads UDebug for supplied problem id.
----------
stats
Displays 3 latest UVA submissions for current user.
----------
proxy (set {hostname port} | get | rm)
Configures proxy server to be used when connecting to UVA.
----------
submit ({problemId} | {filePath})
Submits a code file for specific problem id to UVA.
----------
path
Echoes current working directory.
----------
help
Prints available commands and their descriptions.
----------
