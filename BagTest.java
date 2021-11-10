import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagTest {

    Bag bag;

    /**
     * This method initialises the Bag
     * Puts 100 pebbles in it, each weight 1.
     */
    @BeforeEach
    public void setUp() {
        bag = new Bag("black",'X');

        for(int i=0; i<100; i++) {
            bag.addPebble(new Pebble(1));
        }

    }

    /**
     * test getBagLetter
     */
    @Test
    public void testLetter() {
        assertEquals('X',bag.getBagLetter());
    }

    /**
     * tests getSize()
     */
    @Test
    public void testSize() {
        assertEquals(100, bag.getSize(bag));
    }

    /**
     * tests testAddPebble()
     */
    @Test
    public void testAddPebble() {
        Pebble pebble = new Pebble(1);
        bag.addPebble(pebble);
        // checks if the size of the bag has increased by 1
        assertEquals(101, bag.getSize(bag));
        // checks if the last pebble added is the same as the one earlier
        assertEquals(true, bag.getPebbles().get(bag.getSize(bag)-1).equals(pebble));
    }

    @Test
    public void testRemovePebble() {
        Pebble pebble = new Pebble(1);
        bag.addPebble(pebble);
        bag.removePebble(bag, pebble);
        assertEquals(100, bag.getSize(bag));
        assertEquals(false, bag.getPebbles().get(bag.getSize(bag)-1).equals(pebble));
    }



}