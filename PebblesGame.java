import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class PebblesGame {

  private int NumberOfPlayers;

  public void setup() {

    ArrayList<Bag> whiteBags = new ArrayList<Bag>();
    ArrayList<Bag> blackBags = new ArrayList<Bag>();

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


    String bag0;
    String bag1;
    String bag2;

    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of Players: ");
    NumberOfPlayers =  Integer.parseInt(scanner.nextLine());
    System.out.println("Location of bag 0: ");
    bag0 = scanner.nextLine();
    System.out.println("Location of bag 1: ");
    bag1 = scanner.nextLine();
    System.out.println("Location of bag 2: ");
    bag2 = scanner.nextLine();

    ArrayList<File> files = new ArrayList<File>();

    files.add(new File(bag0));
    files.add(new File(bag1));
    files.add(new File(bag2));

    int pebbleCounter = 0;

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
    }

    for(Bag bag : blackBags) {
      for(Pebble pebble : bag.getPebbles()) {
        System.out.print(pebble);
      }
    }


  }

  static class Player {

  }

}
