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


Then specify the github uri list and the number of version need to parse.
```bash
$ java -jar satdPro-1.0-SNAPSHOT.jar github_list.txt 6
```

A dictionary named `./projects` will be created and the project needed to be parsed will be downloaded into the file `./projects`.
(If the project is too large, it may cause the download to fail. Maybe you should download manually and named the file like GoogleContainerTools-jib.)
SATDdetector will output the results in `./projects/project_name + version_tag.txt`.
