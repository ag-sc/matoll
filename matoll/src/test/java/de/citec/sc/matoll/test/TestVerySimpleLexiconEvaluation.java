package de.citec.sc.matoll.test;


import de.citec.sc.matoll.core.Language;
import static de.citec.sc.matoll.core.Language.EN;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Preposition;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.evaluation.LemmaBasedEvaluation;
import java.util.List;





/**
 *
 * @author swalter
 */
public class TestVerySimpleLexiconEvaluation {
 

    public static void main(String[] args){
                

//        _LexiconLoaderGreaterK_ loader = new _LexiconLoaderGreaterK_(1);
//        LexiconLoader normalloader = new LexiconLoader();
//        Lexicon gold = normalloader.loadFromFile("../dbpedia_en.rdf");
//        Lexicon lex = loader.loadFromFile("/Users/swalter/Downloads/ResultsJanuary2016/dbpedia2014_DE_Full_beforeTraining.ttl");
        
        String subj = "http://lemon-model.net/lemon#subjOfProp";
        String obj = "http://lemon-model.net/lemon#objOfProp";

        Lexicon gold = new Lexicon();
        getNounWithPrep(gold, "wife", subj, obj, "of", "http://dbpedia.org/ontology/spouse", EN);
        getNounWithPrep(gold, "husband", subj, obj, "to", "http://dbpedia.org/ontology/spouse", EN);
        getAdjective(gold,"married", subj, obj, "to","http://dbpedia.org/ontology/spouse",EN);
        getTransitiveVerb(gold, "cross", subj,obj, "http://dbpedia.org/ontology/crossing", EN);

        
        Lexicon lex = new Lexicon();
        getNounWithPrep(lex, "wife", subj, obj, "of", "http://dbpedia.org/ontology/spouse", EN);
        getNounWithPrep(lex, "husband", subj, obj, "to", "http://dbpedia.org/ontology/spouse", EN);
        getAdjective(lex,"married", subj, obj, "to","http://dbpedia.org/ontology/spouse",EN);
        getTransitiveVerb(lex, "cross", subj,obj, "http://dbpedia.org/ontology/crossing", EN);
        
        List<Double> result = LemmaBasedEvaluation.evaluate(lex, gold,true,true);
        System.out.println("----------");
        System.out.println("Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        System.out.println("----------");
        
        result = LemmaBasedEvaluation.evaluate(lex, gold,true,false);
        System.out.println("----------");
        System.out.println("Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        System.out.println("----------");
        
    }
    
    
    
    
    
    
    public static void getNounWithPrep(Lexicon lexicon, String noun, String e1_arg, String e2_arg, String preposition, String reference, Language language) {
	        		 
            LexicalEntry entry = new LexicalEntry(language);
            Sense sense = new Sense();
            entry.setPreposition(new Preposition(language,preposition));

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();
            entry.setCanonicalForm(noun);
            entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+noun.replace(" ", "_")+"_as_Noun_withPrep_"+preposition);
            

            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");

            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    lexicon.addEntry(entry);
            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/adpositionalObject","subject",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    lexicon.addEntry(entry);


            }
	}
	
	
	public static void getAdjective(Lexicon lexicon,String adjective, String e1_arg, String e2_arg, String preposition, String reference, Language language) {
	        		 
            LexicalEntry entry = new LexicalEntry(language);
            entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+adjective.replace(" ", "_")+"_as_Adjective_withPrep_"+preposition);
            entry.setPreposition(new Preposition(language,preposition));
            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();


            /*
            no lemmatizer for the adjectives, in order to avoid that for example married is mapped to marry
            */


            entry.setCanonicalForm(adjective);

            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicateFrame");


            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    lexicon.addEntry(entry);
            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","subject",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    lexicon.addEntry(entry);
            }   			
	}
	
	
	
	
	public static void getTransitiveVerb(Lexicon lexicon, String verb, String e1_arg, String e2_arg, String reference, Language language) {
            LexicalEntry entry = new LexicalEntry(language);

            

            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();

            entry.setCanonicalForm(verb);
            entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+verb.replace(" ", "_")+"_as_TransitiveVerb");
            
            
            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");


            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    lexicon.addEntry(entry);
            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);
                    
                    lexicon.addEntry(entry);
            }

	}
}
