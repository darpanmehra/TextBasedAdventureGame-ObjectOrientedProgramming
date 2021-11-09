package dungeon.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dungeon.location.ILocation;
import dungeon.treasure.ITreasure;
import dungeon.treasure.TreasureType;

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

  /**
   * Constructor for the player class.
   *
   * @param name the name of the player
   */
  public Player(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }
    this.name = name;
    this.treasures = new TreeMap<>();
    this.currentLocation = null;
    this.locationVisited = new ArrayList<>();
  }

  /**
   * A copy constructor for the player class.
   * @param player the player to copy
   */
  public Player(Character player) {

    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    this.name = player.getName();
    this.treasures = player.getTreasures();
    this.currentLocation = player.getCurrentLocation();
    this.locationVisited = player.getLocationVisited();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addTreasure(ITreasure treasure) {
    Map<TreasureType, Integer> treasures = treasure.getTreasure();
    for (TreasureType treasureType : treasures.keySet()) {
      if (this.treasures.containsKey(treasureType)) {
        this.treasures.put(treasureType, this.treasures.get(treasureType)
                + treasures.get(treasureType));
      } else {
        this.treasures.put(treasureType, treasures.get(treasureType));
      }
    }
  }

  @Override
  public Map<TreasureType, Integer> getTreasures() {
    return this.treasures;
  }

  @Override
  public ILocation getCurrentLocation() {
    return this.currentLocation;
  }

  @Override
  public void setCurrentLocation(ILocation location) {
    locationVisited.add(location);
    ITreasure locationTreasure = location.getTreasure();
    if (locationTreasure != null) {
      addTreasure(locationTreasure);
      location.setTreasureEmpty();
    }
    this.currentLocation = location;
  }

  @Override
  public String printTravelStatus() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.name).append(" has traveled to the following locations: [");
    for (ILocation location : this.locationVisited) {
      sb.append(location.getName()).append(" ");
    }
    sb.append("].\nTreasures: ").append(this.treasures.toString());
    return sb.toString();
  }

  @Override
  public List<ILocation> getLocationVisited() {
    return this.locationVisited;
  }

  @Override
  public String toString() {
    return this.name
            + " is at " + this.currentLocation.getName()
            + " and has treasures " + this.treasures.toString();
  }
}
