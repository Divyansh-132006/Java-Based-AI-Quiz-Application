import java.io.*;
import java.util.*;

/**
 * Handles user profile management and progress tracking
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private Map<String, List<QuizResult>> quizHistory;
    private int totalQuizzesTaken;
    private int achievementPoints;
    private List<String> earnedBadges;
    private String preferredDifficulty;
    private String preferredTheme;
    
    public UserProfile(String username) {
        this.username = username;
        this.quizHistory = new HashMap<>();
        this.totalQuizzesTaken = 0;
        this.achievementPoints = 0;
        this.earnedBadges = new ArrayList<>();
        this.preferredDifficulty = "Medium";
        this.preferredTheme = "Light";
    }
    
    public void addQuizResult(String domain, QuizResult result) {
        if (!quizHistory.containsKey(domain)) {
            quizHistory.put(domain, new ArrayList<>());
        }
        quizHistory.get(domain).add(result);
        totalQuizzesTaken++;
        
        // Check for achievements
        checkForAchievements();
    }
    
    private void checkForAchievements() {
        // First quiz completed
        if (totalQuizzesTaken == 1 && !earnedBadges.contains("First Quiz")) {
            earnedBadges.add("First Quiz");
            achievementPoints += 10;
        }
        
        // Fifth quiz completed
        if (totalQuizzesTaken == 5 && !earnedBadges.contains("Quiz Enthusiast")) {
            earnedBadges.add("Quiz Enthusiast");
            achievementPoints += 25;
        }
        
        // Perfect score achievement
        for (Map.Entry<String, List<QuizResult>> entry : quizHistory.entrySet()) {
            List<QuizResult> results = entry.getValue();
            if (!results.isEmpty()) {
                QuizResult latestResult = results.get(results.size() - 1);
                if (latestResult.isPerfectScore() && !earnedBadges.contains("Perfect Score: " + entry.getKey())) {
                    earnedBadges.add("Perfect Score: " + entry.getKey());
                    achievementPoints += 50;
                }
            }
        }
    }
    
    public Map<String, Double> getAverageScores() {
        Map<String, Double> averages = new HashMap<>();
        
        for (Map.Entry<String, List<QuizResult>> entry : quizHistory.entrySet()) {
            String domain = entry.getKey();
            List<QuizResult> results = entry.getValue();
            
            if (results.isEmpty()) continue;
            
            double sum = 0;
            for (QuizResult result : results) {
                sum += result.getPercentageScore();
            }
            averages.put(domain, sum / results.size());
        }
        
        return averages;
    }
    
    public List<QuizResult> getResultsForDomain(String domain) {
        return quizHistory.getOrDefault(domain, new ArrayList<>());
    }
    
    // Save the profile to file
    public void saveProfile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("profiles/" + username + ".dat"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
        }
    }
    
    // Load a profile from file by username
    public static UserProfile loadProfile(String username) {
        File directory = new File("profiles");
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        File profileFile = new File("profiles/" + username + ".dat");
        if (!profileFile.exists()) {
            return new UserProfile(username);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(profileFile))) {
            return (UserProfile) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading profile: " + e.getMessage());
            return new UserProfile(username);
        }
    }
    
    // Get all saved usernames
    public static List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        File directory = new File("profiles");
        
        if (!directory.exists() || !directory.isDirectory()) {
            return usernames;
        }
        
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                usernames.add(filename.substring(0, filename.length() - 4));
            }
        }
        
        return usernames;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public int getTotalQuizzesTaken() {
        return totalQuizzesTaken;
    }
    
    public int getAchievementPoints() {
        return achievementPoints;
    }
    
    public List<String> getEarnedBadges() {
        return earnedBadges;
    }
    
    public String getPreferredDifficulty() {
        return preferredDifficulty;
    }
    
    public void setPreferredDifficulty(String preferredDifficulty) {
        this.preferredDifficulty = preferredDifficulty;
    }
    
    public String getPreferredTheme() {
        return preferredTheme;
    }
    
    public void setPreferredTheme(String preferredTheme) {
        this.preferredTheme = preferredTheme;
    }
}