package de.citec.sc.matoll.test;


import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swalter
 */
public class TestTagger {
     public static void main(String[] args){
        String path_to_tagger_model ="resources/english-caseless-left3words-distsim.tagger";
        MaxentTagger tagger = new MaxentTagger(path_to_tagger_model);
        System.out.println(tagger.tagString("cat"));
        System.out.println(tagger.tagString("blue"));
        System.out.println(tagger.tagString("sunny"));
     }
}
