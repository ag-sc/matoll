package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.lemon.core.Language;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_Predicative_Adjective extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Predicative_Adjective.class.getName());
	
        
        /*
        ADJ
        */
//        
//        ID:7
//property subject: António Damásio
//property subject uri: http://dbpedia.org/resource/Antonio_Damasio
//property object: Hanna Damásio
//property object uri: http://dbpedia.org/resource/Hanna_Damasio
//sentence:: 
//1	António	António	N	NE	_|Nom|Sg	3	subj	_	_ 
//2	Damásio	Damásio	N	NE	_|Nom|Sg	1	app	_	_ 
//3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
//4	verheiratet	verheiratet	ADV	ADJD	Pos|	3	pred	_	_ 
//5	mit	mit	PREP	APPR	Dat	3	pp	_	_ 
//6	Hanna	Hanna	N	NE	_|Dat|_	5	pn	_	_ 
//7	Damásio	Damásio	N	NE	_|Dat|_	6	app	_	_ 
//8	.	.	$.	$.	_	0	root	_	_ 
        
//1	Paul	Paul	N	NE	_|Nom|Sg	3	subj	_	_ 
//2	Biya	Biya	N	NE	_|Nom|Sg	1	app	_	_ 
//3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
//4	verheiratet	verheiratet	ADV	ADJD	Pos|	3	adv	_	_ 
//5	mit	mit	PREP	APPR	Dat	3	pp	_	_ 
//6	Chantal	Chantal	N	NE	_|Dat|_	5	pn	_	_ 
//7	Biya	Biya	N	NE	_|Dat|_	6	app	_	_ 
//8	(	(	$(	$(	_	0	root	_	_ 

        
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?sein. "
                            + "?sein <conll:lemma> \"sein\". "
                            + "?verb <conll:form> ?lemma . "
                            + "?verb <conll:head> ?sein . "
                            + "{?verb <conll:cpostag> \"ADV\" . } UNION"
                            + "{?verb <conll:cpostag> \"V\" .} "
                            + "{?verb <conll:deprel> \"pred\" . } UNION "
                            + "{?verb <conll:deprel> \"adv\" . }"
                            + "?preposition <conll:head> ?sein ."
                            + "?preposition <conll:deprel> \"pp\" ."
                            + "?preposition <conll:lemma> ?prep ."
                            + "?e2 <conll:deprel> \"pn\" . "
                            + "?e2 <conll:head> ?preposition. "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SparqlPattern_DE_Predicative_Adjective";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                while ( rs.hasNext() ) {
                     QuerySolution qs = rs.next();

                     try{
                             verb = qs.get("?lemma").toString();
                             e1_arg = qs.get("?e1_arg").toString();
                             e2_arg = qs.get("?e2_arg").toString();	
                             preposition = qs.get("?prep").toString();
                             if(verb!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                                 Sentence sentence = this.returnSentence(model);
                                 Templates.getAdjective(model, lexicon, sentence, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                             }
                     }
                     catch(Exception e){
                    e.printStackTrace();
                    }
                 }

                 
                qExec.close() ;
    

	}

}
