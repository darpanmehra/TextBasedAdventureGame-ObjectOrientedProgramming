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
import static org.junit.Assert.assertNull;
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
            "nonwrapping", 20, 5, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetup() {
    model = new GameState(-10, -10, -1,
            "nonwrapping", 20, 5, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMonsterCount() {
    model = new GameState(6, 6, -1,
            "nonwrapping", 20, 0, random);
  }

  @Test
  public void getPlayer() {
    assertEquals("Player", model.getPlayer().getName());
  }

  @Test
  public void getPlayerStartLocation() {
    ILocation start = model.getPlayerStartLocation();
    ILocation playerPosition = model.getPlayer().getCurrentLocation();
    assertEquals(start, playerPosition);
  }

  @Test
  public void getPlayerEndLocation() {
    assertEquals("(3, 0)", model.getPlayerEndLocation().toString());
  }

  @Test
  public void getAvailableDirectionsFromPlayerPosition() {
    assertEquals("[NORTH]",
            model.getAvailableDirectionsFromPlayerPosition().toString());
    assertEquals(1, model.getAvailableDirectionsFromPlayerPosition().size());
  }

  @Test
  public void testPlayerCurrentLocation() {
    assertEquals("(5, 3)", model.getPlayerCurrentLocation().toString());
  }

  @Test
  public void testPlayerCurrentLocationStatus() {
    assertEquals("You slightly smell something nearby\n"
            + "You are in a cave and you have 3 Arrows.\n" + "You find 1 Arrows\n"
            + "Doors lead to: [NORTH]\n\n", model.getPlayerCurrentLocationStatus());
  }

  @Test
  public void testInterconnectivityGreaterThanZero() {
    model = new GameState(10, 10, 8,
            "nonwrapping", 20, 5, random);

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
                  && !dungeon[i][j].getTreasure().getTreasure().containsKey(TreasureType.ARROWS)) {
            cavesThatHaveTreasure = cavesThatHaveTreasure + 1;
          }
        }
      }
    }
    double ans = cavesThatHaveTreasure / caves;
    assertEquals(Math.floor(ans * 100.00), 20.00, 0.001);
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
  public void testPickArrowsFromCurrentLocation() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //Pick additional arrows
    model.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    int playerArrows = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertEquals(playerArrows, 4);
  }

  @Test
  public void shootArrowTwiceToKill() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //Pick additional arrows
    model.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    int playerArrows = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertEquals(playerArrows, 4);
    model.movePlayer(Direction.NORTH); //(4, 3)

    //Shoot a monster in West cave
    model.shootArrow(Direction.WEST, 1);
    model.shootArrow(Direction.WEST, 1);
    //Check if monster is dead
    assertEquals(model.getDungeon()[4][2].getMonster().getHealth(), 0);
  }

  @Test
  public void testShootArrowToKillButWrongDistance() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //Pick additional arrows
    model.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    int playerArrows = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertEquals(playerArrows, 4);
    model.movePlayer(Direction.NORTH); //(4, 3)

    //Shoot a monster in West cave
    model.shootArrow(Direction.WEST, 1);
    assertEquals(model.getDungeon()[4][2].getMonster().getHealth(), 50); //First hit
    model.shootArrow(Direction.WEST, 5); //Second hit misses
    assertEquals(model.getDungeon()[4][2].getMonster().getHealth(), 50);//Health is same
    //Check if monster is dead
    assertEquals(model.getDungeon()[4][2].getMonster().isAlive(), true);
  }

  @Test
  public void testArrowsCount() {
    ILocation[][] dungeon = model.getDungeon();
    double totalLocations = model.getDungeon().length * model.getDungeon()[0].length;
    double locationsWithArrows = 0;

    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        if (dungeon[i][j].getTreasure() != null && dungeon[i][j].getTreasure().getTreasure()
                .containsKey(TreasureType.ARROWS)) {
          locationsWithArrows = locationsWithArrows + dungeon[i][j].getTreasure().getTreasure()
                  .get(TreasureType.ARROWS);
        }
      }
    }

    double ans = locationsWithArrows / totalLocations;

    assertEquals(Math.floor(ans * 100.00) >= 20, true);
  }

  @Test
  public void isGameOver() {

    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //When player is at start location
    assertFalse(model.isGameOver());

    //Move player to end location
    model.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    model.movePlayer(Direction.NORTH); //(4, 3)

    //Shoot a monster in West cave
    model.shootArrow(Direction.WEST, 1);
    model.shootArrow(Direction.WEST, 1);
    model.movePlayer(Direction.WEST); //(4,2)
    model.movePlayer(Direction.WEST); //(4,1)

    model.shootArrow(Direction.NORTH, 1);
    model.shootArrow(Direction.NORTH, 1);
    model.movePlayer(Direction.NORTH); //(3,1)
    model.movePlayer(Direction.WEST); //(3,0) - End of dungeon

    //Check if the game is over
    assertTrue(model.isGameOver());
  }

  @Test
  public void testMonsterInEndLocation() {
    ILocation end = model.getPlayerEndLocation();
    assertEquals("Otyugh", end.getMonster().getName());
    assertEquals(end.getMonster().getHealth(), 100);
  }

  @Test
  public void testMonsterNeverAtStartLocation() {
    ILocation start = model.getPlayerStartLocation();
    assertNull(start.getMonster());
  }

  @Test
  public void testPlayerStartsWith3Arrows() {
    int expectedCount = model.getPlayer().getTreasures().get(TreasureType.ARROWS);
    assertEquals(expectedCount, 3);
  }

  @Test
  public void testArrowPassesThroughTunnelAndChangesDirection() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //Shoot north from start location
    //goes through tunnel (4, 3) changes direction to west (4, 2) and hits the monster there
    model.shootArrow(Direction.NORTH, 1);
    assertEquals(model.getDungeon()[4][2].getMonster().getHealth(), 50);
  }

  @Test
  public void testArrowPassesThroughTunnelAndChangesDirectionMissesMonster() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());

    //Shoot north from start location
    //goes through tunnel (4, 3) changes direction to west (4, 2) and misses the monster there
    // as it travels 5 caves which are (4, 2) - cave, (4,1), (3,1)- cave
    //the arrow hits the monster in cave (3,1) where it stops
    model.shootArrow(Direction.NORTH, 2);
    assertEquals(model.getDungeon()[4][2].getMonster().getHealth(), 100);
    assertEquals(model.getDungeon()[3][1].getMonster().getHealth(), 50);
  }

  @Test
  public void testTreasureCountDecreasesAfterPick() {
    assertEquals("(5, 3)", model.getPlayerStartLocation().getName());
    assertEquals("(3, 0)", model.getPlayerEndLocation().getName());
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.WEST);
    int expectedCount = model.getPlayer().getCurrentLocation().getTreasure()
            .getTreasure().get(TreasureType.DIAMONDS);
    assertEquals(expectedCount, 3);
    //Pick up treasure - arrows
    model.pickTreasureFromCurrentLocation(TreasureType.DIAMONDS);
    //Check if treasure count decreases after pick
    expectedCount = model.getPlayer().getCurrentLocation().getTreasure()
            .getTreasure().get(TreasureType.DIAMONDS);
    assertEquals(expectedCount, 2);
  }

  @Test
  public void test50RealRandomGames() {
    //Creating a random maze creation 100 times
    //checking if the path length is correct (>=5)

    random = new Random();
    for (int i = 0; i < 50; i++) {
      model = new GameState(random.nextInt(94) + 6,
              random.nextInt(94) + 6, random.nextInt(10000000),
              "wrapping", 20, 5, random);
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

}