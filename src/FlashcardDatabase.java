import org.jdbi.v3.core.Jdbi;

public class FlashcardDatabase {
    private final Jdbi jdbi;

    public FlashcardDatabase(String db_url) {
        this.jdbi = Jdbi.create(db_url);
        create_table_if_not_exists();
    }

    private void create_table_if_not_exists() {
        jdbi.useHandle(handle ->
                handle.execute("""
                CREATE TABLE IF NOT EXISTS flashcards (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    question TEXT NOT NULL,
                    answer TEXT NOT NULL,
                    created_on TEXT NOT NULL,
                    updated_on TEXT NOT NULL
                )
            """)
        );
    }

    public void insert_flashcard(Flashcard fc) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                INSERT INTO flashcards (question, answer, created_on, updated_on)
                VALUES (:q, :a, :c, :u)
            """)
                        .bind("q", fc.get_question())
                        .bind("a", fc.get_answer())
                        .bind("c", fc.get_created_on().toString())
                        .bind("u", fc.get_updated_on().toString())
                        .execute()
        );
    }

    public void delete_flashcard(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM flashcards WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    public void update_flashcard(int id, String new_question, String new_answer) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                UPDATE flashcards 
                SET question = :q, answer = :a, updated_on = :u 
                WHERE id = :id
            """)
                        .bind("q", new_question)
                        .bind("a", new_answer)
                        .bind("u", java.time.LocalDateTime.now().toString())
                        .bind("id", id)
                        .execute()
        );
    }

    public void get_all_flashcards_with_ids() {
        jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, question, answer FROM flashcards")
                        .map((rs, _) -> String.format("ID: %d | %s -> %s",
                                rs.getInt("id"),
                                rs.getString("question"),
                                rs.getString("answer")))
                        .list()
        ).forEach(System.out::println);
    }

    public void print_all_flashcards() {
        jdbi.withHandle(handle ->
                handle.createQuery("SELECT question, answer, created_on, updated_on FROM flashcards")
                        .map((rs, _) -> new Flashcard(
                                rs.getString("question"),
                                rs.getString("answer")
                        ))
                        .list()
        ).forEach(fc -> System.out.println(fc.get_question() + " -> " + fc.get_answer()));
    }

    public static void main(String[] args) {
        new java.io.File("database").mkdirs();

        FlashcardDatabase db = new FlashcardDatabase("jdbc:sqlite:./database/db.sqlite");

        Flashcard fc1 = new Flashcard("Capital of France?", "Paris");
        Flashcard fc2 = new Flashcard("Capital of Germany?", "Berlin");

        db.insert_flashcard(fc1);
        db.insert_flashcard(fc2);

        System.out.println("All flashcards:");
        db.get_all_flashcards_with_ids();

        // Mettre à jour une flashcard
        db.update_flashcard(1, "Capitale de la France?", "Paris");

        // Supprimer une flashcard
        db.delete_flashcard(2);

        System.out.println("\nAfter updates:");
        db.get_all_flashcards_with_ids();
    }
}
