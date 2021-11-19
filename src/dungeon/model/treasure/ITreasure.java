package dungeon.model.treasure;

import java.util.Map;

/**
 * Interface for treasure.
 */
public interface ITreasure {

  /**
   * Adds random treasure to the treasure object.
   */
  void initializeTreasure();

  /**
   * Add arrows to the treasure.
   */
  void addArrow();

  /**
   * Remove a passed treasure from the treasure object.
   */
  void removeTreasure(TreasureType treasureType);

  /**
   * Get the map of treasures which includes the 3 different types of treasures it can hold.
   * @return the map of treasures.
   */
  Map<TreasureType, Integer> getTreasure();
}
