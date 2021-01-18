package adventure;

/**
 * This class represents an exception for an invalid command.
 */
public class InvalidCommandException extends Exception{
    public InvalidCommandException(String errorMessage){
        super(errorMessage);
    }
}
