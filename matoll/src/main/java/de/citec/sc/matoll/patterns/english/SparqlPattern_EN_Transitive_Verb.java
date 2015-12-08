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
import java.util.List;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;

public class SparqlPattern_EN_Transitive_Verb extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_Transitive_Verb.class.getName());
	
	
	/*	PropSubj:Juana EnrÃ­quez
	PropObj:John II of Aragon
	sentence:Juana EnrÃ­quez , great-granddaughter of the founder of the lineage , married John II of Aragon and was the mother of Ferdinand II . 
	1	Juana	_	NNP	NNP	_	2	nn
	2	EnrÃ­quez	_	NNP	NNP	_	12	nsubj
	3	,	_	,	,	_	2	punct
	4	great-granddaughter	_	NN	NN	_	2	appos
	5	of	_	IN	IN	_	4	prep
	6	the	_	DT	DT	_	7	det
	7	founder	_	NN	NN	_	5	pobj
	8	of	_	IN	IN	_	7	prep
	9	the	_	DT	DT	_	10	det
	10	lineage	_	NN	NN	_	8	pobj
	11	,	_	,	,	_	2	punct
	//married is falsch getagged, müsste VBD seind
	12	married	_	VBN	VBN	_	0	null
	13	John	_	NNP	NNP	_	14	nn
	14	II	_	NNP	NNP	_	12	dobj
	15	of	_	IN	IN	_	14	prep
	16	Aragon	_	NNP	NNP	_	15	pobj
	17	and	_	CC	CC	_	12	cc
	18	was	_	VBD	VBD	_	20	cop
	19	the	_	DT	DT	_	20	det
	20	mother	_	NN	NN	_	12	conj
	21	of	_	IN	IN	_	20	prep
	22	Ferdinand	_	NNP	NNP	_	23	nn
	23	II	_	NNP	NNP	_	21	pobj
	24	.	_	.	.	_	12	punct
        
        
	----------------------*/
        
        @Override
    public String getQuery() {
	String query = "SELECT ?lemma ?e1_arg ?prt_form ?e2_arg WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> \"nsubj\" ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			+ "FILTER regex(?lemma_pos, \"VB\") ."
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
                        + "OPTIONAL {"
                        + "?prt <conll:head> ?y . "
                        + "?prt <conll:form> ?prt_form ."
                        + "?prt <conll:deprel> \"prt\" ."
                        + "}"
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel>  \"dobj\" . "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?e1 <own:senseArg> ?e1_arg . "
			+ "?e2 <own:senseArg> ?e2_arg . "
			+ "}";
        return query;
    }
	
	

	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
                QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String prt_form = null;
                while ( rs.hasNext() ) {
                        QuerySolution qs = rs.next();
                        try{
                            verb = qs.get("?lemma").toString();
                            e1_arg = qs.get("?e1_arg").toString();
                            e2_arg = qs.get("?e2_arg").toString();	

                            try{
                                prt_form = qs.get("?prt_form").toString();
                            }catch(Exception e){
                               }
                           if(verb!=null && e1_arg!=null && e2_arg!=null) {
                                Sentence sentence = this.returnSentence(model);
                                if(prt_form!=null){
                                    Templates.getTransitiveVerb(model, lexicon, sentence, verb+" "+prt_form, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                                }
                                else
                                    Templates.getTransitiveVerb(model, lexicon, sentence, verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                            } 

                         }
                        catch(Exception e){
                       e.printStackTrace();
                       }
                }
                

                qExec.close() ;
    
		
		

	}
	
	


        @Override
	public String getID() {
		return "SPARQLPattern_EN_Transitive_Verb";
	}

}
