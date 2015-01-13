package serialization;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import vocabularies.LEMON;
import vocabularies.LEXINFO;
import vocabularies.PROVO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import core.LexicalEntry;
import core.Lexicon;
import core.Provenance;

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
		model.add(model.createResource(entry.getURI()+"_CanonicalForm"), LEMON.canonicalForm, model.createLiteral(entry.getCanonicalForm()));
		
		
		
		if (entry.getReference() != null)
		{
			model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"_Sense"));
			model.add(model.createResource(entry.getURI()+"_Sense"), LEMON.reference, model.createResource(entry.getReference()));
			
		}
		
		if (entry.getPOS() != null)
		{
			model.add(model.createResource(entry.getURI()), LEXINFO.partOfSpeech, model.createResource(entry.getPOS()));

		}
			
		if (entry.getFrameType() != null)
		{
			model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"_SynBehaviour"));
			model.add(model.createResource(entry.getURI()+"_SynBehaviour"), RDF.type, model.createResource(entry.getFrameType()));
			
		}
	
		entry.computeMappings();
		
		Resource res;

		HashMap<String,String> argumentMap;
		
		argumentMap = entry.getArgumentMap();
		
		for (String synArg: argumentMap.keySet())
		{
			res = model.createResource();
			
			model.add(model.createResource(entry.getURI()+"_SynBehaviour"),model.createProperty(synArg),res);
			model.add(model.createResource(entry.getURI()+"_Sense"),model.createProperty(argumentMap.get(synArg)),res);
		}
		
		Provenance provenance = entry.getProvenance();
		
		if (provenance != null)
		{
			SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");			
			model.add(model.createResource(entry.getURI()), PROVO.generatedBy, model.createResource(entry.getURI()+"_Activity"));
			model.add(model.createResource(entry.getURI()+"_Activity"), RDF.type, PROVO.Activity);
			
			if (provenance.getStartedAtTime() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.startedAtTime, model.createLiteral(df.format(provenance.getStartedAtTime())));
			
			if (provenance.getEndedAtTime() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.endedatTime, model.createLiteral(df.format(provenance.getEndedAtTime())));
			
			if (provenance.getConfidence() != null) model.add(model.createResource(entry.getURI()+"_Activity"), model.createProperty("confidence"), model.createLiteral(provenance.getConfidence().toString()));
		
			if (provenance.getAgent() != null) model.add(model.createResource(entry.getURI()+"_Activity"), PROVO.associatedWith, model.createResource(provenance.getAgent()));
			
		}
		
	}

}
