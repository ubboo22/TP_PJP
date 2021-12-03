package first_follow;
import com.gramatiky.BezkontextovaGramatika;
import com.gramatiky.Pravidlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FirstAFollow {
    public static HashMap<String, ArrayList<String>> first(BezkontextovaGramatika g) {

        HashMap<String, ArrayList<String>> vysledokfirst = new HashMap<>();

        for (String t : g.getTerminaly()) {                     //Prehladavanie terminalov
            ArrayList<String> mnozina = new ArrayList<>();
            mnozina.add(t);
            vysledokfirst.put(t, mnozina);                           //Vytvorenie mnozin first pre terminaly... First(a) = a
        }

        for (String n : g.getNeterminaly()) {                   //Prehladavanie neterminalov
            ArrayList<String> mnozina = new ArrayList<>();
            vysledokfirst.put(n, mnozina);                           //Pridanie neterminalov ako klucov do mapy
        }
        //Riesenie pravidiel tvaru A->aα, kedy "a" patri do First(A)
        for (Pravidlo p : g.getPravidla()) {                    //Prehladavanie pravidiel
            if (g.getTerminaly().contains(p.getPravaStrana().get(0))) {     //Ak je prvy retazec na pravej strane terminal,
                vysledokfirst.get(p.getLavaStrana().get(0)).add(p.getPravaStrana().get(0)); //prida sa do First neterminalu na lavej strane
            }
        }
        //Riesenie pravidiel tvaru A->epsilon, kedy "epsilon" patri do First(A)
        for (Pravidlo p : g.getPravidla()) {
            if (p.getPravaStrana().get(0) == "epsilon") {       //Ak je na pravej strane "epsilon",
                vysledokfirst.get(p.getLavaStrana().get(0)).add("epsilon");   //prida sa do First neterminalu na lavej strane
            }
        }
        //Riesenie pravidel tvaru A->Bα
        boolean zmena = true;
        while (zmena == true) { //cyklus sa opakuje pokial sa do niektorej z First mnozin prida novy prvok
            zmena = false;
            for (Pravidlo p : g.getPravidla()) {
                boolean jeepsilon = false;
                if (g.getNeterminaly().contains(p.getPravaStrana().get(0))) { //ked je prvy retazec na pravej strane neterminal,
                    if (vysledokfirst.get(p.getPravaStrana().get(0)).contains("epsilon")) { //zistujeme ci do jeho first mnoziny patri "epsilon"
                        jeepsilon = true;
                    } else {
                        jeepsilon = false;
                    }
                    for (int i = 0; i < vysledokfirst.get(p.getPravaStrana().get(0)).size(); i++) { //prehladame mnozinu first neterminalu na pravej strane
                        if (vysledokfirst.get(p.getLavaStrana().get(0)).contains(vysledokfirst.get(p.getPravaStrana().get(0)).get(i))) {
                            /*Ak mnozina first neterminalu na lavej strane pravidla uz obsahuje dany prvok mnoziny
                            first neterminalu na pravej strane, tak sa nevykona nic */
                        } else {                                                            //v opacnom pripade
                            if (vysledokfirst.get(p.getPravaStrana().get(0)).get(i) == "epsilon") { //overime, ci dany prvok je epsilon
                            /*Ak nie je epsilon, tak dany prvok pridame do mnoziny first neterminalu na lavej strane pravidla
                            a zaznamename, ze bola vykonana zmena (do first mnoziny bol pridany novy prvok) */
                            } else {
                                vysledokfirst.get(p.getLavaStrana().get(0)).add(vysledokfirst.get(p.getPravaStrana().get(0)).get(i));
                                zmena = true;
                            }
                        }
                    }
                }
                for (int j = 1; j < p.getPravaStrana().size(); j++) { //prehladavame zvysne prvky pravej strany pravidla
                    if (jeepsilon == true) {       // pre pripad, ze first mnozina predchadzajuceho neterminalu na pravej strane obsahuje epsilon
                        if (g.getTerminaly().contains(p.getPravaStrana().get(j))) { //ak je nasledujuci prvok terminal
                            if (vysledokfirst.get(p.getLavaStrana().get(0)).contains(p.getPravaStrana().get(j))) {

                            } else {        // a este nieje vo first mnozine neterminalu na lavej strane,
                                vysledokfirst.get(p.getLavaStrana().get(0)).add(p.getPravaStrana().get(j)); //tak ho tam pridame
                                zmena = true;         //a zaznamename, ze bola vykonana zmena
                            }
                        }
                        if (g.getNeterminaly().contains(p.getPravaStrana().get(j))) {  //ak je nasledujuci prvok neterminal
                            if (vysledokfirst.get(p.getPravaStrana().get(j)).contains("epsilon")) { //zistime, ci jeho first mnozina obsahuje epsilon
                                jeepsilon = true;
                            } else {
                                jeepsilon = false;
                            }
                            for (int k = 0; k < vysledokfirst.get(p.getPravaStrana().get(j)).size(); k++) { //prehladame first mnozinu tohto neterminalu
                                if (vysledokfirst.get(p.getLavaStrana().get(0)).contains(vysledokfirst.get(p.getPravaStrana().get(j)).get(k))) {

                                } else {                    // ak prvok z first mnoziny neterminalu este nieje v mnozine first neterminalu na lavej strane
                                    if (vysledokfirst.get(p.getPravaStrana().get(0)).get(k) == "epsilon") {

                                    } else {                // a tento prvok nieje epsilon
                                        vysledokfirst.get(p.getLavaStrana().get(0)).add(vysledokfirst.get(p.getPravaStrana().get(j)).get(k)); //tak ho pridame do first mnoziny neterminalu na lavej strane
                                        zmena = true;                                       // a zaznamename, ze sa vykonala zmena
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(vysledokfirst);
        return vysledokfirst;
    }

    public static HashMap<String, ArrayList<String>> follow(BezkontextovaGramatika g) {
        HashMap<String, ArrayList<String>> vysledokfirst = first(g);
        HashMap<String, ArrayList<String>> vysledokfollow = new HashMap<>();

        for (String n : g.getNeterminaly()) {
            ArrayList<String> mnozina = new ArrayList<>();
            vysledokfollow.put(n, mnozina);
        }
        ArrayList<String> mnozina = new ArrayList<>();
        mnozina.add("epsilon");
        vysledokfollow.get(g.getZaciatocnySymbol()).add("epsilon");

        boolean zmena = true;
        while (zmena) {
            zmena = false;
            for (Pravidlo p : g.getPravidla()) {
                for (int i = 0; i < p.getPravaStrana().size(); i++) {
                    if (g.getNeterminaly().contains(p.getPravaStrana().get(i))) {
                        if (i + 1 != p.getPravaStrana().size()) {
                            if (g.getTerminaly().contains(p.getPravaStrana().get(i + 1))) {
                                if (!vysledokfollow.get(p.getPravaStrana().get(i)).contains(p.getPravaStrana().get(i + 1))) {
                                    vysledokfollow.get(p.getPravaStrana().get(i)).add(p.getPravaStrana().get(i + 1));
                                    zmena = true;
                                }
                            } else {
                                boolean jeepsilon = true;
                                for (int j = i + 1; j < p.getPravaStrana().size(); j++) {
                                    if (jeepsilon) {
                                        for (int k = 0; k < vysledokfirst.get(p.getPravaStrana().get(j)).size(); k++) {
                                            if (vysledokfirst.get(p.getPravaStrana().get(j)).equals("epsilon")) {
                                                jeepsilon = true;
                                            }
                                            if (!vysledokfirst.get(p.getPravaStrana().get(j)).get(k).equals("epsilon")) {
                                                if(k < vysledokfirst.get(p.getPravaStrana().get(j)).size()){
                                                if (!vysledokfollow.get(p.getPravaStrana().get(i)).contains(vysledokfirst.get(p.getPravaStrana().get(j)).get(k))) {
                                                    vysledokfollow.get(p.getPravaStrana().get(i)).add(vysledokfirst.get(p.getPravaStrana().get(j)).get(k));

                                                    zmena = true;
                                                }
                                            }
                                            }
                                        }
                                    }


                                }
                                if (jeepsilon) {
                                    for (int k = 0; k < vysledokfollow.get(p.getLavaStrana().get(0)).size(); k++) {
                                        if (!vysledokfollow.get(p.getLavaStrana().get(0)).get(k).equals("epsilon")) {
                                            if (!vysledokfollow.get(p.getPravaStrana().get(i)).contains(vysledokfollow.get(p.getLavaStrana().get(0)).get(k))) {
                                                vysledokfollow.get(p.getPravaStrana().get(i)).add(vysledokfollow.get(p.getLavaStrana().get(0)).get(k));
                                                zmena = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int l = 0; l < vysledokfollow.get(p.getLavaStrana().get(0)).size(); l++) {
                                if (!vysledokfollow.get(p.getLavaStrana().get(0)).get(l).equals("epsilon")) {
                                    if (!vysledokfollow.get(p.getPravaStrana().get(i)).contains(vysledokfollow.get(p.getLavaStrana().get(0)).get(l))) {
                                        vysledokfollow.get(p.getPravaStrana().get(i)).add(vysledokfollow.get(p.getLavaStrana().get(0)).get(l));
                                        zmena = true;

                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
        for (Pravidlo p : g.getPravidla()) {
            String lavaStrana = g.getZaciatocnySymbol();
            if (p.getLavaStrana().get(0).equals(lavaStrana)) {
                for (int m = (p.getPravaStrana().size()) - 1; m >= 0; m--) {
                    if (m + 1 == p.getPravaStrana().size()) {
                        if (g.getNeterminaly().contains(p.getPravaStrana().get(m))) {
                            if (!vysledokfollow.get(p.getPravaStrana().get(m)).contains("epsilon")) {
                                vysledokfollow.get(p.getPravaStrana().get(m)).add("epsilon");
                            }
                        }
                    }
                    if (vysledokfirst.get(p.getPravaStrana().get(m)).contains("epsilon")) {
                        if ((m >= 1)) {
                            if (g.getNeterminaly().contains(p.getPravaStrana().get(m - 1))) {
                                if (!p.getPravaStrana().get(m - 1).equals("epsilon")) {
                                    if (!vysledokfollow.get(p.getPravaStrana().get(m - 1)).contains("epsilon")) {
                                        vysledokfollow.get(p.getPravaStrana().get(m - 1)).add("epsilon");
                                    }
                                }
                            }
                        }
                    }

                }

            }

        }
        String lavaStrana = g.getZaciatocnySymbol();
        for (Pravidlo p : g.getPravidla()) {
            if (p.getLavaStrana().get(0).equals(lavaStrana)) {
                for (int m = (p.getPravaStrana().size()) - 1; m >= 0; m--) {
                    if (g.getNeterminaly().contains(p.getPravaStrana().get(m))) {
                        lavaStrana = p.getPravaStrana().get(m);
                        for (Pravidlo o : g.getPravidla()) {
                            if (o.getLavaStrana().get(0).equals(lavaStrana)) {
                                System.out.println(lavaStrana);
                                for (int n = (o.getPravaStrana().size()) - 1; n >= 0; n--) {
                                    if (g.getNeterminaly().contains(o.getPravaStrana().get(n))) {
                                        if (!vysledokfollow.get(o.getPravaStrana().get(n)).contains("epsilon")) {
                                            vysledokfollow.get(o.getPravaStrana().get(n)).add("epsilon");
                                            lavaStrana = o.getPravaStrana().get(n);
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }
        System.out.println(vysledokfollow);
        return vysledokfollow;
    }
}




