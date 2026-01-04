import java.util.List;
import java.util.Scanner;

/**
 * Test program for Gemini AI question generation
 */
public class TestGeminiIntegration {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("    GEMINI AI INTEGRATION TEST");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Initialize
        Config config = new Config();
        QuestionBank questionBank = new QuestionBank();
        
        // Check if API is configured
        if (!config.isGeminiApiKeyConfigured()) {
            System.out.println(" Gemini API key is not configured!");
            System.out.println();
            System.out.println("Please run: java GeminiSetup");
            System.out.println("Or manually edit config.properties with your API key.");
            return;
        }
        
        System.out.println("✓ Gemini API key is configured");
        System.out.println("✓ AI questions enabled: " + config.useAiQuestions());
        System.out.println("✓ AI percentage: " + config.getAiQuestionsPercentage() + "%");
        System.out.println();
        
        // Test connection
        System.out.print("Testing Gemini API connection... ");
        if (questionBank.testGeminiConnection()) {
            System.out.println("✓ SUCCESS");
        } else {
            System.out.println(" FAILED");
            System.out.println("Please check your API key and internet connection.");
            return;
        }
        System.out.println();
        
        // Interactive test
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("-".repeat(70));
            System.out.println("TEST OPTIONS:");
            System.out.println("1. Generate AI questions for a custom domain");
            System.out.println("2. Get mixed questions (AI + Database)");
            System.out.println("3. Display current configuration");
            System.out.println("4. Exit");
            System.out.println("-".repeat(70));
            System.out.print("Select option (1-4): ");
            
            String choice = scanner.nextLine().trim();
            System.out.println();
            
            switch (choice) {
                case "1":
                    testAiGeneration(scanner, questionBank);
                    break;
                case "2":
                    testMixedQuestions(scanner, questionBank);
                    break;
                case "3":
                    displayConfiguration(config);
                    break;
                case "4":
                    System.out.println("Thank you for testing! Goodbye.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            
            System.out.println();
        }
    }
    
    private static void testAiGeneration(Scanner scanner, QuestionBank questionBank) {
        System.out.print("Enter domain (e.g., Java, Math, History): ");
        String domain = scanner.nextLine().trim();
        
        System.out.print("Enter difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine().trim();
        
        System.out.print("Enter number of questions: ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using default: 3");
            count = 3;
        }
        
        System.out.println();
        System.out.println("Generating " + count + " AI questions for " + domain + " (" + difficulty + ")...");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        GeminiQuestionGenerator generator = new GeminiQuestionGenerator(
            questionBank.getConfig().getGeminiApiKey()
        );
        
        List<Question> questions = generator.generateQuestions(domain, difficulty, count);
        
        long endTime = System.currentTimeMillis();
        double seconds = (endTime - startTime) / 1000.0;
        
        System.out.println("✓ Generated " + questions.size() + " questions in " + 
                         String.format("%.2f", seconds) + " seconds");
        System.out.println();
        
        displayQuestions(questions);
    }
    
    private static void testMixedQuestions(Scanner scanner, QuestionBank questionBank) {
        System.out.println("Available domains:");
        List<String> domains = questionBank.getAvailableDomains();
        for (int i = 0; i < domains.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + domains.get(i));
        }
        System.out.println("  " + (domains.size() + 1) + ". Custom domain");
        
        System.out.print("Select domain (1-" + (domains.size() + 1) + "): ");
        int domainChoice;
        try {
            domainChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Using Java.");
            domainChoice = 1;
        }
        
        String domain;
        if (domainChoice > 0 && domainChoice <= domains.size()) {
            domain = domains.get(domainChoice - 1);
        } else {
            System.out.print("Enter custom domain: ");
            domain = scanner.nextLine().trim();
        }
        
        System.out.print("Enter difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine().trim();
        
        System.out.print("Enter number of questions: ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using default: 5");
            count = 5;
        }
        
        System.out.println();
        System.out.println("Getting " + count + " questions for " + domain + " (" + difficulty + ")...");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        List<Question> questions = questionBank.getQuestionsForDomain(domain, difficulty, count);
        long endTime = System.currentTimeMillis();
        
        double seconds = (endTime - startTime) / 1000.0;
        
        System.out.println("Retrieved " + questions.size() + " questions in " + 
                         String.format("%.2f", seconds) + " seconds");
        System.out.println();
        
        displayQuestions(questions);
    }
    
    private static void displayConfiguration(Config config) {
        System.out.println("CURRENT CONFIGURATION:");
        System.out.println("-".repeat(70));
        System.out.println("API Key: " + 
                         (config.isGeminiApiKeyConfigured() ? "✓ Configured" : "❌ Not configured"));
        System.out.println("Use AI Questions: " + config.useAiQuestions());
        System.out.println("AI Percentage: " + config.getAiQuestionsPercentage() + "%");
        System.out.println("Cache AI Questions: " + config.cacheAiQuestions());
        System.out.println("-".repeat(70));
    }
    
    private static void displayQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            System.out.println("No questions generated.");
            return;
        }
        
        System.out.println("=".repeat(70));
        System.out.println("GENERATED QUESTIONS:");
        System.out.println("=".repeat(70));
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println();
            System.out.println("Question " + (i + 1) + " [" + q.getDifficulty() + "]:");
            System.out.println(q.getText());
            System.out.println();
            
            List<String> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                String marker = (j == q.getCorrectOptionIndex()) ? "correct" : " ";
                System.out.println("  " + marker + " " + (char)('A' + j) + ". " + options.get(j));
            }
            
            System.out.println();
            System.out.println("Correct Answer: " + q.getCorrectAnswer());
            System.out.println("Explanation: " + q.getExplanation());
            System.out.println("-".repeat(70));
        }
    }
}
