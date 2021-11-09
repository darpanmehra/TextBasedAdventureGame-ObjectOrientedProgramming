package dungeon;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import dungeon.character.Character;
import dungeon.directions.Direction;
import dungeon.location.ILocation;

/**
 * Driver class for the Dungeon game.
 */
public class Driver {

  /**
   * Main method for the Dungeon game.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {

    int dungeonHeight = Integer.parseInt(args[0]);
    int dungeonWidth = Integer.parseInt(args[1]);
    int interConnectivity = Integer.parseInt(args[2]);
    String dungeonType = (args[3]).toLowerCase();
    int treasurePercentage = Integer.parseInt(args[4]);

    //Mock Random method
    Random rand = new Random();
    rand.setSeed(20);

    // Create a new dungeon- Initialize the model
    GameState model = new GameState(dungeonHeight, dungeonWidth, interConnectivity,
            dungeonType, treasurePercentage, rand);

    Character player = model.getPlayer();

    System.out.println("Welcome to the Dungeon! \n\n");
    ILocation playerStartLocation = model.getPlayerStartLocation();
    System.out.println("Player Start Position: " + playerStartLocation.getName());

    ILocation playerEndLocation = model.getPlayerEndLocation();
    System.out.println("Player End Position: " + playerEndLocation.getName());

    // Print Dungeon Copy
    ILocation[][] dungeon = model.getDungeon();
    StringBuilder dungeonBuilder = new StringBuilder();
    for (int i = 0; i < dungeon.length; i++) {
      for (int j = 0; j < dungeon[i].length; j++) {
        dungeonBuilder.append(dungeon[i][j].printLocationInfo());
        dungeonBuilder.append("\n");
      }
    }
    System.out.println(dungeonBuilder);


    while (!model.isGameOver()) {
      System.out.println(model.getPlayer());
      List<Direction> availableDirections = model.getAvailableDirectionsFromPlayerPosition();
      System.out.println("Location originally had "
              + model.getPlayerCurrentLocation().getOriginalTreasure()
              + " treasures which the player acquired.");
      System.out.println("Available directions from current location: " + availableDirections);
      ILocation newLocation;

      String input;
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter which direction you want to move now?");
      input = sc.nextLine().toUpperCase();
      switch (input) {
        case "NORTH":
          if (!availableDirections.contains(Direction.NORTH)) {
            System.out.println("You cannot move in that direction");
          }
          newLocation = model.movePlayer(Direction.NORTH);
          System.out.println("Moving player to: " + newLocation);
          break;
        case "SOUTH":
          if (!availableDirections.contains(Direction.SOUTH)) {
            System.out.println("You cannot move in that direction");
          }
          newLocation = model.movePlayer(Direction.SOUTH);
          System.out.println("Moving player to: " + newLocation);
          break;
        case "EAST":
          if (!availableDirections.contains(Direction.EAST)) {
            System.out.println("You cannot move in that direction");
          }
          newLocation = model.movePlayer(Direction.EAST);
          System.out.println("Moving player to: " + newLocation);
          break;
        case "WEST":
          if (!availableDirections.contains(Direction.WEST)) {
            System.out.println("You cannot move in that direction");
          }
          newLocation = model.movePlayer(Direction.WEST);
          System.out.println("Moving player to: " + newLocation);
          break;
        default:
          System.out.println("Invalid input. Try again!");
      }
      System.out.print("\n");
    }

    System.out.println(model.printPlayerTravelStatus());
    System.out.println("Game Over");
  }
}
