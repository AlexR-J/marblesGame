import java.util.ArrayList;

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

}
