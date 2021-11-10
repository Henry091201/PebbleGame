<<<<<<< HEAD
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

=======
import static org.junit.jupiter.api.Assertions.*;

class PebbleTest {

    @org.junit.jupiter.api.Test
    void getWeight() {
        Pebble p = new Pebble(42);
        assertEquals(42, p.getWeight() );
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Pebble p = new Pebble(56);
        assertEquals("56", p.toString());
    }
>>>>>>> origin/master
}