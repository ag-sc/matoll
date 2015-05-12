/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.HashMap;

/**
 *
 * @author swalter
 */
public interface Coreference {
    
    void computeCoreference(Model model, HashMap<String,String> Resource2Lemma, HashMap<Integer,String> Int2NodeMapping, HashMap<String,Integer> Node2IntMapping, HashMap<String,String> senseArgs);
    
}
