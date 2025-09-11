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

    public Flashcard get_flashcard_by_id(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT question, answer, created_on, updated_on FROM flashcards WHERE id = :id")
                        .bind("id", id)
                        .map((rs, _) -> new Flashcard(
                                rs.getString("question"),
                                rs.getString("answer"),
                                java.time.LocalDateTime.parse(rs.getString("created_on")),
                                java.time.LocalDateTime.parse(rs.getString("updated_on"))
                        ))
                        .findFirst()
                        .orElse(null)
        );
    }


    public java.util.List<Integer> get_all_ids() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id FROM flashcards ORDER BY id")
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public java.util.List<Flashcard> get_random_flashcards(int limit) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT question, answer, created_on, updated_on FROM flashcards ORDER BY RANDOM() LIMIT :limit")
                        .bind("limit", limit)
                        .map((rs, _) -> new Flashcard(
                                rs.getString("question"),
                                rs.getString("answer"),
                                java.time.LocalDateTime.parse(rs.getString("created_on")),
                                java.time.LocalDateTime.parse(rs.getString("updated_on"))
                        ))
                        .list()
        );
    }


    public void print_flashcard_by_id(int id) {
        Flashcard fc = get_flashcard_by_id(id);
        if (fc != null) {
            System.out.printf("%d | %s -> %s | %s | %s%n",
                    id,
                    fc.get_question(),
                    fc.get_answer(),
                    fc.get_created_on().toString(),
                    fc.get_updated_on().toString()
            );
        } else {
            System.out.println("Error: no flashcard found with ID " + id);
        }
    }



    public void print_all_flashcards() {
        jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, question, answer, created_on, updated_on FROM flashcards")
                        .map((rs, _) -> String.format(
                                "%d | %s -> %s | %s | %s",
                                rs.getInt("id"),
                                rs.getString("question"),
                                rs.getString("answer"),
                                rs.getString("updated_on"),
                                rs.getString("created_on")
                        ))
                        .list()
        ).forEach(System.out::println);
    }


    public static void main(String[] args) {
        new java.io.File("database").mkdirs();

        FlashcardDatabase db = new FlashcardDatabase("jdbc:sqlite:./database/db.sqlite");

        Flashcard fc1 = new Flashcard("Capital of France?", "Paris");
        Flashcard fc2 = new Flashcard("Capital of Germany?", "Berlin");

        db.insert_flashcard(fc1);
        db.insert_flashcard(fc2);

        System.out.println("All flashcards:");
        db.print_all_flashcards();

        // Mettre à jour une flashcard
        db.update_flashcard(1, "Capitale de la France?", "Paris");

        // Supprimer une flashcard
        db.delete_flashcard(2);

        System.out.println("\nAfter updates:");
        db.print_all_flashcards();

        System.out.println(db.get_flashcard_by_id(1));
    }
}
