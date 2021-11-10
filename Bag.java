import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Bag{

    //Arraylist containing the pebbles
    private volatile CopyOnWriteArrayList<Pebble> pebbles = new CopyOnWriteArrayList<>();
    private volatile static CopyOnWriteArrayList<Bag> blackBags = new CopyOnWriteArrayList<>();
    private volatile static CopyOnWriteArrayList<Bag> whiteBags = new CopyOnWriteArrayList<>();
    //Type of bag it is
    private String colour;
    //Letter that the bag is
    private char letter;

    public Bag(String colour, char letter){
        this.colour = colour;
        this.letter = letter;
    }

    public void addPebble(Pebble pebble) {
        pebbles.add(pebble);
    }

    public CopyOnWriteArrayList<Pebble> getPebbles(){
        return pebbles;
    }
    public static void setBlackBags(Bag bag){
        blackBags.add(bag);
    }
    public static void setWhiteBags(Bag bag){
        whiteBags.add(bag);
    }
    public static CopyOnWriteArrayList<Bag> getBlackBags(){
        return blackBags;
    }
    public static CopyOnWriteArrayList<Bag> getWhiteBags(){
        return whiteBags;
    }
    public static int getSize(Bag bagArrayList){
        return bagArrayList.getPebbles().size();
    }
    public static void removePebble(Bag bag, Pebble pebble){
        bag.getPebbles().remove(pebble);
    }
    public char getBagLetter(){
        return letter;
    }
    public static void bagFill(Bag whitebag, Bag blackbag){
        //Loop over every pebble in whitebag
        for(Pebble pebble:whitebag.getPebbles()){
            blackbag.addPebble(pebble);
        }
        //clear white bag arraylist
        whitebag.getPebbles().clear();
    }
}
