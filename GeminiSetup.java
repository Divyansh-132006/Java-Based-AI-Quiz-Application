import java.util.Scanner;

/**
 * Setup utility for configuring Gemini AI integration
 */
public class GeminiSetup {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Config config = new Config();
        
        System.out.println("=".repeat(60));
        System.out.println("    GEMINI AI INTEGRATION SETUP");
        System.out.println("=".repeat(60));
        System.out.println();
        
        System.out.println("This setup will help you configure Gemini AI for generating quiz questions.");
        System.out.println();
        
        // Check if API key is already configured
        if (config.isGeminiApiKeyConfigured()) {
            System.out.println(" Gemini API key is already configured.");
            System.out.print("Do you want to update it? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (!response.equals("y") && !response.equals("yes")) {
                displayCurrentSettings(config);
                configureSettings(scanner, config);
                return;
            }
        }
        
        // Get API key
        System.out.println();
        System.out.println("To use Gemini AI, you need an API key from Google AI Studio.");
        System.out.println("Get your free API key at: https://makersuite.google.com/app/apikey");
        System.out.println();
        System.out.print("Enter your Gemini API key: ");
        String apiKey = scanner.nextLine().trim();
        
        if (apiKey.isEmpty()) {
            System.out.println(" API key cannot be empty. Setup cancelled.");
            return;
        }
        
        // Save API key
        config.setGeminiApiKey(apiKey);
        System.out.println(" API key saved");
        
        // Test connection
        System.out.println();
        System.out.print("Testing connection to Gemini API... ");
        QuestionBank questionBank = new QuestionBank();
        
        if (questionBank.testGeminiConnection()) {
            System.out.println(" Connection successful!");
        } else {
            System.out.println(" Connection failed. Please check your API key.");
            return;
        }
        
        // Configure settings
        System.out.println();
        configureSettings(scanner, config);
        
        // Generate sample questions
        System.out.println();
        System.out.print("Would you like to generate sample AI questions now? (y/n): ");
        String generateSample = scanner.nextLine().trim().toLowerCase();
        
        if (generateSample.equals("y") || generateSample.equals("yes")) {
            generateSampleQuestions(questionBank);
        }
        
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("✓ Setup complete! You can now use AI-generated questions in your quizzes.");
        System.out.println("=".repeat(60));
        
        scanner.close();
    }
    
    private static void displayCurrentSettings(Config config) {
        System.out.println();
        System.out.println("Current Settings:");
        System.out.println("  - Use AI Questions: " + config.useAiQuestions());
        System.out.println("  - AI Questions Percentage: " + config.getAiQuestionsPercentage() + "%");
        System.out.println("  - Cache AI Questions: " + config.cacheAiQuestions());
        System.out.println();
    }
    
    private static void configureSettings(Scanner scanner, Config config) {
        // Enable/disable AI questions
        System.out.print("Enable AI question generation? (y/n) [current: " + 
                        config.useAiQuestions() + "]: ");
        String useAi = scanner.nextLine().trim().toLowerCase();
        if (!useAi.isEmpty()) {
            config.setUseAiQuestions(useAi.equals("y") || useAi.equals("yes"));
        }
        
        if (config.useAiQuestions()) {
            // Set percentage
            System.out.print("Percentage of AI-generated questions (0-100) [current: " + 
                            config.getAiQuestionsPercentage() + "]: ");
            String percentageStr = scanner.nextLine().trim();
            if (!percentageStr.isEmpty()) {
                try {
                    int percentage = Integer.parseInt(percentageStr);
                    config.setAiQuestionsPercentage(percentage);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, keeping current value.");
                }
            }
            
            // Cache AI questions
            System.out.print("Cache AI-generated questions for reuse? (y/n) [current: " + 
                            config.cacheAiQuestions() + "]: ");
            String cache = scanner.nextLine().trim().toLowerCase();
            if (!cache.isEmpty()) {
                config.setCacheAiQuestions(cache.equals("y") || cache.equals("yes"));
            }
        }
        
        System.out.println();
        System.out.println("✓ Settings saved");
        displayCurrentSettings(config);
    }
    
    private static void generateSampleQuestions(QuestionBank questionBank) {
        System.out.println();
        System.out.println("Generating sample questions...");
        System.out.println();
        
        String[] domains = {"Java", "Math", "Science"};
        String[] difficulties = {"Easy", "Medium", "Hard"};
        
        for (String domain : domains) {
            for (String difficulty : difficulties) {
                System.out.println("Generating " + difficulty + " questions for " + domain + "...");
                questionBank.generateAndCacheAiQuestions(domain, difficulty, 2);
            }
        }
        
        System.out.println();
        System.out.println("Sample questions generated and cached");
    }
}
