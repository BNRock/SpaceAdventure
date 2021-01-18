package adventure;

/**
 * This class parses the user command and decides which 
 * command constructor to call.
 */
public class Parser{

    /**
     * Parses the user command and calls different command 
     * constructor based on how many terms are in the user command.
     * @param userCommand string containing the command the user entered
     * @return a valid command
     * @throws InvalidCommandException if the command the user entered isn't valid
     */
    public Command parseUserCommand(String userCommand) throws InvalidCommandException{
        String[] split = userCommand.split(" ", 2);
        Command userCmd = null;
        try{
            if(split.length == 1){userCmd = new Command(split[0]);}
            else if (split.length == 2){userCmd = new Command(split[0], split[1]);}
            else{userCmd = new Command();}
            userCmd.checkOverallValidity();
        }catch (InvalidCommandException ex){
            throw ex;
        }
        return userCmd;
    }

    /**
     * Gets list of all valid commands
     * @return string containing all valid commands
     */
    public String allCommands(){
        String string = "look take inventory go quit eat wear toss read";
        return string;
    }

    /**
     * Gets a string containing a title and all the valid commands.
     * @return string containing title and all valid commands
     */
    public String toString(){
        String str = "Parser commands: " + allCommands() + "\n";
        
        return str;
    }
}
