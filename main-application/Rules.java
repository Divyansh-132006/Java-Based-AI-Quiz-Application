import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern rules screen with attractive design and customization options
 */
public class Rules extends JFrame {
    private UserProfile userProfile;
    private String domain;
    private String difficulty;
    private QuestionBank questionBank;
    private JSpinner questionCountSpinner;
    private JSpinner timePerQuestionSpinner;
    private JCheckBox reviewAnswersCheckbox;
    private JCheckBox showExplanationsCheckbox;
    private JCheckBox soundEffectsCheckbox;
    
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    public Rules(UserProfile userProfile, String domain, 
                String difficulty, QuestionBank questionBank) {
        this.userProfile = userProfile;
        this.domain = domain;
        this.difficulty = difficulty;
        this.questionBank = questionBank;
        
        setTitle("Quiz Setup - " + domain);
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content area with two columns
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(createInfoPanel(), BorderLayout.NORTH);
        leftPanel.add(createRulesPanel(), BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(createOptionsPanel(), BorderLayout.CENTER);
        
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        
        // Buttons
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + userProfile.getUsername() + "! 👋");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel subtitleLabel = new JLabel("Customize your quiz experience before you begin");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("📚 Quiz Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createInfoRow("Domain:", domain, "🎯"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createInfoRow("Difficulty:", difficulty, getDifficultyIcon(difficulty)));
        panel.add(Box.createVerticalStrut(15));
        
        // Question count
        JPanel countPanel = new JPanel(new BorderLayout(10, 0));
        countPanel.setBackground(CARD_COLOR);
        countPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel countLabel = new JLabel("📝 Number of Questions:");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(TEXT_COLOR);
        
        SpinnerNumberModel questionModel = new SpinnerNumberModel(10, 5, 30, 1);
        questionCountSpinner = new JSpinner(questionModel);
        questionCountSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) questionCountSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        countPanel.add(countLabel, BorderLayout.WEST);
        countPanel.add(questionCountSpinner, BorderLayout.EAST);
        panel.add(countPanel);
        
        panel.add(Box.createVerticalStrut(10));
        
        // Time per question
        JPanel timePanel = new JPanel(new BorderLayout(10, 0));
        timePanel.setBackground(CARD_COLOR);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel timeLabel = new JLabel("⏱️ Time per Question (sec):");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(TEXT_COLOR);
        
        SpinnerNumberModel timeModel = new SpinnerNumberModel(30, 10, 120, 5);
        timePerQuestionSpinner = new JSpinner(timeModel);
        timePerQuestionSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) timePerQuestionSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        timePanel.add(timeLabel, BorderLayout.WEST);
        timePanel.add(timePerQuestionSpinner, BorderLayout.EAST);
        panel.add(timePanel);
        
