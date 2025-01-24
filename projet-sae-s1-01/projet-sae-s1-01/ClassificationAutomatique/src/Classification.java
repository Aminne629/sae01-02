import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class Classification {


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

    //cette fonction permet de creer les fichiers reponses et de calculer le pourcentage
    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier + ".txt");
            ArrayList<PaireChaineEntier> listResultat = new ArrayList<>();

            // Partie 1 : Génération du fichier de résultats
            for (int j = 0; j < depeches.size(); j++) {
                ArrayList<PaireChaineEntier> listeScore = new ArrayList<>();
                for (Categorie categorie : categories) {
                    int nombreMax = categorie.score(depeches.get(j));
                    listeScore.add(new PaireChaineEntier(categorie.getNom(), nombreMax));
                }

                String chaineMax = UtilitairePaireChaineEntier.chaineMax(listeScore);
                int indiceChaineMax = UtilitairePaireChaineEntier.indicePourChaine(listeScore, chaineMax);
                int nombreMax = listeScore.get(indiceChaineMax).getEntier();
                int number = j+1;
                String nombreFormate = String.valueOf(1000+number).substring(1);
                file.write(nombreFormate + ":" + chaineMax.toUpperCase() + "\n");
                listResultat.add(new PaireChaineEntier(chaineMax, nombreMax));
            }


            // Partie 2 : Analyse des catégories
            ArrayList<PaireChaineEntier> listePourLaMoyenne = new ArrayList<>();
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
                    String categorieClassee = listResultat.get(j).getChaine();

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
                    System.out.println(categorieNom.toUpperCase() + ":" + tauxPrecision+"%");
                    listePourLaMoyenne.add(new PaireChaineEntier(categorieNom, tauxEnInt));
                } else {
                    System.out.println("Catégorie " + categorieNom + ": Aucune dépêche trouvée.");
                }

                i++;
            }
            file.write("MOYENNE:"+UtilitairePaireChaineEntier.moyenne(listePourLaMoyenne) + "%\n");
            System.out.println("MOYENNE:"+UtilitairePaireChaineEntier.moyenne(listePourLaMoyenne)+"%");
//            System.out.println("Votre saisie a été écrite avec succès dans " + nomFichier + ".txt");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // La méthode initDico retourne une liste de PaireChaineEntier
        public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
            int nbComparaisons = 0;
            // Liste qui contiendra les paires (mot, score)
            categorie = categorie.toUpperCase();
            ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

            // Parcours de toutes les dépêches
            for (Depeche depeche : depeches) {
                // Si la dépêche appartient à la catégorie demandée
                nbComparaisons ++;
                if (depeche.getCategorie().equals(categorie)) {
                    // Récupération des mots de la dépêche
                    ArrayList<String> mots = depeche.getMots();  // La méthode getMots retourne directement une ArrayList<String>

                    // Parcours des mots de la dépêche
                    for (String mot : mots) {

                        // Vérification si le mot n'est pas déjà présent dans resultat
                        boolean present = false;
                        for (int i = 0; i<resultat.size() && !present ; i++) {  //j'ai utilisé une fonction que j'ai rajouté pour ne pas compter
                            nbComparaisons++;
                            if (resultat.get(i).getChaine().equals(mot)) {//les caractères spéciaux pour éviter les erreurs.
                                present = true;
                            }
                        }

                        // Si le mot n'est pas encore dans la liste, on l'ajoute avec un score de 0
                        if (!present) {
                            PaireChaineEntier paire = new PaireChaineEntier(mot, 0);
                            resultat.add(paire);
                        }
                    }
                }
            }
            // Retourner la liste contenant les paires (mot, score)
