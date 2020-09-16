/* Hoang Le 
 * March 2, 2020
 * Homework 3: Problem 2
 * Modified Nim Game with MiniMax algorithm
 * Start with 17; whoever ends up with even number of coins wins
 * Use recursive methods from author
 * Only keep track of the current player's coins
 * If 1 coin left, automatically make last move
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Nim2Client {

  // Constants and Instance Variables
  public static final int WINNING_POSITION = 10;
  public static final int LOSING_POSITION = -10;
  public static final int MAX_DEPTH = 16;

  private static final int MAX_MOVE = 4;
  private static final int STARTING_COINS = 17;
  private static final Player STARTING_PLAYER = Player.HUMAN;


  private int nCoins, currentCoins ; 
  private Scanner sysin;
  private Player currentPlayer;

  public static void main(String[] args) {
    new Nim2Client().run();
  }

  public Nim2Client() {
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
        System.out.printf("You have %d.\n", currentCoins);
      } else {
        Move move = findBestMove(0);
        if (move == null) throw new RuntimeException("No legal moves");
        displayMove(move);
        makeMove(move);
        System.out.printf("I have %d.\n", currentCoins);
      }
      switchPlayer();
      if (nCoins == 1) 
        makeLastMove();
    }
    announceResult();
  }

  public Move findBestMove(int depth) {

    ArrayList<Move> moveList = generateLegalMoves();
    Move bestMove = null;
    int minRating = WINNING_POSITION + 1;
    for (Move move : moveList) {
      makeMove(move);
      switchPlayer();                // Modified
      int moveRating = evaluatePosition(depth + 1);
      switchPlayer();                // Modified       
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

  public void switchPlayer() {
    currentPlayer = (currentPlayer == Player.HUMAN) ? Player.COMPUTER
                                                    : Player.HUMAN;
    currentCoins = STARTING_COINS - nCoins - currentCoins; 
  }
  // End of the author's code

  public void initGame() {
    nCoins = STARTING_COINS;
    currentPlayer = STARTING_PLAYER;
    currentCoins = 0;
  }

  public void printInstructions() {
    System.out.printf("Welcome to the game of Nim!\n");
    System.out.printf("In this game, we will start with a pile of " +
                      STARTING_COINS + " coins on the table.\n");
    System.out.printf("On each turn, you and I will alternately take " +
                      "between 1 and " + MAX_MOVE + " coins\n");
    System.out.printf("from the table. The player who ends up with " +
                      "an even number of coins wins.\n");
  }

  private void displayGame() {
    System.out.printf("There are " + nCoins + " in the pile.\n\n");
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
    currentCoins += move.nTaken;
  }

  public void retractMove(Move move) {
    nCoins += move.nTaken;
    currentCoins -= move.nTaken;
  }

  private boolean gameIsOver() {
    return nCoins < 1;
  }

  // Assign the last coin to current player
  private void makeLastMove() {
    Move lastMove = new Move();
    lastMove.nTaken = 1;
    makeMove(lastMove);
    if ( currentPlayer == Player.HUMAN)
      System.out.printf("The last coin is yours. You have %d.\n", currentCoins);
    else
      System.out.printf("The last coin is mine. I have %d.\n", currentCoins);
  }
    
  private void announceResult() {

    if ( ( currentPlayer == Player.HUMAN && currentCoins % 2 == 1)
      || ( currentPlayer == Player.COMPUTER && currentCoins % 2 == 0))
      System.out.printf("I win.\n");
    else 
      System.out.printf("I lose.\n");
  }

  public int evaluateStaticPosition() {
    return (currentCoins % 2 == 1) ? LOSING_POSITION : WINNING_POSITION;
  }  
}
