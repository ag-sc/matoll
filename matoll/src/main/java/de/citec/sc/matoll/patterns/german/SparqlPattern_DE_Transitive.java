package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_Transitive extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Transitive.class.getName());
	
        /*
        ID:1111
property subject: Rita Hayworth
property subject uri: http://dbpedia.org/resource/Rita_Hayworth
property object: James Hill
property object uri: http://dbpedia.org/resource/James_Hill_(film_producer)
sentence:: 
1	James	James	N	NE	_|Nom|Sg	3	subj	_	_ 
2	Hill	Hill	N	NE	_|Nom|Sg	1	app	_	_ 
3	schlug	schlagen	V	VVFIN	3|Sg|Past|Ind	0	root	_	_ 
4	daraufhin	daraufhin	PAV	PAV	_	3	pp	_	_ 
5	Rita	Rita	N	NE	_|_|_	3	obja	_	_ 
6	Hayworth	Hayworth	N	NE	_|_|_	5	app	_	_ 
7	für	für	N	NN	_|_|_	6	app	_	_ 
8	Leighs	Leigh	N	NN	Masc|Gen|Sg	9	gmod	_	_ 
9	Rolle	Rolle	N	NN	_|_|_	7	app	_	_ 
10	vor	vor	PTKVZ	PTKVZ	_	3	avz	_	_ 
11	.	.	$.	$.	_	0	root	_	_ 

        */
        /*
        Transitive
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?additional_lemma ?avz ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?verb. "
                            + "?verb <conll:lemma> ?lemma . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "{?e2 <conll:deprel> \"obja\" . }"
                            + "UNION"
                            + "{?e2 <conll:deprel> \"objd\" . }"
                            + "?e2 <conll:head> ?verb. "
                            + "OPTIONAL "
                            + "{?lemma_addition <conll:head> ?verb . "
                            + "?lemma_addidtion <conll:deprel> \"obji\". "
                            + "?lemma_addidtion <conll:lemma> ?additional_lemma . }"
                            + " OPTIONAL "
                            + "{?avz_addidtion <conll:deprel> \"avz\". "
                            + "?avz_addidtion <conll:head> ?verb . "
                            + "?avz_addidtion <conll:form> ?avz . "
                            + "} "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SparqlPattern_DE_Transitive";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String additional_lemma = null;
                String avz = null;

                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();


                    try{
                            verb = qs.get("?lemma").toString();
                            e1_arg = qs.get("?e1_arg").toString();
                            e2_arg = qs.get("?e2_arg").toString();
                            
                            try{
                                additional_lemma = qs.get("?additional_lemma").toString();
                            }
                            catch (Exception e){}
                            try{
                                avz = qs.get("?avz").toString();
                            }
                            catch (Exception e){}
                            if(verb!=null && e1_arg!=null && e2_arg!=null) {
                                Sentence sentence = this.returnSentence(model);
                                if(additional_lemma!=null){
                                    Templates.getTransitiveVerb(model, lexicon, sentence, additional_lemma +" "+verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                                }
                                if(avz!=null){
                                    Templates.getTransitiveVerb(model, lexicon, sentence, avz+verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                                }

                                if(avz==null && additional_lemma==null)
                                    Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                            }
                    }
                    catch(Exception e){
                   e.printStackTrace();
                   }
                }

                qExec.close() ;

	}

}
