
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;

// Pebble game class
public class PebbleGame {

    // PebbleGame attributes
    public static final Scanner input = new Scanner(System.in);
    public static String[] output;  //Where each players output will be written to
    private  volatile static boolean gameWon;

    // Player class
    public static class Player implements Runnable{

        // Player attributes
        private static final CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        private final int id;
        private  volatile ArrayList<Pebble> pebbleArrayList = new ArrayList<>();
        private int runningTotal;
        private volatile Bag lastBag; // The bag where the last draw came from

        //constructor
        public Player(int id){
            this.id = id;
        }

        // add a player to the game
        public void addPlayer(int id){players.add(new Player(id));}

        // adds a pebble to the player's list of pebbles and add the weight to the sum
        public void addPebble(Pebble pebble){
            pebbleArrayList.add(pebble);
            runningTotal += pebble.getWeight();
        }

        // removes a pebble from the player's list of pebbles and substracts its weight from the sum
        public void removePebble(Pebble pebble){
            pebbleArrayList.remove(pebble);
            runningTotal -= pebble.getWeight();
        }
        public static CopyOnWriteArrayList<Player> getPlayers(){
            return players;
        }

        // makes the player draw ten pebble
        public synchronized void drawTen() throws IOException {

            Pebble chosenPebble;

            //pick a random black bag
            Random rand = new Random();
            int bagNumber = rand.nextInt(3);
            // Picks the random black bag
            Bag blackBag = Bag.getBlackBags().get(bagNumber);
            //Loops until it finds a black bag with enough pebbles in
            while(blackBag.getPebbles().size() < 10){
                bagNumber = rand.nextInt(3);
                blackBag = Bag.getBlackBags().get(bagNumber);
            }

            // Select the ten pebbles randomly
            for(int i = 0; i < 10; i++){
                synchronized (blackBag){
                    //select the pebble
                    chosenPebble = blackBag.getPebbles().get(rand.nextInt(Bag.getSize(blackBag)));
                    //remove the pebble from black bag
                    Bag.removePebble(blackBag, chosenPebble);
                }
                // adds pebble to player
                addPebble(chosenPebble);
                lastBag = blackBag;
                output[id] = "player" + id + " has drawn a "+ chosenPebble.getWeight() + " from bag "+blackBag.getBagLetter() + '\n';
                printOutputs(id);
                output[id] = "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
                printOutputs(id);
            }
            // checks if the player has already won
            checkWin();
        }

        // draws a random pebble from a random bag
        public void draw() throws InterruptedException {
            // Randomly pick black bag
            //check all the bags arn't empty
            int bagNum = ThreadLocalRandom.current().nextInt(0,3);
            Bag blackBagObj = Bag.getBlackBags().get(bagNum);
            Bag whiteBagObj = Bag.getWhiteBags().get(bagNum);
            CopyOnWriteArrayList<Pebble> blackBag = new CopyOnWriteArrayList<>();

            //Checks if a bag is empty before drawing
            while(true){
                blackBag = Bag.getBlackBags().get(bagNum).getPebbles();
                CopyOnWriteArrayList<Pebble> whiteBag = Bag.getWhiteBags().get(bagNum).getPebbles();
                // if it's empty
                if(blackBag.size() == 0){
                    // adds the contents of the white bag to the black bag
                    blackBag.addAll(whiteBag);
                    output[id] = "Filling bag " + blackBagObj.getBagLetter()+ " with bag " +whiteBagObj.getBagLetter() + "\n";
                    printOutputs(id);
                    // empties the whitebag
                    whiteBag.clear();
                }
                // if its not empty
                else {break;}
            }

            synchronized (blackBag){
                //In case you try and draw from an empty bag
                if(blackBag.size() < 1) {
                    draw();
                }
                int pebbleIndex = ThreadLocalRandom.current().nextInt(0, blackBag.size());
                //remove random pebble from black bag: sync
                Pebble chosenPebble = blackBag.get(pebbleIndex);  //select the pebble
                blackBag.remove(chosenPebble);   //remove the pebble from black bag
                output[id] = "player" + id + " has drawn a "+ chosenPebble.getWeight() + " from bag "+blackBagObj.getBagLetter() + '\n';
                printOutputs(id);
                //add it to player
                addPebble(chosenPebble);
                output[id] = "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
                printOutputs(id);

                lastBag = blackBagObj;
            }
            Thread.sleep((long)(Math.random() * 50));
            checkWin();
        }

