import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Stores the result of a single quiz attempt
 */
public class QuizResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private String domain;
    private String difficulty;
    private int correctAnswers;
    private int totalQuestions;
    private long timeTakenSeconds;
    private LocalDateTime completionDate;
    
    public QuizResult(String domain, String difficulty, int correctAnswers, 
                     int totalQuestions, long timeTakenSeconds) {
        this.domain = domain;
        this.difficulty = difficulty;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.timeTakenSeconds = timeTakenSeconds;
        this.completionDate = LocalDateTime.now();
    }
    
    public double getPercentageScore() {
        return (double) correctAnswers / totalQuestions * 100;
    }
    
    public boolean isPerfectScore() {
        return correctAnswers == totalQuestions;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }
    
    public LocalDateTime getCompletionDate() {
        return completionDate;
    }
    
    @Override
    public String toString() {
        return String.format(
            "[%s] %s difficulty: %d/%d correct (%.1f%%) - Time: %d seconds",
            domain, difficulty, correctAnswers, totalQuestions, 
            getPercentageScore(), timeTakenSeconds
        );
    }
}