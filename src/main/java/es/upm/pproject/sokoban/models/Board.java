package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

public class Board {
    Cell[][] cellMap;

    public Board(int width, int height) {
        cellMap = new Cell[width][height];
    }

    // This method returns the type of cell at the given position
    public Cell getCell(int row, int column) {
        return cellMap[row][column];
    }

    public void setCell(int row, int column, CellType cellType) {
        cellMap[row][column] = new Cell(cellType);
    }

    public void setCellType(int row, int column, CellType cellType) {
        cellMap[row][column].cellType = updateType(cellType);
    }
}
