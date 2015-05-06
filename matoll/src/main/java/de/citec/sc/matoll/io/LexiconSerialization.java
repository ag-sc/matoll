package de.citec.sc.matoll.io;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.vocabularies.LEMON;
import de.citec.sc.matoll.vocabularies.LEXINFO;
import de.citec.sc.matoll.vocabularies.OWL;
import de.citec.sc.matoll.vocabularies.PROVO;

public class LexiconSerialization {

	public void serialize(Lexicon lexicon, Model model) {
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
			serialize(entry,model);
			model.add(model.createResource("Lexicon"), LEMON.entry, model.createResource(entry.getURI()));	
		}
		
		model.add(model.createResource("Lexicon"), RDF.type, LEMON.Lexicon);	
		
	}

	private void serialize(LexicalEntry entry, Model model) {
		
		model.add(model.createResource(entry.getURI()),RDF.type,LEMON.LexicalEntry);
		
		model.add(model.createResource(entry.getURI()), LEMON.canonicalForm, model.createResource(entry.getURI()+"_CanonicalForm"));
		model.add(model.createResource(entry.getURI()+"_CanonicalForm"), LEMON.writtenRep, model.createLiteral(entry.getCanonicalForm()));
		

		if (entry.getReference() != null)
		{
			
			// <rdf:Description rdf:about="http://github.com/cunger/lemon.dbpedia/target/dbpedia_en_9#female__adjective/reference">
			// <owl:hasValue rdf:resource="http://dbpedia.org/resource/Female"/>
			// <owl:onProperty rdf:resource="http://dbpedia.org/ontology/gender"/>
			// <rdf:type rdf:resource="http://www.w3.org/2002/07/owl#Restriction"/></rdf:Description>
			
		
			if (entry.getReference() instanceof de.citec.sc.matoll.core.SimpleReference)
			{
				SimpleReference reference = (SimpleReference) entry.getReference();
				
				model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"_Sense"));
				model.add(model.createResource(entry.getURI()+"_Sense"), LEMON.reference, model.createResource(reference.toString()));
                                for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                                    if (synbehaviour != null)
                                    {
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            model.add(model.createResource(entry.getURI()+"_Sense"),LEMON.isA,model.createResource(entry.getURI()+"_arg"+synarc.getValue()));                    
                                         }

                                    }
                                }
			}
			
			if (entry.getReference() instanceof de.citec.sc.matoll.core.Restriction)
			{
				Restriction reference = (Restriction) entry.getReference();
				
				model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"_Sense"));
				model.add(model.createResource(entry.getURI()+"_Sense"), LEMON.reference, model.createResource(reference.getURI()));
				model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createLiteral(reference.getValue()));
				model.add(model.createResource(reference.getURI()), OWL.onProperty, model.createLiteral(reference.getProperty()));
				model.add(model.createResource(reference.getURI()), RDF.type, model.createResource("http://www.w3.org/2002/07/owl#Restriction"));
                                
                                for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                                    if (synbehaviour != null)
                                    {
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            model.add(model.createResource(entry.getURI()+"_Sense"),LEMON.isA,model.createResource(entry.getURI()+"_arg"+synarc.getValue()));                    
                                         }

                                    }
                                }
				
			}
			
			
			
		}
		
		if (entry.getPOS() != null)
		{
			model.add(model.createResource(entry.getURI()), LEXINFO.partOfSpeech, model.createResource(entry.getPOS()));

		}
			
                /*
                TODO: Check!
                */
                int synbehaviour_counter = 0;
                for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                    synbehaviour_counter+=1;
                    if (synbehaviour != null)
                    {
			model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"_SynBehaviour"+Integer.toString(synbehaviour_counter)));
			model.add(model.createResource(entry.getURI()+"_SynBehaviour"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                            //synarc.getArgumentType();
                            model.add(model.createResource(entry.getURI()+"_SynBehaviour"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(entry.getURI()+"_arg"+synarc.getValue()));                    
                         }
			
                    }
                }
		
	
                /*
                this has to be run under Syntactic Behaviour
                */
                /*for( Sense sense:entry.getSense()){
                    HashMap<String, String> argumentMap = entry.computeMappings(sense);
                    
                    //entry.setMappings(argumentMa);

                    Resource res;
                    
                    for (String synArg: argumentMap.keySet())
                        {
                        res = model.createResource();

                        //TODO: Put this to syntactic behavioir  model.add(model.createResource(entry.getURI()+"_SynBehaviour"),model.createProperty(synArg),res);
                        model.add(model.createResource(entry.getURI()+"_Sense"),model.createProperty(argumentMap.get(synArg)),res);
                        }

                }*/
                
                /*
                Raus!
                int sense_counter = 0;
                for( Sense sense:entry.getSense()){
                    HashMap<String, String> argumentMap = entry.computeMappings(sense);
                    Resource res;
                    sense_counter+=1;
                    for (String synArg: argumentMap.keySet())
                        {
                        res = model.createResource(synArg);
                        model.add(model.createResource(entry.getURI()+"_Sense"+Integer.toBinaryString(sense_counter)),model.createProperty(argumentMap.get(synArg)),res);
                        }

                }*/
		
		
		Provenance provenance = entry.getProvenance();
		
		if (provenance != null)
		{
			SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");			
			model.add(model.createResource(entry.getURI()), PROVO.generatedBy, model.createResource(entry.getURI()+"_Activity"));
			model.add(model.createResource(entry.getURI()+"_Activity"), RDF.type, PROVO.Activity);
			
			if (provenance.getStartedAtTime() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.startedAtTime, model.createLiteral(df.format(provenance.getStartedAtTime())));
			
			if (provenance.getEndedAtTime() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.endedatTime, model.createLiteral(df.format(provenance.getEndedAtTime())));
			
			if (provenance.getConfidence() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.confidence, model.createLiteral(provenance.getConfidence().toString()));
		
			if (provenance.getAgent() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.associatedWith, model.createResource(provenance.getAgent()));
			
                       	if (provenance.getFrequency() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.frequency, model.createResource(provenance.getFrequency().toString()));

			
			
			
		}
		
	}

}
