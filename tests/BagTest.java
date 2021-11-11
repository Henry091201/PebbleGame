import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagTest {

    Bag bag;

    /**
     * This method initialises the Bag
     * Puts 100 pebbles in it, each weight 1.
     */

    @BeforeEach
    public void setUp(){
        bag = new Bag("black", 'X');
        bag.addPebble(new Pebble(100));
        bag.addPebble(new Pebble(50));
        bag.addPebble(new Pebble(10));
        bag.addPebble(new Pebble(75));

    }


    /**
     * test getBagLetter
     */
    @Test
    public void testLetter() {
        assertEquals('X',bag.getBagLetter());
        assertEquals(false, bag.getBagLetter() == 'Y');
    }

    /**
     * tests getSize()
     */
    @Test
    public void testSize() {
        assertEquals(4, bag.getSize(bag));
        assertEquals(false, bag.getPebbles().size() == 0);
    }

    /**
     * tests testAddPebble()
     */
    @Test
    public void testAddPebble() {
        Pebble pebble = new Pebble(1);
        bag.addPebble(pebble);
        // checks if the size of the bag has increased by 1
        assertEquals(5, bag.getSize(bag));
        // checks if the last pebble added is the same as the one earlier
        assertEquals(true, bag.getPebbles().get(bag.getSize(bag)-1).equals(pebble));
    }

    @Test
    public void testRemovePebble() {
        Pebble pebble = new Pebble(1);
        bag.addPebble(pebble);
        bag.removePebble(bag, pebble);
        assertEquals(4, bag.getSize(bag));
        assertEquals(false, bag.getPebbles().get(bag.getSize(bag)-1).equals(pebble));
    }
    @AfterEach
    public void tearDown(){
        bag = null;
    }
}