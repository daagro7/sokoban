package es.upm.pproject.sokoban;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Game {
    
    static WarehouseMan wMan;
    static Board table;
    List<String> fileLines;
    ArrayList<Boxes> boxes = null;
    int totalSteps;


    public Game(int width, int height, List<String> lines) {
        table = new Board(width, height);
        boxes = new ArrayList<>();
        fileLines = lines;
    }


    public void initializeBoard() {
        Board.totalGoals = 0;
        Board.totalBoxes = 0;
        Board.stepsOnLevel = 0;

        int row = 0;
        for (String linea : fileLines.subList(2, fileLines.size())) {
            int column = 0;

            for (int j = 0; j < linea.length(); j++, column++) {
                char character = linea.charAt(j);
                // If it is the Warehouse man cell, we save the position
                if (character == 'W') {
                    wMan = new WarehouseMan(row, column);
                    Board.wMan = wMan;
                }
                // If it is a goal cell, we add it to the total of goals
                else if (character == '*') {
                    Board.totalGoals++;
                }
                // If it is a box cell, we add it to the total of boxes
                else if (character == '#') {
                    boxes.add(new Boxes(row, column));
                }
                Board.cellMap[row][column] = new Cells(character);
            }

            row++;
        }

        Board.totalBoxes = boxes.size();
    }

    public char addKeyboardInput(Scanner scanner) {
        String input = scanner.nextLine();
        return input.charAt(0);
    }

    public void keyboardInput(char input) {
        switch(input) {
            case 'w':
                // Move up
                table.canWarehouseManMove(wMan.getX() - 1, wMan.getY());
                break;
            case 's':
                // Move down
                table.canWarehouseManMove(wMan.getX() + 1, wMan.getY());
                break;
            case 'a':
                // Move left
                table.canWarehouseManMove(wMan.getX(), wMan.getY() - 1);
                break;
            case 'd':
                // Move right
                table.canWarehouseManMove(wMan.getX(), wMan.getY() + 1);
                break;
            case 'r':
                // Restart the level
                initializeBoard();
                System.out.println("-------------------------------");
                System.out.println("Level restarted.");
                break;
            default:
                System.out.println("-------------------------------");
                System.out.println("Invalid input. Please use w, a, s, d to move and r to restart.");
        }
    }

    public void gameLoop(int level) {
        Scanner scanner = new Scanner(System.in);
        Board.viewMap(level);
        while (Board.totalGoals != 0) {
            char input = addKeyboardInput(scanner);
            keyboardInput(input);
            System.out.println("-------------------------------");
            Board.viewMap(level);
        }
        System.out.println("Congratulations! You've completed the level.");
        scanner.close();
    }
}
