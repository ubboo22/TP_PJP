package test;


import com.gramatiky.BezkontextovaGramatika;
import com.gramatiky.Gramatika;
import com.gramatiky.Pravidlo;
import first_follow.FirstAFollow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Main {
    public static void main(String[] args) throws Exception {



        HashSet<String> neterminaly =new HashSet<>(Arrays.asList("S","A","B", "D"));
        HashSet<String> terminaly =new HashSet<>(Arrays.asList("a","b", "c"));
        String zaciatocnySymbol = "S";
        FirstAFollow firstAFollow = new FirstAFollow();


        Pravidlo pravidlo1 = new Pravidlo(new ArrayList<>(Arrays.asList("S")), new ArrayList<>(Arrays.asList("a","A","D","B")));
        Pravidlo pravidlo2 = new Pravidlo(new ArrayList<>(Arrays.asList("S")), new ArrayList<>(Arrays.asList("a","b")));
        Pravidlo pravidlo3 = new Pravidlo(new ArrayList<>(Arrays.asList("A")), new ArrayList<>(Arrays.asList("B","c")));
        Pravidlo pravidlo4 = new Pravidlo(new ArrayList<>(Arrays.asList("A")), new ArrayList<>(Arrays.asList("a","A")));
        Pravidlo pravidlo5 = new Pravidlo(new ArrayList<>(Arrays.asList("B")), new ArrayList<>(Arrays.asList("b","B")));
        Pravidlo pravidlo6 = new Pravidlo(new ArrayList<>(Arrays.asList("B")), new ArrayList<>(Arrays.asList("epsilon")));
        Pravidlo pravidlo7 = new Pravidlo(new ArrayList<>(Arrays.asList("D")), new ArrayList<>(Arrays.asList("a")));
        LinkedHashSet<Pravidlo> pravidla = new LinkedHashSet<>(Arrays.asList(pravidlo1, pravidlo2, pravidlo3, pravidlo4, pravidlo5, pravidlo6,pravidlo7));
        BezkontextovaGramatika g = new BezkontextovaGramatika(terminaly,neterminaly,zaciatocnySymbol,pravidla);
        firstAFollow.follow(g);

    }}

