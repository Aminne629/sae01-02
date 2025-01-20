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
    public void initLexique(String nomFichier) {

    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        int score = 0;



        for (String mot: d.getMots()){// on parcourt chaque mot de la depeche
            for (PaireChaineEntier paire : lexique){
                if (paire.getChaine().equals(mot))
                    score +=paire.getEntier();
            }
        }
        return score;
    }


}
