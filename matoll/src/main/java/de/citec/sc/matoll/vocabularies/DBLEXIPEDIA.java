/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.vocabularies;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

/**
 *
 * @author swalter
 */
public class DBLEXIPEDIA {
    private static Model defaultModel = ModelFactory.createDefaultModel(); 
    public static Property objOfProp = defaultModel.createProperty("http://dblexipedia.org/info#objOfProp");
    public static Property subjOfProp = defaultModel.createProperty("http://dblexipedia.org/info#subjOfProp");
    public static Property sentence = defaultModel.createProperty("http://dblexipedia.org/info#sentence");
    public static Property objOfPropURI = defaultModel.createProperty("http://dblexipedia.org/info#objOfPropURI");
    public static Property subjOfPropURI = defaultModel.createProperty("http://dblexipedia.org/info#subjOfPropURI");
}
