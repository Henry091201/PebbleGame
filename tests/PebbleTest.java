import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleTest {


    /**
     * This method initialises pebble
     */
    @BeforeEach
    public void setup() {

    }

    /**
     * tests getWeight()
     */
    @Test
    public void testWeight() {
        Pebble pebble = new Pebble(100);
        assertEquals(100, pebble.getWeight());
    }

    /**
     * tests toString()
     */
    @Test
    public void testToString() {
        Pebble pebble = new Pebble(100);
        assertEquals("100", pebble.toString());
    }
}