mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train /Users/swalter/Desktop/Resources/Input/ /Users/swalter/Desktop/config.xml"