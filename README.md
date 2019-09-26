# SATDdetector
## Introduction
SATD refers to self-admitted technical debt, which is introduced intentionally (e.g., through temporary fix) and admitted by developers themselves and always recorded in source code comments. SATDDetector is based on [SATDDetector-Core](https://github.com/Tbabm/SATDDetector-Core)

### How to use the SATDdetector ?
Download binary: [SATDdetector.jar](https://github.com/ThJoker/SATDdetector/releases/download/v1.0/satdPro-1.0-SNAPSHOT.jar)

Create a file containing the links to the github projects you need to parse. 
github_list.txt:
```bash
https://github.com/Tbabm/SATDDetector-Core.git
https://github.com/GoogleContainerTools/jib.git
```


```bash
$ java -jar satdPro-1.0-SNAPSHOT.jar github_list.txt
```

A dictionary named `./projects` will be created and the project needed to be parsed will be downloaded into the file `./projects`.
SATDdetector will output the results in `./projects`(e.g. `./projects/project_name.txt`).
