
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class Settings extends Application {
	public static String selection = null;
	public static MediaPlayer mp;
	public minesweeper ms;
    public Settings(minesweeper ms) {
    	this.ms=ms;
    }
    @Override
    public void start(Stage primaryStage) {
    	
        VBox root = new VBox(105);
        root.setPadding(new Insets(15));
        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> {
	        primaryStage.setScene(ms.getMenuScene()); 
	    });
        HBox hb = new HBox(5);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().add(btnBack);
        Label labelvolume = new Label("Volume");
        Slider slidervolume = new Slider(0,1,0.5);
        slidervolume.getStyleClass().add("slidervolume");
        HBox volumeBox = new HBox(5);
        volumeBox.setAlignment(Pos.CENTER);//getClass().getResource("clairedelune.mp3").toString()
        volumeBox.getChildren().addAll(labelvolume, slidervolume);
        
        ComboBox<String> BGM = new ComboBox<>();
        BGM.getItems().addAll("relaxed","intense","classic");
        BGM.setPrefSize(140, 30);
        
        if(mp != null) {
        	slidervolume.valueProperty().bindBidirectional(mp.volumeProperty());
    	}
        if(selection != null)
        {
        	BGM.setValue(selection);
        }
        BGM.setOnAction(e -> {
        	
        	if(mp != null) {
        		mp.stop();
        		mp.dispose();
        		mp=null;
            }
            String selectedstyle = BGM.getSelectionModel().getSelectedItem();
            String path = null;
            switch (selectedstyle) {
                case "relaxed":
                    path = "src/bistrofada.mp3";
                    break;
                case "intense":
                	path = "src/mombasa.mp3";
                    break;
                case "classic":
                	path = "src/clairedelune.mp3";
                    break;
            }
            String selected = BGM.getSelectionModel().getSelectedItem();
            selection = selected;
            if(path != null) {
                Media media = new Media(new File(path).toURI().toString());
                mp = new MediaPlayer(media);
                mp.play(); 
                slidervolume.valueProperty().bindBidirectional(mp.volumeProperty());
                }
        });
        
        HBox bgm = new HBox();
        Label lblbgm = new Label("Select BGM type :");
        bgm.getChildren().addAll(lblbgm,BGM);
        HBox.setMargin(lblbgm, new Insets(0,0,0,100));
        HBox.setMargin(BGM, new Insets(0,0,0,20));
        root.getChildren().addAll(hb, volumeBox,bgm);
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Settings");
        primaryStage.show();
        scene.getStylesheets().add("stylesettings.css");
    }
}