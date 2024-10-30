import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class DifficultySelection extends Application{
    public minesweeper ms ;
    public DifficultySelection(minesweeper ms) {
    	this.ms=ms;
    }
	@Override
	public void start(Stage primaryStage)  {
		
		VBox vbDiff = new VBox();
		
	    vbDiff.setSpacing(80);
	    Button btnBack = new Button("Back");
	    btnBack.setStyle("-fx-font-size: 15px;");
        
        
	    VBox vbDiff1 = new VBox();
	    Label choose = new Label("Choose your difficulty");
	    Button btnEasy = new Button("Easy");
	    Button btnMedium = new Button("Medium");
	    Button btnHard = new Button("Hard");
	    
	    choose.setStyle("-fx-font-size: 25px;");
	    btnEasy.setStyle("-fx-font-size: 20px;");
	    btnMedium.setStyle("-fx-font-size: 20px;");
	    btnHard.setStyle("-fx-font-size: 20px;");
	    vbDiff1.setAlignment(Pos.CENTER);
	    
	    vbDiff.getChildren().addAll(btnBack, vbDiff1);
	    
	    vbDiff1.getChildren().addAll(choose, btnEasy, btnMedium, btnHard);

	    VBox.setMargin(choose, new Insets(-40, 0, 10, 0));
	    VBox.setMargin(btnEasy, new Insets(30, 0, 10, 0));
	    VBox.setMargin(btnMedium, new Insets(30, 0, 10, 0));
	    VBox.setMargin(btnHard, new Insets(30, 0, 10, 0));
	    Scene difficultyScene = new Scene(vbDiff, 600, 450);
	    primaryStage.setScene(difficultyScene);
	    difficultyScene.getStylesheets().add("style.css");

	    btnBack.setOnAction(e -> {
	        primaryStage.setScene(ms.getMenuScene()); 
	    });
	    btnEasy.setOnAction(e -> {
	        ms.showeasymode(); 
	    });
	    btnMedium.setOnAction(e -> {
	        ms.showmediummode(); 
	    });
	    btnHard.setOnAction(e -> {
	        ms.showhardmode(); 
	    });
	}
	}
