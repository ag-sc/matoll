package de.citec.sc.matoll.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class StanfordLemmatizer implements Lemmatizer {
	
	protected StanfordCoreNLP pipeline;
	
	public StanfordLemmatizer(String language){
	    // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        //props.put("annotators", "tokenize, ssplit, pos, lemma");
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        /*
         * TODO: Get different languages working
         */
        /*if(language.equals("en")){
        	
            //use default
        }
        if(language.equals("es")){
        	//props.put("pos.model", "resources/spanish.tagger");
        	//props.put("pos.maxlen", "30");
        	props.put("lemma.model", "resources/spanishPCFG.ser.gz");
        	props.put("encoding", "utf-8");
        }
        if(language.equals("de")){
        	System.out.println("in German");
        	props.put("tokenize.model", "resources/german-fast-caseless.tagger");
        	props.put("pos.model", "resources/german-fast-caseless.tagger");
        	//props.put("pos.maxlen", "30");
        	//props.put("parse.model", "resources/germanPCFG.ser.gz");
        	props.put("parse.model", "resources/spanishPCFG.ser.gz");
        	props.put("encoding", "utf-8");
        }*/
        
     // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

	/*
	 * See http://stackoverflow.com/questions/1578062/lemmatization-java
	 */
    private List<String> lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }

        return lemmas;
    }

	@Override
	public String getLemma(String word) {
		String lemma = "";
		for (String x:lemmatize(word)){
			lemma+=" "+(x);
		}
		lemma = lemma.substring(1);
		return lemma;
	}

}
