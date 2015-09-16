package de.citec.sc.matoll.io;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

import de.citec.sc.matoll.core.Language;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Preposition;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.vocabularies.DBLEXIPEDIA;
import de.citec.sc.matoll.vocabularies.LEMON;
import de.citec.sc.matoll.vocabularies.LEXINFO;
import de.citec.sc.matoll.vocabularies.OWL;
import de.citec.sc.matoll.vocabularies.PROVO;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;



public class LexiconLoader {

	public  LexiconLoader()
	{
		
	}
	
	public Lexicon loadFromFile(String file)
	{
		
		Model model = RDFDataMgr.loadModel(file);
		
		 Statement stmt;
		 Resource loaded_entry;
		 
		 Lexicon lexicon = new Lexicon();
                 		 
		 
		 
		 StmtIterator iter = model.listStatements(null, LEMON.canonicalForm, (RDFNode) null);
		 
		 while (iter.hasNext()) {
			 
			 stmt = iter.next();
			 
			 loaded_entry = stmt.getSubject();
			 
                         /*
                         loaded_entry is one single entry.
                         */
			 //System.out.println("Processing entry "+loaded_entry.toString());
                         
                         Language language = getLanguage(loaded_entry,model);
                         
                         /*
                         Language
                         */
                         LexicalEntry entry = new LexicalEntry(language);
                         
                         entry.setURI(loaded_entry.toString());
                         
                         /*
                         Do not read in sameAs, at it is generated automatically in the LexiconSerilization (and it is not needed elsewhere)
                         */
                         
                         /*
                         Set POS
                         */
                         String pos = getPOS(loaded_entry,model);
                         entry.setPOS(pos);
                         
                         /*
                         Canonnical Form
                         */
                         entry.setCanonicalForm(getCanonicalForm(loaded_entry,model));
                         
                         
                         entry.setPreposition(new Preposition(language,getPreposition(loaded_entry,model)));
                         
                         /*
                         Sense, corresponding SynBehaviour + Provenance
                         */
                         HashMap<Sense,HashSet<SyntacticBehaviour>> hashsetSenseBehaviour = new HashMap<Sense,HashSet<SyntacticBehaviour>>();
                         HashMap<Sense,Provenance> mappingReferenceProvenance = new HashMap<Sense,Provenance>();
                         getSenses(loaded_entry,model,hashsetSenseBehaviour,mappingReferenceProvenance);
                         for(Sense sense : hashsetSenseBehaviour.keySet()) entry.addAllSyntacticBehaviour(hashsetSenseBehaviour.get(sense), sense);
                         for(Sense sense: mappingReferenceProvenance.keySet()) entry.addProvenance(mappingReferenceProvenance.get(sense), sense);
                         
                         if(pos!=null)lexicon.addEntry(entry);
                         
				 
				 
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



	private static HashSet<SyntacticBehaviour> getSyntacticArguments(Resource loaded_entry, Model model, HashSet<String> sense_argument_values) {
		
		Resource synBehaviour;
		
		Resource object;
		
		Resource prepositionEntry;
		
		Statement prepStatement;
		
		String preposition;
		
		Statement synArg;
				
		HashSet<SyntacticBehaviour> behaviours = new HashSet<SyntacticBehaviour>();
		
		SyntacticBehaviour behaviour;
		
		Property predicate;

		Statement stmt;
		
		StmtIterator iter = model.listStatements(loaded_entry, LEMON.syntacticBehaviour, (RDFNode) null);
		 
		while (iter.hasNext()) {
                    stmt = iter.next();
                    
                    behaviour = new SyntacticBehaviour();
                   synBehaviour = (Resource) stmt.getObject();
                   StmtIterator it = model.listStatements(synBehaviour, null, (RDFNode) null); 
                   HashSet<String> argument_value_list = new HashSet<String>();
                     while( it.hasNext() ) {
                        synArg = it.next();
                        object = (Resource) synArg.getObject();

                        predicate = synArg.getPredicate();
                        String argument_value = object.toString();
                        /*
                        Check, if argument_value have a BaseURI
                        */
                        if(argument_value.contains("#")) argument_value = argument_value.split("#")[1];
                        if(argument_value.length()>13 && StringUtils.isNumeric(argument_value.substring(0, 13))) argument_value = argument_value.substring(13);
                        prepStatement = object.getProperty(LEMON.marker);
                        preposition = null;

                        if (prepStatement != null)
                        {
                            prepositionEntry = (Resource) prepStatement.getObject();
                            if (prepositionEntry != null)
                            {
                                    preposition = getCanonicalForm(prepositionEntry,model);
                                    // //System.out.print("Preposition: "+pattern_name+"\n");
                            }
                            else
                            {
                                    preposition = null;
                            }
                        }
                        /*
                        Only way to map sense with syntactic arguments is the unique identifier for the syntactic argument.
                        */
                        if (!predicate.toString().equals(RDF.type.toString())){
                            behaviour.add(new SyntacticArgument(predicate.toString(),argument_value,preposition));
                            argument_value_list.add(argument_value);
                        }
                    }	
                     boolean add_bahaviour = true;
                     for(String p : argument_value_list){
                         if(!sense_argument_values.contains(p))add_bahaviour = false;
                     }
                     if(add_bahaviour){
                        behaviour.setFrame(getFrame(synBehaviour,model));
                        behaviours.add(behaviour);
                     }
                     
                }
		
	
		return behaviours;
	}
	

	private static Resource getReference(Resource sense, Model model) {

            Resource uri; 
            
            try {
                uri = (Resource) sense.getProperty(LEMON.reference).getObject();
                
                if (uri.isAnon()) {
                   return null;
                } 
                else {
                   return uri;
                }
            } 
            catch (Exception e) {
                return null;
            }
        }
            
        private static String getPropertyObject(Resource r, Property p) {
            
            try {
                return r.getProperty(p).getObject().toString();
            }
            catch (Exception e) {
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
                            // //System.out.print(value+"\n");
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
                                        if (form.toString().contains("@")){
                                            return form.toString().split("@")[0];
                                        }
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
			// //System.out.print("Entry "+loaded_entry+" has no canonical form!!!\n");
			return null;
		}		
	}


    private Language getLanguage(Resource subject, Model model) {
        String language;

        Statement stmt;

        stmt = subject.getProperty(LEMON.language);

        if (stmt != null)
        {
                language = stmt.getLiteral().toString().toLowerCase();
                if(language.equals("en")) return Language.EN;
                if(language.equals("de")) return Language.DE;
                if(language.equals("es")) return Language.ES;
                if(language.equals("ja")) return Language.JA;
        }
        /*
        default is English, if no language is given for the entry
        */
        return Language.EN;
    }

    
    private void getSenses(Resource loaded_entry, Model model, HashMap<Sense, HashSet<SyntacticBehaviour>> hashsetSenseBehaviour, HashMap<Sense, Provenance> mappingReferenceProvenance) {
     
        Statement stmt;
        StmtIterator iter = loaded_entry.listProperties(LEMON.sense);
        while(iter.hasNext() ) {
            stmt = iter.next();
            RDFNode rdf_sense = stmt.getObject();
            Sense sense = new Sense();
            
            Resource reference = getReference((Resource)rdf_sense,model);
            if (reference != null) {
                // check whether it's a restriction class
                String property = getPropertyObject(reference,OWL.onProperty);
                String value    = getPropertyObject(reference,OWL.hasValue);

                if (property != null && value != null) {
                    sense.setReference(new Restriction(reference.toString(),value,property));
                }
                else {
                    sense.setReference(new SimpleReference(reference.toString()));
                }
            }
            List<SenseArgument> sense_arguments = getSenseArguments(rdf_sense,model);
            HashSet<String> sense_argument_values = new HashSet<String>();
            for(SenseArgument argument : sense_arguments) {
                sense.addSenseArg(argument);
                sense_argument_values.add(argument.getValue().toString());
            }           
            
//            HashSet<SyntacticBehaviour> list_behaviours = getBehaviours(rdf_sense,loaded_entry, model);
            HashSet<SyntacticBehaviour> list_behaviours = getSyntacticArguments(loaded_entry, model,sense_argument_values);
            hashsetSenseBehaviour.put(sense, list_behaviours);
            
            Provenance provenance = getProvenance(rdf_sense,loaded_entry, model);
            mappingReferenceProvenance.put(sense, provenance);
        }
    }

   
    
    private Provenance getProvenance(RDFNode rdf_sense, Resource loaded_entry,Model model) {
        Statement stmt;
        Provenance provenance = new Provenance();
        StmtIterator iter = model.listStatements(null, PROVO.generatedBy, (RDFNode) null);
        int frequency = 0;
        double confidence = 0.0;
        String agent = "";
        Date starttime = null;
        Date endtime = null;
        List<Sentence> sentences = new ArrayList<>();
        HashSet<String> patterns = new HashSet<String>();
        String preposition;
        while (iter.hasNext()) {
             stmt = iter.next();
             Resource activity;
             if(stmt.getSubject().equals(rdf_sense)){
                 activity= (Resource) stmt.getObject();
                 try{
                     Statement stmt_frequency = activity.getProperty(PROVO.frequency);
                     if (stmt_frequency != null) {
                         frequency = activity.getProperty(PROVO.frequency).getInt();
                         provenance.setFrequency(frequency);
                     }
                     
                 }
                 catch(Exception e){};
                 
                 try{
                     Statement stmt_confidence = activity.getProperty(PROVO.confidence);
                     if (stmt_confidence != null) {
                         confidence = activity.getProperty(PROVO.confidence).getDouble();
                         provenance.setConfidence(confidence);
                     }
                 }
                 catch(Exception e){};
                 
                 try{
                     Statement stmt_agent = activity.getProperty(PROVO.associatedWith);
                     if (stmt_agent != null) agent = activity.getProperty(PROVO.associatedWith).toString();
                 }
                 catch(Exception e){};
                 
                 try{
                     SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");
                     Statement stmt_starttime = activity.getProperty(PROVO.startedAtTime);
                     if (stmt_starttime != null) starttime = df.parse(activity.getProperty(PROVO.startedAtTime).toString());
                 }
                 catch(Exception e){};
                 
                 try{
                     SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");
                     Statement stmt_endtime = activity.getProperty(PROVO.endedatTime);
                     if (stmt_endtime != null) endtime = df.parse(activity.getProperty(PROVO.endedatTime).toString());
                 }
                 catch(Exception e){};
                 
                 /*
                 Add Pattern
                 */
                 
                 try{
                     
                     StmtIterator iter_pattern = activity.listProperties(PROVO.pattern);
                     while(iter_pattern.hasNext() ) {

                         Statement stmt_pattern = iter_pattern.next();
                         if (stmt_pattern != null) {
                             patterns.addAll(getPatternWrittenRepresentation(stmt_pattern.getObject(),model));
                         }
                    }
                     
                     
                 }
                 catch(Exception e){};
                 
                 /*
                 Add Sentences
                 */
                 try{
                     
                    StmtIterator iter_sentence = activity.listProperties(PROVO.sentence);
                     while(iter_sentence.hasNext() ) {

                         Statement stmt_sentence = iter_sentence.next();
                         if (stmt_sentence != null) {
                             
                             Sentence sentence = getSentenceObject(stmt_sentence.getObject().toString(),model);
                             if(sentence!=null) sentences.add(sentence);
                             //sentences.add(stmt_sentence.getObject().toString());
                         }
                    }
                     
                 }
                 catch(Exception e){e.printStackTrace();};
                 
             }
        }

        
        if(!agent.equals(""))provenance.setAgent(agent);
        if(starttime!=null)provenance.setStartedAtTime(starttime);
        if(endtime!=null)provenance.setEndedAtTime(endtime);
        provenance.setPatternset(patterns);
        provenance.setSentences(sentences);                
        
        return provenance;
    }

//    private HashSet<SyntacticBehaviour> getBehaviours(RDFNode rdf_sense, Resource loaded_entry, Model model) {
//        Statement stmt;
//        HashSet<SyntacticBehaviour> hashset = new HashSet<SyntacticBehaviour>();
//        
//        StmtIterator iter = model.listStatements(null, LEMON.syntacticBehaviour, (RDFNode) null);
//
//        while (iter.hasNext()) {
//             stmt = iter.next();
//             Resource rdf_behaviour;
//             if(stmt.getSubject().equals(rdf_sense)){
//                 rdf_behaviour= (Resource) stmt.getObject();
//                 SyntacticBehaviour behaviour = new SyntacticBehaviour();
//                 
//                 /*
//                 Get Frame
//                 */
//                 String frame = getFrame(rdf_behaviour,model);
//                 behaviour.setFrame(frame);
//                 
//                 /*
//                 GetArguments
//                 */
//                 //behaviour.getArguments(rdf_sense, rdf_behaviour,model));
//                 
//                 
//                 
//                 if(frame!=null) hashset.add(behaviour);
//             }
//             
//             
//             
//             
//        }
//        
//        return hashset;
//    }

    private List<SenseArgument> getSenseArguments(RDFNode rdf_sense, Model model) {
        List<SenseArgument> senseArguments = new ArrayList<SenseArgument>();
	Statement senseArg;
        Resource sense = (Resource) rdf_sense;
        Resource object;
	Statement stmt;
        StmtIterator it = sense.listProperties(LEMON.isA);
            while( it.hasNext() ) {

                senseArg = it.next();

                object = (Resource) senseArg.getObject();
                String predicate_string = senseArg.getPredicate().toString();
                String object_string = object.toString();
                /*
                Check, if argument_value have a BaseURI
                */
                if(object_string.contains("#")) object_string = object_string.split("#")[1];
                if(object_string.length()>13 && StringUtils.isNumeric(object_string.substring(0, 13))) object_string = object_string.substring(13);
                senseArguments.add(new SenseArgument(predicate_string,object_string));
            }	

        it = sense.listProperties(LEMON.subjOfProp);
            while( it.hasNext() ) {

                senseArg = it.next();

                object = (Resource) senseArg.getObject();
                String predicate_string = senseArg.getPredicate().toString();
                String object_string = object.toString();
                /*
                Check, if argument_value have a BaseURI
                */
                if(object_string.contains("#")) object_string = object_string.split("#")[1];
                if(object_string.length()>13 && StringUtils.isNumeric(object_string.substring(0, 13))) object_string = object_string.substring(13);
                senseArguments.add(new SenseArgument(predicate_string,object_string));

            }


        it = sense.listProperties(LEMON.objOfProp);
            while( it.hasNext() ) {

                senseArg = it.next();

                object = (Resource) senseArg.getObject();
                String predicate_string = senseArg.getPredicate().toString();
                String object_string = object.toString();
                /*
                Check, if argument_value have a BaseURI
                */
                if(object_string.contains("#")) object_string = object_string.split("#")[1];
                if(object_string.length()>13 && StringUtils.isNumeric(object_string.substring(0, 13))) object_string = object_string.substring(13);
                senseArguments.add(new SenseArgument(predicate_string,object_string));	

            }
        return senseArguments;
    }
    


    private String getPreposition(Resource subject, Model model) {
        Resource preposition_entry;
        Resource canonicalForm;

        Statement stmt;

        Literal form;
        String preposition = "";
            stmt = subject.getProperty(LEMON.marker);
            if(stmt!=null){
                String query = "Select ?prep WHERE{"
                        + "<"+stmt.getObject().toString()+"> <"+LEMON.canonicalForm+">  ?canonicalForm .\n" 
                        +"  ?canonicalForm <"+LEMON.writtenRep+"> ?prep }";
                QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
                ResultSet rs = qExec.execSelect() ;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();
                         try{
                                 preposition = qs.get("?prep").toString();	
                          }
                         catch(Exception e){
                        }
                     }
                }
                catch(Exception e){
                }
                qExec.close() ;  
            }
        if(preposition.contains("@")) return preposition.split("@")[0];
        return preposition;
    }

