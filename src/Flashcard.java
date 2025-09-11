import java.time.LocalDateTime;

/**
 * Flashcard made of a question and an answer
 */
public class Flashcard {
    private String question;
    private String answer;
    private final LocalDateTime created_on;
    private LocalDateTime updated_on;

    /**
     * Creates a new flashcard with a question and an answer
     *
     * @param question flashcard question
     * @param answer flashcard answer
     */
    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.created_on = LocalDateTime.now();
    }

    // Accessors

    public String get_question() {
        return question;
    }

    public String get_answer() {
        return answer;
    }

    public LocalDateTime get_created_on() {
        return created_on;
    }

    public LocalDateTime get_updated_on() {
        return updated_on;
    }

    // Mutators

    public void set_question(String question) {
        this.question = question;
    }

    public void set_answer(String answer) {
        this.answer = answer;
    }

    public void set_updated_on(LocalDateTime date) {
        this.updated_on = date;
    }



}
