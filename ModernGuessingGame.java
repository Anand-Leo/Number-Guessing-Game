import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ModernGuessingGame extends JFrame {
    private int randomNumber;
    private int attemptsLeft = 8;
    private final int MAX_ATTEMPTS = 8;
    private int currentMin = 1;
    private int currentMax = 100;
    private JTextField guessField;
    private JLabel messageLabel, rangeLabel;
    private JButton guessButton, restartButton, hintButton;
    private Color bgColor = new Color(28, 35, 43);
    private Color accentColor = new Color(0, 175, 145);
    private JProgressBar progressBar;
    private JTextArea historyArea;

    // Game statistics
    private int totalGames = 0;
    private int totalWins = 0;
    private int totalLosses = 0;
    private double averageAttempts = 0.0;
    private JLabel statsLabel;

    public ModernGuessingGame() {
        setTitle("Smart Number Guesser");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeGame();
        createUI();
        getContentPane().setBackground(bgColor);
    }

    private void initializeGame() {
        randomNumber = new Random().nextInt(100) + 1;
        attemptsLeft = MAX_ATTEMPTS;
        currentMin = 1;
        currentMax = 100;
        System.out.println("Our Random Number is: "+randomNumber);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Left Panel (History)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(bgColor);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // History Panel
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setForeground(accentColor);  // Changed to match stats
        historyArea.setBackground(new Color(40, 40, 48));  // Same as stats panel
        historyArea.setMargin(new Insets(10, 15, 10, 15));
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(createTitledBorder("Guess History", accentColor, 20));  // Use accent color
        historyScroll.setPreferredSize(new Dimension(300, 300));  // Match stats panel size
        leftPanel.add(historyScroll, BorderLayout.CENTER);

// Add column headers
        // Change header initialization to:
        historyArea.setText(" Att │ Guess  │ Result\n");
        historyArea.append("═════╪════════╪═══════════\n"); // Use proper box-drawing characters

        // Right Panel (Stats)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(bgColor);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(createTitledBorder("Game Statistics", accentColor, 20));
        statsPanel.setBackground(new Color(40, 40, 48));

        statsLabel = new JLabel("<html>"
                + " Games Played: 0<br>"
                + " Wins: 0<br>"
                + " Losses: 0<br>"
                + " Avg. Attempts: 0.0</html>");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(accentColor);
        statsPanel.add(statsLabel);
        rightPanel.add(statsPanel, BorderLayout.CENTER);

        // Center Panel (Main Game)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("NUMBER DETECTIVE");
        titleLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);

        // Detective Icon
        try {
            ImageIcon originalIcon = new ImageIcon("detective.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel detectiveIcon = new JLabel(new ImageIcon(scaledImage));
            detectiveIcon.setHorizontalAlignment(JLabel.CENTER);
            gbc.gridy = 1;
            centerPanel.add(detectiveIcon, gbc);
        } catch (Exception e) {
            System.out.println("Image not found, continuing without icon");
        }

        // Game Controls
        JPanel gameControls = new JPanel(new GridLayout(0, 1, 10, 10));
        gameControls.setBackground(bgColor);

        // Progress Bar
        progressBar = new JProgressBar(0, MAX_ATTEMPTS);
        progressBar.setValue(attemptsLeft);
        progressBar.setString("Attempts: " + attemptsLeft);
        progressBar.setFont(new Font("Cooper Black", Font.PLAIN, 16));
        progressBar.setForeground(new Color(255, 87, 87));
        progressBar.setBackground(new Color(50, 50, 60));
        progressBar.setStringPainted(true);
        gameControls.add(progressBar);

        // Current Range
        rangeLabel = new JLabel("Possible Range: 1 - 100");
        rangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rangeLabel.setForeground(new Color(180, 180, 180));
        rangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameControls.add(rangeLabel);

        // Input Field
        guessField = new JTextField();
        guessField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setToolTipText("Enter number between 1-100");
        guessField.setPreferredSize(new Dimension(180, 45));
        guessField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        gameControls.add(guessField);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(bgColor);
        guessButton = new StyledButton("MAKE GUESS", accentColor);
        hintButton = new StyledButton("GET HINT", new Color(255, 184, 77));
        buttonPanel.add(guessButton);
        buttonPanel.add(hintButton);
        gameControls.add(buttonPanel);

        // Message Label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Cooper Black", Font.BOLD, 20));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameControls.add(messageLabel);

        gbc.gridy = 2;
        centerPanel.add(gameControls, gbc);

        // Restart Button (Centered at bottom)
        restartButton = new StyledButton("NEW GAME", new Color(255, 87, 87));
        restartButton.setVisible(false);
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        centerPanel.add(restartButton, gbc);

        // Add all panels to main layout
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Event Listeners
        guessButton.addActionListener(e -> checkGuess());
        hintButton.addActionListener(e -> showHint());
        restartButton.addActionListener(e -> restartGame());
        guessField.addActionListener(e -> checkGuess());

        add(mainPanel);
    }

    private TitledBorder createTitledBorder(String title, Color color, int fontSize) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, 2),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, fontSize),
                color
        );
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());

            if (guess < 1 || guess > 100) {
                showMessage("⚠️ Please enter between 1-100!", Color.ORANGE);
                return;
            }

            attemptsLeft--;
            guessField.setText("");
            rangeLabel.setText("Possible Range: " + currentMin + " - " + currentMax);
            progressBar.setValue(attemptsLeft);
            progressBar.setString("Attempts: " + attemptsLeft);

            // Add to history
            String historyEntry = String.format(" %03d │ %-6d │ %s\n",
                    MAX_ATTEMPTS - attemptsLeft,
                    guess,
                    guess < randomNumber ? "⬆ Too Low" : "⬇ Too High"
            );
            historyArea.append(historyEntry);

            if (guess == randomNumber) {
                showMessage("🎉 Perfect! Number found: " + randomNumber, new Color(76, 175, 80));
                endGame(true);
            } else if (attemptsLeft == 0) {
                showMessage("💥 Game Over! Correct number was: " + randomNumber, Color.RED);
                endGame(false);
            } else {
                updateRange(guess);
                String hint = guess < randomNumber ?
                        "⏫ Too Low! Try higher" : "⏬ Too High! Try lower";
                showMessage(hint, guess < randomNumber ?
                        new Color(33, 150, 243) : new Color(244, 67, 54));
            }

        } catch (NumberFormatException ex) {
            showMessage("⚠️ Numbers only please!", Color.ORANGE);
        }
    }

    private void updateRange(int guess) {
        if (guess < randomNumber) {
            currentMin = Math.max(currentMin, guess + 1);
        } else {
            currentMax = Math.min(currentMax, guess - 1);
        }
        rangeLabel.setText("Possible Range: " + currentMin + " - " + currentMax);
    }

    private void showHint() {
        int rangeMid = (currentMin + currentMax) / 2;
        String hint = "💡 Hint: The number is " +
                (randomNumber > rangeMid ? "greater than " + rangeMid :
                        "less than or equal to " + rangeMid);
        showMessage(hint, new Color(255, 184, 77));
    }

    private void showMessage(String text, Color color) {
        messageLabel.setForeground(color);
        messageLabel.setText(text);
    }

    private void endGame(boolean won) {
        // Update statistics
        totalGames++;
        if (won) totalWins++;
        else totalLosses++;

        averageAttempts = ((averageAttempts * (totalGames - 1)) +
                (MAX_ATTEMPTS - attemptsLeft)) / totalGames;

        updateStatsDisplay();

        getContentPane().setBackground(won ? new Color(46, 125, 50) : new Color(120, 40, 40));
        guessButton.setEnabled(false);
        hintButton.setEnabled(false);
        guessField.setEnabled(false);
        restartButton.setVisible(true);
        if (won) rangeLabel.setVisible(false);
    }

    private void updateStatsDisplay() {
        statsLabel.setText("<html>"
                + "Games Played: " + totalGames + "<br>"
                + "Wins: " + totalWins + "<br>"
                + "Losses: " + totalLosses + "<br>"
                + "Avg. Attempts: " + String.format("%.1f", averageAttempts) + "</html>");
    }

    private void restartGame() {
        initializeGame();
        guessButton.setEnabled(true);
        hintButton.setEnabled(true);
        guessField.setEnabled(true);
        restartButton.setVisible(false);
        rangeLabel.setVisible(true);
        messageLabel.setText(" ");
        progressBar.setValue(MAX_ATTEMPTS);
        progressBar.setString("Attempts: " + MAX_ATTEMPTS);
        rangeLabel.setText("Possible Range: 1 - 100");
        guessField.setText("");
        historyArea.setText("");
        getContentPane().setBackground(bgColor);
    }

    // Custom Styled Button
    class StyledButton extends JButton {
        public StyledButton(String text, Color bgColor) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setBackground(bgColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.brighter());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
                public void mousePressed(MouseEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.WHITE, 1),
                            BorderFactory.createEmptyBorder(9, 24, 9, 24)
                    ));
                }
                public void mouseReleased(MouseEvent e) {
                    setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            ModernGuessingGame game = new ModernGuessingGame();
            game.setVisible(true);
        });
    }
}