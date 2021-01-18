package adventure;
import org.json.simple.JSONObject;

/**
 * Represents small food item.
 */
public class SmallFood extends Food implements Tossable {

    /**
     * Default constructor. Calls super constructor.
     */
    public SmallFood(){
        super();
    }

    /**
     * Overridden constructor. Calls super constructor.
     * @param item JSONObject containing item details
     */
    public SmallFood(JSONObject item){
        super(item);
    }

    /**
     * Confirms user tossed item.
     * @return string containing confirmation user tossed item.
     */
    public String toss(){
        String str = "You tossed the " + getName() 
        + " from your inventory. I guess you're no longer hungry!\n";
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Small Food Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
