public class Pebble {

    // attribute
    private int weight;

    // Constructor method
    public Pebble(int weight){
        this.weight = weight;
    }

    // returns the weight of the pebble
    public int getWeight(){
        return weight;
    }

    // returns the string weight of the pebble
    public String toString() {
        return Integer.toString(weight);
    }
}
