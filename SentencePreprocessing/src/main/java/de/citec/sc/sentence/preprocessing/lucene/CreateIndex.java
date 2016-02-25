/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.sentence.preprocessing.lucene;

import de.citec.sc.sentence.preprocessing.process.Language;

import static de.citec.sc.sentence.preprocessing.process.Language.JA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/**
 *
 * @author swalter
 */
public class CreateIndex {
    
    
    public static void main(String[] args) throws IOException {
        Analyzer analyzer = null;
        
        List<String> files = new ArrayList<>();
        files.add("/Users/swalter/Downloads/german_sentences_reduced.txt");
        String indexPath = "/Users/swalter/Index/GermanIndexReduced/";
        Language language = Language.DE;
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        
        //files.add("/home/bettina/CITEC/MATOLL/preprocessSentences/idealSentences/idealSents_mecab_jdepp_rmvPunct_CoNLLU");
        //String indexPath = "/home/bettina/CITEC/MATOLL/preprocessSentences/idealSentences/index";
        //Language language = Language.JA;
        //Directory dir = FSDirectory.open(Paths.get(indexPath));
        
        if(language.equals(Language.DE)) analyzer = new GermanAnalyzer();
        if(language.equals(Language.ES)) analyzer = new SpanishAnalyzer();
        if(language.equals(Language.EN)) analyzer = new EnglishAnalyzer();
        if(language.equals(Language.JA)) analyzer = new JapaneseAnalyzer();
        
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE);
        iwc.setRAMBufferSizeMB(12000);
        try (IndexWriter writer = new IndexWriter(dir, iwc)) {
            files.forEach(f->{
                try {
                    indexDocs(writer,Paths.get(f), language);
                } catch (IOException ex) {
                    Logger.getLogger(CreateIndex.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
        
        
    }
    
    
    static void indexDocs(final IndexWriter writer, Path path, Language language) throws IOException {
        Stream<String> lines = Files.lines(path);
        lines.forEach(s->indexDoc(writer,s,language));
    }
    
    
    private static void indexDoc(final IndexWriter writer,String s, Language language) {
        try{
            String plain_sentence = getPlainSentence(s, language);
            Document doc = new Document();
            Field sentence = new TextField("plain",plain_sentence.toLowerCase(),Field.Store.NO);
            Field parsed_sentence = new TextField("parsed",s,Field.Store.YES);
            doc.add(sentence);
            doc.add(parsed_sentence);
            writer.addDocument(doc);
            
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Problem with:"+s);
        }
        
    }
      

    private static String getPlainSentence(String s, Language language) {
        String plain_sentence = "";
        if (language.equals("ja")) {
			s = s.split("\t\t\t")[0];
		}
         
    	for (String item:s.split("\t\t")){
    		String tmp[] =item.split("\t");
    		
    		if (language.equals(JA)) {
				plain_sentence+=tmp[1];
			} else {
				plain_sentence+=" "+tmp[1];
			}
    	}
    	System.out.println("plain_sentence: "+plain_sentence);
        return plain_sentence.trim();
    }

    
  
    
}
