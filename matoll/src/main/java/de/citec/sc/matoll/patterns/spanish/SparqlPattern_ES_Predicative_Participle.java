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

public class SparqlPattern_ES_Predicative_Participle_WithoutCopulative extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Predicative_Participle_WithoutCopulative.class.getName());
	
	

//	ID:25
//	property subject: Antrim
//	property subject uri: http://dbpedia.org/resource/Antrim,_County_Antrim
//	property object: Condado de Antrim
//	property object uri: http://dbpedia.org/resource/County_Antrim
//	sentence:: 
//	1	Históricamente	históricamente	r	RG	_	5	MOD	_	_
//	2	,	,	f	Fc	_	1	punct	_	_
//	3	el	el	d	DA0MS0	_	4	SPEC	_	_
//	4	Castillo_Ballylough	castillo_ballylough	n	NP00000	_	5	SUBJ	_	_
//	5	estaba	estar	v	VAII1S0	_	0	ROOT	_	_
//	6	ubicado	ubicar	v	VMP00SM	_	5	ATR	_	_
//	7	en	en	s	SPS00	_	6	PP-LOC	_	_
//	8	el	el	d	DA0MS0	_	9	SPEC	_	_
//	9	poblado	poblado	n	NCMS000	_	7	COMP	_	_
//	10	Billy	billy	n	NP00000	_	9	MOD	_	_
//	11	en	en	s	SPS00	_	6	MOD	_	_
//	12	el	el	d	DA0MS0	_	13	SPEC	_	_
//	13	condado	condado	n	NCMS000	_	11	COMP	_	_
//	14	de	de	s	SPS00	_	13	MOD	_	_
//	15	Antrim	antrim	n	NP00000	_	14	COMP	_	_
//	16	.	.	f	Fp	_	15	punct	_	_

//	ID:50
//	property subject: BlackBerry OS
//	property subject uri: http://dbpedia.org/resource/BlackBerry_OS
//	property object: BlackBerry
//	property object uri: http://dbpedia.org/resource/BlackBerry_Limited
//	sentence:: 
//	1	RIM	rim	n	NP00000	_	2	SUBJ	_	_
//	2	apuesta	apostar	v	VMIP3S0	_	0	ROOT	_	_
//	3	que	que	c	CS	_	2	DO	_	_
//	4	su	su	d	DP3CS0	_	5	SPEC	_	_
//	5	BlackBerry	blackberry	n	NP00000	_	7	SUBJ	_	_
//	6	6	6	z	Z	_	5	MOD	_	_
//	7	estará	estar	v	VAIF3S0	_	3	COMP	_	_
//	8	enfocado	enfocar	v	VMP00SM	_	7	ATR	_	_
//	9	en	en	s	SPS00	_	8	MOD	_	_
//	10	el	el	d	DA0MS0	_	11	SPEC	_	_
//	11	mercado	mercado	n	NCMS000	_	9	COMP	_	_
//	12	corporativo	corporativo	a	AQ0MS0	_	11	MOD	_	_
//	13	y	y	c	CC	_	12	COORD	_	_
//	14	no-corporativo	no-corporativo	a	AQ0MS0	_	13	CONJ	_	_
//	15	.	.	f	Fp	_	14	punct	_	_


