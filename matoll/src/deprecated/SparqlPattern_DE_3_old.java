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

public class SparqlPattern_DE_3_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_3_old.class.getName());
	
	/*
PropSubj:Iman Abdulmajid
PropObj:David Bowie
sentence:Haywood war zudem mit dem Model Iman Abdulmajid , der heutigen Ehefrau von Popstar David Bowie , verheiratet . 
1	Haywood	Haywood	N	NE	_|Nom|Sg	2	subj	_	_ 
2	war	sein	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
3	zudem	zudem	ADV	ADV	_	18	adv	_	_ 
4	mit	mit	PREP	APPR	Dat	18	pp	_	_ 
5	dem	das	ART	ART	Def|Neut|Dat|Sg	6	det	_	_ 
6	Model	Model	N	NN	Neut|Dat|Sg	4	pn	_	_ 
7	Iman	Iman	N	NN	Neut|Dat|Sg	6	app	_	_ 
8	Abdulmajid	Abdulmajid	N	NE	Neut|Dat|Sg	7	app	_	_ 
9	,	,	$,	$,	_	0	root	_	_ 
10	der	der	ART	ART	Def|Fem|Dat|Sg	12	det	_	_ 
11	heutigen	heutig	ADJA	ADJA	Pos|Fem|Dat|Sg|_|	12	attr	_	_ 
12	Ehefrau	Ehefrau	N	NN	Fem|Dat|Sg	6	app	_	_ 
13	von	von	PREP	APPR	Dat	12	pp	_	_ 
14	Popstar	Popstar	N	NN	Masc|Dat|Sg	13	pn	_	_ 
15	David	David	N	NE	Masc|Dat|Sg	14	app	_	_ 
16	Bowie	Bowie	N	NE	Masc|Dat|Sg	15	app	_	_ 
17	,	,	$,	$,	_	0	root	_	_ 
18	verheiratet	verheiraten	V	VVPP	_	2	aux	_	_ 
19	.	.	$.	$.	_	0	root	_	_ 	
----------------------
	*/
		
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:cpostag> \"N\" . "
                            + "?y <conll:deprel> \"app\" . "
                            + "?y <conll:cpostag> \"N\" . "
                            + "?y <conll:lemma> ?lemma . "
                            + "?y <conll:head> ?e1 . "
                            + "?p <conll:form> ?prep ."
                            + "?p <conll:deprel> \"pp\". "
                            + "?p <conll:head> ?y ."
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            + "?e2 <conll:cpostag> \"N\". "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_3";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getNounWithPrep(model, lexicon, sentences, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
