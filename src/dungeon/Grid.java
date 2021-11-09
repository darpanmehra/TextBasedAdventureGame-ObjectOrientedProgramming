package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import dungeon.directions.Direction;
import dungeon.location.ILocation;
import dungeon.location.Location;

/**
 * A grid of locations which is a dungeon in the game. It is created randomly using the Kruskal's
 * algorithm for Minimum Spanning Tree. The inter-connectivity value adds those number of extra
 * paths in the dungeon randomly.
 */
public class Grid {
  private final Random random;
  private final ILocation[][] maze;
  private Set<SortedSet<ILocation>> potentialPaths;
  private List<SortedSet> leftOverList;

  private List<List<ILocation>> kruskalLocationGroup;

  private final int MIN_PATH_LENGTH = 5;
  private List<ILocation> caveList;
  private final double treasurePercentage;
  private ILocation startLocation;
  private ILocation endLocation;

  /**
   * Constructor for the Grid.
   *
   * @param numRow             the width of the grid
   * @param numCol             the height of the grid
   * @param interConnectivity  the number of extra paths to add to the grid
   * @param dungeonType        the type of dungeon to create
   * @param treasurePercentage the percentage of treasure in the grid
   * @param random             the random number generator
   */
  protected Grid(int numRow, int numCol, int interConnectivity, String dungeonType,
                 double treasurePercentage, Random random) {

    if (numRow < 6 || numRow > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (numCol < 6 || numCol > 100) {
      throw new IllegalArgumentException("Dungeon width must be between 10 and 100");
    }
    if (interConnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity must be between 0 and dungeon size");
    }
    if (!(dungeonType.equalsIgnoreCase("wrapping")
            || dungeonType.equalsIgnoreCase("nonwrapping"))) {
      throw new IllegalArgumentException("Dungeon type must be wrapping or nonwrapping");
    }
    if (treasurePercentage < 0 || treasurePercentage > 100) {
      throw new IllegalArgumentException("Treasure percentage must be between 0 and 100");
    }
    if (random == null) {
      throw new IllegalArgumentException("Random must be specified");
    }

    this.random = random;
    maze = new ILocation[numRow][numCol];
    this.treasurePercentage = treasurePercentage;

    //Initialize data structures
    potentialPaths = new HashSet<>();
    kruskalLocationGroup = new ArrayList<>();
    leftOverList = new ArrayList<>();
    caveList = new ArrayList<>();
    this.startLocation = null;
    this.endLocation = null;

    //Creating Dungeon
    initializeLocations(); //Initialize locations in the maze

    if (dungeonType.equalsIgnoreCase("nonwrapping")) {
      updatePotentialPaths();
    } else if (dungeonType.equalsIgnoreCase("wrapping")) {
      updatePotentialPathsWrapping();
    }

    createDungeonUsingKruskal(); //Create a dungeon using the Kruskal's algorithm
    addInterconnectivity(interConnectivity); //Add interconnectivity to the dungeon

    //Get Cave Locations
    caveList = getAllCavesInDungeon(); //Get the list of caves
    assignTreasures(); //Assign treasure to the caves

    determineStartAndEndLocation(); //Assign start and end locations
  }

  private void initializeLocations() {

    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        maze[i][j] = new Location(i, j, random);
        //Put the location in the kruskal group to join it later in the algorithm
        putInKruskalLocationGrouping(maze[i][j]);
      }
    }
  }

  private void putInKruskalLocationGrouping(ILocation location) {
    List<ILocation> group = new ArrayList<>();
    group.add(location);
    kruskalLocationGroup.add(group);
  }

  private void updatePotentialPaths() {
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        if (((i + 1) < maze.length)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i + 1][j]);
          potentialPaths.add(path);
        }
        if (((j + 1) < maze[i].length)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i][j + 1]);
          potentialPaths.add(path);
        }
      }
    }
  }

  private void updatePotentialPathsWrapping() {
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        if (((i - 1) >= 0)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i - 1][j]);
          potentialPaths.add(path);
        } else {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[maze.length - 1][j]);
          potentialPaths.add(path);
        }
        if (((i + 1) < maze.length)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i + 1][j]);
          potentialPaths.add(path);
        } else {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[0][j]);
          potentialPaths.add(path);
        }
        if (((j + 1) < maze[i].length)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i][j + 1]);
          potentialPaths.add(path);
        } else {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i][0]);
          potentialPaths.add(path);

        }
        if (((j - 1) >= 0)) {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i][j - 1]);
          potentialPaths.add(path);
        } else {
          SortedSet<ILocation> path = new TreeSet<>();
          path.add(maze[i][j]);
          path.add(maze[i][maze[i].length - 1]);
          potentialPaths.add(path);
        }
      }
    }
  }

  private void createDungeonUsingKruskal() {
    List<SortedSet<ILocation>> list = new ArrayList<>();
    list.addAll(potentialPaths);

    while (list.size() > 0) {
      ILocation firstLocation = null;
      ILocation secondLocation = null;
      int firstLocationIndex = -1;
      int secondLocationIndex = -1;
      int count = 0;
      var pathIndex = random.nextInt(list.size());
      var sortedLocationSet = list.get(pathIndex);
      for (ILocation value : sortedLocationSet) {
        if (count == 0) {
          firstLocation = value;
          count++;
        }
        secondLocation = value;
      }

      list.remove(pathIndex);
      for (int i = 0; i < kruskalLocationGroup.size(); i++) {
        if (kruskalLocationGroup.get(i).contains(firstLocation)) {
          firstLocationIndex = i;
        }
        if (kruskalLocationGroup.get(i).contains(secondLocation)) {
          secondLocationIndex = i;
        }

      }

      if (firstLocationIndex != secondLocationIndex) {
        joinLocation(firstLocation, secondLocation); // Make the locations neighbors of each other
        kruskalLocationGroup.get(firstLocationIndex)
                .addAll(kruskalLocationGroup.get(secondLocationIndex));
        kruskalLocationGroup.remove(secondLocationIndex);
      } else { // Path added in the leftover list
        leftOverList.add(sortedLocationSet);
      }
    }
  }

  private void joinLocation(ILocation firstLocation, ILocation secondLocation) {

    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        if (firstLocation.equals(maze[i][j])) {
          if (((i + 1) <= maze.length - 1)) {
            if (maze[i + 1][j].equals(secondLocation)) {
              firstLocation.joinLocationToSouthDirection(secondLocation);
              secondLocation.joinLocationToNorthDirection(firstLocation);
            }
          } else if ((i + 1) > maze.length - 1) {
            if (maze[0][j].equals(secondLocation)) {
              firstLocation.joinLocationToSouthDirection(secondLocation);
              secondLocation.joinLocationToNorthDirection(firstLocation);
            }
          }
          if (((i - 1) >= 0)) {
            if (maze[i - 1][j].equals(secondLocation)) {
              firstLocation.joinLocationToNorthDirection(secondLocation);
              secondLocation.joinLocationToSouthDirection(firstLocation);
            }
          } else if ((i - 1) < 0) {
            if (maze[maze.length - 1][j].equals(secondLocation)) {
              firstLocation.joinLocationToNorthDirection(secondLocation);
              secondLocation.joinLocationToSouthDirection(firstLocation);
            }
          }
          if (((j + 1) <= maze[i].length - 1)) {
            if (maze[i][j + 1].equals(secondLocation)) {
              firstLocation.joinLocationToEastDirection(secondLocation);
              secondLocation.joinLocationToWestDirection(firstLocation);
            }
          } else if ((j + 1) > maze[i].length - 1) {
            if (maze[i][0].equals(secondLocation)) {
              firstLocation.joinLocationToEastDirection(secondLocation);
              secondLocation.joinLocationToWestDirection(firstLocation);
            }
          }
          if (((j - 1) >= 0)) {
            if (maze[i][j - 1].equals(secondLocation)) {
              firstLocation.joinLocationToWestDirection(secondLocation);
              secondLocation.joinLocationToEastDirection(firstLocation);
            }

          } else if ((j - 1) < 0) {
            if (maze[i][maze[i].length - 1].equals(secondLocation)) {
              firstLocation.joinLocationToWestDirection(secondLocation);
              secondLocation.joinLocationToEastDirection(firstLocation);
            }
          }
        }
      }
    }
  }

  private void addInterconnectivity(int numAdditionalConnections) {

    ILocation firstLocation = null;
    ILocation secondLocation = null;
    SortedSet<ILocation> sortedLocationSet;
    for (int i = 0; i < numAdditionalConnections; i++) {
      if (leftOverList.size() == 0) {
        break;
      }
      int randomIndex = random.nextInt(leftOverList.size());
      sortedLocationSet = leftOverList.get(randomIndex);
      int count = 0;
      for (ILocation value : sortedLocationSet) {
        if (count == 0) {
          firstLocation = value;
          count++;
        }
        secondLocation = value;
      }
      joinLocation(firstLocation, secondLocation);
      leftOverList.remove(randomIndex);
    }
  }

  private List<ILocation> getAllCavesInDungeon() {
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        ILocation loc = maze[i][j];
        if (loc.isCave()) {
          caveList.add(loc);
        }
      }
    }
    return caveList;
  }

  private void assignTreasures() {
    int totalCaves = caveList.size();
    double numberOfTreasuresCaves = Math.ceil(totalCaves * (treasurePercentage / 100));
    for (double i = 0.0; i < numberOfTreasuresCaves; i++) {
      int randomIndex = random.nextInt(caveList.size());
      caveList.get(randomIndex).setTreasure();
      caveList.remove(randomIndex);
    }
  }

  private void determineStartAndEndLocation() {
    int randomIndex = random.nextInt(caveList.size());
    startLocation = caveList.get(randomIndex);
    endLocation = modifiedBreadthFirstSearchToFindEnd(startLocation, MIN_PATH_LENGTH);
  }

  private ILocation modifiedBreadthFirstSearchToFindEnd(ILocation startLocation,
                                                        int minPathLength) {
    Set<ILocation> visited = new HashSet<>();
    Integer level = 0;
    Queue<Map<ILocation, Integer>> objects = new LinkedList<>();
    Map<ILocation, Integer> map = new HashMap<>();
    map.put(startLocation, level);
    objects.add(map);
    ILocation currentLocation = startLocation;


    while (!objects.isEmpty()) {

      Map<ILocation, Integer> current = objects.remove();
      for (ILocation location : current.keySet()) {
        currentLocation = location;
      }
      int currentLevel = current.get(currentLocation);

      // if we have found the end location, return it
      if (currentLocation.isCave()
              && current.get(currentLocation) >= minPathLength
              && !visited.contains(currentLocation)) {
        return currentLocation;
      }
      visited.add(currentLocation);

      // add all the adjacent locations to the queue
      for (ILocation value : currentLocation.getNeighbours().values()) {
        if (value != null) {
          if (!visited.contains(value)) {
            Map<ILocation, Integer> newMap = new HashMap<>();
            newMap.put(value, currentLevel + 1);
            objects.add(newMap);
          }
        }
      }

    }
    return currentLocation;
  }

  //Getters
  protected ILocation getPlayerStartLocation() {
    return new Location(startLocation);
  }

  protected ILocation getPlayerEndLocation() {
    return new Location(endLocation);
  }

  protected Map<Direction, ILocation> getNeighbours(ILocation location) {
    return location.getNeighbours();
  }

  protected ILocation[][] getDungeonCopy() {
    ILocation[][] dungeonCopy = new ILocation[maze.length][maze[0].length];
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[i].length; j++) {
        dungeonCopy[i][j] = new Location(maze[i][j]);
      }
    }
    return dungeonCopy;
  }

}
