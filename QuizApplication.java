import javax.swing.*;
import java.awt.*;

/**
 * Main application launcher with enhanced startup checks
 * Ensures proper configuration before launching the quiz application
 */
public class QuizApplication {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        // Print welcome banner
        printWelcomeBanner();
        
        // Perform startup checks
        if (!performStartupChecks()) {
            System.err.println("\n❌ Startup checks failed. Please fix the issues above and restart.");
            System.exit(1);
        }
        
        // Launch application
        SwingUtilities.invokeLater(() -> {
            try {
                Login loginScreen = new Login();
                loginScreen.setVisible(true);
                System.out.println("✅ Application launched successfully!");
                System.out.println("📚 Ready to generate AI-powered questions!\n");
            } catch (Exception e) {
                System.err.println("❌ Failed to launch application: " + e.getMessage());
                e.printStackTrace();
                showErrorDialog("Failed to launch application", e.getMessage());
            }
        });
    }
    
    /**
     * Print welcome banner
     */
    private static void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  🤖 AI-POWERED QUIZ APPLICATION");
        System.out.println("  Powered by Google Gemini AI");
        System.out.println("=".repeat(70));
        System.out.println();
    }
    
    /**
     * Perform startup checks
     */
    private static boolean performStartupChecks() {
        boolean allChecksPassed = true;
        
        System.out.println("🔍 Performing startup checks...\n");
        
        // Check 1: Configuration file
        System.out.print("  Checking configuration... ");
        Config config = new Config();
        System.out.println("✅");
        
        // Check 2: API Key
        System.out.print("  Checking Gemini API key... ");
        if (!config.isGeminiApiKeyConfigured()) {
            System.out.println("⚠ NOT CONFIGURED");
            System.out.println("\n    ⚠️  Gemini API key is not configured!");
            System.out.println("    To configure, run: java GeminiSetup");
            System.out.println("    Or get your key at: https://makersuite.google.com/app/apikey\n");
            
            // Ask user if they want to continue without AI
            if (!askUserToContinue()) {
                return false;
            }
            allChecksPassed = false;
        } else {
            System.out.println("✅");
            
            // Check 3: Test API connection
            System.out.print("  Testing Gemini AI connection... ");
            QuestionBank questionBank = new QuestionBank();
            if (questionBank.testGeminiConnection()) {
                System.out.println("✅");
            } else {
                System.out.println("⚠ FAILED");
                System.out.println("\n    ⚠️  Could not connect to Gemini AI");
                System.out.println("    Possible issues:");
                System.out.println("    - Invalid API key");
                System.out.println("    - No internet connection");
                System.out.println("    - Gemini API temporarily unavailable\n");
                
                if (!askUserToContinue()) {
                    return false;
                }
                allChecksPassed = false;
            }
        }
        
        // Check 4: Profiles directory
        System.out.print("  Checking profiles directory... ");
        java.io.File profilesDir = new java.io.File("profiles");
        if (!profilesDir.exists()) {
            if (profilesDir.mkdir()) {
                System.out.println("✅ (created)");
            } else {
                System.out.println("❌ Could not create directory");
                allChecksPassed = false;
            }
        } else {
            System.out.println("✅");
        }
        
        // Check 5: Write permissions
        System.out.print("  Checking write permissions... ");
        if (profilesDir.canWrite() && new java.io.File(".").canWrite()) {
            System.out.println("✅");
        } else {
            System.out.println("❌ No write permission");
            System.out.println("\n    ❌ The application needs write permissions");
            System.out.println("    to save user profiles and configuration.\n");
            allChecksPassed = false;
        }
        
        System.out.println();
        
        if (allChecksPassed) {
            System.out.println("✅ All checks passed!");
        } else {
            System.out.println("⚠️  Some checks failed, but application can run with limited functionality.");
        }
        
        System.out.println();
        return true;
    }
    
    /**
     * Ask user if they want to continue despite issues
     */
    private static boolean askUserToContinue() {
        try {
            System.out.print("\n  Continue anyway? (y/n): ");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();
            return response.equals("y") || response.equals("yes");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Show error dialog
     */
    private static void showErrorDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
            );
        });
    }
    
    /**
     * Show startup help dialog
     */
    public static void showStartupHelp() {
        String helpMessage = 
            "AI-Powered Quiz Application\n\n" +
            "First Time Setup:\n" +
            "1. Run: java GeminiSetup\n" +
            "2. Enter your Gemini API key\n" +
            "3. Configure settings\n" +
            "4. Run: java QuizApplication\n\n" +
            "Get API Key:\n" +
            "Visit: https://makersuite.google.com/app/apikey\n\n" +
            "Features:\n" +
            "• Fresh AI questions every time\n" +
            "• 20+ domains to explore\n" +
            "• Personal progress tracking\n" +
            "• Achievement system\n\n" +
            "Need Help?\n" +
            "See README.md for detailed documentation";
        
        JTextArea textArea = new JTextArea(helpMessage);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "Application Help",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}