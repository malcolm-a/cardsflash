import java.util.Date;

/**
 * Flashcard made of a question and an answer
 */
public class Flashcard {
    private String question;
    private String answer;
    private Date date;

    /**
     * Creates a new flashcard with a question and an answer
     *
     * @param question flashcard question
     * @param answer flashcard answer
     */
    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Accessors

    public String get_question() {
        return question;
    }

    public String get_answer() {
        return answer;
    }

    public Date get_date() {
        return date;
    }

    // Mutators

    public void set_question(String question) {
        this.question = question;
    }

    public void set_answer(String answer) {
        this.answer = answer;
    }

    public void set_date(Date date) {
        this.date = date;
    }

}
