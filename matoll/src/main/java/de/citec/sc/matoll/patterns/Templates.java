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
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.utils.Lemmatizer;
import java.util.Calendar;
import java.util.Date;

public class Templates {
	
	public static void getNoun(Model model, LexiconWithFeatures lexicon,
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, String language) {
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
                                 
                                 logger.debug("Noun: "+noun, "Templates.getNoun()");
                                 logger.debug("e1_arg: "+e1_arg, "");
                                 logger.debug("e2_arg: "+e2_arg, "");
                                 
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
//                                        Calendar calendar = Calendar.getInstance();
//                                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//	        			long timestamp = currentTimestamp.getTime();
                                        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+noun+"_as_Noun");
                                        
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.addSense(sense);
                                        
                                        Provenance provenance = new Provenance();
                                        provenance.setFrequency(1);
                                        entry.setProvenance(provenance);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.addSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(noun)+"@"+language;
	        				logger.debug("Lemmatized cannonical form:"+term+"/n");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(noun+"@"+language);
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                logger.debug("Added Sentence:"+sentence, "");
	        			}
	        			
                                        //debugger.printDependencys(model);
                                        
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
                                                if(isAlpha(noun)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+noun);
                                                }
	        				
                                                
                                                
                                                
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				if(isAlpha(noun)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+noun);
                                                }
                                                
	        				
	        			}
	        			else{
	        				logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                
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
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, String language) {
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
                                 
                                 logger.debug("Noun: "+noun, "Templates.getNounWithPrep()");
                                 logger.debug("e1_arg: "+e1_arg, "");
                                 logger.debug("e2_arg: "+e2_arg, "");
	        		 logger.debug("prep: "+prep, "");
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
//	        			Calendar calendar = Calendar.getInstance();
//                                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//	        			long timestamp = currentTimestamp.getTime();
                                        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+noun+"_as_Noun_withPrep_"+prep);
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.addSense(sense);
                                        Provenance provenance = new Provenance();
                                        provenance.setFrequency(1);
                                        entry.setProvenance(provenance);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.addSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(noun)+"@"+language;
	        				logger.debug("Lemmatized cannonical form:"+term+"/n");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(noun+"@"+language);
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                logger.debug("Added Sentence:"+sentence, "");
	        			}
                                        
                                        //debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(noun)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+noun);
                                                }
                                                
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/adpositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				if(isAlpha(noun)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+noun);
                                                }
                                                
	        				
	        			}
	        			else{
	        				logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                
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
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, String language) {
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
                                 
                                 logger.debug("Adj: "+adj, "Templates.getAdjective()");
                                 logger.debug("e1_arg: "+e1_arg, "");
                                 logger.debug("e2_arg: "+e2_arg, "");
	        		 logger.debug("prep: "+prep, "");
	        		 
	        		    // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
//                                        Calendar calendar = Calendar.getInstance();
//                                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//	        			long timestamp = currentTimestamp.getTime();
                                        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+adj+"_as_Adjective_withPrep_"+prep);
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	           		 	
	        		 	entry.addSense(sense);
                                        
                                        Provenance provenance = new Provenance();
                                        provenance.setFrequency(1);
                                        entry.setProvenance(provenance);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.addSyntacticBehaviour(behaviour);
	        			
                                        /*
                                        no lemmatizer for the adjectives, in order to avoid that for example married is mapped to marry
                                        */
