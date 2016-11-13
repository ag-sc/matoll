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

public class SparqlPattern_ES_Predicative_Participle_Copulative extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Predicative_Participle_Copulative.class.getName());
	

//	D:555
//	property subject: El Estado y la revolución
//	property subject uri: http://dbpedia.org/resource/The_State_and_Revolution
//	property object: Lenin
//	property object uri: http://dbpedia.org/resource/Vladimir_Lenin
//	sentence:: 
//	1	El	el	d	DA0MS0	_	2	SPEC	_	_
//	2	Estado	estado	n	NP00000	_	6	SUBJ	_	_
//	3	y	y	c	CC	_	2	COORD	_	_
//	4	la	el	d	DA0FS0	_	5	SPEC	_	_
//	5	revolución	revolución	n	NCFS000	_	3	CONJ	_	_
//	6	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	7	un	uno	d	DI0MS0	_	8	SPEC	_	_
//	8	libro	libro	n	NCMS000	_	6	ATR	_	_
//	9	escrito	escribir	v	VMP00SM	_	8	MOD	_	_
//	10	por	por	s	SPS00	_	9	BYAG	_	_
//	11	Lenin	lenin	n	NP00000	_	10	COMP	_	_
//	12	entre	entre	s	SPS00	_	9	MOD	_	_
//	13	agosto	[??:??/8/??:??.??:??]	w	W	_	12	COMP	_	_
//	14	y	y	c	CC	_	13	COORD	_	_
//	15	septiembre_de_1917	[??:??/9/1917:??.??:??]	w	W	_	14	CONJ	_	_
//	16	,	,	f	Fc	_	15	punct	_	_
//	17	mientras	mientras	c	CS	_	6	MOD	_	_
//	18	se	se	p	P00CN000	_	19	MOD	_	_
//	19	encontraba	encontrar	v	VMII3S0	_	17	COMP	_	_
//	20	en	en	s	SPS00	_	19	PP-LOC	_	_
//	21	la	el	d	DA0FS0	_	22	SPEC	_	_
//	22	clandestinidad	clandestinidad	n	NCFS000	_	20	COMP	_	_
//	23	en	en	s	SPS00	_	22	MOD	_	_
//	24	Finlandia	finlandia	n	NP00000	_	23	COMP	_	_
//	25	.	.	f	Fp	_	24	punct	_	_
//
//	ID:5
//	property subject: La vida secreta de las plantas
//	property subject uri: http://dbpedia.org/resource/The_Secret_Life_of_Plants
//	property object: La vida secreta de las plantas
//	property object uri: http://dbpedia.org/resource/The_Secret_Life_of_Plants
//	sentence:: 
//	1	La	el	d	DA0FS0	_	2	SPEC	_	_
//	2	vida	vida	n	NCFS000	_	8	SUBJ	_	_
//	3	secreta	secreto	a	AQ0FS0	_	2	MOD	_	_
//	4	de	de	s	SPS00	_	2	COMP	_	_
//	5	las	el	d	DA0FP0	_	6	SPEC	_	_
//	6	plantas	planta	n	NCFP000	_	4	COMP	_	_
//	7	,	,	f	Fc	_	6	punct	_	_
//	8	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	9	un	uno	d	DI0MS0	_	10	SPEC	_	_
//	10	libro	libro	n	NCMS000	_	8	ATR	_	_
//	11	escrito	escribir	v	VMP00SM	_	10	MOD	_	_
//	12	por	por	s	SPS00	_	11	BYAG	_	_
//	13	Peter_Tompkins	peter_tompkins	n	NP00000	_	12	COMP	_	_
//	14	y	y	c	CC	_	13	COORD	_	_
//	15	Christopher_Bird	christopher_bird	n	NP00000	_	14	CONJ	_	_
//	16	.	.	f	Fp	_	15	punct	_	_
//	
//	ID:133
//	property subject: Adenet Le Roi
//	property subject uri: http://dbpedia.org/resource/Adenes_Le_Roi
//	property object: 1240
//	property object uri: 1240-01-01^^http://www.w3.org/2001/XMLSchema#gYear
//	sentence:: 
//	1	Adenet	adenet	n	NP00000	_	3	SUBJ	_	_
//	2	le	le	p	PP3CSD00	_	3	IO	_	_
//	3	Roi	roi	n	NP00000	_	4	SUBJ	_	_
//	4	fue	ser	v	VSIS3S0	_	0	ROOT	_	_
//	5	un	uno	d	DI0MS0	_	6	SPEC	_	_
//	6	juglar	juglar	n	NCMS000	_	4	ATR	_	_
//	7	nacido	nacer	v	VMP00SM	_	6	MOD	_	_
//	8	hacia	hacia	s	SPS00	_	7	MOD	_	_
//	9	1240	1240	z	Z	_	8	COMP	_	_
//	10	y	y	c	CC	_	9	COORD	_	_
//	11	muerto	morir	v	VMP00SM	_	10	CONJ	_	_
//	12	hacia	hacia	s	SPS00	_	11	MOD	_	_
//	13	1300	1300	z	Z	_	12	COMP	_	_
//	14	.	.	f	Fp	_	13	punct	_	_
//	

