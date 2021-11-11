import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import dungeon.model.GameState;
import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the non-wrapping dungeon of the game.
 */
public class GameTest {

  private Random random;
  private GameState model;
  private int interconnectivity;

  @Before
  public void setUp() {
    random = new Random();
    random.setSeed(50);
    interconnectivity = 0;
    model = new GameState(6, 6, interconnectivity,
            "nonwrapping", 20, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetup() {
    model = new GameState(-10, -10, -1,
            "nonwrapping", 20, null);
  }

  @Test
  public void getPlayer() {
    assertEquals("Player", model.getPlayer().getName());
  }

  @Test
  public void isGameOver() {

    //When player is at start location
    assertFalse(model.isGameOver());

    //Move player to end location
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.EAST);

    //Check if the game is over
    assertTrue(model.isGameOver());
  }

  @Test
  public void getAvailableDirectionsFromPlayerPosition() {

    assertEquals("[WEST]",
            model.getAvailableDirectionsFromPlayerPosition().toString());
    assertEquals(1, model.getAvailableDirectionsFromPlayerPosition().size());
  }

  @Test
  public void getPlayerStartLocation() {
    ILocation start = model.getPlayerStartLocation();
    ILocation playerPosition = model.getPlayer().getCurrentLocation();
    assertEquals(start, playerPosition);
  }

  @Test
  public void getPlayerEndLocation() {
    assertEquals("(3, 4)", model.getPlayerEndLocation().toString());
  }

  @Test
  public void movePlayer() {

    //Player start position
    assertEquals("(2, 2)", model.getPlayerStartLocation().toString());

    model.movePlayer(Direction.WEST);
    assertEquals("(2, 1)", model.getPlayer().getCurrentLocation().toString());

    model.movePlayer(Direction.SOUTH);
    assertEquals("(3, 1)", model.getPlayer().getCurrentLocation().toString());

    model.movePlayer(Direction.NORTH);
    assertEquals("(2, 1)", model.getPlayer().getCurrentLocation().toString());

    model.movePlayer(Direction.EAST);
    assertEquals("(2, 2)", model.getPlayer().getCurrentLocation().toString());

  }

  @Test
  public void testPlayerCurrentCLocation() {
    assertEquals("(2, 2)", model.getPlayerCurrentLocation().toString());
  }

  @Test
  public void testInterconnectivity() {
    ILocation[][] dungeonCopy = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeonCopy.length; i++) {
      for (int j = 0; j < dungeonCopy[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeonCopy[i][j].getNeighbours().size();
      }
    }
    // Paths are bidirectional to totalValidPaths/ 2 gives the path count in the dungeon
    assertEquals(totalValidPaths / 2,
            interconnectivity + ((long) dungeonCopy.length * dungeonCopy[0].length - 1));
  }

  @Test
  public void testInterconnectivityGreaterThanZero() {
    model = new GameState(10, 10, 8,
            "nonwrapping", 20, random);

    ILocation[][] dungeon = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeon[i][j].getNeighbours().size();
      }
    }

