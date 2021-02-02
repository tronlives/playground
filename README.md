## Elaboration of scaling and performance problematic of microservices by finding anagrams

FileImporter

FileImporter is based on Spring Integration and Spring Batch 

you can start it in two ways:
please import the project into your intellij IDE

Minimum JDK version to be used - 1.8 (i used JDK 14 AdoptOpen)

Start configuration:
Main Class - com.importer.BatchImporter
JRE: select JDK 1.8 or higher (i choosed JDK 14 Adoptopen)
Use Class Main Module: FileImportProcessor
used heap space maxmium of 700 MB - sufficient also for very lage files
maven version:  any maven 3.x can be used (used 3.6.3)

Alternative:
use command "mvnw.cmd clean install" (Windows) or "./mvnw clean install" and then start 
application with "mvnw spring-boot:run"

