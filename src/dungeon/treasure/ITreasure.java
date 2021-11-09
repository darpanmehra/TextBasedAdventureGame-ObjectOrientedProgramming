package dungeon.treasure;

import java.util.Map;

/**
 * Interface for treasure.
 */
public interface ITreasure {

  /**
   * Get the map of treasures which includes the 3 different types of treasures it can hold.
   * @return the map of treasures.
   */
  Map<TreasureType, Integer> getTreasure();

}
