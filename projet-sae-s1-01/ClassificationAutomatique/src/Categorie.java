import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public static ArrayList<PaireChaineEntier> initLexique(String nomFichier) {
        ArrayList<PaireChaineEntier> lexique = new ArrayList<>();
        try {
            // Lecture du fichier
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner lecteur = new Scanner(file);

            while (lecteur.hasNextLine()) {
                String ligne = lecteur.nextLine();

                // format "mot:valeur"
                int indexDeuxPoints = ligne.indexOf(':');
                if (indexDeuxPoints != -1) {
                    String chaine = ligne.substring(0, indexDeuxPoints).trim(); // Partie avant ':'
                    int entier = Integer.parseInt(ligne.substring(indexDeuxPoints + 1).trim()); // Partie après ':'

                    // Ajout de la paire au lexique
                    PaireChaineEntier paire = new PaireChaineEntier(chaine, entier);
                    lexique.add(paire);
                }
            }

            lecteur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lexique;
    }






    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        return 0;
    }


}
