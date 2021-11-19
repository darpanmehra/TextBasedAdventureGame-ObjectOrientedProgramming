package dungeon.model.character;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.TreasureType;

import static dungeon.model.treasure.TreasureType.ARROWS;

/**
 * The player class implements the Character interface. It contains information about the player
 * such as the player's name, the player's treasures, the player's current location, and the
 * location the player has visited.
 */
public class Player implements Character {

  private final String name;
  private Map<TreasureType, Integer> treasures;
  private ILocation currentLocation;
  private List<ILocation> locationVisited;
  private int health;
  private final Random random;

  /**
   * Constructor for the player class.
   *
   * @param name the name of the player
   */
  public Player(String name, Random random) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }

    this.name = name;
    this.random = random;
    this.treasures = new TreeMap<>();
    this.currentLocation = null;
    this.locationVisited = new ArrayList<>();
    this.health = 100;
    this.treasures.put(TreasureType.ARROWS, 3);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setCurrentLocation(ILocation location) {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    locationVisited.add(location);
    this.currentLocation = location;
    determinePLayerSurvives(location);
  }

  @Override
  public ILocation getCurrentLocation() {
    return this.currentLocation;
  }

  @Override
  public int getHealth() {
    return this.health;
  }

  @Override
  public void decrementHealth() {
    this.health = 0;
  }

  @Override
  public boolean isAlive() {
    return this.health > 0;
  }

  @Override
  public void pickTreasureFromCurrentLocation(TreasureType treasureType) {
    if (treasureType == null) {
      throw new IllegalArgumentException("Treasure type cannot be null");
    }
    if (this.currentLocation == null) {
      throw new IllegalStateException("Player is not in a location");
    }
    if (this.currentLocation.getTreasure() == null) {
      throw new IllegalStateException("Location does not contain treasure");
    }
    ITreasure treasure = this.currentLocation.getTreasure();
    if (!treasure.getTreasure().containsKey(treasureType)
            || treasure.getTreasure().get(treasureType) <= 0) {
      throw new IllegalStateException("Location does not have "
              + treasureType.toString().toLowerCase());
    }

    //Increment the player's treasure count
    this.treasures.put(treasureType,
            (this.treasures.get(treasureType) != null) ? this.treasures.get(treasureType) + 1 : 1);
    //Decrement the treasure count in the location
    this.currentLocation.removeTreasure(treasureType);
  }

  @Override
  public Map<TreasureType, Integer> getTreasures() {
    return this.treasures;
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    if (this.treasures.get(TreasureType.ARROWS) <= 0) {
      throw new IllegalStateException("No arrows remaining");
    }
    if (!currentLocation.getNeighbours().containsKey(direction)) {
      throw new IllegalArgumentException("Direction is not valid");
    }

    this.treasures.put(ARROWS,
            (this.treasures.get(ARROWS) != null) ? this.treasures.get(ARROWS) - 1 : 0);

    //Shoot arrow
    Set<ILocation> visited = new HashSet<>();
    visited.add(currentLocation);

    this.dfsShootArrowHelper(currentLocation.getNeighbours().get(direction),
            distance, direction, visited);
  }

  @Override
  public String printTravelStatus() {
    StringBuilder sb = new StringBuilder();
    String commaSepTreasureValue = this.treasures.keySet().stream()
            .map(key -> (this.treasures.get(key) > 0) ? this.treasures.get(key) + " " + key : "")
            .collect(Collectors.joining(", "));
    String commaSepLocationVisited = this.locationVisited.stream()
            .map(ILocation::getName).collect(Collectors.joining(", "));
    sb.append(this.name).append(" has traveled to the following locations: [")
            .append(commaSepLocationVisited).append("] and has ").append(commaSepTreasureValue);
    return sb.toString();
  }

  @Override
  public List<ILocation> getLocationVisited() {
    return this.locationVisited;
  }

  @Override
  public String toString() {
    String commaSepValue = this.treasures.keySet().stream()
            .map(key -> (this.treasures.get(key) > 0) ? this.treasures.get(key) + " " + key : ""
            ).collect(Collectors.joining(", "));
    String s = (currentLocation.isCave()) ? "You are in a cave" : "You are in a tunnel";
    if (commaSepValue.length() > 0) {
      s += " and you have " + commaSepValue + ".\n";
    } else {
      s += ".\n";
    }
    return s;
  }

  private void determinePLayerSurvives(ILocation location) {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    if (location.hasMonster()) {
      if (location.getMonster().getHealth() == 100) { // If monster not injured, player dies
        this.decrementHealth();
      } else if (location.getMonster().getHealth() == 50) {
        int randomNumber = random.nextInt(2);
        if (randomNumber == 0) { // If monster injured, player dies 50% chance
          this.decrementHealth();
        }
      }
    }
  }

  private void dfsShootArrowHelper(ILocation location, int distance, Direction direction,
                                   Set<ILocation> visited) {
    if (distance == 0) {
      return;
    }
    visited.add(location);
    if (location.isCave()) {
      distance--;
      if (distance == 0 && location.hasMonster()) { // Monster condition
        location.getMonster().decrementHealth();
        return;
      }
      if (location.getNeighbours().containsKey(direction)) { // Cave with opposite direction
        if (!visited.contains(location.getNeighbours().get(direction))) { // If not visited
          dfsShootArrowHelper(location.getNeighbours().get(direction), distance, direction,
                  visited);
        }
      } else { // Cave with no opposite direction
        return;
      }
    }

    if (!location.isCave()) { // Tunnel condition
      for (Direction neighborDirection : location.getNeighbours().keySet()) {
        if (!visited.contains(location.getNeighbours().get(neighborDirection))) {
          dfsShootArrowHelper(location.getNeighbours().get(neighborDirection), distance,
                  neighborDirection, visited);
        }
      }
    }
  }

}
