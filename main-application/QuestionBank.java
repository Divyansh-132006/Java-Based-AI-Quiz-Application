


import java.io.*;
import java.util.*;

/**
 * AI-Powered Question Bank that generates all questions from Gemini AI
 * Ensures users always get fresh, unique questions
 */
public class QuestionBank {
    private Config config;
    private GeminiQuestionGenerator geminiGenerator;
    private Map<String, List<Question>> sessionCache;
    private static final String[] DEFAULT_DOMAINS = {
        "Java Programming", "Python Programming", "Data Structures",
        "Algorithms", "Database Systems", "Web Development",
        "Mathematics", "Physics", "Chemistry", "Biology",
        "History", "Geography", "General Knowledge", "English Literature",
        "Computer Networks", "Operating Systems", "Artificial Intelligence",
        "Machine Learning", "Cybersecurity", "Cloud Computing"
    };
    
    public QuestionBank() {
        this.config = new Config();
        this.sessionCache = new HashMap<>();
        
        // Initialize Gemini generator if API key is configured
        if (config.isGeminiApiKeyConfigured()) {
            this.geminiGenerator = new GeminiQuestionGenerator(config.getGeminiApiKey());
        } else {
            System.err.println("⚠ Warning: Gemini API key not configured!");
            System.err.println("Please run: java GeminiSetup");
        }
    }
    
    /**
     * Get available domains for quiz
     */
    public List<String> getAvailableDomains() {
        return Arrays.asList(DEFAULT_DOMAINS);
    }
    
    /**
     * Get configuration
     */
    public Config getConfig() {
        return config;
    }
    
    /**
     * Get questions for a specific domain and difficulty
     * ALL questions are generated fresh from Gemini AI
     */
    public List<Question> getQuestionsForDomain(String domain, String difficulty, int count) {
        System.out.println("\n Generating " + count + " fresh AI questions...");
        System.out.println("Domain: " + domain + " | Difficulty: " + difficulty);
        
        // Check if Gemini is configured
        if (geminiGenerator == null) {
            System.err.println(" Gemini AI is not configured!");
            return getFallbackQuestions(domain, difficulty, count);
        }
        
        // Generate fresh questions from Gemini
        List<Question> questions = generateFreshQuestions(domain, difficulty, count);
        
        if (questions.isEmpty()) {
            System.err.println(" Failed to generate questions, using fallback");
            return getFallbackQuestions(domain, difficulty, count);
        }
        
        // Cache for the current session (to show in review)
        String cacheKey = domain + "_" + difficulty;
        sessionCache.put(cacheKey, questions);
        
        System.out.println(" Successfully generated " + questions.size() + " unique questions\n");
        return questions;
    }
    
    /**
     * Generate fresh questions from Gemini AI with retry logic
     */
    private List<Question> generateFreshQuestions(String domain, String difficulty, int count) {
        int maxRetries = 3;
        int attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                List<Question> questions = geminiGenerator.generateQuestions(domain, difficulty, count);
                
                if (questions.size() >= count) {
                    return questions;
                } else if (!questions.isEmpty()) {
                    // Got some questions but not enough, try to get more
                    int remaining = count - questions.size();
                    System.out.println("Got " + questions.size() + " questions, generating " + remaining + " more...");
                    
                    List<Question> additionalQuestions = geminiGenerator.generateQuestions(domain, difficulty, remaining);
                    questions.addAll(additionalQuestions);
                    
                    if (questions.size() >= count) {
                        return questions.subList(0, count);
                    }
                }
                
                attempt++;
                if (attempt < maxRetries) {
                    System.out.println(" Retry attempt " + attempt + " of " + maxRetries);
                    Thread.sleep(1000); // Wait before retry
                }
                
            } catch (Exception e) {
                System.err.println(" Error generating questions (attempt " + (attempt + 1) + "): " + e.getMessage());
                attempt++;
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000); // Wait longer before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Fallback questions in case Gemini fails
     * These are basic questions to keep the app functional
     */
    private List<Question> getFallbackQuestions(String domain, String difficulty, int count) {
        List<Question> fallbackQuestions = new ArrayList<>();
        
        // Create basic fallback questions
        for (int i = 0; i < Math.min(count, 5); i++) {
            List<String> options = Arrays.asList(
                "Option A",
                "Option B", 
                "Option C",
                "Option D"
            );
            
            Question q = new Question(
                "Sample question " + (i + 1) + " for " + domain + " (" + difficulty + ")",
                options,
                0,
                difficulty,
                QuestionType.MULTIPLE_CHOICE,
                "This is a fallback question. Please configure Gemini API for AI-generated questions."
            );
            
            fallbackQuestions.add(q);
        }
        
        return fallbackQuestions;
    }
    
    /**
     * Test Gemini connection
     */
    public boolean testGeminiConnection() {
        if (geminiGenerator == null) {
            return false;
        }
        
        try {
            System.out.print("Testing Gemini AI connection... ");
            List<Question> testQuestions = geminiGenerator.generateQuestions("General Knowledge", "Easy", 1);
            boolean success = !testQuestions.isEmpty();
            
            if (success) {
                System.out.println(" Success!");
            } else {
                System.out.println(" Failed - No questions generated");
            }
            
            return success;
        } catch (Exception e) {
            System.out.println("Failed - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate and cache AI questions for future use
     * This is optional - mainly for pre-generating sample questions
     */
    public void generateAndCacheAiQuestions(String domain, String difficulty, int count) {
        if (geminiGenerator == null) {
            System.err.println(" Gemini generator not initialized");
            return;
        }
        
        System.out.println("Generating " + count + " questions for " + domain + " (" + difficulty + ")...");
        
        try {
            List<Question> questions = geminiGenerator.generateQuestions(domain, difficulty, count);
            
            if (!questions.isEmpty()) {
                String cacheKey = domain + "_" + difficulty;
                sessionCache.put(cacheKey, questions);
                System.out.println(" Generated " + questions.size() + " questions");
            } else {
                System.err.println(" No questions generated");
            }
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
    
    /**
     * Clear session cache
     */
    public void clearSessionCache() {
        sessionCache.clear();
        System.out.println("Session cache cleared");
    }
    
    /**
     * Get statistics about generated questions
     */
    public Map<String, Integer> getGenerationStats() {
        Map<String, Integer> stats = new HashMap<>();
        
        for (Map.Entry<String, List<Question>> entry : sessionCache.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().size());
        }
        
        return stats;
    }
    
    /**
     * Validate Gemini setup
     */
    public boolean isGeminiConfigured() {
        return geminiGenerator != null && config.isGeminiApiKeyConfigured();
    }
    
    /**
     * Get Gemini generator instance
     */
    public GeminiQuestionGenerator getGeminiGenerator() {
        return geminiGenerator;
    }
    
    /**
     * Reinitialize Gemini generator with new API key
     */
    public void reinitializeGemini(String apiKey) {
        config.setGeminiApiKey(apiKey);
        this.geminiGenerator = new GeminiQuestionGenerator(apiKey);
        System.out.println("Gemini generator reinitialized");
    }
}