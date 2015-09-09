package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_EN_3 extends SparqlPattern {

	/*################################
entity1_form:shriram
entity2_form:google
propSubject:ram shriram
propObject:google
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:member
lemma_grammar:null
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query5
intended_lexical_type:Noun
entry:RelationalNoun("board member",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Kavitark Ram Shriram is a board member of Google and one of the first investors in Google . 
1	Kavitark	_	NNP	NNP	_	3	nn
2	Ram	_	NNP	NNP	_	3	nn
3	Shriram	_	NNP	NNP	_	7	nsubj
4	is	_	VBZ	VBZ	_	7	cop
5	a	_	DT	DT	_	7	det
6	board	_	NN	NN	_	7	nn
7	member	_	NN	NN	_	0	null
8	of	_	IN	IN	_	7	prep
9	Google	_	NNP	NNP	_	8	pobj
=>works only in fuzzy mode.

PropSubj:Oxford Brookes University
PropObj:Janet Beer
sentence:Professor Janet Beer is the Vice-Chancellor of Oxford Brookes University . 
1	Professor	_	NNP	NNP	_	3	nn
2	Janet	_	NNP	NNP	_	3	nn
3	Beer	_	NNP	NNP	_	6	nsubj
4	is	_	VBZ	VBZ	_	6	cop
5	the	_	DT	DT	_	6	det
6	Vice-Chancellor	_	NNP	NNP	_	0	null
7	of	_	IN	IN	_	6	prep
8	Oxford	_	NNP	NNP	_	10	nn
9	Brookes	_	NNP	NNP	_	10	nn
10	University	_	NNP	NNP	_	7	pobj
11	.	_	.	.	_	6	punct
----------------------


*/
	
		Logger logger = LogManager.getLogger(SparqlPattern_EN_3.class.getName());

        @Override
    public String getQuery() {
        String query = "SELECT ?lemma ?prefix ?e1_arg ?e2_arg ?prep WHERE"
                        +"{ "
                        +"{ ?y <conll:cpostag> \"NN\" . } "
                        + " UNION "
                        +"{ ?y <conll:cpostag> \"NNS\" . } "
                        + " UNION "
                        +"{ ?y <conll:cpostag> \"NNP\" . } "
                        + "?y <conll:form> ?lemma . "
                        +"OPTIONAL{"
                        + "?lemma_nn <conll:head> ?y. "
                        + "?lemma_nn <conll:form> ?prefix. "
                        + "?lemma_nn <conll:deprel> \"nn\"."
                        +"} "
                        + "?verb <conll:head> ?y . "
                        + "?verb <conll:deprel> \"cop\" ."
                        + "?e1 <conll:head> ?y . "
                        + "?e1 <conll:deprel> \"nsubj\" . "
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
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String modifier = "";

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
                                 try{
                                     modifier = qs.get("?prefix").toString();
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Sentence sentence = this.returnSentence(model);
                    if (!modifier.equals("")){
                        Templates.getNounWithPrep(model, lexicon, sentence, modifier +" "+noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                    }
                    else Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
		
	     
	}

        @Override
	public String getID() {
		return "SPARQLPattern_EN_3";
	}

}