        // discard a random pebble
        public void discard(){
            // pick a random pebble
            Random random = new Random();
            int indexPebble = random.nextInt(pebbleArrayList.size());
            Pebble chosenPebble = pebbleArrayList.get(indexPebble);
            // remove it from the pebble
            removePebble(chosenPebble);
            // adds it to the corresponding white bag
            Bag whiteBag = switch (lastBag.getBagLetter()) {
                case 'X' -> Bag.getWhiteBags().get(0);
                case 'Y' -> Bag.getWhiteBags().get(1);
                case 'Z' -> Bag.getWhiteBags().get(2);
                default -> throw new IllegalStateException("Unexpected value: " + lastBag.getBagLetter());
            };
            // adds the random pebble to the white bag
            synchronized (whiteBag){
                whiteBag.addPebble(chosenPebble);
                output[id] = "player" +id+ " has discarded a "+ chosenPebble.toString() + " to bag " +whiteBag.getBagLetter() + "\n";
                printOutputs(id);
                output[id] = "player" + id+" hand is " + pebbleArrayList.toString() + "\n";
                printOutputs(id);
            }
            // check if the player has won
            checkWin();
        }

        // Check if the player has won
        public void checkWin(){
            // if he player has 10 pebbles and their weight is 100
            if(pebbleArrayList.size() == 10 && runningTotal == 100){
                    gameWon = true;
                    System.out.println("Player" +id+ " has won the game!!!!");
                    output[id] = "player" +id+ " has won the game.";
                    printOutputs(id);
            }
        }

        // run method for thread
        public void run(){
            try {
                System.out.println("Player" + id + " is starting");
                // player draws 10 pebbles
                drawTen();
                // keeps looping until the player has won
                while (!gameWon){
                    discard();
                    draw();
                }
            }catch (InterruptedException e){
                System.out.println("The game has been running for too long without a winner. Closing now");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // adds a player to the static arraylist of players
    public static void addPlayer(int id) {
        PebbleGame.Player.players.add(new Player(id));
    }

    //Read file Method

    // creates and verifies the file
    public static String[] addFile(String filename) {

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
        CopyOnWriteArrayList<Bag> blackBags = Bag.getBlackBags();
        int i = 0;

        while(i<3) {

            Bag bag = blackBags.get(i);

            // input the file
            System.out.println("Please enter the location of bag number " + i + " to load:");
            String filename = input.next();

            File file = new File(filename);

            // checks if the file is in the directory
            while(!file.exists()) {
                // input the file
                System.out.println("This file is not in the directory!");
                System.out.println("Please enter the location of bag number " + i + " to load:");
                filename = input.next();

                file = new File(filename);
                if(file.exists()) {
                    break;
                }
            }

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

    // checks the file is in the correct format
    public static Boolean validateWeights(String line) {
        //Checks file content is in the correct format
        if (line.matches("(\\d+(,)?\\s?)+")) {
            return true;
        }
        else{
            return false;
        }
    }


    //Adds all the white and black bags to the arrays
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

    // create all the player
    public static void setupPlayers(){

        //input the amount of player
        System.out.println("Please enter the number of players:");

        // if valid input
        if(input.hasNextInt()){
            try{

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
            }catch (InputMismatchException e){
                input.nextLine();
                System.out.println("Please only enter numbers ");
                setupPlayers();
            }
        // if invalid input
        }else {
            if(input.hasNext()){
                String err = input.nextLine();
                // if the user inputs "E"
                if(err.equals("E")){
                    // quit the game
                    System.out.println("Quitting");
                    System.exit(0);
                }else {
                    System.out.println("Please enter a number");
                    setupPlayers();
                }
            }
        }

    }



    // Takes a bag object and uses a Str[] to fill it with weights
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

    // write the outputs to the file
    public static void printOutputs(int id) {

        try {
                // Writes the output files to the directory
                File yourFile = new File("Player Outputs/player" + id + "_output.txt");
                yourFile.createNewFile();
                Path path = Paths.get("Player Outputs/player" + id + "_output.txt");
                Files.write(path, Collections.singleton((output[id])), StandardCharsets.UTF_8, StandardOpenOption.APPEND);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main method
    public static void main(String[] args) throws IOException {

        // Creates the white and Black Bags
        setupBags();
        System.out.println("\nWelcome to the PebbleGame!!");
        System.out.println("You will be asked to enter the number of players.");
        System.out.println("and then for the location of three files in turn containing comma separated integer values for the pebble weights.");
        System.out.println("The integer values must be strictly positive.");
        System.out.println("The game will then be simulated, and written to files in this directory.\n");
        // Asks for player number and creates the player objects
        setupPlayers();
        // creates the bag
        askForBags();
        // Creates new directory for the outputs
        File dirLocation = new File("Player Outputs/");
        // Removes any old output files
        if (!dirLocation.mkdir()) {
            for (File file : dirLocation.listFiles()) {
                file.delete();
            }
        }

        // creates the thread pool
        ExecutorService es = Executors.newFixedThreadPool(Player.players.size());
        // loop through the players
        for (int i = 0; i < Player.players.size(); i++) {
            // create the thread
            Thread player = new Thread(new Player(i));
            es.execute(new Thread(player));
        }
        es.shutdown();
        try {
            es.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!es.isTerminated()){
            es.shutdownNow();
            System.out.println("The Game has run for a while, it may not be possible to win. \nShutting Down.");
            System.exit(0);
        }
    }


}
