/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.evaluation;

import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;

/**
 *
 * @author swalter
 */
public class TestEvaluation {
    public static void main(String[] args){
        LexiconLoader loader = new LexiconLoader();
        Lexicon gold = loader.loadFromFile("../lexica/dbpedia_en.rdf");
        System.out.println("Loaded gold");
        //Lexicon lexicon = loader.loadFromFile("../lexica/dbpedia_en.rdf");
        Lexicon lexicon = loader.loadFromFile("/Users/swalter/Downloads/dbpedia2014_beforeTraining.ttl");
        System.out.println("Loaded automatic lex");
        LexiconEvaluation eval = new LexiconEvaluation();
        
        eval.evaluate(lexicon,gold);
        System.out.println(eval.getPrecision("lemma")+"\t"+eval.getRecall("lemma")+"\t"+eval.getFMeasure("lemma")+"\t"+eval.getPrecision("syntactic")+"\t"+eval.getRecall("syntactic")+"\t"+eval.getFMeasure("syntactic")+"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")+"\t"+eval.getFMeasure("mapping"));

        
        
    }
}
