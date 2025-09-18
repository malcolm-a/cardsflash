# CardsFlash!

<!-- TOC -->

  * [📖 About](#-about)
  * [🛠️ Installation](#-installation)
  * [👤 Usage](#-usage)
    * [Menu](#menu)
    * [New card](#new-card)
    * [Edit card](#edit-card)
    * [Delete card](#delete-card)
    * [Random Cards](#random-cards)
    * [All cards](#all-cards)
    * [Quit](#quit)
<!-- TOC -->

## 📖 About

Cardsflash! is a simple flashcard application for your terminal. It allows you to create, review, and manage flashcards using a straightforward command-line interface. 

## 🛠️ Installation

To install Cardsflash!, you need to have Java installed on your machine. You can download the latest version of Java from [here](https://www.java.com/en/download/).

1. Clone the repository:
   ```bash
   git clone https://github.com/malcolm-a/cardsflash.git
   ```
   
2. Navigate to the project directory:
   ```bash
    cd cardsflash
    ```
   
3. Compile the Java files:
   ```bash
    javac -cp "lib/" -d out src/.java
   ```
   
4. Run the application:
   ```bash
   java -cp "out:lib/*" Main
   ```

## 👤 Usage

### Menu

Once the application is running, you will be presented with the following menu:

```
====== CARDSFLASH ======

______________________________

(N)ew card
(E)dit card
(D)elete card
(R)andom Cards
(A)ll cards
e(X)port to JSON
(Q)uit
```

Simply enther the letter corresponding to the action you wish to perform.

### New card

To create a new flashcard, select the `(N)ew card` option. You will be prompted to enter the question and answer for the flashcard.

### Edit card

To edit an existing flashcard, select the `(E)dit card` option. You will be prompted to enter the ID of the card you wish to edit, followed by the new question and answer.

### Delete card

To delete a flashcard, select the `(D)elete card` option. You will be prompted to enter the ID of the card you wish to delete.

### Random Cards

To review flashcards in random order, select the `(R)andom Cards` option. You will be shown a sequence of 10 random cards to review. Press Enter to see the answer and then press Enter again to review the next card.

### All cards

To view all flashcards, select the `(A)ll cards` option. You will be presented with a list of all your flashcards with their IDs and timestamps.

### Export to JSON

To export your flashcards to a JSON file, select the `(X)port to JSON` option. You will be prompted to enter the filename for the exported JSON file.

### Quit

To exit the application, select the `(Q)uit` option.

