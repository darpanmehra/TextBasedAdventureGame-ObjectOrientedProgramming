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
import dungeon.model.treasure.TreasureType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the wrapping of the game.
 */
public class GameTestWrapping {

  private Random random;
  private GameState model;
  private int interconnectivity;

  @Before
  public void setUp() {
    random = new Random();
    random.setSeed(50);
    interconnectivity = 0;
    model = new GameState(6, 6, interconnectivity,
            "wrapping", 20, 5, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetup() {

    model = new GameState(-10, -10, -1,
            "wrapping", 20, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMonsterCount() {
    model = new GameState(6, 6, -1,
            "wrapping", 20, -1, random);
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
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);

    //Shoot arrow at end location to kill monster at end location
    model.shootArrow(Direction.EAST, 1);
    model.shootArrow(Direction.EAST, 1);
    //Check if player has 1 arrow left
    int arrows = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertTrue(arrows == 1);

    //Move player to final location
    model.movePlayer(Direction.EAST);
    //Check if the game is over
    assertTrue(model.isGameOver());
  }

  @Test
  public void getAvailableDirectionsFromPlayerPosition() {
    assertEquals("[EAST, NORTH, SOUTH]",
            model.getAvailableDirectionsFromPlayerPosition().toString());
    assertEquals(3, model.getAvailableDirectionsFromPlayerPosition().size());
  }

  @Test
  public void getPlayerStartLocation() {
    assertEquals("(5, 0)", model.getPlayerStartLocation().toString());
  }

  @Test
  public void getPlayerEndLocation() {
    assertEquals("(4, 4)", model.getPlayerEndLocation().toString());
  }

  @Test
  public void movePlayer() {

    assertEquals("(5, 0)", model.getPlayer().getCurrentLocation().toString());

    //non-wrapping move
    model.movePlayer(Direction.EAST);
    assertEquals("(5, 1)", model.getPlayer().getCurrentLocation().toString());
    model.movePlayer(Direction.WEST); //back to start

    //wrapping move top - down
    model.movePlayer(Direction.SOUTH); // goes to (0, 0)
    assertEquals("(0, 0)", model.getPlayer().getCurrentLocation().toString());

    // Wrapping move right - left
    model.movePlayer(Direction.NORTH); // goes to (5, 0)
    model.movePlayer(Direction.NORTH); // goes to (4, 0)
    model.movePlayer(Direction.EAST); //goes to (4, 1)
    model.movePlayer(Direction.NORTH); // goes to (3, 1)
    model.movePlayer(Direction.WEST); // goes to (3, 0)
    model.movePlayer(Direction.NORTH); // goes to (2, 0)
    model.movePlayer(Direction.WEST); // wrapping move - goes to (2, 5)
    assertEquals("(2, 5)", model.getPlayer().getCurrentLocation().toString());
  }

  @Test
  public void testPlayerCurrentCLocation() {
    assertEquals("(5, 0)", model.getPlayerCurrentLocation().toString());
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
            "wrapping", 20, random);

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
        if (!dungeon[i][j].isCave() && dungeon[i][j].getTreasure() != null
                && !dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.ARROWS)) {
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
          if (dungeon[i][j].getTreasure() != null
                  && dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.DIAMONDS)
                  && dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.SAPPHIRES)
                  && dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.RUBIES)
          ) {
            cavesThatHaveTreasure = cavesThatHaveTreasure + 1;
          }
        }
      }
    }

    double ans = cavesThatHaveTreasure / caves;
    assertTrue(Math.floor(ans * 100.00) >= 20);
  }

  @Test
  public void testArrowsCount() {
    ILocation[][] dungeon = model.getDungeon();
    double totalLocations = model.getDungeon().length * model.getDungeon()[0].length;
    double locationsWithArrows = 0;

    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (dungeon[i][j].getTreasure() != null
                && dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.ARROWS)) {
          locationsWithArrows = locationsWithArrows
                  + dungeon[i][j].getTreasure().getTreasure().get(TreasureType.ARROWS);
        }
      }
    }

    double ans = locationsWithArrows / totalLocations;

    assertEquals(Math.floor(ans * 100.00) >= 20, true);
  }

  @Test
  public void testMonstersLocation() {
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
  public void testMonsterNumber() {
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

  @Test
  public void printPlayerTravelStatus() {
    assertEquals("Player has traveled to the following locations: "
            + "[(5, 0)] and has 3 Arrows", model.printPlayerTravelStatus());
  }

  @Test
  public void playerPickingTreasure() {
    model.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    int count = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertEquals(count, 4);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.WEST);
    //Pick up the treasure - Diamonds
    model.pickTreasureFromCurrentLocation(TreasureType.DIAMONDS);
    model.pickTreasureFromCurrentLocation(TreasureType.DIAMONDS);
    count = model.getPlayer().getTreasures().get(TreasureType.DIAMONDS);
    assertEquals(count, 2);

    //Pick up the treasure - Sapphires
    model.pickTreasureFromCurrentLocation(TreasureType.SAPPHIRES);
    count = model.getPlayer().getTreasures().get(TreasureType.SAPPHIRES);
    assertEquals(count, 1);

    //Pick up the treasure - Rubies
    model.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    model.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    model.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    count = model.getPlayer().getTreasures().get(TreasureType.RUBIES);
    assertEquals(count, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalMove() {
    model.movePlayer(Direction.WEST); // Can't move to West as it's a wall
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
            "wrapping", 20, random);

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
            "wrapping", 20, random);
    ILocation[][] dungeonCopy = model.getDungeon();
    int totalValidPaths = 0;
    for (int i = 0; i < dungeonCopy.length; i++) {
      for (int j = 0; j < dungeonCopy[i].length; j++) {
        totalValidPaths = totalValidPaths + dungeonCopy[i][j].getNeighbours().size();
      }
    }

    //All paths open from each location, so for 36 locations we have 36 * 4 = 144 paths
    assertEquals((totalValidPaths), (4 * dungeonCopy.length * dungeonCopy[0].length));
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

  @Test
  public void playerMovementsAllDirections() {
    model = new GameState(6, 10, 1000,
            "wrapping", 20, random);
    try {
      //Move in South direction
      model.movePlayer(Direction.SOUTH);
      //Move in North direction
      model.movePlayer(Direction.NORTH);
      //Move in East direction
      model.movePlayer(Direction.EAST);
      //Move in West direction
      model.movePlayer(Direction.WEST);

    } catch (IllegalArgumentException e) {
      fail("Fails if player cannot move in the direction");
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

}