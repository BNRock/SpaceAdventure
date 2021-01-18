package adventure;

/**
 * Class representing a command.
 */
public class Command {
    private String action;
    private String noun;
    public static final String[] COMMANDS_ONE_WORD = {"look", "help", "inventory", "q", "quit", "items"};
    public static final String[] COMMANDS_TWO_WORDS = {"go", "look","take", "eat", 
    "wear", "toss", "read"};
    private Adventure adv;

  /**
     * Create a command object with default values.  
     * both instance variables are set to null
     * 
     */
    public Command() throws InvalidCommandException {
        this(null, null);
    }



  /**
     * Create a command object given only an action.  this.noun is set to null
     *
     * @param command The first word of the command. 
     * 
     */
    public Command(String command) throws InvalidCommandException{
        this(command, null);
    }

    /**
     * Create a command object given both an action and a noun.
     *
     * @param command The first word of the command. 
     * @param what      The second word of the command.
     * @throws InvalidCommandException if the command is not valid
     */
    public Command(String command, String what) throws InvalidCommandException{
        action = command;
        noun = what;
        adv = Game.getAdventure();
    }

    /**
     * Checks if whole command is valid
     */
    public void checkOverallValidity() throws InvalidCommandException{
        if(action == null){
            throw new InvalidCommandException("You must enter a command!");
        }else if(noun == null){
            validateCommandOneWord(action);
        }else{
            validateCommandTwoWords(action, noun);
        }
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     *
     * @return The command word.
     */
    public String getActionWord() {
        return action;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getNoun() {
        return noun;
    }

    /**
     * @param str first word of command
     */
    public void setAction(String str) {
        action = str;
    }

    /**
     * @param str second word of command
     */
    public void setNoun(String str){
        noun = str;
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (noun != null);
    }
    
    /**
     * Validates command if it is one word.
     * @param command the first word of the command
     * @throws InvalidCommandException if the command is not valid
     */
    public void validateCommandOneWord(String command) throws InvalidCommandException{
        action = null;
        if(isValidCommand(COMMANDS_ONE_WORD, command)){
            action = command;
        }else if(isValidCommand(COMMANDS_TWO_WORDS, command)){
            invalidOneWord(command);
        }else{
            throw new InvalidCommandException("Sorry, I don't recognize '" + command + "'."
            + " Type 'help' if you need the instructions.\n");
        }
    }

    /**
     * Throws different exceptions based on why the command is invalid.
     * @param command invalid command
     * @throws InvalidCommandException with feedback as to why the command is invalid
     */
    private void invalidOneWord(String command) throws InvalidCommandException{
        if(command.equalsIgnoreCase("go")){
            throw new InvalidCommandException("You must enter a direction after 'go'.\n");
        }else if(command.equalsIgnoreCase("take")){
            throw new InvalidCommandException("You must enter an item in the room after 'take'.\n");
        }else{
            throw new InvalidCommandException("You must enter an item in your inventory after '" + command + "'.\n");
        }
    }

    /**
     * Validates command if it is two words.
     * @param command the first word of the command
     * @param what the second word of the command
     * @throws InvalidCommandException if the command is not valid
     */
    public void validateCommandTwoWords(String command, String what) throws InvalidCommandException{
        action = null;
        noun = null;
        if(isValidCommand(COMMANDS_TWO_WORDS, command)){
            if(validateSpecificTwoWordCommand(command, what)){
                action = command;
                noun = what;
            }
        }else{
            throw new InvalidCommandException("Sorry, I don't recognize '" + command + "\n");
        }
    }

    /**
     * Calls the appropriate method to deal with command and returns validity
     * @param command first word of command
     * @param what second work of command
     * @return true if valid command, false if not
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateSpecificTwoWordCommand(String command, String what) throws InvalidCommandException{
        boolean result = false;

        if(command.equalsIgnoreCase("look")){ result = validateLook(what);}
        else if (command.equalsIgnoreCase("take")){ result = validateTake(what);}
        else if (command.equalsIgnoreCase("go")){ result = validateGo(what);}
        else if (command.equalsIgnoreCase("eat")){ result = validateEat(what);}
        else if (command.equalsIgnoreCase("wear")){ result = validateWear(what);}
        else if (command.equalsIgnoreCase("toss")){ result = validateToss(what);}
        else if (command.equalsIgnoreCase("read")){ result = validateRead(what);}

        return result;
    }

    /**
     * Validates look command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateLook(String what) throws InvalidCommandException{
        for(Item item : adv.getCurrentRoom().listItems()){
            if(what.equalsIgnoreCase(item.getName())){
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in the room."
        + " Type 'items' for a list of items in the room.\n");
    }

    /**
     * Validates take command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateTake(String what) throws InvalidCommandException{
        for(Item item : adv.getCurrentRoom().listItems()){
            if(what.equalsIgnoreCase(item.getName())){
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in the room."
        + " Type 'items' for a list of items in the room.\n");
    }

    /**
     * Validates go command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateGo(String what) throws InvalidCommandException{
        if(adv.getCurrentRoom().getConnectedRoom(what) != null){
            return true;
        }
        if(what.matches("(?i)North|East|South|West|N|E|S|W|Up|Down")){
            throw new InvalidCommandException("You cannot go " + what + ".\n");
        }else{
            throw new InvalidCommandException(what + " is not a valid direction.\n");
        }
    }

    /**
     * Validates eat command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateEat(String what) throws InvalidCommandException{
        for(Item item : Game.getPlayer().getInventory()){
            if(what.equalsIgnoreCase(item.getName())){
                if(!(item instanceof Food || item instanceof SmallFood)){
                    throw new InvalidCommandException("You can't eat the " + item.getName() + "!\n");
                }
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in your inventory."
        + " Type 'inventory' for a list of items in your inventory.\n");
    }

    /**
     * Validates wear command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateWear(String what) throws InvalidCommandException{
        for(Item item : Game.getPlayer().getInventory()){
            if(what.equalsIgnoreCase(item.getName())){
                if(!(item instanceof Clothing || item instanceof BrandedClothing)){
                    throw new InvalidCommandException("You can't wear the " + item.getName() + "!\n");
                }
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in your inventory."
        + " Type 'inventory' for a list of items in your inventory.\n");
    }

    /**
     * Validates toss command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateToss(String what) throws InvalidCommandException{
        for(Item item : Game.getPlayer().getInventory()){
            if(what.equalsIgnoreCase(item.getName())){
                if(!(item instanceof SmallFood || item instanceof Weapon)){
                    throw new InvalidCommandException("You can't toss the " + item.getName() + "!\n");
                }
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in your inventory."
        + " Type 'inventory' for a list of items in your inventory.\n");
    }

    /**
     * Validates read command
     * @param what the second word of the command
     * @return true if valid
     * @throws InvalidCommandException if command is invalid
     */
    private boolean validateRead(String what) throws InvalidCommandException{
        for(Item item : Game.getPlayer().getInventory()){
            if(what.equalsIgnoreCase(item.getName())){
                if(!(item instanceof BrandedClothing || item instanceof Spell)){
                    throw new InvalidCommandException("You can't read the " + item.getName() + "!\n");
                }
                return true;
            }
        }
        throw new InvalidCommandException(what + " is not an item in your inventory."
        + " Type 'inventory' for a list of items in your inventory.\n");
    }
    /**
     * Evaluates whether or not the user's command is valid according to COMMANDS array.
     * @param commandsList list of valid commands
     * @param userCommand string containing the user's command
     * @return true if valid, false if not
     */
    public boolean isValidCommand(String[] commandsList, String userCommand){
        for(String command : commandsList){
            if(userCommand.equalsIgnoreCase(command)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a string containing command details.
     * @return string containing first and second word of command
     */
    public String toString(){
        String str = "Command first word: " + getActionWord() + "\nSecond word: "
                     + getNoun() + "\n";

        return str;
    }
}
