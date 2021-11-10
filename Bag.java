import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;

public class Bag{

  private ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
  private String color;
  private String name;
  private PebblesGame game;
  private Bag partnerBag;

  public String getName() {
    return name;
  }

  public Bag(String color, String name, PebblesGame game) {
    this.color = color;
    this.name = name;
    this.game = game;
  }

  public void setPartnerBag(Bag partnerBag) {
    this.partnerBag = partnerBag;
  }

  public Bag getPartnerBag() {
    return partnerBag;
  }

  public Boolean isEmpty() {
    if(pebbles.size() < 1) {
      return true;
    } else {
      return false;
    }
  }

  public void givePebbles(ArrayList<Pebble> newPebbles) {
    for(Pebble pebble : newPebbles ) {
      this.pebbles.add(pebble);
    }
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

  // public ArrayList<Pebble> giveHand() {
  //   //create a temporary arraylist to contain the pebbles to be given to a player.
  //   ArrayList<Pebble> temp = new ArrayList<Pebble>();
  //   Random rand = new Random();
  //   //10 times generate a random number and choose the pebble and that index.
  //   //then add this pebble to the temprary arraylist.
  //   for(int i = 0; i<10; i++) {
  //     System.out.print(pebbles.size());
  //     int randomNumber = rand.nextInt(pebbles.size()-1);
  //     temp.add(pebbles.get(randomNumber));
  //     pebbles.remove(randomNumber);
  //     game.checkEmpty(this);
  //   }
  //   return the list of pebbles now containing 10 pebbles.
  //   return temp;
  // }

  public Pebble giveRandomPebble() {
      //generate a randm number between 0 and the number of pebles in the bag
      //then use this to choose a new pebble and return it to the player.
      Random rand = new Random();
      int randomNumber = rand.nextInt(pebbles.size());
      Pebble temp = pebbles.get(randomNumber);
      pebbles.remove(temp);
      return temp;
  }

  public ArrayList<Pebble> takePebble(ArrayList<Pebble> playersHand, PebblesGame.Player player, Bag whiteBag) {
      //generate a random number between 0 and the number of pebbles in the
      //players hand. Then use that to remove a pebble from the players hand,
      //and return it to them.
      Random rand = new Random();
      int randomNumber = rand.nextInt(playersHand.size());
      Pebble temp = playersHand.get(randomNumber);
      playersHand.remove(temp);
      partnerBag.addPebble(temp);
      String output = "Player"+player.getValue()+" had discarded a "+temp.getWeight()+" to bag "+whiteBag.getName();
      this.writeToFile(player.getFileName(), output);
      output = "Player"+player.getValue()+" current hand ";
      for(Pebble pebble : playersHand) {
        output = output + pebble.getWeight()+", ";
      }
      this.writeToFile(player.getFileName(), output);

      return playersHand;
  }

  public ArrayList<Pebble> getAll() {
    ArrayList<Pebble> temp = new ArrayList<Pebble>();
    ArrayList<Pebble> removePebbles = new ArrayList<Pebble>();
    for(Pebble pebble : pebbles) {
      temp.add(pebble);
      removePebbles.add(pebble);
    }
    for(Pebble pebble : removePebbles) {
      pebbles.remove(pebble);
    }

    return temp;
  }

  public void writeToFile(String filename, String message) {
    try{
      FileWriter writer = new FileWriter(filename, true);
      writer.write(message+System.getProperty( "line.separator" ));
      writer.close();
    } catch (Exception e ) {
      e.printStackTrace();
    }


  }



}
