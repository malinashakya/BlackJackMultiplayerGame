package blackjackmultiplayergame;

import blackjackmultiplayergame.game_type.GameTypeAJQKare10;
import blackjackmultiplayergame.game_type.GameType;
import blackjackmultiplayergame.game_type.GameTypeJQKare10;
import blackjackmultiplayergame.game_type.GameTypeSumIsLessThan17;
import blackjackmultiplayergame.game_type.GameTypeSumIsLessThan21;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BlackJackMultiplayerGame {

    public static Random random = new Random();

    public static String display(int card) {
        char[] temp_card = new char[3];
        temp_card[2] = '\0';
        if (card == 11) {
            temp_card[0] = 'J';
        } else if (card == 12) {
            temp_card[0] = 'Q';
        } else if (card == 13) {
            temp_card[0] = 'K';
        } else if (card == 1) {
            temp_card[0] = 'A';
        } else if (card >= 2 && card < 10) {
            temp_card[0] = (char) (card + 48);
        } else if (card == 10) {
            temp_card[0] = '1';
            temp_card[1] = '0';
        }
        return new String(temp_card);
    }

    static int playgame(int betPoint, int card1, int card2, Player player, GameRecord[] records, int round,
            GameType gametype) {
        int card3, sum;
        card3 = random.nextInt(13) + 1;
        sum = gametype.calculate(card1, card2, card3);
        records[round] = new GameRecord();
        records[round].card1 = card1;
        records[round].card2 = card2;
        records[round].card3 = card3;
        records[round].betPoint = betPoint;

        System.out.println("Your third card is :" + display(card3));
        System.out.println("Your sum is:" + sum);
        if (gametype.evaluate(card1, card2, card3) == 0) {
            player.balance -= betPoint;
            records[round].result = -betPoint;
            System.out.println("You lose " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        } else {
            player.balance += betPoint;
            records[round].result = betPoint;
            System.out.println("You win " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        }
        records[round].balance = player.balance;
        return player.balance;
    }

    static void displayHistory(GameRecord[] records, int rounds, Player[] players, int numPlayers) {
        try (Writer writer = new FileWriter("game_history.txt")) {
            writer.write("Name,Round,Card1,Card2,Card3,Bet,Result,Balance\n");
            System.out.println("Name,Round,Card1,Card2,Card3,Bet,Result,Balance");
            int count = 0, j = 0;
            for (int i = 0; i < rounds; i++) {
                if (i % numPlayers == 0) {
                    count++;
                }
                j++;
                if (i % numPlayers == 0) {
                    j = 0;
                }
                if (players[j].balance == 0) {
                    String message = players[j].name + "," + count + ","
                            + display(records[i].card1) + ","
                            + display(records[i].card2) + ","
                            + display(records[i].card3) + ","
                            + records[i].betPoint + ","
                            + records[i].result + ","
                            + records[i].balance;
                    System.out.println(message);
                    writer.write(message + "\n");
                } else {
                    String message = players[j].name + "," + count + ","
                            + display(records[i].card1) + ","
                            + display(records[i].card2) + ","
                            + display(records[i].card3) + ","
                            + records[i].betPoint + ","
                            + records[i].result + ","
                            + records[i].balance;
                    System.out.println(message);
                    writer.write(message + "\n");
                }
            }
            System.out.println("Game history has been saved to game_history.txt");
        } catch (IOException e) {
            System.err.println("Error writing game history to file: " + e.getMessage());
        }
    }

//      static int countUniqueNames(ArrayList<String> names) {
//        ArrayList<String> uniqueNames = new ArrayList<>();
//
//        for (String name : names) {
//            if (!uniqueNames.contains(name)) {
//                uniqueNames.add(name);
//            }
//        }
//
//        return uniqueNames.size();
//    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to resume the previous game? (y/n): ");
        char resumeResponse = scanner.next().charAt(0);
        if (resumeResponse == 'y') {
            try (BufferedReader reader = new BufferedReader(new FileReader("game_history.txt"))) {
                String line;
                  int round=0;
                boolean startReading = false;
                ArrayList<String> uniqueNames = new ArrayList<>(); // ArrayList to store unique player names

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Name,Round,Card1,Card2,Card3,Bet,Result,Balance")) {
                        startReading = true;
                        continue;
                    }
                    if (startReading) {
                      
                        String[] parts = line.split(",");
                        if (parts.length == 8) {
                            String playerName = parts[0];
                            round=Integer.parseInt(parts[1]);
                            // Add the player name to the ArrayList only if it's not already present
                            if (!uniqueNames.contains(playerName)) {
                                uniqueNames.add(playerName);
                            }
                        }
                    }
                }
                System.out.println("Unique player names:");
                for (String name : uniqueNames) {
                    System.out.println(name);
                }

                // Count unique names
                int uniqueCount = uniqueNames.size();
                System.out.println("Number of unique players: " + uniqueCount+ "round played "+round);
            } catch (IOException e) {
                System.err.println("Error reading game history: " + e.getMessage());
            }
        } else if (resumeResponse == 'n') {
            System.out.println("Starting a new game.");
            System.out.println("Welcome to the game of Blackjack!");

            int condition;
            GameType gametype = null;

            System.out.println("Select the game condition:");
            System.out.println("1. Win if sum is less than 21");
            System.out.println("2. Win if sum is less than 17");
            System.out.println("3. Win if J, K, Q, A and sum is less than 21");
            System.out.println("4. Win if J, K, Q are equal to 10, A is equal to 1 and sum is less than 21");
            condition = scanner.nextInt();

            switch (condition) {
                case 1:
                    gametype = new GameTypeSumIsLessThan21();
                    break;
                case 2:
                    gametype = new GameTypeSumIsLessThan17();
                    break;
                case 3:
                    gametype = new GameTypeAJQKare10();
                    break;
                case 4:
                    gametype = new GameTypeJQKare10();
                    break;
                default:
                    System.out.println("Wrong selection of game");
                    return;
            }

            int numPlayers;
            System.out.print("Enter the number of players (1-17): ");
            numPlayers = scanner.nextInt();

            if (numPlayers < 1 || numPlayers > 17) {
                System.out.println("Invalid number of players. Exiting...");
                return;
            }
            int startingPoints = 100;
            Player[] players = new Player[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                System.out.print("Enter name of Player " + (i + 1) + ": ");
                String playerName = scanner.next();
                players[i] = new Player(playerName, startingPoints);
            }

            System.out.println("Names of players:");
            for (int i = 0; i < numPlayers; i++) {
                System.out.println("Player " + (i + 1) + ": " + players[i].name);
            }

            int betPoint, card1, card2, rounds = 0;
            GameRecord[] records = new GameRecord[100];

            // Array to store player balances
            int[] playerBalances = new int[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerBalances[i] = startingPoints;
            }

            System.out.println("\nYou each have 100 points in your accounts.");

            // Main game loop
            while (true) {
                // Loop through each player
                for (int player = 0; player < numPlayers; player++) {
                    if (playerBalances[player] <= 0) {
                        System.out.println("Player " + players[player].name + " is out of the game.");
                        continue;
                    }

                    card1 = random.nextInt(13) + 1;
                    card2 = random.nextInt(13) + 1;

                    System.out.println("\nPlayer " + players[player].name + ", your cards are " + display(card1)
                            + " and " + display(card2));
                    System.out.println("How many points do you want to bet?");

                    betPoint = scanner.nextInt();
                    if (betPoint > 0 && betPoint <= playerBalances[player]) {
                        playerBalances[player] = playgame(betPoint, card1, card2, players[player], records, rounds, gametype);
                        rounds++;
                    } else if (betPoint <= 0) {
                        System.out.println("Bet point cannot be negative or zero");
                    } else {
                        System.out.println("Player " + players[player].name + ", you don't have enough points");
                    }
                }

                System.out.print("\nDo you want to continue to the next round? (y/n): ");
                char response = scanner.next().charAt(0);
                if (response == 'n') {
                    break; // Exit the game if the user does not want to continue
                } else if (response != 'y') {
                    System.out.println("Wrong input");
                }
            }

            displayHistory(records, rounds, players, numPlayers);

            System.out.println("Game over!!!");
        }
    }
}
