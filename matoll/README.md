## Running 

You can run M-ATOLL with the following command:

mvn clean && mvn install

mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train PathToSentences config.xml"


