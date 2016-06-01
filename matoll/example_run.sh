#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train /Users/swalter/Downloads/tmp_extractPropertiesWithData/results/ontologySentences_EN/dbpedia/ontology/en/test/ /Users/swalter/Desktop/config.xml"
