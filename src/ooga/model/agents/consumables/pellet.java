package ooga.model.agents.consumables;

import java.util.function.Consumer;
import ooga.model.Consumable;
import ooga.model.movement.MovementStrategyContext;
import ooga.model.movement.Static;
import ooga.model.util.AgentInfo;

public class pellet implements Consumable {

  private AgentInfo myData;
  private MovementStrategyContext myMover;

  @Override
  public void setData(AgentInfo data) {
    myData = data;
    myMover = new MovementStrategyContext(new Static());
  }

  @Override
  public AgentInfo getData() {
    return myData;
  }

  @Override
  public void addConsumer(Consumer<AgentInfo> consumer) {

  }

  @Override
  public void updateConsumer() {

  }

  @Override
  public void step() {

  }

  @Override
  public void consume() {

  }

  @Override
  public void agentReact() {

  }

  @Override
  public void applyEffects() {

  }
}
