/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.patterns.english;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author swalter
 */
public class SparqlPattern_EN_DatatypeNoun extends SparqlPattern{
    
    Logger logger = LogManager.getLogger(SparqlPattern_EN_DatatypeNoun.class.getName());
    @Override
    public String getID() {
        return "SparqlPattern_EN_DatatypeNoun";
    }

    /*
    ID:648
property subject: Rajca
property subject uri: http://dbpedia.org/resource/Rajca_(Resen_Municipality)
property object: 66
property object uri: 66^^http://www.w3.org/2001/XMLSchema#nonNegativeInteger
sentence::
1	Rajca	_	NNP	NNP	_	2	nsubj	_	_
2	has	_	VBZ	VBZ	_	0	null	_	_
3	66	_	CD	CD	_	4	num	_	_
4	inhabitants	_	NNS	NNS	_	2	dobj	_	_
5	as	_	IN	IN	_	4	prep	_	_
6	of	_	IN	IN	_	5	dep	_	_
7	the	_	DT	DT	_	10	det	_	_
8	most	_	RBS	RBS	_	9	advmod	_	_
9	recent	_	JJ	JJ	_	10	amod	_	_
10	census	_	NN	NN	_	6	pobj	_	_
11	of	_	IN	IN	_	10	prep	_	_
12	2002	_	CD	CD	_	11	pobj	_	_
13	.	_	.	.	_	2	punct	_	_


ID:1207
property subject: Noordeinde
property subject uri: http://dbpedia.org/resource/Noordeinde,_Nieuwkoop
property object: 561
property object uri:
561^^http://www.w3.org/2001/XMLSchema#nonNegativeInteger
sentence::
1	In	_	IN	IN	_	8	prep	_	_
2	2001	_	CD	CD	_	1	pobj	_	_
3	,	_	,	,	_	8	punct	_	_
4	the	_	DT	DT	_	5	det	_	_
5	town	_	NN	NN	_	8	nsubj	_	_
6	of	_	IN	IN	_	5	prep	_	_
7	Noordeinde	_	NNP	NNP	_	6	pobj	_	_
8	had	_	VBD	VBD	_	0	null	_	_
9	561	_	CD	CD	_	10	num	_	_
10	inhabitants	_	NNS	NNS	_	8	dobj	_	_
11	.	_	.	.	_	8	punct	_	_


    */
    
    @Override
    public String getQuery() {
        String query = "SELECT ?form ?e1_arg ?e2_arg WHERE {"
                + "?e1 <conll:head> ?verb . "
                + "{?e1 <conll:deprel> \"pobj\".} UNION {?e1 <conll:deprel> \"nsubj\".}"
                + "?verb <conll:cpostag> ?verb_pos. "
                + "FILTER regex(?verb_pos, \"VB\") ."
                + "?noun <conll:head> ?verb . "
                + "?noun <conll:form> ?form. "
                + "{?noun <conll:deprel> \"pobj\".} UNION {?noun <conll:deprel> \"dobj\".}"
                + "?e2 <conll:deprel> \"num\". "
                + "?e2 <conll:head> ?noun. "
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

        while ( rs.hasNext() ) {
                QuerySolution qs = rs.next();
                try{
                        noun = qs.get("?form").toString();
                        e1_arg = qs.get("?e1_arg").toString();
                        e2_arg = qs.get("?e2_arg").toString();	

                 }
                catch(Exception e){
               e.printStackTrace();
               }
            }

        qExec.close() ;

        if(noun!=null && e1_arg!=null && e2_arg!=null) {
            Sentence sentence = this.returnSentence(model);
            Templates.getNounPossessive(model, lexicon, sentence, noun, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
        } 
                
    }
    
}
