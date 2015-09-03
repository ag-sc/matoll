package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import org.apache.jena.shared.Lock;

public class SparqlPattern_DE_6_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_6_old.class.getName());
	
	/*################################
PropSubj:Adelheid von Meißen
PropObj:Ottokar I Přemysl
sentence:Adelheid von Meißen tschechisch Adléta Míšeňská * nach 1160 ; † 2. Februar 1211 in Meißen war die erste Ehefrau des böhmischen Fürsten und Königs Ottokar I . Přemysl . 
1	Adelheid	Adelheid	N	NE	_	0	root	_	_ 
2	von	von	PREP	APPR	Dat	1	pp	_	_ 
3	Meißen	Meißen	N	NE	Neut|Dat|Sg	2	pn	_	_ 
4	tschechisch	tschechisch	ADV	ADJD	Pos|	6	adv	_	_ 
5	Adléta	Adléta	ADJA	ADJA	_	6	attr	_	_ 
6	Míšeňská	Míšeňská	N	NN	Neut|Dat|Sg	3	app	_	_ 
7	*	*	XY	XY	_	0	root	_	_ 
8	nach	nach	PREP	APPR	Dat	2	kon	_	_ 
9	1160	1160	CARD	CARD	_	8	pn	_	_ 
10	;	;	$.	$.	_	0	root	_	_ 
11	†	†	ADJA	ADJA	_|Masc|Nom|Sg|_	13	attr	_	_ 
12	2.	2.	ADJA	ADJA	_|Masc|Nom|Sg|_|	13	attr	_	_ 
13	Februar	Februar	N	NN	Masc|Nom|Sg	17	subj	_	_ 
14	1211	1211	CARD	CARD	_	13	app	_	_ 
15	in	in	PREP	APPR	_	14	pp	_	_ 
16	Meißen	Meißen	N	NE	Neut|_|Sg	15	pn	_	_ 
17	war	sein	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
18	die	die	ART	ART	Def|Fem|Nom|Sg	20	det	_	_ 
19	erste	erst	ADJA	ADJA	_|Fem|Nom|Sg|_|	20	attr	_	_ 
20	Ehefrau	Ehefrau	N	NN	Fem|Nom|Sg	17	pred	_	_ 
21	des	das	ART	ART	Def|Masc|Gen|Sg	23	det	_	_ 
22	böhmischen	böhmisch	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	23	attr	_	_ 
23	Fürsten	Fürst	N	NN	Masc|Gen|Sg	20	gmod	_	_ 
24	und	und	KON	KON	_	23	kon	_	_ 
25	Königs	König	N	NN	Masc|Gen|Sg	24	cj	_	_ 
26	Ottokar	Ottokar	N	NE	Masc|Gen|Sg	25	app	_	_ 
27	I	I	FM	FM	Masc|Gen|Sg	26	app	_	_ 
28	.	.	$.	$.	_	0	root	_	_ 
29	Přemysl	Přemysl	N	NE	_	0	root	_	_ 
30	.	.	$.	$.	_	0	root	_	_ 	
----------------------

PropSubj:Kim Jong-un
PropObj:Ri Sol-ju
sentence:Ri Sol-ju , oder ; * Januar 1985–1989 ist die Ehefrau des nordkoreanischen Führers Kim Jong-un . 
1	Ri	Ri	ADJA	ADJA	_	2	attr	_	_ 
2	Sol-ju	Sol-ju	N	NN	_	0	root	_	_ 
3	,	,	$,	$,	_	0	root	_	_ 
4	oder	oder	KON	KON	_	0	root	_	_ 
5	;	;	$.	$.	_	0	root	_	_ 
6	*	*	XY	XY	_	0	root	_	_ 
7	Januar	Januar	N	NE	Masc|Nom|Sg	9	subj	_	_ 
8	1985–1989	1985–1989	N	NN	Masc|Nom|Sg	7	app	_	_ 
9	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
10	die	die	ART	ART	Def|Fem|Nom|Sg	11	det	_	_ 
11	Ehefrau	Ehefrau	N	NN	Fem|Nom|Sg	9	pred	_	_ 
12	des	das	ART	ART	Def|Masc|Gen|Sg	14	det	_	_ 
13	nordkoreanischen	Nordkoreanisch	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	14	attr	_	_ 
14	Führers	Führer	N	NN	Masc|Gen|Sg	11	gmod	_	_ 
15	Kim	Kim	N	NE	Masc|Gen|Sg	14	app	_	_ 
16	Jong-un	Jong-un	N	NE	Masc|Gen|Sg	15	app	_	_ 
17	.	.	$.	$.	_	0	root	_	_ 	
----------------------

PropSubj:Minnesota River
PropObj:Big Stone Lake
sentence:Der Big Stone Lake ist die Quelle des Minnesota River , einem 534 km langer Nebenfluss des Mississippi River . 
1	Der	der	ART	ART	Def|Fem|Dat|Sg	4	det	_	_ 
2	Big	Big	ADV	ADJD	_	3	adv	_	_ 
3	Stone	Stone	ADJA	ADJA	_|Fem|Dat|Sg|_	4	attr	_	_ 
4	Lake	Lake	N	NN	Fem|Dat|Sg	5	objd	_	_ 
5	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
6	die	die	ART	ART	Def|Fem|Nom|Sg	7	det	_	_ 
7	Quelle	Quell	N	NN	Fem|Nom|Sg	5	subj	_	_ 
8	des	das	ART	ART	Def|_|Gen|Sg	9	det	_	_ 
9	Minnesota	Minnesota	N	NE	_|Gen|Sg	7	gmod	_	_ 
10	River	River	N	NE	_|Dat|Sg	9	app	_	_ 
11	,	,	$,	$,	_	0	root	_	_ 

*/
	@Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:head> ?verb . "
                            + "?e1 <conll:deprel> ?e1_grammar . "
                            //+ "FILTER regex(?e1_grammar, \"subj\") ."
                            + "FILTER( regex(?e1_grammar, \"obj\") || regex(?e1_grammar, \"subj\"))"
                            + "?y <conll:cpostag> ?lemma_pos . "
                            + "?y <conll:cpostag> \"N\" . "
                            + "?y <conll:deprel> \"pred\" . "
                            + "FILTER( regex(?lemma_grammar, \"subj\") || regex(?lemma_grammar, \"pred\"))"
                            + "?y <conll:lemma> ?lemma . "
                            + "?y <conll:head> ?verb . "
                            + "?verb <conll:cpostag> \"V\" ."
                            + "?verb <conll:lemma> \"sein\" ."
                            + "?e2 <conll:head> ?y . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_6";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
                
		model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
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
                model.leaveCriticalSection() ;
    
		if(noun!=null && e1_arg!=null && e2_arg!=null) {
                    Templates.getNounPossessive(model, lexicon, sentences, noun, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
		
		
	}

}
