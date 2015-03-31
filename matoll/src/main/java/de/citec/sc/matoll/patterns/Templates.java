package de.citec.sc.matoll.patterns;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.utils.Debug;
import de.citec.sc.matoll.utils.Lemmatizer;

public class Templates {
	
	public static void getNoun(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, Debug debugger) {
	    String e1_arg ="";
	    String e2_arg = "";
	    String noun = "";
	    // match SPARQL query
	    QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	 	ResultSet rs = qExec.execSelect() ;
	     
	 	try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 
	        	 // System.out.print("Query 3 matched\n!!!");
	        	 
	        	 try{
	        		 noun = qs.get("?lemma").toString();
                    		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();	
                                 debugger.printWaiter();
                                 debugger.print("Noun: "+noun, "Templates.getNoun()");
                                 debugger.print("e1_arg: "+e1_arg, "");
                                 debugger.print("e2_arg: "+e2_arg, "");
                                 
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(noun)+"@en";
	        				logger.info("Lemmatized cannonical form:"+term+"/n");
                                                debugger.print("Lemmatized cannonical form:"+term, "");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(noun+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                debugger.print("Added Sentence:"+sentence, "");
	        			}
	        			
                                        debugger.printDependencys(model);
                                        
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.setWait(true);
                                                debugger.printWaiter();
                                                debugger.setWait(false);
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}
	        			else{
	        				logger.info("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                debugger.printWaiter();
	        			}
	        			 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
	}
	
	
	
	public static void getNounWithPrep(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer,Debug debugger) {
	    String e1_arg ="";
	    String e2_arg = "";
	    String prep = "";
	    String noun = "";
	    // match SPARQL query
	    QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	 	ResultSet rs = qExec.execSelect() ;
	     
	 	try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 
	        	 // System.out.print("Query 3 matched\n!!!");
	        	 
	        	 try{
	        		 noun = qs.get("?lemma").toString();
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
	        		 
	        		 prep = qs.get("?prep").toString();
                                 debugger.printWaiter();
                                 debugger.print("Noun: "+noun, "Templates.getNounWithPrep()");
                                 debugger.print("e1_arg: "+e1_arg, "");
                                 debugger.print("e2_arg: "+e2_arg, "");
	        		 debugger.print("prep: "+prep, "");
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(noun)+"@en";
	        				logger.info("Lemmatized cannonical form:"+term+"/n");
                                                debugger.print("Lemmatized cannonical form:"+term, "");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(noun+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                debugger.print("Added Sentence:"+sentence, "");
	        			}
                                        
                                        debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/adpositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}
	        			else{
	        				logger.info("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                debugger.printWaiter();
	        			}
	        			 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
	}
	
	
	public static void getAdjective(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, Debug debugger) {
		//TODO: Check this entry
	    String e1_arg ="";
	    String e2_arg = "";
	    String prep = "";
	    String adj = "";
	    // match SPARQL query
	    QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	 	ResultSet rs = qExec.execSelect() ;
	     
	 	try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 
	        	 // System.out.print("Query 3 matched\n!!!");
	        	 
	        	 try{
	        		 adj = qs.get("?lemma").toString();
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
	        		 
	        		 prep = qs.get("?prep").toString();
                                 debugger.printWaiter();
                                 debugger.print("Adj: "+adj, "Templates.getAdjective()");
                                 debugger.print("e1_arg: "+e1_arg, "");
                                 debugger.print("e2_arg: "+e2_arg, "");
	        		 debugger.print("prep: "+prep, "");
	        		 
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(adj)+"@en";
	        				logger.info("Lemmatized cannonical form:"+term+"/n");
                                                debugger.print("Lemmatized cannonical form:"+term, "");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(adj+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicateFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                debugger.print("Added Sentence:"+sentence, "");
	        			}
                                        
                                        debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}
	        			else{
	        				logger.info("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                debugger.printWaiter();
	        			}
	        			 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
	}
	
	
	
	
	public static void getTransitiveVerb(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer,Debug debugger) {
		
		// match SPARQL query
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	    ResultSet rs = qExec.execSelect() ;
	    
	    String verb = "";
	    String e1_arg ="";
	    String e2_arg = "";
		
	     
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{
	        		 verb = qs.get("?lemma").toString();
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
                                 debugger.printWaiter();
                                 debugger.print("Verb: "+verb, "Templates.getTransitiveVerb()");
                                 debugger.print("e1_arg: "+e1_arg, "");
                                 debugger.print("e2_arg: "+e2_arg, "");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	        		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(verb)+"@en";
	        				logger.info("Lemmatized cannonical form:"+term+"/n");
                                                debugger.print("Lemmatized cannonical form:"+term, "");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(verb+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                debugger.print("Added Sentence:"+sentence, "");
	        			}
	        			
                                        debugger.printDependencys(model);
                                        
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}
	        			else{
	        				logger.info("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                debugger.printWaiter();
	        			}
	        		 
	        		 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;

	}
	
	
	
	public static void getIntransitiveVerb(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, Debug debugger) {
		
		// match SPARQL query
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	    ResultSet rs = qExec.execSelect() ;
	    
	    String verb = "";
	    String e1_arg ="";
	    String e2_arg = "";
	    String prep = "";
	    RDFNode dobj_form;
		
	     
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{   
                                 debugger.printWaiter();
	        		 verb = qs.get("?lemma").toString();
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
	        		 prep = qs.get("?prep").toString();
	        		 dobj_form = qs.get("?dobj_form");
                                 debugger.print("Verb: "+verb, "Templates.getIntransitiveVerb()");
                                 debugger.print("e1_arg: "+e1_arg, "");
                                 debugger.print("e2_arg: "+e2_arg, "");
                                 debugger.print("Prep: "+prep, "");
                                 debugger.print("dobj_form: "+dobj_form, "");
	        		 
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	        		 	sense.setReference(new SimpleReference(reference));
	        		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(verb)+"@en";
	        				logger.info("Lemmatized cannonical form:"+term+"/n");
                                                debugger.print("Lemmatized cannonical form:"+term, "");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(verb+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePPFrame");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                debugger.print("Added Sentence:"+sentence, "");
	        			}
                                        
                                        debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2",prep));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",prep));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"\n");
                                                debugger.printWaiter();
	        				
	        			}	
	        			else{
	        				logger.info("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                debugger.printWaiter();
	        			}
	        		 
	        		 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;

	}
	

}
