package com.willhero.willhero;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private ImageView cloud1, cloud2, cloud3, cloud4, cloud5, cloud6;
    @FXML private ImageView Orc1, RedOrc1, RedOrc2;
    @FXML private ImageView hero;
    @FXML private ImageView tnt;
    @FXML private ImageView quit;
    @FXML private AnchorPane scenePane;
    @FXML private StackPane parentContainer;
    public static Stage ComStage = new Stage();
//    @FXML private Button OK;

    public static AudioClip note;
    private void loadSavedGames(){
        for(int i= 0;i < Home.fileslist.length; i++){
            String str = Home.fileslist[i].toString().substring(87);
            Home.filesArr.add(str);
        }
    }
    private void clouds(ImageView cloud, int toX, int pause) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(cloud);
        translate.setDuration((Duration.millis(10000)));
        translate.setCycleCount((TranslateTransition.INDEFINITE));
        translate.setToX(toX);
        SequentialTransition seqTransition = new SequentialTransition (new PauseTransition(Duration.millis(pause)),translate);
        seqTransition.play();
    }

    protected void genCloud(int toX, int[] pause, ImageView[] clouds) {
        for (int i = 0; i < pause.length; i++) {
            clouds(clouds[i],toX,pause[i]);
        }
    }

    protected SequentialTransition jump(ImageView img, int transTime, int toY, int pause) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(img);
        translate.setDuration(Duration.millis(transTime));
        translate.setCycleCount((TranslateTransition.INDEFINITE));
        translate.setByY(toY);
        translate.setAutoReverse(true);
        SequentialTransition seqTransition = new SequentialTransition (new PauseTransition(Duration.millis(pause)),translate);
        seqTransition.play();
        return seqTransition;
    }

    protected void toStop(TranslateTransition transition, boolean stop) {
        if (stop) {
            transition.stop();
        } else {
            transition.play();
        }
    }

    protected void tntTrans(ImageView img) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(img);
        scale.setDuration(Duration.millis(650));
        scale.setCycleCount(TranslateTransition.INDEFINITE);
        scale.setByX(0.12);
        scale.setByY(0.12);
        scale.setAutoReverse(true);
        scale.play();
    }
    @FXML
    private void SaveMenu(MouseEvent e) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SavedGames.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage newStage = new Stage();
//        HomeController.ComStage.initModality(Modality.WINDOW_MODAL);
//        HomeController.ComStage.initOwner(scenePane.getScene().getWindow());
        HomeController.ComStage.setTitle("Player Data");
        HomeController.ComStage.setScene(scene);
        HomeController.ComStage.show();
        HomeController.note.stop();

    }
    @FXML
    private void gameScreen(MouseEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameRecords.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage newStage = new Stage();
//        HomeController.ComStage.initModality(Modality.WINDOW_MODAL);
//        HomeController.ComStage.initOwner(scenePane.getScene().getWindow());
        HomeController.ComStage.setTitle("Player Data");
        HomeController.ComStage.setScene(scene);
        HomeController.ComStage.show();
        HomeController.note.stop();

    }
    @FXML
    private void OpenGameScreen(MouseEvent e) throws IOException {
        HomeController.note.stop();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gamescreen.fxml")));
        sceneSwitch(root,parentContainer,scenePane, 1200, 750);
    }


    protected void sceneSwitch(Parent sceneDir, StackPane stackPane, AnchorPane scenePanePara, int wid, int hei) throws IOException {
        Stage StW = (Stage)scenePanePara.getScene().getWindow();
        StW.setWidth(wid);
        StW.setHeight(hei);
        sceneDir.translateXProperty().set(scenePanePara.getWidth());
        stackPane.getChildren().add(sceneDir);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(sceneDir.translateXProperty(), 0, Interpolator.TANGENT(Duration.millis(500), 20, Duration.millis(500), 20));
        KeyFrame kf = new KeyFrame(Duration.seconds(2), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> stackPane.getChildren().remove(scenePanePara));
        timeline.play();
    }

    @FXML
    private void exitGame(Event e) {
        exitFunc((Stage) scenePane.getScene().getWindow());
    }

    protected void exitFunc(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Do you want to quit?");
        alert.setHeaderText("You're exiting the Game!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //clouds

        play_audio();
        loadSavedGames();
        genCloud(-2200,new int[] {0,0,1000,1000,3000,3000}, new ImageView[] {cloud1,cloud2,cloud3,cloud4,cloud5,cloud6});
        //hero
        jump(hero,850,-130,0);

        //orcs
        int pause = 500;
        jump(Orc1,1000,-130,pause);
        jump(RedOrc1,950,-90,pause);
        jump(RedOrc2,850,-60,pause);
        //tnt
        tntTrans(tnt);
    }

    private void play_audio(){
        HomeController.note = new AudioClip(Objects.requireNonNull(this.getClass().getResource("Arcade.mp3")).toString());
        HomeController.note.setCycleCount(AudioClip.INDEFINITE);
        HomeController.note.setVolume(0.09);
        HomeController.note.play();
    }
}