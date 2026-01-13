import java.util.Scanner;

public class PE7EscacsPolH {
    public static void main(String[] args) {
        PE7EscacsPolH p = new PE7EscacsPolH();
        p.principal();
    }

    Scanner scanner = new Scanner(System.in);
    String peça = "";

    public void principal() {
        System.out.println("ESCACS");
        System.out.println("");
        char[][] tauler = new char[8][8];
        boolean[][] blancNegre = new boolean[8][8]; // true=blanc false=negre
        inserirPeces(tauler, blancNegre);
        imprimirTauler(tauler);
        Scanner scanner = new Scanner(System.in);

        jugar(tauler, blancNegre);

    }

    public void inserirPeces(char[][] tauler, boolean[][] blancNegre) {
        /* Inserir espai buit '-' */
        for (int fila = 0; fila < 8; fila++) {
            for (int colum = 0; colum < 8; colum++) {
                tauler[fila][colum] = '-';
                if (fila == 0 || fila == 1) {
                    blancNegre[fila][colum] = false;
                } else if (fila == 6 || fila == 7) {
                    blancNegre[fila][colum] = true;
                }
            }
        }

        /* Inserir peons */

        for (int colum = 0; colum < 8; colum++) {
            tauler[1][colum] = 'p';
        }
        for (int colum = 0; colum < 8; colum++) {
            tauler[6][colum] = 'P';
        }
        /* Inserir Cavalls */
        tauler[7][6] = 'C';
        tauler[7][1] = 'C';
        tauler[0][6] = 'c';
        tauler[0][1] = 'c';
        /* Inserir Torres */
        tauler[0][0] = 't';
        tauler[0][7] = 't';
        tauler[7][7] = 'T';
        tauler[7][0] = 'T';
        /* Inserir alfils */
        tauler[7][2] = 'A';
        tauler[7][5] = 'A';
        tauler[0][5] = 'a';
        tauler[0][2] = 'a';
        /* Inserir Reines */
        tauler[7][3] = 'Q';
        tauler[0][3] = 'q';
        /* Inserir Reis */
        tauler[0][4] = 'k';
        tauler[7][4] = 'K';

    }

    public void imprimirTauler(char[][] tauler) {
        int n = 8;
        for (int fila = 0; fila < 8; fila++) {
            System.out.print(n + "|");
            for (int colum = 0; colum < 8; colum++) {

                System.out.print(tauler[fila][colum] + " ");
            }
            System.out.println("");
            n--;
        }
        System.out.println("/|///////////////");
        System.out.println("/|A B C D E F G H");
        System.out.println("");
    }

    public void configTorns(char[][] tauler) {

    }

    public void jugar(char[][] tauler, boolean[][] blancNegre) {
        System.out.print("Introdueix coordenada (ex: 2H): ");

        boolean errorCoord = false;
        int filaTemp;
        int columna;
        int fila;
        do {
            String entrada = scanner.nextLine().toUpperCase();
            errorCoord = false;
            filaTemp = Character.getNumericValue(entrada.charAt(0));
            columna = entrada.charAt(1) - 'A';
            /* Invertir fila */
            fila = 8 - filaTemp;

            if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
                System.out.println("Introdueix entre '1A' i '8H'");
                errorCoord = true;
            }

            peça = identificarPeça(tauler, fila, columna);
            if (peça.equals("null")) {
                System.out.println("L'ubicació seleccionada està buida, torna a triar.");
                System.out.println("");
                errorCoord = true;
            }
        } while (errorCoord);

        String color = blancONegre(blancNegre, fila, columna);
        System.out.println("La peça seleccionada es " + peça + " " + color);
        configTorns(tauler);
    }

    public String blancONegre(boolean[][] blancNegre, int fila, int columna) {
        String color = "";
        if (blancNegre[fila][columna]) {
            color = "blanca";
        } else {
            color = "negre";
        }
        return color;
    }

    public String identificarPeça(char[][] tauler, int fila, int columna) {
        String p = "";
        if (tauler[fila][columna] == 'p' || tauler[fila][columna] == 'P') {
            p = "peó";
        } else if (tauler[fila][columna] == 'k' || tauler[fila][columna] == 'K') {
            p = "rei";
        } else if (tauler[fila][columna] == 'q' || tauler[fila][columna] == 'Q') {
            p = "reina";
        } else if (tauler[fila][columna] == 'c' || tauler[fila][columna] == 'C') {
            p = "cavall";
        } else if (tauler[fila][columna] == 't' || tauler[fila][columna] == 'T') {
            p = "torre";
        } else if (tauler[fila][columna] == 'a' || tauler[fila][columna] == 'A') {
            p = "alfil";
        } else if (tauler[fila][columna] == '-') {
            p = "null";
        }
        return p;

    }
}
