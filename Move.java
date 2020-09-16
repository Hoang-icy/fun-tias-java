public class Move {

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  private int rating;

  public Move(int nTaken) {
    this.nTaken = nTaken;
  }
  public Move() {}
  public int nTaken;
}
