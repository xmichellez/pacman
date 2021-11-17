package ooga.factories;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import ooga.model.interfaces.Agent;

public class AgentFactory {

  private static final String DEFAULT_RESOURCE_PACKAGE =
      AgentFactory.class.getPackageName() + ".resources.";
  private static final String PACKAGES_FILENAME = "Packages";
  private static final String CLASS_NAMES_FILENAME = "classNames";

  public Agent createAgent(String agent, int x, int y)
      throws IllegalArgumentException {
    Agent createdAgent = null;
    int numNot = 0;
    ResourceBundle packages = ResourceBundle.getBundle(
        String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, PACKAGES_FILENAME));
    ResourceBundle classNames = ResourceBundle.getBundle(String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, CLASS_NAMES_FILENAME));
    for (String aPackage : packages.keySet()) {
      try {
        createdAgent = (Agent) Class.forName(
                String.format("%s%s", packages.getString(aPackage), classNames.getString(agent))).getConstructor(int.class, int.class)
            .newInstance(x, y);
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        numNot++;
      }
    }

    if (numNot == packages.keySet().size()) {
      System.out.println(agent);
      throw new IllegalArgumentException();
    }
    return createdAgent;
  }
}
