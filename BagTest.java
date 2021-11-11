package pebblesGame;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test;
import java.util.ArrayList;

public class BagTest {
	private Bag bag;
	
	//setup function
	@Before
	public void setup() {
		PebblesGame game = new PebblesGame();
		bag = new Bag("white", "A", game);
	}
	
	//testing the give RandomPebble function
	@Test
	public void testGiveRandomPebble() {
		//Add 3 pebbles to the bag.
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		for(int i = 0; i < 3; i++) {
			pebbles.add(new Pebble(1));
		}
		//attempt to remove the pebbles from the bag.
		bag.givePebbles(pebbles);
		Pebble pebble = bag.giveRandomPebble();
		assertNotNull(pebble);
	}

	//testing getAll method
	@Test
	public void testGetAll() {
		//create some pebbles and add them too the bag. 
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		for(int i = 0; i < 3; i++) {
			pebbles.add(new Pebble(1));
		}
		ArrayList<Pebble> temp = new ArrayList<Pebble>();
		//attmep to remove pebbles assert if seccesful
		temp = bag.getAll();
		assertNotNull(temp);
	}
	
	//testing getAll but with no pebbles in the bag.
	@Test
	public void testGetAllNoPebbles() {
		ArrayList<Pebble> temp = new ArrayList<Pebble>();
		temp = bag.getAll();
		//assert if the size of the returned array is zero.
		assertTrue(temp.size() == 0);
	}

	@Test
	public void testWriteToFile() {
		String filename = "test.txt";
		bag.writeToFile(filename, "this worked");
	}

}
