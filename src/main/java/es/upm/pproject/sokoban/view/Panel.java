package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.models.*;
import es.upm.pproject.sokoban.core.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {

    private final int CELL_SIZE = 65;

    private Window window;
    private Board board;
    private Game game;

    private int selectedOption;
    private final String sokobanString = "SOKOBAN";
    private final String MenuString = "MENU";
    private final String[] menuOptions = {"NEW GAME", "LOAD GAME", "CLOSE SOKOBAN"};

    public Panel(Window window) {
        this.window = window;
        this.setFocusable(true);

        game = new Game(window, this);
        this.addKeyListener(game);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);

        if (window.getLevel() == 0) { // Starting menu
            drawMenu(g);
        }
        else if (window.getLevel() > 0) { // Level board
            drawBoard(g);
        }
    }

    public void drawBoard(Graphics g) {
        for (int heigth = 0; heigth < board.getHeigth(); heigth++) {
            for (int width = 0; width < board.getWidth(); width++) {
                
                int x = width * CELL_SIZE;
                int y = heigth * CELL_SIZE;

                Cell cell = board.getCell(width, heigth);

                switch (cell.getCellType()) {
                    case WALL: // Wall
                        g.setColor(Color.GRAY);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        break;
                    case BOX: // Box
                        if (cell.isGoal()) { // Efect if box is in goal position
                            g.setColor(new Color(210, 145, 60));
                        } else {
                            g.setColor(new Color(139, 69, 19));
                        }
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        break;
                    case GOAL_POSITION: // Goal
                        g.setColor(Color.RED);
                        g.fillOval(x + CELL_SIZE/4, y + CELL_SIZE/4, CELL_SIZE/2, CELL_SIZE/2);
                        break;
                    case WAREHOUSE_MAN: // Player
                        g.setColor(Color.BLUE);
                        g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                        break;
                    default:
                        break;
                }
                
                // Draw the lines that separates every cell
                g.setColor(new Color(50, 50, 50));
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }
    
    public void drawMenu(Graphics g) {
        // Menu title
        // Center the text on the option box
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 32));
        FontMetrics metrics = g.getFontMetrics(g.getFontMetrics().getFont());
        int textWidth = metrics.stringWidth(sokobanString);
        int centeredX = (this.getWidth() - textWidth) / 2;
        
        g.drawString(sokobanString, centeredX, 80);

        // Boxes configuration
        int boxWidth = 220;
        int boxHeight = 50;
        int startX = (this.getWidth() - boxWidth) / 2;
        int spacing = 30;
        int totalMenuHeight = (menuOptions.length * boxHeight) + ((menuOptions.length - 1) * spacing);
        int startY = (this.getHeight() - totalMenuHeight) / 2;

        g.setFont(new Font("SansSerif", Font.BOLD, 18));

        for (int i = 0; i < menuOptions.length; i++) {
            int currentY = startY + i * (boxHeight + spacing);

            // Efect of the selected option to the boxes
            if (i == selectedOption) {
                g.setColor(new Color(46, 139, 87));
            } else {
                g.setColor(new Color(70, 70, 70));
            }
            g.fillRect(startX, currentY, boxWidth, boxHeight);
        
            // Center the text on the option box
            metrics = g.getFontMetrics(g.getFontMetrics().getFont());
            textWidth = metrics.stringWidth(menuOptions[i]);
            centeredX = (this.getWidth() - textWidth) / 2;

            // Draw the text on the panel
            g.setColor(Color.WHITE);
            g.drawString(menuOptions[i], centeredX, currentY + 32);
        }
    }

    public int getSelectedOption() { return this.selectedOption; }
    public void setSelectedOption(int option) { this.selectedOption = option; }
    public int getNumberOptions() { return menuOptions.length; }

    public void setBoard(Board newBoard) {
        int heigthPanel;
        int widthPanel;

        if (window.getLevel() <= 0) { // Menus panel
            heigthPanel = 10 * CELL_SIZE;
            widthPanel = 10 * CELL_SIZE;
        }
        else { // Board panel
            // Added new board
            board = newBoard;

            // Dimensions for the panel
            heigthPanel = board.getHeigth() * CELL_SIZE;
            widthPanel = board.getWidth() * CELL_SIZE;
        }

        setPreferredSize(new Dimension(widthPanel, heigthPanel));
        // If the panels is in a JFrame, it adaps the size and centers the window
        if (this.getTopLevelAncestor() instanceof JFrame) {
            ((JFrame) this.getTopLevelAncestor()).pack();
            ((JFrame) this.getTopLevelAncestor()).setLocationRelativeTo(null);
        }

        // Repaint the board
        this.revalidate();
        this.repaint();
    }

    public Game getGame() {
        return this.game;
    }

    public Window getWindow() {
        return this.window;
    }
}
