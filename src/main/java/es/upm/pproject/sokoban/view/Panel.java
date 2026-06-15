package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.models.*;
import es.upm.pproject.sokoban.core.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {

    private final int CELL_SIZE = 65;

    private Window window;
    private Board board;
    private Game game;


    public Panel(Window window) {
        this.window = window;
        this.setFocusable(true);

        game = new Game(window, this);
        this.addKeyListener(game);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If the board exists, it paints its cells
        if (board != null) {
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
                            if (cell.isGoal()) {
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
    }

    public void setBoard(Board newBoard) {
        board = newBoard;

        int heigthPanel = board.getHeigth() * CELL_SIZE;
        int widthPanel = board.getWidth() * CELL_SIZE;

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
