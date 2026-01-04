import java.io.*;
import java.util.*;

/**
 * Manages application configuration including API keys
 */
public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;
    
    public Config() {
        properties = new Properties();
        loadConfig();
    }
    
    /**
     * Load configuration from file
     */
    private void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading config: " + e.getMessage());
                createDefaultConfig();
            }
        } else {
            createDefaultConfig();
        }
    }
    
    /**
     * Create default configuration file
     */
    private void createDefaultConfig() {
        properties.setProperty("gemini.api.key", "YOUR_GEMINI_API_KEY_HERE");
        properties.setProperty("use.ai.questions", "false");
        properties.setProperty("ai.questions.percentage", "50");
        properties.setProperty("cache.ai.questions", "true");
        
        saveConfig();
        
        System.out.println("Created default config.properties file.");
        System.out.println("Please add your Gemini API key to enable AI-generated questions.");
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Quiz Application Configuration");
        } catch (IOException e) {
            System.err.println("Error saving config: " + e.getMessage());
        }
    }
    
    /**
     * Get Gemini API key
     */
    public String getGeminiApiKey() {
        return properties.getProperty("gemini.api.key", "");
    }
    
    /**
     * Set Gemini API key
     */
    public void setGeminiApiKey(String apiKey) {
        properties.setProperty("gemini.api.key", apiKey);
        saveConfig();
    }
    
    /**
     * Check if AI questions should be used
     */
    public boolean useAiQuestions() {
        String value = properties.getProperty("use.ai.questions", "false");
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Set whether to use AI questions
     */
    public void setUseAiQuestions(boolean use) {
        properties.setProperty("use.ai.questions", String.valueOf(use));
        saveConfig();
    }
    
    /**
     * Get percentage of AI questions (0-100)
     */
    public int getAiQuestionsPercentage() {
        String value = properties.getProperty("ai.questions.percentage", "50");
        try {
            int percentage = Integer.parseInt(value);
            return Math.max(0, Math.min(100, percentage));
        } catch (NumberFormatException e) {
            return 50;
        }
    }
    
    /**
     * Set percentage of AI questions
     */
    public void setAiQuestionsPercentage(int percentage) {
        percentage = Math.max(0, Math.min(100, percentage));
        properties.setProperty("ai.questions.percentage", String.valueOf(percentage));
        saveConfig();
    }
    
    /**
     * Check if AI questions should be cached
     */
    public boolean cacheAiQuestions() {
        String value = properties.getProperty("cache.ai.questions", "true");
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Set whether to cache AI questions
     */
    public void setCacheAiQuestions(boolean cache) {
        properties.setProperty("cache.ai.questions", String.valueOf(cache));
        saveConfig();
    }
    
    /**
     * Check if API key is configured
     */
    public boolean isGeminiApiKeyConfigured() {
        String key = getGeminiApiKey();
        return key != null && !key.isEmpty() && !key.equals("YOUR_GEMINI_API_KEY_HERE");
    }
}
