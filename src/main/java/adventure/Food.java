package adventure;
import org.json.simple.JSONObject;

/**
 * Represent a food item.
 */
public class Food extends Item implements Edible{

    /**
     * Default constructor. Calls super constructor.
     */
    public Food(){
        super();
    }

    /**
     * Overridden constructor. Calls super constructor.
     * @param item JSONObject containing item details
     */
    public Food(JSONObject item){
        super(item);
    }

    /**
     * Confirms items is eaten and sets containing room to null.
     * @return string containing confirmation item is eaten
     */
    public String eat(){
        String str = "You ate the " + getName() + ". Yummy!\n";
        setContainingRoom(null);
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Food Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
