import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;

public class Game extends JPanel {

    private int tileSize = 25;
    int screenWidth = 500;
    int screenHeight = 800;
    private int[] spawnerPosition;
    private int MasterSpawners = 2;
    private int playerX;
    private int playerY;
    boolean gameOver = false;

    private HighScore highScore;

    private long startTime;

    public Game() {
        // Initialize spawner positions
        spawnerPosition = new int[screenHeight/tileSize];
        startTime = System.currentTimeMillis();

        // Set block path to start in the middle of the screen
        int pathWidth = 6;
        int pathStart = (screenWidth/tileSize) / 2;
        for (int i = 0; i < screenHeight/tileSize; i++) {
            if (i == screenHeight/tileSize-1) {
                for (int j = pathStart; j < pathStart + pathWidth; j++) {
                    spawnerPosition[i] = j;
                    i++; // only increment i in this loop
                    if (i >= screenHeight/tileSize) {
                        break; // break out of loop if end of array is reached
                    }
                }
            } else {
                spawnerPosition[i] = MasterSpawners;
            }
        }

        // Initialize player position in the middle of the block path
        playerX = 5;
        playerY = 25;

        setFocusable(true);

        // Set up KeyBindings for player movement
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerX > 0) {
                    playerX--;
                    repaint();
                }
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerX < screenWidth/tileSize-1) {
                    playerX++;
                    repaint();
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Calculate elapsed time
        long elapsed = System.currentTimeMillis() - startTime;
        // Draw elapsed time on the screen
        g.setColor(Color.WHITE);
        g.drawString(String.format("Time: %d sec", elapsed / 1000), 10, 20);
        // Draw game board
        for (int i = 0; i < screenWidth/tileSize; i++) {
            for (int j = 0; j < screenHeight/tileSize; j++) {
                // color you can collide with
                if (i < spawnerPosition[j] || i > spawnerPosition[j] + 5) {
                    g.setColor(new Color(40,40,40));
                } else {
                    g.setColor(new Color(200,200,200));
                }
                g.fillRect(i*tileSize,j*tileSize,tileSize,tileSize);
                // Draw player
                if (i == playerX && j == playerY) {
                    g.setColor(new Color(128,255,128)); // set color to green
                    g.fillRect(playerX*tileSize,playerY*tileSize, tileSize,tileSize);
                }
            }
        }
    }

    public void moveSpawners() {
        int move = (int)(Math.random()*3)-1;

        for (int i = screenHeight/tileSize-1; i > 0; i--) {
            spawnerPosition[i] = spawnerPosition[i-1];
        }
        spawnerPosition[0] += move;
        if (spawnerPosition[0] < MasterSpawners) {
            spawnerPosition[0] = MasterSpawners;
        }
        if (spawnerPosition[0] > screenWidth/tileSize-6) {
            spawnerPosition[0] = screenWidth/tileSize-6;
        }

        checkCollision();
    }

    public void checkCollision() {
        if (this.highScore == null) {
            this.highScore = new HighScore();
        }
        if (playerY >= screenHeight/tileSize-1) {
            gameOver = true;
        } else if (playerY >= 0) {
            if (playerX < spawnerPosition[playerY] || playerX > spawnerPosition[playerY] + 5) {
                gameOver = true;
            }
        }
        if (gameOver) {
            // Calculate elapsed time
            long elapsed = System.currentTimeMillis() - startTime;
            // Prompt the user for an option to save their score
            int option = JOptionPane.showOptionDialog(this, String.format("Game over! Elapsed time: %d sec. Do you want to save your score?", elapsed / 1000), "Save Score", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == JOptionPane.YES_OPTION) {
                // Prompt the user for their name and add their score to the high score list
                String name = JOptionPane.showInputDialog(this, "Enter your name:");
                highScore.addScore(new Score(name, elapsed/1000));
            }
            System.exit(0);
        }
    }
}