//	ID:38
//	property subject: Antrim
//	property subject uri: http://dbpedia.org/resource/Antrim,_County_Antrim
//	property object: Condado de Antrim
//	property object uri: http://dbpedia.org/resource/County_Antrim
//	sentence:: 
//	1	El	el	d	DA0MS0	_	4	SPEC	_	_
//	2	Carrick-a-rede	carrick-a-rede	n	NP00000	_	5	SUBJ	_	_
//	3	rope	rope	n	NCMS000	_	2	COMP	_	_
//	4	bridge	bridge	n	NCMS000	_	3	COMP	_	_
//	5	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	6	un	uno	d	DI0MS0	_	7	SPEC	_	_
//	7	puente	puente	n	NCMS000	_	5	ATR	_	_
//	8	colgante	colgante	a	AQ0CS0	_	7	MOD	_	_
//	9	situado	situar	v	VMP00SM	_	7	MOD	_	_
//	10	en_las_cercanías_de	en_las_cercanías_de	s	SPS00	_	9	MOD	_	_
//	11	Ballintoy	ballintoy	n	NP00000	_	10	COMP	_	_
//	12	,	,	f	Fc	_	11	punct	_	_
//	13	condado	condado	n	NCMS000	_	11	MOD	_	_
//	14	de	de	s	SPS00	_	13	MOD	_	_
//	15	Antrim	antrim	n	NP00000	_	14	COMP	_	_
//	16	,	,	f	Fc	_	15	punct	_	_
//	17	Irlanda_de_el_Norte	irlanda_de_el_norte	n	NP00000	_	15	MOD	_	_
//	18	.	.	f	Fp	_	17	punct	_	_
//
//	ID:8
//	property subject: Dart
//	property subject uri: http://dbpedia.org/resource/Dart_(programming_language)
//	property object: Google
//	property object uri: http://dbpedia.org/resource/Google
//	sentence:: 
//	1	DartEditor	darteditor	n	NP00000	_	2	SUBJ	_	_
//	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	3	el	el	d	DA0MS0	_	5	SPEC	_	_
//	4	primer	1	a	AO0MS0	_	5	MOD	_	_
//	5	editor	editor	n	NCMS000	_	2	ATR	_	_
//	6	lanzado	lanzar	v	VMP00SM	_	5	MOD	_	_
//	7	por	por	s	SPS00	_	6	BYAG	_	_
//	8	Google	google	n	NP00000	_	7	COMP	_	_
//	9	(	(	f	Fpa	_	10	punct	_	_
//	10	noviembre_de_2011	[??:??/11/2011:??.??:??]	w	W	_	6	MOD	_	_
//	11	)	)	f	Fpt	_	10	punct	_	_
//	12	para	para	s	SPS00	_	5	MOD	_	_
//	13	escribir	escribir	v	VMN0000	_	12	COMP	_	_
//	14	aplicaciones	aplicación	n	NCFP000	_	13	DO	_	_
//	15	Dart	dart	n	NP00000	_	14	MOD	_	_
//	16	.	.	f	Fp	_	15	punct	_	_

//	ID:37
//	property subject: BlackBerry OS
//	property subject uri: http://dbpedia.org/resource/BlackBerry_OS
//	property object: BlackBerry
//	property object uri: http://dbpedia.org/resource/BlackBerry_Limited
//	sentence:: 
//	1	El	el	d	DA0MS0	_	2	SPEC	_	_
//	2	BlackBerry_Q10	blackberry_q10	n	NP00000	_	3	SUBJ	_	_
//	3	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	4	un	uno	d	DI0MS0	_	5	SPEC	_	_
//	5	teléfono	teléfono	n	NCMS000	_	3	ATR	_	_
//	6	inteligente	inteligente	a	AQ0CS0	_	5	MOD	_	_
//	7	,	,	f	Fc	_	6	punct	_	_
//	8	fabricado	fabricar	v	VMP00SM	_	5	MOD	_	_
//	9	por	por	s	SPS00	_	8	BYAG	_	_
//	10	la	el	d	DA0FS0	_	11	SPEC	_	_
//	11	compañía	compañía	n	NCFS000	_	9	COMP	_	_
//	12	canadiense	canadiense	a	AQ0CS0	_	11	MOD	_	_
//	13	Blackberry	blackberry	n	NP00000	_	11	MOD	_	_
//	14	.	.	f	Fp	_	13	punct	_	_

