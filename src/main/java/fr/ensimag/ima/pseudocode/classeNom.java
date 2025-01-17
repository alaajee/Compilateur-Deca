package fr.ensimag.ima.pseudocode;

/*
On genere le DVal pour la construction de la table des méthodes
 */


public class classeNom extends DVal {
    private String nom;
    private String methode;
    public classeNom(String nom,String methode) {
        this.nom = nom;
        this.methode = methode;
    }

    @Override
    public String toString() {
        return "code."+nom+"."+methode ;
    }


}