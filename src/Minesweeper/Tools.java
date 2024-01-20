package Minesweeper;

import java.util.Scanner;

/**
 *
 * @author ivangarcia
 */
abstract public class Tools {

    static final Scanner scanner = new Scanner(System.in);

    /**
     * Retorna un numero double obtingut desde l'input
     * @return numero double obtingut del input de l'usuari
     */
    public static double getDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Introdueix un numero valid!");
            scanner.nextLine();
        }
        return scanner.nextDouble();

    }

    /**
     * Retorna un numero int obtingut desde l'input
     *
     * @return numero int obtingut del input de l'usuari
     */
    public static int getInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Introdueix un numero valid!");
            scanner.nextLine();
        }
        int num = scanner.nextInt();
        return num;
    }

    /**
     * Igual que {@link Tools#getInt()} pero amb un rang passat
     * com parametres
     *
     * @param min int numero minin a introduir
     * @param max int numero maxim a introduir
     * @return int numero introduir per l'usuari
     */
    public static int getInt(int min, int max) {

        while (!scanner.hasNextInt()) {
            System.out.println("Introdueix un numero valid!");

            scanner.nextLine();
        }
        int num = scanner.nextInt();
        while (num < min || num > max) {
            System.out.printf("Introdueix un numero entre %d i %d\n", min, max);
            num = getInt(min, max);

        }

        return num;
    }

    /**
     * Igual que {@link Tools#getInt(int min, int max)} pero amb un minin només
     * com parametre
     *
     * @param min int numero minin a introduir
     *
     * @return int numero introduir per l'usuari
     */
    public static int getInt(int min) {

        while (!scanner.hasNextInt()) {
            System.out.println("Introdueix un numero valid!");

            scanner.nextLine();
        }
        int num = scanner.nextInt();
        while (num < min) {
            System.out.printf("Introdueix un numero major o igual que %d\n", min);
            num = getInt(min);
        }

        return num;
    }

    /**
     * Retorna una string
     * @return string que introdueix l'usuari
     */
    public static String getString() {
        String paraula;
        do {
            System.out.println("Introdueix una cadena");
            paraula = scanner.nextLine();
        } while (paraula.length() == 0);

        return paraula;
    }

}
