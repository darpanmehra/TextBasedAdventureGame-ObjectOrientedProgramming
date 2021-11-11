package dungeon.model.treasure;

/**
 * TreasureType is an enum that represents the type of treasure that can be stored in a Treasure
 * object.
 */
public enum TreasureType {
  DIAMONDS, RUBIES, SAPPHIRES, ARROWS;

  @Override
  public String toString() {
    switch (this) {
      case DIAMONDS:
        return "Diamonds";
      case RUBIES:
        return "Rubies";
      case SAPPHIRES:
        return "Sapphires";
      case ARROWS:
        return "Arrows";
      default:
        return "";
    }
  }
}