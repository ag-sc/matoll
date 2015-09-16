package de.citec.sc.matoll.io;

import de.citec.sc.matoll.core.Language;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.jena.vocabulary.RDF;
import org.apache.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Preposition;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.utils.Dbnary;
import de.citec.sc.matoll.utils.Stopwords;
import de.citec.sc.matoll.utils.Uby;
import de.citec.sc.matoll.vocabularies.DBLEXIPEDIA;
import de.citec.sc.matoll.vocabularies.LEMON;
import de.citec.sc.matoll.vocabularies.LEXINFO;
import de.citec.sc.matoll.vocabularies.OWL;
import de.citec.sc.matoll.vocabularies.PROVO;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;


public class LexiconSerialization {
    Dbnary dbnary = null;
    Uby uby = null;
    Map<String,String> patternSparqlMapping = new HashMap<>();
    Stopwords stopwords = null;
    boolean removeStopwords = false;
    
        public LexiconSerialization(boolean removestopwords){
            this.dbnary = new Dbnary();
            this.uby = new Uby();
            this.stopwords=new Stopwords();
            this.removeStopwords=removestopwords;
        }
        
        public LexiconSerialization(Map<String,String> sparqlpattern,boolean removestopwords){
            this.dbnary = new Dbnary();
            this.uby = new Uby();
            this.patternSparqlMapping=sparqlpattern;
            this.stopwords=new Stopwords();
            this.removeStopwords=removestopwords;
        }

