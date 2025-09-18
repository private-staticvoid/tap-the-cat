/*

TAP THE CAT
by:
Bermudez, Princess Glyza B.
Pagaoa, Achiles Troy T.
INF - 231
04/03/2024

GAME MECHANICS : 
This game is about feeding the cat and tapping the cat to make it angry and hungry!
1. Tap the cat 5 times it will decrease both the happines bar and huner bar by 25%
2. To restore the Happines and Hunger bar feed the cat with Cake, Tuna, Icecream
3. IF you feed the cat with veggies it will be disgusted and will reduce both  of the bar by 25%
4. exit button to end the game.

FEATURES :

1. Have a Menu and Exit Button
2. Progress bar (COLOUR CHANGING)
3. Cat Images that is according to the emotions
4. Background music

*/

package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

// Enum to represent different emotions ( attributes )
enum Emotion {
    HAPPY("Happy", "(=^ェ^=)"),
    ANNOYED("Annoyed", "(눈_눈)"),
    ANGRY("Angry", "(^._.^)ﾉ"),
    DISGUSTED("Disgusted", "(^｡>﹏<^)ﾉ"),
    HUNGRY("Hungry", "(^ ≧∇≦^)ﾉ");

    private String label;
    private String emoji;

    Emotion(String label, String emoji) {
        this.label = label;
        this.emoji = emoji;
    }

    public String getLabel() {
        return label;
    }

    public String getEmoji() {
        return emoji;
    }
}

// Class representing the Cat entity
class Cat {
    private String name;
    private int tapCount;
    private int feedingCount; // Count the number of times the cat is fed
    private int hungerLevel; // Track hunger level (0 to 4, representing 0% to 100%)
    private int happinessLevel; // Track happiness level (0 to 4, representing 0% to 100%)
    private Emotion emotion; // Track emotion

    // Constructor to initialize the Cat object
    public Cat(String name) {
        this.name = name;
        this.tapCount = 0;
        this.feedingCount = 0;
        this.hungerLevel = 4; // Initially set hunger to 100% so that you can tap the cat as soon as the game starts
        this.happinessLevel = 4; // Initially set happiness to 100%
        this.emotion = Emotion.HAPPY; // Initially set emotion to Happy
    }

    // Method to feed the cat (different food to choose from)
    public void feed(String food) {
        // Increase feeding count
        feedingCount++;
        // Reset tap count every 5 taps ( loop the tap so that you can play many as you want)
        if (feedingCount % 5 == 0) {
            tapCount = 0; // Reset tap count
        }
        // Update happiness and hunger based on food
        if (food.equals("cake") || food.equals("tuna") || food.equals("ice cream")) {
            increaseBarsBy25Percent();
            emotion = Emotion.HAPPY;
            JOptionPane.showMessageDialog(null, "Yey! I love this, it's my favorite.");
        } else if (food.equals("veggies")) {
            decreaseBarsBy25Percent();
            emotion = Emotion.DISGUSTED;
            JOptionPane.showMessageDialog(null, "Eww! I don't like veggies.");
        }
    }

    // Method to tap the cat
    public void tap() {
        // Increase tap count
        tapCount++;
        // Change emotion based on tap count
        if (tapCount == 5) {
            emotion = Emotion.ANNOYED;
            JOptionPane.showMessageDialog(null, "Stop tapping! The cat is annoyed.");
            decreaseBarsBy25Percent();
        } else if (tapCount == 10) {
            emotion = Emotion.ANGRY;
            JOptionPane.showMessageDialog(null, "Please stop tapping! The cat is angry.");
            decreaseBarsBy25Percent();
        } else if (tapCount == 15) {
            emotion = Emotion.HUNGRY;
            hungerLevel = 0;
            JOptionPane.showMessageDialog(null, "Please feed the cat. The cat is hungry.");
            decreaseBarsBy25Percent();
        }
    }

    // Method to increase both happiness and hunger bars by 25%
    private void increaseBarsBy25Percent() {
        if (hungerLevel < 4) {
            hungerLevel = Math.min(hungerLevel + 1, 4);
        }
        if (happinessLevel < 4) {
            happinessLevel = Math.min(happinessLevel + 1, 4);
        }
    }

    // Method to decrease both happiness and hunger bars by 25%
    private void decreaseBarsBy25Percent() {
        if (hungerLevel > 0) {
            hungerLevel = Math.max(hungerLevel - 1, 0);
        }
        if (happinessLevel > 0) {
            happinessLevel = Math.max(happinessLevel - 1, 0);
        }
    }

    // Getter for emotion level
    public Emotion getEmotion() {
        return emotion;
    }

    // Getter for hunger level
    public int getHungerLevel() {
        return hungerLevel;
    }

    // Getter for happiness level
    public int getHappinessLevel() {
        return happinessLevel;
    }
}

// Main class representing the game GUI
public class TapTheCat {
    private JFrame frame;
    private Cat cat;
    private JProgressBar hungerBar; // Hunger bar
    private JProgressBar happinessBar; // Happiness bar
    private JLabel emotionLabel; // Label to display cat's emotion
    private JLabel catLabel; // Label to display cat image
    private Clip musicClip; // Clip for background music

