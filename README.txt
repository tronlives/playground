BatchImporter - finds Anagrams in text files and displays them in the output console. 
text file shall contain per line only one word like in  this example - 

act
cat
tree
race
care
acre
bee

the file must have the ending .txt. 

The Readme shall come with a complete project setup to be imported in  Intellij. 
It can be directly opened in Intellij and configured for start with Main-Class: com.importer.BatchImporter

Minium required Java Vesion is Java8 - i personally used and runned this application with Java 14 (AdoptOpen JDK). 
Maven version 3.6.3 was used - but also older Maven3 can be used as no specific feature were used. 

Note: this application comes with an embedded database which stores the standard data when executing 
Spring Batch Runs. The H2-database can be accessed via browser per standard via http://localhost:8080/h2-console







