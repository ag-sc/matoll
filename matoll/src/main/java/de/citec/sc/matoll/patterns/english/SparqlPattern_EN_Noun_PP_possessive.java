package de.citec.sc.matoll.patterns.english;

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

public class SparqlPattern_EN_Noun_PP_possessive extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_Noun_PP_possessive.class.getName());
	
        
    /*
        property subject: Mount Lucania
property subject uri: http://dbpedia.org/resource/Mount_Lucania
property object: Bradford Washburn
property object uri: http://dbpedia.org/resource/Bradford_Washburn
sentence::
1    The    _    DT    DT    _    3    det    _    _
2    first    _    JJ    JJ    _    3    amod    _    _
3    ascent    _    NN    NN    _    8    nsubjpass    _    _
4    of    _    IN    IN    _    3    prep    _    _
5    Mount    _    NNP    NNP    _    6    nn    _    _
6    Lucania    _    NNP    NNP    _    4    pobj    _    _
7    was    _    VBD    VBD    _    8    auxpass    _    _
8    made    _    VBN    VBN    _    0    null    _    _
9    in    _    IN    IN    _    8    prep    _    _
10    1937    _    CD    CD    _    9    pobj    _    _
11    by    _    IN    IN    _    8    prep    _    _
12    Bradford    _    NNP    NNP    _    13    nn    _    _
13    Washburn    _    NNP    NNP    _    11    pobj    _    _
14    and    _    CC    CC    _    13    cc    _    _
15    Robert    _    NNP    NNP    _    17    nn    _    _
16    Hicks    _    NNP    NNP    _    17    nn    _    _
17    Bates    _    NNP    NNP    _    13    conj    _    _
18    .    _    .    .    _    8    punct    _    _

        */
    @Override
    public String getQuery() {
	String query = "SELECT ?prefix ?prep ?lemma ?e1_arg ?e2_arg WHERE {"
			
                        +"?noun <connl:head> ?verb . "
                        +"?noun <connl:deprel> \"nsubjpass\" . "
                        +"?noun <connl:form> ?form . "
                        +"?p <conll:head> ?noun . "
                        +"?p <connl:deprel> \"prep\" . "
                        +"?p <conll:form> ?prep . "
                        +"?e1 <conll:head> ?p . "
                        +"?e1 <conll:deprel> \"pobj\" . "
                        +"?addition <connl:head> ?noun . "
                        +"?addition <conll:form> ?prefix . "
                        +"?addition <conll:deprel> \"amod\" . "
                        +"?verb <conll:form> \"made\" . "
                        //verb is root
                        +" ?verb <conll:deprel> \"null\" . "
                        +"?p2 <conll:head> ?verb . "
                        +"?p2 <connl:deprel> \"prep\" . "
                        +"?e2 <connl:head> ?p2 . "
                        +"?e2 <conll:deprel> \"pobj\" . "
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
		return "SparqlPattern_EN_Noun_PP_possessive";
	}

}
