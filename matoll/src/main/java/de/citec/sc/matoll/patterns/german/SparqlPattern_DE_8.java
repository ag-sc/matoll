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

public class SparqlPattern_DE_8 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_8.class.getName());
	
	/*
	 * PropSubj:Barbara Amiel
	PropObj:Conrad Black
	sentence:Conrad Black heiratete im Juli 1992 die englische Journalistin und Schriftstellerin Barbara Amiel * 4. Dezember 1940 , die damals Kolumnistin der Londoner Times war und vor dieser Ehe mit Conrad Black bereits mit David Graham , George Jonas und Gary Smith verheiratet gewesen war . 
	1	Conrad	Conrad	N	NE	_|Nom|Sg	3	subj	_	_ 
	2	Black	Black	FM	FM	_|Nom|Sg	1	app	_	_ 
	3	heiratete	heiraten	V	VVFIN	3|Sg|Past|_	0	root	_	_ 
	4	im	im	PREP	APPRART	Dat	3	pp	_	_ 
	5	Juli	Juli	N	NE	Masc|Dat|Sg	4	pn	_	_ 
	6	1992	1992	CARD	CARD	_	5	app	_	_ 
	7	die	die	ART	ART	Def|Fem|Akk|Sg	9	det	_	_ 
	8	englische	englisch	ADJA	ADJA	Pos|Fem|Akk|Sg|_|	9	attr	_	_ 
	9	Journalistin	Journalistin	N	NN	Fem|Akk|Sg	3	obja	_	_ 
			----------------------*/
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:form> ?e1_form . "
                            + "?e1 <conll:deprel> ?e1_grammar . "
                            + "FILTER regex(?e1_grammar, \"subj\") ."
                            + "?e1 <conll:head> ?y . "
                            + "?y <conll:cpostag> ?lemma_pos . "
                            + "FILTER regex(?lemma_pos, \"V\") ."
                            + "?y <conll:lemma> ?lemma . "
                            + "?e2 <conll:head> ?y . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			

			

	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_8";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
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
    
		if(verb!=null && e1_arg!=null && e2_arg!=null) {
                    Templates.getTransitiveVerb(model, lexicon,sentences, verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
		
		
	}

}
