package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("sampleanil.fxml"));
        GridPane rootGridPane = loader.load();
        controller = loader.getController();
        controller.createPlayground();
        MenuBar menuBar= createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());//thos will make the menu bar start from top left corner to extends to left corner
      Pane menuPane = (Pane) rootGridPane.getChildren().get(0);//to get the first child of the grid pane 0->pane for menubar
menuPane.getChildren().add(menuBar);
        Scene scene =new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.show();
    }
private MenuBar createMenu(){
        //file menu
    Menu fileMenu = new Menu("File");

    MenuItem newGame = new MenuItem("New Game");
    //
    newGame.setOnAction(event -> resetGame());

    MenuItem resetGame = new MenuItem("Reset Game");
    //
    resetGame.setOnAction(event -> resetGame());
    SeparatorMenuItem separatorMenuItem =new SeparatorMenuItem();
    //
    MenuItem exitGame = new MenuItem("Exit Game");
    exitGame.setOnAction(event -> exitgame());
    fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);
    // help menu
    Menu helpMenu = new Menu("Help");

    MenuItem aboutDeveloper = new MenuItem("About Developer");
    aboutDeveloper.setOnAction(event -> aboutDeveloper());
    MenuItem aboutGame = new MenuItem("About ConnecrFour Game");
    aboutGame.setOnAction(event -> aboutGame());
    SeparatorMenuItem separatorMenuIte = new SeparatorMenuItem();
    MenuItem tech= new MenuItem("Technical Support");
    tech.setOnAction(event -> tech());

    helpMenu.getItems().addAll(aboutDeveloper,aboutGame,separatorMenuIte,tech);
    //menu bar
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu,helpMenu);
    return menuBar ;


}

    private void tech() {
        Alert alert= new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("Technical assistant");
        alert.setHeaderText(" We are always to assist you 24/7");
        alert.setContentText("Please connect to ianilg68@gmail.com." +
                "  " +
                "The developer will guide you personally"
        +"THANKYOU");
        alert.show();
    }

    private void aboutGame() {
        Alert alert= new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("About Connect four App");
        alert.setHeaderText("How to play?");
  alert.setContentText("Connect Four (also known as Four Up, Plot Four, Find Four, Captain's Mistress, Four in a Row, Drop Four, and Gravitrips in the Soviet Union) is a two-player connection board game, in which the players choose a color and then take turns dropping colored discs into a seven-column, six-row vertically suspended grid." +
          " The pieces fall straight down, occupying the lowest available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. " +
          "The first player can always win by playing the right moves.");
  alert.show();
    }

    private void aboutDeveloper() {
        Alert alert= new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("About Developer");
        alert.setHeaderText("Anil Gupta");
        alert.setContentText("Love to learn new things .This keeps me updated and helps me to inprove my personality.");
        alert.show();
    }

    private void exitgame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        controller.resetgame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
