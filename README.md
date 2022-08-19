# Gatling PPDM
[![](https://img.shields.io/badge/Dell-blue?style=for-the-badge)](https://www.dell.com/en-us)
[![](https://img.shields.io/badge/Maven-red?style=for-the-badge)](https://maven.apache.org/)
[![](https://img.shields.io/badge/Gatling-blueviolet?style=for-the-badge)](https://gatling.io/docs/gatling/)
[![](https://img.shields.io/badge/Jenkins-yellow?style=for-the-badge)](https://www.jenkins.io/doc/)
[![](https://img.shields.io/badge/PPDM-orange?style=for-the-badge)](https://www.dell.com/en-us/dt/data-protection/powerprotect-data-manager.htm#:~:text=%20PowerProtect%20Data%20Manager%20%201%20Orchestrate%20protection,Leverage%20your%20existing%20Dell%20PowerProtect%20appliances%20More%20)
[![](https://img.shields.io/badge/github-blue?style=for-the-badge)](https://github.com/joshuajerome/Gatling-PPDM)

Gatling PPDM (Power Protect Data Manager) is a Maven Gatling project that assess the performance of different APIs hosted by the APSS microservice running on the PPDM server.

For a more detailed description of this project, see [further documentation](https://github.com/joshuajerome/Gatling-PPDM/blob/master/READMORE.md#gatling-ppdm).

## Getting Started
This project does not require any external dependency download. All dependencies can be found within **[pom.xml](https://github.com/joshuajerome/Gatling-PPDM/blob/master/pom.xml)**.

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
    
    Gatling PPDM project includes two **csv** files (_**data.csv**_ and _**test.csv**_). 

    - _**data.csv**_ contains 7 test suites; 6 **GET** and 1 **POST**. Used for performance testing APIs hosted by APSS microservice. 
    - _**test.csv**_ contains 3 test suites; 1 **GET** and 2 **POST**. Can be used like **data.csv**, but is primarily used for testing Gatling PPDM code instead. 
    
- _**data.csv**_ is a configuration file with the following parameters:

    Test Suite #|REST API URI|Port #|HTTP Verb|Request Count|User/Thread Count|Request Body(s) (.json)|Test Duration|IP Address
    ---|---|---|---|---|---|---|---|---

    > __Note__ 
    > _Request Body(s) is only required for HTTP Verb: POST. Request Body(s) provided for other HTTP Verbs will not be used._

    **Request Body Parameters:**

    - Given a single (1) request body parameter, Gatling PPDM will update and reuse the same **POST** body for all specfied **POST** requests.
    - For multiple (2) request body paramters, Gatling PPDM will make an initial **POST** using the first request body parameter. Then, will use certain fields from the returned **response body** as a host for the subsequent request body parameter. Gatling PPDM will continue to update and use the second **POST** body for all specified **POST** requests.   
    
    **Example CSV**:
    ```
    1,/some/uri/path,80,GET,10,50,,12.345.67.891
    2,/another/uri/path,443,POST,10,50,appHost.json,12.345.67.891
    3,/oneMore/uri/path,443,POST,10,50,appHost.json/appSys.json,12.345.67.891
    ```
> __Warning__ _Configuration files must be placed into **src/test/resources** folder within the project or else they cannot be accessed_

### Installation
1. Clone the repo
```
git clone https://github.com/joshuajerome/Gatling-PPDM.git
```
2. Open a terminal window
3. Navigate into the directory where the cloned repo exists
4. Execute _**run.bat**_ script

### Results and Debugging

Once _**run.bat**_ finished running, a file location will be printed on the same terminal window. This same file location can be found on the path ```/target/gatling/testing-{time stamp}```. Furthermore, after the test is complete, an **error.log** file is created. **error.log** is the result of 
**logback-test.xml** whose root level is set to _**debug**_. Due to the large size of **error.log**, this file is ignored in **.gitignore**, and will only be available after a test has been run.  

Again, due to the vast size of **error.log**, the file is overwritten every run. To store the **error.log** of several runs, set the _append_ status to true in **logback-test.xml**.

```xml
<append>true</append>
```
See **[logback.xml](https://github.com/joshuajerome/Gatling-PPDM/blob/master/src/test/resources/logback-test.xml)**.

## Credits
This tool was developed by **Yuxin Huang**, **Joshua Jerome**, **Kevin Kodama**, and **Edward Xia** under the supervision of **Hadi Abdo**, **Prabhash Krishnan**, and **Thao Pham**. All rights to this project belong to **Dell Technologies**

