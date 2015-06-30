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

public class SparqlPattern_ES_7 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7.class.getName());
	
	// Add se casó con....
	
//	<class:class622>  <conll:language>  "es" ;
//    <conll:reference>  "http://dbpedia.org/ontology/spouse" ;
//    <conll:sentence>   "Todavía muy joven , Pandú se casó con Madrí ― hija de el rey de Madra ― , y con Kuntí ― hija de el rey vrisni Kunti-Bhoya ." ;
//    <own:obj>          "pandú" ;
//    <own:subj>         "kuntí" .
//    
//    <token:token622_1>  <conll:cpostag>  "r" ;
//    <conll:deprel>      "MOD" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "todavía" ;
//    <conll:head>        <token:token622_7> ;
//    <conll:lemma>       "todavía" ;
//    <conll:postag>      "RG" ;
//    <conll:wordnumber>  "1" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_2>  <conll:cpostag>  "r" ;
//    <conll:deprel>      "SPEC" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "muy" ;
//    <conll:head>        <token:token622_3> ;
//    <conll:lemma>       "muy" ;
//    <conll:postag>      "RG" ;
//    <conll:wordnumber>  "2" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_3>  <conll:cpostag>  "a" ;
//    <conll:deprel>      "SUBJ" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "joven" ;
//    <conll:head>        <token:token622_7> ;
//    <conll:lemma>       "joven" ;
//    <conll:postag>      "AQ0CS0" ;
//    <conll:wordnumber>  "3" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_4>  <conll:cpostag>  "f" ;
//    <conll:deprel>      "punct" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "," ;
//    <conll:head>        <token:token622_3> ;
//    <conll:lemma>       "," ;
//    <conll:postag>      "Fc" ;
//    <conll:wordnumber>  "4" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_5>  <conll:cpostag>  "n" ;
//    <conll:deprel>      "MOD" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "pandú" ;
//    <conll:head>        <token:token622_3> ;
//    <conll:lemma>       "pandú" ;
//    <conll:postag>      "NP00000" ;
//    <conll:wordnumber>  "5" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_6>  <conll:cpostag>  "p" ;
//    <conll:deprel>      "DO" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "se" ;
//    <conll:head>        <token:token622_7> ;
//    <conll:lemma>       "se" ;
//    <conll:postag>      "P00CN000" ;
//    <conll:wordnumber>  "6" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_7>  <conll:cpostag>  "v" ;
//    <conll:deprel>      "ROOT" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "casó" ;
//    <conll:head>        <token:token622_0> ;
//    <conll:lemma>       "casar" ;
//    <conll:postag>      "VMIS3S0" ;
//    <conll:wordnumber>  "7" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_8>  <conll:cpostag>  "s" ;
//    <conll:deprel>      "MOD" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "con" ;
//    <conll:head>        <token:token622_7> ;
//    <conll:lemma>       "con" ;
//    <conll:postag>      "SPS00" ;
//    <conll:wordnumber>  "8" ;
//    <own:partOf>        <class:class622> .
//    
//    <token:token622_9>  <conll:cpostag>  "n" ;
//    <conll:deprel>      "COMP" ;
//    <conll:feats>       "_" ;
//    <conll:form>        "madrí" ;
//    <conll:head>        <token:token622_8> ;
//    <conll:lemma>       "madrí" ;
//    <conll:postag>      "NP00000" ;
//    <conll:wordnumber>  "9" ;
//    <own:partOf>        <class:class622> .
//    
//    
    
    



	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?verb <conll:cpostag> ?verb_pos ."
			+ "FILTER regex(?deprel, \"VMIS\") ."
			
			+ "?se <conll:lemma> \"se\" ."
			+ "?se <conll:deprel> \"DO\" ."
			+ "?se <conll:head> ?verb ."
			
			+ "?e1 <conll:head> ?verb."
			+ "?e1 <conll:deprel> \"SUBJ\" ."

			+ "?p <conll:head> ?verb."
			+ "?p <conll:deprel> \"MOD\" ."
			+ "?p <conll:lemma> ?prep. "
		
			+ "?e2 <conll:head> ?p."
			+ "?e2 <conll:deprel> \"COMP \"."
			
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
			
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
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
		
		
	}

}
