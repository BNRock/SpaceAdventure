package adventure;
import org.json.simple.JSONObject;

/**
 * Represents a Spell Item
 */
public class Spell extends Item implements Readable{

    /**
     * Default constructor. Calls super constructor.
     */
    public Spell(){
        super();
    }

    /**
     * Overridden constructor. Calls super constructor.
     * @param item JSONObject containing item details
     */
    public Spell(JSONObject item){
        super(item);
    }

    /**
     * Confirms user read item and gets description.
     * @return string containing confirmation user read item and item description.
     */
    public String read(){
        String str = "You read the item: '" + getLongDescription() + "'. Sounds cool!\n";
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Spell Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