	public void serialize(Lexicon lexicon, Model model) {
            
                String baseURI = lexicon.getBaseURI();
                
                /*
                Create Resource for each used pattern.
                */
                if(!patternSparqlMapping.isEmpty()){
                    for(String p: patternSparqlMapping.keySet()){
                        model.add(model.createResource("http://dblexipedia.org/Lexicon"), LEMON.sparqlPattern, model.createResource(baseURI+"pattern_"+p));
                        model.add(model.createResource(baseURI+"pattern_"+p), OWL.hasValue, model.createLiteral(patternSparqlMapping.get(p)));
                        model.add(model.createResource(baseURI+"pattern_"+p), LEMON.canonicalForm, model.createResource(baseURI+"pattern_"+p+"#CanonicalForm"));
                        model.add(model.createResource(baseURI+"pattern_"+p+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(p));
                    }
                }
                
                for(Preposition prep : lexicon.getPrepositions()){
                    model.add(model.createResource("http://dblexipedia.org/Lexicon"), LEMON.entry, model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()));
                    model.add(model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()), LEMON.language,model.createLiteral(prep.getLanguage().toString().toLowerCase()));
                    model.add(model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()), LEXINFO.partOfSpeech,model.createResource("http://www.lexinfo.net/ontology/2.0/lexinfo#preposition"));
                    model.add(model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()), LEMON.canonicalForm, model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()+"#CanonicalForm"));
                    model.add(model.createResource(lexicon.getBaseURI()+"preposition_"+prep.getCanonicalForm()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(prep.getCanonicalForm()+"@"+prep.getLanguage().toString().toLowerCase()));
                }
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
                    if(!entry.getURI().contains(" ")){
                        boolean add_entry = true;
                        if(entry.getPreposition()!=null){
                            /*
                            igrnore entries with wired prepositions, such as %
                            */
                            if(!StringUtils.isAlpha(entry.getPreposition().getCanonicalForm())) add_entry = false;
                        }
                        
                        if(add_entry){
                            if(removeStopwords && stopwords.isStopword(entry.getCanonicalForm(), entry.getLanguage())){
                                //do nothing
                            }
                            else{
                                serialize(entry,model,baseURI);
                                model.add(model.createResource("http://dblexipedia.org/Lexicon"), LEMON.entry, model.createResource(entry.getURI()));
                            }
                            	
                        }
                        
                    }
			
		}
		
		model.add(model.createResource("http://dblexipedia.org/Lexicon"), RDF.type, LEMON.Lexicon);	
		
	}

	private void serialize(LexicalEntry entry, Model model, String baseURI) {
            
                int numberReturnedSentences = 5;
                int sentence_counter = 0;
                Set<String> syn_signatures = new HashSet<>();
                		
		model.add(model.createResource(entry.getURI()),RDF.type,LEMON.LexicalEntry);
                
                if(entry.getPreposition()!=null){
                    model.add(model.createResource(entry.getURI()), LEMON.marker, model.createResource(baseURI+"preposition_"+entry.getPreposition().getCanonicalForm()));
                    model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(entry.getCanonicalForm()+" ("+entry.getPreposition().getCanonicalForm()+")"));
                }
                else{
                    model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(entry.getCanonicalForm()));
                }
		
                model.add(model.createResource(entry.getURI()), LEMON.language, model.createLiteral(entry.getLanguage().toString().toLowerCase()));

		model.add(model.createResource(entry.getURI()), LEMON.canonicalForm, model.createResource(entry.getURI()+"#CanonicalForm"));
		model.add(model.createResource(entry.getURI()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(entry.getCanonicalForm()+"@"+entry.getLanguage().toString().toLowerCase()));
                
                
                String dbnary_uri = dbnary.getURI(entry.getCanonicalForm(), entry.getPOS().replace("http://www.lexinfo.net/ontology/2.0/lexinfo#",""),entry.getLanguage());
                if(!dbnary_uri.equals("")){
                    model.add(model.createResource(entry.getURI()), OWL.sameAs, model.createResource(dbnary_uri));

                }
                HashSet<String> uby_uri = uby.getURI(entry.getCanonicalForm(), entry.getPOS().replace("http://www.lexinfo.net/ontology/2.0/lexinfo#",""),entry.getLanguage());
                if(uby_uri.size()>0){
                    for(String tmp_uri : uby_uri){
                        model.add(model.createResource(entry.getURI()), OWL.sameAs, model.createResource(tmp_uri));
                    }
                }
                //System.out.println("entry.getReferences().size():"+entry.getReferences().size());
		if (entry.getReferences().size()>0)
		{
			int ref_counter = 0;
                        for(Sense sense:entry.getSenseBehaviours().keySet()){
                            
                            Reference ref = sense.getReference();
                            ref_counter+=1;
                            //System.out.println("Sense"+Integer.toString(ref_counter)+":"+sense.toString());
                            model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)));
                            
                            Provenance provenance = entry.getProvenance(sense);
                            model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), PROVO.generatedBy, model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)));
                            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");			
                            model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), RDF.type, PROVO.Activity);
                            if (provenance.getStartedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.startedAtTime, model.createLiteral(df.format(provenance.getStartedAtTime())));
                            if (provenance.getEndedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.endedatTime, model.createLiteral(df.format(provenance.getEndedAtTime())));
                            if (provenance.getConfidence() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.confidence, model.createTypedLiteral(provenance.getConfidence()));
                            if (provenance.getAgent() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.associatedWith, model.createLiteral(provenance.getAgent()));
                            if (provenance.getFrequency() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.frequency, model.createTypedLiteral(provenance.getFrequency()));
                            if(provenance.getSentences()!=null){
                                
                               for(Sentence sentence : provenance.getShortestSentences(numberReturnedSentences)){
//                                   model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.sentence, model.createTypedLiteral(sentence));
                                   sentence_counter+=1;
                                   model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.sentence, model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.subjOfProp, model.createLiteral(sentence.getSubjOfProp()));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.objOfProp, model.createLiteral(sentence.getObjOfProp()));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.sentence, model.createLiteral(sentence.getSentence()));
                                   if(sentence.getSubjOfProp_uri()!=null){
                                       model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.subjOfPropURI, model.createLiteral(sentence.getSubjOfProp_uri()));
                                   }
                                   if(sentence.getObjOfProp_uri()!=null){
                                       model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.objOfPropURI, model.createLiteral(sentence.getObjOfProp_uri()));
                                   }
                               }
                            }
                            
                            HashSet<String> patterns = new HashSet<String>();
                            patterns = provenance.getPatternset();
                            for(String pattern : patterns) {
                                if(patternSparqlMapping.isEmpty() || !patternSparqlMapping.containsKey(pattern))
                                    model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.pattern, model.createLiteral(pattern));
                                else{
                                    model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.pattern, model.createResource(baseURI+"pattern_"+pattern));
                                }
                            }

                                
                            if (ref instanceof de.citec.sc.matoll.core.SimpleReference)
                            {
                                SimpleReference reference = (SimpleReference) ref;
                                model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), LEMON.reference, model.createResource(reference.getURI()));
                                int synbehaviour_counter = 0;
                                for(SyntacticBehaviour synbehaviour : entry.getSenseBehaviours().get(sense)){
                                    synbehaviour_counter+=1;
                                    if (synbehaviour != null)
                                    {
                                        model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)));
                                        model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                                        long timestamp = System.currentTimeMillis();
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            
                                            String insert_value = entry.getURI()+"#"+Long.toString(timestamp)+synarc.getValue();
                                            if(synarc.getValue().length()>13 && StringUtils.isNumeric(synarc.getValue().substring(0, 13))){
                                                insert_value = entry.getURI()+"#"+synarc.getValue().substring(13);
                                            }
                                            for(SenseArgument argument : sense.getSenseArgs()){
                                                if(argument.getValue().equals(synarc.getValue())){
                                                    model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),model.createProperty(argument.getArgumenType()),model.createResource(insert_value));
                                                }
                                            }
