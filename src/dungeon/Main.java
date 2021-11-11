package dungeon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import dungeon.controller.Controller;
import dungeon.controller.IController;
import dungeon.model.GameState;
import dungeon.model.IGameState;
import dungeon.model.location.ILocation;

public class Main {

  //6 6 0 nonwrapping 20 2

  public static void main(String[] args) throws IOException {
    int dungeonHeight = Integer.parseInt(args[0]);
    int dungeonWidth = Integer.parseInt(args[1]);
    int interConnectivity = Integer.parseInt(args[2]);
    String dungeonType = (args[3]).toLowerCase();
    int treasurePercentage = Integer.parseInt(args[4]);
    int monsterCount = Integer.parseInt(args[5]);

    //Mock Random method
    Random rand = new Random();
    rand.setSeed(50);

    // Create a new dungeon- Initialize the model
    IGameState model = new GameState(dungeonHeight, dungeonWidth, interConnectivity,
            dungeonType, treasurePercentage, monsterCount, rand);

//    System.out.println("Welcome to the Dungeon! \n\n");
//    ILocation playerStartLocation = model.getPlayerStartLocation();
//    System.out.println("Player Start Position: " + playerStartLocation.getName());
//
//    ILocation playerEndLocation = model.getPlayerEndLocation();
//    System.out.println("Player End Position: " + playerEndLocation.getName());
//
//    // Print Dungeon Copy
//    ILocation[][] dungeon = model.getDungeon();
//    StringBuilder dungeonBuilder = new StringBuilder();
//    for (int i = 0; i < dungeon.length; i++) {
//      for (int j = 0; j < dungeon[i].length; j++) {
//        dungeonBuilder.append(dungeon[i][j].printLocationInfo());
//        dungeonBuilder.append("\n");
//      }
//    }
//    System.out.println(dungeonBuilder);

    //Controller
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    IController controller = new Controller(input, output, model);

    //Play game
    controller.playGame();
  }

}
