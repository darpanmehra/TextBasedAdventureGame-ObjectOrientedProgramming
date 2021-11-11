package dungeon.model.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dungeon.model.location.ILocation;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.TreasureType;

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
  private int arrowCount;

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

    this.health = 100;
    this.arrowCount = 3;
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
    this.health = player.getHealth();
    this.arrowCount = player.getArrowCount();
  }

  @Override
  public String getName() {
    return this.name;
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
    if (!treasure.getTreasure().containsKey(treasureType) || treasure.getTreasure().get(treasureType) <= 0) {
      throw new IllegalArgumentException("Treasure does not have " + treasureType.toString().toLowerCase());
    }

    //Increment the player's treasure count
    this.treasures.put(treasureType, (this.treasures.get(treasureType) != null)?  this.treasures.get(treasureType) + 1 : 1);
    //Decrement the treasure count in the location
    this.currentLocation.removeTreasure(treasureType);
  }

//  private void addTreasure(ITreasure treasure) {
//    Map<TreasureType, Integer> treasures = treasure.getTreasure();
//    for (TreasureType treasureType : treasures.keySet()) {
//      if (this.treasures.containsKey(treasureType)) {
//        this.treasures.put(treasureType, this.treasures.get(treasureType)
//                + treasures.get(treasureType));
//      } else {
//        this.treasures.put(treasureType, treasures.get(treasureType));
//      }
//    }
//  }


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
  public int getArrowCount() {
    return this.arrowCount;
  }

  @Override
  public void setArrowCount(int arrowCount) {
    this.arrowCount = arrowCount;
  }

  @Override
  public List<ILocation> getLocationVisited() {
    return this.locationVisited;
  }

  @Override
  public String toString() {
    String s = (currentLocation.isCave())? "You are in a cave with " : "You are in a tunnel with ";
    s += this.arrowCount +" arrows and treasures: " + this.treasures.toString() + " "+this.currentLocation+".\n";
    return s;
  }
}
