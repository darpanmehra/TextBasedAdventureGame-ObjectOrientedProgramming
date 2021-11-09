package dungeon.treasure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A class that represents a Treasure. It has a map of treasures, and a random function to get
 * random treasures, minimum quantity of each treasure and maximum quantity of each treasure.
 */
public class Treasure implements ITreasure {
  private Map<TreasureType, Integer> treasury;
  private final Random random;
  private final int MIN_TREASURE_QUANTITY = 1;
  private final int MAX_TREASURE_QUANTITY = 10;


  /**
   * Constructor for Treasure.
   * @param random Random object to generate random numbers.
   */
  public Treasure(Random random) {

    if (random == null) {
      throw new IllegalArgumentException("Random object cannot be null.");
    }

    this.random = random;
    this.treasury = new HashMap<>();
    initializeTreasure();
  }

  private void initializeTreasure() {
    treasury.put(TreasureType.RUBIES, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
    treasury.put(TreasureType.DIAMONDS, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
    treasury.put(TreasureType.SAPPHIRES, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
  }

  @Override
  public Map<TreasureType, Integer> getTreasure() {
    return treasury;
  }

  @Override
  public String toString() {
    return treasury.toString();
  }
}
