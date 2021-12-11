package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.model.agents.Wall;
import ooga.model.agents.consumables.Pellet;
import ooga.model.agents.players.Pacman;
import ooga.model.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {

  private GameBoard gameBoard;

  @BeforeEach
  void setUp() {

  }

  @Test
  void correctPlayerInstantiated()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    Map<String, List<Position>> initialStates = new HashMap<>();
    initialStates.put("Pacman", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 0));
    initialStates.put("pellet", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 1));
    Map<String, Boolean> pelletInfo = new HashMap<>();
    pelletInfo.put("pellet", Boolean.TRUE);

    GameData vanillaGame = new GameData(initialStates, "Pacman", 0, 3, pelletInfo, 1, 1);

    gameBoard = new GameBoard(vanillaGame);
    Assertions.assertTrue(gameBoard.getGameState().findAgent(new Position(0, 0)) instanceof Pacman);
  }

  @Test
  void correctConsumableInstantiated()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    Map<String, List<Position>> initialStates = new HashMap<>();
    initialStates.put("Pacman", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 0));

    initialStates.put("pellet", new ArrayList<>());
    initialStates.get("pellet").add(new Position(0, 1));

    Map<String, Boolean> pelletInfo = new HashMap<>();
    pelletInfo.put("pellet", Boolean.TRUE);

    GameData vanillaGame = new GameData(initialStates, "Pacman", 0, 3, pelletInfo, 1, 1);

    gameBoard = new GameBoard(vanillaGame);
    Assertions.assertTrue(gameBoard.getGameState().findAgent(new Position(0, 1)) instanceof Pellet);
  }

  @Test
  void correctWallInstantiated()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    Map<String, List<Position>> initialStates = new HashMap<>();
    initialStates.put("Pacman", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 0));

    initialStates.put("Wall", new ArrayList<>());
    initialStates.get("Wall").add(new Position(0, 1));

    initialStates.put("pellet", new ArrayList<>());
    initialStates.get("pellet").add(new Position(0, 2));

    Map<String, Boolean> pelletInfo = new HashMap<>();
    pelletInfo.put("pellet", Boolean.TRUE);

    GameData vanillaGame = new GameData(initialStates, "Pacman", 0, 3, pelletInfo, 1, 1);

    gameBoard = new GameBoard(vanillaGame);
    Assertions.assertTrue(gameBoard.getGameState().findAgent(new Position(0, 1)) instanceof Wall);
  }

  @Test
  void mappingWorks()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    Map<String, List<Position>> initialStates = new HashMap<>();
    initialStates.put("Pacman", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 0));

    initialStates.put("Wall", new ArrayList<>());
    initialStates.get("Wall").add(new Position(0, 1));
    initialStates.put("Dot", new ArrayList<>());
    initialStates.get("Dot").add(new Position(0, 2));

    Map<String, Boolean> pelletInfo = new HashMap<>();
    pelletInfo.put("Dot", Boolean.TRUE);

    GameData vanillaGame = new GameData(initialStates, "Pacman", 0, 3, pelletInfo, 1, 1);

    gameBoard = new GameBoard(vanillaGame);
    Assertions.assertTrue(gameBoard.getGameState().findAgent(new Position(0, 1)) instanceof Wall);
    Assertions.assertTrue(gameBoard.getGameState().findAgent(new Position(0, 2)) instanceof Pellet);
  }

  @Test
  void scoreInputTest()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
    Map<String, List<Position>> initialStates = new HashMap<>();
    initialStates.put("Pacman", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 0));
    initialStates.put("pellet", new ArrayList<>());
    initialStates.get("Pacman").add(new Position(0, 1));
    Map<String, Boolean> pelletInfo = new HashMap<>();
    pelletInfo.put("pellet", Boolean.TRUE);

    GameData vanillaGame = new GameData(initialStates, "Pacman", 5, 3, pelletInfo, 1, 1);

    gameBoard = new GameBoard(vanillaGame);
    Assertions.assertEquals(5, gameBoard.getMyPacScore());
  }
}