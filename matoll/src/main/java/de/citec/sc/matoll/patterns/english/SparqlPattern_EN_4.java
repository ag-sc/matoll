package de.citec.sc.matoll.patterns.english;

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


public class SparqlPattern_EN_4 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_4.class.getName());

	
	/*
	 * 
	PropSubj:Joe Wright
	PropObj:Anoushka Shankar
	sentence:Joe Wright is married to sitarist Anoushka Shankar , daughter of Ravi Shankar and half-sister of Norah Jones . 
	1	Joe	_	NNP	NNP	_	2	nn
	2	Wright	_	NNP	NNP	_	4	nsubjpass
	3	is	_	VBZ	VBZ	_	4	auxpass
	4	married	_	VBN	VBN	_	0	null
	5	to	_	TO	TO	_	6	aux
	6	sitarist	_	VB	VB	_	4	xcomp
	7	Anoushka	_	NNP	NNP	_	8	nn
	8	Shankar	_	NNP	NNP	_	6	dobj
	9	,	_	,	,	_	8	punct
	10	daughter	_	NN	NN	_	8	appos
	11	of	_	IN	IN	_	10	prep
	12	Ravi	_	NNP	NNP	_	13	nn
	13	Shankar	_	NNP	NNP	_	11	pobj
	14	and	_	CC	CC	_	10	cc
	15	half-sister	_	NN	NN	_	10	conj
	16	of	_	IN	IN	_	10	prep
	17	Norah	_	NNP	NNP	_	18	nn
	18	Jones	_	NNP	NNP	_	16	pobj
	19	.	_	.	.	_	4	punct
	----------------------
	IGONORE sitarist: so x is married to y
	 */
	/*
	 * TODO: Check lemma addition
	 */
        
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?lemma_addition ?prep ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:deprel> \"nsubjpass\" . "
                            + "?e1 <conll:cpostag> ?e1_pos . "
                            //+ "FILTER regex(?e1_pos, \"NN\") ."
                            + "?e1 <conll:head> ?y . "
                            + "?y <conll:cpostag> ?lemma_pos . "
                            + "{?y <conll:cpostag> \"VBN\" . }"
                            + "UNION"
                            + "{?y <conll:cpostag> \"VBG\" . }"
                            + "?y <conll:form> ?lemma . "
                            +"OPTIONAL{"
                            + "?lemma_nn <conll:head> ?y. "
                            + "?lemma_nn <conll:form> ?lemma_addition. "
                            + "?lemma_nn <conll:deprel> \"nn\"."
                            +"} "
                            + "?verb <conll:head> ?y . "
                            + "?verb <conll:deprel> \"auxpass\" . "
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"prep\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> \"pobj\" . "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

                
                QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String adjective = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String lemma_addition = "";

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 adjective = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
                                 try{
                                     lemma_addition = qs.get("?lemma_addition").toString();
                                 }
                                 catch(Exception e){
                                     
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
    
		if(adjective!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Sentence sentence = this.returnSentence(model);
                    if(!lemma_addition.equals("")){
                        Templates.getAdjective(model, lexicon, sentence, lemma_addition+" "+adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                    }
                    else Templates.getAdjective(model, lexicon, sentence, adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
				
	}

        @Override
	public String getID() {
		return "SPARQLPattern_EN_4";
	}

}
