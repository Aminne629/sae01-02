import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class knn {
    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }


    //fonction pour savoir si le mot est un mode dit "vide"
    public static boolean estMotVide(String mot) {
        //return true si le mot est vide sinon false
        ArrayList<String> motsVides = new ArrayList<>(Arrays.asList("le", "la", "les", "de", "et", "à", "en", "un", "une", "du", "des"));
        for (String motVide : motsVides) {
            if (mot.equalsIgnoreCase(motVide)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> retirerMotsVides(ArrayList<String> mots) {
        ArrayList<String> motsSansVides = new ArrayList<>();
        for (String mot : mots) {
            if (!estMotVide(mot)) {
                motsSansVides.add(mot);
            }
        }
        return motsSansVides;
    }


    //fonction pour savoir le nombre de mots communs entre des depeches, sans compter les mots vides
    public static int motsCommuns(Depeche d1, Depeche d2) {
        ArrayList<String> motD1 = retirerMotsVides(d1.getMots());
        ArrayList<String> motD2 = retirerMotsVides(d2.getMots());

        // Compter les mots communs
        int nbrMotsCommuns = 0;
        for (String unMotD1 : motD1) {
            for (String unMotD2 : motD2) {
                if (unMotD1.equals(unMotD2)) {
                    nbrMotsCommuns++;
                    break; // Éviter de compter plusieurs fois le même mot
                }
            }
        }
        return nbrMotsCommuns;
    }

    public static int motsCommunsLexique(Depeche d1, ArrayList<String> lexique) {
        ArrayList<String> motD1 = retirerMotsVides(d1.getMots());
        ArrayList<String> motLexique = retirerMotsVides(lexique);

        // Compter les mots communs
        int nbrMotsCommuns = 0;
        for (String unMotD1 : motD1) {
            for (String unMotLexique : motLexique) {
                if (unMotD1.equals(unMotLexique)) {
                    nbrMotsCommuns++;
                    break; // Éviter de compter plusieurs fois le même mot
                }
            }
        }
        return nbrMotsCommuns;
    }

    public static ArrayList<Depeche> triDepeche(ArrayList<Depeche> depeches) {
        //on trouve le plus proche et on le met à coté de la depeche courante
        if (!depeches.isEmpty()) {
            int i = 0;
            int NbMotsCommuns;
            while (i < depeches.size() - 1) {
                int j = i + 2;
                int indDuPlusProche = i + 1;
                NbMotsCommuns = motsCommuns(depeches.get(i), depeches.get(indDuPlusProche));
                while (j < depeches.size()) {
                    if (motsCommuns(depeches.get(i), depeches.get(j)) > NbMotsCommuns) {
                        indDuPlusProche = j;
                    }
                    j++;
                }
                if (indDuPlusProche != i + 1) {
                    Depeche temp = depeches.get(i + 1);
                    depeches.set(i + 1, depeches.get(indDuPlusProche));
                    depeches.set(indDuPlusProche, temp);
                }
                i++;
            }
        }
        return depeches;
    }
}
    //renvoie une arraylist qui contient les k voisins de la depeche demandé
//    public static ArrayList<Depeche> voisinsK(ArrayList<Depeche> depeches,Depeche depecheCour,int k){
//        int indiceDepecheCour = 0;
//        boolean trouve = false;
//        for (int i = 0; i < depeches.size() && !trouve; i++){
//            if (depeches.get(i).equals(depecheCour)){
//                indiceDepecheCour = i;
//                trouve = true;
//            }
//        }
//        ArrayList<Depeche> voisinsK = new ArrayList<>();
//        int i = indiceDepecheCour;
//        int indiceK;
//        int tailleListe = depeches.size()-1;
//        if ((i+k)<=tailleListe){
//            indiceK = i+k;
//        } else {
//            return voisinsK;
//        }
//      for (int j = indiceDepecheCour+1; j <= j+k; j++){
//            voisinsK.add(depeches.get(j));
//      }
//      return voisinsK;
//    }

    //il faut utiliser la fonction voisinK pour trouver les k voisins de la depecheCour
//    public static Categorie categorieDepeche(Depeche depecheCour,ArrayList<Depeche> voisinK,ArrayList<Categorie> vCategories){
//        ArrayList<Categorie> Categories = new ArrayList<>();
//
//        ArrayList<Depeche> depechesEconomie = lectureDepeches("./economie-lexique-automatique.txt");
//        ArrayList<String> motsEconomie = new ArrayList<>();
//        ArrayList<Depeche> depechesSport = lectureDepeches("./sport-lexique-automatique.txt");
//        ArrayList<String> motsSport = new ArrayList<>();
//        ArrayList<Depeche> depechesPolitique = lectureDepeches("./politique-lexique-automatique.txt");
//        ArrayList<String> motsPolitique = new ArrayList<>();
//        ArrayList<Depeche> depechesSciences = lectureDepeches("./sciences-lexique-automatique.txt");
//        ArrayList<String> motsSciences = new ArrayList<>();
//        ArrayList<Depeche> depechesCulture = lectureDepeches("./culture-lexique-automatique.txt");
//        ArrayList<String> motsCulture = new ArrayList<>();
//
//        for (Depeche depeche: depechesEconomie){
//            motsEconomie = (depeche.getMots());
//        }
//
//        for (Depeche depeche: depechesSport){
//            motsSport = (depeche.getMots());
//        }
//
//        for (Depeche depeche: depechesPolitique){
//            motsPolitique = (depeche.getMots());
//        }
//
//        for (Depeche depeche: depechesSciences){
//            motsSciences = (depeche.getMots());
//        }
//
//        for (Depeche depeche: depechesCulture){
//            motsCulture = (depeche.getMots());
//        }
//
//        for (Depeche depecheVoisinK: voisinK){
//            int nbMotsCommuns = 0;
//            Categorie categorieDuVoisinK = new Categorie("");
//
//            int nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsEconomie);
//            if (nbMotsCommunsTest>nbMotsCommuns){
//                nbMotsCommuns = nbMotsCommunsTest;
//                categorieDuVoisinK = new Categorie("economie");
//            }
//            nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsSport);
//            if (nbMotsCommunsTest>nbMotsCommuns){
//                nbMotsCommuns = nbMotsCommunsTest;
//                categorieDuVoisinK = new Categorie("sport");
//            }
//
//            nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsPolitique);
//            if (nbMotsCommunsTest>nbMotsCommuns){
//                nbMotsCommuns = nbMotsCommunsTest;
//                categorieDuVoisinK = new Categorie("politique");
//            }
//
//            nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsSciences);
//            if (nbMotsCommunsTest>nbMotsCommuns){
//                nbMotsCommuns = nbMotsCommunsTest;
//                categorieDuVoisinK = new Categorie("sciences");
//            }
//
//            nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsCulture);
//            if (nbMotsCommunsTest>nbMotsCommuns){
//                nbMotsCommuns = nbMotsCommunsTest;
//                categorieDuVoisinK = new Categorie("culture");
//            }
//
//            Categories.add(categorieDuVoisinK);
//
//        }
//        int nbScience =0;
//        int nbCulture =0;
//        int nbPolitique = 0;
//        int nbEconomie = 0;
//        int nbSport = 0;
//
//        for (Categorie categorie: Categories){
//            if (categorie.getNom().equals("economie")){
//                nbEconomie++;
//            }
//            if (categorie.getNom().equals("politique")){
//                nbPolitique++;
//            }
//            if (categorie.getNom().equals("sciences")){
//                nbScience++;
//            }
//            if (categorie.getNom().equals("culture")){
//                nbCulture++;
//            }
//            if (categorie.getNom().equals("sport")){
//                nbSport++;
//            }
//        }
//        PaireChaineEntier scienceP = new PaireChaineEntier("science",nbScience);
//        PaireChaineEntier cultureP = new PaireChaineEntier("culture",nbCulture);
//        PaireChaineEntier politiqueP = new PaireChaineEntier("politique",nbPolitique);
//        PaireChaineEntier economieP = new PaireChaineEntier("economie",nbEconomie);
//        PaireChaineEntier sportP = new PaireChaineEntier("sport",nbSport);
//
//        ArrayList<PaireChaineEntier> vCategorieEtScore = new ArrayList<>(Arrays.asList(scienceP,cultureP,politiqueP,economieP,sportP));
//
//        int score = 0;
//        PaireChaineEntier resultatP = new PaireChaineEntier("resultat",score);
//        for (PaireChaineEntier Categorie: vCategorieEtScore){
//            if (score<Categorie.getEntier()){
//                score = Categorie.getEntier();
//                resultatP = Categorie;
//            }
//        }
//        Categorie resultatC = new Categorie(resultatP.getChaine());
//
//        return resultatC;
//    }
//
//}
