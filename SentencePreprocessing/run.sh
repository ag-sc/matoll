mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.sentence.preprocessing.process.Process" -Dexec.args="config.xml"