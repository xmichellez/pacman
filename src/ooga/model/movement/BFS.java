package ooga.model.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import ooga.model.GameState;
import ooga.model.interfaces.Movable;
import ooga.model.util.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements one type of automatic movement for agent.
 */
public class BFS implements Movable {

  private static final Logger LOG = LogManager.getLogger(BFS.class);

  @Override
  public Position move(GameState state, Position currentPos) {
    Position pacPos = state.getPacman().getPosition();
    Queue<Position> BFSqueue = new LinkedList<>();
    Map<Position, Position> myPath = new HashMap<>();
    List<Position> visited = new ArrayList<>();
    List<Position> optimalPath = new ArrayList<>();

    BFSqueue.add(currentPos);

    while (BFSqueue.size() != 0) {
      Position current = BFSqueue.poll();
      visited.add(current);
      List<Position> availablePositions = state.getPotentialMoveTargets(current);

      for (Position next : availablePositions) {
        if (!visited.contains(next)) {
          BFSqueue.add(next);
          myPath.put(next, current);
        }
      }

      BFSqueue.remove(current);
      if (current.equals(pacPos)) {
        BFSqueue.clear();
        break;
      }
    }

    Position first = pacPos;

    while (first != null) {
      optimalPath.add(first);
      first = myPath.get(first);
    }

//    myPath.forEach(
//        (key, value) -> System.out.println(Arrays.toString(key.getCoords()) + ":" + Arrays.toString(
//            value.getCoords())));
//    optimalPath.forEach(s -> System.out.println(Arrays.toString(s.getCoords())));

    if (optimalPath.size() == 1) {
      //this should never happen unless literally stuck in a box
      return optimalPath.get(0);
    } else {
      //take second index which is next step from currentPos
      return optimalPath.get(optimalPath.size() - 2);
    }
  }

}
