import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dungeon.model.location.ILocation;
import dungeon.model.location.Location;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.Treasure;
import dungeon.model.treasure.TreasureType;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the Treasure class.
 */
public class TreasureTest {

  private ITreasure treasure;

  @Before
  public void setUp() throws Exception {
    Random random = new Random();
    random.setSeed(50);
    treasure = new Treasure(random);
  }

  @Test
  public void initializeTreasure() {
    Map<TreasureType, Integer> expected = new HashMap<>();
    expected.put(TreasureType.RUBIES, 8);
    expected.put(TreasureType.SAPPHIRES, 4);
    expected.put(TreasureType.DIAMONDS, 9);
    treasure.initializeTreasure();
    assertEquals(expected, treasure.getTreasure());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testIncorrectTreasureCreation() {
    treasure = new Treasure(null);
  }

  @Test
  public void getTreasure() {
    Map<TreasureType, Integer> expected = new HashMap<>();
    expected.put(TreasureType.RUBIES, 8);
    expected.put(TreasureType.SAPPHIRES, 4);
    expected.put(TreasureType.DIAMONDS, 9);
    treasure.initializeTreasure();
    assertEquals(expected, treasure.getTreasure());
  }

  @Test
  public void testToString() {
    treasure.initializeTreasure();
    String expected = "4 Sapphires, 8 Rubies, 9 Diamonds";
    assertEquals(expected, treasure.toString());
  }

  @Test
  public void addRemoveTreasureToLocation() {
    Random random = new Random();
    random.setSeed(10);
    ILocation location = new Location(1, 1, random);
    ILocation east = new Location(1, 2, random);
    location.joinLocationToEastDirection(east);

    location.setTreasure();
    assertEquals("4 Sapphires, 4 Rubies, 1 Diamonds", location.getTreasure().toString());

    location.removeTreasure(TreasureType.RUBIES);
    assertEquals("4 Sapphires, 3 Rubies, 1 Diamonds", location.getTreasure().toString());
    location.removeTreasure(TreasureType.RUBIES);
    assertEquals("4 Sapphires, 2 Rubies, 1 Diamonds", location.getTreasure().toString());
    location.removeTreasure(TreasureType.RUBIES);
    assertEquals("4 Sapphires, 1 Rubies, 1 Diamonds", location.getTreasure().toString());
    location.removeTreasure(TreasureType.RUBIES);
    assertEquals("4 Sapphires, 1 Diamonds", location.getTreasure().toString());


    location.addArrow();
    location.addArrow();
    assertEquals("4 Sapphires, 2 Arrows, 1 Diamonds", location.getTreasure().toString());

  }

  @Test
  public void testAddArrows() {
    treasure.initializeTreasure();
    treasure.addArrow();
    treasure.addArrow();
    treasure.addArrow();
    treasure.addArrow();
    assertEquals("4 Sapphires, 8 Rubies, 4 Arrows, 9 Diamonds", treasure.toString());
  }

  @Test
  public void testRemoveArrows() {
    treasure.initializeTreasure();
    treasure.addArrow();
    treasure.addArrow();
    treasure.addArrow();
    treasure.addArrow();
    assertEquals("4 Sapphires, 8 Rubies, 4 Arrows, 9 Diamonds", treasure.toString());
    treasure.removeTreasure(TreasureType.ARROWS);
    assertEquals("4 Sapphires, 8 Rubies, 3 Arrows, 9 Diamonds", treasure.toString());
  }

}