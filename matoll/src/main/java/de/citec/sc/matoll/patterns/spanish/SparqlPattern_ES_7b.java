package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_7b extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7b.class.getName());
	
	// X y Y se casaron
	
//	<class:class125>  <conll:language>  "es" ;
//    <conll:reference>  "http://dbpedia.org/ontology/spouse" ;
//    <conll:sentence>   "Poco después Claudio y Agripinila se casaron y éste adoptó a el hijo de Agripinila , Nerón , como su heredero por encima_de Británico ." ;
//    <own:obj>          "claudio" ;
//    <own:subj>         "agripinila" .
//    
//        <token:token125_1>  <conll:cpostag>  "r" ;
//        <conll:deprel>      "SPEC" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "poco" ;
//        <conll:head>        <token:token125_2> ;
//        <conll:lemma>       "poco" ;
//        <conll:postag>      "RG" ;
//        <conll:wordnumber>  "1" ;
//        <own:partOf>        <class:class125> .
//        
//        <token:token125_2>  <conll:cpostag>  "r" ;
//        <conll:deprel>      "MOD" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "después" ;
//        <conll:head>        <token:token125_7> ;
//        <conll:lemma>       "después" ;
//        <conll:postag>      "RG" ;
//        <conll:wordnumber>  "2" ;
//        <own:partOf>        <class:class125> .
//        
//
//        
//        <token:token125_3>  <conll:cpostag>  "n" ;
//        <conll:deprel>      "SUBJ" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "claudio" ;
//        <conll:head>        <token:token125_7> ;
//        <conll:lemma>       "claudio" ;
//        <conll:postag>      "NP00000" ;
//        <conll:wordnumber>  "3" ;
//        <own:partOf>        <class:class125> .
//        
//        
//        <token:token125_4>  <conll:cpostag>  "c" ;
//        <conll:deprel>      "COORD" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "y" ;
//        <conll:head>        <token:token125_3> ;
//        <conll:lemma>       "y" ;
//        <conll:postag>      "CC" ;
//        <conll:wordnumber>  "4" ;
//        <own:partOf>        <class:class125> .
//        
//        <token:token125_5>  <conll:cpostag>  "n" ;
//        <conll:deprel>      "CONJ" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "agripinila" ;
//        <conll:head>        <token:token125_4> ;
//        <conll:lemma>       "agripinila" ;
//        <conll:postag>      "NP00000" ;
//        <conll:wordnumber>  "5" ;
//        <own:partOf>        <class:class125> .
//        

//        <token:token125_6>  <conll:cpostag>  "p" ;
//        <conll:deprel>      "DO" ;
//        <conll:feats>       "_" ;
//        <conll:form>        "se" ;
//        <conll:head>        <token:token125_7> ;
//        <conll:lemma>       "se" ;
//        <conll:postag>      "P00CN000" ;
//        <conll:wordnumber>  "6" ;
//        <own:partOf>        <class:class125> .
        
//	   <token:token125_7>  <conll:cpostag>  "v" ;
//	    <conll:deprel>      "ROOT" ;
//	    <conll:feats>       "_" ;
//	    <conll:form>        "casaron" ;
//	    <conll:head>        <token:token125_0> ;
//	    <conll:lemma>       "casar" ;
//	    <conll:postag>      "VMIS3P0" ;
//	    <conll:wordnumber>  "7" ;
//	    <own:partOf>        <class:class125> .
        
        
	// */
	String query = "SELECT ?verb_lemma ?e1_arg ?e2_arg  WHERE {"
			+ "?verb <conll:cpostag> ?verb_pos ."
			+ "FILTER regex(?deprel, \"VMIS\") ."
			
			+ "?se <conll:lemma> \"se\" ."
			+ "?se <conll:deprel> \"DO\" ."
			+ "?se <conll:head> ?verb ."
			
			+ "?e1 <conll:head> ?verb."
			+ "?e1 <conll:deprel> \"SUBJ\" ."

			+ "?coord <conll:head> ?e1 ."
			+ "?coord <conll:deprel> \"COORD\" ."
			+ "?coord <conll:lemma> \"y\" ."
			
			+ "?e2 <conll:head> ?coord."
			+ "?e2 <conll:deprel> \"CONJ \"."
			
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	// we need a new construction for casar+se
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_7";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES);
		
		
	}

}
