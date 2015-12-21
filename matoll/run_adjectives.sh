#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.LabelApproach.Process" 