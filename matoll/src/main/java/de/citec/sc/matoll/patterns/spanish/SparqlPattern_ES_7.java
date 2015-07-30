package de.citec.sc.matoll.patterns.spanish;

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

public class SparqlPattern_ES_7 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7.class.getName());

// aus spouse	
	
//	ID:291
//	property subject: Joviano
//	property object: Charito
//	sentence:: 
//	1	Charito	charito	n	NP00000	_	3	SUBJ
//	2	se	se	p	P00CN000	_	3	DO
//	3	casó	casar	v	VMIS3S0	_	0	ROOT
//	4	con	con	s	SPS00	_	3	MOD
//	5	Joviano	joviano	n	NP00000	_	4	COMP
//	6	,	,	f	Fc	_	5	punct
//	7	un	uno	d	DI0MS0	_	8	SPEC
//	8	hijo	hijo	n	NCMS000	_	3	DO
//	9	de	de	s	SPS00	_	8	MOD
//	10	Varroniano	varroniano	n	NP00000	_	9	COMP
//	11	.	.	f	Fp	_	10	punct

 
	// reflexive verb: se casó con
	// Template needs to be changed!!!
    
        // X verheiratet sich mit Y
        // -> Reflexiv 
        // X heiratete Y -> Transitive
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
                            + "?verb <conll:cpostag> ?verb_pos ."
                            + "?verb <conll:lemma> ?lemma ."
                            + "FILTER regex(?verb_pos, \"VMIS\") ."

                            // "DO" can also be "MPAS"
                            + "?se <conll:lemma> \"se\" ."
                            + "?se <conll:deprel> \"DO\" ."
                            + "?se <conll:head> ?verb ."

                            + "?e1 <conll:head> ?verb."
                            + "?e1 <conll:deprel> \"SUBJ\" ."


                            // can be OBLC instead of MOD
                            + "?p <conll:head> ?verb."
                            + "?p <conll:deprel> \"MOD\" ."
                            + "?p <conll:lemma> ?prep. "

                            + "?e2 <conll:head> ?p."
                            + "?e2 <conll:deprel> \"COMP \"."

                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_7";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
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
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getIntransitiveVerb(model, lexicon, sentences, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
            } 
		
	
	}

}
