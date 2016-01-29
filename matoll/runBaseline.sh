#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll_Baseline" -Dexec.args="--mode=train /Users/swalter/Downloads/tmp_extractPropertiesWithData/results/ontologySentences_EN/dbpedia/ontology/en/spouse/ /Users/swalter/Desktop/config.xml"
