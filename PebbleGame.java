import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PebbleGame {
    public static final Scanner input = new Scanner(System.in);

    //Should have a nested player class
    public static class Player{

        private static ArrayList<Player> players = new ArrayList<>();
        private int id;
        //constructor
        public Player(int id){
            this.id = id;
        }

        //Methods
        public void addPlayer(int id){players.add(new Player(id));}
    }

    public static void addPlayer(int id) {
        PebbleGame.Player.players.add(new Player(id));
    }

    //Read file Method

    /**
     * Takes the name of the file for a black bag. Validates the content format with validateWeights method.
     * Checks there are 11 times the amount of pebbles than players.
     * @param filename name of black bag input file
     * @return a Str[] containing all the weights of pebbles
     * @throws IOException TODO: Catch it
     */
    public static String[] addFile(String filename) throws IOException {

        String line = null;
        BufferedReader bufferedReader = null;
        String[] weights = null;

        try {
            //reading the file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            // Validate the file content format
            while ((line = bufferedReader.readLine()) != null){
                //Validates the file content is in the correct format
                if(validateWeights(line)){
                    weights = line.replaceAll("\\s+","").split(",");
                }else {
                    System.out.println("The file didn't follow the specified format.");
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            // close the buffered reader
            //bufferedReader.close();
        }
        // Checks if there is 11 times the amount of pebbles than players
        if(weights.length < 11*Player.players.size()){
            System.out.println("There is not 11 Times the amount of pebbles than players, please input another file");
            return null;
        }
        return weights;
    }

    public static void askForBags() throws IOException {
        ArrayList<Bag> blackBags = Bag.getBlackBags();
        int i = 0;

        while(i<3) {

            Bag bag = blackBags.get(i);

            // input the file
            System.out.println("Please enter the location of bag number " + i + " to load:");
            String filename = input.next();
            // store the weights
            String[] weights = null;
            weights = PebbleGame.addFile(filename);

            // fill the bag with the weights only if the file was validated correctly
            if(weights != null){
                PebbleGame.fillBag(bag, weights);
                i++;
            }

        }

        input.close();
    }

    /**
     * Checks the file follows the correct format
     * @param line
     * @return Bool. True if follows correct format
     */
    public static Boolean validateWeights(String line) {
        //Checks file content is in the correct format
        if (line.matches("(\\d+(,)?\\s?)+")) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Adds all the white and black bags to the arrays
     */
    public static void setupBags(){
        // Add the black bags to the array
        Bag.setBlackBags(new Bag("black", 'X'));
        Bag.setBlackBags(new Bag("black", 'Y'));
        Bag.setBlackBags(new Bag("black", 'Z'));
        // Add the white bags to the array
        Bag.setWhiteBags(new Bag("white", 'A'));
        Bag.setWhiteBags(new Bag("white", 'B'));
        Bag.setWhiteBags(new Bag("white", 'C'));
    }
    public static void setupPlayers(){
        //input the amount of players
        System.out.println("Please enter the number of players:");
        int number = input.nextInt();
        // Checks the input is strictly positive
        while(number < 1){
            System.out.println("Error: Number of players must be strictly positive. Please retry");
            number = input.nextInt();
        }
        // create the number of players as inputted and add them to the list of players
        for(int i=0; i<number; i++) {
            PebbleGame.addPlayer(i);
        }
        System.out.println(Player.players);
    }



    /**
     * Takes a bag object and uses a Str[] to fill it with weights
     * @param bag bag object (should be black)
     * @param weights String list filled with string type integers separated by commas
     */
    public static void fillBag(Bag bag, String[] weights) {

        int weight;

        // iterate through the array
        for (int i=0; i < weights.length; i++) {
            weight = Integer.parseInt(weights[i]);
            if(weight < 1) {
                throw new RuntimeException("The weight must be positive");
            }
            bag.addPebble(new Pebble(weight));
        }
    }

    public static void main(String[] args) throws IOException {
        // Pre threading
        /**
         * To-Do:
         * Strictly positive number of players :)
         * Three white bads A,B,C :)    yes
         * Three black bags X,Y,Z :)    yes
         *
         * Request the number of players ---> Err: Players are greater than 0   yes
         * Request the location of the 3 Files ---> Err: Correct file type      no
         * Validate the 3 files ---> In the right format, only contains integers, each bag contains 11 times the number of players  yes
         */

        // Creates the white and Black Bags
        setupBags();


        System.out.println("\nWelcome to the PebbleGame!!");
        System.out.println("You will be asked to enter the number of players.");
        System.out.println("and then for the location of three files in turn containing comma separated integer values for the pebble weights.");
        System.out.println("The integer values must be strictly positive.");
        System.out.println("The game will then be simulated, and written to files in this directory.\n");

        // Asks for player number and creates the player objects

        setupPlayers();

        askForBags();


        //Post Threading - starts before they draw their initial pebbles
        /**
         * Each player take 10 pebbles
         * win condition: player has ten pebbles with weight totalling exactly 100
         * Once black bag empty, all pebbles from white bag emptied into it. X filled by A, Y by B
         * Drawing black pebbles is uniformly at random
         * players act as concurrent threads
         * Drawing and discarding are atomic actions (cont)
         * Bag pebble is discarded to is paired to white bag that pebble just came from
         * If player attempts to draw from empty bag, try again till they select a bag with pebbles
         */
        ArrayList<Bag> blackBags = Bag.getBlackBags();
        ArrayList<Bag> whiteBags = Bag.getWhiteBags();
    }


}
