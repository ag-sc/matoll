#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train /Users/swalter/Desktop/test_input/ /Users/swalter/Desktop/config.xml"
