package de.citec.sc.matoll.patterns.spanish;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.lemon.core.Language;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_Predicative_Participle_Passive extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Predicative_Participle_Passive.class.getName());
	
	
//	ID:2
//	property subject: Sodagreen
//	property subject uri: http://dbpedia.org/resource/Sodagreen
//	property object: Sodagreen
//	property object uri: http://dbpedia.org/resource/Sodagreen
//	sentence:: 
//	1	Sodagreen	sodagreen	n	NP00000	_	2	SUBJ	_	_
//	2	fue	ser	v	VSIS3S0	_	0	ROOT	_	_
//	3	nombrado	nombrar	v	VMP00SM	_	2	ATR	_	_
//	4	por	por	s	SPS00	_	3	BYAG	_	_
//	5	Shih	shih	n	NP00000	_	4	COMP	_	_
//	6	,	,	f	Fc	_	5	punct	_	_
//	7	entonces	entonces	r	RG	_	8	MOD	_	_
//	8	líder	líder	n	NCCS000	_	2	MOD	_	_
//	9	de	de	s	SPS00	_	8	MOD	_	_
//	10	la	el	d	DA0FS0	_	11	SPEC	_	_
//	11	banda	banda	n	NCFS000	_	9	COMP	_	_
//	12	y	y	c	CC	_	11	COORD	_	_
//	13	Wu	wu	n	NP00000	_	12	CONJ	_	_
//	14	,	,	f	Fc	_	13	punct	_	_
//	15	el	el	d	DA0MS0	_	16	SPEC	_	_
//	16	vocalista	vocalista	n	NCCS000	_	2	ATR	_	_
//	17	.	.	f	Fp	_	16	punct	_	_
//
//	ID:318
//	property subject: Rage
//	property subject uri: http://dbpedia.org/resource/Rage_(video_game)
//	property object: Id Software
//	property object uri: http://dbpedia.org/resource/Id_Software
//	sentence:: 
//	1	"	"	f	Fe	_	4	MOD	_	_
//	2	Rage	rage	n	NP00000	_	1	MOD	_	_
//	3	"	"	f	Fe	_	4	SUBJ	_	_
//	4	fue	ser	v	VSIS3S0	_	0	ROOT	_	_
//	5	desarrollado	desarrollar	v	VMP00SM	_	4	ATR	_	_
//	6	por	por	s	SPS00	_	5	BYAG	_	_
//	7	"	"	f	Fe	_	6	COMP	_	_
//	8	id	ir	v	VMM02P0	_	9	SPEC	_	_
//	9	Software	software	n	NP00000	_	4	SUBJ	_	_
//	10	"	"	f	Fe	_	9	punct	_	_
//	11	y	y	c	CC	_	9	COORD	_	_
//	12	distribuido	distribuir	v	VMP00SM	_	11	CONJ	_	_
//	13	por	por	s	SPS00	_	12	MOD	_	_
//	14	"	"	f	Fe	_	13	COMP	_	_
//	15	Bethesda_Softworks	bethesda_softworks	n	NP00000	_	4	MOD	_	_
//	16	"	"	f	Fe	_	15	punct	_	_
//	17	.	.	f	Fp	_	15	punct	_	_
//

//	ID:2
//	property subject: Sodagreen
//	property subject uri: http://dbpedia.org/resource/Sodagreen
//	property object: Sodagreen
//	property object uri: http://dbpedia.org/resource/Sodagreen
//	sentence:: 
//	1	Sodagreen	sodagreen	n	NP00000	_	2	SUBJ	_	_
//	2	fue	ser	v	VSIS3S0	_	0	ROOT	_	_
//	3	nombrado	nombrar	v	VMP00SM	_	2	ATR	_	_
//	4	por	por	s	SPS00	_	3	BYAG	_	_
//	5	Shih	shih	n	NP00000	_	4	COMP	_	_
//	6	,	,	f	Fc	_	5	punct	_	_
//	7	entonces	entonces	r	RG	_	8	MOD	_	_
//	8	líder	líder	n	NCCS000	_	2	MOD	_	_
//	9	de	de	s	SPS00	_	8	MOD	_	_
//	10	la	el	d	DA0FS0	_	11	SPEC	_	_
//	11	banda	banda	n	NCFS000	_	9	COMP	_	_
//	12	y	y	c	CC	_	11	COORD	_	_
//	13	Wu	wu	n	NP00000	_	12	CONJ	_	_
//	14	,	,	f	Fc	_	13	punct	_	_
//	15	el	el	d	DA0MS0	_	16	SPEC	_	_
//	16	vocalista	vocalista	n	NCCS000	_	2	ATR	_	_
//	17	.	.	f	Fp	_	16	punct	_	_

	
//	ID:577
//	property subject: SimCity
//	property subject uri: http://dbpedia.org/resource/SimCity_(1989_video_game)
//	property object: Maxis
//	property object uri: http://dbpedia.org/resource/Maxis
//	sentence:: 
//	1	A_el_igual_que	al_igual_que	c	CS	_	0	ROOT	_	_
//	2	los	el	d	DA0MP0	_	3	SPEC	_	_
//	3	títulos	título	n	NCMP000	_	11	SUBJ	_	_
//	4	anteriores	anterior	a	AQ0CP0	_	3	MOD	_	_
//	5	(	(	f	Fpa	_	6	punct	_	_
//	6	a_excepción_de	a_excepción_de	s	SPS00	_	3	MOD	_	_
//	7	SimCity_Societies	simcity_societies	n	NP00000	_	6	COMP	_	_
//	8	)	)	f	Fpt	_	7	punct	_	_
//	9	,	,	f	Fc	_	7	punct	_	_
//	10	SimCity	simcity	n	NP00000	_	7	MOD	_	_
//	11	será	ser	v	VSIF3S0	_	1	COMP	_	_
//	12	desarrollado	desarrollar	v	VMP00SM	_	11	ATR	_	_
//	13	por	por	s	SPS00	_	12	BYAG	_	_
//	14	Maxis	maxis	n	NP00000	_	13	COMP	_	_
//	15	,	,	f	Fc	_	14	punct	_	_
//	16	una	uno	d	DI0FS0	_	17	SPEC	_	_
//	17	subsidiaria	subsidiaria	n	NCFS000	_	0	ROOT	_	_
//	18	de	de	s	SPS00	_	17	MOD	_	_
//	19	Electronic_Arts.	electronic_arts.	n	NP00000	_	18	COMP	_	_
//	
	
