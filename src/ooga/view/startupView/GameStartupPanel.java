package ooga.view.startupView;

import static java.util.Objects.isNull;
import static ooga.Main.LANGUAGE;
import static ooga.view.center.agents.MovableView.IMAGE_PATH;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import ooga.controller.Controller;
import ooga.controller.IO.User;
import ooga.controller.IO.UserPreferences;
import ooga.view.instructions.InstructionsView;
import ooga.view.mainView.MainView;
import ooga.view.popups.ErrorPopups;
import ooga.view.userProfileView.UserInformationView;

/**
 * Class that creates the startup panel, which welcomes the user with their profile picture and
 * name; allows them to view their profile; allows the user to select a game file from their local,
 * firebase, or their favorites; allows the user to select a language; allows the user to select a
 * view mode; and when they have selected all options, lets them hit PLAY! to play the game with
 * those specifications.
 *
 * @author Kat Cottrell, Dane Erickson
 */
public class GameStartupPanel {

  public static final String NO_FILE_TEXT = "No file selected";
  public static final String EXAMPLES_PATH = "data/basic_examples";
  public static final String RUN_LOCAL_FILE_METHOD = "uploadLocalFile";
  public static final String RUN_FAVORITE_FILE_METHOD = "uploadFavoriteFile";
  public static final String RUN_FIREBASE_FILE_METHOD = "uploadFirebaseFile";
  private static final int SCREEN_WIDTH = 400;
  private static final int SCREEN_HEIGHT = 530;
  public static final int SELECTOR_WIDTH = 150;
  public static final int SPACING = 10;
  public static final Paint BACKGROUND = Color.BLACK;
  public static final String STARTUP_PACKAGE = "ooga.view.startupView.";
  public static final String DEFAULT_STYLESHEET = String.format("/%sGameStartupPanel.css",
      STARTUP_PACKAGE.replace(".", "/"));
  public static final String RESOURCES_PATH_WITH_LANGUAGE = "ooga.view.resources.English";
  public static final String RESOURCES_PATH = "ooga.view.resources.";
  public static final String DEFAULT_LANGUAGE = "English";
  public static final String GAME_TYPE_KEYS[] = {"VanillaPacman", "SuperPacman", "MrsPacman",
      "GhostPacman"};
  public static final String LANGUAGE_KEYS[] = {"English", "French", "German", "Italian",
      "Spanish"};
  public static final String VIEW_MODE_KEYS[] = {"Dark", "Duke", "Light"};
  public static final String LOAD_FILE_KEYS[] = {"SelectLocally", "SelectFromDatabase",
      "SelectFromFavorites"};
  public static final String STARTUP_RESOURCES = "ooga.view.startupView.resources.";

  private Stage startupStage;
  private Button viewProfile;
  private ComboBox<String> selectLanguage;
  private ComboBox<String> selectViewMode;
  private ComboBox<String> selectFileComboBox;
  private File gameFile;
  private String fileString;
  private String selectedLanguage;
  private String selectedViewMode;
  private ResourceBundle myResources;
  private Text displayFileName;
  private User myUser;
  private Controller myController;
  private String runMethodName;
  private UserPreferences myPreferences;

  /**
   * Constructor to create a GameStartupPanel object that gives user options and loads the file
   *
   * @param stage is the stage
   */
  @Deprecated
  public GameStartupPanel(Stage stage) {
    myResources = ResourceBundle.getBundle(RESOURCES_PATH_WITH_LANGUAGE);
    startupStage = stage;
    startupStage.setScene(createStartupScene());
    startupStage.setTitle("PACMAN STARTUP");
    Image favicon = new Image(new File("data/images/pm_favicon.png").toURI().toString());
    startupStage.getIcons().add(favicon);
    startupStage.show();
  }

  /**
   * Constructor to create a GameStartupPanel object that gives user options and loads the file
   *
   * @param stage is the stage
   */
  public GameStartupPanel(Stage stage, User user, Controller controller) {
    myResources = ResourceBundle.getBundle(RESOURCES_PATH_WITH_LANGUAGE);
    startupStage = stage;
    myUser = user;
    myController = controller;
    selectedLanguage = LANGUAGE;
    startupStage.setScene(createStartupScene());
    startupStage.setTitle("PACMAN STARTUP");
    Image favicon = new Image(new File("data/images/pm_favicon.png").toURI().toString());
    startupStage.getIcons().add(favicon);
    startupStage.show();
  }

  private Scene createStartupScene() {
    VBox root = new VBox();
    root.setSpacing(SPACING);
    root.getStyleClass().add("grid-pane");
    makeBackground(root);
    addPacMan307Img(root);
    addStartupOptions(root);
    addStartButton(root);
    Scene myScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets().add(getClass().getResource(DEFAULT_STYLESHEET).toExternalForm());
    return myScene;
  }

  private void addPacMan307Img(VBox root) {
    ImageView pm307 = new ImageView(
        new Image(new File("data/images/pac_man_307.png").toURI().toString()));
    setImgWidth(pm307, SCREEN_WIDTH);
    root.getChildren().add(pm307);
  }

