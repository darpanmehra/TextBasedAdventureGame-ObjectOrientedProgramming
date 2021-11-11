package dungeon.model.character;

import java.util.List;
import java.util.Map;

import dungeon.model.location.ILocation;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.TreasureType;

/**
 * This class represents a character in the dungeon. This interface can be extended to a class that
 * represents a specific character.
 */
public interface Character {

  /**
   * This method is called when you need the name of the character.
   *
   * @return the name of the character.
   */
  String getName();

  /**
   * This method is called when you need to pick treasure from current location.
   */
  void pickTreasureFromCurrentLocation(TreasureType treasureType);

  /**
   * This method is called when you need to get the treasure the character holds.
   *
   * @return the treasure the character holds.
   */
  Map<TreasureType, Integer> getTreasures();

  /**
   * This method is called when you need to get the current location of the character.
   *
   * @return the current location of the character.
   */
  ILocation getCurrentLocation();

  /**
   * This method is called when you need to set the current location of the character.
   *
   * @param location the location to set.
   */
  void setCurrentLocation(ILocation location);

  /**
   * This method is called when you need to get the list of locations visited by the character.
   * @return the list of locations visited by the character.
   */
  List<ILocation> getLocationVisited();

  /**
   * This method is called to get the travel information of the character such as the locations
   * visited and the treasures collected.
   *
   * @return the travel information of the character.
   */
  String printTravelStatus();

  /**
   * This method is called when you need to get the health of the character.
   *
   * @return the health of the character.
   */
  int getHealth();

  /**
   * This method is called when you need to set the health of the character.
   *
   */
  void decrementHealth();

  /**
   * Check if the character is alive.
   * @return true if the character is alive, false otherwise.
   */
  boolean isAlive();

  /**
   * This method is called when you need to get the arrow count of the character.
   * @return
   */
  int getArrowCount();

  /**
   * This method is called when you need to set the arrow count of the character.
   */
  void setArrowCount(int arrowCount);

}
