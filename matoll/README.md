#  M-ATOLL

M-ATOLL consists of two different approaches, one called dependency-based approach and one called label-based approach. The MATOLL-API itself can be used independently from both approaches to create,load, manipulate or write [lemon] (http://lemon-model.net) based lexica.


## Dependency-based Approach

### How to run this approach?
The easiest way to run this approach is using maven.


```java
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train /path/to/inputFiles/ /path/to/config.xml"

```
Input examples to test M-ATOLL can be found here:

*	[English] (http://dblexipedia.org/public/Input_EN.tar.gz)
* 	[German] (http://dblexipedia.org/public/Input_DE.tar.gz)
*  [Spanish] (http://dblexipedia.org/public/Input_ES.tar.gz)


The following is an example for the config.xml

```xml
<Config>
   <Language>EN</Language>
   <Coreference>False</Coreference>
   <GoldStandardLexicon>../lexica/dbpedia_en.rdf</GoldStandardLexicon>
   <OutputLexicon>dbpedia2014Full_new.lex</OutputLexicon>
   <Output>dbpedia2014.eval</Output>
   <NumLexItems>10000</NumLexItems>
   <RemoveStopwords>True</RemoveStopwords>
   <BaseURI>http://localhost:8080/</BaseURI>
</Config>

```
Using the input examples above and the currently (October 2015) implemented pattern, the results for the DBpedia 2014 ontology are the following:

*	[English] (http://dblexipedia.org/public/dbpedia2014_EN.ttl)
* 	[German] (http://dblexipedia.org/public/dbpedia2014_DE.ttl)
*  [Spanish] (http://dblexipedia.org/public/dbpedia2014_ES.ttl)

## Label-based Approach

Currently under construction.

<!--## What do I have to do to port MATOLL to other languages? -->