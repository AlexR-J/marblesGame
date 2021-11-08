import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class PebblesGame extends Thread {

  private int NumberOfPlayers;

  //method that sestup the game (creating bags and pebbles from the given
  //inputs.)
  public void setup() {

    ArrayList<Bag> whiteBags = new ArrayList<Bag>();
    ArrayList<Bag> blackBags = new ArrayList<Bag>();

    //Loops 3 times creating 3 bags with coresponding names. Then appends them to
    //the array that contains all the white bags.
    for (int i = 0; i<3; i++) {
      String name = "";
      if (i == 0) {
        name = "A";
      } else if (i == 1) {
        name = "B";
      } else if (i == 2) {
        name = "C";
      }
      Bag bag = new Bag("white", name);
      whiteBags.add(bag);
    }

    //Loops 3 times creating all 3 bags with corsponding names. Then appends Then
    //to the array containing all of the black bags.
    for (int i = 0; i<3; i++) {
      String name = "";
      if (i == 0) {
        name = "X";
      } else if (i == 1) {
        name = "Y";
      } else if (i == 2) {
        name = "Z";
      }
      Bag bag = new Bag("black", name);
      blackBags.add(bag);
    }


    //placeholders for the locations of the bags to be given.
    String bag0;
    String bag1;
    String bag2;

    //Creates a scanner for the command prompt. Then prints the questions and
    //waits for a nextLine to read a response.
    //Then adds the response to the corresponding placeholder.
    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of Players: ");
    NumberOfPlayers =  Integer.parseInt(scanner.nextLine());
    System.out.println("Location of bag 0: ");
    bag0 = scanner.nextLine();
    System.out.println("Location of bag 1: ");
    bag1 = scanner.nextLine();
    System.out.println("Location of bag 2: ");
    bag2 = scanner.nextLine();

    //Array list to contain the files themselves.
    ArrayList<File> files = new ArrayList<File>();

    //Create new File objects with the addresses given then append them to them
    //array containing the files.
    files.add(new File(bag0));
    files.add(new File(bag1));
    files.add(new File(bag2));

    int pebbleCounter = 0;

    //Now for each of the bags. Go through tbe corresponding file and then, make
    //pebbles to gointo each bag. Then use the .addPebble method to add them to
    //the bag.
    try {
      for(int i = 0; i<3; i++) {
        Scanner fileScanner = new Scanner(files.get(i));
        while (fileScanner.hasNextLine()) {
          int weight = Integer.parseInt(fileScanner.nextLine());
          Pebble pebble = new Pebble(weight);
          pebbleCounter++;
          blackBags.get(i).addPebble(pebble);
        }
      }
    } catch (FileNotFoundException e)  {
      e.printStackTrace();
    }

    if(pebbleCounter != NumberOfPlayers * 11) {
      System.out.print("incorrect Number of Pebbles");
    } else {
      this.startGame();
    }

    // for(Bag bag : blackBags) {
    //   for(Pebble pebble : bag.getPebbles()) {
    //     System.out.print(pebble);
    //   }
    // }

  }

  public void startGame() {


  }

  class Player extends Thread{


    private ArrayList<Pebble> currentHand = new ArrayList<Pebble>();

    //method to start game taking 10 pebbles.
    public void takeHand(Bag bag) {

    }

    //method to discard a pebble to a bag.
    public void discardPebble(Bag bag) {

    }

    //method to take a pebble from a random bag.
    public void takePebble(Bag bag) {

    }

    //method to check if a player has won.
    public Boolean checkWin() {
      //create a counter and add the weight of each pebble in the hand to it.
      //If equals 100 then return true.
      int temp = 0;
      for(Pebble pebble : currentHand) {
        temp += pebble.getWeight();
      }
      if (temp == 100) {
        return true;
      } else {
        return false;
      }
    }
  }
}
