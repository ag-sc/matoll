package de.citec.sc.matoll.patterns.german;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_4 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_4.class.getName());
	
	/*
	 * 24	Big	Big	ADV	ADJD	_	25	adv	_	_ 
25	Stone	Stone	ADJA	ADJA	_|_|Gen|Sg|_	26 attr
26	Lake	Lake	N	NN	_|Gen|Sg	22	gmod	_	_ 
27	,	,	$,	$,	_	0	root	_	_ 
28	der	der	ART	ART	Def|_|Gen|_	29	det	_	_ 
29	Quelle	Quell	N	NN	_|Gen|_	26	app	_	_ 
30	des	das	ART	ART	Def|_|Gen|Sg	31	det	_	_ 
31	Minnesota	Minnesota	N	NE	_|Gen|Sg	29	gmod	_	_ 
32	River	River	N	NE	_|Gen|Sg	31	app	_	_ 
	 */
	//ohnePrep
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:cpostag> \"N\" . "
                            + "?y <conll:deprel> \"app\" . "
                            + "?y <conll:cpostag> \"N\" . "
                            + "?y <conll:lemma> ?lemma . "
                            + "?y <conll:head> ?e1 . "
                            + "?e2 <conll:head> ?y . "
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
		return "SPARQLPattern_DE_4";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
                QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null) {
                    Templates.getNoun(model, lexicon, sentences, noun, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
		
		
		
	}

}
