/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import de.citec.sc.matoll.core.Language;

/**
 *
 * @author swalter
 */
public class DbnaryObject {
    
    private String label = "";
    private String partOfSpeech = "";
    private String dbnary_uri = "";
    private Language language = Language.EN;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getDbnary_uri() {
        return dbnary_uri;
    }

    public void setDbnary_uri(String dbnary_uri) {
        this.dbnary_uri = dbnary_uri;
    }
}
