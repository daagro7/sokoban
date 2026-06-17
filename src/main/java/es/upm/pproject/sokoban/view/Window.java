package es.upm.pproject.sokoban.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

public class Window extends JFrame {

    private Panel panel;
    private JLabel text;
    private int levelPanel;
    private int stepsOnGame;
    private String levelName;

    public Window() {
        panel = new Panel(this);
        
        // Add the panel into the window
        this.add(panel);
        
        this.setTitle("Sokoban"); // Window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To close the window clicking 'X'
        this.setResizable(false); // The user cannot resize the window
        
        // Adapt the size of the window to the panel size and centers if
        this.pack();
        this.setLocationRelativeTo(null);
        
        // Add a border layout
        setLayout(new BorderLayout());

        // Create the text tag
        text = new JLabel(" ", JLabel.CENTER);
        text.setFont(new Font("Serif", Font.PLAIN, 18));

        add(text, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Make the window visible
        this.setVisible(true);
    }


    public void updateSteps(int newStep) {
        if (levelPanel > 0) {
            text.setText(levelName +
            " | Steps: " + newStep +
            " | Total steps: " + stepsOnGame);
        }
        else {
            text.setText(" ");
        }
    }

    public void newLevel(int newLevel) {
        levelPanel = newLevel;
        if (levelPanel > 0) {
            text.setText(levelName +
            " | Steps: 0" +
            " | Total steps: " + stepsOnGame);
        }
        else {
            text.setText(" ");
        }
    }

    public void closeApplication() {
        this.dispose();
    }

    // Getters/Setters
    public Panel getPanel() {
        return panel;
    }

    public int getLevelPanel() {
        return levelPanel;
    }

    public boolean isMenu(){
        return levelPanel <= 0;
    }

    public void setStepsOnGame(int steps) {
        this.stepsOnGame = steps;
    }

    public int getStepsOnGame() {
        return stepsOnGame;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }
}
