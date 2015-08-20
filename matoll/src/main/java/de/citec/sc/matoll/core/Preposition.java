
package de.citec.sc.matoll.core;

/**
 *
 * @author swalter
 */
public class Preposition {
    Language language = Language.EN;
    String canonical_form = "";
    public Preposition(Language language, String canonical_form) {
        this.language = language;
        this.canonical_form = canonical_form;
    }
    
    public Language getLanguage(){
        return language;
    }
    
    public String getCanonicalForm(){
        return canonical_form;
    }

}
