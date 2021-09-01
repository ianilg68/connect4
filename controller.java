package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int circleDiameter = 80;
	private static final String disccolor1 ="#24303E";
	private static final String disccolor2 ="#4CAA88";

	private static String playerOne ="PLAYER ONE";
	private static String playerTwo ="PLAYER Two";
	private boolean isPlayerOneTurn=true;

	private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS]; //for structural changes for developers


	@FXML
	public GridPane rootGridPane ;
	@FXML
	public Pane inserteddiskpane ;
	@FXML
	public Label playernamelabel ;
	@FXML
	public TextField name1;
	@FXML
	public TextField name2;
	@FXML
	public Button b1;

	private boolean isAllowedToInsert = true;//to avoid the user to add multiple disc at the same time by fast multiple clicking

	public void createPlayground(){
		Shape RectanglewithHoles= createGameStructuralGrid();

	rootGridPane.add(RectanglewithHoles,0,1);
	List<Rectangle> rectangleList = createClickablecolumn();
		for (Rectangle rectangle :rectangleList) {
			rootGridPane.add(rectangle,0,1);
		}

	}
	private Shape createGameStructuralGrid(){
		Shape RectanglewithHoles = new Rectangle((COLUMNS+1)*circleDiameter,(ROWS+1)*circleDiameter);
		// we have done +1 in the colulmns and rows because to add some space in the margins corners
		//burt this will only add the dpace in the botton and right corner
		for (int row = 0; row <ROWS ; row++) {
			for (int column = 0; column <COLUMNS ; column++) {

				Circle circle= new Circle();
				circle.setRadius(circleDiameter/2);
				circle.setCenterX(circleDiameter/2);
				circle.setCenterY(circleDiameter/2);
				circle.setSmooth(true);//to smooth the edges

				circle.setTranslateX(column*(circleDiameter+5)+circleDiameter/4);//+5 to get some space between each of the holes
				circle.setTranslateY(row*(circleDiameter+5)+circleDiameter/4);//diameter /4 couse to get some space iin the top and left corner
				RectanglewithHoles =Shape.subtract(RectanglewithHoles,circle);
			}

		}

		RectanglewithHoles.setFill(Color.WHITE);
	return RectanglewithHoles;
	}
	private List<Rectangle> createClickablecolumn(){
		List<Rectangle> rectangleList = new ArrayList<>();
		for (int col = 0; col <COLUMNS ; col++) {


			Rectangle rectangle = new Rectangle(circleDiameter, (ROWS + 1) * circleDiameter);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX((col*(circleDiameter+5))+circleDiameter / 4);
			//hover effect for each of the rectangle means every column will highlighted with a rectangle
			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee27")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			final int column=col;
			rectangle.setOnMouseClicked(event -> {
				if(isAllowedToInsert) {
               isAllowedToInsert=false;
					insertDisc(new Disc(isPlayerOneTurn), column);
				}
				});
	rectangleList.add(rectangle);
		}
	return rectangleList;
	}

	private  void insertDisc(Disc disc,int column){
		int row =ROWS-1;
		while (row>=0)
		{
			if (getdiscIfpresent(row,column) ==null)
				break;

			row--;
		}
		if (row<0) // when the row is full the player cont be able to insert the disc any further
			return;

		insertedDiscArray[row][column] = disc;//for structural changes
		inserteddiskpane.getChildren().add(disc);//for visual changes for player to insert the disc virtually
		disc.setTranslateX((column*(circleDiameter+5))+circleDiameter / 4);
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);

		translateTransition.setToY((row*(circleDiameter+5))+circleDiameter / 4);
		int currentrow = row;

		translateTransition.setOnFinished(event -> {
isAllowedToInsert = true;
			if (gameEnded(currentrow,column)) //this will dicide the game winner
			{
				gameover();
				return;
			}
			isPlayerOneTurn=!isPlayerOneTurn; //this will change the turn from player one to player two
			// this will also change the color of both disc

			 String nameone = name1.getText();
			 String nametwo = name2.getText();
			playernamelabel.setText(isPlayerOneTurn? nameone:nametwo);
		});
		translateTransition.play();


	}



	private boolean gameEnded(int currentrow, int column) {
		//find the vertical points to find the winning combination
	List<Point2D> verticalpoints = IntStream.rangeClosed(currentrow-3,currentrow+3)//this will help us to get the range values
			.mapToObj(r->new Point2D(r,column))// this will help us to get the list of point 2d objects where the disc may be present
			.collect(Collectors.toList());// here we are converting those points to 2d object to store

		//find the horizontal points to find the winning combination
		List<Point2D> horizontalpoints = IntStream.rangeClosed(column-3,column+3)//this will help us to get the range values
				.mapToObj(c->new Point2D(currentrow,c))// this will help us to get the list of point 2d objects where the disc may be present
				.collect(Collectors.toList());// here we are converting those points to 2d object to store

		//find the  first diagonal points to find the winner combination
		Point2D startpoint1 =new Point2D(currentrow-3,column+3);
		List<Point2D> diagonal1points =IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint1.add(i,-i))
				.collect(Collectors.toList());

		//find the  second diagonal points to find the winner combination
		Point2D startpoint2 =new Point2D(currentrow-3,column-3);
		List<Point2D> diagonal2points =IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint2.add(i,i))
				.collect(Collectors.toList());


		boolean isEnded= checkCombination(verticalpoints) || checkCombination(horizontalpoints)
				|| checkCombination(diagonal1points) ||checkCombination(diagonal2points);


		return  isEnded;
	}

	private boolean checkCombination(List<Point2D> points) {
		int chain = 0;
		for (Point2D point : points) {

			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();
			//to check if the disc is present at the perticular pooint or not
			Disc disc = getdiscIfpresent(rowIndexForArray,columnIndexForArray);
			if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {

				chain++;
				if (chain == 4) {
					return true;//when we got a combination of four
				}
			}else {
					chain = 0;

			}
		}
		return false;//if we didnt got any combination of four

	}
	private Disc getdiscIfpresent(int row,int column){//to prevent ArrayIndexOutOfBoundException

		if(row>=ROWS||row<0||column>=COLUMNS||column<0) //if row or column index is invalid
		{
			return null;
		}
		return insertedDiscArray[row][column];

	}
	private void gameover() {
		String nameone = name1.getText();
		String nametwo = name2.getText();
String winner = isPlayerOneTurn?nameone:nametwo;
		System.out.println("Winner is "+winner);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect four");
		alert.setHeaderText("THE WINNER IS: "+winner);
		alert.setContentText("Want to play again?");

		//costomise alert dialog
		ButtonType yesbutton = new ButtonType("Yes");
		ButtonType nobutton = new ButtonType("No,Exit");
		alert.getButtonTypes().setAll(yesbutton,nobutton);
	//to add the functionality to the dialog buttons use optional container

		Platform.runLater(()->{
			Optional<ButtonType> buttonclicked= alert.showAndWait();
			if (buttonclicked.isPresent() && buttonclicked.get()==yesbutton){
				//reset the game
				resetgame();
			}
			else {
				//exit the game
				Platform.exit();
				System.exit(0);

			}
		});


	}

	public void resetgame() {
		inserteddiskpane.getChildren().clear();//remove all the disc inserted in the pane virtually
		//to clear the pane structurally
		for (int row = 0; row < insertedDiscArray.length ; row++) {
			for (int col = 0; col < insertedDiscArray[row].length; col++) {
				insertedDiscArray[row][col]=null;
				name1.setText("Player 1");
				name2.setText("Player 2");
			}

		}
		isPlayerOneTurn = true;//let player start the new game
		playernamelabel.setText(playerOne);
		createPlayground();//prepare the fresh playground
	}

	//class to insert disc
	private static class Disc extends Circle
	{
		private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove){
			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(circleDiameter/2);
			setCenterX(circleDiameter/2);
			setCenterY(circleDiameter/2);

			setFill(isPlayerOneMove?Color.valueOf(disccolor1):Color.valueOf(disccolor2));

		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
b1.setOnAction(event -> {
	String s = name1.getText();
	  name1.setText(s);
	String v = name2.getText();
	 name2.setText(v);
	playernamelabel.setText(s);
});
	}
}
