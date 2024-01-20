package Minesweeper;

/**
 * Holds data for a Box, such if it's hidden, if has a bomb, if it has been
 * flagged also we can keep track of the position without using any loop after
 * the table has been initialized.
 *
 * @author ivangarcia
 */
public class Box {

    public boolean hidden;
    public boolean hasBomb;
    public char symbol;
    public int positionX;
    public int positionY;
    public int bombsAround;
}
