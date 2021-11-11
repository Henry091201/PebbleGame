
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleGameTest {

    PebbleGame game = new PebbleGame();



    @BeforeEach
    public void setUp(){

    }
    @After
    public void tearDown(){
        Bag.getBlackBags().clear();
        Bag.getWhiteBags().clear();
    }

    @Test
    public void validateWeightTest(){
        String weights = "1,2,3,4,54,7,8,9,0";
        assertEquals(true, PebbleGame.validateWeights(weights));
        String willFail = "hello,244,sup ";
        assertEquals(false, PebbleGame.validateWeights(willFail));
    }

    @Test
    public void setUpBagsTest(){
        PebbleGame.setupBags();
        assertEquals(true, Bag.getBlackBags().size() == 3);
        assertEquals(true, Bag.getWhiteBags().size() == 3);

    }
    @Test
    public void fillBagTest(){
        Bag blackBag = new Bag("black", 'X');
        String[] weights = new String[3];
        weights[0] = "1";
        weights[1] = "2";
        weights[2] = "3";
        PebbleGame.fillBag(blackBag, weights);
        assertEquals(3, blackBag.getPebbles().size());
        weights[0] = "-1";
        try {
            PebbleGame.fillBag(blackBag, weights);
            fail("Should have caught a runtime exception for the negative weights");
        }catch (RuntimeException e){
        }
    }
    @Test
    public void addPlayersTest(){
        PebbleGame.addPlayer(1);
        PebbleGame.addPlayer(2);
        PebbleGame.addPlayer(3);
        assertEquals(3, PebbleGame.Player.getPlayers().size());
        assertEquals(false, PebbleGame.Player.getPlayers().size() == 4);
    }

    public class PlayerTest {

        PebbleGame.Player player = new PebbleGame.Player(1);
        Bag bag;
        PebbleGame game = new PebbleGame();

        @BeforeEach
        public void setup() {

        }

    }
}