//	ID:912
//	property subject: ITunes
//	property subject uri: http://dbpedia.org/resource/ITunes
//	property object: Apple
//	property object uri: http://dbpedia.org/resource/Apple_Inc.
//	sentence:: 
//	1	«	«	f	Fz	_	5	MOD	_	_
//	2	Hey_Mama	hey_mama	n	NP00000	_	1	MOD	_	_
//	3	»	»	f	Fz	_	5	SUBJ	_	_
//	4	ha	haber	v	VAIP3S0	_	5	AUX	_	_
//	5	sido	ser	v	VSP00SM	_	0	ROOT	_	_
//	6	utilizado	utilizar	v	VMP00SM	_	5	ATR	_	_
//	7	durante	durante	s	SPS00	_	6	MOD	_	_
//	8	varios	varios	d	DI0MP0	_	9	SPEC	_	_
//	9	anuncios	anuncio	n	NCMP000	_	7	COMP	_	_
//	10	incluyendo	incluir	v	VMG0000	_	6	MOD	_	_
//	11	anuncios	anuncio	n	NCMP000	_	10	DO	_	_
//	12	de	de	s	SPS00	_	11	MOD	_	_
//	13	Apple	apple	n	NP00000	_	12	COMP	_	_
//	14	e	y	c	CC	_	13	COORD	_	_
//	15	iTunes	itunes	a	AQ0CP0	_	14	CONJ	_	_
//	16	.	.	f	Fp	_	15	punct	_	_
//	
//	ID:570
//	property subject: Mandriva
//	property subject uri: http://dbpedia.org/resource/Mandriva_Linux
//	property object: Mandriva
//	property object uri: http://dbpedia.org/resource/Mandriva
//	sentence:: 
//	1	El	el	d	DA0MS0	_	2	SPEC	_	_
//	2	desarrollo	desarrollo	n	NCMS000	_	5	SUBJ	_	_
//	3	de	de	s	SPS00	_	2	COMP	_	_
//	4	Ulteo	ulteo	n	NP00000	_	3	COMP	_	_
//	5	está	estar	v	VAIP3S0	_	0	ROOT	_	_
//	6	siendo	ser	v	VSG0000	_	5	COMP	_	_
//	7	liderado	liderar	v	VMP00SM	_	6	ATR	_	_
//	8	por	por	s	SPS00	_	7	BYAG	_	_
//	9	Gaël_Duval	gaël_duval	n	NP00000	_	8	COMP	_	_
//	10	creador	creador	n	NCMS000	_	9	COMP	_	_
//	11	de	de	s	SPS00	_	10	COMP	_	_
//	12	Linux_Mandrake	linux_mandrake	n	NP00000	_	11	COMP	_	_
//	13	(	(	f	Fpa	_	14	punct	_	_
//	14	conocida	conocer	v	VMP00SF	_	0	ROOT	_	_
//	15	actualmente	actualmente	r	RG	_	14	MOD	_	_
//	16	como	como	c	CS	_	14	MOD	_	_
//	17	Linux_Mandriva	linux_mandriva	n	NP00000	_	16	COMP	_	_
//	18	)	)	f	Fpt	_	17	punct	_	_
//	19	y	y	c	CC	_	14	COORD	_	_
//	20	cofundador	cofundador	n	NCMS000	_	19	CONJ	_	_
//	21	de	de	s	SPS00	_	20	MOD	_	_
//	22	Mandriva	mandriva	n	NP00000	_	21	COMP	_	_
//	23	.	.	f	Fp	_	22	punct	_	_
	
	 */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"

			 + "{?auxpass <conll:lemma> \"ser\" .} UNION "
			 + "{?auxpass <conll:lemma> \"estar\" .}"
			 + "?auxpass <conll:cpostag> \"v\" ."
			
			 + "?e1 <conll:head> ?auxpass ."
			 + "?e1 <conll:deprel> \"SUBJ\"."
			
			 + "?participle <conll:lemma> ?lemma ."
			 + "?participle <conll:form> ?form ."
			 + "?participle <conll:head> ?auxpass ."
			 + "?participle <conll:deprel> \"ATR\". "
			 + "?participle <conll:postag> \"VMP00SM\". "
			
			 + "?p <conll:lemma> ?prep ."
			 + "?p <conll:head> ?participle ."
			 + "?p <conll:postag> \"SPS00\"."
			 + "{?p <conll:deprel> \"BYAG\" .} UNION"
			 + "{?p <conll:deprel> \"MOD\". } UNION"
			 + "{?p <conll:deprel> \"PP-LOC\". }
			
			 + "?e2 <conll:head> ?p ."
			 + "{?e2 <conll:deprel> \"COMP\". } UNION "
			 + "{?e2 <conll:deprel> \"MOD\". }"
			
			
			 + "?e1 <own:senseArg> ?e1_arg. "
			 + "?e2 <own:senseArg> ?e2_arg. "
            return query;
        }
                        
	@Override
	public String getID() {
		return "SparqlPattern_ES_Predicative_Participle_WithoutCopulative";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String participle = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String lemma = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 participle = qs.get("?form").toString();
                                 lemma = qs.get("?lemma").toString();
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
                    Sentence sentence = this.returnSentence(model);
                    Templates.getAdjective(model, lexicon, sentence, participle, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                    
                    if (preposition.equals("por"))
                    {
                    	Templates.getTransitiveVerb(model, lexicon, sentence, lemma, e2_arg, e1_arg, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                    }
		
		} 
		
	}

}
