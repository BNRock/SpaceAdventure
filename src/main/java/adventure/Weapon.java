package adventure;
import org.json.simple.JSONObject;

/**
 * Represent a weapon item
 */
public class Weapon extends Item implements Tossable{

    /**
     * Default constructor. Calls super constructor.
     */
    public Weapon(){
        super();
    }

    /**
     * Overridden constructor. Calls super constructor.
     * @param item JSONObject containing item details
     */
    public Weapon(JSONObject item){
        super(item);
    }

    /**
     * Confirms user tossed itemn.
     * @return string containing confirmation user tossed item.
     */
    public String toss(){
        String str = "You tossed the " + getName() + " from your inventory."
        + " Hope you don't come across anything dangerous!\n";
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Weapon Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