    // Constructor to initialize the game GUI
    public TapTheCat() {
        // Create a new Cat object
        cat = new Cat("Fluffy");

        // Create the main frame for the game
        frame = new JFrame("Tap The Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Adjusted size
        frame.getContentPane().setBackground(Color.BLACK);

        // Create a panel for the home page
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Tap The Cat");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36)); // Changed font to Comic Sans MS
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        homePanel.add(Box.createVerticalGlue());
        homePanel.add(titleLabel);
        homePanel.add(Box.createVerticalStrut(20));

        // Create a start button
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(200, 50)); // Increased button size
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGamePanel(); // Show the game panel when start button is clicked
            }
        });
        homePanel.add(startButton);
        homePanel.add(Box.createVerticalStrut(100));

        // Add the home panel to the frame
        frame.add(homePanel);
        frame.setVisible(true);
    }

    // Method to show the game panel
    private void showGamePanel() {
        frame.getContentPane().removeAll(); // Remove all components from the frame
        frame.getContentPane().setBackground(Color.BLACK);

    
        // Create a panel for the game elements
        JPanel gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a label for the cat image
        catLabel = new JLabel();
        updateCatImage(); // Set initial cat image
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gamePanel.add(catLabel, gbc);

 
        // Create hunger bar
        hungerBar = new JProgressBar(0, 4);
        hungerBar.setStringPainted(true);
        hungerBar.setString("Hunger");
        hungerBar.setPreferredSize(new Dimension(500, 30));
        hungerBar.setForeground(Color.GREEN);
        hungerBar.setBackground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gamePanel.add(hungerBar, gbc);

        // Create happiness bar
        happinessBar = new JProgressBar(0, 4);
        happinessBar.setStringPainted(true);
        happinessBar.setString("Happiness");
        happinessBar.setPreferredSize(new Dimension(500, 30));
        happinessBar.setForeground(Color.GREEN);
        happinessBar.setBackground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gamePanel.add(happinessBar, gbc);

        // Create a label for the cat's emotion
        emotionLabel = new JLabel("");
        emotionLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gamePanel.add(emotionLabel, gbc);

        // Create a button to tap the cat
        JButton tapButton = new JButton("Tap the Cat");
        tapButton.setPreferredSize(new Dimension(250, 50));
        tapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cat.tap();
                updateBars();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gamePanel.add(tapButton, gbc);

        // Create a button to feed the cat
        JButton feedButton = new JButton("Feed the Cat");
        feedButton.setPreferredSize(new Dimension(250, 50));
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] foodOptions = {"cake", "tuna", "veggies", "ice cream"};
                String selectedFood = (String) JOptionPane.showInputDialog(null, "Select Food", "Feed Options", JOptionPane.QUESTION_MESSAGE, null, foodOptions, foodOptions[0]);
                if (selectedFood != null) {
                    cat.feed(selectedFood);
                    updateBars();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gamePanel.add(feedButton, gbc);

        // Create an exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(250, 50));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the game when exit button is clicked
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gamePanel.add(exitButton, gbc);

        // Add the game panel to the frame
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();

        // Play background music
        playBackgroundMusic();
    }

    // Method to update the progress bars and emotion label
    private void updateBars() {
        hungerBar.setValue(cat.getHungerLevel());
        happinessBar.setValue(cat.getHappinessLevel());
        emotionLabel.setText(cat.getEmotion().getEmoji());
        updateCatImage();
        int hungerLevel = cat.getHungerLevel();
    int happinessLevel = cat.getHappinessLevel();
      // Set hunger bar color
    if (hungerLevel == 4) {
        hungerBar.setForeground(Color.GREEN);
    } else if (hungerLevel == 3) {
        hungerBar.setForeground(Color.YELLOW);
    } else if (hungerLevel == 2) {
        hungerBar.setForeground(Color.ORANGE);
    } else {
        hungerBar.setForeground(Color.RED);
    }

    // Set happiness bar color
    if (happinessLevel == 4) {
        happinessBar.setForeground(Color.GREEN);
    } else if (happinessLevel == 3) {
        happinessBar.setForeground(Color.YELLOW);
    } else if (happinessLevel == 2) {
        happinessBar.setForeground(Color.ORANGE);
    } else {
        happinessBar.setForeground(Color.RED);
    }
}

    // Method to update the cat image based on emotion
    private void updateCatImage() {
        String imagePath;
        switch (cat.getEmotion()) {
           case HAPPY:
                imagePath = "C:\\Users\\Aki\\Downloads\\Happy_cat.png";
                break;
            case ANNOYED:
                imagePath = "C:\\Users\\Aki\\Downloads\\Annoyed_cat.png";
                break;
            case ANGRY:
                imagePath = "C:\\Users\\Aki\\Downloads\\Angry_cat.png";
                break;
            case DISGUSTED:
                imagePath = "C:\\Users\\Aki\\Downloads\\disgusted.jpg";
                break;
            case HUNGRY:
                imagePath = "C:\\Users\\Aki\\Downloads\\Hungry_cat.png";
                break;
            default:
                imagePath = "C:\\Users\\Aki\\Downloads\\images.png";
                break;
        }
        ImageIcon catIcon = new ImageIcon(imagePath);
        catLabel.setIcon(catIcon);
    }

    // Method to play background music
    private void playBackgroundMusic() {
        try {
            String audioFilePath = "C:\\Users\\Aki\\Downloads\\meow.wav.crdownload"; // Specify the path to your audio file
            File audioFile = new File(audioFilePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + audioFilePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TapTheCat();
            }
        });
    }
}
