import java.util.concurrent.CopyOnWriteArrayList;

public class Bag{

    //Arraylist containing the pebbles
    private volatile CopyOnWriteArrayList<Pebble> pebbles = new CopyOnWriteArrayList<>();
    private volatile static CopyOnWriteArrayList<Bag> blackBags = new CopyOnWriteArrayList<>();
    private volatile static CopyOnWriteArrayList<Bag> whiteBags = new CopyOnWriteArrayList<>();
    private String colour;
    private char letter;

    // Constructor
    public Bag(String colour, char letter){
        this.colour = colour;
        this.letter = letter;
    }

    // adds the pebble
    public void addPebble(Pebble pebble) {
        pebbles.add(pebble);
    }
    // returns the arraylist of pebbles contained within the bag
    public CopyOnWriteArrayList<Pebble> getPebbles(){
        return pebbles;
    }
    // set the static black bags
    public static void setBlackBags(Bag bag){
        blackBags.add(bag);
    }
    // sets the static white bags
    public static void setWhiteBags(Bag bag){
        whiteBags.add(bag);
    }
    // returns the black bags
    public static CopyOnWriteArrayList<Bag> getBlackBags(){
        return blackBags;
    }
    // returns the white bags
    public static CopyOnWriteArrayList<Bag> getWhiteBags(){
        return whiteBags;
    }
    // returns the amount of pebbles the bag contains
    public static int getSize(Bag bagArrayList){
        return bagArrayList.getPebbles().size();
    }
    // removes a certain pebble from the bag
    public static void removePebble(Bag bag, Pebble pebble){
        bag.getPebbles().remove(pebble);
    }
    // getter method for the letter
    public char getBagLetter(){
        return letter;
    }

}
