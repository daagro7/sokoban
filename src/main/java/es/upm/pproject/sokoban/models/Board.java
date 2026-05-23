package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

public class Board {
    Cell[][] cellMap;

    public Board(int width, int height) {
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
        String result = "";
        for (int row = 0; row < cellMap.length; row++) {
            for (int column = 0; column < cellMap[row].length; column++) {
                result += cellMap[row][column].toString();
            }

            result += "\n";
        }

        return result;
    }
}