//	        			if (Lemmatizer != null)
//	        			{
//	        				String term = Lemmatizer.getLemma(adj)+"@"+language;
//	        				logger.debug("Lemmatized cannonical form:"+term+"/n");
//                                                logger.debug("Lemmatized cannonical form:"+term, "");
//	        				entry.setCanonicalForm(term);
//	        			}
//	        			else
//	        			{
//	        				entry.setCanonicalForm(adj+"@"+language);
//	        			}
	        				
                                        
                                        entry.setCanonicalForm(adj+"@"+language);
                                        
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicateFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                logger.debug("Added Sentence:"+sentence, "");
	        			}
                                        
                                        //debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(adj)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+adj);
                                                }
                                                
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","1",prep));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				if(isAlpha(adj)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+adj);
                                                }
                                                
	        				
	        			}
	        			else{
	        				logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                
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
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, String language) {
		
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
                                 
                                 logger.debug("Verb: "+verb, "Templates.getTransitiveVerb()");
                                 logger.debug("e1_arg: "+e1_arg, "");
                                 logger.debug("e2_arg: "+e2_arg, "");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
//                                        Calendar calendar = Calendar.getInstance();
//                                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//	        			long timestamp = currentTimestamp.getTime();
                                        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+verb+"_as_TransitiveVerb");
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(reference));
	        		 	
	        		 	entry.addSense(sense);
                                        Provenance provenance = new Provenance();
                                        provenance.setFrequency(1);
                                        entry.setProvenance(provenance);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.addSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(verb)+"@"+language;
	        				logger.debug("Lemmatized cannonical form:"+term+"/n");
                                                verb = term;
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(verb+"@"+language);
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                logger.debug("Added Sentence:"+sentence, "");
	        			}
	        			
                                        //debugger.printDependencys(model);
                                        
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(verb)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+verb);
                                                }
                                                
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(verb)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+verb);
                                                }
                                                
	        				
	        			}
	        			else{
	        				logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                
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
			FeatureVector vector, List<String> sentences, String query, String reference,Logger logger,Lemmatizer Lemmatizer, String language) {
		
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
                                 
	        		 verb = qs.get("?lemma").toString();
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
	        		 prep = qs.get("?prep").toString();
	        		 dobj_form = qs.get("?dobj_form");
                                 logger.debug("Verb: "+verb, "Templates.getIntransitiveVerb()");
                                 logger.debug("e1_arg: "+e1_arg, "");
                                 logger.debug("e2_arg: "+e2_arg, "");
                                 logger.debug("Prep: "+prep, "");
                                 logger.debug("dobj_form: "+dobj_form, "");
	        		 
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
//                                        Calendar calendar = Calendar.getInstance();
//                                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//	        			long timestamp = currentTimestamp.getTime();
                                        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+verb+"_as_IntransitiveVerb_withPrep_"+prep);
	        			
	        		 	Sense sense = new Sense();
	        		 	
	        		 	sense.setReference(new SimpleReference(reference));
	        		 	
	        		 	entry.addSense(sense);
                                        
                                        Provenance provenance = new Provenance();
                                        provenance.setFrequency(1);
                                        entry.setProvenance(provenance);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.addSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				String term = Lemmatizer.getLemma(verb)+"@"+language;
	        				logger.debug("Lemmatized cannonical form:"+term+"/n");
	        				entry.setCanonicalForm(term);
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(verb+"@"+language);
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePPFrame");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
                                                logger.debug("Added Sentence:"+sentence, "");
	        			}
                                        
                                        //debugger.printDependencys(model);
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2",prep));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(verb)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+verb);
                                                }
                                                
	        				
	        			}	
	        			
	        			else if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",prep));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				if(isAlpha(verb)){
                                                    lexicon.add(entry, vector);
	        				
                                                    logger.debug("Found entry:"+entry+"\n");
                                                }
                                                else{
                                                    logger.debug("Dit not add entry, beause of label: "+verb);
                                                }
                                                
	        				
	        			}	
	        			else{
	        				logger.debug("no argument mapping found -> No entry added \n"+"e1_arg:"+e1_arg+"\n"+"e2_arg:"+e2_arg+"\n");
                                                
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
        
        
        private static boolean isAlpha(String label) {
            char[] chars = label.toCharArray();

            for (char c : chars) {
                if(!Character.isLetter(c)) {
                    return false;
                }
            }

            return true;
        }
	

}
