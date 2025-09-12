import java.util.Scanner;

/**
 * Main app logic interface
 */
public class CardsApp {
    private final Scanner scanner;
    private final FlashcardDatabase db;

    public CardsApp() {
        scanner = new Scanner(System.in);
        db = new FlashcardDatabase("jdbc:sqlite:./database/db.sqlite");
    }

    /**
     * Menu display for operation choice
     */
    public void menu() {
        System.out.println("\n" + "_".repeat(30) + "\n");
        System.out.println("(N)ew card");
        System.out.println("(E)dit card");
        System.out.println("(D)elete card");
        System.out.println("(R)andom Cards");
        System.out.println("(A)ll cards");
        System.out.println("(Q)uit");
        System.out.print("\n>");
    }


    /**
     * Main logic loop
     */
    public void run() {
        System.out.println("====== CARDSFLASH ======");
        menu();
        boolean running = true;
        while (running) {
            switch (scanner.nextLine()) {
                case "N", "n" -> new_card();
                case "E", "e" -> edit_card();
                case "D", "d" -> delete_card();
                case "R", "r" -> random_cards();
                case "A", "a" -> all_cards();
                case "Q", "q" -> running = false;
            }
        }
    }

    /**
     * Card creation interface
     */
    public void new_card() {
        System.out.println("\n--- New Card ---");
        System.out.print("Question\n>");
        String question = scanner.nextLine();
        System.out.print("\nAnswer\n>");
        String answer = scanner.nextLine();
        this.db.insert_flashcard(new Flashcard(question, answer));
        System.out.println("Successfully created new card!");
        menu();
    }

    /**
     * Card update interface
     */
    public void edit_card() {
        System.out.println("\n--- Edit Card ---");
        System.out.println("ID\n>");
        int id = Integer.parseInt(scanner.nextLine());
        db.print_flashcard_by_id(id);
        if (db.get_all_ids().contains(id)) {
            System.out.println("\n--- New Card ---");
            System.out.print("Question\n>");
            String question = scanner.nextLine();
            System.out.print("\nAnswer\n>");
            String answer = scanner.nextLine();
            db.update_flashcard(id, question, answer);
        }
        menu();
    }

    /**
     * Card deletion interface
     */
    public void delete_card() {
        System.out.println("\n--- Delete Card ---");
        System.out.println("ID\n>");
        int id = Integer.parseInt(scanner.nextLine());
        db.delete_flashcard(id);
        System.out.println("Successfully deleted card!");
        menu();
    }

    /**
     * Displays a random sequence of 10 cards
     */
    public void random_cards() {
        System.out.println("\n--- Random Cards ---");

        var random_cards = db.get_random_flashcards(10);

        if (random_cards.isEmpty()) {
            System.out.println("No cards available.");
            menu();
            return;
        }

        System.out.println("\nPress Enter to see answer, then Enter for next card...");

        for (int i = 0; i < random_cards.size(); i++) {
            var card = random_cards.get(i);
            System.out.println("\n" + "_".repeat(30));
            System.out.println("Card " + (i + 1) + "/" + random_cards.size());
            System.out.print("Question: " + card.get_question());
            scanner.nextLine();

            System.out.println("Answer: " + card.get_answer());

            if (i < random_cards.size() - 1) {
                System.out.print("\nPress Enter");
                scanner.nextLine();
            }
        }

        System.out.println("\nSequence completed!");
        menu();
    }


    /**
     * Shows all cards information
     */
    public void all_cards() {
        db.print_all_flashcards();
        menu();
    }



}
