package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_EN_2 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_2.class.getName());
	
    @Override
    public String getQuery() {
	String query = "SELECT ?prefix ?prep ?lemma ?e1_arg ?e2_arg WHERE {"
			+ "{?y <conll:deprel> \"appos\".} UNION {?y <conll:deprel> \"dep\".}"
			+ "?y <conll:form> ?lemma . "
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+"OPTIONAL{"
			+ "?modifier <conll:head> ?y. "
			+ "?modifier <conll:form> ?prefix. "
			+ "?modifier <conll:deprel> \"nn\"."
			+"} "
			+ "?y <conll:head> ?e1 . "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel>  \"pobj\". "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
        return query;
    }
	
	/*
	 * 
entity1_form:murdoch
entity2_form:company
propSubject:rupert murdoch
propObject:fox broadcasting company
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:creator
lemma_grammar:appos
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query2
intended_lexical_type:Noun
entry:RelationalNoun("creator",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Also featured in the episode is Rupert Murdoch , creator of the Fox Broadcasting Company . 
1       Also    _       RB      RB      _       2       advmod
2       featured        _       VBN     VBN     _       0       null
3       in      _       IN      IN      _       2       prep
4       the     _       DT      DT      _       5       det
5       episode _       NN      NN      _       3       pobj
6       is      _       VBZ     VBZ     _       2       dep
7       Rupert  _       NNP     NNP     _       8       nn
8       Murdoch _       NNP     NNP     _       6       nsubj
9       ,       _       ,       ,       _       8       punct
10      creator _       NN      NN      _       8       appos
11      of      _       IN      IN      _       10      prep
12      the     _       DT      DT      _       15      det
13      Fox     _       NNP     NNP     _       15      nn
14      Broadcasting    _       NNP     NNP     _       15      nn
15      Company _       NNP     NNP     _       11      pobj
16      .       _       .       .       _       2       punct
PropSubj:Anne Hyde
PropObj:James II of England
sentence:Ann was later named after Lady Anne Hyde the first wife of King James II of England . 
1	Ann	_	NNP	NNP	_	4	nsubjpass
2	was	_	VBD	VBD	_	4	auxpass
3	later	_	RB	RB	_	4	advmod
4	named	_	VBN	VBN	_	0	null
5	after	_	IN	IN	_	4	prep
6	Lady	_	NNP	NNP	_	8	nn
7	Anne	_	NNP	NNP	_	8	nn
8	Hyde	_	NNP	NNP	_	5	pobj
9	the	_	DT	DT	_	11	det
10	first	_	JJ	JJ	_	11	amod
11	wife	_	NN	NN	_	8	dep
12	of	_	IN	IN	_	11	prep
13	King	_	NNP	NNP	_	15	nn
14	James	_	NNP	NNP	_	15	nn
15	II	_	NNP	NNP	_	12	pobj
16	of	_	IN	IN	_	15	prep
17	England	_	NNP	NNP	_	16	pobj
18	.	_	.	.	_	4	punct
	 */
	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
                
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
                    Templates.getNounWithPrep(model, lexicon, sentences, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
                
				
	}

        @Override
	public String getID() {
		return "SPARQLPattern_EN_2";
	}

}
