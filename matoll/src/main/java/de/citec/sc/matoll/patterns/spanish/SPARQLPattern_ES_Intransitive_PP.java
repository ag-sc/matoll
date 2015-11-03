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

public class SPARQLPattern_ES_Intransitive_PP extends SparqlPattern{

	Logger logger = LogManager.getLogger(SPARQLPattern_ES_Intransitive_PP.class.getName());

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
        
        /*
        TODO: "se" has to be optional.
        If "se" is found, create a ReflexiveTransitiveVerb, otherwise create an Intransitive entry
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?se_form ?e2_arg ?prep  WHERE {"
                            + "?verb <conll:postag> ?verb_pos ."
                            + "?verb <conll:lemma> ?lemma ."
                            + "FILTER regex(?verb_pos, \"VMIS\") ."
                            + "OPTIONAL{"
                                 + "?se <conll:form> \"se\" ."
                                 + "{?se <conll:deprel> \"DO\" .} UNION "
                                 + "{?se <conll:deprel> \"MPAS\" .}"
                                 + "?se <conll:head> ?verb ."
                                 + "?se <conll:form> ?se_form ."
                            + "}"

                            + "?e1 <conll:head> ?verb."
                            + "?e1 <conll:deprel> \"SUBJ\" ."

                            + "?p <conll:head> ?verb."
                            + "{?p <conll:deprel> \"MOD\" .} UNION "
                            + "{?p <conll:deprel> \"OBCL\" .} "
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
		return "SPARQLPattern_ES_Intransitive_PP";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String se_form = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();
                                 try{
                                     
                                 }
                                 catch(Exception e){
                                     se_form = qs.get("?se_form").toString();
                                 }
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
                    Sentence sentence = this.returnSentence(model);
                    if(se_form!=null)
                        Templates.getReflexiveTransitiveVerb(model, lexicon, sentence, "se "+verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                    else
                        Templates.getIntransitiveVerb(model, lexicon, sentence, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());

                }
		
	
	}

}
