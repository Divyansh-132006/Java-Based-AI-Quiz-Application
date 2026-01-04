

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Modern and attractive login screen with gradient backgrounds and smooth styling
 */
public class Login extends JFrame {
    private JTextField usernameField;
    private JComboBox<String> domainCombo;
    private JComboBox<String> difficultyCombo;
    private JComboBox<String> themeCombo;
    private JRadioButton newUserRadio;
    private JRadioButton existingUserRadio;
    private JComboBox<String> existingUsersCombo;
    private QuestionBank questionBank;
    
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(27, 79, 114);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public Login() {
        questionBank = new QuestionBank();
        
        setTitle("Quiz Application - Login");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), 0, h, new Color(109, 213, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Header with modern styling
        JPanel headerPanel = createHeaderPanel();
        
        // Center card panel
        JPanel cardPanel = new JPanel(new BorderLayout(15, 15));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // User profile section
        JPanel profilePanel = createProfilePanel();
        
        // Settings section
        JPanel settingsPanel = createSettingsPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Layout
        JPanel centerContent = new JPanel(new GridLayout(2, 1, 15, 15));
        centerContent.setOpaque(false);
        centerContent.add(profilePanel);
        centerContent.add(settingsPanel);
        
        cardPanel.add(centerContent, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
  // ...existing code...
private JPanel createHeaderPanel() {
    JPanel header = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), ACCENT_COLOR);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    };
    header.setOpaque(true);
    header.setBorder(new EmptyBorder(16, 24, 16, 24));

    JLabel titleLabel = new JLabel("Quiz Application", SwingConstants.CENTER);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

    // Developers (separate lines)
    JPanel devPanel = new JPanel(new GridLayout(0, 1, 0, 2));
    devPanel.setOpaque(false);

    Font devFont = new Font("SansSerif", Font.PLAIN, 12);

    JLabel dev1 = new JLabel(" Developed by : Ayush Singh", SwingConstants.CENTER);
    dev1.setForeground(Color.WHITE);
    dev1.setFont(devFont);

    JLabel dev2 = new JLabel("                   Kajal Kumari", SwingConstants.CENTER);
    dev2.setForeground(Color.WHITE);
    dev2.setFont(devFont);

    JLabel dev3 = new JLabel("                 Tamana", SwingConstants.CENTER);
    dev3.setForeground(Color.WHITE);
    dev3.setFont(devFont);

    devPanel.add(dev1);
    devPanel.add(dev2);
    devPanel.add(dev3);

    JPanel center = new JPanel();
    center.setOpaque(false);
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    devPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    center.add(titleLabel);
    center.add(Box.createVerticalStrut(6));
    center.add(devPanel);

