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
	
	
	private List<List<String>> runSearch(String subj, String obj,int sentence_lenght)
			throws IOException {
            Set<String> cache = new HashSet<>();
            List<List<String>> results = new ArrayList<>();
            try {

                    //Generate Boolean query out of term
                    BooleanQuery booleanQuery = new BooleanQuery();

                    subj = preprocessing(subj);
                    obj = preprocessing(obj);
                    if (!language.equals(Language.JA)) {
                            if(subj.length()<=2||obj.length()<=2) return results;
                    }
                    String term = subj+" "+obj;
                    if (language.equals(Language.JA)) {
                            QueryParser queryParser = new QueryParser("sentence", analyzer);
                            queryParser.setDefaultOperator(QueryParser.Operator.AND);
                            try {
                                    // TODO JapaneseAnalyzer removes stop words by default; leave it that way?
                                    if (queryParser.parse(subj).toString().length()==0 || queryParser.parse(obj).toString().length()==0) return results;
                                    booleanQuery.add(queryParser.parse(term), BooleanClause.Occur.MUST);
                            } catch (Exception e) {
                                    System.err.println("Problem with "+term);
                            }
                    } else {
                            String[] tmp = term.split(" ");
                            //or/and/not has to be checked here, otherwise I would for example remove the or from order, or notice etc
                            for (String x : tmp){
                                    if(!x.equals("")&&!x.toLowerCase().equals("or")&&!x.toLowerCase().equals("and")&&!x.toLowerCase().equals("not")&&x.length()>2){
                                            try{
                                                    booleanQuery.add(new QueryParser("plain", analyzer).parse(x.toLowerCase()), BooleanClause.Occur.MUST);
                                            }
                                            catch(Exception e){
                                                    System.err.println("Problem with "+x);
                                            }
                                    }
                            }
                    }
                //int hitsPerPage = 1000;
                int hitsPerPage = 100;
		    
		    
	        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
	        searcher.search(booleanQuery, collector);
	        
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          ArrayList<String> result = new ArrayList<>();
	          String sentence = d.get("parsed");
	          if((sentence.split("\t\t")).length<=sentence_lenght){
	        	  if(!cache.contains(sentence)){
	        		  result.add(sentence);
			          result.add(subj);
			          result.add(obj);
			          results.add(result);
			          cache.add(sentence);
	        	  }
	        	  
	          }
	          
	        }
		}
		catch(Exception e){
			//System.out.println("Error in term: "+subj+" "+obj);
			//e.printStackTrace();
			//System.out.println();
			//indexReader.close();
			//return results_fail;
		}
		
		return results;
	}
	
        
        private List<List<String>> runStrictSearch(String subj, String obj)
			throws IOException {
            Set<String> cache = new HashSet<>();
            List<List<String>> results = new ArrayList<>();
            try {

                //Generate Boolean query out of term
                BooleanQuery booleanQuery = new BooleanQuery();

                subj = preprocessing(subj).trim();
                obj = preprocessing(obj).trim();
                if (language.equals(Language.JA)) {
                        if(subj.length()<=2||obj.length()<=2) return results;
                }

//                System.out.println(searcher.collectionStatistics("plain").docCount());
//                System.out.println(searcher.collectionStatistics("parsed").docCount());
                booleanQuery.add(new QueryParser("plain", analyzer).parse(subj.toLowerCase()), BooleanClause.Occur.MUST);
                booleanQuery.add(new QueryParser("plain", analyzer).parse(obj.toLowerCase()), BooleanClause.Occur.MUST);
                    
                
                int hitsPerPage = 99;
		    
		    
	        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
	        searcher.search(booleanQuery, collector);
	        
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          ArrayList<String> result = new ArrayList<>();
	          String sentence = d.get("parsed");
                  if(!cache.contains(sentence)){
                      result.add(sentence);
                      result.add(subj);
                      result.add(obj);
                      results.add(result);
                      cache.add(sentence);
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
                        for(List<String> sentence_item : this.runStrictSearch(entity.get(0), entity.get(1))){
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

}
