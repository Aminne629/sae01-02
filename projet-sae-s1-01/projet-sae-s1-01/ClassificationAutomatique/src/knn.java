import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
        //on retire les mots vides, on sait si il y en a grâce à la fonction "estMotVide"
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



    public static ArrayList<Depeche> triDepecheKnn(ArrayList<Depeche> depeches) {
        // Vérifier si la liste des dépêches n'est pas vide
        if (!depeches.isEmpty()) {
            int i = 0; // Index de la dépêche courante
            int NbMotsCommuns; // Nombre de mots communs avec la dépêche courante

            // Parcours de toutes les dépêches sauf la dernière
            while (i < depeches.size() - 1) {
                int j = i + 2; // Index du prochain candidat à comparer
                int indDuPlusProche = i + 1; // Initialisation : on suppose que la dépêche la plus proche est celle juste après

                // Calculer les mots communs entre la dépêche courante et la dépêche supposée "plus proche"
                NbMotsCommuns = motsCommuns(depeches.get(i), depeches.get(indDuPlusProche));

                // Parcourir toutes les dépêches restantes pour trouver la plus proche
                while (j < depeches.size()) {
                    // Comparer le nombre de mots communs avec la dépêche courante
                    if (motsCommuns(depeches.get(i), depeches.get(j)) > NbMotsCommuns) {
                        // Mise à jour de l'indice de la dépêche la plus proche si une meilleure correspondance est trouvée
                        indDuPlusProche = j;
                    }
                    j++;
                }

                // Si la dépêche la plus proche n'est pas déjà à la position suivante, effectuer un échange
                if (indDuPlusProche != i + 1) {
                    // Échanger la dépêche située juste après la dépêche courante avec la dépêche la plus proche
                    Depeche temp = depeches.get(i + 1);
                    depeches.set(i + 1, depeches.get(indDuPlusProche));
                    depeches.set(indDuPlusProche, temp);
                }

                // Passer à la dépêche suivante
                i++;
            }
        }

        // Retourner la liste des dépêches triées
        return depeches;
    }


    //il faut mettre les categories du fichier fait avec knn et aussi  quelque differentes categories (science, politique etc)
    public static void classementResultat(ArrayList<Depeche> depeches, ArrayList<Categorie> categoriesKnn,ArrayList<Categorie> categories, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier + ".txt");
            ArrayList<PaireChaineEntier> listePourLaMoyenne = new ArrayList<>();


            //étape 1 : on écrit dans le fichier ceci : numero:categorie, par exemple 032:POLITIQUE
            knn.triDepecheKnn(depeches);                                           // 033:SCIENCE
            for (int i = 0; i < depeches.size(); i++) {
                String iFormate = String.valueOf(1000+i).substring(1);
                file.write(iFormate+":"+depeches.get(i).getCategorie()+"\n");
                Categorie cateCouranteKnn = new Categorie(depeches.get(i).getCategorie());
            }


            //étape 2
            int i = 0;
            while (i < categories.size()) {
                Categorie categorie = categories.get(i);
                String categorieNom = categorie.getNom();

                int totalDansCategorie = 0; // Nombre de dépêches qui appartiennent à cette catégorie
                int correctementClassees = 0; // Nombre de dépêches correctement classées

                int j = 0;
                while (j < depeches.size()) {
                    Depeche depeche = depeches.get(j);
                    String categorieReelle = depeche.getCategorie();
                    String categorieClassee = categoriesKnn.get(j).getNom();

                    if (categorieReelle.equalsIgnoreCase(categorieNom)) {
                        totalDansCategorie++;
                        if (categorieReelle.equalsIgnoreCase(categorieClassee)) {
                            correctementClassees++;
                        }
                    }
                    j++;
                }


                if (totalDansCategorie > 0) {
                    double tauxPrecision = (double) correctementClassees / totalDansCategorie * 100;
                    int tauxEnInt = (int) tauxPrecision;
                    file.write(categorieNom.toUpperCase() + ":" + tauxPrecision + "%\n");
                    listePourLaMoyenne.add(new PaireChaineEntier(categorieNom, tauxEnInt));
                } else {
                    System.out.println("Catégorie " + categorieNom + ": Aucune dépêche trouvée.");
                }

                i++;
            }
            file.write("MOYENNE:" + UtilitairePaireChaineEntier.moyenne(listePourLaMoyenne) + "%\n");
            System.out.println("Votre saisie a été écrite avec succès dans " + nomFichier + ".txt");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int motsCommunsLexique(Depeche d1, ArrayList<String> lexique) {
        // Extraire uniquement les mots avant les ":" dans le lexique
        ArrayList<String> motsLexique = new ArrayList<>();
        for (String entry : lexique) {
            String[] parts = entry.split(":");
            if (parts.length > 0) {
                motsLexique.add(parts[0].trim());
            }
        }

        // Compter les mots communs
        int nbrMotsCommuns = 0;
        for (String unMotD1 : retirerMotsVides(d1.getMots())) {
            for (String motLexique : motsLexique) {
                if (unMotD1.equals(motLexique)) {
                    nbrMotsCommuns++;
                    break; // Éviter de compter plusieurs fois le même mot
                }
            }
        }

        return nbrMotsCommuns;
    }




    // Renvoie une ArrayList qui contient les k voisins de la dépêche demandée
    public static ArrayList<Depeche> voisinsK(ArrayList<Depeche> depeches, Depeche depecheCour, int k) {
        // Trouver l'indice de la dépêche courante
        int indiceDepecheCour = -1;
        for (int i = 0; i < depeches.size(); i++) {
            if (depeches.get(i).equals(depecheCour)) {
                indiceDepecheCour = i;
                break;
            }
        }

        // Si la dépêche n'a pas été trouvée, retourner une liste vide
        if (indiceDepecheCour == -1) {
            return new ArrayList<>();
        }

        // Créer une liste pour les voisins
        ArrayList<Depeche> voisinsK = new ArrayList<>();

        // Ajouter les k voisins suivants si disponibles
        for (int j = 1; j <= k; j++) {
            int indiceVoisin = indiceDepecheCour + j;
            if (indiceVoisin < depeches.size()) {
                voisinsK.add(depeches.get(indiceVoisin));
            } else {
                break; // Sortir si on dépasse la taille de la liste
            }
        }

        return voisinsK;
    }

    public static String categorieLaPlusPresente(ArrayList<Categorie> categories) {
        // Liste pour stocker les noms de catégories déjà rencontrés
        ArrayList<String> nomsCategories = new ArrayList<>();
        ArrayList<Integer> frequences = new ArrayList<>();

        // Comptage des fréquences
        for (Categorie categorie : categories) {
            String nomCategorie = categorie.getNom();

            if (nomsCategories.contains(nomCategorie)) {
                int index = nomsCategories.indexOf(nomCategorie);
                frequences.set(index, frequences.get(index) + 1);
            } else {
                nomsCategories.add(nomCategorie);
                frequences.add(1);
            }
        }

        // Recherche de la catégorie la plus fréquente
        int maxFrequence = 0;
        String categoriePlusFrequent = "";

        for (int i = 0; i < frequences.size(); i++) {
            if (frequences.get(i) > maxFrequence) {
                maxFrequence = frequences.get(i);
                categoriePlusFrequent = nomsCategories.get(i);
            }
        }
        return categoriePlusFrequent;
    }



    //il faut utiliser la fonction voisinK pour trouver les k voisins de la depecheCour
    //on va determiner la categorie de chaque voisin k grace au lexique et grace à ça on determine la categorie de la depeche courante en prenant là categorie majoritaire.
        public static Categorie categorieDepeche(Depeche depecheCour,ArrayList<Depeche> voisinK,ArrayList<Categorie> vCategories){

            ArrayList<Categorie> Categories = new ArrayList<>();

            ArrayList<Depeche> depechesEconomie = lectureDepeches("./economie-lexique-automatique.txt");
            ArrayList<String> motsEconomie = new ArrayList<>();
            ArrayList<Depeche> depechesSport = lectureDepeches("./sport-lexique-automatique.txt");
            ArrayList<String> motsSport = new ArrayList<>();
            ArrayList<Depeche> depechesPolitique = lectureDepeches("./politique-lexique-automatique.txt");
            ArrayList<String> motsPolitique = new ArrayList<>();
            ArrayList<Depeche> depechesSciences = lectureDepeches("./sciences-lexique-automatique.txt");
            ArrayList<String> motsSciences = new ArrayList<>();
            ArrayList<Depeche> depechesCulture = lectureDepeches("./culture-lexique-automatique.txt");
            ArrayList<String> motsCulture = new ArrayList<>();

            for (Depeche depeche: depechesEconomie){
                motsEconomie = (depeche.getMots());
            }

            for (Depeche depeche: depechesSport){
                motsSport = (depeche.getMots());
            }

            for (Depeche depeche: depechesPolitique){
                motsPolitique = (depeche.getMots());
            }

            for (Depeche depeche: depechesSciences){
                motsSciences = (depeche.getMots());
            }

            for (Depeche depeche: depechesCulture){
                motsCulture = (depeche.getMots());
            }

            for (Depeche depecheVoisinK: voisinK){
                int nbMotsCommuns = 0;
                Categorie categorieDuVoisinK = new Categorie("problème");

                int nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsEconomie);
                if (nbMotsCommunsTest>nbMotsCommuns){
                    nbMotsCommuns = nbMotsCommunsTest;
                    categorieDuVoisinK = new Categorie("economie");
                }
                nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsSport);
                if (nbMotsCommunsTest>nbMotsCommuns){
                    nbMotsCommuns = nbMotsCommunsTest;
                    categorieDuVoisinK = new Categorie("sport");
                }

                nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsPolitique);
                if (nbMotsCommunsTest>nbMotsCommuns){
                    nbMotsCommuns = nbMotsCommunsTest;
                    categorieDuVoisinK = new Categorie("politique");
                }

                nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsSciences);
                if (nbMotsCommunsTest>nbMotsCommuns){
                    nbMotsCommuns = nbMotsCommunsTest;
                    categorieDuVoisinK = new Categorie("sciences");
                }

                nbMotsCommunsTest = motsCommunsLexique(depecheVoisinK,motsCulture);
                if (nbMotsCommunsTest>nbMotsCommuns){
                    nbMotsCommuns = nbMotsCommunsTest;
                    categorieDuVoisinK = new Categorie("culture");
                }

                Categories.add(categorieDuVoisinK);

            }
