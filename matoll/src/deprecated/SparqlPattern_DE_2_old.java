package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import org.apache.jena.shared.Lock;

public class SparqlPattern_DE_2_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_2_old.class.getName());
	
	/*
	 * PropSubj:Islamisch Republikanische Partei
PropObj:1979 Februar 
sentence:Am 19. Februar 1979 gründeten die Gefolgsleute Chomeinis die Islamisch Republikanische Partei . 
1	Am	am	PREP	APPRART	Dat	5	pp	_	_ 
2	19.	19.	ADJA	ADJA	_|Masc|Dat|Sg|_|	3	attr	_	_ 
3	Februar	Februar	N	NN	Masc|Dat|Sg	1	pn	_	_ 
4	1979	1979	CARD	CARD	_	3	app	_	_ 
5	gründeten	gründen	V	VVFIN	3|Pl|Past|_	0	root	_	_ 
6	die	die	ART	ART	Def|Masc|Nom|Pl	7	det	_	_ 
7	Gefolgsleute	Gefolgsmann	N	NN	Masc|Nom|Pl	5	subj	_	_ 
8	Chomeinis	Chomeinis	N	NN	Masc|Nom|Pl	7	app	_	_ 
9	die	die	ART	ART	Def|_|_|_	10	det	_	_ 
10	Islamisch	islamisch	N	NN	Fem|Akk|Sg	5	obja	_	_ 
11	Republikanische	republikanisch	ADJA	ADJA	Pos|Fem|Akk|Sg|_|	12	attr	_	_ 
12	Partei	Partei	N	NN	Fem|Akk|Sg	10	app	_
	 */
	
	@Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?dobj_form ?e1_arg ?e2_arg  WHERE {"
                            + "?y <conll:cpostag> \"V\" ."
                            //VVFIN
                            + "?y <conll:lemma> ?lemma . "
                            + "?e1 <conll:head> ?p . "
                            + "?e1 <conll:deprel> ?deprel. "
                            + "FILTER regex(?deprel, \"pn\") ."
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"pp\" . "
                            +" ?p <conll:postag> \"APPRART\". "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?y . "
                            + "?e2 <conll:deprel> \"obja\" . "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_2";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
                          }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
                        }
                     }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                qExec.close() ;
                model.leaveCriticalSection() ;
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getIntransitiveVerb(model, lexicon, sentences, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
