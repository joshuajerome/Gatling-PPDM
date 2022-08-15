# Gatling PPDM
[![](https://img.shields.io/badge/Dell-blue?style=for-the-badge)](https://www.dell.com/en-us)
[![](https://img.shields.io/badge/Maven-red?style=for-the-badge)](https://maven.apache.org/)
[![](https://img.shields.io/badge/Gatling-blueviolet?style=for-the-badge)](https://gatling.io/docs/gatling/)
[![](https://img.shields.io/badge/Jenkins-yellow?style=for-the-badge)](https://www.jenkins.io/doc/)
[![](https://img.shields.io/badge/PPDM-orange?style=for-the-badge)](https://www.dell.com/en-us/dt/data-protection/powerprotect-data-manager.htm#:~:text=%20PowerProtect%20Data%20Manager%20%201%20Orchestrate%20protection,Leverage%20your%20existing%20Dell%20PowerProtect%20appliances%20More%20)
[![](https://img.shields.io/badge/github-blue?style=for-the-badge)](https://github.com/joshuajerome/Gatling-PPDM)

Gatling PPDM (Power Protect Data Manager) is a Maven Gatling project that assess the performance of different APIs hosted by the APSS microservice running on the PPDM server.

## Getting Started
This project does not require any external dependency download. All dependencies can be found within _**pom.xml**_.

### Project Stucture
```bash
├── Gatling-PPDM
│   ├── src
│   │   ├── main
│   ├── favicon.ico
│   ├── images
│   ├── index.html
│   ├── js
│   │   ├── **/*.js
│   └── partials/template
├── dist (or build)
├── node_modules
├── bower_components (if using bower)
├── test
├── Gruntfile.js/gulpfile.js
├── README.md
├── package.json
├── bower.json (if using bower)
└── .gitignore
```

### Prerequisites
Gatling PPDM includes a command line script (_**run.bat**_) to improve its automation capabilities.
Requirements for this project include an updated _**run.bat**_ script and correctly formatted _**datafile.csv**_ file (see below).

- _**run.bat**_ script:
    ```
    @mvn gatling:test -Dgatling.simulationClass=packageName.className -Ddatafile="datafile.csv" -Dusername="username" -Dpassword="password"
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
- _**datafile.csv**_ is a configuration file with the following parameters:

    Test Suite #|REST API URI|Port #|HTTP Verb|Request Count|User/Thread Count|Request Bodies (.json)|Test Duration|IP Address
    ---|---|---|---|---|---|---|---|---

    _Request Bodies are only required for HTTP Verb: POST_

    **Example CSV**:
    ```
    1,/some/uri/path,80,GET,10,50,,12.345.67.891
    2,/another/uri/path,443,POST,10,50,postBody.json,12.345.67.891
    ```
Configuration files must be placed into **src/test/resources** folder within the project

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



<details>
<summary>Further Documentation</summary>
<br>
put futher documentation here
</br>
</details>


## Credits
This tool was developed by **Yuxin Huang**, **Joshua Jerome**, **Kevin Kodama**, and **Edward Xia** under the supervision of **Hadi Abdo**, **Prabhash Krishnan**, and **Thao Pham**. All rights to this project belong to **Dell Technologies** 

