import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
                String nombreFormate = String.format("%03d", j + 1);
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
            file.write("MOYENNE:"+UtilitairePaireChaineEntier.moyenne(listePourLaMoyenne) + "%\n");
            System.out.println("Votre saisie a été écrite avec succès dans " + nomFichier + ".txt");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
    }

    public static int poidsPourScore(int score) {
        return 0;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

    }



    public static void main(String[] args) {

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");

        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }



        System.out.println("Initialisation des lexiques");


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
        System.out.println(economie.getLexique());


        for(int i=0; i<sport.getLexique().size();i++){
            System.out.print(sport.getLexique().get(i).getChaine() + ":");
            System.out.println(sport.getLexique().get(i).getEntier());
        }

        Scanner lecteur = new Scanner(System.in);
        System.out.print("Saisissez un mot à rechercher dans les lexiques: ");
        String mot = lecteur.nextLine();
        System.out.println(UtilitairePaireChaineEntier.entierPourChaine(sport.getLexique(),mot));


        System.out.println(sport.score(depeches.get(402)));
        System.out.println(economie.score(depeches.get(204)));


        ArrayList<Categorie> listCategorie = new ArrayList<>();
        listCategorie.add(sport);
        listCategorie.add(economie);
        listCategorie.add(sciences);
        listCategorie.add(politique);
        listCategorie.add(culture);

        System.out.print("Donnez un  numéro de depeche: ");
        int numDepeche = lecteur.nextInt();lecteur.nextLine();
        ArrayList<PaireChaineEntier> listeScore = new ArrayList<>();
        for (int i = 0; i < listCategorie.size(); i++) {

            int nombreMax = listCategorie.get(i).score(depeches.get(numDepeche));
            PaireChaineEntier aAjouter = new PaireChaineEntier(listCategorie.get(i).getNom(), nombreMax);
            listeScore.add(aAjouter);

        }


        System.out.println(UtilitairePaireChaineEntier.chaineMax(listeScore));
        classementDepeches(depeches,listCategorie,"fichier-reponse");

    }




}