    header.add(center, BorderLayout.CENTER);
    return header;
}
// ...existing code...
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(ACCENT_COLOR, 2, true), "👤 User Profile",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // User type selection
        ButtonGroup userTypeGroup = new ButtonGroup();
        newUserRadio = new JRadioButton("New User");
        existingUserRadio = new JRadioButton("Existing User");
        styleRadioButton(newUserRadio);
        styleRadioButton(existingUserRadio);
        userTypeGroup.add(newUserRadio);
        userTypeGroup.add(existingUserRadio);
        newUserRadio.setSelected(true);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        radioPanel.setBackground(CARD_COLOR);
        radioPanel.add(newUserRadio);
        radioPanel.add(existingUserRadio);
        panel.add(radioPanel);
        
        panel.add(Box.createVerticalStrut(10));
        
        // New user field
        JPanel newUserPanel = new JPanel(new BorderLayout(10, 0));
        newUserPanel.setBackground(CARD_COLOR);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(TEXT_COLOR);
        usernameField = new JTextField(15);
        styleTextField(usernameField);
        newUserPanel.add(usernameLabel, BorderLayout.WEST);
        newUserPanel.add(usernameField, BorderLayout.CENTER);
        panel.add(newUserPanel);
        
        panel.add(Box.createVerticalStrut(5));
        
        // Existing user combo
        JPanel existingUserPanel = new JPanel(new BorderLayout(10, 0));
        existingUserPanel.setBackground(CARD_COLOR);
        JLabel existingLabel = new JLabel("Select User:");
        existingLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        existingLabel.setForeground(TEXT_COLOR);
        existingUsersCombo = new JComboBox<>();
        styleComboBox(existingUsersCombo);
        refreshUserList();
        existingUserPanel.add(existingLabel, BorderLayout.WEST);
        existingUserPanel.add(existingUsersCombo, BorderLayout.CENTER);
        panel.add(existingUserPanel);
        existingUserPanel.setVisible(false);
        
        // Radio button listeners
        newUserRadio.addActionListener(e -> {
            newUserPanel.setVisible(true);
            existingUserPanel.setVisible(false);
        });
        
        existingUserRadio.addActionListener(e -> {
            newUserPanel.setVisible(false);
            existingUserPanel.setVisible(true);
            refreshUserList();
        });
        
        return panel;
    }
    
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(ACCENT_COLOR, 2, true), "⚙️ Quiz Settings",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10,10, 10, 10)
        ));
        
        // Domain
        panel.add(createSettingRow("📚 Domain:", 
            domainCombo = createStyledCombo(questionBank.getAvailableDomains().toArray(new String[0]))));
        panel.add(Box.createVerticalStrut(10));
        
        // Difficulty
        panel.add(createSettingRow("🎯 Difficulty:", 
            difficultyCombo = createStyledCombo(new String[]{"Easy", "Medium", "Hard"})));
        panel.add(Box.createVerticalStrut(10));
        
        // Theme
        panel.add(createSettingRow("🎨 Theme:", 
            themeCombo = createStyledCombo(new String[]{"Light", "Dark", "Blue", "Green"})));
        
        return panel;
    }
    
    private JPanel createSettingRow(String labelText, JComboBox<String> combo) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_COLOR);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setPreferredSize(new Dimension(120, 25));
        row.add(label, BorderLayout.WEST);
        row.add(combo, BorderLayout.CENTER);
        return row;
    }
    
    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        styleComboBox(combo);
        return combo;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(CARD_COLOR);
        
        JButton startButton = createStyledButton("🚀 Start Quiz", PRIMARY_COLOR, Color.WHITE);
        JButton viewStatsButton = createStyledButton("📊 View Statistics", SUCCESS_COLOR, Color.WHITE);
        
        startButton.addActionListener(e -> startQuiz());
        viewStatsButton.addActionListener(e -> viewStatistics());
        
        panel.add(startButton);
        panel.add(viewStatsButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
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
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(189, 195, 199), 1, true));
    }
    
    private void styleRadioButton(JRadioButton radio) {
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        radio.setBackground(CARD_COLOR);
        radio.setForeground(TEXT_COLOR);
        radio.setFocusPainted(false);
    }
    
    private void refreshUserList() {
        existingUsersCombo.removeAllItems();
        List<String> usernames = UserProfile.getAllUsernames();
        for (String username : usernames) {
            existingUsersCombo.addItem(username);
        }
    }
    
    private void startQuiz() {
        String username;
        if (newUserRadio.isSelected()) {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showStyledMessage("Please enter a username", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (existingUsersCombo.getSelectedItem() == null) {
                showStyledMessage("No existing users found. Please create a new user.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            username = existingUsersCombo.getSelectedItem().toString();
        }
        
        UserProfile profile = UserProfile.loadProfile(username);
        profile.setPreferredDifficulty(difficultyCombo.getSelectedItem().toString());
        profile.setPreferredTheme(themeCombo.getSelectedItem().toString());
        profile.saveProfile();
        
        String domain = domainCombo.getSelectedItem().toString();
        String difficulty = difficultyCombo.getSelectedItem().toString();
        
        setVisible(false);
        SwingUtilities.invokeLater(() -> {
            Rules rulesScreen = new Rules(profile, domain, difficulty, questionBank);
            rulesScreen.setVisible(true);
        });
    }
    
    private void viewStatistics() {
        String username;
        if (newUserRadio.isSelected()) {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showStyledMessage("Please enter a username", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!UserProfile.getAllUsernames().contains(username)) {
                showStyledMessage("User not found. Create a profile by starting a quiz first.", 
                    "User Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (existingUsersCombo.getSelectedItem() == null) {
                showStyledMessage("No existing users found.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            username = existingUsersCombo.getSelectedItem().toString();
        }
        
        UserProfile profile = UserProfile.loadProfile(username);
        showStatisticsDialog(profile);
    }
    
    private void showStatisticsDialog(UserProfile profile) {
        JDialog dialog = new JDialog(this, "📊 Statistics - " + profile.getUsername(), true);
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Statistics for " + profile.getUsername());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Achievements panel
        JPanel achievPanel = createAchievementsPanel(profile);
        JPanel statsPanel = createStatsPanel(profile);
        
        contentPanel.add(achievPanel);
        contentPanel.add(statsPanel);
        
        JButton closeBtn = createStyledButton("Close", PRIMARY_COLOR, Color.WHITE);
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(BACKGROUND_COLOR);
        btnPanel.add(closeBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createAchievementsPanel(UserProfile profile) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("🏆 Achievements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CARD_COLOR);
        
        JLabel points = new JLabel("Points: " + profile.getAchievementPoints());
        points.setFont(new Font("Segoe UI", Font.BOLD, 14));
        points.setForeground(SUCCESS_COLOR);
        content.add(points);
        content.add(Box.createVerticalStrut(10));
        
        if (profile.getEarnedBadges().isEmpty()) {
            JLabel noBadges = new JLabel("No badges yet!");
            noBadges.setForeground(TEXT_SECONDARY);
            content.add(noBadges);
        } else {
            for (String badge : profile.getEarnedBadges()) {
                JLabel badgeLabel = new JLabel("🎖️ " + badge);
                badgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                content.add(badgeLabel);
                content.add(Box.createVerticalStrut(5));
            }
        }
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(content), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel(UserProfile profile) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("📈 Performance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CARD_COLOR);
        
        JLabel total = new JLabel("Total Quizzes: " + profile.getTotalQuizzesTaken());
        total.setFont(new Font("Segoe UI", Font.BOLD, 13));
        content.add(total);
        content.add(Box.createVerticalStrut(10));
        
        if (profile.getTotalQuizzesTaken() > 0) {
            for (String domain : questionBank.getAvailableDomains()) {
                List<QuizResult> results = profile.getResultsForDomain(domain);
                if (!results.isEmpty()) {
                    double avg = results.stream()
                        .mapToDouble(QuizResult::getPercentageScore)
                        .average().orElse(0);
                    
                    JLabel domainLabel = new JLabel(String.format("%s: %.1f%%", domain, avg));
                    domainLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    content.add(domainLabel);
                    content.add(Box.createVerticalStrut(5));
                }
            }
        } else {
            JLabel noStats = new JLabel("No quiz data yet!");
            noStats.setForeground(TEXT_SECONDARY);
            content.add(noStats);
        }
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(content), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showStyledMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}