import org.jdbi.v3.core.Jdbi;

/**
 * Database management class for flashcards using SQLite and JDBI
 * Provides CRUD operations for flashcard storage and retrieval
 */
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

    /**
     * Inserts a new flashcard into the database
     *
     * @param fc the flashcard to insert
     */
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

    /**
     * Deletes a flashcard from the database by its ID
     *
     * @param id the ID of the flashcard to delete
     */
    public void delete_flashcard(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM flashcards WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    /**
     * Updates an existing flashcard's question and answer
     *
     * @param id           the ID of the flashcard to update
     * @param new_question the new question text
     * @param new_answer   the new answer text
     */
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

    /**
     * Retrieves a flashcard by its ID
     *
     * @param id the ID of the flashcard to retrieve
     * @return the flashcard if found, null otherwise
     */
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

    /**
     * Retrieves all flashcard IDs from the database
     *
     * @return a list of all flashcard IDs, ordered by ID
     */
    public java.util.List<Integer> get_all_ids() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id FROM flashcards ORDER BY id")
                        .mapTo(Integer.class)
                        .list()
        );
    }

    /**
     * Retrieves a random selection of flashcards from the database
     *
     * @param limit the maximum number of flashcards to retrieve
     * @return a list of randomly selected flashcards
     */
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

    /**
     * Prints a single flashcard's information to the console by its ID
     *
     * @param id the ID of the flashcard to print
     */
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

    /**
     * Prints all flashcards in the database to the console
     * Each flashcard is displayed with ID, question, answer, and timestamps
     */
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

        db.update_flashcard(1, "Capitale de la France?", "Paris");

        db.delete_flashcard(2);

        System.out.println("\nAfter updates:");
        db.print_all_flashcards();

        System.out.println(db.get_flashcard_by_id(1));
    }
}
