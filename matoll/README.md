#  M-ATOLL

M-ATOLL consists of two different approaches, one called dependency-based approach and one called label-based approach. The MATOLL-API itself can be used independently from both approaches to create,load, manipulate or write [lemon] (http://lemon-model.net) based lexica.

## API
In this section some examples of how to use the MATOLL-API are presented. You find the current version of the API (including all dependencies) [here] (http://dblexipedia.org/public/MATOLL.jar), or you can build it directly from the source code.

### LexicalEntry
The class LexicalEntry is one of the core classes of M-ATOLL. With this class, syntactically correct lemon entries can be created.

`LexicalEntry entry = new LexicalEntry(language);` initializes  a lexical entry for a given language. Currently the language class of MATOLL supports four languages:

````
public enum Language {    
    EN, DE, ES, JA
}
````

For each entry the canonical form, e.g. `setCanonicalForm("wife")`, the POS-tag, e.g. `setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun")` and the URI of the lexical entry, e.g. `setURI("http://dblexipedia.org/LexicalEntry_wife_as_Noun_withPrep_of")`, has to be defined. This URI is later used in the lexicon to distinguish between different entries.

Additionally each lexical entry can contain one or more senses (this results from the fact that in principle each lexical entry can be used to lexicalize different properties, e.g. as done [here](http://dblexipedia.org/LexicalEntry_village_as_Noun_withPrep_in)), with each sense containing one reference. In M-ATOLL we have two different types of references, one simple reference, and one reference object for restriction classes, as described below in the section about the label-based approach.

````
Sense sense = new Sense();
Reference ref = new SimpleReference("http://dbpedia.org/ontology/spouse");
sense.setReference(ref);
````


While creating a lexical entry, it is also mandatory to define the syntactic behaviour of the entry. This is later bound to the sense, as again one entry can be a lexicalization for different properties, containing different senses, with different references and different behaviours.
It is also possible two have twice the same sense, lexicalizing the same reference, but only differ in the syntactic behaviour.

````
SyntacticBehaviour behaviour  = new SyntacticBehaviour();

behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPossessiveFrame");

behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",preposition));
behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","subject",null));

sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

entry.addSyntacticBehaviour(behaviour,sense);
````

Imagine the use case, where you iterate over a lexical entry with the goal to print out the URI of the property the entry lexicalize; again one can only do this by iterating over the pair of SyntacticBehaviour and Sense

````
for(Sense sense : entry.getSenseBehaviours().keySet()){
     Reference ref = sense.getReference();
     //for a simple reference
     if (ref instanceof de.citec.sc.matoll.core.SimpleReference)  
     		System.out.println(ref.getURI());
     		
     //for a restriction class
     if (ref instanceof de.citec.sc.matoll.core.Restriction)
          System.out.println(reference.getProperty());
}           
````



Each lexical entry may contain a Provenance, where additional information about the entry, such as the frequency, is stored. 

````
Provenance provenance = new Provenance();
provenance.setFrequency(1);
entry.addProvenance(provenance,sense);
````
To retrieve the information of the provenance the same construct as to access the reference is needed.


More detailed examples, how in M-ATOLL lexical entries are generated, are presented in [Templates.java] (src/main/java/de/citec/sc/matoll/patterns/Templates.java).


### Lexicon
If not empty, a lexicon consists of a set of lexical entries.

If a new lexical entry is added to the lexicon, e.g `lexicon.addEntry(entry)`, based on the URI of the lexical entry, it is automatically verified, whether the entry already exists or not. 

If the entry does not exists yet, it is simply added to the lexicon. If the lexical entry already exists in the lexicon, the lexicon itself checks, whether the Sense of the given lexical entry already exists in the corresponding lexical entry of the lexicon or not. Again, if the Sense does not exist, it is added to the corresponding lexical entry in the lexicon, but if it already exists, only the frequency of the provenance of the relevant sense is increased by one (in the lexicon).

The lexicon has some build-in functions to retrieve lexical entries:

*	`lexicon.getEntries()`, retrieving all lexical entries
* 	`lexicon.getEntries(language)`, retrieving all lexical entries for a certain language, where the language is from the class Language as described above
*  `lexicon.getEntriesWithCanonicalForm("wife")`, retrieving all lexical entries with a certain canonical form, e.g "wife"
*  `lexicon.getEntriesForReference("http://dbpedia.org/ontology/spouse")`, retrieving all lexical entries for a certain property, e.g. "http://dbpedia.org/ontology/spouse"

Additionally some other helpful functions are available:


* `lexicon.getPrepositions()`, returns all used prepositions, e.g. "to", "for", "while", etc. , from the lexical entries in the lexicon.
* 	`lexicon.getReferences()`, returns all references (e.g. "http://dbpedia.org/ontology/spouse") in a given lexicon.
* `lexicon.size()`, returns the number of lexical entries. Note: As one entry can lexicalize multiple properties(have multiple references), it is for example possible, to have more references than lexical entries. 
*  `lexicon.setBaseURI("http://localhost:8080/")`, sets the BaseURI for the lexicon to "http://lodalhost:8080". If this function is not used, the BaseURI is automatically set to "http://dblexipedia.org/"





### LexiconLoader
 ````
 LexiconLoader loader = new LexiconLoader();
 Lexicon lexicon = loader.loadFromFile("example.ttl");
 ````
 After this two steps the lexicon `lexicon` can be used as described in the subsection above.

### LexiconSerialization

With the help of the LexiconSerialization, a lexicon is written into an RDF file.

````
LexiconSerialization serializer = new LexiconSerialization(true);
Model model = ModelFactory.createDefaultModel();

serializer.serialize(lexicon, model);

FileOutputStream out = new FileOutputStream(new File("lexicon.ttl"));
RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
out.close();

````

When initializing the LexiconSerialization `LexiconSerialization serializer = new LexiconSerialization(true);`, a boolean operator (true/false) defines if during the serialization all stop words for the given language of the lexical entry (in the given lexicon) are removed. E.g. if by mistake for the English language a lexical entry with the canonical form "she" is created  (or "sie" in case of German), with initializing the LexiconSerialization with `true`, this entry is not written into RDF, but ignored.


## Dependency-based Approach

### How to run this approach?
The easiest way to run this approach is using maven.


````
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="de.citec.sc.matoll.process.Matoll" -Dexec.args="--mode=train /path/to/inputFiles/ /path/to/config.xml"

````
Input examples to test M-ATOLL can be found here:

*	[English] (http://dblexipedia.org/public/Input_EN.tar.gz)
* 	[German] (http://dblexipedia.org/public/Input_DE.tar.gz)
*  [Spanish] (http://dblexipedia.org/public/Input_ES.tar.gz)


The following is an example for the config.xml

````
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

````


## Label-based Approach

Currently under construction.

<!--## What do I have to do to port MATOLL to other languages? -->