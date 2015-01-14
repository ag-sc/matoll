package io;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;

import vocabularies.LEMON;
import vocabularies.LEXINFO;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import core.LexicalEntry;
import core.Lexicon;
import core.Sense;
import core.SenseArgument;
import core.SyntacticArgument;
import core.SyntacticBehaviour;


public class LexiconLoader {

	public  LexiconLoader()
	{
		
	}
	
	public Lexicon loadFromFile(String file)
	{
		
		Model model = RDFDataMgr.loadModel(file);
		
		 Statement stmt;
		 Resource subject;
		 
		 Lexicon lexicon = new Lexicon();
		 
		 LexicalEntry entry;
		 
		 StmtIterator iter = model.listStatements(null, RDF.type, LEMON.LexicalEntry);
		 
		 while (iter.hasNext()) {
			 
			 stmt = iter.next();
			 
			 subject = stmt.getSubject();
			 
			 // System.out.println("Processing entry "+subject.toString());

			 Set<SyntacticBehaviour> behaviours = getSyntacticArguments(subject,model);
			 
			 Set<Sense> senses = getSenseArguments(subject,model);
			 
			 // System.out.println(behaviours.size()+" synargs extracted");
			 
			 // System.out.println(senses.size()+" senses extracted");
			 
			 HashMap<String,String> map;
			 
			 if (behaviours.size() > 0)
			 {
				 for (SyntacticBehaviour behaviour: behaviours)
				 {
					 System.out.println(behaviour);
					 
					 for (Sense sense: senses)
					 {
						 // System.out.println(sense);
						 
						 entry = new LexicalEntry();
						 
						 entry.setURI(subject.toString());
					 
						 entry.setCanonicalForm(getCanonicalForm(subject,model));
					 			 
						 entry.setPOS(getPOS(subject,model));
					 
						 entry.setSyntacticBehaviour(behaviour);
						 
						 map = entry.computeMappings(sense);
						 
						 if (map.keySet().size() > 0)
						 {
							 // System.out.println("Mapping found: "+map);
							 
							 entry.setSense(sense);
							 entry.setMappings(map);
							 lexicon.addEntry(entry);
						 }
					 }
					 
				 }
			 }
				 
			else
			{
				
				 entry = new LexicalEntry();
				 
				 entry.setURI(subject.toString());
			 
				 entry.setCanonicalForm(getCanonicalForm(subject,model));
			 				 
				 entry.setPOS(getPOS(subject,model));
				 
				 for (Sense sense: senses)
				 {
					 entry.setSense(sense);
				 }
			 				
			}
				 
				 
		 }
			 
		 
		 return lexicon;
		 
	}
	
	
	private String getPOS(Resource subject, Model model) {
		
		Resource pos;
		
		Statement stmt;
		
		stmt = subject.getProperty(LEXINFO.partOfSpeech);
		
		if (stmt != null)
		{
			pos = (Resource) stmt.getObject();
			
			return pos.toString();
		}
		else
		{
			return null;
		}
		
	}

	private Set<Sense> getSenseArguments(Resource subject, Model model) {
		
		Set<Sense> senses = new HashSet<Sense>();
		
		Sense sen;
		
		Resource sense;
		
		Resource object;
		
		Statement stmt;
				
		Set<SenseArgument> senseArguments = new HashSet<SenseArgument>();
		
		Statement senseArg;
		
		StmtIterator iter = model.listStatements(subject, LEMON.sense, (RDFNode) null);
		 
		 while (iter.hasNext()) {
		
			 stmt = iter.next();
			 
			 sense = (Resource) stmt.getObject();
			 					
			 sen = new Sense();
			
			 senseArguments = new HashSet<SenseArgument>();
			

	 		StmtIterator it = sense.listProperties(LEMON.isA);
		    while( it.hasNext() ) {
		    
		    	senseArg = it.next();
		    	
		    	object = (Resource) senseArg.getObject();
		    	
		    	senseArguments.add(new SenseArgument(senseArg.getPredicate().toString(),object.toString()));
		    }	
		    	
	    	it = sense.listProperties(LEMON.subjOfProp);
		    while( it.hasNext() ) {
		    
		    	senseArg = it.next();
		    	
		    	object = (Resource) senseArg.getObject();
		    	
		    	senseArguments.add(new SenseArgument(senseArg.getPredicate().toString(),object.toString()));
		    	
		    }
		    
		   
	    	it = sense.listProperties(LEMON.objOfProp);
		    while( it.hasNext() ) {
		    
		    	senseArg = it.next();
		    	
		    	object = (Resource) senseArg.getObject();
		    	
		  
		    	senseArguments.add(new SenseArgument(senseArg.getPredicate().toString(),object.toString()));	

		    }
		   
		    sen.setSenseArgs(senseArguments);
		    sen.setReference(getReference(sense,model));
		    senses.add(sen);
		    	
		}
	
		return senses;
		
		
	}

