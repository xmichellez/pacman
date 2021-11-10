package ooga.model;

/**Interface to allow controller to track the state of objects that are able to be consumed, including how many points they are worth**/
public interface Consumable extends Agent {

  /** upon being consumed by the main agent, set the value of consumed to true **/
  void consume();

  /** implemenrts the logic for how an agent reacts after being consumed **/
  void agentReact();

  /** implements the external effects of that consumable being consumed; fruits and dots increase point values, super-dots set pacman into super mode, ghosts increase the score**/
  void applyEffects();

}
