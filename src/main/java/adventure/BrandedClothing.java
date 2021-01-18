package adventure;
import org.json.simple.JSONObject;

/**
 * Represent an branded clothing item
 */
public class BrandedClothing extends Clothing implements Readable{
    
    /**
     * Default constructor, calls super constructor to set members.
     */
    public BrandedClothing(){
        super();
    }

    /**
     * Overloaded constructor, calls super constructor to set members.
     * @param item JSONObject containing item details
     */
    public BrandedClothing(JSONObject item){
        super(item);
    }

    /**
     * Gets a string containing item description.
     * @return string containing item description 
     */
    public String read(){
        String str = "You read the item: '" + getLongDescription() + "'. Fascinating!\n";
        return str;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Branded Clothing Name: " + getName() + "\nID: " + getID() 
                     + "\nDescription: " + getLongDescription() + "\n";

        if(getContainingRoom() != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