//            System.out.println(nbComparaisons);
            return resultat;
        }


    public static ArrayList<PaireChaineEntier> initDicoDicho(ArrayList<Depeche> depeches, String categorie) {
        categorie = categorie.toUpperCase();
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        int nbComparaisons=0;


        // Parcours de toutes les dépêches
        for (Depeche depeche : depeches) {
            // Si la dépêche appartient à la catégorie demandée
            nbComparaisons++;
            if (depeche.getCategorie().equals(categorie)) {
                // Récupération des mots de la dépêche
                ArrayList<String> mots = depeche.getMots(); // La méthode getMots retourne directement une ArrayList<String>
                Utilitaire.triFusion(mots,0, mots.size()-1);
                // Parcours des mots de la dépêche
                for (String mot : mots) {
                    // Recherche dichotomique dans resultat pour trouver l'index du mot ( il est à l'indice 0);
                    ArrayList<Integer> recherche = rechercheDichotomique(resultat, mot);
                    int index = recherche.get(0);
                    //on ajoute le nombre de comparaisons qui est retourné à l'indice 1
                    nbComparaisons += recherche.get(1);

                    // Si le mot n'est pas trouvé (index négatif), on l'insère à la position appropriée
                    if (index < 0) {
                        index = -(index + 1); // Convertir l'index négatif en position d'insertion
                        resultat.add(index, new PaireChaineEntier(mot, 0));
                    }
                }
            }
        }
//        System.out.println(nbComparaisons);
        // Retourner la liste contenant les paires (mot, score)
        return resultat;
    }

    // Méthode de recherche dichotomique pour trouver un mot dans une liste triée
    //renvoie l'indice du mot ou alors l'indice ou il faut l'insérer grace à une technique d'inversion à l'indice 0
    // et renvoie le nb de comparaisons à l'indice 1
    private static ArrayList<Integer> rechercheDichotomique(ArrayList<PaireChaineEntier> liste, String mot) {
        int nbComparaisons=1;
        int inf = 0;
        int sup = liste.size() - 1;
        ArrayList<Integer> resultat = new ArrayList<>();

        while (inf <= sup) {
            nbComparaisons++;
            int milieu = (inf + sup) / 2;
            String chaineMilieu = liste.get(milieu).getChaine();
            int comparaison = mot.compareTo(chaineMilieu);
            nbComparaisons++;
            if (comparaison == 0) {
                resultat.add(milieu);
                resultat.add(nbComparaisons);
                return resultat;
            } else if (comparaison < 0) {
                sup = milieu - 1; // Chercher dans la partie gauche
            } else {
                inf = milieu + 1; // Chercher dans la partie droite
            }
        }

        resultat.add(-(inf + 1));
        resultat.add(nbComparaisons);
        return resultat;
    }




    // La méthode calculScores met à jour les scores des mots dans dictionnaire


    public static int calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int nbComparaisons = 1;
        categorie = categorie.toUpperCase();

        // Assurez-vous que le dictionnaire est trié (par exemple, par ordre alphabétique des chaînes)
        dictionnaire.sort(Comparator.comparing(PaireChaineEntier::getChaine));

        // Parcours de toutes les dépêches
        for (Depeche depecheCourante : depeches) {
            nbComparaisons++;
            boolean estDansLaCategorie = depecheCourante.getCategorie().equals(categorie);

            // Récupération des mots de la dépêche
            ArrayList<String> mots = depecheCourante.getMots();

            // Parcours des mots de la dépêche
            for (String motDeLaDepeche : mots) {
                nbComparaisons++;

                // Recherche dichotomique pour trouver le mot dans le dictionnaire
                int index = Collections.binarySearch(dictionnaire,
                        new PaireChaineEntier(motDeLaDepeche, 0),
                        Comparator.comparing(PaireChaineEntier::getChaine));

                if (index >= 0) { // Mot trouvé
                    nbComparaisons++;
                    PaireChaineEntier paire = dictionnaire.get(index);

                    // Mise à jour du score
                    if (estDansLaCategorie) {
                        paire.setEntier(paire.getEntier() + 1);  // Incrémentation
                    } else {
                        paire.setEntier(paire.getEntier() - 1);  // Décrémentation
                    }
                }
            }
        }


        return nbComparaisons;
    }


    public static int poidsPourScore(int score) {
        if (score <=-2) {
            return 0; // Score très négatif : Poids faible
        } else if (score <=0) {
            return 1;
        } else if (score <= 2 ) {
            return 2; // Score modéré (négatif ou positif) : Poids moyen
        } else {
            return 3; // Score très positif : Poids élevé
        }
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier + ".txt");
            ArrayList<PaireChaineEntier> dictionnaire = initDicoDicho(depeches, categorie);
            int nbComparaisons = calculScores(depeches, categorie, dictionnaire);
            for (PaireChaineEntier paire : dictionnaire) {
                String mot = paire.getChaine();

                file.write(mot + ":" + poidsPourScore(paire.getEntier()) + "\n");

            }
            System.out.println("Pour la fonction calculScores il y a eu "+nbComparaisons+" comparaisons.");
        } catch(IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        long globalStartTime = System.currentTimeMillis();
        System.out.println("Début du programme...");

        // Chargement des dépêches
        long startTime = System.currentTimeMillis();
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        ArrayList<Depeche> test = lectureDepeches("./test.txt");
        System.out.println("Temps d'exécution pour lectureDepeches : " + (System.currentTimeMillis() - startTime) + " ms");

        // Initialisation des catégories avec leurs lexiques manuels
        startTime = System.currentTimeMillis();
        Categorie sport = new Categorie("sport");
        sport.initLexique("./sport.txt");
        Categorie economie = new Categorie("economie");
        economie.initLexique("./economie.txt");
        Categorie sciences = new Categorie("sciences");
        sciences.initLexique("./science.txt");
        Categorie politique = new Categorie("politique");
        politique.initLexique("./politique.txt");
        Categorie culture = new Categorie("culture");
        culture.initLexique("./culture.txt");
        System.out.println("Temps d'exécution pour initialisation des catégories : " + (System.currentTimeMillis() - startTime) + " ms");

        ArrayList<Categorie> listCategorie = new ArrayList<>();
        listCategorie.add(sport);
        listCategorie.add(economie);
        listCategorie.add(sciences);
        listCategorie.add(politique);
        listCategorie.add(culture);

        // Évaluation avec les lexiques manuels
        startTime = System.currentTimeMillis();
        System.out.println("\n========================================");
        System.out.println("Taux de réussite avec lexiques manuels:");
        System.out.println("========================================\n");
        classementDepeches(test, listCategorie, "fichier-reponse");
        System.out.println("Temps d'exécution pour classementDepeches (lexiques manuels) : " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("Fichier 'fichier-reponse' créé avec succès.");

        // Génération automatique des lexiques
        startTime = System.currentTimeMillis();
        System.out.println("\n========================================");
        System.out.println("Nombre de comparaisons par catégorie avec calculScores :");
        System.out.println("========================================\n");
        for (Categorie categorie : listCategorie) {
            generationLexique(depeches, categorie.getNom(), categorie.getNom() + "-lexique-automatique");
            System.out.println("Fichier '" + categorie.getNom() + "-lexique-automatique.txt' créé avec succès.");
        }
        System.out.println("Temps d'exécution pour générationLexique : " + (System.currentTimeMillis() - startTime) + " ms");

        // Initialisation des lexiques automatiques
        startTime = System.currentTimeMillis();
        sport.initLexique("./sport-lexique-automatique.txt");
        economie.initLexique("./economie-lexique-automatique.txt");
        sciences.initLexique("./sciences-lexique-automatique.txt");
        politique.initLexique("./politique-lexique-automatique.txt");
        culture.initLexique("./culture-lexique-automatique.txt");
        System.out.println("Temps d'exécution pour initialisation des lexiques automatiques : " + (System.currentTimeMillis() - startTime) + " ms");

        // Évaluation avec les lexiques automatiques
        startTime = System.currentTimeMillis();
        System.out.println("\n========================================");
        System.out.println("Taux de réussite avec lexiques automatiques:");
        System.out.println("========================================\n");
        classementDepeches(test, listCategorie, "fichier-reponse-automatique");
        System.out.println("Temps d'exécution pour classementDepeches (lexiques automatiques) : " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("Fichier 'fichier-reponse-automatique' créé avec succès.");

        // classification2
        System.out.println("\n========================================");
        System.out.println("Classification 2:");
        System.out.println("========================================\n");

        System.out.println("Chargement des depeches");
        ArrayList<Depeche> depeches2 = lectureDepeches("./depeches2.txt"); // Création fichier depeches
        System.out.println("Temps d'exécution pour lectureDepeches : " + (System.currentTimeMillis() - startTime) + " ms");

        System.out.println("Découverte des catégories");
        ArrayList<Categorie> categories = Classification2.decouvrirCategories(depeches2); // Création des catégories
        System.out.println("Temps d'exécution pour decouvrirCategories : " + (System.currentTimeMillis() - startTime) + " ms");

        System.out.println("Initialisation des lexiques");
        for (Categorie categorie : categories) {
            startTime = System.currentTimeMillis(); // Redémarrer le temps à chaque fonction
            generationLexique(depeches2, categorie.getNom(), "./" + categorie.getNom().toLowerCase() + "-lexique2-automatique");
            System.out.println("Temps d'exécution pour generationLexique (" + categorie.getNom() + ") : " + (System.currentTimeMillis() - startTime) + " ms");
        }

        for (int i = 0; i < categories.size(); i++) {
            startTime = System.currentTimeMillis();
            categories.get(i).initLexique("./" + categories.get(i).getNom().toLowerCase() + "-lexique2-automatique.txt"); // Initialisation des lexiques
            System.out.println("Temps d'exécution pour initLexique (" + categories.get(i).getNom() + ") : " + (System.currentTimeMillis() - startTime) + " ms");
        }

        System.out.println("Chargement du fichier de test");
        ArrayList<Depeche> testDepeches = lectureDepeches("./test2.txt");
        System.out.println("Temps d'exécution pour lectureDepeches (test2.txt) : " + (System.currentTimeMillis() - startTime) + " ms");

        System.out.println("Classification des dépêches");
        startTime = System.currentTimeMillis();
        classementDepeches(testDepeches, categories, "./fichier-reponse2_automatique");
        System.out.println("Temps d'exécution pour classementDepeches (lexiques automatiques) : " + (System.currentTimeMillis() - startTime) + " ms");

        System.out.println("Fichier 'fichier-reponse2_automatique' créé avec succès.");



        // Test de la méthode KNN
        startTime = System.currentTimeMillis();
        System.out.println("\n========================================");
        System.out.print("Test de la méthode KNN.\nEntrez un entier K représentant le nombre de dépêches les plus proches à comparer : ");
        Scanner lecteur = new Scanner(System.in);
        int k = lecteur.nextInt();
        lecteur.nextLine();

        ArrayList<Categorie> categorieKnn = new ArrayList<>();
        for (Depeche depecheCourante : depeches) {
            ArrayList<Depeche> voisins = knn.voisinsK(depeches, depecheCourante, k);
            Categorie categoriePredite = knn.categorieDepeche(depecheCourante, voisins, listCategorie);
            categorieKnn.add(categoriePredite);
        }
        knn.classementResultat(depeches, categorieKnn, listCategorie, "fichier-reponse-knn");
        System.out.println("Fichier 'fichier-reponse-knn' créé avec succès.");
        System.out.println("Temps d'exécution pour méthode KNN : " + (System.currentTimeMillis() - startTime) + " ms");

        // Fin du programme
        long globalEndTime = System.currentTimeMillis();
        System.out.println("\nProgramme total exécuté en : " + (globalEndTime - globalStartTime) + " ms");
    }




}




