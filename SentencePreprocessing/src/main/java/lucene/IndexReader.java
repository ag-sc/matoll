package lucene;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexReader {
	private static String pathIndex = "";
	private static StandardAnalyzer analyzer;
	//private static MMapDirectory index;
	private static DirectoryReader indexReader;
    private static IndexSearcher searcher;
	
	public IndexReader(String pathToIndex) throws IOException{
		IndexReader.pathIndex = pathToIndex;
		IndexReader.analyzer = new StandardAnalyzer(Version.LATEST);
		indexReader = DirectoryReader.open(FSDirectory.open(new File(pathToIndex)));
		searcher = new IndexSearcher(indexReader);
		//IndexReader.index = new MMapDirectory(new File(IndexReader.pathIndex));
	}
	
	
	private List<List<String>> runSearch(String subj, String obj,int sentence_lenght)
			throws IOException {
		HashMap<String, String> cache = new HashMap<String, String>();
		List<List<String>> results = new ArrayList<List<String>>();
		try {
			
			//Generate Boolean query out of term
			BooleanQuery booleanQuery = new BooleanQuery();
			subj = preprocessing(subj);
			obj = preprocessing(obj);
			if(subj.length()<=2||obj.length()<=2) return results;
			String term = subj+" "+obj;
			String[] tmp = term.split(" ");
			//or/and/not has to be checked here, otherwise I would for example remove the or from order, or notice etc
			for (String x : tmp){
				if(!x.equals("")&&!x.toLowerCase().equals("or")&&!x.toLowerCase().equals("and")&&!x.toLowerCase().equals("not")&&x.length()>2){
					try{
						booleanQuery.add(new QueryParser(Version.LUCENE_47, "sentence", analyzer).parse(x), BooleanClause.Occur.MUST);
					}
					catch(Exception e){
						System.err.println("Problem with "+x);
					}
				}
			}

		    int hitsPerPage = 1000;
		    
		    
	        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	        searcher.search(booleanQuery, collector);
	        
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          ArrayList<String> result = new ArrayList<String>();
	          String sentence = d.get("sentence");
	          if((sentence.split("\t\t")).length<=sentence_lenght){
	        	  if(!cache.containsKey(sentence)){
	        		  result.add(sentence);
			          result.add(subj);
			          result.add(obj);
			          results.add(result);
			          cache.put(sentence, "");
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
	
	
	private String preprocessing(String term) {
		term = term.replace("/","");
		term = term.replace(":","");
		term = term.replace("!","");
		term = term.replace("\"","");
		term = term.replace("+","");
		term = term.replace("","");
		return term;
	}


	public List<List<String>> search(List<List<String>> entities){
		List<List<String>> sentences = new ArrayList<List<String>>();
		
		for(List<String> entity : entities){
			try {
				sentences.addAll(this.runSearch(entity.get(0), entity.get(1), 60));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(Integer.toString(sentences.size())+" #sentences");
		return sentences;
	}

}
