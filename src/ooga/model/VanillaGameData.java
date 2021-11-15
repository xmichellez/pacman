package ooga.model;

import java.util.List;
import java.util.Map;
import ooga.model.util.AgentInfo;

public class VanillaGameData implements VanillaGameDataInterface {
  private Map<String, List<AgentInfo>> wallMap;
  private String player;
  private Map<String, Boolean> pelletInfo;

  public VanillaGameData (Map<String, List<AgentInfo>> wallMap, String player, Map<String, Boolean> pelletInfo) {
    this.wallMap = wallMap;
    this.player = player;
    this.pelletInfo = pelletInfo;
  }

  @Override
  public Map<String, List<AgentInfo>> getWallMap() {
    return wallMap;
  }

  @Override
  public String getPlayer() {
    return player;
  }

  @Override
  public Map<String, Boolean> getPelletInfo() {
    return pelletInfo;
  }
}
