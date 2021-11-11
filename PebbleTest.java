import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleTest {


    /**
     * This method
    /**
     * tests getWeight()
     */
    @Test
    public void testWeight() {
        Pebble pebble = new Pebble(100);
        assertEquals(100, pebble.getWeight());
    }
}