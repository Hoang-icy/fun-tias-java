/* Hoang Le 
 * March 2, 2020
 * Homework 3: Problem 2
 * Original Nim Game with MiniMax algorithm
 * Start with 13; whoever takes the last coin loses
 * Use recursive methods findBestMove() and evaluatePosition() from author
 */

import java.util.ArrayList;
import java.util.Scanner;

public class NimClient {

  // Constants and Instance Variables
  public static final int WINNING_POSITION = 10;
  public static final int LOSING_POSITION = -10;
  public static final int MAX_DEPTH = 10;

  private static final int MAX_MOVE = 3;
  private static final int STARTING_COINS = 13;
  private static final Player STARTING_PLAYER = Player.HUMAN;

  private int nCoins; 
  private Scanner sysin;
  private Player currentPlayer;

  public static void main(String[] args) {
    new NimClient().run();
  }

  public NimClient() {
    sysin = new Scanner(System.in);
  }

  // The author's code
  public void run() {
    initGame();
    printInstructions();
    while (!gameIsOver()) {
      displayGame();
      if (currentPlayer == Player.HUMAN) {
        makeMove(getUserMove());
      } else {
        Move move = findBestMove(0);
        if (move == null) throw new RuntimeException("No legal moves");
        displayMove(move);
        makeMove(move);
      }
      switchPlayer();
    }
    announceResult();
  }

  public Move findBestMove(int depth) {

    ArrayList<Move> moveList = generateLegalMoves();
    Move bestMove = null;
    int minRating = WINNING_POSITION + 1;
    for (Move move : moveList) {
      makeMove(move);
      int moveRating = evaluatePosition(depth + 1);
      if (moveRating < minRating) {
        bestMove = move;
        minRating = moveRating;
      }
      retractMove(move);
    }
    if (bestMove != null) bestMove.setRating(-minRating);
    return bestMove;
  }

  public int evaluatePosition(int depth) {
    if (gameIsOver() || depth >= MAX_DEPTH) {
      return evaluateStaticPosition();
    }
    return findBestMove(depth).getRating();
  }
  // End of the author's code


  public void switchPlayer() {
    currentPlayer = (currentPlayer == Player.HUMAN) ? Player.COMPUTER
                                                    : Player.HUMAN;
  }

  public void initGame() {
    nCoins = STARTING_COINS;
    currentPlayer = STARTING_PLAYER;
  }

  public void printInstructions() {
    System.out.printf("Welcome to the game of Nim!\n");
    System.out.printf("In this game, we will start with a pile of " +
                      STARTING_COINS + " coins on the table.\n");
    System.out.printf("On each turn, you and I will alternately take " +
                      "between 1 and " + MAX_MOVE + " coins\n");
    System.out.printf("from the table. The player who takes the " +
                      "last coin loses.\n");
  }

  private void displayGame() {
    System.out.printf("\nThere are " + nCoins + " in the pile.\n");
  }

  private void displayMove(Move move) {
    System.out.printf("I'll take " + move.nTaken + ".\n");
  }

  private Move getUserMove() {
    int limit = (nCoins <= MAX_MOVE) ? nCoins : MAX_MOVE;
    while (true) {
      System.out.printf("How many would you like? ");
      int nTaken = sysin.nextInt();
      if (nTaken > 0 && nTaken <= limit) {
        Move move = new Move();
        move.nTaken = nTaken;
        return move;
      }
      System.out.printf("That's cheating! Please choose a number" +
                        " between 1 and " + limit + ".\n");
      displayGame();
    }
  }

  public ArrayList<Move> generateLegalMoves() {
    ArrayList<Move> allMove = new ArrayList<Move>();
    int limit = (nCoins <= MAX_MOVE) ? nCoins : MAX_MOVE;
    for (int i = 1; i <= limit; i++) 
      allMove.add(new Move( i ));
    return allMove;
  }    

  public void makeMove(Move move) {
    nCoins -= move.nTaken;
  }
  public void retractMove(Move move) {
    nCoins += move.nTaken;
  }

  private boolean gameIsOver() {
    return nCoins <= 1;
  }

  private void announceResult() {
    if (nCoins == 0) {
       System.out.printf("You took the last coin.  You lose.\n");
    } else {
      System.out.printf("There is only one coin left.\n");
      if (currentPlayer == Player.HUMAN) {
        System.out.printf("I win.\n");
      } else {
        System.out.printf("I lose.\n");
      }
    }
  }

  public int evaluateStaticPosition() {
    return (nCoins == 1) ? LOSING_POSITION : WINNING_POSITION;
  }
}