    assertEquals(totalValidPaths / 2,
            8 + (long) dungeon.length * dungeon[0].length - 1);
  }

  @Test
  public void testTreasureInRightLocation() {
    ILocation[][] dungeon = model.getDungeon();
    boolean correct = true;
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (!dungeon[i][j].isCave() && dungeon[i][j].getTreasure() != null) {
          correct = false;
        }
      }
    }
    assertTrue(correct);
  }


  @Test
  public void testTreasurePercentage() {
    ILocation[][] dungeon = model.getDungeon();
    double caves = 0.0;
    double cavesThatHaveTreasure = 0.0;
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (dungeon[i][j].isCave()) {
          caves = caves + 1;
          if (dungeon[i][j].getTreasure() != null) {
            cavesThatHaveTreasure = cavesThatHaveTreasure + 1;
          }
        }
      }
    }
    double ans = cavesThatHaveTreasure / caves;
    assertEquals(Math.floor(ans * 100.00), 20.00, 0.001);
  }

  @Test
  public void printPlayerTravelStatus() {
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    assertEquals("Player has traveled to the following locations: [(2, 2) (2, 1) (3, 1)" +
            " ].\n" +
            "Treasures: {}", model.printPlayerTravelStatus());
  }

  @Test
  public void playerPickingTreasure() {

    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.EAST);

    assertEquals("Player has traveled to the following locations: [(2, 2) (2, 1) (3, 1)"
            + " (3, 2) (3, 3) (3, 4) ].\n"
            + "Treasures: {DIAMONDS=3, RUBIES=8, SAPPHIRES=10}", model.printPlayerTravelStatus());
  }

  @Test
  public void treasureNullAfterPick() {

    ILocation[][] dungeon = model.getDungeon();
    StringBuilder dungeonBuilder = new StringBuilder();
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        dungeonBuilder.append(dungeon[i][j].printLocationInfo());
        dungeonBuilder.append("\n");
      }
    }
    System.out.println(dungeonBuilder);

    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.NORTH); // goes to (2, 0) and collects treasure
    assertEquals("Player has traveled to the following locations: [(2, 2) (2, 1) " +
            "(3, 1) (3, 0) (2, 0) ].\n"
            + "Treasures: {DIAMONDS=9, RUBIES=6, SAPPHIRES=9}", model.printPlayerTravelStatus());

    model.movePlayer(Direction.SOUTH); // Go back to (3, 0)
    model.movePlayer(Direction.NORTH); // goes to (2, 0) -comes back to same location where treasure

    assertEquals("Player has traveled to the following locations: [(2, 2) (2, 1) (3, 1)"
            + " (3, 0) (2, 0) (3, 0) (2, 0) ].\n"
            + "Treasures: {DIAMONDS=9, RUBIES=6, SAPPHIRES=9}", model.printPlayerTravelStatus());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalMove() {
    model.movePlayer(Direction.NORTH); // Can't move to NORTH as it's a wall
  }

  @Test
  public void dungeonCreationCorrectness() {
    ILocation[][] dungeonCopy = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeonCopy.length; i++) {
      for (int j = 0; j < dungeonCopy[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeonCopy[i][j].getNeighbours().size();
      }
    }
    assertEquals((totalValidPaths / 2),
            (long) dungeonCopy.length * dungeonCopy[0].length - 1);
  }

  @Test
  public void dungeonCreationWithHighInterConnectivity() {
    model = new GameState(10, 10, 4,
            "nonwrapping", 20, random);

    ILocation[][] dungeonCopy = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeonCopy.length; i++) {
      for (int j = 0; j < dungeonCopy[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeonCopy[i][j].getNeighbours().size();
      }
    }
    assertEquals((totalValidPaths / 2),
            4 + (long) dungeonCopy.length * dungeonCopy[0].length - 1);
  }

  @Test
  public void dungeonCreationWithVeryHighInterconnectivity() {
    model = new GameState(6, 6, 1000000,
            "nonwrapping", 20, random);
    ILocation[][] dungeonCopy = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeonCopy.length; i++) {
      for (int j = 0; j < dungeonCopy[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeonCopy[i][j].getNeighbours().size();
      }
    }
    //All paths open from each location
    assertEquals(120, (totalValidPaths));
  }

  @Test
  public void testPathLength() {
    //Creating a random maze creation 100 times and checking if the path length is correct (>=5) on
    //each run
    random = new Random();
    for (int i = 0; i < 100; i++) {
      model = new GameState(random.nextInt(94) + 6,
              random.nextInt(94) + 6, random.nextInt(10000000),
              "wrapping", 20, random);
      ILocation start = model.getPlayerStartLocation(); // Selects a random cave as start
      ILocation end = model.getPlayerEndLocation(); // selects a random cave as end

      assertTrue(checkDistance(start, end) >= 5);

    }
  }


  private int checkDistance(ILocation startLocation, ILocation endLocation) {

    Set<ILocation> visited = new HashSet<>();
    Integer level = 0;
    Queue<Map<ILocation, Integer>> objects = new LinkedList<>();
    Map<ILocation, Integer> map = new HashMap<>();
    map.put(startLocation, level);
    objects.add(map);
    ILocation currentLocation = startLocation;


    while (!objects.isEmpty()) {

      Map<ILocation, Integer> current = objects.remove();
      for (ILocation location : current.keySet()) {
        currentLocation = location;
      }
      int currentLevel = current.get(currentLocation);

      // if we have found the end location, return the level
      if (currentLocation.equals(endLocation)) {
        return currentLevel;
      }
      visited.add(currentLocation);

      // add all the adjacent locations to the queue
      for (ILocation value : currentLocation.getNeighbours().values()) {
        if (value != null) {
          if (!visited.contains(value)) {
            Map<ILocation, Integer> newMap = new HashMap<>();
            newMap.put(value, currentLevel + 1);
            objects.add(newMap);
          }
        }
      }

    }
    return -1;
  }

  //Project 4 tests

  @Test
  public void testMonstersLocation(){
    model = new GameState(6, 6, interconnectivity,
            "nonwrapping", 20, 15, random);

    ILocation[][] dungeon = model.getDungeon();
    boolean correct = true;
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (!dungeon[i][j].isCave() && dungeon[i][j].getMonster() != null) {
          correct = false;
        }
      }
    }
    assertTrue(correct);
  }

  @Test
  public void testMonsterNumber(){
    model = new GameState(6, 6, interconnectivity,
            "nonwrapping", 20, 5, random);

    ILocation[][] dungeon = model.getDungeon();
    int totalCaves = 0;
    int cavesThatHaveMonsters = 0;
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (dungeon[i][j].isCave()) {
          totalCaves = totalCaves + 1;
          if (dungeon[i][j].getMonster() != null) {
            cavesThatHaveMonsters = cavesThatHaveMonsters + 1;
          }
        }
      }
    }
    assertEquals(5, cavesThatHaveMonsters);
  }
}