import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleTest {

    Pebble pebble;

    /**
     * This method initialises pebble
     */
    @BeforeEach
    public void setup() {
        pebble = new Pebble(100);
    }

    /**
     * tests getWeight()
     */
    @Test
    public void testWeight() {
        assertEquals(100, pebble.getWeight());
    }

    /**
     * tests toString()
     */
    @Test
    public void testToString() {
        assertEquals("100",pebble.toString());
    }

}