  private void addStartupOptions(VBox root) {
    VBox profileCluster = new VBox();
    VBox gameFileCluster = new VBox();
    VBox languageCluster = new VBox();
    VBox viewModeCluster = new VBox();
    HBox selectCluster1 = new HBox();
    HBox selectCluster2 = new HBox();
    root.getChildren().add(makeProfileInfo());
    viewProfile = makeButton("viewProfile", profileCluster, e -> makeProfileView());
    selectLanguage = makeSelectorBox(languageCluster, "Language", LANGUAGE_KEYS);
    addToCluster(root, profileCluster, gameFileCluster, selectCluster1);
    selectViewMode = makeSelectorBox(viewModeCluster, "ViewingMode", VIEW_MODE_KEYS);
    selectFileComboBox = makeSelectorBox(gameFileCluster, "GameFile", LOAD_FILE_KEYS);
    selectFileComboBox.setOnAction(e -> selectFileAction());
    displayFileName = makeText(Color.LIGHTGRAY, NO_FILE_TEXT, FontWeight.NORMAL,
        FontPosture.ITALIC, 11, gameFileCluster);
    addToCluster(root, languageCluster, viewModeCluster, selectCluster2);
  }

  private VBox makeProfileInfo() {
    VBox profileInfo = new VBox();
    profileInfo.setSpacing(3);
    profileInfo.setAlignment(Pos.TOP_CENTER);
    HBox pfpBorder = new HBox();
    ImageView profilePic = new ImageView(
        new Image(new File(myUser.imagePath()).toURI().toString()));
    double sideLen = Math.min(profilePic.getFitWidth(), profilePic.getFitHeight());
    profilePic.setViewport(new Rectangle2D(0, 0, sideLen, sideLen));
    setImgWidth(profilePic, SCREEN_WIDTH / 4);
    pfpBorder.getChildren().add(profilePic);
    pfpBorder.setMaxWidth(SCREEN_WIDTH / 4);
    pfpBorder.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID,
        new CornerRadii(5), new BorderWidths(6))));
    profileInfo.getChildren().add(pfpBorder);
    Text username = makeText(Color.LIGHTGRAY,
        String.format("WELCOME, %s!", myUser.username().toUpperCase(Locale.ROOT)),
        FontWeight.BLACK, FontPosture.REGULAR, 16, profileInfo);
    return profileInfo;
  }

  private void makeProfileView() {
    Stage newStage = new Stage();
    new UserInformationView(myController, newStage, selectedLanguage);
  }

  private void selectFileAction() {
    String location = selectFileComboBox.getValue();
    String locationKey = findMethodKey(selectFileComboBox.getValue());
    ResourceBundle fileLoaderMethods = ResourceBundle.getBundle(
        String.format("%s%s", STARTUP_RESOURCES, "fileLoader"));
    String methodName = fileLoaderMethods.getString(locationKey);
    try {
      Method m = GameStartupPanel.class.getDeclaredMethod(methodName, null);
      m.invoke(this, null);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      new ErrorPopups(e, selectedLanguage, "ReflectionError");
    }
  }

  private void localHelper() {
    makeFileExplorer();
  }

  private void firebaseHelper() {
    try {
      makeChoiceDialog(myController.getFirebaseFilenames(), LOAD_FILE_KEYS[1]);
    } catch (FirebaseException | UnsupportedEncodingException e) {
      new ErrorPopups(e, selectedLanguage, "FirebaseError");
    }
  }

  private void favoriteHelper() {
    makeChoiceDialog(Arrays.asList(myUser.favorites()), LOAD_FILE_KEYS[2]);
  }

  private void makeChoiceDialog(Collection files, String resourcesKey) {
    ChoiceDialog fileChoices = new ChoiceDialog<>(myResources.getString(resourcesKey), files);
    fileChoices.setHeaderText(myResources.getString(String.format("%sHeader", resourcesKey)));
    fileChoices.setTitle(myResources.getString(String.format("%sTitle", resourcesKey)));
    fileChoices.showAndWait();
    fileString = fileChoices.getSelectedItem().toString();
    String splitFileString[] = fileString.split("/");
    displayFileName.setText(splitFileString[splitFileString.length - 1]);
  }

  private Text makeText(Paint color, String message, FontWeight fontWeight,
      FontPosture fontPosture, int fontSize, VBox vBox) {
    Text text = new Text();
    text.setFont(Font.font("Verdana", fontWeight, fontPosture, fontSize));
    text.setFill(color);
    text.setText(message);
    vBox.getChildren().add(text);
    return text;
  }

  private ComboBox<String> makeSelectorBox(VBox vBox, String category, String keys[]) {
    ImageView imageLabel = new ImageView(
        new Image(
            new File(String.format("%sselect%s.png", IMAGE_PATH, category)).toURI().toString()));
    setImgWidth(imageLabel, SCREEN_WIDTH / 2);
    List<String> choices = new ArrayList<>();
    for (String s : keys) {
      choices.add(myResources.getString(s));
    }
    ComboBox<String> comboBox = makeDropDown(category, choices.toArray(new String[0]));
    comboBox.setId(category);
    vBox.getChildren().addAll(imageLabel, comboBox);
    vBox.setAlignment(Pos.TOP_CENTER);
    return comboBox;
  }

  private void addToCluster(VBox root, VBox vBoxChild1, VBox vBoxChild2, HBox hBoxParent) {
    hBoxParent.setAlignment(Pos.TOP_CENTER);
    hBoxParent.getChildren().addAll(vBoxChild1, vBoxChild2);
    root.getChildren().add(hBoxParent);
  }

  private Button makeButton(String id, VBox vbox, EventHandler<ActionEvent> handler) {
    ImageView buttonLabel = new ImageView(new Image(
        new File("data/images/" + id + ".png").toURI().toString()));
    setImgWidth(buttonLabel, SCREEN_WIDTH / 2);
    Button button = new Button();
    button.setMinWidth(SELECTOR_WIDTH);
    button.setId(id);
    button.setText(myUser.username());
    button.setOnAction(handler);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.getChildren().addAll(buttonLabel, button);
    return button;
  }

  private void makeFileExplorer() {
    gameFile = fileExplorer();
    fileString = gameFile.getPath();
    if (gameFile != null) {
      displayFileName.setText(gameFile.getName());
    }
  }

  private File fileExplorer() {
    // Credit to Carl Fisher for writing this code in Cell Society team 6
    FileChooser myFileChooser = new FileChooser();
    myFileChooser.setInitialDirectory(new File(EXAMPLES_PATH));
    return myFileChooser.showOpenDialog(startupStage);
  }

  private void addStartButton(VBox root) {
    ImageView startButton = new ImageView(
        new Image(new File("data/images/playButton.png").toURI().toString()));
    setImgWidth(startButton, SCREEN_WIDTH / 4);
    startButton.setId("startButton");
    startButton.setOnMouseReleased(e -> startButtonAction());
    HBox playBox = new HBox();
    playBox.getChildren().add(startButton);
    playBox.setAlignment(Pos.CENTER);
    root.getChildren().add(playBox);
  }

  private void startButtonAction() {
    selectedLanguage = selectLanguage.getValue();
    selectedViewMode = selectViewMode.getValue();
    if (!isNull(selectedLanguage) && !isNull(selectedViewMode)) {
      runFile();
      openInstructions(selectedLanguage, selectedViewMode);
      selectLanguage.setValue(null);
      selectViewMode.setValue(null);
      displayFileName.setText(NO_FILE_TEXT);
    } else {
      if (selectedLanguage == null) {
        selectedLanguage = DEFAULT_LANGUAGE;
      }
      new ErrorPopups(new Exception(), selectedLanguage, "RequiredFields");
    }
  }

  private void openInstructions(String selectedLanguage, String selectedViewMode) {
    Stage instructionsStage = new Stage();
    InstructionsView instructionsView = new InstructionsView(instructionsStage, selectedLanguage,
        selectedViewMode);
  }

  private String findMethodKey(String value) {
    for (String key : myResources.keySet()) {
      if (myResources.getString(key).equals(value)) {
        return key;
      }
    }
    return null;
  }

  private void runFile() {
//    Controller application = new Controller(selectedLanguage, mainStage, selectedViewMode);
    UserPreferences userPreferences;
    String locationKey = findMethodKey(selectFileComboBox.getValue());
    ResourceBundle uploadMethods = ResourceBundle.getBundle(
        String.format("%s%s", STARTUP_RESOURCES, "uploadMethods"));
    String methodName = uploadMethods.getString(locationKey);
    try {
      Method m = Controller.class.getDeclaredMethod(methodName, String.class);
      myPreferences = (UserPreferences) m.invoke(myController, fileString);
      if (!myController.getPlayPause()) {
        myController.pauseOrResume();
      }
      new MainView(myController, myController.getVanillaGame(), new Stage(), selectedViewMode,
          myPreferences, myUser);
    } catch (Exception e) {
      if (gameFile == null) {
        new ErrorPopups(e, selectedLanguage, "NoFile");
      } else {
        new ErrorPopups(e, selectedLanguage, "InvalidFile");
        e.printStackTrace();
      }
    }
  }

  // Used for testing to show but that new language is not updated
  protected String getNewLanguage() { return myPreferences.language(); }

  private ComboBox makeDropDown(String category, String[] options) {
    ComboBox<String> newComboBox = new ComboBox<>();
    newComboBox.setPromptText(myResources.getString(String.format("Select%s", category)));
    for (String option : options) {
      newComboBox.getItems().add(option);
    }
    newComboBox.setMinWidth(SELECTOR_WIDTH);
    newComboBox.setId(category);
    return newComboBox;
  }

  private void makeBackground(VBox root) {
    BackgroundFill background_fill = new BackgroundFill(BACKGROUND,
        CornerRadii.EMPTY, Insets.EMPTY);
    Background background = new Background(background_fill);
    root.setBackground(background);
  }

  private void setImgWidth(ImageView img, int width) {
    img.setPreserveRatio(true);
    img.setFitWidth(width);
  }

  /**
   * Getter method to get the selected language. Used in user profile info screen.
   *
   * @return String selectedLanguage
   */
  public String getLanguage() {
    return selectedLanguage;
  }

}
