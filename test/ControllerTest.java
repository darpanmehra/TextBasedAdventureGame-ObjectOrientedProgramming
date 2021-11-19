import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Random;

import dungeon.controller.Controller;
import dungeon.controller.IController;
import dungeon.model.GameState;
import dungeon.model.IGameState;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the Controller class.
 */
public class ControllerTest {

  private IGameState model;

  @Before
  public void setUp() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 20, 5, rand);
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    // Testing when something goes wrong with the Appendable
    // Here we are passing it a mock of an Appendable that always fails
    StringReader in = new StringReader("M west m west m west s south 1"
            + " s south 1 m south m south");
    Appendable gameLog = new FailingAppendable();
    IController c = new Controller(in, gameLog, model);
    c.playGame();
  }

  @Test
  public void testMovePlayer() {

    StringReader in = new StringReader("M North q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving NORTH"));
  }

  @Test
  public void testPickTreasureArrow() {
    StringReader in = new StringReader("P arrow Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Picking up Arrows"));
    assertTrue(out.toString().contains("You are in a cave and you have 4 Arrows."));
  }

  @Test
  public void testShootArrow() {
    StringReader in = new StringReader("S north 1 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Shooting in NORTH at distance of 1"));
  }

  @Test
  public void testShootArrowInvalidDirection() {
    StringReader in = new StringReader("S south 1 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Direction is not valid"));
  }

  @Test
  public void testShootArrowInvalidDistance() {
    StringReader in = new StringReader("S north -1 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid caves count"));
  }

  @Test
  public void testShootArrowInvalidDistance2() {
    StringReader in = new StringReader("S north 6 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid caves count"));
  }

  @Test
  public void testShootArrowInvalidDistance3() {
    StringReader in = new StringReader("S north 0 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid caves count"));
  }

  @Test
  public void testShootToKillAndEnterSameCave() {
    StringReader in = new StringReader("S north 1 S north 1 M north M west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    // Sees a dead monster when player moves west before quiting
    assertTrue(out.toString().contains("You see a dead monster here."));
  }

  @Test
  public void testShootOneArrowEntersItAndDies() {
    StringReader in = new StringReader("S north 1 M north M west M west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Game Over!"));
    assertTrue(out.toString().contains("Chomp, chomp, chomp, you are eaten by an Otyugh!"));
  }

  @Test
  public void testGettingKilledAtEndLocation() {

    StringReader in = new StringReader("M north P arrows S west 1 S west 1 M west "
            + "M west P arrows S north 1 S north 1 M north M west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();

    assertTrue(out.toString().contains("Game Over!"));
    assertTrue(out.toString().contains("Chomp, chomp, chomp, you are eaten by an Otyugh!"));
  }

  @Test
  public void testQuitGame() {
    StringReader in = new StringReader("Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You quit the game."));
  }

  @Test
  public void testInvalidCommandInput() {
    StringReader in = new StringReader("Go north Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Not a valid command: north"));
  }

  @Test
  public void testInvalidDirection() {
    StringReader in = new StringReader("M south Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Cannot move in that direction"));
  }

  @Test
  public void testInvalidCavesCount() {
    StringReader in = new StringReader("S north -1 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid caves count"));
  }

  @Test
  public void testWinGame() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("M west m west m west s south 1"
            + " s south 1 m south m south");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You win! You made it to the end."));

  }

  @Test
  public void testLessPungentSmell() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("M west m west m south Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You slightly smell something nearby"));
  }

  @Test
  public void testMorePungentSmell() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("M west m west m north Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You smell something terrible nearby"));
  }

  @Test
  public void testOutOfArrowsMessage() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("S east 1 S east 1 S east 1 M west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You are out of arrows. Explore to find more."));
  }

  @Test
  public void testMoveNorth() {
    StringReader in = new StringReader("M north Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving NORTH"));
  }

  @Test
  public void testMoveSouth() {
    StringReader in = new StringReader("M north M south Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving SOUTH"));
  }

  @Test
  public void testMoveEast() {
    StringReader in = new StringReader("S north 1 S north 1 M north M west M east Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving EAST"));
  }

  @Test
  public void testMoveWest() {
    StringReader in = new StringReader("M north M west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving WEST"));
  }

  @Test
  public void testMoveInWrappingDungeonWestEast() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 100,
            "wrapping", 100, 5, rand);

    // As per the model created, the player starting position is (4,5) which is the right
    // edge of the dungeon. The player makes a wrap move to the right, which is (4,0).

    StringReader in = new StringReader("M east Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving EAST"));
  }

  @Test
  public void testMoveInWrappingDungeonNorthSouth() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 100,
            "wrapping", 100, 5, rand);

    // As per the model created, the player starting position is (4,5) which is the right
    // edge of the dungeon. The player makes a wrap move to the right, which is (4,0). Then the
    // player goes south to (5,0). After this, the player makes a wrap move to the south,
    // which goes to (0,0).

    StringReader in = new StringReader("M east M South M South Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Moving EAST"));
  }

  @Test
  public void testTreasureInformation() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("M west m west Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("You find 9 Diamonds, 3 Sapphires, 9 Rubies"));
  }

  @Test
  public void testPickInvalidTreasure() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("P xjknwkxn Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid treasure type"));
  }

  @Test
  public void testPickUpNonExistingTreasure() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("P arrows Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Location does not have arrows"));
  }

  @Test
  public void testShootInvalidDirection() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("S south-east Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid direction"));
  }

  @Test
  public void testShootWrongDirection() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("S south 1 Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Direction is not valid"));
  }

  @Test
  public void testMoveInvalidDirection() {
    Random rand = new Random();
    rand.setSeed(50);
    model = new GameState(6, 6, 0,
            "nonwrapping", 100, 5, rand);

    StringReader in = new StringReader("M south-east Q");
    Appendable out = new StringBuffer();
    IController controller = new Controller(in, out, model);
    controller.playGame();
    assertTrue(out.toString().contains("Invalid direction"));
  }

}