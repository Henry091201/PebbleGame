import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleGameTest {


    @BeforeEach
    public void setup(){

    }
    @AfterEach
    public void tearDown(){     // Clears bag arrays when tests finish
        Bag.getBlackBags().clear();
        Bag.getWhiteBags().clear();
        PebbleGame.Player.getPlayers().clear();
    }

    @Test
    public void TestValidateWeight(){
        String weights = "1,2,3,4,54,7,8,9,0";
        assertEquals(true, PebbleGame.validateWeights(weights));    // When it follows format
        String willFail = "hello,244,sup ";
        assertEquals(false, PebbleGame.validateWeights(willFail));  //When it doesn't
    }

    @Test
    public void TestSetUpBags(){
        PebbleGame.setupBags();
        assertEquals(true, Bag.getBlackBags().size() == 3); //Sets up the black bags correctly
        assertEquals(true, Bag.getWhiteBags().size() == 3);

    }
    @Test
    public void TestFillBag(){
        Bag blackBag = new Bag("black", 'X');
        String[] weights = new String[3];
        weights[0] = "1";
        weights[1] = "2";
        weights[2] = "3";
        PebbleGame.fillBag(blackBag, weights);
        assertEquals(3, blackBag.getPebbles().size());  //Fills bag correctly
        weights[0] = "-1";
        try {
            PebbleGame.fillBag(blackBag, weights);  //Throws error if there is a negative weight
            fail("Should have caught a runtime exception for the negative weights");
        }catch (RuntimeException e){
        }
    }
    @Test
    public void TestAddPlayers(){
        PebbleGame.addPlayer(1);
        PebbleGame.addPlayer(2);
        PebbleGame.addPlayer(3);
        assertEquals(3, PebbleGame.Player.getPlayers().size());
        assertEquals(false, PebbleGame.Player.getPlayers().size() == 4);
    }
}