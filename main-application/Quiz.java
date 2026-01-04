import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Quiz extends JFrame {
    private UserProfile userProfile;
    private String domain;
    private String difficulty;
    private QuestionBank questionBank;
    private int questionCount;
    private int timePerQuestion;
    private boolean reviewAnswers;
    private boolean showExplanations;
    private boolean soundEffects;
    
    private List<Question> questions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private long startTime;
    private long questionStartTime;
    private Timer questionTimer;
    private int timeRemaining;
    private int totalScore;
    
   
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JPanel optionsPanel;
    private JButton[] optionButtons;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JProgressBar progressBar;
    private JButton nextButton;
    
    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_IDLE = new Color(52, 152, 219);
    private static final Color BUTTON_HOVER = new Color(41, 128, 185);
    
    public Quiz(UserProfile userProfile, String domain, String difficulty,
               QuestionBank questionBank, int questionCount, int timePerQuestion,
               boolean reviewAnswers, boolean showExplanations, boolean soundEffects) {
        
        this.userProfile = userProfile;
        this.domain = domain;
        this.difficulty = difficulty;
        this.questionBank = questionBank;
        this.questionCount = questionCount;
        this.timePerQuestion = timePerQuestion;
        this.reviewAnswers = reviewAnswers;
        this.showExplanations = showExplanations;
        this.soundEffects = soundEffects;
        
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.totalScore = 0;
        
        this.questions = questionBank.getQuestionsForDomain(domain, difficulty, questionCount);
        
        setTitle("Quiz - " + domain + " (" + difficulty + ")");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with stats
        JPanel headerPanel = createHeaderPanel();
        
        // Question card
        JPanel questionCard = createQuestionCard();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(questionCard, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Progress bar
        progressBar = new JProgressBar(0, questionCount);
        progressBar.setValue(1);
        progressBar.setStringPainted(true);
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setBackground(Color.WHITE);
        progressBar.setBorder(new LineBorder(PRIMARY_COLOR, 2, true));
        progressBar.setPreferredSize(new Dimension(0, 30));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        
        questionNumberLabel = createStatCard(" Question", "1 of " + questionCount, PRIMARY_COLOR);
        scoreLabel = createStatCard(" Score", "0", SUCCESS_COLOR);
        timerLabel = createStatCard(" Time", timePerQuestion + "s", WARNING_COLOR);
        
        statsPanel.add(questionNumberLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(timerLabel);
        
        panel.add(progressBar, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createStatCard(String title, String value, Color color) {
        JLabel card = new JLabel("<html><center><b>" + title + "</b><br><font size='5'>" + 
                                 value + "</font></center></html>");
        card.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.setForeground(color);
        card.setOpaque(true);
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        card.setHorizontalAlignment(JLabel.CENTER);
        return card;
    }
    
    private JPanel createQuestionCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 2, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Question text
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        questionTextLabel.setForeground(TEXT_COLOR);
        JScrollPane questionScroll = new JScrollPane(questionTextLabel);
        questionScroll.setBorder(null);
        questionScroll.setBackground(CARD_COLOR);
        
        // Options panel
        optionsPanel = new JPanel(new GridLayout(0, 1, 0, 12));
        optionsPanel.setBackground(CARD_COLOR);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        optionButtons = new JButton[6];
        for (int i = 0; i < optionButtons.length; i++) {
            final int optionIndex = i;
            optionButtons[i] = createOptionButton();
            optionButtons[i].addActionListener(e -> selectAnswer(optionIndex));
            optionsPanel.add(optionButtons[i]);
        }
        
        JScrollPane optionsScroll = new JScrollPane(optionsPanel);
        optionsScroll.setBorder(null);
        optionsScroll.setBackground(CARD_COLOR);
        
        card.add(questionScroll, BorderLayout.NORTH);
        card.add(optionsScroll, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createOptionButton() {
        JButton btn = new JButton();
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(BUTTON_IDLE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BUTTON_IDLE, 2, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled() && btn.getBackground() == BUTTON_IDLE) {
                    btn.setBackground(BUTTON_HOVER);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled() && btn.getBackground() == BUTTON_HOVER) {
                    btn.setBackground(BUTTON_IDLE);
                }
            }
        });
        
        return btn;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(BACKGROUND_COLOR);
        
        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nextButton.setBackground(PRIMARY_COLOR);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setPreferredSize(new Dimension(180, 45));
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setEnabled(false);
        
        nextButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (nextButton.isEnabled()) {
                    nextButton.setBackground(PRIMARY_COLOR.darker());
                }
            }
            public void mouseExited(MouseEvent e) {
                if (nextButton.isEnabled()) {
                    nextButton.setBackground(PRIMARY_COLOR);
                }
            }
        });
        
        nextButton.addActionListener(e -> nextQuestion());
        panel.add(nextButton);
        
        return panel;
    }
    
    public void startQuiz() {
        setVisible(true);
        startTime = System.currentTimeMillis();
        displayCurrentQuestion();
    }
    
    private void displayCurrentQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        // Update question number
        questionNumberLabel.setText("<html><center><b>Question</b><br><font size='5'>" + 
                                    (currentQuestionIndex + 1) + " of " + questionCount + 
                                    "</font></center></html>");
        
        // Update progress bar
        progressBar.setValue(currentQuestionIndex + 1);
        progressBar.setString("Progress: " + (currentQuestionIndex + 1) + " / " + questionCount);
        
        // Update question text
        questionTextLabel.setText("<html><body style='width: 750px; padding: 10px;'>" + 
                                 currentQuestion.getText() + "</body></html>");
        
        // Update option buttons
        List<String> options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                optionButtons[i].setText("<html><body style='width: 650px;'>" + 
                                        (char)('A' + i) + ".  " + options.get(i) + 
                                        "</body></html>");
                optionButtons[i].setEnabled(true);
                optionButtons[i].setBackground(BUTTON_IDLE);
                optionButtons[i].setForeground(Color.WHITE);
                optionButtons[i].setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BUTTON_IDLE, 2, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }
        
        nextButton.setEnabled(false);
        startQuestionTimer();
    }
    
    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }
        
        timeRemaining = timePerQuestion;
        updateTimerDisplay();
        questionStartTime = System.currentTimeMillis();
        
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                SwingUtilities.invokeLater(() -> {
                    updateTimerDisplay();
                    
                    if (timeRemaining <= 0) {
                        cancel();
                        SwingUtilities.invokeLater(() -> timeUp());
                    }
                });
            }
        }, 1000, 1000);
    }
    
    private void updateTimerDisplay() {
        Color timerColor;
        if (timeRemaining <= 5) {
            timerColor = ERROR_COLOR;
        } else if (timeRemaining <= 10) {
            timerColor = WARNING_COLOR;
        } else {
            timerColor = SUCCESS_COLOR;
        }
        
        timerLabel.setText("<html><center><b>Time</b><br><font size='5'>" + 
                          timeRemaining + "s</font></center></html>");
        timerLabel.setForeground(timerColor);
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(timerColor, 2, true),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
    }
    
    private void timeUp() {
        for (JButton button : optionButtons) {
            if (button.isVisible()) {
                button.setEnabled(false);
            }
        }
        
        if (reviewAnswers) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            int correctIndex = currentQuestion.getCorrectOptionIndex();
            highlightAnswer(correctIndex, true);
        }
        
        nextButton.setEnabled(true);
        showNotification(" Time's Up!", "Moving to next question...", WARNING_COLOR);
    }
    
    private void selectAnswer(int selectedIndex) {
        questionTimer.cancel();
        long timeTaken = System.currentTimeMillis() - questionStartTime;
        
        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean isCorrect = currentQuestion.isCorrect(selectedIndex);
        
        
        for (JButton button : optionButtons) {
            if (button.isVisible()) {
                button.setEnabled(false);
            }
        }
        
        // Calculate score
        int questionScore = 0;
        if (isCorrect) {
            correctAnswers++;
            
            switch (difficulty) {
                case "Easy": questionScore = 10; break;
                case "Medium": questionScore = 20; break;
                case "Hard": questionScore = 30; break;
                default: questionScore = 10;
            }
            
            if (timeTaken < (timePerQuestion * 1000 / 2)) {
                questionScore = (int)(questionScore * 1.5);
            }
            
            totalScore += questionScore;
            scoreLabel.setText("<html><center><b>Score</b><br><font size='5'>" + 
                              totalScore + "</font></center></html>");
        }
        
        // Highlight selected answer
        highlightAnswer(selectedIndex, isCorrect);
        
        // Show correct answer if wrong
        if (reviewAnswers && !isCorrect) {
            int correctIndex = currentQuestion.getCorrectOptionIndex();
            highlightAnswer(correctIndex, true);
        }
        
        nextButton.setEnabled(true);
        
        // Show explanation if enabled
        if (showExplanations && !currentQuestion.getExplanation().isEmpty()) {
            showExplanationDialog(currentQuestion.getExplanation(), isCorrect);
        } else {
            showNotification(isCorrect ? "✓ Correct!" : "✗ Incorrect", 
                           isCorrect ? "Great job!" : "Keep trying!", 
                           isCorrect ? SUCCESS_COLOR : ERROR_COLOR);
        }
    }
    
    private void highlightAnswer(int index, boolean correct) {
        Color bgColor = correct ? SUCCESS_COLOR : ERROR_COLOR;
        Color borderColor = correct ? new Color(39, 174, 96) : new Color(192, 57, 43);
        
        optionButtons[index].setBackground(bgColor);
        optionButtons[index].setForeground(Color.WHITE);
        optionButtons[index].setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 3, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
    }
    
    private void showNotification(String title, String message, Color color) {
        JLabel notif = new JLabel("<html><b>" + title + "</b><br>" + message + "</html>");
        notif.setForeground(color);
        notif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    
    private void showExplanationDialog(String explanation, boolean correct) {
        JDialog dialog = new JDialog(this, correct ? "✓ Correct!" : "✗ Incorrect", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        Color headerColor = correct ? SUCCESS_COLOR : ERROR_COLOR;
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel header = new JLabel(correct ? "Excellent!" : " Learn from this");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(headerColor);
        header.setHorizontalAlignment(JLabel.CENTER);
        
        JTextArea explanationArea = new JTextArea(explanation);
        explanationArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setEditable(false);
        explanationArea.setBackground(new Color(250, 250, 250));
        explanationArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton okBtn = new JButton("Got it!");
        okBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okBtn.setBackground(headerColor);
        okBtn.setForeground(Color.WHITE);
        okBtn.setFocusPainted(false);
        okBtn.setBorderPainted(false);
        okBtn.setPreferredSize(new Dimension(120, 40));
        okBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(CARD_COLOR);
        btnPanel.add(okBtn);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(new JScrollPane(explanationArea), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void nextQuestion() {
        currentQuestionIndex++;
        
        if (currentQuestionIndex < questionCount && currentQuestionIndex < questions.size()) {
            displayCurrentQuestion();
        } else {
            finishQuiz();
        }
    }
    
    private void finishQuiz() {
        long totalTimeTaken = (System.currentTimeMillis() - startTime) / 1000;
        
        QuizResult result = new QuizResult(domain, difficulty, correctAnswers, 
                                          questions.size(), totalTimeTaken);
        
        userProfile.addQuizResult(domain, result);
        userProfile.saveProfile();
        
        showResultsDialog(result);
    }
    
    private void showResultsDialog(QuizResult result) {
        JDialog dialog = new JDialog(this, " Quiz Completed!", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        double percentScore = result.getPercentageScore();
        String emoji, message;
        Color themeColor;
        
        if (percentScore >= 90) {
           
            message = "Outstanding! You're a master!";
            themeColor = new Color(241, 196, 15);
        } else if (percentScore >= 70) {
            
            message = "Great job! You know your stuff!";
            themeColor = SUCCESS_COLOR;
        } else if (percentScore >= 50) {
          
            message = "Good effort. Keep practicing!";
            themeColor = PRIMARY_COLOR;
        } else {
            
            message = "Keep learning and try again!";
            themeColor = WARNING_COLOR;
        }
        
        JLabel headerLabel = new JLabel("<html><center><font size='6'>"  + 
                                       "</font><br><b>" + message + "</b></center></html>");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(themeColor);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        statsPanel.add(createResultCard(" Total Score", totalScore + " points", SUCCESS_COLOR));
        statsPanel.add(createResultCard("✓ Correct Answers", 
            result.getCorrectAnswers() + " / " + result.getTotalQuestions(), PRIMARY_COLOR));
        statsPanel.add(createResultCard(" Percentage", 
            String.format("%.1f%%", percentScore), themeColor));
        statsPanel.add(createResultCard(" Time Taken", 
            result.getTimeTakenSeconds() + " seconds", WARNING_COLOR));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton newQuizBtn = createStyledButton(" New Quiz", PRIMARY_COLOR);
        JButton reviewBtn = createStyledButton(" Review", SUCCESS_COLOR);
        JButton exitBtn = createStyledButton(" Exit", ERROR_COLOR);
        
        newQuizBtn.addActionListener(e -> {
            dialog.dispose();
            dispose();
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        });
        
        reviewBtn.setEnabled(reviewAnswers);
        reviewBtn.addActionListener(e -> {
            dialog.dispose();
            showAnswerReviewDialog();
        });
        
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(newQuizBtn);
        buttonPanel.add(reviewBtn);
        buttonPanel.add(exitBtn);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createResultCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private void showAnswerReviewDialog() {
        JDialog dialog = new JDialog(this, "Review Your Answers", true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            JPanel qPanel = createQuestionReviewPanel(q, i + 1);
            tabbedPane.addTab("Q" + (i + 1), qPanel);
        }
        
        JButton closeBtn = createStyledButton("Close", PRIMARY_COLOR);
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(BACKGROUND_COLOR);
        btnPanel.add(closeBtn);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createQuestionReviewPanel(Question q, int num) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel qLabel = new JLabel("<html><body style='width: 600px;'><b>Q" + num + ":</b> " + 
                                   q.getText() + "</body></html>");
        qLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        qLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(CARD_COLOR);
        
        List<String> options = q.getOptions();
        for (int i = 0; i < options.size(); i++) {
            JPanel optPanel = new JPanel(new BorderLayout(10, 0));
            optPanel.setBackground(CARD_COLOR);
            optPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            JLabel optLabel = new JLabel("<html>" + (char)('A' + i) + ". " + options.get(i) + "</html>");
            optLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            if (i == q.getCorrectOptionIndex()) {
                optPanel.setBackground(new Color(212, 239, 223));
                optPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(SUCCESS_COLOR, 2, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                JLabel checkLabel = new JLabel("✓");
                checkLabel.setForeground(SUCCESS_COLOR);
                checkLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                optPanel.add(checkLabel, BorderLayout.WEST);
            }
            
            optPanel.add(optLabel, BorderLayout.CENTER);
            optionsPanel.add(optPanel);
            optionsPanel.add(Box.createVerticalStrut(8));
        }
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.add(qLabel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
        
        if (showExplanations && !q.getExplanation().isEmpty()) {
            JPanel expPanel = new JPanel(new BorderLayout());
            expPanel.setBackground(new Color(254, 249, 231));
            expPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(WARNING_COLOR, 2, true),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            
            JLabel expLabel = new JLabel("<html><b> Explanation:</b><br>" + 
                                        q.getExplanation() + "</html>");
            expLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            expPanel.add(expLabel);
            
            contentPanel.add(expPanel, BorderLayout.SOUTH);
        }
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to quit?\nYour progress will be lost.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
    
    private void playSound(String soundType) {
        // Placeholder for sound effects
    }
}