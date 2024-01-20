package Minesweeper;

/**
 * Gametable register This hold information for the game table, it contains one
 * array of Box type for the system internal table used to know where is bombs
 * located
 *
 *
 *
 * @author ivangarcia
 */
public class Gametable {

    public Box[][] playerTable;
    public int uncoverNumbers;
    public int totalBombs;
    public int WIDTH;
    public int HEIGHT;
    public boolean cheat;
    public boolean exit;
    public UserAttack userAttack;
}