//	ID:238
//	property subject: MS-DOS
//	property subject uri: http://dbpedia.org/resource/MS-DOS
//	property object: Microsoft
//	property object uri: http://dbpedia.org/resource/Microsoft
//	sentence:: 
//	1	Aunque	aunque	c	CC	_	0	ROOT	_	_
//	2	MS-DOS	ms-dos	v	VMP00PM	_	1	_	_	_
//	3	y	y	c	CC	_	2	COORD	_	_
//	4	PC-DOS	pc-dos	n	NP00000	_	3	CONJ	_	_
//	5	fueron	ser	v	VSIS3P0	_	17	MOD	_	_
//	6	desarrollados	desarrollar	v	VMP00PM	_	5	ATR	_	_
//	7	por	por	s	SPS00	_	6	BYAG	_	_
//	8	Microsoft	microsoft	n	NP00000	_	7	COMP	_	_
//	9	e	y	c	CC	_	8	COORD	_	_
//	10	IBM	ibm	n	NP00000	_	9	CONJ	_	_
//	11	en_paralelo	en_paralelo	r	RG	_	6	MOD	_	_
//	12	,	,	f	Fc	_	11	punct	_	_
//	13	los	el	d	DA0MP0	_	15	SPEC	_	_
//	14	dos	2	z	Z	_	15	MOD	_	_
//	15	productos	producto	n	NCMP000	_	17	SUBJ	_	_
//	16	se	se	p	P00CN000	_	15	MOD	_	_
//	17	separaron	separar	v	VMIS3P0	_	0	ROOT	_	_
//	18	con	con	s	SPS00	_	17	OBLC	_	_
//	19	el	el	d	DA0MS0	_	20	SPEC	_	_
//	20	tiempo	tiempo	n	NCMS000	_	18	COMP	_	_
//	21	.	.	f	Fp	_	20	punct	_	_

//	ID:318
//	property subject: Rage
//	property subject uri: http://dbpedia.org/resource/Rage_(video_game)
//	property object: Id Software
//	property object uri: http://dbpedia.org/resource/Id_Software
//	sentence:: 
//	1	"	"	f	Fe	_	4	MOD	_	_
//	2	Rage	rage	n	NP00000	_	1	MOD	_	_
//	3	"	"	f	Fe	_	4	SUBJ	_	_
//	4	fue	ser	v	VSIS3S0	_	0	ROOT	_	_
//	5	desarrollado	desarrollar	v	VMP00SM	_	4	ATR	_	_
//	6	por	por	s	SPS00	_	5	BYAG	_	_
//	7	"	"	f	Fe	_	6	COMP	_	_
//	8	id	ir	v	VMM02P0	_	9	SPEC	_	_
//	9	Software	software	n	NP00000	_	4	SUBJ	_	_
//	10	"	"	f	Fe	_	9	punct	_	_
//	11	y	y	c	CC	_	9	COORD	_	_
//	12	distribuido	distribuir	v	VMP00SM	_	11	CONJ	_	_
//	13	por	por	s	SPS00	_	12	MOD	_	_
//	14	"	"	f	Fe	_	13	COMP	_	_
//	15	Bethesda_Softworks	bethesda_softworks	n	NP00000	_	4	MOD	_	_
//	16	"	"	f	Fe	_	15	punct	_	_
//	17	.	.	f	Fp	_	15	punct	_	_


	
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?form ?prep ?e1_arg ?e2_arg  WHERE {"

				 + "?auxpass <conll:lemma> \"ser\" ."
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
				 + "{?p <conll:deprel> \"MOD\". }"
				
				 + "?e2 <conll:head> ?p ."
				 + "{?e2 <conll:deprel> \"COMP\". } UNION "
				 + "{?e2 <conll:deprel> \"MOD\". }"

				 + "?e1 <own:senseArg> ?e1_arg. "
				 + "?e2 <own:senseArg> ?e2_arg. "
            		
                            + "}";
            return query;
        }
			
	@Override
	public String getID() {
		return "SparqlPattern_ES_Predicative_Participle_Passive";
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

                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();

                    try{
                        e1_arg = qs.get("?e1_arg").toString();
                        e2_arg = qs.get("?e2_arg").toString();
                        lemma = qs.get("?lemma").toString();
                        participle = qs.get("?form").toString();
                        preposition = qs.get("?prep").toString();
                        if(participle!=null && e1_arg!=null && e2_arg!=null) {
                            Sentence sentence = this.returnSentence(model);
                            Templates.getAdjective(model, lexicon, sentence, participle, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());

                            if (preposition!=null)
                            {   if(preposition.equals("por"))
                                Templates.getTransitiveVerb(model, lexicon, sentence, lemma, e2_arg, e1_arg, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                            }
                        }
                    }
                    catch(Exception e){
                   e.printStackTrace();
                   }
                }

                qExec.close() ;
    

		
	}

}
