import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.io.FileWriter;

public class PebblesGame extends Thread {

  private int numberOfPlayers;
  private int counter;
  private ArrayList<Player> players;
  private Bag tempTest = new Bag("white", "R", this);
  private ArrayList<Bag> whiteBags = new ArrayList<Bag>();
  private ArrayList<Bag> blackBags = new ArrayList<Bag>();
  private int winner = -1;
  private int noOfTurns = 0;


  //method that sestup the game (creating bags and pebbles from the given
  //inputs.)
  public void setup() {


    counter = 0;
    players = new ArrayList<Player>();
    //Testing variable @@@@remove later@@@@


    //Loops 3 times creating 3 bags with coresponding names. Then appends them to
    //the array that contains all the white bags.
    for (int i = 0; i<3; i++) {
      String name = "";
      String name2 = "";
      if (i == 0) {
        name = "A";
        name2 = "X";
      } else if (i == 1) {
        name = "B";
        name2 = "Y";
      } else if (i == 2) {
        name = "C";
        name2 = "Z";
      }
      Bag whiteBag = new Bag("white", name, this);
      whiteBags.add(whiteBag);
      Bag blackBag = new Bag("black", name2, this);
      blackBag.setPartnerBag(whiteBag);
      blackBags.add(blackBag);
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
    numberOfPlayers =  Integer.parseInt(scanner.nextLine());
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
        fileScanner.useDelimiter(",");
        while (fileScanner.hasNext()) {
          int weight = Integer.parseInt(fileScanner.next());
          Pebble pebble = new Pebble(weight);
          pebbleCounter++;
          if(weight < 0) {
            System.out.print("Non-positive Weight found");
            System.exit(0);
          }
          blackBags.get(i).addPebble(pebble);
        }
        fileScanner.close();
      }
    } catch (FileNotFoundException e)  {
      e.printStackTrace();
    }

    if(pebbleCounter < numberOfPlayers * 11) {
      System.out.println("incorrect Number of Pebbles");
      System.exit(0);
    } else {
      this.startGame();
    }

    // for(Bag bag : blackBags) {
    //   for(Pebble pebble : bag.getPebbles()) {
    //     System.out.print(pebble);
    //   }
    // }

  }

  public synchronized void playerTurn(int playerName, Player player) {
    Random rand = new Random();
    String output;
    while(playerName != counter) {
      try {
        wait();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    int randNo = rand.nextInt(3);
    Bag bag = blackBags.get(randNo);
    Boolean check = this.checkEmpty(bag);
    Bag whiteBag = whiteBags.get(randNo);
    ArrayList<Pebble> hand = player.getHand();
    hand = bag.takePebble(hand, player, whiteBag);
    player.setHand(hand);
    Pebble temp = player.takeNewPebble();
    hand.add(temp);
    player.setHand(hand);
    output = "Player"+player.getValue()+" had picked up a "+temp.getWeight()+" from "+bag.getName();
    bag.writeToFile(player.getFileName(), output);
    output = "Player"+player.getValue()+" current hand ";
    for(Pebble pebble : hand) {
      output = output + pebble.getWeight()+", ";
    }
    bag.writeToFile(player.getFileName(), output);
    if(player.checkWin()) {
      this.winner = player.getValue();
      System.out.println("The Winner is player "+ this.getWinner());
      System.out.println("It took "+noOfTurns+" turns to win.");
    };
    noOfTurns++;
    incrementCounter();
    notifyAll();
  }

  public void incrementCounter() {
    if(counter == numberOfPlayers-1) {
      counter  = 0;
    } else {
      counter +=1;
    }
  }

  public void startGame() {

    for(int i = 0; i<numberOfPlayers; i++) {
      Player temp = new Player(this, i, blackBags);
      players.add(temp);
      temp.giveHand();
    }

    for(Player player : players) {

      player.start();
    }

  }

  public synchronized Boolean checkEmpty(Bag bag) {
    if(!bag.isEmpty()) {
      return false;
    } else {
      ArrayList<Pebble> temp = new ArrayList<Pebble>();
      Bag whiteBag = bag.getPartnerBag();
      if(whiteBag.isEmpty()) {
        return true;
      } else {
        temp = whiteBag.getAll();
        bag.givePebbles(temp);
        return true;
      }
    }
  }

  public int getWinner() {
    return winner;
  }

  public ArrayList<Bag> getWhiteBags() {
    return whiteBags;
  }

  class Player extends Thread{

    private int name;
    private PebblesGame game;
    private ArrayList<Pebble> currentHand = new ArrayList<Pebble>();
    private ArrayList<Bag> bags = new ArrayList<Bag>();
    private String fileName;

    public Player(PebblesGame game,int name, ArrayList<Bag> bags) {
      this.game = game;
      this.name = name;
      this.bags = bags;
      int temp= name +1;
      try {
        fileName = "player"+temp+"_output.txt";
        FileWriter writer = new FileWriter(fileName);
        writer.write("");
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public int getValue() {
      return name+1;
    }

    public void run() {
      // this.giveHand();
      // for(int i = 0; i<3; i++) {
      //   game.playerTurn(name);
      // }
      while(game.getWinner() == -1) {
        game.playerTurn(name, this);
      }

    }

    public void giveHand() {
      for(int i = 0; i <10; i++) {
        Pebble tempPebble;
        tempPebble = this.takeNewPebble();
        currentHand.add(tempPebble);
      }
      // currentHand = bag.giveHand();
    }

    public Pebble takeNewPebble() {
      // System.out.print("@@@");
      // for (Bag bag : bags ) {
      //   System.out.println(bag.getPebbles());
      // }
      Pebble temp;
      Random rand = new Random();
      int randomNumber = rand.nextInt(3);
      Bag bag = bags.get(randomNumber);
      // System.out.print(game.getWhiteBags().get(2).getPebbles());
      if(game.checkEmpty(bag) == false) {
        temp = bag.giveRandomPebble();
      } else {
        while(true) {
          System.out.print(bag.getPebbles().size());
          System.out.print(";;");
          System.out.print(game.getWhiteBags().get(2).getPebbles().size());

          int newRandomNumber = rand.nextInt(3);
          if(newRandomNumber != randomNumber) {
            bag = bags.get(newRandomNumber);
            if(game.checkEmpty(bag)== false) {
              temp = bag.giveRandomPebble();
              break;

            } else {
              randomNumber = newRandomNumber;
            }
          }
        }
      }

      return temp;

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

    public void setHand(ArrayList<Pebble> pebbles) {
      this.currentHand = pebbles;
    }

    public ArrayList<Pebble> getHand() {
      return currentHand;
    }

    public String getFileName() {
      return fileName;
    }
  }
}