//                                            model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),LEMON.isA,model.createResource(insert_value));
                                            String syn_signature = synarc.getArgumentType()+insert_value;
                                            if(!syn_signatures.contains(syn_signature)){
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(insert_value));
                                                syn_signatures.add(syn_signature);
                                            }
                                        }

                                    }
                                }


                            }

                            if (ref instanceof de.citec.sc.matoll.core.Restriction)
                            {

                                Restriction reference = (Restriction) ref;
                                model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), LEMON.reference, model.createResource(reference.getURI()));
                                if(reference.getValue().contains("dbpedia.org")){
                                    model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createResource(reference.getValue()));
                                }
                                else model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createLiteral(reference.getValue()));
                                model.add(model.createResource(reference.getURI()), OWL.onProperty, model.createResource(reference.getProperty()));
                                model.add(model.createResource(reference.getURI()), RDF.type, model.createResource("http://www.w3.org/2002/07/owl#Restriction"));
                                
                                int synbehaviour_counter = 0;
                                for(SyntacticBehaviour synbehaviour : entry.getSenseBehaviours().get(sense)){
                                    synbehaviour_counter+=1;
                                    if (synbehaviour != null)
                                    {
                                        model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)));
                                        model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                                        long timestamp = System.currentTimeMillis();
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            String insert_value = entry.getURI()+"#"+Long.toString(timestamp)+synarc.getValue();
                                            if(synarc.getValue().length()>13 && StringUtils.isNumeric(synarc.getValue().substring(0, 13))){
                                                insert_value = entry.getURI()+"#"+synarc.getValue().substring(13);
                                            }
                                            for(SenseArgument argument : sense.getSenseArgs()){
                                                if(argument.getValue().equals(synarc.getValue())){
                                                    model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),model.createProperty(argument.getArgumenType()),model.createResource(insert_value));
                                                }
                                            }
//                                            model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),LEMON.isA,model.createResource(insert_value));
                                            String syn_signature = synarc.getArgumentType()+insert_value;
                                            if(!syn_signatures.contains(syn_signature)){
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(insert_value));
                                                syn_signatures.add(syn_signature);
                                            }                                         }

                                    }

                                }
                            }
                   }
			
			
			
			
		}
		
		if (entry.getPOS() != null)
		{
			model.add(model.createResource(entry.getURI()), LEXINFO.partOfSpeech, model.createResource(entry.getPOS()));

		}
			
		
	}

}
