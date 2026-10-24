package kanza15;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class kanza15 {

    static String[] cards = {
            "1 pumpkin", "2 pumpkin", "3 pumpkin", "4 pumpkin", "5 pumpkin", "6 pumpkin",
            "7 pumpkin", "10 pumpkin", "11 pumpkin", "12 pumpkin",

            "1 gold", "2 gold", "3 gold", "4 gold", "5 gold", "6 gold",
            "7 gold", "10 gold", "11 gold", "12 gold",

            "1 sword", "2 sword", "3 sword", "4 sword", "5 sword", "6 sword",
            "7 sword", "10 sword", "11 sword", "12 sword",

            "1 plate", "2 plate", "3 plate", "4 plate", "5 plate", "6 plate",
            "7 plate", "10 plate", "11 plate", "12 plate"
    };

    static Random random = new Random();

    static String language = "en";

    static String[] player1CollectedCards = new String[50];
    static String[] player2CollectedCards = new String[50];

    static int player1CollectedCount = 0;
    static int player2CollectedCount = 0;

    public static void main(String[] args) {

        final String BLUE = "\033[0;34m";
        final String GREEN = "\033[0;32m";
        final String RESET = "\033[0m";
        final String RED = "\033[0;31m";
        final String YELLOW = "\033[0;33m";

        System.out.println(BLUE +
                "  _  __                          __ _____ \r\n"
                + " | |/ /                         /_ | ____|\r\n"
                + " | ' / __ _ _ __  ______ _ ______| | |__  \r\n"
                + " |  < / _` | '_ \\|_  / _` |______| |___ \\ \r\n"
                + " | . \\ (_| | | | |/ / (_| |      | |___) |\r\n"
                + " |_|\\_\\__,_|_| |_/___\\__,_|      |_|____/ "
                + RESET);

        Scanner scan = new Scanner(System.in);

        // Language selection
        System.out.println(YELLOW + "\n\nChoose a language / Choisissez une langue:");
        System.out.println("1. English");
        System.out.println("2. Français");

        int choice = 0;
        boolean validChoice = false;

        while (!validChoice) {

            System.out.print("Enter the number of your choice: " + RESET);

            try {
                choice = Integer.parseInt(scan.nextLine().trim());

                if (choice == 1 || choice == 2) {
                    validChoice = true;
                } else {
                    System.out.println(RED +
                            "Invalid choice. Please enter 1 or 2."
                            + RESET);
                }

            } catch (NumberFormatException e) {
                System.out.println(RED +
                        "Invalid input. Please enter 1 or 2."
                        + RESET);
            }
        }

        if (choice == 2) {
            language = "fr";
        } else {
            language = "en";
        }

        // Player names
        String player1;
        String player2;

        if (language.equals("fr")) {
            System.out.print(YELLOW +
                    "➜ Entrez le nom du joueur 1: " + RESET);
        } else {
            System.out.print(YELLOW +
                    "➜ Enter name for Player 1: " + RESET);
        }

        player1 = scan.nextLine();

        if (language.equals("fr")) {
            System.out.print(YELLOW +
                    "➜ Entrez le nom du joueur 2: " + RESET);
        } else {
            System.out.print(YELLOW +
                    "➜ Enter name for Player 2: " + RESET);
        }

        player2 = scan.nextLine();

        // Shuffle cards
        String[] shuffledCards = shuffleArray(cards);

        String[] player1Cards = new String[4];
        String[] player2Cards = new String[4];

        String[] tableCards;

        // Deal initial cards
        dealCards(player1Cards, shuffledCards);
        dealCards(player2Cards, shuffledCards);

        boolean gameActive = true;

        while (gameActive) {

            // Draw table cards
            tableCards = drawCards(shuffledCards);

            // Player 1 turn
            playTurn(
                    player1Cards,
                    tableCards,
                    shuffledCards,
                    player1,
                    player1CollectedCards
            );

            // Player 2 turn
            playTurn(
                    player2Cards,
                    tableCards,
                    shuffledCards,
                    player2,
                    player2CollectedCards
            );

            // Check if cards remain
            gameActive = Arrays.stream(shuffledCards)
                    .anyMatch(card -> card != null);
        }

        // Final results
        calculateWinner(
                player1,
                player1CollectedCards,
                player2,
                player2CollectedCards
        );

        if (language.equals("fr")) {
            System.out.println(
                    "\nLa partie est terminée ! Il n'y a plus de cartes."
            );
        } else {
            System.out.println(
                    "\nGame over! No more cards left."
            );
        }

        scan.close();
    }


    // Shuffle cards
    private static String[] shuffleArray(String[] array) {

        for (int i = 0; i < array.length; i++) {

            int j = random.nextInt(array.length);

            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return array;
    }


    // Get card number
    private static int getCardValue(String card) {

        String[] cardParts = card.split(" ");

        return Integer.parseInt(cardParts[0]);
    }


    // Deal four cards
    private static void dealCards(
            String[] playerCards,
            String[] shuffledCards) {

        for (int i = 0; i < 4; i++) {

            if (shuffledCards[i] != null) {
                playerCards[i] = shuffledCards[i];
            }
        }

        int remainingLength = shuffledCards.length - 4;

        System.arraycopy(
                shuffledCards,
                4,
                shuffledCards,
                0,
                remainingLength
        );

        for (int i = remainingLength;
             i < shuffledCards.length;
             i++) {

            shuffledCards[i] = null;
        }
    }


    // Draw four cards for the table
    private static String[] drawCards(String[] shuffledCards) {

        String[] tableCards = new String[4];

        for (int i = 0;
             i < 4 && i < shuffledCards.length;
             i++) {

            tableCards[i] = shuffledCards[i];
        }

        int remainingLength = shuffledCards.length - 4;

        if (remainingLength > 0) {

            System.arraycopy(
                    shuffledCards,
                    4,
                    shuffledCards,
                    0,
                    remainingLength
            );
        }

        for (int i = remainingLength;
             i < shuffledCards.length;
             i++) {

            shuffledCards[i] = null;
        }

        return tableCards;
    }


    // Remove a card
    private static String[] removeCard(
            String[] cards,
            String card,
            String[] shuffledCards) {

        int index = -1;

        for (int i = 0; i < cards.length; i++) {

            if (cards[i] != null &&
                    cards[i].equals(card)) {

                index = i;
                break;
            }
        }

        if (index == -1) {
            return cards;
        }

        if (shuffledCards[0] != null) {

            cards[index] = shuffledCards[0];

            System.arraycopy(
                    shuffledCards,
                    1,
                    shuffledCards,
                    0,
                    shuffledCards.length - 1
            );

            shuffledCards[shuffledCards.length - 1] = null;
        }

        return cards;
    }


    // Display ASCII cards
    public static String renderAsciiCards(String[] playerCards) {

        if (playerCards == null ||
                playerCards.length == 0) {

            if (language.equals("fr")) {
                throw new IllegalArgumentException(
                        "Le tableau de cartes des joueurs ne peut pas être nul ou vide."
                );
            } else {
                throw new IllegalArgumentException(
                        "playerCards array cannot be null or empty."
                );
            }
        }

        StringBuilder asciiCards = new StringBuilder();

        // 7 rows for each card
        StringBuilder[] cardRows = new StringBuilder[7];

        for (int i = 0; i < 7; i++) {
            cardRows[i] = new StringBuilder();
        }

        for (String card : playerCards) {

            if (card == null || !card.contains(" ")) {

                if (language.equals("fr")) {
                    asciiCards.append(
                            "Format de carte invalide : "
                    );
                } else {
                    asciiCards.append(
                            "Invalid card format: "
                    );
                }

                asciiCards
                        .append(card == null ? "null" : card)
                        .append("\n");

                continue;
            }

            String[] parts = card.split(" ");

            if (parts.length < 2) {

                if (language.equals("fr")) {
                    asciiCards.append(
                            "Format de carte invalide : "
                    );
                } else {
                    asciiCards.append(
                            "Invalid card format: "
                    );
                }

                asciiCards
                        .append(card)
                        .append("\n");

                continue;
            }

            String number = parts[0];
            String type = parts[1];

            String top = "┌──────────┐";
            String middle1 =
                    String.format(
                            "│ %-2s       │",
                            number
                    );

            String middle2 = "│          │";

            String middle3 =
                    String.format(
                            "│ %-9s│",
                            type
                    );

            String bottom =
                    String.format(
                            "│       %-2s │",
                            number
                    );

            String bottomEnd = "└──────────┘";

            cardRows[0].append(top).append(" ");
            cardRows[1].append(middle1).append(" ");
            cardRows[2].append(middle2).append(" ");
            cardRows[3].append(middle3).append(" ");
            cardRows[4].append(middle2).append(" ");
            cardRows[5].append(bottom).append(" ");
            cardRows[6].append(bottomEnd).append(" ");
        }

        for (StringBuilder row : cardRows) {

            asciiCards
                    .append(row.toString().trim())
                    .append("\n");
        }

        return asciiCards.toString();
    }


    // Player turn
    private static void playTurn(
            String[] playerCards,
            String[] tableCards,
            String[] shuffledCards,
            String playerName,
            String[] collectedCards) {

        Scanner scanner = new Scanner(System.in);

        // Player cards
        if (language.equals("fr")) {
            System.out.println(
                    playerName +
                    " cartes : " +
                    Arrays.toString(playerCards)
            );
        } else {
            System.out.println(
                    playerName +
                    "'s cards: " +
                    Arrays.toString(playerCards)
            );
        }

        System.out.println(renderAsciiCards(playerCards));

        // Table cards
        if (language.equals("fr")) {
            System.out.println(
                    "Cartes sur la table : " +
                    Arrays.toString(tableCards)
            );
        } else {
            System.out.println(
                    "Table cards: " +
                    Arrays.toString(tableCards)
            );
        }

        System.out.println(renderAsciiCards(tableCards));

        int currentSum = 0;

        String[] selectedCards = new String[3];

        int selectedCount = 0;

        // Instructions
        if (language.equals("fr")) {

            System.out.println(
                    "Entrez les cartes que vous souhaitez utiliser " +
                    "pour atteindre 15 points " +
                    "(choisissez jusqu'à 3 cartes) :"
            );

        } else {

            System.out.println(
                    "Enter the cards you want to use to reach 15 points " +
                    "(choose up to 3 cards):"
            );
        }


        while (currentSum < 15 &&
                selectedCount < 3) {

            // Ask for card
            if (language.equals("fr")) {

                System.out.print(
                        "Choisissez une carte de votre main ou de la table " +
                        "(ou tapez 'suivant' pour passer) : "
                );

            } else {

                System.out.print(
                        "Choose a card from your hand or the table " +
                        "(or type 'next' to skip): "
                );
            }

            String chosenCard = scanner.nextLine();

            // Skip turn
            if (language.equals("fr")) {

                if (chosenCard.equalsIgnoreCase("suivant")) {
                    break;
                }

            } else {

                if (chosenCard.equalsIgnoreCase("next")) {
                    break;
                }
            }


            boolean isValid = false;

            // Check player's cards
            for (String card : playerCards) {

                if (card != null &&
                        card.equals(chosenCard)) {

                    selectedCards[selectedCount] =
                            chosenCard;

                    currentSum +=
                            getCardValue(chosenCard);

                    if (language.equals("fr")) {

                        System.out.println(
                                "Carte ajoutée : " +
                                chosenCard +
                                ", Total actuel : " +
                                currentSum
                        );

                    } else {

                        System.out.println(
                                "Card added: " +
                                chosenCard +
                                ", Current Total: " +
                                currentSum
                        );
                    }

                    selectedCount++;

                    isValid = true;

                    break;
                }
            }


            // Check table cards
            if (!isValid) {

                for (String card : tableCards) {

                    if (card != null &&
                            card.equals(chosenCard)) {

                        selectedCards[selectedCount] =
                                chosenCard;

                        currentSum +=
                                getCardValue(chosenCard);

                        if (language.equals("fr")) {

                            System.out.println(
                                    "Carte ajoutée : " +
                                    chosenCard +
                                    ", Total actuel : " +
                                    currentSum
                            );

                        } else {

                            System.out.println(
                                    "Card added: " +
                                    chosenCard +
                                    ", Current Total: " +
                                    currentSum
                            );
                        }

                        selectedCount++;

                        isValid = true;

                        break;
                    }
                }
            }


            // Invalid card
            if (!isValid) {

                if (language.equals("fr")) {

                    System.out.println(
                            "Carte invalide. Essayez encore."
                    );

                } else {

                    System.out.println(
                            "Invalid card. Try again."
                    );
                }
            }


            // Reached 15
            if (currentSum == 15) {

                if (language.equals("fr")) {

                    System.out.println(
                            playerName +
                            " a atteint 15 ! " +
                            "Collectez vos points.\n\n\n"
                    );

                } else {

                    System.out.println(
                            playerName +
                            " reached 15! " +
                            "Collect your points.\n\n\n"
                    );
                }

                break;
            }
        }


        // Collect cards
        if (currentSum == 15) {

            for (String card : selectedCards) {

                if (card != null) {

                    if (playerName.equals("Player 1")) {

                        collectedCards[
                                player1CollectedCount++
                        ] = card;

                    } else {

                        collectedCards[
                                player2CollectedCount++
                        ] = card;
                    }

                    playerCards =
                            removeCard(
                                    playerCards,
                                    card,
                                    shuffledCards
                            );

                    tableCards =
                            removeCard(
                                    tableCards,
                                    card,
                                    shuffledCards
                            );
                }
            }

        } else {

            if (language.equals("fr")) {

                System.out.println(
                        playerName +
                        " n'a pas atteint 15. " +
                        "Passage du tour."
                );

            } else {

                System.out.println(
                        playerName +
                        " could not reach 15. " +
                        "Skipping turn."
                );
            }
        }
    }


    // Calculate winner
    private static void calculateWinner(
            String player1,
            String[] player1CollectedCards,
            String player2,
            String[] player2CollectedCards) {

        int player1Points =
                calculatePoints(player1CollectedCards);

        int player2Points =
                calculatePoints(player2CollectedCards);


        System.out.println();

        if (language.equals("fr")) {

            System.out.println("Statistiques des points finaux :");

            System.out.println(
                    player1 +
                    " a marqué : " +
                    player1Points +
                    " points"
            );

            System.out.println(
                    player2 +
                    " a marqué : " +
                    player2Points +
                    " points"
            );

        } else {

            System.out.println("Final Points Stats:");

            System.out.println(
                    player1 +
                    " scored: " +
                    player1Points +
                    " points"
            );

            System.out.println(
                    player2 +
                    " scored: " +
                    player2Points +
                    " points"
            );
        }


        // Winner
        if (player1Points > player2Points) {

            if (language.equals("fr")) {

                System.out.println(
                        "\nGagnant : " +
                        player1
                );

            } else {

                System.out.println(
                        "\nWinner: " +
                        player1
                );
            }

        } else if (player2Points > player1Points) {

            if (language.equals("fr")) {

                System.out.println(
                        "\nGagnant : " +
                        player2
                );

            } else {

                System.out.println(
                        "\nWinner: " +
                        player2
                );
            }

        } else {

            if (language.equals("fr")) {

                System.out.println(
                        "\nC'est une égalité !"
                );

            } else {

                System.out.println(
                        "\nIt's a tie!"
                );
            }
        }
    }


    // Calculate points
    private static int calculatePoints(
            String[] collectedCards) {

        int totalPoints = 0;

        for (String card : collectedCards) {

            if (card == null) {
                continue;
            }

            String[] parts = card.split(" ");

            int cardValue =
                    Integer.parseInt(parts[0]);

            String cardType = parts[1];


            if (cardType.equals("gold")) {

                if (cardValue == 1) {

                    totalPoints += 20;

                } else {

                    totalPoints += cardValue;
                }

            } else {

                totalPoints += 1;
            }
        }

        return totalPoints;
    }
}