//            int nbScience = 0;
//            int nbCulture = 0;
//            int nbPolitique = 0;
//            int nbEconomie = 0;
//            int nbSport = 0;
//
//            for (Categorie categorie: Categories){
//                if (categorie.getNom().equals("economie")){
//                    nbEconomie++;
//                }
//                if (categorie.getNom().equals("politique")){
//                    nbPolitique++;
//                }
//                if (categorie.getNom().equals("sciences")){
//                    nbScience++;
//                }
//                if (categorie.getNom().equals("culture")){
//                    nbCulture++;
//                }
//                if (categorie.getNom().equals("sport")){
//                    nbSport++;
//                }
//            }
//            PaireChaineEntier scienceP = new PaireChaineEntier("science",nbScience);
//            PaireChaineEntier cultureP = new PaireChaineEntier("culture",nbCulture);
//            PaireChaineEntier politiqueP = new PaireChaineEntier("politique",nbPolitique);
//            PaireChaineEntier economieP = new PaireChaineEntier("economie",nbEconomie);
//            PaireChaineEntier sportP = new PaireChaineEntier("sport",nbSport);
//
//            ArrayList<PaireChaineEntier> vCategorieEtScore = new ArrayList<>(Arrays.asList(scienceP,cultureP,politiqueP,economieP,sportP));
//
            String categorie = categorieLaPlusPresente(Categories);
//            int score = 0;
//            PaireChaineEntier resultatP = new PaireChaineEntier("resultat",score);
//            for (PaireChaineEntier Categorie: vCategorieEtScore){
//                if (score<Categorie.getEntier()){
//                    score = Categorie.getEntier();
//                    resultatP = Categorie;
//                }
//            }
//        Categorie resultatC = new Categorie(resultatP.getChaine());
            Categorie resultatC = new Categorie(categorie);


            return resultatC;

    }
}
