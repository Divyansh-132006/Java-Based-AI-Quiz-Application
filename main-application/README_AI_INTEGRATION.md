# 🎓 Quiz Application with AI-Powered Questions

A Java-based quiz application featuring **AI-generated questions** using Google's Gemini API. Generate unlimited questions on any topic with customizable difficulty levels!

---

## ✨ Features

### Core Features
- ✅ Multiple question types (Multiple Choice, True/False)
- ✅ Multiple domains (Java, Math, Science, General Knowledge)
- ✅ Three difficulty levels (Easy, Medium, Hard)
- ✅ User profiles and progress tracking
- ✅ Quiz history and statistics
- ✅ Achievement system with badges

### 🤖 NEW: AI Integration
- ✅ **Dynamic question generation** using Gemini AI
- ✅ **Custom domains** - Generate questions for ANY topic
- ✅ **Hybrid mode** - Mix AI and database questions
- ✅ **Smart caching** - Save AI questions for reuse
- ✅ **Configurable** - Control AI usage percentage
- ✅ **Automatic fallback** - Database questions if AI unavailable

---

## 🚀 Quick Start

### 1. Clone/Download the Project
```bash
cd divyanshguvi
```

### 2. Set Up Gemini AI (Optional but Recommended)

#### Get API Key
Visit: https://makersuite.google.com/app/apikey
- Sign in with Google account
- Create a new API key (FREE)
- Copy the key

#### Run Setup Wizard
```bash
# Compile all files
javac *.java

# Run setup
java GeminiSetup
```

Follow the prompts to configure AI integration.

### 3. Test the Integration
```bash
# Run test program
java TestGeminiIntegration
```

### 4. Run the Application
```bash
# Run main login screen
java Login
```

---

## 📁 Project Structure

```
divyanshguvi/
├── Login.java                      # Login screen
├── Question.java                   # Question model
├── QuestionBank.java              # Question management (now with AI!)
├── Quiz.java                      # Quiz interface
├── QuizResult.java                # Results tracking
├── Rules.java                     # Quiz rules display
├── UserProfile.java               # User management
│
├── GeminiQuestionGenerator.java   # ⭐ NEW: AI question generator
├── Config.java                    # ⭐ NEW: Configuration management
├── SimpleJsonParser.java          # ⭐ NEW: JSON parser (no dependencies)
├── GeminiSetup.java               # ⭐ NEW: Setup wizard
├── TestGeminiIntegration.java     # ⭐ NEW: Test program
│
├── config.properties              # Configuration file (auto-generated)
├── profiles/                      # User profile data
└── questions/                     # Question database files
```

---

## 🔧 Configuration

### config.properties

Auto-generated on first run. Customize as needed:

```properties
# Gemini API Key (get from https://makersuite.google.com/app/apikey)
gemini.api.key=YOUR_API_KEY_HERE

# Enable/disable AI question generation
use.ai.questions=true

# Percentage of AI-generated questions (0-100)
# 0 = all database, 50 = half & half, 100 = all AI
ai.questions.percentage=50

# Cache AI questions for faster loading
cache.ai.questions=true
```

---

## 💻 Usage Examples

### Basic Usage in Code

```java
// Initialize the question bank
QuestionBank questionBank = new QuestionBank();

// Get questions (automatically mixes AI + database based on config)
List<Question> questions = questionBank.getQuestionsForDomain(
    "Java",      // Domain
    "Medium",    // Difficulty: Easy, Medium, or Hard
    10           // Number of questions
);

// Use the questions
for (Question q : questions) {
    System.out.println(q.getText());
    for (String option : q.getOptions()) {
        System.out.println("  - " + option);
    }
    System.out.println("Answer: " + q.getCorrectAnswer());
}
```

### Generate Questions for Custom Domain

```java
QuestionBank questionBank = new QuestionBank();

// Generate questions for ANY topic!
List<Question> questions = questionBank.getQuestionsForDomain(
    "Machine Learning",      // Custom domain
    "Hard",                  // Difficulty level
    15                       // Count
);

// Or pre-generate and cache questions
questionBank.generateAndCacheAiQuestions("Blockchain", "Medium", 20);
```

### Control AI Usage

```java
QuestionBank questionBank = new QuestionBank();

// Only use database questions (no AI)
List<Question> dbQuestions = questionBank.getQuestionsForDomain(
    "Math",
    "Easy",
    10,
    false    // useAI = false
);

// Enable/disable AI dynamically
questionBank.setUseAiQuestions(true);
questionBank.setAiQuestionsPercentage(75);  // 75% AI questions
```

