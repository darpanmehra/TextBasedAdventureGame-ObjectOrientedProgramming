package dungeon.model.treasure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static dungeon.model.treasure.TreasureType.ARROWS;

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
   *
   * @param random Random object to generate random numbers.
   */
  public Treasure(Random random) {

    if (random == null) {
      throw new IllegalArgumentException("Random object cannot be null.");
    }
    this.random = random;
    this.treasury = new HashMap<>();
  }

  @Override
  public void initializeTreasure() {
    treasury.put(TreasureType.RUBIES, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
    treasury.put(TreasureType.DIAMONDS, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
    treasury.put(TreasureType.SAPPHIRES, random.nextInt(MAX_TREASURE_QUANTITY)
            + MIN_TREASURE_QUANTITY);
  }

  @Override
  public void addArrow() {
    this.treasury.put(ARROWS,
            (this.treasury.get(ARROWS) != null) ? this.treasury.get(ARROWS) + 1 : 1);
  }

  @Override
  public void removeTreasure(TreasureType treasureType) {
    //Decrement the count of treasure type in the treasure
    if (this.treasury.containsKey(treasureType)) {
      this.treasury.put(treasureType, this.treasury.get(treasureType) - 1);
    }
    //Remove that type if the count goes 0 or below (if at all)
    if (this.treasury.containsKey(treasureType) && this.treasury.get(treasureType) <= 0) {
      this.treasury.remove(treasureType);
    }
  }

  @Override
  public Map<TreasureType, Integer> getTreasure() {
    return treasury;
  }

  @Override
  public String toString() {
    String commaSepValue = this.treasury.keySet().stream()
            .map(key -> (this.treasury.get(key) > 0) ? this.treasury.get(key) + " " + key : ""
            ).collect(Collectors.joining(", "));
    return commaSepValue;
  }
}
