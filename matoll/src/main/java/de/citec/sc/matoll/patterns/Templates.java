package de.citec.sc.matoll.patterns;

import java.util.List;

import org.apache.logging.log4j.Logger;

import org.apache.jena.rdf.model.Model;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Preposition;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.utils.Lemmatizer;

public class Templates {
	
   
	public static void getNounPossessive(Model model, Lexicon lexicon, Sentence sentence, String noun, String e1_arg, String e2_arg, String reference,Logger logger,Lemmatizer Lemmatizer, Language language, String pattern_name) {
	        		 
            LexicalEntry entry = new LexicalEntry(language);

            Sense sense = new Sense();
            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);
            provenance.addPattern(pattern_name);

            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();



            if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(noun);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_PossessiveNoun");
            }
            else
            {
                    entry.setCanonicalForm(noun);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(noun)+"_as_PossessiveNoun");
            }
            
            
            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPossessiveFrame");

            // System.out.print(entry+"\n");

            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(noun)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+noun);
                    }





            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","subject",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(noun)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+noun);
                    }


            }
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }
	        			 
	}
	
	
	
	public static void getNounWithPrep(Model model, Lexicon lexicon, Sentence sentence, String noun, String e1_arg, String e2_arg, String preposition, String reference,Logger logger,Lemmatizer Lemmatizer, Language language, String pattern_name) {
	        		 
            LexicalEntry entry = new LexicalEntry(language);
            Sense sense = new Sense();
            entry.setPreposition(new Preposition(language,preposition));

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.addPattern(pattern_name);
            provenance.setFrequency(1);
            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();


            if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(noun);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_Noun_withPrep_"+preposition);
            }
            else
            {
                    entry.setCanonicalForm(noun);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(noun)+"_as_Noun_withPrep_"+preposition);
            }

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

                    if(isAlpha(noun)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+noun);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","subject",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(noun)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+noun);
                    }


            }
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }
	}
	
	
	public static void getAdjective(Model model, Lexicon lexicon, Sentence sentence, String adjective, String e1_arg, String e2_arg, String preposition, String reference,Logger logger,Lemmatizer Lemmatizer, Language language, String pattern_name) {
	        		 
            LexicalEntry entry = new LexicalEntry(language);
            entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(adjective)+"_as_Adjective_withPrep_"+preposition);
            entry.setPreposition(new Preposition(language,preposition));
            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);
            provenance.addPattern(pattern_name);
            provenance.addSentence(sentence);

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

                    if(isAlpha(adjective)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+adjective);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","subject",preposition));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(adjective)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+adjective);
                    }


            }
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }
	        			
	}
	
	
	
	
	public static void getTransitiveVerb(Model model, Lexicon lexicon, Sentence sentence, String verb, String e1_arg, String e2_arg, String reference,Logger logger,Lemmatizer Lemmatizer, Language language,String pattern_name) {
            LexicalEntry entry = new LexicalEntry(language);

            

            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.addPattern(pattern_name);
            provenance.setFrequency(1);
            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();


            
            if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(verb);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_TransitiveVerb");
            }
            else
            {
                    entry.setCanonicalForm(verb);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(verb)+"_as_TransitiveVerb");
            }
            
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

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }

	}
	
        public static void getReflexiveTransitiveVerb(Model model, Lexicon lexicon,  Sentence sentence, String verb, String e1_arg, String e2_arg, String preposition, String reference,Logger logger,Lemmatizer Lemmatizer, Language language,String pattern_name) {

            LexicalEntry entry = new LexicalEntry(language);
            entry.setPreposition(new Preposition(language,preposition));
            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);
            provenance.addPattern(pattern_name);
            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();
            
            
             if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(verb);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_ReflexiveTransitiveVerb_withPrep_"+preposition);
            }
            else
            {
                    entry.setCanonicalForm(verb);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(verb)+"_as_ReflexiveTransitiveVerb_withPrep_"+preposition);
            }
             
             
            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#ReflexiveTransitivePPFrame");


            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",preposition));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","subject",preposition));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }

	}
                
    public static void getReflexiveTransitiveVerbWihoutPrep(Model model, Lexicon lexicon,  Sentence sentence, String verb, String e1_arg, String e2_arg, String reference,Logger logger,Lemmatizer Lemmatizer, Language language,String pattern_name) {

            LexicalEntry entry = new LexicalEntry(language);
            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);
            provenance.addPattern(pattern_name);
            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();
            
            
             if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(verb);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_ReflexiveTransitiveVerb_withoutPrep");
            }
            else
            {
                    entry.setCanonicalForm(verb);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(verb)+"_as_ReflexiveTransitiveVerb_withoutPrep");
            }
             
             
            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#ReflexiveTransitiveFrame");


            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","subject",null));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }

	}
                        
	public static void getIntransitiveVerb(Model model, Lexicon lexicon,  Sentence sentence, String verb, String e1_arg, String e2_arg, String preposition, String reference,Logger logger,Lemmatizer Lemmatizer, Language language,String pattern_name) {

            LexicalEntry entry = new LexicalEntry(language);
            entry.setPreposition(new Preposition(language,preposition));
            Sense sense = new Sense();

            Reference ref = new SimpleReference(reference);
            sense.setReference(ref);

            Provenance provenance = new Provenance();
            provenance.setFrequency(1);
            provenance.addPattern(pattern_name);
            provenance.addSentence(sentence);


            SyntacticBehaviour behaviour = new SyntacticBehaviour();
            
            
             if (Lemmatizer != null)
            {
                    String term = Lemmatizer.getLemma(verb);
                    //logger.debug("Lemmatized cannonical form:"+term+"/n");
                    entry.setCanonicalForm(term);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(term)+"_as_IntransitiveVerb_withPrep_"+preposition);
            }
            else
            {
                    entry.setCanonicalForm(verb);
                    entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+cleanTerm(verb)+"_as_IntransitiveVerb_withPrep_"+preposition);
            }
             
             
            entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");

            behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePPFrame");


            if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",preposition));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	

            else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
            {

                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
                    behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","subject",preposition));

                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
                    sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

                    entry.addSyntacticBehaviour(behaviour,sense);
                    entry.addProvenance(provenance,sense);

                    if(isAlpha(verb)){
                        lexicon.addEntry(entry);

                        //logger.debug("Found entry:"+entry+"\n");
                    }
                    else{
                        //logger.debug("Dit not add entry, beause of label: "+verb);
                    }


            }	
            else{
                    //logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");

            }

	}
        
        
        
        
        
        
        
        
        private static boolean isAlpha(String label) {
            if(label.length()<=2) return false;
            label = cleanTerm(label);
            label = label.replace("_","");
            char[] chars = label.toCharArray();

            for (char c : chars) {
                if(!Character.isLetter(c)) {
                    return false;
                }
            }
            return true;
        }
        
        
        private static String cleanTerm(String input){
            String output = input.replace("ü", "ue")
                    .replace("ö", "oe")
                    .replace("ß", "ss")
                    .replace("-", "_")
                    .replace(" ", "_")
                    .replace("\"", "")
                    .replace("+", "_");
            return output;
        }
	

}
