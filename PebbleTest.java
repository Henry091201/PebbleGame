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
}