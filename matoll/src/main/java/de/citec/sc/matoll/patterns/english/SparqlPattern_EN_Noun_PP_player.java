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
public class SparqlPattern_EN_Noun_PP_player extends SparqlPattern{
    
    Logger logger = LogManager.getLogger(SparqlPattern_EN_Noun_PP_player.class.getName());
    @Override
    public String getID() {
        return "SparqlPattern_EN_Noun_PP_player";
    }

    /*


ID:81
property subject: Itimi Dickson
property subject uri: http://dbpedia.org/resource/Itimi_Dickson
property object: Persidafon Dafonsoro
property object uri: http://dbpedia.org/resource/Persidafon_Dafonsoro
sentence::
1 Itimi _ NNP NNP _ 3 nn _ _
2 Dickson _ NNP NNP _ 3 nn _ _
3 Edherefe _ NNP NNP _ 9 nsubj _ _
4 is _ VBZ VBZ _ 9 cop _ _
5 a _ DT DT _ 9 det _ _
6 Singapore _ NNP NNP _ 9 nn _ _
7 international _ JJ JJ _ 9 amod _ _
8 football _ NN NN _ 9 nn _ _
9 player _ NN NN _ 0 null _ _
10 who _ WP WP _ 12 nsubj _ _
11 currently _ RB RB _ 12 advmod _ _
12 plays _ VBZ VBZ _ 9 rcmod _ _
13 for _ IN IN _ 12 prep _ _
14 Persidafon _ NNP NNP _ 15 nn _ _
15 Dafonsoro _ NNP NNP _ 13 pobj _ _
16 . _ . . _ 9 punct _ _

ID:107
property subject: Rob Evans
property subject uri: http://dbpedia.org/resource/Rob_Evans_(rugby_player)
property object: Scarlets
property object uri: http://dbpedia.org/resource/Scarlets
sentence::
1 Rob _ NNP NNP _ 2 nn _ _
2 Evans _ NNP NNP _ 8 nsubj _ _
3 is _ VBZ VBZ _ 8 cop _ _
4 a _ DT DT _ 8 det _ _
5 Welsh _ NNP NNP _ 8 nn _ _
6 rugby _ JJ JJ _ 8 amod _ _
7 union _ NN NN _ 8 nn _ _
8 player _ NN NN _ 0 null _ _
9 , _ , , _ 8 punct _ _
10 currently _ RB RB _ 11 advmod _ _
11 playing _ VBG VBG _ 8 xcomp _ _
12 for _ IN IN _ 11 prep _ _
13 Scarlets _ NNS NNS _ 12 pobj _ _
14 . _ . . _ 8 punct _ _



    */
    
    @Override
    public String getQuery() {
        String query = "SELECT ?form ?prep ?e1_arg ?e2_arg WHERE {"
                + "?e1 <conll:head> ?noun . "
                + "?e1 <conll:deprel> \"nsubj\"."
                + "?noun <conll:form> ?form. "
                + "?noun <conll:cpostag> \"NN\". "
                + "?verb <conll:head> ?noun. "
                + "?verb <conll:cpostag> ?verb_pos. "
                + "FILTER regex(?verb_pos, \"VB\") ."
                + "?p <conll:deprel> \"prep\" . "
                + "?p <conll:head> ?verb. "
                + "?p <conll:form> ?prep. "
                + "?e2 <conll:deprel> \"pobj\". "
                + "?e2 <conll:head> ?p. "
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
