import java.util.ArrayList;

public class Utilitaire {

    public static void triFusion(ArrayList<String> vString, int inf, int sup) {
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion(vString, inf, m);
            triFusion(vString, m + 1, sup);
            fusionTabGTabD(vString, inf, m, sup);
        }
    }


    public static void fusionTabGTabD(ArrayList<String> vString,
                                      int inf, int m, int sup) {
        // { inf <= sup, m = (inf+sup)/2, vPaireChaineEntier[inf..m] trié, vPaireChaineEntier[m+1..sup] trié }
        // => { vPaireChaineEntier[inf..sup] trié }
        int i = inf;
        int j = m + 1;
        ArrayList<String> temp = new ArrayList<>();

        // Fusionner les deux sous-listes triées
        while (i <= m && j <= sup) {
            if (vString.get(i).compareTo(vString.get(j)) <= 0) {
                temp.add(vString.get(i));
                i++;
            } else {
                temp.add(vString.get(j));
                j++;
            }
        }

        // Ajouter les éléments restants de la première sous-liste
        while (i <= m) {
            temp.add(vString.get(i));
            i++;
        }

        // Ajouter les éléments restants de la seconde sous-liste
        while (j <= sup) {
            temp.add(vString.get(j));
            j++;
        }

        // Copier le contenu de temp dans vPaireChaineEntier[inf..sup]
        for (int l = 0; l < temp.size(); l++) {
            vString.set(inf + l, temp.get(l));
        }
    }

}
