# mc2-toolset
The purpose of the mc2 toolset is to provide the simplest environment as possible for configuring and accessing services, without any development or deployment effort by the scientific application developer. This is composed of three tools: the mc2 gateway engine, the mc2 cli and the mc2 rest.

There are manuals available for each tool that can be foun in: [manuals](https://github.com/bastosbf/mc2-toolset/tree/master/manuals).

mc2 gateway engine
--------------
The module responsible for building dynamic user interfaces based on the settings defined by its configuration files, as well as providing additional features of file sharing and restricted anonymous access.

REQUIREMENTS: Java 8, Tomcat 7

mc2 cli
--------------
A command-line user interface for executing and monitoring jobs and also for data management. This tool does not provide features such as file sharing or restricted anonymous access, but it is useful for scripting large scale bag-of-tasks executions. 

REQUIREMENTS: Java 8

mc2 rest
--------------
A programming-language independent application interface for accessing the mc2 services programmatically. This may be used by science gateways not developed in Java or by other tools interested in accessing the underlying infrastructure in a middleware-independent way.

REQUIREMENTS: Java 8, Tomcat 7

*Notice that access the PMES/BES/COMPSS address from some machines you need to add the web service address security certificate do the Java security folder.  An executable jar file called install-cert.jar is available in [utils](https://github.com/bastosbf/mc2-toolset/tree/master/utils) folder and will generate a jssecacert file that must be copied to JAVA_HOME/jre/lib/security folder manually.*

About the project
---------------
This project is funded by the HPC Network of the Brazilian Ministry of Science, Technology and Innovation ([SINAPAD](http://www.lncc.br/sinapad)). It also receives support from [EUBrazil Cloud Connect](http://www.eubrazilcloudconnect.eu), a project funded under the second EU-Brazil coordinated call.
