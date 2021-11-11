package pebblesGame;
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
      //create new bags with the correct names. And set the partner bag of the black one.
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

    //check to see if the number of pebbles is enough for the number of players.
    if(pebbleCounter < numberOfPlayers * 11) {
      System.out.println("incorrect Number of Pebbles");
      //kill program if not enough players.
      System.exit(0);
    } else {
      this.startGame();
    }

  }

  //method to be run every time a players Thread takes their turn.
  public synchronized void playerTurn(int playerName, Player player) {
    Random rand = new Random();
    String output;
    //check to see if it is that players turn. If not then wait until notified.
    while(playerName != counter) {
      try {
        wait();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    //generate a random number and use it to choose a random black bag.
    int randNo = rand.nextInt(3);
    Bag bag = blackBags.get(randNo);
    Boolean check = this.checkEmpty(bag);
    //get the coresponding white bag.
    Bag whiteBag = whiteBags.get(randNo);
    //get the players hand and use the takePebble() mehtod to remove a pebble from it.
    ArrayList<Pebble> hand = player.getHand();
    hand = bag.takePebble(hand, player, whiteBag);
    //set the players hand to the new arraylist.
    player.setHand(hand);
    //take a new pebble from the bag using takeNewPebble. (It handles which bag.)
    Pebble temp = player.takeNewPebble();
    //add the pebble to the new hand. and then set the players hand to the new value.
    hand.add(temp);
    player.setHand(hand);
    //create the string needed for an output. Then use the .writeTofIle function
    //to output it to file.
    output = "Player"+player.getValue()+" had picked up a "+temp.getWeight()+" from "+bag.getName();
    bag.writeToFile(player.getFileName(), output);
    output = "Player"+player.getValue()+" current hand ";
    for(Pebble pebble : hand) {
      output = output + pebble.getWeight()+", ";
    }
    bag.writeToFile(player.getFileName(), output);
    //check of the player had now won the game.
    if(player.checkWin()) {
      //if they have won, set the winning player to the players name.
      this.winner = player.getValue();
      //output the winner to console.
      System.out.println("The Winner is player "+ this.getWinner());
      System.out.println("It took "+noOfTurns+" turns to win.");
    } else {
      System.out.println("Player: "+player.getValue()+ " took turn.");
    };
    //print to console which player tooka turn
    //increment the number of turns played, and set the counter to the next players name.
    noOfTurns++;
    incrementCounter();
    //notify all waiting threads that this method is now available.
    notifyAll();
  }

  //function to increase the players counter to the next player
  public void incrementCounter() {
    //if they are the last player then reset the counter to the first.
    if(counter == numberOfPlayers-1) {
      counter  = 0;
    } else {
      counter +=1;
    }
  }

  //function to create the players, deal, and then start them running.
  public void startGame() {

    //create as manyt players as has been given in the setup.
    for(int i = 0; i<numberOfPlayers; i++) {
      //create the new player, add them to the list of players, then deal them a hand.
      Player temp = new Player(this, i, blackBags);
      players.add(temp);
      temp.giveHand();
    }

    for(Player player : players) {
      //loop through all the players and start them running.
      player.start();
    }

  }

  //function to check whether a given bag is empty, if so then swap any Pebbles
  //in its corresponding white bag into it and clear the whitebag.
  public synchronized Boolean checkEmpty(Bag bag) {
    //return false of the bag is not empty.
    if(!bag.isEmpty()) {
      return false;
    } else {
      //get the coresponding bag for the given black bag. Check to see if its empty.
      ArrayList<Pebble> temp = new ArrayList<Pebble>();
      Bag whiteBag = bag.getPartnerBag();
      if(whiteBag.isEmpty()) {
        //if the white bag is empty return true and exit.
        return true;
      } else {
        //otherwise swap anything in the white bag into the black bag.
        ///getAll() will clear the white bag of all its contents.
        temp = whiteBag.getAll();
        bag.givePebbles(temp);
        return true;
      }
    }
  }

  //get and set methods for private variables.
  public ArrayList<Bag> getWhiteBags() {
    return whiteBags;
  }

  public int getWinner() {
    return winner;
  }

  //nested class to contain the players threads.
  class Player extends Thread{

    private int name;
    private PebblesGame game;
    private ArrayList<Pebble> currentHand = new ArrayList<Pebble>();
    private ArrayList<Bag> bags = new ArrayList<Bag>();
    private String fileName;

    //contructor to set variables.
    public Player(PebblesGame game,int name, ArrayList<Bag> bags) {
      this.game = game;
      this.name = name;
      this.bags = bags;
      int temp= name +1;
      try {
        //create a string than contains the file name for this players outputs.
        fileName = "player"+temp+"_output.txt";
        FileWriter writer = new FileWriter(fileName);
        writer.write("");
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    //overiding the run() method in Thread.
    public void run() {
      //while the game has not been won, players repeatedly attempt to take their turn.
      //winner will be intiallized as -1, and updated when a players has won.
      while(game.getWinner() == -1) {
        game.playerTurn(name, this);
      }

    }

    //dealing function to give players their inital hand.
    public void giveHand() {
      //iterate 10 times.
      for(int i = 0; i <10; i++) {
        //create a new pebble from the takeNewPebble() method.
        Pebble tempPebble;
        tempPebble = this.takeNewPebble();
        //add it to the players hand.
        currentHand.add(tempPebble);
      }
    }

    //method to give the player a new pebble from a random bag.
    public Pebble takeNewPebble() {
      Pebble temp;
      //generate a random number and use to to choose a random bag.
      Random rand = new Random();
      int randomNumber = rand.nextInt(3);
      Bag bag = bags.get(randomNumber);
      //if that bag is not empty then take a random pebble using. .giveRandomPebble().
      if(game.checkEmpty(bag) == false) {
        temp = bag.giveRandomPebble();
      } else {
        //if bag is empty then keep trying to choose a new bag until it finds
        //a bag that stil has pebbles in it.
        while(true) {
          //choose random bag.
          int newRandomNumber = rand.nextInt(3);
          //if it is not the same as the last bag chosen.
          if(newRandomNumber != randomNumber) {
            bag = bags.get(newRandomNumber);
            //check to see if this bag is empty.
            if(game.checkEmpty(bag)== false) {
              //if not then give a pebble from that bag and break the loop.
              temp = bag.giveRandomPebble();
              break;

            } else {
              //otherwise resen randomNumber and keep looking for a new bag.
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

    //get and set methods.
    public void setHand(ArrayList<Pebble> pebbles) {
      this.currentHand = pebbles;
    }

    public ArrayList<Pebble> getHand() {
      return currentHand;
    }

    public String getFileName() {
      return fileName;
    }
    public int getValue() {
      return name+1;
    }
    public void givePebble(Pebble pebble) {
    	currentHand.add(pebble);
    }
  }
}
