package dungeon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Player;
import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;
import dungeon.model.location.Location;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.TreasureType;

/**
 * The GameState class is used to keep track of the state of the game.
 */
public class GameState implements IGameState {

  private final Grid dungeon;
  private final Character player;

  /**
   * Constructor for the GameState class.
   *
   * @param dungeonHeight      The height of the dungeon.
   * @param dungeonWidth       The width of the dungeon.
   * @param interConnectivity  The interconnectivity of the dungeon.
   * @param dungeonType        The type of dungeon.
   * @param treasurePercentage The percentage of treasure in the dungeon.
   * @param random             The random function for the dungeon.
   */
  public GameState(int dungeonHeight, int dungeonWidth, int interConnectivity, String dungeonType,
                   int treasurePercentage, Random random) {
    if (dungeonHeight < 6 || dungeonHeight > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (dungeonWidth < 6 || dungeonWidth > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (interConnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity must be between 0 and dungeon size");
    }
    if (!(dungeonType.equalsIgnoreCase("wrapping")
            || dungeonType.equalsIgnoreCase("nonwrapping"))) {
      throw new IllegalArgumentException("Dungeon type must be wrapping or nonwrapping");
    }
    if (treasurePercentage < 0 || treasurePercentage > 100) {
      throw new IllegalArgumentException("Treasure percentage must be between 0 and 100");
    }
    if (random == null) {
      throw new IllegalArgumentException("Random must be specified");
    }

    dungeon = new Grid(dungeonHeight, dungeonWidth, interConnectivity, dungeonType,
            treasurePercentage, random);

    //Create Player and assign start location
    player = new Player("Player");
    player.setCurrentLocation(dungeon.getPlayerStartLocation());
  }

  // new Constructor with monsters
  public GameState(int dungeonHeight, int dungeonWidth, int interConnectivity, String dungeonType,
                   int treasurePercentage, int monsterCount, Random random) {
    if (dungeonHeight < 6 || dungeonHeight > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (dungeonWidth < 6 || dungeonWidth > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (interConnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity must be between 0 and dungeon size");
    }
    if (!(dungeonType.equalsIgnoreCase("wrapping")
            || dungeonType.equalsIgnoreCase("nonwrapping"))) {
      throw new IllegalArgumentException("Dungeon type must be wrapping or nonwrapping");
    }
    if (treasurePercentage < 0 || treasurePercentage > 100) {
      throw new IllegalArgumentException("Treasure percentage must be between 0 and 100");
    }
    if (monsterCount < 1) {
      throw new IllegalArgumentException("Monster count must be greater than 0");
    }
    if (random == null) {
      throw new IllegalArgumentException("Random must be specified");
    }

    dungeon = new Grid(dungeonHeight, dungeonWidth, interConnectivity, dungeonType,
            treasurePercentage, monsterCount, random);

    //Create Player and assign start location
    player = new Player("Player");
    player.setCurrentLocation(dungeon.getPlayerStartLocation());
  }

  @Override
  public boolean isGameOver() {
    return player.getCurrentLocation().equals(dungeon.getPlayerEndLocation());
  }

  @Override
  public Character getPlayer() {
    return new Player(player);
  }

  @Override
  public List<Direction> getAvailableDirectionsFromPlayerPosition() {
    Map<Direction, ILocation> neighbours = dungeon.getNeighbours(player.getCurrentLocation());
    List<Direction> list = new ArrayList<>();
    list.addAll(neighbours.keySet());
    return list;
  }

  @Override
  public ILocation movePlayer(Direction direction) {
    ILocation newLocation = dungeon.getNeighbours(player.getCurrentLocation()).get(direction);
    if (newLocation == null) {
      throw new IllegalArgumentException("Cannot move in that direction");
    }
    player.setCurrentLocation(newLocation);
    return newLocation;
  }

  @Override
  public void pickTreasureFromCurrentLocation(TreasureType treasureType) {
    player.pickTreasureFromCurrentLocation(treasureType);
  }

  @Override
  public ILocation getPlayerStartLocation() {
    return dungeon.getPlayerStartLocation();
  }

  @Override
  public ILocation getPlayerEndLocation() {
    return dungeon.getPlayerEndLocation();
  }

  @Override
  public ILocation getPlayerCurrentLocation() {
    return new Location(player.getCurrentLocation());
  }

  @Override
  public ILocation[][] getDungeon() {
    return dungeon.getDungeonCopy();
  }

  @Override
  public String printPlayerTravelStatus() {
    return player.printTravelStatus();
  }

  @Override
  public String getPlayerCurrentLocationStatus() {
    List<Direction> doors = new ArrayList<>(dungeon.getNeighbours(player.getCurrentLocation()).keySet());
    StringBuilder sb = new StringBuilder();
    sb.append(player);
    if (player.getCurrentLocation().getTreasure() != null) {
      sb.append("You find ").append(player.getCurrentLocation().getTreasure().toString()).append("\n");
    }
    sb.append("Doors lead to: ").append(doors).append("\n");


    sb.append("\n");
    return sb.toString();
  }
}