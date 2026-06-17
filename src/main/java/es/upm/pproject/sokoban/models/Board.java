package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

public class Board {
    Cell[][] cellMap;
    private int heigth;
    private int width;

    public Board(int width, int height) {
        this.heigth = height;
        this.width = width;
        cellMap = new Cell[height][width];
    }

    // This method returns the type of cell at the given position
    public Cell getCell(int x, int y) {
        return cellMap[y][x];
    }

    public void setCell(int x, int y, CellType cellType) {
        cellMap[y][x] = new Cell(cellType);
    }

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

    public int getHeigth() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }
}
