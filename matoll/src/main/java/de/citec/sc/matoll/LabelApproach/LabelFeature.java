/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.LabelApproach;

import java.util.List;
import java.util.Set;

/**
 *
 * @author swalter
 */
public class LabelFeature {

    public boolean isNAF() {
        return NAF;
    }

    public boolean isTrigrams() {
        return Trigrams;
    }

    public boolean isBigrams() {
        return Bigrams;
    }

    public boolean isPR() {
        return PR;
    }

    public boolean isPOSPR() {
        return POSPR;
    }

    public boolean isAR() {
        return AR;
    }

    public boolean isPAP() {
        return PAP;
    }

    public boolean isNOF() {
        return NOF;
    }

    public boolean isPP() {
        return PP;
    }

    public boolean isNLD() {
        return NLD;
    }

    public boolean isAP() {
        return AP;
    }

    public boolean isAFP() {
        return AFP;
    }

    public boolean isALP() {
        return ALP;
    }
    
    boolean NAF = false;
    boolean Trigrams = false;
    boolean Bigrams = false;
    boolean PR = false;
    boolean POSPR = false;
    boolean AR = false;
    boolean PAP = false;
    boolean NOF = false;
    boolean PP = false;
    boolean NLD = false;
    boolean AP = false;
    boolean AFP = false;
    boolean ALP = false;
    
    
    public void setFeature(Set<String> features){
        for(String f : features){
            if(f.equals("NAF")) NAF = true;
            if(f.equals("Trigrams")) Trigrams = true;
            if(f.equals("Bigrams")) Bigrams = true;
            if(f.equals("PR")) PR = true;
            if(f.equals("POSPR")) POSPR = true;
            if(f.equals("AR")) AR = true;
            if(f.equals("PAP")) PAP = true;
            if(f.equals("NOF")) NOF = true;
            if(f.equals("PP")) PP = true;
            if(f.equals("NLD")) NLD = true;
            if(f.equals("AP")) AP = true;
            if(f.equals("AFP")) AFP = true;
            if(f.equals("ALP")) ALP = true;
        }
    }
    
    public void reset(){
        NAF = false;
        Trigrams = false;
        Bigrams = false;
        PR = false;
        POSPR = false;
        AR = false;
        PAP = false;
        NOF = false;
        PP = false;
        NLD = false;
        AP = false;
        AFP = false;
        ALP = false;
    }
    
    
}
