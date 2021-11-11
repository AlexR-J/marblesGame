package pebblesGame;
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

  //Contructor to set variables.
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

  //method to check if bag is empty.
  public Boolean isEmpty() {
    //if empty return true.
    if(pebbles.size() < 1) {
      return true;
    } else {
      return false;
    }
  }

  //method to add multiple pebbles to the bag in one go.
  public void givePebbles(ArrayList<Pebble> newPebbles) {
    //loop through the given aray and add them all to the bag.
    for(Pebble pebble : newPebbles ) {
      this.pebbles.add(pebble);
    }
  }

  //method to add a pebble to bag.
  public void addPebble(Pebble pebble) {
    pebbles.add(pebble);
  }

  //method to remove a specific pebble from the bag.
  public void removePebble(Pebble pebble) {
    pebbles.remove(pebble);
  }

  public ArrayList<Pebble> getPebbles() {
    return pebbles;
  }

  //method to give the player a random pebble from this bag. (this does not hanndle
  //giving the pebble to the white bag that happens in PebblesGame)
  public Pebble giveRandomPebble() {
      //generate a randm number between 0 and the number of pebles in the bag
      //then use this to choose a new pebble and return it to the player.
      Random rand = new Random();
      int randomNumber = rand.nextInt(pebbles.size());
      Pebble temp = pebbles.get(randomNumber);
      pebbles.remove(temp);
      return temp;
  }

  //method take a random pebble from the players hand and add it to the pebbles in
  //this bags corresponding white bag.
  public ArrayList<Pebble> takePebble(ArrayList<Pebble> playersHand, PebblesGame.Player player, Bag whiteBag) {
      //generate a random number between 0 and the number of pebbles in the
      //players hand. Then use that to remove a pebble from the players hand,
      //and return it to them.
      Random rand = new Random();
      int randomNumber = rand.nextInt(playersHand.size());
      Pebble temp = playersHand.get(randomNumber);
      playersHand.remove(temp);
      partnerBag.addPebble(temp);
      //setup a write to file string and then write to the correct player file
      //the turn that just occured.
      String output = "Player"+player.getValue()+" had discarded a "+temp.getWeight()+" to bag "+whiteBag.getName();
      this.writeToFile(player.getFileName(), output);
      output = "Player"+player.getValue()+" current hand ";
      for(Pebble pebble : playersHand) {
        output = output + pebble.getWeight()+", ";
      }
      this.writeToFile(player.getFileName(), output);

      return playersHand;
  }

  //method to an array containing all of the pebbles from a bag.
  //and remove all of those pebbles from the bags arraylist.
  public ArrayList<Pebble> getAll() {
    ArrayList<Pebble> temp = new ArrayList<Pebble>();
    ArrayList<Pebble> removePebbles = new ArrayList<Pebble>();
    //loop throug all of the pebbles and add them to the temporary array.
    for(Pebble pebble : pebbles) {
      temp.add(pebble);
      removePebbles.add(pebble);
    }
    //remove the pebbles in the to be removed array from the bag.
    for(Pebble pebble : removePebbles) {
      pebbles.remove(pebble);
    }
    return temp;
  }

  //method to write an event to a file.
  public void writeToFile(String filename, String message) {
    try{
      //cretae a file writer and print the given string and a newline.
      FileWriter writer = new FileWriter(filename, true);
      writer.write(message+System.getProperty( "line.separator" ));
      writer.close();
    } catch (Exception e ) {
      e.printStackTrace();
    }
  }
}
