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

public class SparqlPattern_ES_Intransitive_PP extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Intransitive_PP.class.getName());

// aus spouse	
	
//	ID:291
//	property subject: Joviano
//	property object: Charito
//	sentence:: 
//	1	Charito	charito	n	NP00000	_	3	SUBJ
//	2	se	se	p	P00CN000	_	3	DO
//	3	casó	casar	v	VMIS3S0	_	0	ROOT
//	4	con	con	s	SPS00	_	3	MOD
//	5	Joviano	joviano	n	NP00000	_	4	COMP
//	6	,	,	f	Fc	_	5	punct
//	7	un	uno	d	DI0MS0	_	8	SPEC
//	8	hijo	hijo	n	NCMS000	_	3	DO
//	9	de	de	s	SPS00	_	8	MOD
//	10	Varroniano	varroniano	n	NP00000	_	9	COMP
//	11	.	.	f	Fp	_	10	punct

 
//        ID:202
//property subject: Meno
//property subject uri: http://dbpedia.org/resource/Main_(river)
//property object: Alemania
//property object uri: http://dbpedia.org/resource/Germany
//sentence::
//1    El    el    d    DA0MS0    _    2    SPEC    _    _
//2    río    río    n    NCMS000    _    4    SUBJ    _    _
//3    Tauber    tauber    n    NP00000    _    2    MOD    _    _
//4    nace    nacer    v    VMIP3S0    _    0    ROOT    _    _
//5    cerca    cerca    r    RG    _    4    MOD    _    _
//6    de    de    s    SPS00    _    4    MOD    _    _
//7    Rot    rot    n    NP00000    _    6    COMP    _    _
//8    am    ametro    n    NCMN000    _    15    SUBJ    _    _
//9    See    see    n    NP00000    _    8    MOD    _    _
//10    en    en    s    SPS00    _    0    ROOT    _    _
//11    Baden-Wurtemberg    baden-wurtemberg    n    NP00000    _ 10    COMP    _    _
//12    ,    ,    f    Fc    _    11    punct    _    _
//13    Alemania    alemania    n    NP00000    _    11    _    _    _
//14    y    y    c    CC    _    13    COORD    _    _
//15    desemboca    desembocar    v    VMM02S0    _    14    CONJ _    _
//16    en    en    s    SPS00    _    15    MOD    _    _
//17    el    el    d    DA0MS0    _    18    SPEC    _    _
//18    Meno    meno    n    NP00000    _    16    COMP    _    _
//19    en    en    s    SPS00    _    15    MOD    _    _
//20    Wertheim    wertheim    n    NP00000    _    19    COMP _    _
//21    .    .    f    Fp    _    20    punct    _    _
//
//
//ID:204
//property subject: Río Níger
//property subject uri: http://dbpedia.org/resource/Niger_River
//property object: Guinea
//property object uri: http://dbpedia.org/resource/Guinea
//sentence::
//1    El    el    d    DA0MS0    _    2    SPEC    _    _
//2    río    río    n    NCMS000    _    4    SUBJ    _    _
//3    Níger    níger    n    NP00000    _    2    MOD    _    _
//4    nace    nacer    v    VMIP3S0    _    0    ROOT    _    _
//5    cerca    cerca    r    RG    _    4    MOD    _    _
//6    de    de    s    SPS00    _    4    MOD    _    _
//7    los    el    d    DA0MP0    _    8    SPEC    _    _
//8    montes    monte    n    NCMP000    _    6    COMP    _    _
//9    Loma    loma    n    NP00000    _    8    MOD    _    _
//10    ,    ,    f    Fc    _    9    punct    _    _
//11    en    en    s    SPS00    _    4    MOD    _    _
//12    la    el    d    DA0FS0    _    13    SPEC    _    _
//13    frontera    frontera    n    NCFS000    _    11    COMP _    _
//14    entre    entre    s    SPS00    _    13    MOD    _    _
//15    Sierra_Leona    sierra_leona    n    NP00000    _    14 COMP    _    _
//16    y    y    c    CC    _    15    COORD    _    _
//17    Guinea    guinea    n    NP00000    _    16    CONJ    _    _
//18    .    .    f    Fp    _    17    punct    _    _
//
//ID:319
//property subject: Río Liard
//property subject uri: http://dbpedia.org/resource/Liard_River
//property object: Yukón
//property object uri: http://dbpedia.org/resource/Yukon
//sentence::
//1    El    el    d    DA0MS0    _    2    SPEC    _    _
//2    río    río    n    NCMS000    _    4    SUBJ    _    _
//3    Liard    liard    n    NP00000    _    2    MOD    _    _
//4    nace    nacer    v    VMIP3S0    _    0    ROOT    _    _
//5    en    en    s    SPS00    _    4    MOD    _    _
//6    la    el    d    DA0FS0    _    7    SPEC    _    _
//7    cordillera    cordillera    n    NCFS000    _    5    COMP _    _
//8    de    de    s    SPS00    _    7    MOD    _    _
//9    Saint_Cyr    saint_cyr    n    NP00000    _    8    COMP _    _
//10    ,    ,    f    Fc    _    9    punct    _    _
//11    en    en    s    SPS00    _    4    MOD    _    _
//12    las    el    d    DA0FP0    _    13    SPEC    _    _
//13    montañas    montaña    n    NCFP000    _    11    COMP    _ _
//14    Pelly    pelly    n    NP00000    _    13    MOD    _    _
//15    ,    ,    f    Fc    _    14    punct    _    _
//16    a    a    s    SPS00    _    4    MOD    _    _
//17    el    el    d    DA0MS0    _    18    SPEC    _    _
//18    sudeste    sudeste    n    NCMS000    _    16    COMP    _ _
//19    de    de    s    SPS00    _    18    MOD    _    _
//20    el    el    d    DA0MS0    _    21    SPEC    _    _
//21    territorio    territorio    n    NCMS000    _    19    COMP _    _
//22    de    de    s    SPS00    _    21    MOD    _    _
//23    el    el    d    DA0MS0    _    24    SPEC    _    _
//24    Yukón    yukón    n    NP00000    _    22    COMP    _    _
//25    .    .    f    Fp    _    24    punct    _    _
//
//ID:1250
//property subject: Dahme
//property subject uri: http://dbpedia.org/resource/Dahme_(river)
//property object: Alemania
//property object uri: http://dbpedia.org/resource/Germany
//sentence::
//1    El    el    d    DA0MS0    _    2    SPEC    _    _
//2    Dahme    dahme    n    NP00000    _    3    SUBJ    _    _
//3    es    ser    v    VSIP3S0    _    0    ROOT    _    _
//4    un    uno    d    DI0MS0    _    5    SPEC    _    _
//5    río    río    n    NCMS000    _    3    ATR    _    _
//6    que    que    p    PR0CN000    _    7    SUBJ    _    _
//7    fluye    fluir    v    VMIP3S0    _    5    MOD    _    _
//8    a_través_de    a_través_de    s    SPS00    _    7    MOD _    _
//9    los    el    d    DA0MP0    _    10    SPEC    _    _
//10    estados    estado    n    NCMP000    _    8    COMP    _    _
//11    de    de    s    SPS00    _    10    MOD    _    _
//12    Brandenburgo    brandenburgo    n    NP00000    _    11 COMP    _    _
//13    y    y    c    CC    _    12    COORD    _    _
//14    Berlín    berlín    n    NP00000    _    13    CONJ    _    _
//15    en    en    s    SPS00    _    7    MOD    _    _
//16    Alemania    alemania    n    NP00000    _    15    COMP _    _
//17    .    .    f    Fp    _    16    punct    _    _
//
//ID:507
//property subject: Brahmaputra
//property subject uri: http://dbpedia.org/resource/Brahmaputra_River
//property object: Tibet
//property object uri: http://dbpedia.org/resource/Tibet
//sentence::
//1    Sirve    servir    v    VMIP3S0    _    0    ROOT    _    _
//2    como    como    c    CS    _    1    MOD    _    _
//3    la    el    d    DA0FS0    _    4    SPEC    _    _
//4    rama    rama    n    NCFS000    _    2    COMP    _    _
//5    principal    principal    a    AQ0CS0    _    4    MOD    _ _
//6    de    de    s    SPS00    _    4    COMP    _    _
//7    el    el    d    DA0MS0    _    8    SPEC    _    _
//8    río    río    n    NCMS000    _    6    COMP    _    _
//9    Brahmaputra    brahmaputra    n    NP00000    _    8    MOD _    _
//10    ,    ,    f    Fc    _    9    punct    _    _
//11    que    que    p    PR0CN000    _    12    SUBJ    _    _
//12    fluye    fluir    v    VMIP3S0    _    4    MOD    _    _
//13    por    por    s    SPS00    _    12    MOD    _    _
//14    el    el    d    DA0MS0    _    15    SPEC    _    _
//15    Tíbet    tíbet    n    NP00000    _    13    COMP    _    _
//16    (    (    f    Fpa    _    17    punct    _    _
//17    China    china    n    NP00000    _    15    MOD    _    _
//18    )    )    f    Fpt    _    17    punct    _    _
//19    e    y    c    CC    _    17    COORD    _    _
//20    India    india    n    NP00000    _    19    CONJ    _    _
//21    .    .    f    Fp    _    20    punct    _    _
//
//ID:220
//property subject: Kuntí
//property subject uri: http://dbpedia.org/resource/Kunti
//property object: Pandú
//property object uri: http://dbpedia.org/resource/Pandu
//sentence::
//1    Más    más    r    RG    _    2    SPEC    _    _
//2    tarde    tarde    r    RG    _    5    MOD    _    _
//3    Kuntí    kuntí    n    NP00000    _    5    MOD    _    _
//4    se    se    p    P00CN000    _    5    SUBJ    _    _
//5    casó    casar    v    VMIS3S0    _    0    ROOT    _    _
//6    con    con    s    SPS00    _    5    OBLC    _    _
//7    el    el    d    DA0MS0    _    9    SPEC    _    _
//8    joven    joven    a    AQ0CS0    _    9    MOD    _    _
//9    rey    rey    n    NCMS000    _    6    COMP    _    _
//10    Pandú    pandú    n    NP00000    _    9    MOD    _    _
//11    ,    ,    f    Fc    _    10    punct    _    _
//12    que    que    p    PR0CN000    _    14    SUBJ    _    _
//13    ya    ya    r    RG    _    14    MOD    _    _
//14    estaba    estar    v    VAII1S0    _    9    MOD    _    _
//15    casado    casar    v    VMP00SM    _    14    ATR    _    _
//16    con    con    s    SPS00    _    15    OBLC    _    _
//17    Madrí    madrí    n    NP00000    _    16    COMP    _    _
//18    .    .    f    Fp    _    17    punct    _    _
//
//
//        
	// reflexive verb: se casó con
	// Template needs to be changed!!!
    
        // X verheiratet sich mit Y
        // -> Reflexiv 
        // X heiratete Y -> Transitive
        


        
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
                            + "?verb <conll:cpostag> \"v\" ."
                            + "?verb <conll:lemma> ?lemma ."
                            + "FILTER NOT EXISTS{"
                                 + "?se <conll:form> \"se\" ."
                                 + "?se <conll:head> ?verb ."
                            + "}"
                            + "FILTER NOT EXISTS{"
                                 + "{?se <conll:deprel> \"DO\" .} UNION "
                                 + "{?se <conll:deprel> \"MPAS\" .}"
                                 + "?se <conll:head> ?verb ."
                            + "}"
//
                            + "?e1 <conll:head> ?verb."
                            + "?e1 <conll:deprel> \"SUBJ\" ."

                            + "?p <conll:head> ?verb. "
                            + "?p <conll:cpostag> \"s\". "
                            + "{?p <conll:deprel> \"MOD\" .} UNION "
                            + "{?p <conll:deprel> \"OBLC\" .} "
                            + "?p <conll:lemma> ?prep. "
//
                            + "?e2 <conll:head> ?p."
                            + "?e2 <conll:deprel> \"COMP\"."

                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			
	@Override
	public String getID() {
		return "SparqlPattern_ES_Intransitive_PP";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                int number = 0;

                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();
                    number+=1;


                    try{
                            verb = qs.get("?lemma").toString();
                            e1_arg = qs.get("?e1_arg").toString();
                            e2_arg = qs.get("?e2_arg").toString();	
                            preposition = qs.get("?prep").toString();
                     }
                    catch(Exception e){
                   e.printStackTrace();
                   }
                }

                qExec.close() ;
                
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && preposition!=null && number==1) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getIntransitiveVerb(model, lexicon, sentence, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                }
		
	
	}

}
