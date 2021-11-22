package com.automaty;

import java.util.HashMap;
import java.util.HashSet;

public class EkvivalentnyDKA {
    private HashSet<String> eStavy;
    private HashSet<String> eSymboly;
    private HashSet<String> eAkceptujuceStavy;
    private HashSet<String> eZaciatocnyStav;
    private HashMap<String, HashMap<String,HashSet<String>>> ePrechodovaTabulka;

    public EkvivalentnyDKA(NedeterministickyKonecnyAutomat nedeterministickyKA){
        this.eSymboly = nedeterministickyKA.vratSymbolyAut();

    }
}
