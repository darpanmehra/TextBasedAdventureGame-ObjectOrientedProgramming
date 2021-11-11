package dungeon.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import dungeon.controller.commands.ICommand;
import dungeon.controller.commands.Move;
import dungeon.controller.commands.Pick;
import dungeon.model.IGameState;

public class Controller implements IController {

  private final Scanner scan;
  private final Appendable output;
  private final IGameState model;
  private final Map<String, Function<Scanner, ICommand>> supportedCommands;

  public Controller(Readable scan, Appendable output, IGameState model) {
    if (scan == null || output == null) {
      throw new IllegalArgumentException("input and output must not be null");
    }
    this.scan = new Scanner(scan);
    this.output = output;
    this.model = model;
    supportedCommands = new HashMap<>();
    loadCommands();
  }

  private void loadCommands() {
    supportedCommands.put("M", input -> new Move(scan.next()));
    supportedCommands.put("P", input -> new Pick(scan.next()));
  }

  @Override
  public void playGame() throws IOException {
    output.append("Welcome to the dungeon!\n");

    for (int i = 0; i < 5; i++) {
      output.append(model.getPlayerCurrentLocationStatus());
      output.append("Move, Pickup, or Shoot (M-P-S)? ");
      playGameHelper();
      output.append("\n");
    }
  }

  private void playGameHelper() throws IOException {
    ICommand c;
    String in = inputHelper();
    if (in == "quit") {
      output.append("Goodbye!\n");
      return;
    }
    Function<Scanner, ICommand> cmd = supportedCommands.getOrDefault(in, null);
    if (cmd == null) {
      throw new IllegalArgumentException();
    } else {
      c = cmd.apply(scan);
      c.playGame(model);
    }
  }

  private String inputHelper() throws IOException {
    String input = scan.next();
    if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
      return "quit";
    }
    if (input.equalsIgnoreCase("m")) {
      output.append("Move where? ");
      return input;
    }
    if (input.equalsIgnoreCase("p")) {
      output.append("Pickup what? ");
      return input;
    }
    return null;
  }
}
