import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Classification2 {


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
                int number = j + 1;
                String nombreFormate = String.valueOf(1000 + number).substring(1);
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


    // La méthode initDico retourne une liste de PaireChaineEntier
    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        int nbComparaisons = 0;
        // Liste qui contiendra les paires (mot, score)
        categorie = categorie.toUpperCase();
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        // Parcours de toutes les dépêches
        for (Depeche depeche : depeches) {
            // Si la dépêche appartient à la catégorie demandée
            nbComparaisons++;
            if (depeche.getCategorie().equals(categorie)) {
                // Récupération des mots de la dépêche
                ArrayList<String> mots = depeche.getMots();  // La méthode getMots retourne directement une ArrayList<String>

                // Parcours des mots de la dépêche
                for (String mot : mots) {

                    // Vérification si le mot n'est pas déjà présent dans resultat
                    boolean present = false;
                    for (int i = 0; i < resultat.size() && !present; i++) {  //j'ai utilisé une fonction que j'ai rajouté pour ne pas compter
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
        System.out.println(nbComparaisons);
        return resultat;
    }


    public static ArrayList<PaireChaineEntier> initDicoDicho(ArrayList<Depeche> depeches, String categorie) {
        categorie = categorie.toUpperCase();
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        int nbComparaisons = 0;


        // Parcours de toutes les dépêches
        for (Depeche depeche : depeches) {
            // Si la dépêche appartient à la catégorie demandée
            nbComparaisons++;
            if (depeche.getCategorie().equals(categorie)) {
                // Récupération des mots de la dépêche
                ArrayList<String> mots = depeche.getMots(); // La méthode getMots retourne directement une ArrayList<String>
                Utilitaire.triFusion(mots, 0, mots.size() - 1);
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
        System.out.println(nbComparaisons);
        // Retourner la liste contenant les paires (mot, score)
        return resultat;
    }

    // Méthode de recherche dichotomique pour trouver un mot dans une liste triée
    //renvoie l'indice du mot ou alors l'indice ou il faut l'insérer grace à une technique d'inversion à l'indice 0
    // et renvoie le nb de comparaisons à l'indice 1
    private static ArrayList<Integer> rechercheDichotomique(ArrayList<PaireChaineEntier> liste, String mot) {
        int nbComparaisons = 1;
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
    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        categorie = categorie.toUpperCase();
        // Parcours de toutes les dépêches
        for (int i = 0; i < depeches.size(); i++) {
            Depeche depecheCourante = depeches.get(i);

            // Vérification si la dépêche appartient à la catégorie demandée
            boolean estDansLaCategorie = depecheCourante.getCategorie().equals(categorie);

            // Récupération des mots de la dépêche
            ArrayList<String> mots = depecheCourante.getMots();  // Cette méthode doit retourner une ArrayList<String>

            // Parcours des mots de la dépêche
            for (int j = 0; j < mots.size(); j++) {
                String motDeLaDepeche = mots.get(j);
                boolean present = false;
                // Parcours du dictionnaire pour trouver la PaireChaineEntier associée au mot
                for (int k = 0; k < dictionnaire.size() && !present; k++) {
                    PaireChaineEntier paire = dictionnaire.get(k);
                    if (paire.getChaine().equals(motDeLaDepeche)) {
                        // Si la dépêche appartient à la catégorie, incrémenter le score
                        if (estDansLaCategorie) {
                            paire.setEntier(paire.getEntier() + 1);  // Incrémentation
                        } else {
                            paire.setEntier(paire.getEntier() - 1);  // Décrémentation
                        }
                        present = true;
                    }
                }
            }
        }
    }

    public static int poidsPourScore(int score) {
        if (score <= -2) {
            return 0; // Score très négatif : Poids faible
        } else if (score <= 0) {
            return 1;
        } else if (score <= 2) {
            return 2; // Score modéré (négatif ou positif) : Poids moyen
        } else {
            return 3; // Score très positif : Poids élevé
        }
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier + ".txt");
            ArrayList<PaireChaineEntier> dictionnaire = initDicoDicho(depeches, categorie);
            calculScores(depeches, categorie, dictionnaire);
            for (PaireChaineEntier paire : dictionnaire) {
                String mot = paire.getChaine();

                file.write(mot + ":" + poidsPourScore(paire.getEntier()) + "\n");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Commencement de la prise en compte automatique d'autres jeux de données (classification2)

    public static ArrayList<Categorie> decouvrirCategories(ArrayList<Depeche> depeches) {
        ArrayList<String> categoriesTrouvees = new ArrayList<>();

        // Parcours des dépêches pour extraire les categories uniques
        for (Depeche depeche : depeches) {
            if (!categoriesTrouvees.contains(depeche.getCategorie()))
                categoriesTrouvees.add(depeche.getCategorie().toUpperCase()); //si la categorie n'existe pas dans cat trouvés : on l'ajoute
        }

        // Création de nouveaux objets Categorie a partir des categories decouvertes
        ArrayList<Categorie> categories = new ArrayList<>();
        for (int i = 0; i < categoriesTrouvees.size(); i++) {
            categories.add(new Categorie(categoriesTrouvees.get(i)));
        }
        return categories;
    }






    public static void main(String[] args) {
        //on extrait les categories et classe les depeches;
        System.out.println("Chargement des depeches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches2.txt"); //creation fichier depeches
        ArrayList<Categorie> categories = decouvrirCategories(depeches); //creation des categories


        System.out.println("Initialisation des lexiques");
        for (Categorie categorie : categories) {
            generationLexique(depeches,categorie.getNom(),"./"+categorie.getNom().toLowerCase()+"-lexique2-automatique");
        }
        for (int i =0; i < categories.size(); i++) {
            categories.get(i).initLexique("./"+categories.get(i).getNom().toLowerCase()+"-lexique2-automatique.txt"); // initialisation des lexiques
        }
        classementDepeches(depeches, categories, "./fichier-reponses2");







        }





    }