	private static Set<SyntacticBehaviour> getSyntacticArguments(Resource subject, Model model) {
		
		Resource synBehaviour;
		
		Resource object;
		
		Resource prepositionEntry;
		
		String preposition;
		
		Statement synArg;
				
		Set<SyntacticBehaviour> behaviours = new HashSet<SyntacticBehaviour>();
		
		SyntacticBehaviour behaviour;

		Statement stmt;
		
	
		StmtIterator iter = model.listStatements(subject, LEMON.syntacticBehaviour, (RDFNode) null);
		 
		while (iter.hasNext()) {
		
			 stmt = iter.next();
		
			 behaviour = new SyntacticBehaviour();
			 
			 synBehaviour = (Resource) stmt.getObject();
			
			 StmtIterator it = synBehaviour.listProperties(LEXINFO.subject);
			 while( it.hasNext() ) {
		    	
				synArg = it.next();
		    	
			 	object = (Resource) synArg.getObject();
		    	
			 	behaviour.add(new SyntacticArgument(synArg.getPredicate().toString(),object.toString(),null));
		    }	
		    	
	    	it = synBehaviour.listProperties(LEXINFO.object);
		    while( it.hasNext() ) {
		    
			
		    	synArg = it.next();
		    	
		    	object = (Resource) synArg.getObject();
		    	
		    	behaviour.add(new SyntacticArgument(synArg.getPredicate().toString(),object.toString(),null));
		    	
		    }
		    
		    
	    	it = synBehaviour.listProperties(LEXINFO.pobject);
		   
	    	while( it.hasNext() ) {
		    
	    	 	synArg = it.next();
		    	
		    	object = (Resource) synArg.getObject();
		    	
		    	prepositionEntry = (Resource) object.getProperty(LEMON.marker).getObject();
		    	
		    	if (prepositionEntry != null)
		    	{
		    		preposition = getCanonicalForm(prepositionEntry,model);
		    		
		    		// System.out.print("Preposition: "+preposition+"\n");
		    		
		    	}
		    	else
		    	{
		    		preposition = null;
		    	}
		    	
		    	behaviour.add(new SyntacticArgument(synArg.getPredicate().toString(),object.toString(),preposition));	
		    	
		    }
	    	
	    	behaviour.setFrame(getFrame(synBehaviour,model));
	    	
	    	behaviours.add(behaviour);
		   
		}
	
		   
		return behaviours;
	}
	

	private static String getReference(Resource sense, Model model) {
		
		    Statement stmt2;
		    
		    RDFNode reference;
			
			stmt2 = sense.getProperty(LEMON.reference);
			
			if (stmt2 != null)
			{
				reference =  stmt2.getObject();
		
				// fix this for adjectives which have a blank node as reference
				
				if (reference != null && !reference.isAnon())
				{
					return reference.toString();
				}
				else
				{
					// System.out.print("Entry: "+subject+" has no reference!!!\n");
					return null;
				}
			}
			else
			{
				return null;
			}
		
		}	

	private static String getFrame(Resource syntacticBehaviour, Model model) {
				
		String value = null;
		
		Statement stmt;
		
						
		StmtIterator it = syntacticBehaviour.listProperties(RDF.type);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	value = stmt.getObject().toString();
		    	
		    	if (!value.equals("http://lemon-model.net/lemon#Frame"))
		    	{
		    		// System.out.print(value+"\n");
			    	return value;
			    	
		    	}
		    }
			

		return value;
		
	}

	private static String getCanonicalForm(Resource subject, Model model) {
		
		Resource canonicalForm;
		
		Statement stmt;
		
		Literal form;
		
		stmt = subject.getProperty(LEMON.canonicalForm);
		
		if (stmt != null)
		{
			canonicalForm = (Resource) stmt.getObject();
			
			if (canonicalForm != null)
			{
				stmt = canonicalForm.getProperty(LEMON.writtenRep);
				
				if (stmt != null)
				{
				form = (Literal) canonicalForm.getProperty(LEMON.writtenRep).getObject();
					return form.toString();
				}
				else
				{
					return null;
				}
				
			}
			else
			{
				return null;
			}
		}
		else
		{
			// System.out.print("Entry "+subject+" has no canonical form!!!\n");
			return null;
		}		
	}		
}