        return panel;
    }
    
    private JPanel createInfoRow(String label, String value, String icon) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_COLOR);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(400, 30));
        
        JLabel labelComp = new JLabel(icon + " " + label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(TEXT_COLOR);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComp.setForeground(PRIMARY_COLOR);
        
        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.EAST);
        
        return row;
    }
    
    private String getDifficultyIcon(String diff) {
        switch (diff) {
            case "Easy": return "🟢";
            case "Medium": return "🟡";
            case "Hard": return "🔴";
            default: return "⭐";
        }
    }
    
    private JPanel createRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUCCESS_COLOR, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("📋 Quiz Rules");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(SUCCESS_COLOR);
        
        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rulesText.setBackground(new Color(250, 250, 250));
        rulesText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rulesText.setText(
            "SCORING SYSTEM:\n\n" +
            "• Easy Questions: 10 points\n" +
            "• Medium Questions: 20 points\n" +
            "• Hard Questions: 30 points\n\n" +
            " SPEED BONUS:\n\n" +
            "Answer within half the time limit to earn a 50% bonus!\n\n" +
            " ACHIEVEMENTS:\n\n" +
            "• Complete quizzes to earn badges\n" +
            "• Perfect scores unlock special achievements\n" +
            "• Track your progress across domains\n\n" +
            "IMPORTANT:\n\n" +
            "• No negative marking for wrong answers\n" +
            "• Review explanations to learn\n" +
            "• Challenge yourself and have fun!"
        );
        
        JScrollPane scrollPane = new JScrollPane(rulesText);
        scrollPane.setBorder(null);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(WARNING_COLOR, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel("⚙️ Quiz Options");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WARNING_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Review answers option
        reviewAnswersCheckbox = createStyledCheckbox(
            "Review Answers After Quiz",
            "See which answers were correct after completing the quiz"
        );
        reviewAnswersCheckbox.setSelected(true);
        panel.add(reviewAnswersCheckbox);
        panel.add(Box.createVerticalStrut(15));
        
        // Show explanations option
        showExplanationsCheckbox = createStyledCheckbox(
            "Show Explanations",
            "Display detailed explanations for each answer"
        );
        showExplanationsCheckbox.setSelected(true);
        panel.add(showExplanationsCheckbox);
        panel.add(Box.createVerticalStrut(15));
        
        // Sound effects option
        soundEffectsCheckbox = createStyledCheckbox(
            "Enable Sound Effects",
            "Play sounds for correct/incorrect answers"
        );
        soundEffectsCheckbox.setSelected(true);
        panel.add(soundEffectsCheckbox);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Tips section
        JPanel tipsPanel = createTipsPanel();
        tipsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tipsPanel);
        
        return panel;
    }
    
    private JCheckBox createStyledCheckbox(String title, String description) {
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(CARD_COLOR);
        checkboxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox checkbox = new JCheckBox(title);
        checkbox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkbox.setForeground(TEXT_COLOR);
        checkbox.setBackground(CARD_COLOR);
        checkbox.setFocusPainted(false);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><i>" + description + "</i></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        checkboxPanel.add(checkbox);
        checkboxPanel.add(descLabel);
        
        return checkbox;
    }
    
    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 248, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(WARNING_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tipLabel = new JLabel("💡 Pro Tips");
        tipLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tipLabel.setForeground(new Color(183, 149, 11));
        
        JTextArea tipText = new JTextArea();
        tipText.setEditable(false);
        tipText.setLineWrap(true);
        tipText.setWrapStyleWord(true);
        tipText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tipText.setBackground(new Color(255, 248, 220));
        tipText.setBorder(null);
        tipText.setText(
            "• Read questions carefully\n" +
            "• Manage your time wisely\n" +
            "• Trust your first instinct\n" +
            "• Learn from explanations"
        );
        
        panel.add(tipLabel, BorderLayout.NORTH);
        panel.add(tipText, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(BACKGROUND_COLOR);
        
        JButton backButton = createStyledButton("← Back", new Color(149, 165, 166), Color.WHITE);
        JButton startButton = createStyledButton("Start Quiz ", SUCCESS_COLOR, Color.WHITE);
        
        backButton.addActionListener(e -> goBackToLogin());
        startButton.addActionListener(e -> startQuiz());
        
        panel.add(backButton);
        panel.add(startButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void startQuiz() {
        int questionCount = (Integer) questionCountSpinner.getValue();
        int timePerQuestion = (Integer) timePerQuestionSpinner.getValue();
        boolean reviewAnswers = reviewAnswersCheckbox.isSelected();
        boolean showExplanations = showExplanationsCheckbox.isSelected();
        boolean soundEffects = soundEffectsCheckbox.isSelected();
        
        setVisible(false);
        
        SwingUtilities.invokeLater(() -> {
            Quiz quizScreen = new Quiz(
                userProfile, domain, difficulty, questionBank,
                questionCount, timePerQuestion, reviewAnswers, 
                showExplanations, soundEffects
            );
            quizScreen.startQuiz();
        });
    }
    
    private void goBackToLogin() {
        dispose();
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}