    private List getPatternWrittenRepresentation(RDFNode pattern,Model model) {
        List<String> pattern_name = new ArrayList<>();

        String query = "Select DISTINCT ?form WHERE{"
                + "<"+pattern+"> <"+LEMON.canonicalForm+"> ?canonicalForm. "
                + "?canonicalForm <"+LEMON.writtenRep+"> ?form}";
        QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
        ResultSet rs = qExec.execSelect() ;

        try {
         while ( rs.hasNext() ) {
                 QuerySolution qs = rs.next();
                 try{
                         pattern_name.add(qs.get("?form").toString());	
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
        
        return pattern_name;
    }

    private Sentence getSentenceObject(String subject, Model model) {
       
        Sentence sentence = null;
        StmtIterator iter = model.listStatements(model.createResource(subject), DBLEXIPEDIA.sentence, (RDFNode) null);
        Statement stmt;
        String plain_sentence = null;
        String subjOfProp  = null;
        String objOfProp = null;
        
        while (iter.hasNext()) {
             stmt = iter.next();
             plain_sentence = stmt.getObject().toString();
        }
        
        
        iter = model.listStatements(model.createResource(subject), DBLEXIPEDIA.subjOfProp, (RDFNode) null);
        while (iter.hasNext()) {
             stmt = iter.next();
             subjOfProp = stmt.getObject().toString();
        }
        
        iter = model.listStatements(model.createResource(subject), DBLEXIPEDIA.objOfProp, (RDFNode) null);
        while (iter.hasNext()) {
             stmt = iter.next();
             objOfProp = stmt.getObject().toString();
        }
        
        if(objOfProp!=null && subjOfProp!=null && plain_sentence!=null){
            sentence = new Sentence(plain_sentence,subjOfProp,objOfProp);
            //System.out.println(sentence.toString());
        }
        
        
        return sentence;
        
    }
        
        
        
}

