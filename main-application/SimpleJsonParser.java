/**
 * Lightweight JSON parser for handling Gemini API responses
 * This avoids external dependencies
 */
public class SimpleJsonParser {
    
    /**
     * Extract a string value from JSON
     */
    public static String getString(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "";
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return "";
        
        int startQuote = json.indexOf("\"", colonIndex);
        if (startQuote == -1) return "";
        
        int endQuote = json.indexOf("\"", startQuote + 1);
        if (endQuote == -1) return "";
        
        return json.substring(startQuote + 1, endQuote);
    }
    
    /**
     * Extract an integer value from JSON
     */
    public static int getInt(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return -1;
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return -1;
        
        // Find the number
        int numStart = colonIndex + 1;
        while (numStart < json.length() && !Character.isDigit(json.charAt(numStart)) && json.charAt(numStart) != '-') {
            numStart++;
        }
        
        int numEnd = numStart;
        while (numEnd < json.length() && (Character.isDigit(json.charAt(numEnd)) || json.charAt(numEnd) == '-')) {
            numEnd++;
        }
        
        try {
            return Integer.parseInt(json.substring(numStart, numEnd));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Extract an array from JSON
     */
    public static String getArray(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "[]";
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return "[]";
        
        int arrayStart = json.indexOf("[", colonIndex);
        if (arrayStart == -1) return "[]";
        
        int arrayEnd = findMatchingBracket(json, arrayStart);
        if (arrayEnd == -1) return "[]";
        
        return json.substring(arrayStart, arrayEnd + 1);
    }
    
    /**
     * Extract an object from JSON
     */
    public static String getObject(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "{}";
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return "{}";
        
        int objStart = json.indexOf("{", colonIndex);
        if (objStart == -1) return "{}";
        
        int objEnd = findMatchingBrace(json, objStart);
        if (objEnd == -1) return "{}";
        
        return json.substring(objStart, objEnd + 1);
    }
    
    /**
     * Split array elements
     */
    public static String[] splitArray(String arrayJson) {
        if (arrayJson.length() <= 2) return new String[0];
        
        // Remove [ and ]
        String content = arrayJson.substring(1, arrayJson.length() - 1).trim();
        if (content.isEmpty()) return new String[0];
        
        java.util.List<String> elements = new java.util.ArrayList<>();
        int depth = 0;
        int start = 0;
        boolean inString = false;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '"' && (i == 0 || content.charAt(i-1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    elements.add(content.substring(start, i).trim());
                    start = i + 1;
                }
            }
        }
        
        // Add last element
        if (start < content.length()) {
            elements.add(content.substring(start).trim());
        }
        
        return elements.toArray(new String[0]);
    }
    
    /**
     * Remove quotes from string
     */
    public static String unquote(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
    
    /**
     * Find matching closing bracket
     */
    private static int findMatchingBracket(String json, int startIndex) {
        int depth = 1;
        boolean inString = false;
        
        for (int i = startIndex + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i-1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '[') {
                    depth++;
                } else if (c == ']') {
                    depth--;
                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Find matching closing brace
     */
    private static int findMatchingBrace(String json, int startIndex) {
        int depth = 1;
        boolean inString = false;
        
        for (int i = startIndex + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i-1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
}
