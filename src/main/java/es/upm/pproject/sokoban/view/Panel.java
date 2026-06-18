package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.models.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {

    private static final int CELL_SIZE = 65;

    private Window window;
    private transient Board board; // Last board painted
    private boolean completed;

    private int selectedOption;
    private static final String FONTTYPE = "SansSerif";
    private static final String SOKOBANSTRING = "SOKOBAN";
    private static final String[] STARTINGMENUOPTIONS = { "NEW GAME", "LOAD GAME", "CLOSE SOKOBAN" };
    private static final String MENUSTRING = "MENU";
    private static final String[] PAUSEMENUSTRING = { "RESUME", "SAVE GAME", "RESTART LEVEL", "UNDO MOVEMENT", "QUIT" };

    /**
     * Constructs a Panel for the Sokoban game.
     *
     * @param window window of the game
     */
    public Panel(Window window) {
        this.window = window;
        this.setFocusable(true);
        completed = false;
    }

    /**
     * Draws a component based on the level panel
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);

        if (window.getLevelPanel() == 0) { // Starting menu
            drawStartingMenu(g);
        } else if (window.getLevelPanel() == -1) { // Level menu
            drawPauseMenu(g);
        } else if (window.getLevelPanel() == -2) { // New Level/Congratulations
            drawNewLevelPanel(g);
        } else if (window.getLevelPanel() > 0) { // Level board
            drawBoard(g);
        }
    }

    /**
     * Draws the game board with the game elements.
     *
     * @param g graphics context
     */
    public void drawBoard(Graphics g) {
        for (int heigth = 0; heigth < board.getHeigth(); heigth++) {
            for (int width = 0; width < board.getWidth(); width++) {

                int x = width * CELL_SIZE;
                int y = heigth * CELL_SIZE;

                Cell cell = board.getCell(width, heigth);

                switch (cell.getCellType()) {
                    case WALL: // Wall
                        g.setColor(Color.DARK_GRAY);
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
                        g.fillOval(x + CELL_SIZE / 4, y + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);
                        break;
                    case WAREHOUSE_MAN: // Player
                        g.setColor(Color.BLUE);
                        g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                        break;
                    default:
                        break;
                }

                if (cell.getCellType() != Cell.CellType.WALL) {
                    // Draw the lines that separates every cell
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * Draws the starting menu with its options.
     *
     * @param g graphics context
     */
    public void drawStartingMenu(Graphics g) {
        // Menu title
        // Center the text on the option box
        g.setColor(Color.BLACK);
        g.setFont(new Font(FONTTYPE, Font.BOLD, 32));
        FontMetrics metrics = g.getFontMetrics(g.getFontMetrics().getFont());
        int textWidth = metrics.stringWidth(SOKOBANSTRING);
        int centeredX = (this.getWidth() - textWidth) / 2;

        g.drawString(SOKOBANSTRING, centeredX, 80);

        // Boxes configuration
        int boxWidth = 220;
        int boxHeight = 50;
        int startX = (this.getWidth() - boxWidth) / 2;
        int spacing = 30;
        int totalMenuHeight = (STARTINGMENUOPTIONS.length * boxHeight) + ((STARTINGMENUOPTIONS.length - 1) * spacing);
        int startY = (this.getHeight() - totalMenuHeight) / 2;

        g.setFont(new Font(FONTTYPE, Font.BOLD, 18));

        for (int i = 0; i < STARTINGMENUOPTIONS.length; i++) {
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
            textWidth = metrics.stringWidth(STARTINGMENUOPTIONS[i]);
            centeredX = (this.getWidth() - textWidth) / 2;

            // Draw the text on the panel
            g.setColor(Color.WHITE);
            g.drawString(STARTINGMENUOPTIONS[i], centeredX, currentY + 32);
        }
    }

    /**
     * Draws the pause menu with its options.
     *
     * @param g graphics context
     */
    public void drawPauseMenu(Graphics g) {
        // Menu title
        // Center the text on the option box
        g.setColor(Color.BLACK);
        g.setFont(new Font(FONTTYPE, Font.BOLD, 32));
        FontMetrics metrics = g.getFontMetrics(g.getFontMetrics().getFont());
        int textWidth = metrics.stringWidth(MENUSTRING);
        int centeredX = (this.getWidth() - textWidth) / 2;

        g.drawString(MENUSTRING, centeredX, 80);

        // Boxes configuration
        int boxWidth = 220;
        int boxHeight = 50;
        int startX = (this.getWidth() - boxWidth) / 2;
        int spacing = 20;
        int totalMenuHeight = (PAUSEMENUSTRING.length * boxHeight) + ((PAUSEMENUSTRING.length - 1) * spacing);
        int startY = (this.getHeight() - totalMenuHeight) / 2;

        g.setFont(new Font(FONTTYPE, Font.BOLD, 18));

        for (int i = 0; i < PAUSEMENUSTRING.length; i++) {
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
            textWidth = metrics.stringWidth(PAUSEMENUSTRING[i]);
            centeredX = (this.getWidth() - textWidth) / 2;

            // Draw the text on the panel
            g.setColor(Color.WHITE);
            g.drawString(PAUSEMENUSTRING[i], centeredX, currentY + 32);
        }
    }

    /**
     * Draws the new level or final congratulations panel.
     *
     * @param g graphics context
     */
    public void drawNewLevelPanel(Graphics g) {
        // Center the text
        g.setColor(Color.BLACK);
        g.setFont(new Font(FONTTYPE, Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics(g.getFontMetrics().getFont());
        String text = "";
        String text1 = "";
        String text2 = "";
        if (this.completed) {
            text = "Congratulations!";
            text1 = "You've completed all the levels in the game";
            text2 = "in " + window.getStepsOnGame() + " steps";
        } else {
            text = window.getLevelName();
            text1 = "";
            text2 = "";
        }
        String textPress = "<<<PRESS ENTER/SPACE>>>";

        int lineHeight = metrics.getHeight();
        int spacing = 25;
        int extraSeparator = 50;

        int totalBlockHeight = (4 * lineHeight) + (2 * spacing) + extraSeparator;
        int startY = (this.getHeight() - totalBlockHeight) / 2 - 50;

        // First text
        int currentY = startY + (lineHeight + spacing);
        int textWidth = metrics.stringWidth(text);
        int centeredX = (this.getWidth() - textWidth) / 2;
        g.drawString(text, centeredX, currentY);

        if (this.completed) {
            // Second text
            currentY = startY + 2 * (lineHeight + spacing);
            textWidth = metrics.stringWidth(text1);
            centeredX = (this.getWidth() - textWidth) / 2;
            g.drawString(text1, centeredX, currentY);

            // Third text
            currentY = startY + 3 * (lineHeight + spacing);
            textWidth = metrics.stringWidth(text2);
            centeredX = (this.getWidth() - textWidth) / 2;
            g.drawString(text2, centeredX, currentY);

            currentY += extraSeparator;
        } else {
            currentY = startY + lineHeight + spacing + extraSeparator;
        }

        // Press enter/space text
        textWidth = metrics.stringWidth(textPress);
        centeredX = (this.getWidth() - textWidth) / 2;
        g.drawString(textPress, centeredX, currentY);
    }

    // Getters/Setters

    /**
     * Sets the game as completed or not completed.
     *
     * @param completed true if completed, false if not
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Sets the panel to show the board or menu.
     *
     * @param newBoard board to display, null for menus
     */

    public void setPanel(Board newBoard) {
        int heigthPanel;
        int widthPanel;

        if (window.getLevelPanel() <= 0) { // Menus panel size
            heigthPanel = 10 * CELL_SIZE;
            widthPanel = 10 * CELL_SIZE;
        } else { // Board panel size
                 // Added new board
            board = newBoard;

            // Dimensions for the panel
            heigthPanel = board.getHeigth() * CELL_SIZE;
            widthPanel = board.getWidth() * CELL_SIZE;
        }

        setPreferredSize(new Dimension(widthPanel, heigthPanel));
        // If the panels is in a JFrame, it adaps the size and centers the window
        if (this.getTopLevelAncestor() instanceof JFrame jframe) {
            jframe.pack();
            jframe.setLocationRelativeTo(null);
        }

        // Repaint the board
        this.revalidate();
        this.repaint();
    }

    /**
     * Returns the menu current selected option.
     *
     * @return current selected option index
     */
    public int getSelectedOption() {
        return this.selectedOption;
    }

    /**
     * Sets the menu current selected option.
     *
     * @param option index of the option to select
     */
    public void setSelectedOption(int option) {
        this.selectedOption = option;
    }

    /**
     * Returns the number of options in the current menu.
     *
     * @return number of options
     */
    public int getNumberOptions() {
        return window.getLevelPanel() == 0 ? STARTINGMENUOPTIONS.length : PAUSEMENUSTRING.length;
    }
}
