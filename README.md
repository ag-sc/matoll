# M-ATOLL


M-ATOLL is developed by the [Semantic Computing Group](http://www.sc.cit-ec.uni-bielefeld.de/en/home) at CITEC, Bielefeld University.
It is a framework for the automatic lexicalization of ontologies in multiple languages. 
The results of M-ATOLL are published at [DBlexipedia](http://dblexipedia.org)

Presently DBlexipedia provides English, German and Spanish lexicalizations for over 600 properties of the DBpedia ontology. 

## Repository
This repository contains two separated projects/folders.

1. 	[matoll](matoll/README.md), which contains the actual M-ATOLL project
2. [SentencePreprocessing](SentencePreprocessing/README.md), which is used to create the necessary input for MATOLL.

Examples for the input to both projects can be found on the projects README. Both projects are maven-based projects. Additionally to the main M-ATOLL approach, the MATOLL-API itself can be used without invoking the whole automatic approach.

The folder `lexica` contains example lexica to test different functions of M-ATOLL.

Finally the folder `public` contain results for our paper [Automatic acquisition of adjective lexicalizations of restriction classes](http://pub.uni-bielefeld.de/publication/2763507).

For further questions, or if you are interested in extending M-ATOLL to more languages, please contact the main developer [Sebastian Walter](http://sebastianwalter.org).


## References 

When citing M-ATOLL, please refer to the following paper:

Sebastian Walter, Christina Unger, Philipp Cimiano: [M-ATOLL: A Framework for the Lexicalization of Ontologies in Multiple Languages](http://link.springer.com/chapter/10.1007/978-3-319-11964-9_30). In: The Semantic Web – ISWC 2014. Lecture Notes in Computer Science Volume 8796, pp 472-486. Springer, 2014. ([Preprint PDF](https://github.com/lidingpku/iswc2014/blob/master/paper/87960464-m-atoll-a-framework-for-the-lexicalization-of-ontologies-in-multiple-languages.pdf?raw=true))




