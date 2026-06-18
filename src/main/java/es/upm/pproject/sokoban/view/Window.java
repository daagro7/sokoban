package es.upm.pproject.sokoban.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Main window of the Sokoban game
 */
public class Window extends JFrame {

    private Panel panel;
    private JLabel text;
    private int levelPanel;
    private int stepsOnGame;
    private String levelName;

    /**
     * Constructs a Window for the Sokoban game.
     */
    public Window() {
        panel = new Panel(this);

        // Add the panel into the window
        this.add(panel);

        this.setTitle("Sokoban"); // Window title
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // To close the window clicking 'X'
        this.setResizable(false); // The user cannot resize the window

        // Adapt the size of the window to the panel size and centers
        this.pack();
        this.setLocationRelativeTo(null);

        // Add a border layout
        setLayout(new BorderLayout());

        // Create the text tag
        text = new JLabel(" ", SwingConstants.CENTER);
        text.setFont(new Font("Serif", Font.PLAIN, 18));

        add(text, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Make the window visible
        this.setVisible(true);
    }

    /**
     * Updates the text in the window with the game information
     *
     * @param newStep number of steps in the level
     */
    public void updateSteps(int newStep) {
        if (levelPanel > 0) {
            text.setText(levelName +
                    " | Steps: " + newStep +
                    " | Total steps: " + stepsOnGame);
        } else {
            text.setText(" ");
        }
    }

    /**
     * Updates the text in the window with the new level starting data
     *
     * @param newLevel number of the new level
     */
    public void newLevel(int newLevel) {
        levelPanel = newLevel;
        if (levelPanel > 0) {
            text.setText(levelName +
                    " | Steps: 0" +
                    " | Total steps: " + stepsOnGame);
        } else {
            text.setText(" ");
        }
    }

    /**
     * Closes the application window.
     */
    public void closeApplication() {
        this.dispose();
    }

    // Getters/Setters

    /**
     * Returns the panel
     *
     * @return panel
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Returns the current level panel number
     *
     * @return level panel number
     */
    public int getLevelPanel() {
        return levelPanel;
    }

    /**
     * Checks if the current panel is a menu panel
     *
     * @return true if it's a menu, false if not
     */
    public boolean isMenu() {
        return levelPanel <= 0;
    }

    /**
     * Sets the number of steps in the game
     *
     * @param steps number of steps
     */
    public void setStepsOnGame(int steps) {
        this.stepsOnGame = steps;
    }

    /**
     * Returns the number of steps in the game
     *
     * @return number of steps
     */
    public int getStepsOnGame() {
        return stepsOnGame;
    }

    /**
     * Sets the name of the current level
     *
     * @param levelName name of the level
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Returns the name of the current level
     *
     * @return name of the level
     */
    public String getLevelName() {
        return levelName;
    }
}
