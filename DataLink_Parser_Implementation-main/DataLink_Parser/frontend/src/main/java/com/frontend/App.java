package com.frontend;


import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private VBox vbox;

    @Override
    public void start(Stage primaryStage) {

        vbox = new VBox(10); // 10: Differentiates the distance between labels.
        vbox.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
        Label initialLabel = new Label("Initial Label"); // Eğer isterseniz başlangıçta bir etiket ekleyebilirsiniz.
        vbox.getChildren().add(initialLabel);
        Scene scene = new Scene(vbox, 600, 600);

        primaryStage.setTitle("GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start a new thread to read input and update the GUI
        
    }

    public void updateLabels(TrackList trackList) {
        // Update the GUI on the JavaFX Application Thread using Platform.runLater
        Platform.runLater(() -> {
            for(int i=0;i<trackList.getSize();i++){
                Track track= trackList.getList().get(i);
                Label trackLabel = new Label();
                trackLabel.setText("Track ID: " + track.get_trackID() +
                               "\nTrack Heading: " + track.get_trackHeading() +
                               "\nTrack Speed: " + track.get_trackSpeed() +
                               "\nTrack Latitude: " + track.get_trackLatitude() +
                               "\nTrack Longitude: " + track.get_trackLongitude());
            vbox.getChildren().addAll(trackLabel);
            }
        });
    }

   

    
}