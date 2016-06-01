#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train path_to_sentences /path_to/config.xml"
