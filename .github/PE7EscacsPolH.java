import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class PE7EscacsPolH {
    public static void main(String[] args) {
        PE7EscacsPolH p = new PE7EscacsPolH();
        p.principal();
    }

    Scanner scanner = new Scanner(System.in);
    String peça = "";
    int modeCasella = 0;
    boolean errorCoord = false;
    char[] eliminadesBlanques = new char[16];
    char[] eliminadesNegres = new char[16];
    int countB = 0;
    int countN = 0;
    boolean blancElimina = false;
    boolean negreElimina = false;
    boolean sortir = false;
    boolean errorColor = false;
    boolean abandonar = false;
    ArrayList<String> movimentsBlanc = new ArrayList<>();
    ArrayList<String> movimentsNegre = new ArrayList<>();

    public void principal() {

        System.out.println("ESCACS");
        System.out.println("");
        char[][] tauler = new char[8][8];
        boolean[][] blancNegre = new boolean[8][8]; // true=blanc false=negre
        inserirPeces(tauler, blancNegre);
        int n = 0;
        String[] noms = new String[2];
        demanarNoms(noms, n);
        jugar(tauler, blancNegre, n, noms);

    }

    public void demanarNoms(String[] noms, int n) {
        String nomTemp = "";
        String triarColor = "";
        for (int c = 0; c < noms.length; c++) {
            noms[c] = "empty";
        }
        boolean error = false;
        for (int c = 0; c < noms.length; c++) {
            System.out.print("Indica el teu nom: ");
            nomTemp = tryCatchString();
            do {
                error = false;
                System.out.println("Quines peces vols jugar? (blanques/negres): ");
                triarColor = tryCatchString();
                if (triarColor.equalsIgnoreCase("blanques") && noms[0].equals("empty")) {
                    noms[0] = nomTemp;
                } else if (triarColor.equalsIgnoreCase("negres") && noms[1].equals("empty")) {
                    noms[1] = nomTemp;
                } else {
                    System.out.println("Tria un color vàlid o que no s'hagi triat previament");
                    error = true;
                }
            } while (error);

        }
    }

    public void jugar(char[][] tauler, boolean[][] blancNegre, int n, String[] noms) {

        int ronda = 1;
        String torn = "";

        int filaTemp = 0;
        int columna = 0;
        int fila = 0;
        String color = "";
        int countKings = 0;
        boolean tornarJugar = false;

        do {
            ronda = 1;
            countKings = 2;
            countB = 0;
            countN = 0;
            blancElimina = false;
            negreElimina = false;
            tornarJugar = false;
            abandonar = false;
            abandonar = false;
            movimentsBlanc.clear();
            movimentsNegre.clear();
            // Per esborrar tot en cas de tornar a començar partida
            for (int i = 0; i < 16; i++) {
                eliminadesBlanques[i] = '\0';
                eliminadesNegres[i] = '\0';
            }

            inserirPeces(tauler, blancNegre);

            do {

                imprimirTauler(tauler);
                imprimirEliminats();
                modeCasella = 0;
                if (ronda % 2 == 0) {
                    torn = "negre";
                    n = 1;
                } else {
                    torn = "blanc";
                    n = 0;
                }

                System.out.println("Torn de " + noms[n] + " jugador " + torn);
                System.out.println("Per abandonar escriu 'abandonar'");
                System.out.print("Introdueix coordenada (ex: 2H): ");

                errorColor = false;
                int[] coordenades = new int[2]; // Array per guardar fila i columna
                do {
                    errorColor = false;
                    sortir = false;

                    comprovarCasella(filaTemp, columna, fila, tauler, blancNegre, coordenades, countKings);

                    if (!abandonar) {

                        fila = coordenades[0];
                        columna = coordenades[1];

                        color = blancONegre(blancNegre, fila, columna);

                        System.out.println("La peça seleccionada es " + peça + " " + color);

                        if (!color.equals(torn)) {
                            System.out.println("Has de seleccionar una peça del teu color!");
                            System.out.print("> ");
                            errorColor = true;
                        } else {
                            System.out.println("A quina posicio vols moure't? (escriu '00' per tornar a triar)");
                            System.out.print("> ");

                            moviments(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça,
                                    countKings);

                            if (sortir) {
                                System.out.println("Torna a triar peça:");
                                System.out.print("> ");
                                errorColor = true;
                            }
                        }
                    }

                } while (errorColor && !abandonar);

                ronda++;
                countKings = estatRei(tauler, countKings);
                if (countKings != 2) {
                    abandonar = true;
                }
            } while (countKings == 2 && !abandonar);

            if (abandonar && countKings == 2) {
                System.out.println("Partida abandonada!");
            } else {
                System.out.println("El joc ha acabat, " + color + " ha guanyat!");
                System.out.println("\n=== RESUM DE MOVIMENTS ===");
                System.out.println("\nMoviments Blanques:");
                for (int i = 0; i < movimentsBlanc.size(); i++) {
                    System.out.println((i + 1) + ". " + movimentsBlanc.get(i));
                }

                System.out.println("\nMoviments Negres:");
                for (int i = 0; i < movimentsNegre.size(); i++) {
                    System.out.println((i + 1) + ". " + movimentsNegre.get(i));
                }
                System.out.println("=========================\n");
            }

            boolean respostaValida = false;
            do {
                System.out.println("Vols tornar a jugar? (si/no)");
                String queFer = tryCatchString();

                if (queFer.equals("SI")) {
                    tornarJugar = true;
                    respostaValida = true;

                    System.out.println("Vols mantenir els mateixos jugadors? (si/no)");
                    String mantenir = tryCatchString();

                    if (mantenir.equals("SI")) {
                        // Si el guanyador és negre, intercanviar colors
                        if (color.equals("negre")) {
                            String temp = noms[0];
                            noms[0] = noms[1];
                            noms[1] = temp;
                            System.out.println("S'han canviat els colors. Ara " + noms[0] + " juga amb blanques.");
                        }
                    } else if (mantenir.equals("NO")) {
                        // Demanar nous noms
                        demanarNoms(noms, n);
                    } else {
                        System.out.println("Escriu 'si' o 'no'");
                        respostaValida = false;
                    }

                } else if (queFer.equals("NO")) {
                    tornarJugar = false;
                    respostaValida = true;
                    System.out.println("Fins aviat!");
                } else {
                    System.out.println("Escriu 'si' o 'no'");
                    respostaValida = false;
                }
            } while (!respostaValida);
        } while (tornarJugar);

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
        System.out.println("| ┌─────────────────┐");
        for (int fila = 0; fila < 8; fila++) {
            System.out.print("|" + n + "| ");
            for (int colum = 0; colum < 8; colum++) {

                System.out.print(tauler[fila][colum] + " ");
            }
            System.out.print("|");
            System.out.println("");
            n--;
        }
        System.out.println("| └─────────────────┘");
        System.out.println("|   A B C D E F G H ");
        System.out.println("");
    }

    public String blancONegre(boolean[][] blancNegre, int fila, int columna) {
        String color = "";
        if (blancNegre[fila][columna]) {
            color = "blanc";
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
        } else {
            System.out.println("Insereix alguna coordenada real");
            System.out.print("> ");
        }
        return p;

    }

    public void moviments(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int countKings) {
        int novaFila = 0;
        int novaColum = 0;
        int orientacio = 0;
        int doblecasella = 0;
        boolean[][] posibleLocations = new boolean[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                posibleLocations[f][c] = false;
            }
        }
        do {
            errorCoord = false;
            sortir = false;
            comprovarCasella(filaTemp, novaColum, novaFila, tauler, blancNegre, coordenades, countKings);

            // Només continua si NO vol sortir
            if (!sortir) {
                novaFila = coordenades[0];
                novaColum = coordenades[1];

                if (peça.equals("reina")) {
                    movimentReina(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça,
                            novaFila,
                            novaColum, orientacio, doblecasella, posibleLocations);
                }
                if (peça.equals("peó")) {
                    movimentPeo(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça, novaFila,
                            novaColum, orientacio, doblecasella);
                }
                if (peça.equals("torre")) {
                    movimentTorre(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça,
                            novaFila,
                            novaColum, orientacio, doblecasella, posibleLocations);
                }
                if (peça.equals("alfil")) {
                    movimentAlfil(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça,
                            novaFila,
                            novaColum, orientacio, doblecasella, posibleLocations);
                }
                if (peça.equals("cavall")) {
                    movimentCavall(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça,
                            novaFila,
                            novaColum, orientacio, doblecasella, posibleLocations);
                }
                if (peça.equals("rei")) {
                    movimentRei(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça, novaFila,
                            novaColum, orientacio, doblecasella, posibleLocations);
                }
            }

        } while (errorCoord && !sortir); // Surt del bucle si sortir = true
    }

    public void comprovarCasella(int filaTemp, int columna, int fila, char[][] tauler,
            boolean[][] blancNegre, int[] coordenades, int countKings) {

        do {
            String entrada = tryCatchString();
            errorCoord = false;

            if (entrada.equalsIgnoreCase("ABANDONAR")) {
                System.out.println("Has abandonat la partida!");
                abandonar = true;
                coordenades[0] = -1;
                coordenades[1] = -1;
                errorCoord = false; // Surt del bucle
            } else if (entrada.equals("00") && modeCasella == 1) {
                modeCasella = 0;
                sortir = true;
                coordenades[0] = -1;
                coordenades[1] = -1;
                errorCoord = false; // Surt del bucle
            } else if (entrada.length() != 2) {
                System.out.println("Has d'escriure 2 carácters");
                System.out.print("> ");
                errorCoord = true;
            } else {
                filaTemp = Character.getNumericValue(entrada.charAt(0));
                columna = entrada.charAt(1) - 'A';
                fila = 8 - filaTemp;

                if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
                    System.out.println("Introdueix entre '1A' i '8H'");
                    System.out.print("> ");
                    errorCoord = true;
                } else {
                    peça = identificarPeça(tauler, fila, columna);
                    if (modeCasella == 0) {
                        if (peça.equals("null")) {
                            System.out.println("L'ubicació seleccionada està buida, torna a triar.");
                            System.out.print("> ");
                            errorCoord = true;
                        } else {
                            modeCasella++;
                        }
                    }
                }
            }
        } while (errorCoord && !abandonar);

        coordenades[0] = fila;
        coordenades[1] = columna;
    }

    public void tramitCanvis(char[][] tauler, int novaFila, int novaColum, int fila, int columna,
            boolean[][] blancNegre) {
        String origen = convertirCoord(fila, columna);
        String desti = convertirCoord(novaFila, novaColum);
        String moviment = peça + " de " + origen + " a " + desti;

        if (blancNegre[fila][columna]) {
            movimentsBlanc.add(moviment);
        } else {
            movimentsNegre.add(moviment);
        }
        tauler[novaFila][novaColum] = tauler[fila][columna];
        tauler[fila][columna] = '-';
        blancNegre[novaFila][novaColum] = blancNegre[fila][columna];
    }

    public void movimentPeo(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella)

    {
        int filaCanvi = 2;
        char qoQ = ' ';
        if (tauler[fila][columna] == 'p') {
            orientacio = 1;
            doblecasella = 1;
            filaCanvi = 7;
            qoQ = 'q';

        } else if (tauler[fila][columna] == 'P') {
            orientacio = -1;
            doblecasella = 6;
            filaCanvi = 0;
            qoQ = 'Q';
        }
        boolean killChanceL = false;
        boolean killChanceR = false;
        // comprovar si pot eliminar
        if (columna >= 1) {
            if ((tauler[fila + orientacio][columna - 1] != '-')
                    && (blancNegre[novaFila][novaColum] != blancNegre[fila][columna])) {
                killChanceL = true;
            }
        }
        if (columna <= 6) {
            if ((tauler[fila + orientacio][columna + 1] != '-')
                    && (blancNegre[novaFila][novaColum] != blancNegre[fila][columna])) {
                killChanceR = true;
            }
        }

        if (((columna == novaColum && novaFila == (fila + orientacio) && tauler[novaFila][novaColum] == '-')
                || (fila == doblecasella && columna == novaColum && novaFila == (fila + 2 * orientacio)
                        && tauler[novaFila][novaColum] == '-'))
                || (killChanceL && novaColum == columna - 1 && novaFila == (fila + orientacio))
                || (killChanceR && novaColum == columna + 1 && novaFila == (fila + orientacio))) {

            if (novaFila == filaCanvi) {
                tauler[fila][columna] = qoQ;
            }
            pecesEliminades(tauler, novaFila, novaColum, blancNegre);
            tramitCanvis(tauler, novaFila, novaColum, fila, columna, blancNegre);
        } else {
            errorSyso();
        }

    }

    public void errorSyso() {
        System.out.println("No pots tirar allà, torna-ho a intentar.");
        System.out.print("> ");
        errorCoord = true;
    }

    public void movimentTorre(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella, boolean[][] posibleLocations) {
        // Comprovar Amunt
        for (int count = 0, f = fila - 1; count == 0 && f >= 0; f--) {
            if (tauler[f][columna] == '-') {
                posibleLocations[f][columna] = true;
            } else {
                if (blancNegre[f][columna] != blancNegre[fila][columna]) {
                    posibleLocations[f][columna] = true;
                }
                count++;
            }
        }

        // Comprovar avall
        for (int count = 0, f = fila + 1; count == 0 && f <= 7; f++) {
            if (tauler[f][columna] == '-') {
                posibleLocations[f][columna] = true;
            } else {
                if (blancNegre[f][columna] != blancNegre[fila][columna]) {
                    posibleLocations[f][columna] = true;
                }
                count++;
            }
        }

        // Comprovar esquerre
        for (int count = 0, c = columna - 1; count == 0 && c >= 0; c--) {
            if (tauler[fila][c] == '-') {
                posibleLocations[fila][c] = true;
            } else {
                if (blancNegre[fila][c] != blancNegre[fila][columna]) {
                    posibleLocations[fila][c] = true;
                }
                count++;
            }
        }

        // Comprovar dreta
        for (int count = 0, c = columna + 1; count == 0 && c <= 7; c++) {
            if (tauler[fila][c] == '-') {
                posibleLocations[fila][c] = true;
            } else {
                if (blancNegre[fila][c] != blancNegre[fila][columna]) {
                    posibleLocations[fila][c] = true;
                }
                count++;
            }
        }

        // Comprovar si el moviment és vàlid
        if (tauler[fila][columna] == 't' || tauler[fila][columna] == 'T')
            if (posibleLocations[novaFila][novaColum]) {
                pecesEliminades(tauler, novaFila, novaColum, blancNegre);
                tramitCanvis(tauler, novaFila, novaColum, fila, columna, blancNegre);
            } else {
                errorSyso();
            }

    }

    public void movimentAlfil(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella, boolean[][] posibleLocations) {

        // Diagonal amunt esquerra
        for (int count = 0, f = fila - 1, c = columna - 1; count == 0 && f >= 0 && c >= 0; c--, f--) {
            if (tauler[f][c] == '-') {
                posibleLocations[f][c] = true;
            } else {
                if (blancNegre[f][c] != blancNegre[fila][columna]) {
                    posibleLocations[f][c] = true;
                }
                count++;
            }
        }

        // Diagonal avall dreta
        for (int count = 0, f = fila + 1, c = columna + 1; count == 0 && f <= 7 && c <= 7; c++, f++) {
            if (tauler[f][c] == '-') {
                posibleLocations[f][c] = true;
            } else {
                if (blancNegre[f][c] != blancNegre[fila][columna]) {
                    posibleLocations[f][c] = true;
                }
                count++;
            }
        }

        // Diagonal amunt dreta
        for (int count = 0, f = fila - 1, c = columna + 1; count == 0 && f >= 0 && c <= 7; c++, f--) {
            if (tauler[f][c] == '-') {
                posibleLocations[f][c] = true;
            } else {
                if (blancNegre[f][c] != blancNegre[fila][columna]) {
                    posibleLocations[f][c] = true;
                }
                count++;
            }
        }

        // Diagonal avall esquerra
        for (int count = 0, f = fila + 1, c = columna - 1; count == 0 && f <= 7 && c >= 0; c--, f++) {
            if (tauler[f][c] == '-') {
                posibleLocations[f][c] = true;
            } else {
                if (blancNegre[f][c] != blancNegre[fila][columna]) {
                    posibleLocations[f][c] = true;
                }
                count++;
            }
        }

        if (posibleLocations[novaFila][novaColum]) {
            pecesEliminades(tauler, novaFila, novaColum, blancNegre);
            tramitCanvis(tauler, novaFila, novaColum, fila, columna, blancNegre);
        } else {
            errorSyso();
        }
    }

    public void movimentReina(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella, boolean[][] posibleLocations) {
        movimentTorre(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça, novaFila,
                novaColum, orientacio, doblecasella, posibleLocations);
        movimentAlfil(tauler, color, fila, columna, ronda, filaTemp, coordenades, blancNegre, peça, novaFila, novaColum,
                orientacio, doblecasella, posibleLocations);
    }

    public void movimentCavall(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella, boolean[][] posibleLocations) {

        if (fila + 2 <= 7 && columna + 1 <= 7) {
            if (tauler[fila + 2][columna + 1] == '-'
                    || blancNegre[fila + 2][columna + 1] != blancNegre[fila][columna]) {
                posibleLocations[fila + 2][columna + 1] = true;
            }
        }

        if (fila + 2 <= 7 && columna - 1 >= 0) {
            if (tauler[fila + 2][columna - 1] == '-'
                    || blancNegre[fila + 2][columna - 1] != blancNegre[fila][columna]) {
                posibleLocations[fila + 2][columna - 1] = true;
            }
        }

        if (fila - 2 >= 0 && columna + 1 <= 7) {
            if (tauler[fila - 2][columna + 1] == '-'
                    || blancNegre[fila - 2][columna + 1] != blancNegre[fila][columna]) {
                posibleLocations[fila - 2][columna + 1] = true;
            }
        }

        if (fila - 2 >= 0 && columna - 1 >= 0) {
            if (tauler[fila - 2][columna - 1] == '-'
                    || blancNegre[fila - 2][columna - 1] != blancNegre[fila][columna]) {
                posibleLocations[fila - 2][columna - 1] = true;
            }
        }

        if (fila + 1 <= 7 && columna + 2 <= 7) {
            if (tauler[fila + 1][columna + 2] == '-'
                    || blancNegre[fila + 1][columna + 2] != blancNegre[fila][columna]) {
                posibleLocations[fila + 1][columna + 2] = true;
            }
        }

        if (fila + 1 <= 7 && columna - 2 >= 0) {
            if (tauler[fila + 1][columna - 2] == '-'
                    || blancNegre[fila + 1][columna - 2] != blancNegre[fila][columna]) {
                posibleLocations[fila + 1][columna - 2] = true;
            }
        }

        if (fila - 1 >= 0 && columna + 2 <= 7) {
            if (tauler[fila - 1][columna + 2] == '-'
                    || blancNegre[fila - 1][columna + 2] != blancNegre[fila][columna]) {
                posibleLocations[fila - 1][columna + 2] = true;
            }
        }

        if (fila - 1 >= 0 && columna - 2 >= 0) {
            if (tauler[fila - 1][columna - 2] == '-'
                    || blancNegre[fila - 1][columna - 2] != blancNegre[fila][columna]) {
                posibleLocations[fila - 1][columna - 2] = true;
            }
        }

        if (posibleLocations[novaFila][novaColum]) {
            pecesEliminades(tauler, novaFila, novaColum, blancNegre);
            tramitCanvis(tauler, novaFila, novaColum, fila, columna, blancNegre);
        } else {
            errorSyso();
        }
    }

    public void movimentRei(char[][] tauler, String color, int fila, int columna, int ronda,
            int filaTemp, int[] coordenades, boolean[][] blancNegre, String peça, int novaFila, int novaColum,
            int orientacio, int doblecasella, boolean[][] posibleLocations) {
        for (int c = columna - 1; c <= columna + 1; c++) {
            for (int f = fila - 1; f <= fila + 1; f++) {
                if (f >= 0 && f <= 7 && c >= 0 && c <= 7) {
                    if (!(f == fila && c == columna)) {
                        if (tauler[f][c] == '-' || blancNegre[f][c] != blancNegre[fila][columna]) {
                            posibleLocations[f][c] = true;
                        }
                    }
                }
            }
        }
        if (posibleLocations[novaFila][novaColum]) {
            pecesEliminades(tauler, novaFila, novaColum, blancNegre);
            tramitCanvis(tauler, novaFila, novaColum, fila, columna, blancNegre);
        } else {
            errorSyso();
        }
    }

    public int estatRei(char[][] tauler, int countKings) {
        countKings = 0;
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (tauler[f][c] == 'k' || tauler[f][c] == 'K') {
                    countKings++;
                }
            }
        }
        return countKings;
    }

    public void pecesEliminades(char[][] tauler, int novaFila, int novaColum, boolean[][] blancNegre) {
        if (tauler[novaFila][novaColum] != '-') {

            if (blancNegre[novaFila][novaColum]) {
                blancElimina = true;
                eliminadesBlanques[countB] = tauler[novaFila][novaColum];
                countB++;
            } else {
                negreElimina = true;
                eliminadesNegres[countN] = tauler[novaFila][novaColum];
                countN++;
            }
        }

    }

    public String tryCatchString() {
        String input = "";
        boolean error = false;

        do {
            error = false;
            try {
                input = scanner.nextLine().toUpperCase();
                if (input.length() < 1) {
                    System.out.println("Introdueix un nom");
                    error = true;
                }

            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: entrada invàlida");
                System.out.print("> ");

                error = true;
            } catch (Exception e) {
                System.out.println("Error inesperat");
                System.out.print("> ");
                error = true;
            }

        } while (error);

        return input;
    }

    public void imprimirEliminats() {
        if (blancElimina) {
            System.out.print("El blanc ha eliminat a: ");
            for (int c = 0; c < countN; c++) {
                System.out.print(eliminadesNegres[c] + " ");
            }
            System.out.println("");
        }
        if (negreElimina) {
            System.out.print("El negre ha eliminat a: ");
            for (int c = 0; c < countB; c++) {
                System.out.print(eliminadesBlanques[c] + " ");
            }
            System.out.println("");
        }

    }

    public int llegrInt() {
        boolean ok = true;
        int entrada = 0;
        do {
            ok = true;
            entrada = 0;
            try {
                entrada = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error en el tipus d'entrada");
                ok = false;
            } catch (Exception e) {
                System.out.println("Error inesperat, torna a provar-ho");
                ok = false;
            }

        } while (!ok);
        return entrada;
    }

    public String convertirCoord(int fila, int columna) {
        int filaUsuari = 8 - fila;
        char columnaUsuari = (char) ('A' + columna);
        return filaUsuari + "" + columnaUsuari;
    }
}