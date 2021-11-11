<<<<<<< HEAD
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleGameTest {

    PebbleGame game = new PebbleGame();

    public class PlayerTest {

        PebbleGame.Player player = new PebbleGame.Player(1);
        Bag bag;
        PebbleGame game = new PebbleGame();

        @BeforeEach
        public void setup() {

        }

    }

    PebbleGame pebbleGame = new PebbleGame();


=======
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PebbleGameTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addPlayer() {

    }

    @Test
    void addFile() {
    }

    @Test
    void askForBags() {
    }

    @Test
    void validateWeights() {
        String correct = "1,2,3,4,5,6,7,8";
        assertEquals (true, PebbleGame.validateWeights(correct));
    }

    @Test
    void setupBags() {
    }

    @Test
    void setupPlayers() {
    }

    @Test
    void fillBag() {
    }

    @Test
    void printOutputs() {
    }

    @Test
    void main() {
    }
>>>>>>> origin/master
}