package adventure;
import org.json.simple.JSONObject;

/**
 * Represent a clothing item.
 */
public class Clothing extends Item implements Wearable{

    /**
     * Default constructor. Calls super constructor
     */
    public Clothing(){
        super();
    }

    /**
     * Overloaded constructor. Calls super constructor.
     * @param item JSONObject containing item details
     */
    public Clothing(JSONObject item){
        super(item);
    }

    /**
     * Puts on the item and gets feedback.
     * @return string containing item name and feedback
     */
    public String wear(){
        String str = "You put on the " + getName() + ". So chic!\n";
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Clothing Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
