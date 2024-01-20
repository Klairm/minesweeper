package Minesweeper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ivangarcia
 */
public class Minesweeper {
    // Everything is public for the javadoc lol

    final public static char COVER = 'X';
    final public static char CLEAR = '-';
    final public static char BOMB = '!';
    public static long START_TIME = 0;

    public static void main(String[] args) {

        mainMenu();

    }

    /**
     * Shows main menu
     */
    public static void mainMenu() {
        int userOption = 0;
        GameOptions gameOptions = new GameOptions();
        Gametable table = null;

        boolean exit = false;
        do {

            System.out.printf("1. Nivell 1: Taulell 8x8 amb 10 bombes\n"
                    + "2. Nivell 2: Taulell 16x20 amb 50 bombes\n"
                    + "3. Nivell personalitzat\n"
                    + "4. Sortir\n"
                    + "\nIntrodueix una opcio:");

            userOption = Tools.getInt();

            switch (userOption) {
                case 1:
                    gameOptions.HEIGHT = 8;
                    gameOptions.WIDTH = 8;
                    gameOptions.totalBombs = 10;

                    table = initializeTable(gameOptions);
                    playMenu(table);

                    break;
                case 2:
                    gameOptions.HEIGHT = 16;
                    gameOptions.WIDTH = 20;
                    gameOptions.totalBombs = 50;

                    table = initializeTable(gameOptions);
                    playMenu(table);

                    break;
                case 3:
                    gameOptions = getGameOptions();
                    table = initializeTable(gameOptions);

                    playMenu(table);

                    break;

                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Opcio incorrecta.");
                    break;
            }

        } while (!exit);
    }

    /**
     * Shows the menu where the user will play on
     *
     * @param table Gametable the table to play on
     */
    public static void playMenu(Gametable table) {

        table.exit = false;

        START_TIME = System.nanoTime();
        System.out.printf("Jugaras amb un tauler %d * %d i un total de %d bombes.\n", table.HEIGHT, table.WIDTH, table.totalBombs);
        do {
            System.out.printf("\nTemps: %d\n\n", getTimeElapsed(START_TIME));

            showTable(table);

            table.cheat = false;

            table.userAttack = getUserAttack(table);

            // If the user press 0 enable the cheat
            if (table.userAttack.xPosition == -1 || table.userAttack.yPosition == -1) {
                table.cheat = true;
            } else {

                Box gameBox = table.playerTable[table.userAttack.yPosition][table.userAttack.xPosition];

                if (gameBox.hasBomb) {

                    gameBox.hidden = false;
                    gameOver(table);

                } else if (gameBox.hidden == false) {

                    System.out.println("Aquesta casella ja esta descoberta.");

                } else {

                    gameBox.symbol = CLEAR;
                    gameBox.hidden = false;
                    revealSurrounding(table, gameBox);

                    if (gameBox.bombsAround > 0) {
                        table.uncoverNumbers++;
                    }

                    if (checkWin(table)) {
                        winGame(table);
                    }
                }
            }

        } while (!table.exit);
    }

    /**
     * Returns a register with the options for the game
     *
     * @return GameOptions
     */
    public static GameOptions getGameOptions() {
        GameOptions options = new GameOptions();

        System.out.println("Tria les opcions a personalitzar:");

        System.out.printf("Altura: ");
        options.HEIGHT = Tools.getInt(4);

        System.out.printf("\nAmplada:");
        options.WIDTH = Tools.getInt(4);

        System.out.printf("\nTotal de bombes: ");
        // TODO: Set bombs using percentatge instead.
        options.totalBombs = Tools.getInt(options.HEIGHT, options.WIDTH);

        return options;

    }

    /**
     * Initializes the table to be played on
     *
     * @param options GameOptions the options for the table to initialize
     * @return Gametable the table to play with
     */
    public static Gametable initializeTable(GameOptions options) {
        Gametable table = new Gametable();

        table.HEIGHT = options.HEIGHT;

        table.WIDTH = options.WIDTH;

        table.totalBombs = options.totalBombs;

        table.playerTable = new Box[table.HEIGHT][table.WIDTH];

        table.cheat = false;

        fillTable(table);

        return table;

    }

    /**
     * Reveal the clear boxes surrounding the gamebox The code is basically the
     * same as
     * {@link Minesweeper#setBombsAround(garciadiscipioivan_m3_uf2_buscamines.Gametable, garciadiscipioivan_m3_uf2_buscamines.Box)}
     * with just some checks inside the inner for to make sure we are not
     * clearing around a number box
     *
     * @param table Gametable
     * @param gameBox Box
     */
    public static void revealSurrounding(Gametable table, Box gameBox) {

        final int Y = gameBox.positionY;
        final int X = gameBox.positionX;

        for (int row = Y - 1; row <= Y + 1; row++) {
            for (int column = X - 1; column <= X + 1; column++) {
                if (row >= 0 && row < table.HEIGHT && column >= 0 && column < table.WIDTH) {
                    if (table.playerTable[row][column].hidden == true && table.playerTable[row][column].symbol == CLEAR && gameBox.bombsAround == 0) {
                        table.playerTable[row][column].hidden = false;
                        gameBox = table.playerTable[row][column];
                        revealSurrounding(table, gameBox);
                    }

                }
            }
        }

    }

