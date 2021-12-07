package ooga.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import ooga.factories.AgentFactory;
import ooga.factories.ConsumableFactory;
import ooga.model.interfaces.Agent;
import ooga.model.interfaces.Consumable;
import ooga.model.util.Position;


public class GameStateData {

  private boolean isSuper;
  private int myPacScore;
  private int myGhostScore;
  private int foodLeft;
  private final AgentFactory agentFactory = new AgentFactory();
  private final ConsumableFactory consumableFactory = new ConsumableFactory();
  private List<Agent> myAgentStates;
  private List<Position> myInitAgentPositions;
  private List<Consumable> myRequiredPelletStates;
  private List<Agent> myWallStates;
  private boolean[][] myWallMap;
  private int pacmanLives;

  public GameStateData() {
    myPacScore = 0;
    myGhostScore = 0;
    myAgentStates = new ArrayList<>();
    myRequiredPelletStates = new ArrayList<>();
  }

  public void initialize(GameData data) {
    Map<String, List<Position>> gameDict = data.wallMap();
    Map<String, Boolean> pelletInfo = data.pelletInfo();
    int rows = calculateDimension(gameDict, 1) + 1;
    int cols = calculateDimension(gameDict, 0) + 1;
    isSuper = false;
    myPacScore = 0;
    myGhostScore = 0;
    myAgentStates = new ArrayList<>();
    myRequiredPelletStates = new ArrayList<>();
    myWallStates = new ArrayList<>();
    myInitAgentPositions = new ArrayList<>();
    myWallMap = new boolean[cols][rows];
    pacmanLives = data.numLives();
//    System.out.println(pacmanLives);
    createWallMap(gameDict, rows, cols);
    createAgentList(gameDict);
    createWallList(gameDict);
    createRequiredPelletList(gameDict, pelletInfo);
    createEmptySpots(gameDict);
  }

  public int getFoodLeft() {
    foodLeft = myRequiredPelletStates.size();
    return foodLeft;
  }

  public List<Consumable> getMyRequiredPelletStates() {
    return myRequiredPelletStates;
  }

  public List<Agent> getMyWallStates() {
    return myWallStates;
  }

  public boolean isSuper() {
    return isSuper;
  }

  public void setSuper() {
    isSuper = true;
    attachSuperTimer();

  }

  private void attachSuperTimer() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        isSuper = false;
      }
    }, 5000);
  }

  public int getMyPacScore() {
    return myPacScore;
  }

  public int getMyGhostScore() {
    return myGhostScore;
  }

  public boolean isWall(int x, int y) {
    try {
      return myWallMap[x][y];
    } catch (Exception e) {
      return false;
    }
  }

  public List<Agent> getAgents() {
    return myAgentStates;
  }

  public Agent findAgent(Position pos) {
    Agent potentialAgent = null;
    for (Agent agent : myAgentStates) {
      if (agent.getPosition().getCoords()[0] == pos.getCoords()[0]
          && agent.getPosition().getCoords()[1] == pos.getCoords()[1]) {
        potentialAgent = agent;
      }
    }

    for (Agent agent : myRequiredPelletStates) {
      if (agent.getPosition().getCoords()[0] == pos.getCoords()[0]
          && agent.getPosition().getCoords()[1] == pos.getCoords()[1]) {
        potentialAgent = agent;
      }
    }

    for (Agent agent : myWallStates) {
      if (agent.getPosition().getCoords()[0] == pos.getCoords()[0]
          && agent.getPosition().getCoords()[1] == pos.getCoords()[1]) {
        potentialAgent = agent;
      }
    }
    return potentialAgent;
  }

  private void createRequiredPelletList(Map<String, List<Position>> gameDict,
      Map<String, Boolean> pelletInfo) {
    for (String key : pelletInfo.keySet()) {
      if (pelletInfo.get(key)) {
        List<Position> tempPellets = gameDict.get(key);
        if (tempPellets != null) {
          for (Position dot : tempPellets) {
            int x = dot.getCoords()[0];
            int y = dot.getCoords()[1];
            myRequiredPelletStates.add(consumableFactory.createConsumable(key, x, y));
          }
        } else {
          throw new IllegalArgumentException("We can't win without required pellets!");
        }
      }
    }
    foodLeft = myRequiredPelletStates.size();
  }

  private void createEmptySpots(Map<String, List<Position>> gameDict) {
    if (gameDict.get("Empty") != null) {
      for (Position emptyPos : gameDict.get("Empty")) {
        int x = emptyPos.getCoords()[0];
        int y = emptyPos.getCoords()[1];
        myInitAgentPositions.add(new Position(x, y));
        myAgentStates.add(agentFactory.createAgent("Empty", x, y));
      }
    }
  }

  public int getPacmanLives() {
    return pacmanLives;
  }

  private void createAgentList(Map<String, List<Position>> gameDict) {
    for (Position agentPos : gameDict.get("Pacman")) {
      int x = agentPos.getCoords()[0];
      int y = agentPos.getCoords()[1];
      myInitAgentPositions.add(new Position(x, y));
      myAgentStates.add(agentFactory.createAgent("Pacman", x, y));
    }

    if (gameDict.get("Ghost") != null) {
      for (Position agentPos : gameDict.get("Ghost")) {
        int x = agentPos.getCoords()[0];
        int y = agentPos.getCoords()[1];
        myInitAgentPositions.add(new Position(x, y));
        myAgentStates.add(agentFactory.createAgent("Ghost", x, y));
      }
    }
  }

  private void createWallList(Map<String, List<Position>> gameDict) {
    if (gameDict.get("Wall") != null) {
      for (Position wallPos : gameDict.get("Wall")) {
        int x = wallPos.getCoords()[0];
        int y = wallPos.getCoords()[1];
        myWallStates.add(agentFactory.createAgent("Wall", x, y));
      }
    }
  }

  public void decreaseLives() {
    pacmanLives--;
  }

  public List<Position> getMyInitAgentPositions() {
    return myInitAgentPositions;
  }

  private void createWallMap(Map<String, List<Position>> gameDict, int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        myWallMap[j][i] = false;
      }
    }
    List<Position> walls = gameDict.get("Wall");

    if (walls != null) {
      for (Position wall : walls) {
        myWallMap[wall.getCoords()[0]][wall.getCoords()[1]] = true;
      }
    }
  }

  private int calculateDimension(Map<String, List<Position>> initialStates, int dim) {
    int maxCol = 0;
    for (String key : initialStates.keySet()) {
      for (Position position : initialStates.get(key)) {
        maxCol = Math.max(position.getCoords()[dim], maxCol);
      }
    }
    return maxCol;
  }


}
