package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

/**
 * Represents the game board in Sokoban as a grid of cells.
 */
public class Board {
    Cell[][] cellMap;
    private int heigth;
    private int width;

    /**
     * Creates a Board with the specified width and height.
     *
     * @param width  width of the board
     * @param height height of the board
     */
    public Board(int width, int height) {
        this.heigth = height;
        this.width = width;
        cellMap = new Cell[height][width];
    }

    /**
     * Returns the cell at the specified coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     *
     * @return Cell at the specified coordinates
     */
    public Cell getCell(int x, int y) {
        return cellMap[y][x];
    }

    /**
     * Change the cell at the specified coordinates to a new cell of the new type.
     *
     * @param x        x coordinate
     * @param y        y coordinate
     * @param cellType type of the new cell
     */
    public void setCell(int x, int y, CellType cellType) {
        cellMap[y][x] = new Cell(cellType);
    }

    /**
     * Updates the type of the cell at the given coordinates to the given type.
     *
     * @param x        x coordinate
     * @param y        y coordinate
     * @param cellType new type for the cell
     */
    public void setCellType(int x, int y, CellType cellType) {
        cellMap[y][x].updateType(cellType);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < cellMap.length; row++) {
            for (int column = 0; column < cellMap[row].length; column++) {
                result.append(cellMap[row][column]);
            }

            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Returns the height of the board.
     *
     * @return height of the board
     */
    public int getHeigth() {
        return heigth;
    }

    /**
     * Returns the width of the board.
     *
     * @return width of the board
     */
    public int getWidth() {
        return width;
    }
}