    /**
     * Prints the table passed as argument
     *
     * @param table Gametable table to show
     */
    public static void showTable(Gametable table) {
        System.out.printf("  ");
        for (int width = 0; width < table.WIDTH; width++) {
            System.out.printf("%d ", width + 1);
        }
        System.out.printf("\n\n");
        for (int row = 0; row < table.HEIGHT; row++) {
            System.out.printf("%d ", row + 1);
            for (int column = 0; column < table.WIDTH; column++) {
                Box box = table.playerTable[row][column];

                // TODO: setBombsAround should be done at initializeTable() for better perfomance
                setBombsAround(table, box);

                if (table.cheat || !box.hidden) {
                    System.out.printf("%c ", box.symbol);

                } else {
                    System.out.printf("%c ", COVER);
                }

            }
            System.out.println("");
        }
    }

    /**
     * Fill the system table and the game table arrays
     *
     * @param table Gametable the table to fill
     */
    public static void fillTable(Gametable table) {

        Box gameBox = null;

        for (int row = 0; row < table.HEIGHT; row++) {
            for (int column = 0; column < table.WIDTH; column++) {

                gameBox = new Box();
                gameBox.positionX = column;
                gameBox.positionY = row;
                gameBox.symbol = COVER;
                gameBox.hidden = true;

                table.playerTable[row][column] = gameBox;

            }
        }
        generateBombs(table);

    }

    /**
     * Gets time elapsed since the start of the game
     *
     * @param startTime int 
     * @return long Time in seconds
     */
    public static long getTimeElapsed(long startTime) {
        return TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }

    /**
     * Returns total points , representing the sum of all the cells uncover
     *
     * @param table Gametable
     * @return int total points
     */
    public static int getPoints(Gametable table) {
        int totalPoints = 0;
        for (int row = 0; row < table.HEIGHT; row++) {
            for (int column = 0; column < table.WIDTH; column++) {
                Box gameBox = table.playerTable[row][column];
                if (!gameBox.hidden) {
                    totalPoints += gameBox.bombsAround;
                }
            }
        }
        return totalPoints;
    }

    /**
     * Generates the bombs randomly into the gametable boxes.
     * @param table
     */
    public static void generateBombs(Gametable table) {
        Random rand = new Random();
        Box box = null;
        for (int i = 0; i < table.totalBombs; i++) {
            box = table.playerTable[rand.nextInt(table.HEIGHT)][rand.nextInt(table.WIDTH)];
            box.hasBomb = true;
            box.symbol = BOMB;

        }

    }

    /**
     * Sets the number of bombs around the game box passed as argument
     *
     * @param table Gametable the table of the game
     * @param gameBox Box the game box to set the numbers on
     */
    public static void setBombsAround(Gametable table, Box gameBox) {
        final int Y = gameBox.positionY;
        final int X = gameBox.positionX;

        if (gameBox.hasBomb) {
            gameBox.symbol = BOMB;
            return;
        }

        // initialize the bomb count to 0
        int bombCount = 0;

        /*  With this for loop we check a 3x3 area, so basically
            we start on the Y top position of the gameBox ( Y - 1 ) 
            until the last box of that row, so Y + 1.
        
            Then in the inside for we realize the same for the X ( column ) position
            and the various if are checking respectively:
            A: the position is not smaller or bigger than the table
            B: the position is not the center box
            C: that position has a bomb
      
         */
        for (int row = Y - 1; row <= Y + 1; row++) {
            for (int column = X - 1; column <= X + 1; column++) {
                if (row >= 0 && row < table.HEIGHT && column >= 0 && column < table.WIDTH) {
                    if (!(row == Y && column == X)) {
                        if (table.playerTable[row][column].hasBomb) {
                            bombCount++;
                            gameBox.bombsAround = bombCount;

                        }
                    }
                }
            }

        }

        if (gameBox.bombsAround > 0) {
            gameBox.symbol = Character.forDigit(gameBox.bombsAround, Character.MAX_RADIX);

        } else {
            gameBox.symbol = CLEAR;
            gameBox.bombsAround = 0;

        }
    }

    /**
     * Checks the table to see if the player has won
     *
     * @param table Gametable
     * @return true or false depending if the player has won
     */
    public static boolean checkWin(Gametable table) {
        int totalNumbers = getTotalNumbers(table);
        return totalNumbers == table.uncoverNumbers;

    }

    /**
     * Handles exit and shows the table to the user when looses the game.
     *
     * @param table
     */
    public static void gameOver(Gametable table) {
        table.cheat = true;
        showTable(table);
        System.out.printf("Has perdut! Has tocat una bomba! Puntuacio: %d\n", getPoints(table));

        table.exit = true;
    }

    /**
     * Handles exit and shows the table to the user when wins the game.
     *
     * @param table
     */
    public static void winGame(Gametable table) {
        table.cheat = true;
        showTable(table);
        System.out.printf("Has guanyat! Puntuacio: %d\n", getPoints(table));
        table.exit = true;
    }

    /**
     * Returns the total of boxes with numbers on its
     *
     * @param table Gametable
     * @return int
     */
    public static int getTotalNumbers(Gametable table) {
        int total = 0;
        for (int row = 0; row < table.HEIGHT; row++) {
            for (int column = 0; column < table.WIDTH; column++) {
                if (table.playerTable[row][column].bombsAround > 0) {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * Sets the positions where the user will "attack" We substract 1 from user
     * input to avoid errors.
     *
     * @param table Gametable
     * @return UserAttack with positions attack
     */
    public static UserAttack getUserAttack(Gametable table) {
        table.userAttack = new UserAttack();
        System.out.println("Introdueix la posicio X de la casella: ");
        table.userAttack.xPosition = Tools.getInt(0, table.WIDTH) - 1;

        System.out.println("Introdueix la posicio Y de la casella: ");
        table.userAttack.yPosition = Tools.getInt(0, table.HEIGHT) - 1;

        return table.userAttack;

    }
}
