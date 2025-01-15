package fr.ensimag.ima.pseudocode;

/*
On genere le DVal pour la construction de la table des méthodes
 */


public class classeNom extends DVal {
    private String nom;
    public classeNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "code."+nom+".equals" ;
    }

    public DVal getObject() {
        return new classeNom("Object") ;
    }
}