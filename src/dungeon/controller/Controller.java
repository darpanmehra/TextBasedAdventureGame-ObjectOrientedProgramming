package dungeon.controller;

import java.io.IOException;
import java.util.Scanner;

import dungeon.controller.commands.ICommand;
import dungeon.controller.commands.Move;
import dungeon.controller.commands.Pick;
import dungeon.controller.commands.Shoot;
import dungeon.model.IGameState;
import dungeon.model.directions.Direction;
import dungeon.model.treasure.TreasureType;

/**
 * The controller for the dungeon game.
 */
public class Controller implements IController {

  private final Scanner scan;
  private final Appendable output;
  private final IGameState model;

  /**
   * Constructor for the controller.
   *
   * @param scan   the scanner to read input from.
   * @param output the output to write to.
   * @param model  the model to use.
   */
  public Controller(Readable scan, Appendable output, IGameState model) {
    if (scan == null || output == null) {
      throw new IllegalArgumentException("input and output must not be null");
    }
    this.scan = new Scanner(scan);
    this.output = output;
    this.model = model;
  }

  @Override
  public void playGame() {
    try {
      output.append("Welcome to the dungeon!\n\n");
      Boolean isPlayerQuit = false;
      while (!model.isGameOver()) {
        output.append(model.getPlayerCurrentLocationStatus());
        output.append("Move, Pickup, or Shoot (M-P-S-Q)? ");
        isPlayerQuit = playGameHelper();
        if (isPlayerQuit) {
          output.append("You quit the game.\n");
          output.append("Goodbye!\n");
          break;
        }
        output.append("\n");
      }
      //Print out the final result of the game
      if (!isPlayerQuit) {
        output.append("Game Over!").append("\n");
        output.append(model.getGameOverStatus());
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }

  private boolean playGameHelper() throws IOException {
    String command = getInputHelper();
    switch (command) {
      case "move":
        try {
          output.append("Move where? ");
          Direction direction = inputHelperDirection(scan.next());
          ICommand move = new Move(direction);
          move.playGame(model);
          output.append("Moving " + direction + "\n");
        } catch (IllegalArgumentException e) {
          output.append(e.getMessage()).append("\n");
        }
        break;
      case "pick":
        try {
          output.append("Pickup what? ");
          TreasureType item = inputHelperTreasureType(scan.next());
          ICommand pick = new Pick(item);
          pick.playGame(model);
          output.append("Picking up " + item + "\n");
        } catch (IllegalArgumentException e) {
          output.append(e.getMessage()).append("\n");
        } catch (IllegalStateException e) {
          output.append(e.getMessage()).append("\n");
        } catch (Exception e) {
          output.append("We cannot service this request.\n");
        }
        break;
      case "shoot":
        try {
          output.append("Which direction? ");
          Direction direction = inputHelperDirection(scan.next());
          output.append("No.of caves (1-5)? ");
          int cavesCount = inputHelperCavesCount(scan.next());
          ICommand shoot = new Shoot(direction, cavesCount);
          shoot.playGame(model);
          output.append("Shooting in " + direction + " at distance of " + cavesCount + "\n");
        } catch (IllegalArgumentException e) {
          output.append(e.getMessage()).append("\n");
        } catch (IllegalStateException e) {
          output.append(e.getMessage()).append("\n");
        } catch (Exception e) {
          output.append("We cannot service this request.\n");
        }
        break;
      case "quit":
        return true;
      default:
        output.append("Not a valid command: " + command + "\n");
    }
    return false;
  }

  private int inputHelperCavesCount(String input) {
    if (!input.matches("[1-5]")) {
      throw new IllegalArgumentException("Invalid caves count");
    }
    return Integer.parseInt(input);
  }

  private Direction inputHelperDirection(String input) {
    input = input.toLowerCase();
    switch (input) {
      case "n":
      case "north":
        return Direction.NORTH;
      case "s":
      case "south":
        return Direction.SOUTH;
      case "e":
      case "east":
        return Direction.EAST;
      case "w":
      case "west":
        return Direction.WEST;
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
  }

  private TreasureType inputHelperTreasureType(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Treasure to be picked cannot be null");
    }

    input = input.toLowerCase();
    switch (input) {
      case "rubies":
      case "ruby":
        return TreasureType.RUBIES;
      case "diamonds":
      case "diamond":
        return TreasureType.DIAMONDS;
      case "sapphire":
      case "sapphires":
        return TreasureType.SAPPHIRES;
      case "arrow":
      case "arrows":
        return TreasureType.ARROWS;
      default:
        throw new IllegalArgumentException("Invalid treasure type");
    }
  }

  private String getInputHelper() throws IOException {
    String input;
    boolean moreInput = true;
    while (moreInput) {
      input = scan.next();
      if (input.equalsIgnoreCase("m")
              || input.equalsIgnoreCase("move")) {
        return "move";
      } else if (input.equalsIgnoreCase("p")
              || input.equalsIgnoreCase("pick")) {
        return "pick";
      } else if (input.equalsIgnoreCase("s")
              || input.equalsIgnoreCase("shoot")) {
        return "shoot";
      } else if (input.equalsIgnoreCase("q")
              || input.equalsIgnoreCase("quit")) {
        return "quit";
      } else {
        output.append("Not a valid command: " + input + ".\n").append("Try again: ");
        return getInputHelper();
      }
    }
    return null;
  }

}
