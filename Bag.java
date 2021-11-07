import java.util.ArrayList;
import java.util.Random;

public class Bag {

  private ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
  private String color;
  private String name;

  public Bag(String color, String name) {
    this.color = color;
    this.name = name;
  }

  public void addPebble(Pebble pebble) {
    pebbles.add(pebble);
  }

  public void removePebble(Pebble pebble) {
    pebbles.remove(pebble);
  }

  public ArrayList<Pebble> getPebbles() {
    return pebbles;
  }

  public synchronized ArrayList<Pebble> giveHand() {
    //create a temporary arraylist to contain the pebbles to be given to a player.
    ArrayList<Pebble> temp = new ArrayList<Pebble>();
    Random rand = new Random();
    //10 times generate a random number and choose the pebble and that index.
    //then add this pebble to the temprary arraylist.
    for(int i = 0; i<10; i++) {
      int randomNumber = rand.nextInt(pebbles.size());
      temp.add(pebbles.get(randomNumber));
    }
    //return the list of pebbles now containing 10 pebbles.
    return temp;
  }

  public synchronized Pebble giveRandomPebble() {
      //generate a randm number between 0 and the number of pebles in the bag
      //then use this to choose a new pebble and return it to the player.
      Random rand = new Random();
      int randomNumber = rand.nextInt(pebbles.size());
      Pebble temp = pebbles.get(randomNumber);
      return temp;
  }

  public synchronized ArrayList<Pebble> takePebble(ArrayList<Pebble> playersHand) {
      //generate a random number between 0 and the number of pebbles in the
      //players hand. Then use that to remove a pebble from the players hand,
      //and return it to them.
      Random rand = new Random();
      int randomNumber = rand.nextInt(playersHand.size());
      Pebble temp = playersHand.get(randomNumber);
      playersHand.remove(temp);
      return playersHand;
  }
}
