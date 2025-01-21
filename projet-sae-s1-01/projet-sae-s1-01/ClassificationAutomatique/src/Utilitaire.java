import java.util.ArrayList;

public class Utilitaire {

    public static void triFusion(ArrayList<PaireChaineEntier> vPaireChaineEntier, int inf, int sup) {
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion(vPaireChaineEntier, inf, m);
            triFusion(vPaireChaineEntier, m + 1, sup);
            fusionTabGTabD(vPaireChaineEntier, inf, m, sup);
        }
    }


    public static void fusionTabGTabD(ArrayList<PaireChaineEntier> vPaireChaineEntier,
                                      int inf, int m, int sup) {
        // { inf <= sup, m = (inf+sup)/2, vPaireChaineEntier[inf..m] trié, vPaireChaineEntier[m+1..sup] trié }
        // => { vPaireChaineEntier[inf..sup] trié }
        int i = inf;
        int j = m + 1;
        ArrayList<PaireChaineEntier> temp = new ArrayList<>();

        // Fusionner les deux sous-listes triées
        while (i <= m && j <= sup) {
            if (vPaireChaineEntier.get(i).getChaine().compareTo(vPaireChaineEntier.get(j).getChaine()) <= 0) {
                temp.add(vPaireChaineEntier.get(i));
                i++;
            } else {
                temp.add(vPaireChaineEntier.get(j));
                j++;
            }
        }

        // Ajouter les éléments restants de la première sous-liste
        while (i <= m) {
            temp.add(vPaireChaineEntier.get(i));
            i++;
        }

        // Ajouter les éléments restants de la seconde sous-liste
        while (j <= sup) {
            temp.add(vPaireChaineEntier.get(j));
            j++;
        }

        // Copier le contenu de temp dans vPaireChaineEntier[inf..sup]
        for (int l = 0; l < temp.size(); l++) {
            vPaireChaineEntier.set(inf + l, temp.get(l));
        }
    }

}
