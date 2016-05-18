package de.citec.sc.sentence.preprocessing.lucene;



import de.citec.sc.sentence.preprocessing.process.Language;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;



public class ReadIndex {

	private Analyzer analyzer;
        private final  IndexReader reader ;
        private final  IndexSearcher searcher;
        private final  Language language;
	
	public ReadIndex(String pathToIndex, Language input_language) throws IOException{
		if(input_language.equals(Language.DE)) this.analyzer = new GermanAnalyzer();
                if(input_language.equals(Language.ES)) this.analyzer = new SpanishAnalyzer();
                if(input_language.equals(Language.EN)) this.analyzer = new EnglishAnalyzer();
                if(input_language.equals(Language.JA)) this.analyzer = new JapaneseAnalyzer();
                this.language=input_language;
                this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(pathToIndex)));
                this.searcher = new IndexSearcher(reader);
                
	}
	
	
        
        public List<List<String>> runSearch(String subj, String obj, String subj_uri, String obj_uri, boolean strict)
			throws IOException {
            Set<String> cache = new HashSet<>();
            List<List<String>> results = new ArrayList<>();
            try {

                //Generate Boolean query out of term
                BooleanQuery booleanQuery = new BooleanQuery();

                subj = preprocessing(subj).trim();
                obj = preprocessing(obj).trim();
                if (!language.equals(Language.JA)) {
                        if(subj.length()<=2||obj.length()<=2) return results;
                }

                if(strict){
                    booleanQuery.add(new QueryParser("plain", analyzer).parse("\""+subj.toLowerCase()+"\""), BooleanClause.Occur.MUST);
                    booleanQuery.add(new QueryParser("plain", analyzer).parse("\""+obj.toLowerCase()+"\""), BooleanClause.Occur.MUST);
                }
                else{
                    booleanQuery.add(new QueryParser("plain", analyzer).parse(subj.toLowerCase()), BooleanClause.Occur.MUST);
                    booleanQuery.add(new QueryParser("plain", analyzer).parse(obj.toLowerCase()), BooleanClause.Occur.MUST);
                }
                
                System.out.println(booleanQuery.toString());
                    
                
                int hitsPerPage = 99;
		    
		    
	        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
	        searcher.search(booleanQuery, collector);
	        
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          ArrayList<String> result = new ArrayList<>();
	          String sentence = d.get("parsed");
                  String plain_sentence = getPlainSentence(sentence);
                  //System.out.println(plain_sentence);
                  //System.out.println(subj);
                  //System.out.println(obj);
                  //if(!cache.contains(plain_sentence+subj+obj) && plain_sentence.contains(subj) && plain_sentence.contains(obj)
                  //        && !subj.contains(obj) && !obj.contains(subj) && !subj.equals(obj)){
                  if(!cache.contains(plain_sentence+subj+obj)){
                      result.add(sentence);
                      result.add(subj);
                      result.add(obj);
                      result.add(subj_uri);
                      result.add(obj_uri);
                      results.add(result);
                      cache.add(plain_sentence+subj+obj);
                  }
	        	  
	          
	          
	        }
		}
		catch(Exception e){
			System.out.println("Error in term: "+subj+" "+obj);
		}
		
		return results;
	}
        
	
	private String preprocessing(String term) {
		term = term.replace("/","");
		term = term.replace(":","");
		term = term.replace("!","");
		term = term.replace("\"","");
		term = term.replace("+","");
		// hyphen seems to affect output of QueryParser
		// not sure if this may affect e.g. processing of dates
		// for other languages
		if (language.equals(Language.JA)) term = term.replace("-", "");
		// JapaneseAnalyzer does not like stars
		if (language.equals(Language.JA)) term = term.replace("*", "");
		return term;
	}


	public List<List<String>> search(List<List<String>> entities){
		List<List<String>> sentences = new ArrayList<List<String>>();
                Set<String> unique_sentence = new HashSet<String>();
		
                entities.stream().forEach((entity) -> {
                    try {
                        for(List<String> sentence_item : this.runSearch(entity.get(0), entity.get(1),entity.get(2),entity.get(3),true)){
                            if(!unique_sentence.contains(sentence_item.get(0))){
                                sentences.add(sentence_item);
                                unique_sentence.add(sentence_item.get(0));
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
			}
            });
		
		System.out.println(Integer.toString(sentences.size())+" #sentences");
		return sentences;
	}

    private String getPlainSentence(String sentence) {
        String output = "";
        if (sentence.contains("\t\t")){
            String[] tmp = sentence.split("\t\t");
            for(String t : tmp){
                output+=" "+t.split("\t")[1];
            }
        }
        
        return output.trim();
    }

}
