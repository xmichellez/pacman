package ooga.view.center.agents;

import static ooga.model.agents.players.Pacman.ALIVE_STATE;
import static ooga.view.center.BoardView.BOARD_HEIGHT;
import static ooga.view.center.BoardView.BOARD_WIDTH;
import static ooga.view.center.agents.PelletView.PELLET_COLOR;

import java.util.List;
import java.util.function.Consumer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ooga.model.interfaces.Agent;

public class SuperPelletView extends StationaryView {

  public static final double LARGE_PELLET_SIZE = 0.25; // large pellets radii are 50% of grid square

//  private VanillaGame myGame;
//  private Position myInfo;
  private Agent myAgent; //TODO: change to correct agent subclass
  private Circle myCircle;
  private Consumer<Agent> updatePellet = newInfo -> updateAgent(newInfo);
  private int numCols;
  private int numRows;
  private double gridWidth;
  private double gridHeight;
  private double imageBuffer;
  private double pelletBufferX;
  private double pelletBufferY;

  public SuperPelletView(Agent pelletAgent, int gridRows, int gridCols) {
//    this(pelletAgent, PELLET_COLOR, gridRows, gridCols);
    myAgent = pelletAgent;
    numCols = gridCols;
    numRows = gridRows;
    makeLayoutSettings();
    myCircle = makeCircle(PELLET_COLOR);
    superPelletViewSetup();
  }

  public SuperPelletView(Agent pelletAgent, List<Double> rgb, int gridRows, int gridCols) {
    myAgent = pelletAgent;
    numCols = gridCols;
    numRows = gridRows;
    makeLayoutSettings();
    // TODO: move these values to a props file
    Color pelletColor = new Color(rgb.get(0), rgb.get(1), rgb.get(2), 1);
    myCircle = makeCircle(pelletColor);
    superPelletViewSetup();
  }

  private void makeLayoutSettings() {
    gridWidth = BOARD_WIDTH / numCols;
    gridHeight = BOARD_HEIGHT / numRows;
    imageBuffer = IMAGE_BUFFER_FACTOR * Math.min(gridWidth, gridHeight);
    pelletBufferX = gridWidth / 2;
    pelletBufferY = gridHeight / 2;
  }

  private void superPelletViewSetup() {
    setImage(myCircle);
    myCircle.setCenterX(gridWidth*myAgent.getPosition().getCoords()[0] + pelletBufferX);
    myCircle.setCenterY(gridHeight*myAgent.getPosition().getCoords()[1] + pelletBufferY);
    myAgent.addConsumer(updatePellet);
  }

  private Circle makeCircle(Paint color) {
    int x = myAgent.getPosition().getCoords()[0];
    int y = myAgent.getPosition().getCoords()[1];
    return new Circle(x, y, Math.min(gridHeight,gridWidth)*LARGE_PELLET_SIZE, color);
  }

  @Override
  protected void updateState(int newState) {
    myCircle.setVisible(newState == ALIVE_STATE);
  }
}
