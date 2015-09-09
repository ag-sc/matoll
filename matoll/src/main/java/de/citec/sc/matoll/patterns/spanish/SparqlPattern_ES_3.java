package de.citec.sc.matoll.patterns.spanish;

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
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_3 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_3.class.getName());
	
	
	/*
	 * 
propSubject:flickr
propObject:ludicorp
--------------
sentence:Ludicorp es la empresa creadora de Flickr , sitio web de organizacin de fotografas digitales y red social . 
1	Ludicorp	ludicorp	n	NP00000	_	2	SUBJ
2	es	ser	v	VSIP3S0	_	0	ROOT
3	la	el	d	DA0FS0	_	4	SPEC
4	empresa	empresa	n	NCFS000	_	2	ATR
5	creadora	creador	a	AQ0FS0	_	4	MOD
6	de	de	s	SPS00	_	4	MOD
7	Flickr	flickr	n	NP00000	_	6	COMP
	 */
	
	// TODO: concatenate ?noun and ?lemma as lemma of the lexical entry, literally, no lemmatization
	// further checks need to be done here actually, but using the adjective as before was clearly wrong
	// ideally we would check that the adjective actually directly follows the noun
	
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
                            + "?adjective <conll:lemma> ?lemma . "
                            + "?adjective <conll:head> ?blank . "
                            + "?adjective <conll:deprel> \"MOD\" . "
                            + "?adjective <conll:postag> \"AQ0FS0\". "
                            + "?noun <conll:head> ?noun. "
                            + "?noun <conll:deprel> \"ATR\". "
                            + "?verb <conll:postag> ?verb_pos . "
                            + "FILTER regex(?verb_pos, \"VS\") ."
                            + "?e1 <conll:head> ?verb . "
                            + "?e1 <conll:deprel> ?e1_grammar . "
                            + "FILTER regex(?e1_grammar, \"SUBJ\") ."
                            + "?p <conll:head> ?blank . "
                            + "?p <conll:deprel> \"MOD\". "
                            + "?p <conll:postag> \"SPS00\". "
                            + "?p <conll:lemma> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:cpostag> \"n\" . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER regex(?e2_grammar, \"COMP\") ."
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_3";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
            } 
		
	}

}
