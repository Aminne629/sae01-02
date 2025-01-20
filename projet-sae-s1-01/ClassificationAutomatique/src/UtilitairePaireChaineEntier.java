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
        if (listePaires.isEmpty())//on verifie si listepaires est vide
            return null;

        String chainemax = listePaires.get(0).getChaine();//on initialise le plus grand
        int maxscore = listePaires.get(0).getEntier();// on initialise là aussi avec le premier element

        //on parcourt pour trouver le max
        for (int i = 1;i<listePaires.size();i++){ // on commence a i=1(deuxieme element) car on a initialiser avec le premier element
            int score = listePaires.get(i).getEntier();// on initialise une variable score(pr aller plus vite)
            if (score > maxscore){//on compare
                maxscore = score;//on met à jour si la condition est verifiée
                chainemax = listePaires.get(i).getChaine();//on met a jour aussi
            }
        }
        return chainemax;
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        return 0;
    }

}
