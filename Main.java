public class Main {
    public static void main(String[] args) {
        int x = 10;
        int y = 15;
        int z = 20;
        int result = 0;
        // Boucle 3 : Multiplication conditionnelle
        while (z > 10) {
            if ((z % 6 != 0 && z % 5 == 0) || (z % 4 == 0)
                    || (z % 3 == 0 && z % 2 != 0)

            ) {
                result = result + 2;
            }
            z = z - 1;
        }
        System.out.println(result);
    }
}