import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartScreen extends JPanel {

    // Declare instance variables
    private JFrame frame;
    private HighScore highScore;
    private JButton clearButton;
    private JButton startButton;

    // Constructor
    public StartScreen() {
        // Initialize JFrame
        frame = new JFrame("Block Ascension");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 800));
        frame.setResizable(false);

        // Initialize Clear Scores button
        clearButton = new JButton("Clear Scores");
        clearButton.setBounds(200, 300, 100, 50);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear scores when button is clicked
                highScore.clearScores();
                repaint();
            }
        });
        add(clearButton);

        // Initialize Start Game button
        startButton = new JButton("Start Game");
        startButton.setBounds(200, 200, 100, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start game when button is clicked
                Game game = new Game();
                frame.getContentPane().removeAll();
                frame.getContentPane().add(game);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Start the game loop in a separate thread
                Thread gameThread = new Thread(new Runnable() {
                    int speed = 100;
                    int counter = 0;
                    public void run() {
                        while (!game.gameOver) {
                            game.moveSpawners();
                            game.checkCollision();
                            game.repaint();
                            try {
                                Thread.sleep(speed);
                            } catch (InterruptedException r) {
                                r.printStackTrace();
                            }
                            counter+=10;
                            if (counter % 100 == 0) {
                                double rand = Math.random();
                                if (rand < 0.2) {
                                    speed++;
                                }else {
                                    speed--;
                                }
                            }
                        }
                    }
                });
                gameThread.start();
            }
        });
        add(startButton);

        // Add panel to JFrame and show it
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.highScore == null) {
            this.highScore = new HighScore();
        }
        super.paintComponent(g);

        // Draw title text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Block Ascension", 100, 100);

        // Draw high scores
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("High Scores:", 100, 200);

        // Retrieve scores and display them up to the top 5
        ArrayList<Score> scores = highScore.getScores();
        int numScoresToDisplay = Math.min(5, scores.size());
        for (int i = 0; i < numScoresToDisplay; i++) {
            Score score = scores.get(i);
            long timeInSeconds = score.getTime();
            g.drawString((i+1) + ". " + score.getName() + ": " + timeInSeconds + "s", 100, 250 + i*50);
        }
    }
}