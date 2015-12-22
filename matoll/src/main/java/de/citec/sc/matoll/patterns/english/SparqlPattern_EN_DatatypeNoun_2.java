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
public class SparqlPattern_EN_DatatypeNoun_2 extends SparqlPattern{
    
    Logger logger = LogManager.getLogger(SparqlPattern_EN_DatatypeNoun_2.class.getName());
    @Override
    public String getID() {
        return "SparqlPattern_EN_DatatypeNoun_2";
    }

    /*
    

    ID:2612
    property subject: Lingampet
    property subject uri: http://dbpedia.org/resource/Lingampet
    property object: 470
    property object uri: 470^^http://www.w3.org/2001/XMLSchema#integer
    sentence::
    1 Lingampet _ NNP NNP _ 2 nsubj _ _
    2 has _ VBZ VBZ _ 0 null _ _
    3 an _ DT DT _ 5 det _ _
    4 average _ JJ JJ _ 5 amod _ _
    5 elevation _ NN NN _ 2 dobj _ _
    6 of _ IN IN _ 5 prep _ _
    7 470 _ CD CD _ 8 num _ _
    8 meters _ NNS NNS _ 6 pobj _ _
    9 -LRB- _ -LRB- -LRB- _ 8 dep _ _
    10 1545 _ CD CD _ 11 num _ _
    11 feet _ NNS NNS _ 12 measure _ _
    12 -RRB- _ -RRB- -RRB- _ 9 dep _ _
    13 . _ . . _ 2 punct _ _

    */
    
    @Override
    public String getQuery() {
        String query = "SELECT ?form ?prep ?e1_arg ?e2_arg WHERE {"
                + "?e1 <conll:head> ?verb . "
                + "?e1 <conll:deprel> \"nsubj\"."
                + "?verb <conll:cpostag> ?verb_pos. "
                + "FILTER regex(?verb_pos, \"VB\") ."
                + "?noun <conll:head> ?verb . "
                + "?noun <conll:form> ?form. "
                + "?noun <conll:deprel> \"dobj\". "
                + "?p <conll:deprel> \"prep\" . "
                + "?p <conll:head> ?noun. "
                + "?p <conll:form> ?prep. "
                + "?unit <conll:head> ?p. "
                + "{?unit <conll:deprel> \"pobj\".} UNION {?unit <conll:deprel> \"dep\". } "
                + "?e2 <conll:deprel> \"num\". "
                + "?e2 <conll:head> ?unit. "
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

        while ( rs.hasNext() ) {
                QuerySolution qs = rs.next();
                try{
                        noun = qs.get("?form").toString();
                        e1_arg = qs.get("?e1_arg").toString();
                        e2_arg = qs.get("?e2_arg").toString();	
                        preposition = qs.get("?prep").toString();
                        if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                            Sentence sentence = this.returnSentence(model);
                            Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg,preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                        }

                }
                catch(Exception e){
               e.printStackTrace();
               }
            }

        qExec.close() ;


                
    }
    
}
