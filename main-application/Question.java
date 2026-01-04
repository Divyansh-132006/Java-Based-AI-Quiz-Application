import java.io.Serializable;
import java.util.List;

/**
 * Represents a single quiz question with various types and difficulty levels
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String text;
    private List<String> options;
    private int correctOptionIndex;
    private String difficulty;  // "Easy", "Medium", "Hard"
    private QuestionType type;
    private String explanation;
    
    public Question(String text, List<String> options, int correctOptionIndex, 
                   String difficulty, QuestionType type, String explanation) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.difficulty = difficulty;
        this.type = type;
        this.explanation = explanation;
    }
    
    public String getText() {
        return text;
    }
    
    public List<String> getOptions() {
        return options;
    }
    
    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
    
    public String getCorrectAnswer() {
        return options.get(correctOptionIndex);
    }
    
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public QuestionType getType() {
        return type;
    }
    
    public String getExplanation() {
        return explanation;
    }
}

/**
 * Enum for different question types
 */
enum QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    FILL_IN_BLANK,
    MATCHING
}