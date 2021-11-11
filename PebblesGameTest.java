package pebblesGame;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PebblesGameTest {
	
	private PebblesGame game;
	private Bag blackBag;
	private Bag whiteBag;

	@Before
	public void setUp() throws Exception {
		game = new PebblesGame();
		whiteBag = new Bag("white", "A", game);
		blackBag = new Bag("black", "X", game);
		blackBag.setPartnerBag(whiteBag);
		
	}

	@Test
	public void testSetup() {
		try {
			game.setup();
		} catch (Exception e){
			fail("Exception thrown");
		}
	}

	//checkEmpty test with whiteBag being full and blackBag empty
	@Test
	public void testCheckEmpty() {
		//fill the white bag
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		for(int i = 0; i < 3; i++) {
			pebbles.add(new Pebble(1));
		}
		whiteBag.givePebbles(pebbles);
		//if bag is empty assert
		assertTrue(game.checkEmpty(blackBag));	
	}
	
	//checkEmpty test with blackBag being full and whiteBag empty
	@Test
	public void testCheckEmptyFullBag() {
		//fill the white bag
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		for(int i = 0; i < 3; i++) {
			pebbles.add(new Pebble(1));
		}
		blackBag.givePebbles(pebbles);
		//if bag is full assert
		assertFalse(game.checkEmpty(blackBag));	
	}

}
