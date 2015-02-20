package de.citec.sc.sentence.preprosessing.rdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDF {
	
	
	private static void convertSentenceToRDF(Model default_model,String input_sentence, String propSubj, String propObj, String language, String uri, int counter){
		String class_token = "class"+Integer.toString(counter);
		
		String plain_sentence = "";
		for (String item:input_sentence.split("\t\t")){
			String tmp[] =item.split("\t");
			plain_sentence+=" "+tmp[1];
		}
		plain_sentence = plain_sentence.substring(1);			
		Resource res_class_token = default_model.createResource("class:"+class_token)
				.addProperty(default_model.createProperty("own:subj"),propSubj.toLowerCase())
				.addProperty(default_model.createProperty("own:obj"),propObj.toLowerCase())
				.addProperty(default_model.createProperty("conll:reference"),uri)
				.addProperty(default_model.createProperty("conll:language"),language)
				.addProperty(default_model.createProperty("conll:sentence"),plain_sentence);
		
		
		
		
		int word_number = 0;
		for (String item:input_sentence.split("\t\t")){
			word_number ++;
			String[] x = item.split("\t");
			if (x.length > 1){
				try{
					String id = x[0].replace(" ", "");
					String head_id = x[6].replace(" ", ""); 
					String token = "token"+Integer.toString(counter)+"_"+id;
					String head_token = "token"+Integer.toString(counter)+"_"+head_id;
					
					Resource row_subject = default_model.createResource("token:"+token)
							.addProperty(default_model.createProperty("own:partOf"), res_class_token)
							.addProperty(default_model.createProperty("conll:wordnumber"), Integer.toString(word_number));
					
					
					String form = x[1].toLowerCase().replace("\"","");
					form = form.replace("(", "");
					form = form.replace(")", "");
					form = form.replace(" %", "");
					row_subject.addProperty(default_model.createProperty("conll:form"), form);
					
					
					
					//sometimes set, sometimes not
					if(!x[2].equals("_")){
						row_subject.addProperty(default_model.createProperty("conll:lemma"), x[2].toLowerCase());
					}
					
					
					row_subject.addProperty(default_model.createProperty("conll:cpostag"), x[3]);
					
					row_subject.addProperty(default_model.createProperty("conll:postag"), x[4]);

					row_subject.addProperty(default_model.createProperty("conll:feats"), x[5]);
					
					row_subject.addProperty(default_model.createProperty("conll:head"), default_model.createResource("token:"+head_token));
					
					row_subject.addProperty(default_model.createProperty("conll:deprel"), x[7].replace(" ", ""));
					
					//normally these two are not set and they are also not in the Index in the moment
					/*rdf_list.add("<token:"+token+"> <conll:phead> \""+x[8]+"\"");
					rdf_list.add("<token:"+token+"> <conll:pdeprel> \""+x[9]+"\"");*/
				}
				catch(Exception e){
					
				}
				
			}			
			
		}
		
	}
	
	public static void writeModel(List<List<String>> input_sentences, String path_to_write,String language, String uri) throws IOException{
		if(!path_to_write.endsWith("/"))path_to_write+="/";
		Model default_model = ModelFactory.createDefaultModel();
		int counter = 0;
		for(List<String> input: input_sentences) {
			counter+=1;
			String input_sentence = input.get(0);
			String propSubj = input.get(1);
			String propObj = input.get(2);
			convertSentenceToRDF(default_model,input_sentence,propSubj,propObj,language,uri,counter);
		}
		long timestamp = System.currentTimeMillis();
		OutputStream output_stream_turtel = new FileOutputStream(path_to_write+Long.toString(timestamp)+".ttl");
		default_model.write(output_stream_turtel,"TURTLE");
		output_stream_turtel.close();
		
	}

}