//	ID:275
//	property subject: RPG
//	property subject uri: http://dbpedia.org/resource/IBM_RPG
//	property object: IBM
//	property object uri: http://dbpedia.org/resource/IBM
//	sentence:: 
//	1	El	el	d	DA0MS0	_	2	SPEC	_	_
//	2	lenguaje	lenguaje	n	NCMS000	_	6	SUBJ	_	_
//	3	de	de	s	SPS00	_	2	MOD	_	_
//	4	programación	programación	n	NCFS000	_	3	COMP	_	_
//	5	RPG	rpg	n	NP00000	_	2	MOD	_	_
//	6	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	7	un	uno	d	DI0MS0	_	8	SPEC	_	_
//	8	lenguaje	lenguaje	n	NCMS000	_	6	ATR	_	_
//	9	de	de	s	SPS00	_	8	MOD	_	_
//	10	programación	programación	n	NCFS000	_	9	COMP	_	_
//	11	desarrollado	desarrollar	v	VMP00SM	_	8	MOD	_	_
//	12	por	por	s	SPS00	_	11	BYAG	_	_
//	13	IBM	ibm	n	NP00000	_	12	COMP	_	_
//	14	en	en	s	SPS00	_	11	MOD	_	_
//	15	1964	1964	z	Z	_	14	COMP	_	_
//	16	y	y	c	CC	_	15	COORD	_	_
//	17	diseñado	diseñar	v	VMP00SM	_	16	CONJ	_	_
//	18	para	para	s	SPS00	_	17	MOD	_	_
//	19	generar	generar	v	VMN0000	_	18	COMP	_	_
//	20	informes	informe	n	NCMP000	_	19	DO	_	_
//	21	comerciales	comercial	a	AQ0CP0	_	20	MOD	_	_
//	22	o	o	c	CC	_	21	COORD	_	_
//	23	de	de	s	SPS00	_	22	CONJ	_	_
//	24	negocios	negocio	n	NCMP000	_	23	COMP	_	_
//	25	.	.	f	Fp	_	24	punct	_	_
//
//	ID:471
//	property subject: Superman
//	property subject uri: http://dbpedia.org/resource/Superman_(Sunsoft_game)
//	property object: Sunsoft
//	property object uri: http://dbpedia.org/resource/Sunsoft
//	sentence:: 
//	1	Superman	superman	n	NP00000	_	2	SUBJ	_	_
//	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	3	un	uno	d	DI0MS0	_	4	SPEC	_	_
//	4	videojuego	videojuego	n	NCMS000	_	2	ATR	_	_
//	5	desarrollado	desarrollar	v	VMP00SM	_	4	MOD	_	_
//	6	y	y	c	CC	_	5	COORD	_	_
//	7	distribuido	distribuir	v	VMP00SM	_	6	CONJ	_	_
//	8	por	por	s	SPS00	_	7	BYAG	_	_
//	9	Sunsoft	sunsoft	n	NP00000	_	8	COMP	_	_
//	10	para	para	s	SPS00	_	7	MOD	_	_
//	11	Sega_Mega_Drive	sega_mega_drive	n	NP00000	_	10	COMP	_	_
//	12	en	en	s	SPS00	_	7	MOD	_	_
//	13	1992	1992	z	Z	_	12	COMP	_	_
//	14	.	.	f	Fp	_	13	punct	_	_



	
        @Override
        public String getQuery() {
            String query= "SELECT ?lemma ?form ?e1_arg ?e2_arg ?prep WHERE {"

                            
                            + "?copula <conll:lemma> \"ser\" ."
                            + "?copula <conll:cpostag> \"v\" ."

                            + "?e1 <conll:head> ?copula ."
                            + "?e1 <conll:deprel> \"SUBJ\"."

                            + "?noun <conll:head> ?copula ."
                            + "?noun <conll:deprel> \"ATR\". "
                            + "?noun <conll:cpostag> \"n\". " 
					        
                            + "?participle <conll:lemma> ?lemma ."
                            + "?participle <conll:form> ?form ."
                            + "?participle <conll:head> ?noun ."
                            + "?participle <conll:deprel> \"MOD\". "
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
		return "SparqlPattern_ES_Predicative_Participle_Copulative";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
				
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String participle = null;
                String lemma = null;
                int number = 0;


                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();
                    number+=1;

                    try{
                            participle = qs.get("?form").toString();
                            e1_arg = qs.get("?e1_arg").toString();
                            e2_arg = qs.get("?e2_arg").toString();	
                            preposition = qs.get("?prep").toString();
                            lemma = qs.get("?lemma").toString();             
                     }
                    catch(Exception e){
                   e.printStackTrace();
                   }
                }

                qExec.close() ;
    
		if(participle!=null && e1_arg!=null && e2_arg!=null && preposition!=null && number==1) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getAdjective(model, lexicon, sentence, participle, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                                
                    if(preposition.equals("por"))
                            Templates.getTransitiveVerb(model, lexicon, sentence, lemma, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                    
		
		}
		
	}

}
