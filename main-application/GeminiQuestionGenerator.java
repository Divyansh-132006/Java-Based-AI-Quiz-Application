import java.io.*;
import java.net.*;
import java.util.*;

/**
 * FIXED: Generates quiz questions using Google's Gemini AI API
 * Properly handles JSON parsing and question extraction
 */
public class GeminiQuestionGenerator {
    private String apiKey;
    // Using gemini-2.5-flash - the latest stable model (June 2025)
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent";
    
    public GeminiQuestionGenerator(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * Generate questions from Gemini AI
     */
    public List<Question> generateQuestions(String domain, String difficulty, int count) {
        List<Question> questions = new ArrayList<>();
        
        try {
            String prompt = buildPrompt(domain, difficulty, count);
            String response = callGeminiAPI(prompt);
            questions = parseGeminiResponse(response, difficulty);
            
            if (questions.size() > 0) {
                System.out.println("✓ Successfully generated " + questions.size() + " questions from Gemini AI");
            } else {
                System.out.println("✗ No questions generated - check API response");
            }
            
        } catch (Exception e) {
            System.err.println("Error generating questions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return questions;
    }
    
    /**
     * Build the prompt - OPTIMIZED for faster generation
     */
    private String buildPrompt(String domain, String difficulty, int count) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate ").append(count).append(" quiz questions about ")
              .append(domain).append(" (").append(difficulty).append(" level).\n\n");
        
        prompt.append("Return ONLY valid JSON array with this exact format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"question\": \"Question text?\",\n");
        prompt.append("    \"options\": [\"A\", \"B\", \"C\", \"D\"],\n");
        prompt.append("    \"correctIndex\": 0,\n");
        prompt.append("    \"explanation\": \"Why this is correct\"\n");
        prompt.append("  }\n");
        prompt.append("]\n\n");
        
        // Shorter difficulty guidelines
        switch (difficulty.toLowerCase()) {
            case "easy":
                prompt.append("Easy: Basic concepts and definitions.\n");
                break;
            case "medium":
                prompt.append("Medium: Applied knowledge and problem-solving.\n");
                break;
            case "hard":
                prompt.append("Hard: Advanced topics and complex scenarios.\n");
                break;
        }
        
        prompt.append("\nReturn only the JSON array, no markdown, no extra text.");
        
        return prompt.toString();
    }
    
    /**
     * Call Gemini API with improved error handling
     */
    private String callGeminiAPI(String prompt) throws Exception {
        URL url = new URL(GEMINI_API_URL + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(60000); // 60 seconds - increased for slower connections
            conn.setReadTimeout(60000);    // 60 seconds - increased for AI processing time
            
            // Build request - escape properly
            String escapedPrompt = escapeJson(prompt);
            String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Check response code
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                String error = readStream(conn.getErrorStream());
                throw new IOException("API Error " + responseCode + ": " + error);
            }
            
            // Read response
            String response = readStream(conn.getInputStream());
            return response;
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Read stream to string
     */
    private String readStream(InputStream stream) throws IOException {
        if (stream == null) return "";
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }
        return response.toString();
    }
    
    /**
     * Properly escape JSON string
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Parse Gemini response - IMPROVED
     */
    private List<Question> parseGeminiResponse(String response, String difficulty) throws Exception {
        List<Question> questions = new ArrayList<>();
        
        try {
            // Extract the text content from Gemini response
            String textContent = extractTextFromResponse(response);
            
            if (textContent == null || textContent.isEmpty()) {
                System.err.println("No text content in response");
                return questions;
            }
            
            // Clean the text - remove markdown if present
            textContent = cleanJsonText(textContent);
            
            // Debug output
            System.out.println("DEBUG - Extracted JSON: " + textContent.substring(0, Math.min(200, textContent.length())) + "...");
            
            // Parse the JSON array
            questions = parseQuestionsArray(textContent, difficulty);
            
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
            System.err.println("Response was: " + response.substring(0, Math.min(500, response.length())));
            throw e;
        }
        
        return questions;
    }
    
    /**
     * Extract text from Gemini API response structure
     */
    private String extractTextFromResponse(String response) {
        try {
            // Find the "text" field in the response
            int textIndex = response.indexOf("\"text\"");
            if (textIndex == -1) return null;
            
            int colonIndex = response.indexOf(":", textIndex);
            if (colonIndex == -1) return null;
            
            int startQuote = response.indexOf("\"", colonIndex);
            if (startQuote == -1) return null;
            
            // Find the matching end quote (accounting for escaped quotes)
            int endQuote = findMatchingQuote(response, startQuote + 1);
            if (endQuote == -1) return null;
            
            String text = response.substring(startQuote + 1, endQuote);
            
            // Unescape the string
            text = text.replace("\\n", "\n")
                       .replace("\\\"", "\"")
                       .replace("\\\\", "\\");
            
            return text;
        } catch (Exception e) {
            System.err.println("Error extracting text: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Find matching quote considering escapes
     */
    private int findMatchingQuote(String str, int startIndex) {
        for (int i = startIndex; i < str.length(); i++) {
            if (str.charAt(i) == '"' && str.charAt(i - 1) != '\\') {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Clean JSON text - remove markdown formatting
     */
    private String cleanJsonText(String text) {
        // Remove markdown code blocks
        text = text.replaceAll("```json\\s*", "");
        text = text.replaceAll("```\\s*", "");
        text = text.trim();
        
        // Find the actual JSON array
        int startBracket = text.indexOf('[');
        int endBracket = text.lastIndexOf(']');
        
        if (startBracket != -1 && endBracket != -1 && endBracket > startBracket) {
            text = text.substring(startBracket, endBracket + 1);
        }
        
        return text;
    }
    
    /**
     * Parse questions from JSON array string
     */
    private List<Question> parseQuestionsArray(String jsonArray, String difficulty) {
        List<Question> questions = new ArrayList<>();
        
        // Remove outer brackets
        if (jsonArray.startsWith("[")) {
            jsonArray = jsonArray.substring(1);
        }
        if (jsonArray.endsWith("]")) {
            jsonArray = jsonArray.substring(0, jsonArray.length() - 1);
        }
        
        // Split into individual question objects
        List<String> questionJsons = splitJsonObjects(jsonArray);
        
        for (String questionJson : questionJsons) {
            try {
                Question q = parseQuestion(questionJson, difficulty);
                if (q != null && !q.getText().isEmpty()) {
                    questions.add(q);
                }
            } catch (Exception e) {
                System.err.println("Error parsing question: " + e.getMessage());
            }
        }
        
        return questions;
    }
    
    /**
     * Split JSON array into individual objects
     */
    private List<String> splitJsonObjects(String jsonContent) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = 0;
        boolean inString = false;
        
        for (int i = 0; i < jsonContent.length(); i++) {
            char c = jsonContent.charAt(i);
            
            // Track string boundaries
            if (c == '"' && (i == 0 || jsonContent.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{') {
                    if (depth == 0) {
                        start = i;
                    }
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        objects.add(jsonContent.substring(start, i + 1));
                    }
                }
            }
        }
        
        return objects;
    }
    
    /**
     * Parse a single question object
     */
    private Question parseQuestion(String questionJson, String difficulty) {
        try {
            String questionText = extractJsonString(questionJson, "question");
            String optionsArray = extractJsonArray(questionJson, "options");
            int correctIndex = extractJsonInt(questionJson, "correctIndex");
            String explanation = extractJsonString(questionJson, "explanation");
            
            // Parse options
            List<String> options = parseOptionsArray(optionsArray);
            
            // Validate
            if (questionText.isEmpty() || options.size() < 4) {
                System.err.println("Invalid question: text=" + questionText + ", options=" + options.size());
                return null;
            }
            
            if (correctIndex < 0 || correctIndex >= options.size()) {
                System.err.println("Invalid correctIndex: " + correctIndex);
                correctIndex = 0; // Default to first option
            }
             String correctAnswer = options.get(correctIndex);
        Collections.shuffle(options);
        int newCorrectIndex = options.indexOf(correctAnswer);
            
            return new Question(
                questionText,
                options,
                correctIndex,
                difficulty,
                QuestionType.MULTIPLE_CHOICE,
                explanation
            );
            
        } catch (Exception e) {
            System.err.println("Error creating question: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract string value from JSON
     */
    private String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "";
        
        int valueStart = json.indexOf("\"", keyIndex + searchKey.length());
        if (valueStart == -1) return "";
        
        int valueEnd = findMatchingQuote(json, valueStart + 1);
        if (valueEnd == -1) return "";
        
        return json.substring(valueStart + 1, valueEnd);
    }
    
    /**
     * Extract array from JSON
     */
    private String extractJsonArray(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "[]";
        
        int arrayStart = json.indexOf("[", keyIndex);
        if (arrayStart == -1) return "[]";
        
        int arrayEnd = findMatchingBracket(json, arrayStart);
        if (arrayEnd == -1) return "[]";
        
        return json.substring(arrayStart, arrayEnd + 1);
    }
    
    /**
     * Extract integer from JSON
     */
    private int extractJsonInt(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return 0;
        
        int valueStart = keyIndex + searchKey.length();
        while (valueStart < json.length() && !Character.isDigit(json.charAt(valueStart))) {
            valueStart++;
        }
        
        int valueEnd = valueStart;
        while (valueEnd < json.length() && Character.isDigit(json.charAt(valueEnd))) {
            valueEnd++;
        }
        
        try {
            return Integer.parseInt(json.substring(valueStart, valueEnd));
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Parse options array
     */
    private List<String> parseOptionsArray(String arrayJson) {
        List<String> options = new ArrayList<>();
        
        // Remove brackets
        String content = arrayJson.substring(1, arrayJson.length() - 1).trim();
        
        // Split by commas (accounting for strings)
        boolean inString = false;
        int start = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                inString = !inString;
            } else if (c == ',' && !inString) {
                String option = content.substring(start, i).trim();
                if (option.startsWith("\"")) option = option.substring(1);
                if (option.endsWith("\"")) option = option.substring(0, option.length() - 1);
                options.add(option);
                start = i + 1;
            }
        }
        
        // Add last option
        if (start < content.length()) {
            String option = content.substring(start).trim();
            if (option.startsWith("\"")) option = option.substring(1);
            if (option.endsWith("\"")) option = option.substring(0, option.length() - 1);
            options.add(option);
        }
        
        return options;
    }
    
    /**
     * Find matching bracket
     */
    private int findMatchingBracket(String str, int startIndex) {
        int depth = 1;
        boolean inString = false;
        
        for (int i = startIndex + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (c == '"' && (i == 0 || str.charAt(i - 1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '[') depth++;
                else if (c == ']') {
                    depth--;
                    if (depth == 0) return i;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Test connection
     */
    public boolean testConnection() {
        try {
            List<Question> test = generateQuestions("General Knowledge", "Easy", 1);
            return !test.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Set API key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}