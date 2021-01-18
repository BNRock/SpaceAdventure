package adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

/**
 * METHOD CORRECT BEHAVIOUR ASSUMPTION
 * getConnectedRoom() will always return the correct room
 * when given the first letter or full name of a compass direction
 * or the direction up or down, no matter the case of the word.
 */

public class JSONTest{
    private Game myGame = new Game();

@Before
public void setup(){
}

@Test(expected = Exception.class)
public void testExitSyntax() throws Exception{
    System.out.println("Testing generate adventure with bad exit syntax");
    myGame.generateAdventure(myGame.loadJSON("badexit.json"));
}
@Test(expected = Exception.class)
public void testNoExit() throws Exception{
    System.out.println("Testing generate adventure with rooms with no exits");
    myGame.generateAdventure(myGame.loadJSON("noexit.json"));
}
@Test(expected = Exception.class)
public void testBadItem() throws Exception{
    System.out.println("Testing generate adventure with items in room that aren't in the dungeon");
    myGame.generateAdventure(myGame.loadJSON("baditem.json"));
}
@Test(expected = Exception.class)
public void testEntranceID() throws Exception{
    System.out.println("Testing generate adventure with entrance IDs that don't correspond to rooms in the dungeon");
    myGame.generateAdventure(myGame.loadJSON("badentranceID.json"));
}
}
