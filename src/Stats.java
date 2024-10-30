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

public class Stats extends Application{
	
	public static IntegerProperty indicationleft = new SimpleIntegerProperty(null, "indicationleft", 3);
    public static IntegerProperty gamesplayed = new SimpleIntegerProperty(null, "gamesplayed", 0);
    public static IntegerProperty score = new SimpleIntegerProperty(null, "score", 0);
	public minesweeper ms;
	    public Stats(minesweeper ms) {
	    	this.ms=ms;
	    }
	    @Override
	    public void start(Stage primaryStage) {
	        Button btnBack = new Button("Back");
	        
	        Label lblgamesplayed = new Label("games played: "+ gamesplayed.get());
	        lblgamesplayed.textProperty().bind(Bindings.concat("games played: ",gamesplayed.asString()));
	        Label lblscore = new Label("your score: "+score.get());
	        lblscore.textProperty().bind(Bindings.concat("your score: ",score.asString()));
	        Button btnincrease = new Button("+");
	        Button btndecrease = new Button("-"); 
	        btnincrease.setOnAction(e -> {
	        	if(score.get()>5)
	        	{
	        		score.set(score.get()-10);
	        		indicationleft.set(indicationleft.get()+1);
	        	}
	        	else {
	        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    	    alert.setTitle("Not enough score");
	    	    alert.setHeaderText(null);
	    	    alert.setContentText("You don't have enough score to trade.");
	    	    alert.showAndWait();
	        	}
	        });
	        btndecrease.setOnAction(e -> {
	        	score.set(score.get()+5);
	            indicationleft.set(indicationleft.get()-1);
	        });
	        Label lblindication = new Label(""+indicationleft.get());
	        lblindication.textProperty().bind(indicationleft.asString());
	        HBox hb = new HBox(10,btndecrease,lblindication,btnincrease);
	        HBox.setMargin(btnincrease, new Insets(0,0,0,25));
	        HBox.setMargin(lblindication, new Insets(0,0,0,30));
	        btnBack.setOnAction(e -> {
		        primaryStage.setScene(ms.getMenuScene()); 
		    });
	        GridPane gp = new GridPane();
	        gp.setAlignment(Pos.CENTER);
	        gp.setVgap(25);
	        gp.add(lblgamesplayed, 0, 0);
	        gp.add(lblscore, 0, 1);
	        gp.add(hb, 0, 2);
	        VBox vbox = new VBox(20, btnBack, gp);
	        VBox.setMargin(btnBack, new Insets(0,0,-40,0));
	        vbox.setPadding(new javafx.geometry.Insets(15));
	        VBox.setMargin(gp, new Insets(100,0,0,0));
	        Scene scene = new Scene(vbox, 600, 450);
	        primaryStage.setScene(scene);
	        scene.getStylesheets().add("stylestats.css");
	        primaryStage.setTitle("Stats");
	        primaryStage.show();
	    }
	}

