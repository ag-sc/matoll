/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.core;

import java.util.Objects;

/**
 *
 * @author swalter
 */
public class Sentence {
    String sentence;
    String subjOfProp;
    String objOfProp;
    String subjOfProp_uri;
    String objOfProp_uri;
    
    public Sentence(String sentence, String subjOfProp, String objOfProp){
        this.sentence = sentence;
        this.subjOfProp = subjOfProp;
        this.objOfProp = objOfProp;
    }
    
    public String getSentence() {
        return sentence;
    }

    public String getSubjOfProp() {
        return subjOfProp;
    }

    public String getObjOfProp() {
        return objOfProp;
    }
    public String getSubjOfProp_uri() {
        return subjOfProp_uri;
    }

    public void setSubjOfProp_uri(String subjOfProp_uri) {
        this.subjOfProp_uri = subjOfProp_uri;
    }

    public String getObjOfProp_uri() {
        return objOfProp_uri;
    }

    public void setObjOfProp_uri(String objOfProp_uri) {
        this.objOfProp_uri = objOfProp_uri;
    }
    
    @Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
                Sentence input = (Sentence) obj;
		if (!getSubjOfProp().equals(input.getSubjOfProp()))
			return false;
		if (!getObjOfProp().equals(input.getObjOfProp()))
			return false;
		return true;
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.sentence);
        hash = 67 * hash + Objects.hashCode(this.subjOfProp);
        hash = 67 * hash + Objects.hashCode(this.objOfProp);
        return hash;
    }
    
    @Override
    public String toString(){
        return sentence+";"+subjOfProp+";"+objOfProp;
    }


    
    
}
