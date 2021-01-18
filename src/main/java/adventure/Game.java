package adventure;
import java.util.Scanner;
import java.io.Reader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This is the class that runs the game and handles user input.
 */
public class Game{
    private Scanner sc;
    private final String defaultFilename = "adventure.json";
    private JSONObject jsonAdventure;
    private static Adventure adventure;
    private Parser parser;
    private static Player player;

    /**
     * Default constructor for Game class. Initializes member variable.
     */
    public Game(){
        parser = new Parser();
        player = null;
        sc = new Scanner(System.in);
        jsonAdventure = null;
        adventure = null;
    }

    /**
     * @param scanner scanner object to be set
     */
    public void setSc(Scanner scanner){
        sc = scanner;
    }

    /**
     * @param obj JSONObject containing adventure details
     */
    public void setJsonAdventure(JSONObject obj){
        jsonAdventure = obj;
    }

    /**
     * @param p parser object to be set
     */
    public void setParser(Parser p){
        parser = p;
    }

    public static void main(String[] args){
        Game theGame = new Game();

        // 1. Print a welcome message to the user
        theGame.loadFileOrSave(args);
        // 4. Print the beginning of the adventure
        // 5. Begin game loop here
        
        while(theGame.sc.hasNext()){
            String toPrint = adventure.parseCommand(theGame.getUserCommand());
            if(toPrint == null){theGame.confirmQuit();}
            else{System.out.print(toPrint);}
            System.out.println();
            System.out.print("> ");   
        }
    }

    /**
     * Parse the given .json file and get a JSONObject that contains the .json data.
     * @param filename the relative filepath of the .json adventure file
     * @return a JSONObject containing the info inside the given .json file
     */
    public JSONObject loadJSON(String filename){
        JSONParser myParser = new JSONParser();
        JSONObject jsonAdv = null;
        try (Reader reader = new FileReader(filename)){
            jsonAdv = (JSONObject) myParser.parse(reader);
        } catch (Exception e) {
            return null;
        }

        return jsonAdv;
    }

    

    /**
     * Parse the given input stream and get a JSONObject that contains the input stream data.
     * @param inputStream the input stream to be read
     * @return a JSONObject containing the info inside the given input stream
     */
    public JSONObject loadJSON(InputStream inputStream){
        JSONParser myParser = new JSONParser();
        JSONObject jsonAdv = null;
        try (InputStreamReader reader = new InputStreamReader(inputStream)){
            jsonAdv = (JSONObject) myParser.parse(reader);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return jsonAdv;
    }

    /**
     * Creates and returns new adventure based on JSON object.
     * @param obj a JSONObject containing all the info of the adventure .json
     * @return the Adventure object that will be used for the game
     */
    public Adventure generateAdventure(JSONObject obj) throws Exception{
        Adventure adv = null;
        try {
            JSONObject jsonAdv = (JSONObject) obj.get("adventure");
            player = new Player();
            adv = new Adventure(jsonAdv); 
        } catch (Exception e) {
            throw e;
        }
        adventure = adv;
        return adv;
    }

    /**
     * Prints the selected room name, short description, and items in the room.
     * @param room the room to print info about
     */
    public void printNewRoom(Room room){
        System.out.println("You are in " + room.getName());
        System.out.println(room.getShortDescription());
        System.out.println("Items:");
        if(room.listItems().size() == 0){System.out.println("There are no items in this room.");}
        else{
            for(Item item : room.listItems()){
                System.out.println(item.getName());
            }
        }  
        System.out.println();
    }


    /**
     * Turns the default JSON file into an inputstream.
     * @return InputStream containing default adventure details
     */
    public InputStream getInputStream(){
        InputStream inputStream = Game.class.getClassLoader().getResourceAsStream(defaultFilename);
        return inputStream;
    }

    /**
     * Checks if user want to load a new file or a save, and calls functions accordingly.
     * @param args command line arguments
     */
    public void loadFileOrSave(String[] args){
        try {
            if(args.length > 1){
                if(args[0].equals("-a")){
                    adventure = generateAdventure(loadJSON(args[1]));
                }else if(args[0].equals("-l")){
                    loadSave(args[1]);
                }else{
                    System.out.println("Command line error. Loading default adventure...\n");
                    adventure = generateAdventure(loadJSON(getInputStream()));
                }
            }else{
                System.out.println("Loading default adventure...\n");
                adventure = generateAdventure(loadJSON(getInputStream()));
            }
            gameStart();
        } catch (Exception e) {
            System.out.println("Error: JSON file cannot be parsed.");
            System.exit(1);
        }
    }

    /**
     * Checks if the user wants to quit, and quits if they want to.
     */
    public void confirmQuit(){
        if(yesOrNo("Do you really want to quit? (Y/N)")){
            if(yesOrNo("Do you want to save your game? (Y/N)")){saveGame(promptForSaveName());}
            else{
                System.out.println("Thanks for playing! Have a nice day.");
                sc.close();
                System.exit(0);
            }    
        }else{System.out.println("Ok. Thanks for not giving up on me :).");}
    }

    /**
     * Prompts user for name of their saved file.
     * @return string containing what the user wants to call their save file
     */
    private String promptForSaveName(){
        String str = "";
        System.out.println("What would you like to name your save file?");
        System.out.print("> ");
        str = sc.nextLine();

        return str;
    }

     /**
     * Prints a starting message and prints the starting room details
     */
    public void gameStart(){
        System.out.println("Starting adventure now! Type 'help' if you need instructions.");
        if(player.getName().equals("")){
            setUserName();
        }
        player.setCurrentRoom(adventure.getCurrentRoom());

        printNewRoom(adventure.getCurrentRoom());
        System.out.print("> ");
    }


    /**
     * Asks the user for their name and sets the player name accordingly.
     */
    public void setUserName(){
        System.out.println("What's your name?");
        System.out.print("> ");
        player.setName(sc.nextLine());
        System.out.println("Hey there " + player.getName() + "!\n");
    }

    /**
     * Keep asking for a command from user until its a valid command.
     * @param userCommand user's command string
     * @return a valid user command.
     */
    public String commandOutput(String userCommand){
        Command cmd = null;
        try {
            cmd = parser.parseUserCommand(userCommand);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        }
        return adventure.parseCommand(cmd);
    }

    /**
     * Loads a serialized game state
     * @param filename string containing filepath of saved game state.
     * @return true if succesful false if not
     */
    public boolean loadSave(String filename){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){ 
            adventure = (Adventure)in.readObject();
            player = (Player)in.readObject();
            System.out.println("Game state successfully loaded.");
            System.out.println("Welcome back to your game, " + player.getName() + ".");
        } catch(Exception e) { 
            System.out.println("Error: Couldn't open file.");
            return false;
        }

        return true;
    }

