public class EstGrand {
    // Déclaration du champ x avec un accès protégé
    protected int x;

    // Méthode pour obtenir la valeur de x
    int getX() {
        return x;
    }

    public static void main(String[] args) {
        // Création d'une instance de A
        EstGrand a = new EstGrand();

        // Affichage de la valeur de x via la méthode getX
        System.out.println("a.getX() = " + a.getX()); // Devrait afficher "a.getX() = 0"
    }
}
