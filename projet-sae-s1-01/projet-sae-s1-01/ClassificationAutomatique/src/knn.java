import java.io.FileInputStream;
import java.io.FileWriter;
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
}
