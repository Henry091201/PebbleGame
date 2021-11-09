import java.util.ArrayList;
import java.util.Iterator;

public class Bag{

    //Arraylist containing the pebbles
    private ArrayList<Pebble> pebbles = new ArrayList<>();
    private static ArrayList<Bag> blackBags = new ArrayList<>();
    private static ArrayList<Bag> whiteBags = new ArrayList<>();
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

    public ArrayList<Pebble> getPebbles(){
        return pebbles;
    }
    public static void setBlackBags(Bag bag){
        blackBags.add(bag);
    }
    public static void setWhiteBags(Bag bag){
        whiteBags.add(bag);
    }
    public static ArrayList<Bag> getBlackBags(){
        return blackBags;
    }
    public static ArrayList<Bag> getWhiteBags(){
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
