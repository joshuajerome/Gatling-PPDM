# Gatling PPDM Further Documentation
[![](https://img.shields.io/badge/Dell-blue?style=for-the-badge)](https://www.dell.com/en-us)
[![](https://img.shields.io/badge/Maven-red?style=for-the-badge)](https://maven.apache.org/)
[![](https://img.shields.io/badge/Gatling-blueviolet?style=for-the-badge)](https://gatling.io/docs/gatling/)
[![](https://img.shields.io/badge/Jenkins-yellow?style=for-the-badge)](https://www.jenkins.io/doc/)
[![](https://img.shields.io/badge/PPDM-orange?style=for-the-badge)](https://www.dell.com/en-us/dt/data-protection/powerprotect-data-manager.htm#:~:text=%20PowerProtect%20Data%20Manager%20%201%20Orchestrate%20protection,Leverage%20your%20existing%20Dell%20PowerProtect%20appliances%20More%20)
[![](https://img.shields.io/badge/github-blue?style=for-the-badge)](https://github.com/joshuajerome/Gatling-PPDM)

### Table of Contents
- Gatling Project
- [About Gatling PPDM](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#about-gatling-ppdm)
- [Project Stucture](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#project-stucture)
  - [Prerequistites](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#prerequisites)
  - [Installation](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#installation)
  - src/test/java
    - [(default package)](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#default-package)
      - Engine.java
      - Recorder.java
    - [testone](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#testone)
      - Testing.java
      - TestSuite.java
      - CSVReader.java
  - [src/test/resources](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#srctestresources)
    - data.csv
    - postBody.json
    - gatling.conf
    - recorder.conf 
  - [target](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#target)
    - pom.xml
    - run.bat
    - test.eml
    - test.userLibraries
- [Credits](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#credits)

## Gatling Project
Gatling PPDM is a sub-project of Asset Microservice Performance Analysis.

#### What is PPDM?

#### A brief overview of Asset Microservice Performance Analysis:
- Problem Statement:
  - Assess the performance of the APIs hosted by the APSS microservice running on the PPDM server.
- Objectives:
  - Build a performance testing framework for the APSS microservice.
  - Evaluate throughput, latency, workload efficiency, and response times.
- Deliverables:
  - Performance testing framework to simulate users and spin up threads.
  - Automate load testing for different PPDM APIs.
  - Configurations are passed as parameters through a CSV file.

### About Gatling PPDM

### Project Stucture
```
├── Gatling-PPDM
│   └──  src
│       ├── main
|       |   └── java
│       └── test
|           ├── java
|           |   ├── (default package)
|           |   └── testone
|           |       ├── Testing.java
|           |       ├── TestSuite.java
|           |       └── CSVReader.java
|           └── resources
|                   ├── data.csv
|                   ├── postBody.json
|                   ├── gatling.conf
|                   └── recorder.conf
├── target
|   ├── pom.xml
|   ├── run.bat
|   ├── test.eml
|   └── test.userlibraries
├── JRE System Library [JavaSE-1.8]
├── Maven Dependencies
├── README.md
└── .gitignore
```

### Prerequisites
Gatling PPDM includes a command line script (_**run.bat**_) to improve its automation capabilities.
Requirements for this project include an updated _**run.bat**_ script and correctly formatted _**data.csv**_ file (see below).

- _**run.bat**_ script:
    ```
    @mvn gatling:test -Dgatling.simulationClass=packageName.className -Ddatafile="data.csv" -Dusername="username" -Dpassword="password"
    ```
    ```mermaid
    flowchart TD;
        A["@"]-->B["hides print of
        run.bat script"];
        C["mvn gatling:test"]-->D["runs gatling script with 
        maven build tool."];
        E["-DgatlingSimulationClass=packageName.className"]-->F["specifies class to be run"];
        G["-Ddatafile"]-->H["CSV config file"];
        I["-Dusername / -Dpassword"]-->J["API login credentials"];
    ```
- _**data.csv**_ is a configuration file with the following parameters:

    Test Suite #|REST API URI|Port #|HTTP Verb|Request Count|User/Thread Count|Request Bodies (.json)|Test Duration|IP Address
    ---|---|---|---|---|---|---|---|---

    > __Note__ 
    > _Request Bodies are only required for HTTP Verb: POST. Request Bodies provided for other HTTP Verbs will not be used._

    **Example CSV**:
    ```
    1,/some/uri/path,80,GET,10,50,,12.345.67.891
    2,/another/uri/path,443,POST,10,50,postBody.json,12.345.67.891
    ```
> __Warning__ _Configuration files must be placed into **src/test/resources** folder within the project or else they cannot be accessed_

### Installation
1. Clone the repo
```
git clone https://github.com/joshuajerome/Gatling-PPDM.git
```
2. Open a terminal window
3. Navigate into the directory where the cloned repo exists
4. Execute 
      - _**run.bat**_ script (Windows)
      - _**run.sh**_ script (Mac)

## Diving In

### src/test/java

- #### Engine.java

- #### Recorder.java

### (default package)

### testone

- #### Testing.java

- #### TestSuite.java

- #### CSVReader.java

### src/test/resources

- #### data.csv

- #### postBody.json

- #### gatling.conf

- #### recorder.conf

### target

- #### pom.xml

- #### run.bat

- #### test.eml

- #### test.userlibraries

### JRE System Library [JavaSE-1.8]

### Maven Dependencies

### .gitignore

## Credits
This tool was developed by **Yuxin Huang**, **Joshua Jerome**, **Kevin Kodama**, and **Edward Xia** under the supervision of **Hadi Abdo**, **Prabhash Krishnan**, and **Thao Pham**. All rights to this project belong to **Dell Technologies** 