    /**
     * Serializes the game state if the user requests.
     * @param filename the filename to name the file
     * @return true if success, false if not
     */
    public boolean saveGame(String filename){
        player.setSaveGameName(filename);
        try(FileOutputStream fileStream = new FileOutputStream(player.getSaveGameName());
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream)){

            outputStream.writeObject(adventure); 
            outputStream.writeObject(player);
            
        } catch (Exception e) {
            System.out.println("Error: Game state cannot be saved.");
            return false;
        }
        return true;
    }

    /**
     * Keep asking for a command from user until its a valid command.
     * @return a valid user command.
     */
    public Command getUserCommand(){
        boolean isGoodCommand = false;
        Command cmd = null;
        String ans = null;
        while(!isGoodCommand){
            ans = sc.nextLine().trim();
            try {
                cmd = parser.parseUserCommand(ans);
                isGoodCommand = true;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                isGoodCommand = false;
                System.out.println();
                System.out.print("> ");
            }
        }
        return cmd;
    }


    /**
     * Keeps asking user for a yes or no to a given prompt and returns answer.
     * @param prompt string containing prompt for the user to answer
     * @return true if yes, false if no
     */
    public boolean yesOrNo(String prompt){
        boolean isAnswer = false;
        String ans = null;
        while(!isAnswer){
            System.out.println(prompt + "\n");
            System.out.print("> ");
            ans = sc.nextLine().trim();
            if(ans.equalsIgnoreCase("N") || ans.equalsIgnoreCase("no")){
                isAnswer = true;
                return false;
            }else if(ans.equalsIgnoreCase("Y") || ans.equalsIgnoreCase("yes")){
                isAnswer = true;
                return true;
            }else{
                System.out.println("Oops! Please enter Y/N.\n");
            }
        }
        return false;
    }

    /**
     * Gets the player object in use.
     * @return player object being used by game
     */
    public static Player getPlayer(){
        return player;
    }

    /**
     * Gets the adventure object in use.
     * @return adventure object being used by game.
     */
    public static Adventure getAdventure(){
        return adventure;
    }

    /**
     * Returns details of adventure and player.
     * @return string containing adventure and player details
     */
    public String toString(){
        String str = "This is the game object. It holds the adventure: " 
        + getAdventure().toString() + "and the player: " + getPlayer().toString() + "\n";

        return str;
    }
}
