/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.sentence.preprocessing.lucene;

import de.citec.sc.sentence.preprocessing.process.Language;
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
                    indexDocs(writer,Paths.get(f));
                } catch (IOException ex) {
                    Logger.getLogger(CreateIndex.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
        
        
    }
    
    
    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
        Stream<String> lines = Files.lines(path);
        lines.forEach(s->indexDoc(writer,s));
    }
    
    
    private static void indexDoc(final IndexWriter writer,String s) {
        try{
            String plain_sentence = getPlainSentece(s);
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
      

    private static String getPlainSentece(String s) {
        String plain_sentence = "";
        for (String item:s.split("\t\t")){
            String tmp[] =item.split("\t");
            plain_sentence+=" "+tmp[1];
        }
        
        return plain_sentence.trim();
    }

    
  
    
}
