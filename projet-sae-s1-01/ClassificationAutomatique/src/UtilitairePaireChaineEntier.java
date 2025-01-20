import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        return 0;

    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int res = 0;
        for (int i =0; i<listePaires.size();i++){ //boucle qui parcourt la liste
            if (listePaires.get(i).getChaine().equals(chaine)) //on vérifie si le string donnée equivaut au string dans l'elt de la liste
                res=listePaires.get(i).getEntier();//si oui, res prend la valeur de l'entier de l'elt
        }
        return res; //on retourne l'entier
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        return "SPORT";
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        return 0;
    }

}