---

## 🎯 How AI Integration Works

### Hybrid Question System

```
User requests 10 questions
         ↓
Config: 50% AI questions
         ↓
    ┌────┴────┐
    ↓         ↓
5 Database  5 AI-Generated
Questions   Questions
    ↓         ↓
    └────┬────┘
         ↓
   Shuffle & Display
```

### Smart Fallback

```
Need 10 questions
         ↓
Only 3 in database
         ↓
AI generates 7 more ← Automatic!
         ↓
10 total questions
```

---

## 📊 Benefits

| Feature | Without AI | With AI |
|---------|-----------|---------|
| **Question Pool** | Limited to database | Unlimited |
| **Custom Topics** | Manual creation needed | Instant generation |
| **Difficulty Control** | Pre-set questions | Dynamic adjustment |
| **Maintenance** | Update database regularly | Auto-generated fresh |
| **Scalability** | Limited by storage | Unlimited topics |

---

## 🛠️ Development

### Compile All Files
```bash
javac *.java
```

### Run Tests
```bash
java TestGeminiIntegration
```

### Run Setup
```bash
java GeminiSetup
```

### Run Application
```bash
java Login
```

---

## 📚 Documentation

- **Quick Start Guide**: [QUICK_START.md](QUICK_START.md)
- **Full AI Documentation**: [GEMINI_AI_GUIDE.md](GEMINI_AI_GUIDE.md)
- **Gemini API Docs**: https://ai.google.dev/docs

---

## 🔒 Security Notes

⚠️ **Important**: Never commit your API key to version control!

Add to `.gitignore`:
```
config.properties
*.dat
profiles/
```

For production:
- Use environment variables
- Rotate API keys regularly
- Monitor API usage

---

## ❓ FAQ

**Q: Is Gemini API free?**  
A: Yes! Google offers a generous free tier perfect for learning and development.

**Q: Do I need AI to use the app?**  
A: No! The app works perfectly with database questions only. AI is optional.

**Q: Can I generate questions for any topic?**  
A: Yes! AI can generate questions for virtually any subject you specify.

**Q: How fast is AI generation?**  
A: Typically 2-5 seconds for 10 questions. Cached questions load instantly.

**Q: What happens if API fails?**  
A: The app automatically falls back to database questions.

**Q: Can I mix AI and database questions?**  
A: Yes! That's the default mode. Configure the percentage in config.properties.

---

## 🐛 Troubleshooting

### Issue: "API key not configured"
**Solution**: Run `java GeminiSetup` or manually edit config.properties

### Issue: "Connection failed"
**Solutions**:
- Check internet connection
- Verify API key at https://makersuite.google.com/app/apikey
- Check API quota limits

### Issue: Questions not generating
**Solutions**:
- Ensure `use.ai.questions=true` in config
- Test connection: `java TestGeminiIntegration`
- Check console for error messages

### Issue: Slow performance
**Solutions**:
- Enable caching: `cache.ai.questions=true`
- Reduce AI percentage
- Pre-generate questions for popular domains

---

## 🤝 Contributing

Contributions are welcome! Areas for improvement:
- Additional question types
- More domain templates
- UI enhancements
- Mobile version
- Multi-language support

---

## 📝 License

This project is open source. The Gemini API is provided by Google LLC.

---

## 🙏 Acknowledgments

- **Google Gemini** for AI-powered question generation
- **Java** for the robust programming platform
- Original quiz application developers

---

## 📞 Support

For issues or questions:
1. Check the [troubleshooting section](#-troubleshooting)
2. Review [GEMINI_AI_GUIDE.md](GEMINI_AI_GUIDE.md)
3. Test with `java TestGeminiIntegration`
4. Check Gemini API status

---

## 🎓 Learn More

- [Gemini API Documentation](https://ai.google.dev/docs)
- [Java Documentation](https://docs.oracle.com/en/java/)
- [AI Question Generation Best Practices](https://ai.google.dev/docs/prompt_best_practices)

---

**Made with ❤️ and powered by AI**

---

## 🚀 Next Steps

1. ✅ Run `java GeminiSetup` to configure
2. ✅ Test with `java TestGeminiIntegration`
3. ✅ Generate your first AI questions
4. ✅ Start building amazing quizzes!

**Happy Coding! 🎉**
