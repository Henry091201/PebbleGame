import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PebbleGame {
    public static final Scanner input = new Scanner(System.in);
    public static String[] output;  //Where each players output will be written to
    private  volatile static boolean gameWon;

    //Should have a nested player class
    public static class Player implements Runnable{

        private static final ArrayList<Player> players = new ArrayList<>();
        private int id;
        private ArrayList<Pebble> pebbleArrayList = new ArrayList<>();
        private int runningTotal;
        private Bag lastBag; // The bag where the last draw came from
        //constructor
        public Player(int id){
            this.id = id;
        }

        //Methods
        public void addPlayer(int id){players.add(new Player(id));}

        public void addPebble(Pebble pebble){
            pebbleArrayList.add(pebble);
            runningTotal += pebble.getWeight();
        }
        public void removePebble(Pebble pebble){
            pebbleArrayList.remove(pebble);
            runningTotal -= pebble.getWeight();
        }
        public synchronized void drawTen(){

            Pebble chosenPebble;

            //pick a random black bag
            Random rand = new Random();
            int bagNumber = rand.nextInt(3);
            Bag blackBag = Bag.getBlackBags().get(bagNumber);   // Picks the random black bag
            while(blackBag.getPebbles().size() < 10){           //Loops until it finds a black bag with enough pebbles in
                bagNumber = rand.nextInt(3);
                blackBag = Bag.getBlackBags().get(bagNumber);
            }

            for(int i = 0; i < 10; i++){        // Select the ten pebbles randomly
                synchronized (blackBag){
                    chosenPebble = blackBag.getPebbles().get(rand.nextInt(Bag.getSize(blackBag)));  //select the pebble
                    Bag.removePebble(blackBag, chosenPebble);   //remove the pebble from black bag
                }
                addPebble(chosenPebble); // adds pebble to player
                lastBag = blackBag;
                output[id] += "player" + id + " has drawn a "+ chosenPebble.getWeight() + "from bag "+blackBag.getBagLetter() + '\n';
                output[id] += "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
            }
            checkWin();
        }
        public void draw(){
            // Randomly pick black bag
            Pebble chosenPebble;
            Random rand = new Random();
            int bagNumber = rand.nextInt(3);
            Bag blackBag = Bag.getBlackBags().get(bagNumber);
            System.out.println(blackBag.getBagLetter());
            // Check if empty: yes --> fill corresponding; no --> draw pebble
               while(true) {//in case there are more than 1 empty bag at the same time
                   if (blackBag.getPebbles().size() == 0) {
                       System.out.println(blackBag.getPebbles().size());
                       synchronized (this) {
                           Bag.bagFill(Bag.getWhiteBags().get(bagNumber), blackBag);// Empties white bag into corresponding black bag
                           output[id] += "player" + id + " is filling bag" + blackBag.getBagLetter() + " with bag " + Bag.getWhiteBags().get(bagNumber).getBagLetter() + "\n";
                       }
                   } else {
                       break;
                   }
               }
            //remove random pebble from black bag: sync
            synchronized (blackBag){
                chosenPebble = blackBag.getPebbles().get(rand.nextInt(Bag.getSize(blackBag)));  //select the pebble
                Bag.removePebble(blackBag, chosenPebble);   //remove the pebble from black bag
            }
            output[id] += "player" + id + " has drawn a "+ chosenPebble.getWeight() + "from bag "+blackBag.getBagLetter() + '\n';
            output[id] += "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
            //add it to player
            addPebble(chosenPebble);
            lastBag = blackBag;
            checkWin();
        }
        public void discard(){
            //Remove the pebble from the player
            Random random = new Random();
            int indexPebble = random.nextInt(pebbleArrayList.size());
            Pebble chosenPebble = pebbleArrayList.get(indexPebble);
            removePebble(chosenPebble);
            Bag whiteBag;
            switch (lastBag.getBagLetter()){        // adds it to the corresponding white bag
                case 'X':
                    whiteBag = Bag.getWhiteBags().get(0);
                    break;
                case 'Y':
                    whiteBag = Bag.getWhiteBags().get(1);
                    break;
                case 'Z':
                    whiteBag = Bag.getWhiteBags().get(2);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + lastBag.getBagLetter());
            }
            synchronized (whiteBag){
                whiteBag.addPebble(chosenPebble);
                output[id] += "player" +id+ " has discarded a "+ chosenPebble.toString() + "to bag " +whiteBag.getBagLetter() + "\n";
                output[id] += "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
            }
            checkWin();
        }
        public void checkWin(){
            if(pebbleArrayList.size() == 10){
                if(runningTotal == 100){
                    gameWon = true;
                    output[id] += "player" +id+ " has won the game.";
                }
            }
        }


        public void run(){
            try {
                drawTen();
                while (!gameWon){
                    draw();
                    discard();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
        output = new String[number]; // Set the output array to the amount of players
    }



    /**
     * Takes a bag object and uses a Str[] to fill it with weights
     * @param bag bag object (should be black)
     * @param weights String list filled with string type integers separated by commas
     */
    public static void fillBag(Bag bag, String[] weights) {

        int weight;

        // iterate through the array
        for (String s : weights) {
            weight = Integer.parseInt(s);
            if (weight < 1) {
                throw new RuntimeException("The weight must be positive");
            }
            bag.addPebble(new Pebble(weight));
        }
    }
    public static void printOutputs() throws IOException {

        try {
            // Creates new directory for the outputs
            File dirLocation = new File("Player Outputs/");
            // Removes any old output files
            if (!dirLocation.mkdir()) {
                for (File file : dirLocation.listFiles()) {
                    file.delete();
                }
            }
            // Writes the output files to the directory
            for (int i = 0; i < (Player.players.size()); i++) {
                Path path = Paths.get("Player Outputs/player" + i + "_output.txt");
                Files.write(path, Collections.singleton((output[i])), StandardCharsets.UTF_8);
            }
            System.out.println("Player outputs have successfully been written to text files in the " +
                    "directory \"Player Outputs\".");

        } catch (IOException e) {
            System.out.println("Failed to write some or all outputs to file. Please make sure:" +
                    "\n\t - any old output files are not opened or used by another program," +
                    "\n\t - you have the required permissions to write to a file.");
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

        ExecutorService es = Executors.newFixedThreadPool(Player.players.size());
        for (int i = 0; i < Player.players.size(); i++) {
            Thread player = new Thread(new Player(i));
            es.execute(new Thread(player));
        }
        es.shutdown();

        // Shuts the game down after 1 minute (so that it does not run forever)
        try {
            es.awaitTermination(60, TimeUnit.SECONDS);

        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        if(!es.isTerminated()){
            es.shutdownNow();
            printOutputs();
            System.out.println("Game ran for over 1 minute and it may be impossible to simulate it, so" +
                    "it has been interrupted.");
            System.exit(0);
        }

        printOutputs();

        System.out.println("Player   draws! Saving player outputs...");
        printOutputs();
    }


}
