package de.citec.sc.matoll.patterns.spanish;

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

public class SparqlPattern_ES_Noun_PP_copulative extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Noun_PP_copulative.class.getName());
	
	
	/*
	 * 
propSubject:flickr
propObject:ludicorp
--------------
sentence:Ludicorp es la empresa creadora de Flickr , sitio web de organizacin de fotografas digitales y red social . 
1	Ludicorp	ludicorp	n	NP00000	_	2	SUBJ
2	es	ser	v	VSIP3S0	_	0	ROOT
3	la	el	d	DA0FS0	_	4	SPEC
4	empresa	empresa	n	NCFS000	_	2	ATR
5	creadora	creador	a	AQ0FS0	_	4	MOD
6	de	de	s	SPS00	_	4	MOD
7	Flickr	flickr	n	NP00000	_	6	COMP
	 */
	
	
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?adjective_lemma ?adjective_wordnumber ?lemma_wordnumber ?e1_arg ?e2_arg ?prep  WHERE {"
                            + " OPTIONAL {"
                                 + "?adjective <conll:form> ?adjective_lemma . "
                                 + "?adjective <conll:wordnumber> ?adjective_wordnumber ."
                                 + "?adjective <conll:head> ?noun . "
                                 + "?adjective <conll:deprel> \"MOD\" . "
                                 + "?adjective <conll:cpostag> \"a\".  }"
                            + "?noun <conll:head> ?verb. "
                            + "?noun <conll:deprel> \"ATR\". "
                            + "?noun <conll:lemma> ?lemma ."
                            + "?noun <conll:wordnumber> ?lemma_wordnumber ."
                            + "?verb <conll:postag> ?verb_pos . "
                            + "?verb <conll:lemma> \"ser\" . "
                            + "FILTER regex(?verb_pos, \"VS\") ."
                            + "?e1 <conll:head> ?verb . "
                            + "?e1 <conll:deprel> \"SUBJ\". "
                            + "?p <conll:head> ?noun . "
                            + "?p <conll:deprel> \"MOD\". "
                            + "?p <conll:cpostag> \"s\". "
                            + "?p <conll:lemma> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:cpostag> \"n\" . "
                            + "?e2 <conll:deprel> \"COMP\" . "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	@Override
	public String getID() {
		return "SparqlPattern_ES_Noun_PP_copulative";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String adjective_lemma = null;
                int adjective_wordnumber = 0;
                int lemma_wordnumber = 0;
                
                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();


                    try{
                            noun = qs.get("?lemma").toString();
                            e1_arg = qs.get("?e1_arg").toString();
                            e2_arg = qs.get("?e2_arg").toString();	
                            preposition = qs.get("?prep").toString();
                            try{
                                adjective_lemma = qs.get("?adjective_lemma").toString();
                                adjective_wordnumber = Integer.getInteger(qs.get("?adjective_wordnumber").toString());
                                lemma_wordnumber = Integer.getInteger(qs.get("?lemma_wordnumber").toString());
                            }
                            catch(Exception e){};
                            if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                                Sentence sentence = this.returnSentence(model);
                                if(adjective_lemma!=null)
                                    if(lemma_wordnumber<adjective_wordnumber)
                                        Templates.getNounWithPrep(model, lexicon, sentence, noun+" "+adjective_lemma, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                                    else
                                        Templates.getNounWithPrep(model, lexicon, sentence, adjective_lemma+" "+noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                                else
                                    Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                            }
                    }
                    catch(Exception e){
                   e.printStackTrace();
                   }
                }

                qExec.close() ;
    

		
	}

}
