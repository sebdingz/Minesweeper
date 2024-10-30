import java.time.Duration;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EasyMinesweeper extends Application {
	public Stage primaryStage;
	public int mines=3;
	public IntegerProperty minesleft = new SimpleIntegerProperty(this, "minesleft", mines);
	public minesweeper ms;
	public int rows=8;
	public int cols=8;
	public int score = 0;
    public int timeRemaining = 30;
    
    public Label timeLabel;
    public Label scoreLabel;
    public Label difficultyLabel;
    Label indicationLabel = new Label(" : "+Stats.indicationleft.get());
    public Label minesLabel = new Label("mines left : "+ minesleft.get());
    public Image indicationimage = new Image(getClass().getResourceAsStream("questionmark.jpg"));
    
    public Button surrender;
    public Button[][] gridButtons = new Button[8][8];
    public Label[][] gridLabels = new Label[8][8];
    public boolean boolsurrender = true;
    public boolean[][] visitedCells;
    public boolean boolwin=false;
    public boolean boollost=false;
    boolean[][] bombs = new boolean[rows][cols];
    private final int[][] offset = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
    public EasyMinesweeper(minesweeper ms) {
    	this.ms=ms;
    }
	@Override
	public void start(Stage primaryStage){
		
		GridPane grid = new GridPane();
		visitedCells = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	visitedCells[i][j] = false;
            	final int row = i; 
            	final int col = j; 
            	StackPane stack = new StackPane();
            	gridLabels[i][j] = new Label(" "); 
                gridButtons[i][j] = new Button(" ");
                gridButtons[i][j].getStyleClass().add("grid-button");
                gridButtons[i][j].setFocusTraversable(false);  
                gridButtons[i][j].setMinSize(50, 50);
                gridButtons[i][j].setUserData(new int[]{i, j});
                gridButtons[i][j].setOnDragOver(event -> {
                	
                    if (event.getDragboard().hasString() && event.getDragboard().getString().equals("indicator")) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                    
                });
                gridButtons[i][j].setOnDragDropped(event -> {
                	///System.out.println(indication);
                	
                	if(Stats.indicationleft.get()>0) {
                        if("💣".equals(gridLabels[row][col].getText()))
                        {
                        	gridButtons[row][col].setText("🚩");
                        	minesleft.set(minesleft.get() - 1);
                        	if(minesleft.get()==0)
                        	{
                        		win();
                        	}
                        }
                        else {
                        	gridButtons[row][col].fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                        }
                        Stats.indicationleft.set(Stats.indicationleft.get()-1);
                        indicationLabel.setText(" : "+ Stats.indicationleft.get());
                	}
                	else {
                		Alert alert = new Alert(AlertType.INFORMATION);
                	    alert.setTitle("No indications left");
                	    alert.setHeaderText(null);
                	    alert.setContentText("Your indication count has run out!");
                	    alert.showAndWait();
                	}
                   
                });
                gridButtons[i][j].setOnMouseClicked(event -> {
                	Button clickedButton = (Button) event.getSource();
        		    minesleft.addListener((observable, oldValue, newValue) -> {
        		        minesLabel.setText("mines left : " + newValue);
        		    });
                  	if(event.getButton()==MouseButton.PRIMARY) {
                    open(clickedButton);
                    if(boollost)
                    {
                    	primaryStage.setScene(ms.getMenuScene());
                    	Stats.score.set(Stats.score.get()-20);
                    }
                	}
                	else if(event.getButton()==MouseButton.SECONDARY)
                	{	
                		    String text=clickedButton.getText();
                		    
                		    if("🚩".equals(text)) {
                	            clickedButton.setText("");
                	            minesleft.set(minesleft.get() + 1);
                	        }
                	        else if(minesleft.get() > 0) {
                	            clickedButton.setText("🚩");
                	            minesleft.set(minesleft.get() - 1);
                	        }
                            win();
                            if(boolwin&&timeRemaining>0) {
                            	primaryStage.setScene(ms.getMenuScene());
                            	Stats.score.set(Stats.score.get()+40);
                            }
                            else if (boolwin) {
                            	primaryStage.setScene(ms.getMenuScene());
                            	Stats.score.set(Stats.score.get()+30);
                            }
                	}
                });
                stack.getChildren().addAll(gridLabels[i][j], gridButtons[i][j]);
                gridButtons[i][j].getStyleClass().add("minebutton");
                gridLabels[i][j].getStyleClass().add("minelabel");
                grid.add(stack, i, j);

            }
        }
        generatebombs();
        timeLabel=new Label("Time :" + timeRemaining+" s");
        scoreLabel = new Label("Score: "+ Stats.score.get());
        scoreLabel.textProperty().bind(Bindings.concat("Score: ",Stats.score.asString()));
        difficultyLabel= new Label("Difficulty : easy");
        ImageView indicationiv = new ImageView(indicationimage);
        indicationiv.setFitHeight(50);
        indicationiv.setFitWidth(50);
        
        surrender = new Button("surrender");
        timeLabel.setStyle("-fx-font-size: 20px;");
        timeLabel.getStyleClass().add("timeLabel");
        scoreLabel.setStyle("-fx-font-size: 20px;");
        difficultyLabel.setStyle("-fx-font-size: 15px;");
        minesLabel.setStyle("-fx-font-size: 20px;");     
        indicationiv.setOnDragDetected(event -> {
        	///System.out.println("drag");
            Dragboard db = indicationiv.startDragAndDrop(TransferMode.ANY);
            ClipboardContent cc = new ClipboardContent();
            cc.putString("indicator");
            db.setContent(cc);
            event.consume();
        });
        
        HBox indicationbox = new HBox(10);
        indicationbox.getChildren().addAll(indicationiv,indicationLabel);
        surrender.setStyle("-fx-font-size: 20px;");
        surrender.getStyleClass().add("surrender");
        VBox infoBox = new VBox(20);
        infoBox.getChildren().addAll(difficultyLabel,timeLabel,scoreLabel,minesLabel,indicationbox,surrender);
        HBox root = new HBox(20);
        root.getChildren().addAll(grid,infoBox);
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("styleeasyminesweeper.css");
        primaryStage.setScene(scene);
        HBox.setMargin(infoBox, new Insets(0, 10, 0, 10));
        VBox.setMargin(difficultyLabel, new Insets(5,-20,20,0));
        VBox.setMargin(indicationiv, new Insets(0,0,0,40));
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(1),
                e -> updateTimer()
            );
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(keyFrame);
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
        surrender.setOnAction(e -> {
        	Stats.score.set(Stats.score.get()-10);
        	boolsurrender = false;
        	VBox vbsurrender = new VBox(20);
            Stage surrenderStage = new Stage();
            surrenderStage.initModality(Modality.APPLICATION_MODAL);
            surrenderStage.initOwner(primaryStage); 
            Label surrendertext = new Label("You surrender!");
            Button btnsurrenderback = new Button("Back");
            Label surrenderscore = new Label("Your score : " + Stats.score.get());
            surrenderscore.textProperty().bind(Bindings.concat("your score: ",Stats.score.asString()));
            surrendertext.setStyle("-fx-font-size: 20px;");
            btnsurrenderback.setStyle("-fx-font-size: 20px;");
            surrenderscore.setStyle("-fx-font-size: 20px;");
            vbsurrender.getChildren().addAll(surrendertext,btnsurrenderback,surrenderscore);
            
            btnsurrenderback.setOnAction(event -> {
            	surrenderStage.close();      	
            	primaryStage.setScene(ms.getMenuScene());
            });
            vbsurrender.setAlignment(Pos.CENTER);
            Scene surrenderScene = new Scene(vbsurrender,300,200);
            surrenderStage.setScene(surrenderScene);
            surrenderScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            surrenderStage.setX(primaryStage.getX() + (primaryStage.getWidth() - 300) / 2);
            surrenderStage.setY(primaryStage.getY() + (primaryStage.getHeight() - 200) / 2);
            surrenderStage.show();
        });
	}

	private void open(Button btn) {
	    int[] position = (int[]) btn.getUserData();
	    int r = position[0];
	    int c = position[1];
	    if ("💣".equals(gridLabels[r][c].getText())) {
	        GameOver();
	        boollost=true;
	        bombclicked();
	        return; 
	    }
	    if (visitedCells[r][c]) {
	        return;
	    }
	    visitedCells[r][c] = true;
	    btn.setVisible(false);
	    int bombCount = countBombsAround(r, c);
	    if (bombCount == 0) {
	        for (int[] off: offset) {
	            int newRow = r + off[0];
	            int newCol = c + off[1];
	            if (newRow >= 0 && newCol >= 0 && newRow < rows && newCol < cols) {
	                Button sButton = gridButtons[newRow][newCol];
	                if (sButton.isVisible()) {
	                    open(sButton); 
	                }
	            }
	        }
	    } else {
	        gridLabels[r][c].setText(String.valueOf(bombCount));
	        switch (bombCount) {
	        case 1:
                gridLabels[r][c].setTextFill(Color.BLUE);
                break;
            case 2:
                gridLabels[r][c].setTextFill(Color.GREEN);
                break;
            case 3:
                gridLabels[r][c].setTextFill(Color.RED);
                break;
	        }
	    }
	}
	public int countBombsAround(int r, int c) {
	    int count = 0;
	    for (int[] off: offset) {
	        int newRow = r + off[0];
	        int newCol = c + off[1];
	        if (newRow >= 0 && newCol >= 0 && newRow < rows && newCol < cols) {
	            if ("💣".equals(gridLabels[newRow][newCol].getText())) {
	                count++;
	            }
	        }
	    }
	    return count;
	}
	public void GameOver() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Game Over");
	    alert.setHeaderText(null);
	    alert.setContentText("You Lost! Clicked on a bomb.");
	    Optional<ButtonType> result = alert.showAndWait();
	    if(result.isPresent() && result.get()==ButtonType.OK) {
	    	Stats.gamesplayed.set(Stats.gamesplayed.get()+1);
	    }
	}
	public void bombclicked() {
	    new Thread(() -> {
	        for (int i = 0; i < rows; i++) {
	            for (int j = 0; j < cols; j++) {
	                if ("💣".equals(gridLabels[i][j].getText())) {
	                    try {
	                        Thread.sleep(200);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                    final int finalI = i;
	                    final int finalJ = j;
	                    Platform.runLater(() -> {
	                        gridButtons[finalI][finalJ].setVisible(false);
	                    });
	                }
	            }
	        }
	    }).start();
	}
	public void Flag(Button btn) {
		String text=btn.getText();
		if("".equals(text)) {
			btn.setText("🚩");
		}
		else {
			btn.setText("");
		}
	}
	public void win() {

		    for (int i = 0; i < rows; i++) {
		        for (int j = 0; j < cols; j++) {
		            if (bombs[i][j] && !"🚩".equals(gridButtons[i][j].getText())) {
		                return;
		            }
		            if (!bombs[i][j] && "🚩".equals(gridButtons[i][j].getText())) {
		                return;
		            }
		        }
		    }
		    boolwin=true;
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("Winning !");
		    alert.setHeaderText(null);
		    alert.setContentText("You Win!😊");
		    Optional<ButtonType> result = alert.showAndWait();
		    if(result.isPresent() && result.get()==ButtonType.OK) {
		    	Stats.gamesplayed.set(Stats.gamesplayed.get()+1);
		    	
		    }
		    
		    
	}
	public void updateTimer() {
		if (timeRemaining == 0) {
			boolsurrender=false;
			
        }
		if(boolsurrender&&!boolwin) {
	        timeRemaining--;
	        timeLabel.setText("Time: " + timeRemaining + "s");
		}
	}
	public void generatebombs() {
	    int numbombs = 0;
	    while (numbombs < mines) {
	        int randomRow = (int) (Math.random() * rows);
	        int randomCol = (int) (Math.random() * cols);
	        if (!bombs[randomRow][randomCol]) {
	            gridLabels[randomRow][randomCol].setText("💣");
	            bombs[randomRow][randomCol] = true;
	            numbombs++;
	        }
	    }
	}
}
