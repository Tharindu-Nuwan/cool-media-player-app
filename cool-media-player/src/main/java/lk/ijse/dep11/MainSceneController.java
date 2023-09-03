package lk.ijse.dep11;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MainSceneController {
    public AnchorPane root;
    public Button btnChooseFile;
    public Button btnPlay;
    public Button btnPause;
    public Button btnStop;
    public MediaView mvMyVideo;
    public Button btnSlow;
    public Button btnFast;
    public Button btnBackward;
    public Button btnForward;
    public Slider slrSeek;
    public Slider slrVolume;
    public ImageView imgBackground;
    public Label lblTitle;

    private String path;
    private MediaPlayer mediaPlayer;

    public void initialize() {
        slrVolume.setValue(50.0);
        lblTitle.setVisible(true);
        imgBackground.setDisable(false);
    }
    public void btnChooseFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wave Files", "*.wav"));
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null) {
            path = file.toURI().toString();
            mvMyVideo.setVisible(true);
            lblTitle.setVisible(false);
            imgBackground.setDisable(true);
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mvMyVideo.setMediaPlayer(mediaPlayer);
            DoubleProperty widthProperty = mvMyVideo.fitWidthProperty();
            DoubleProperty heightProperty = mvMyVideo.fitHeightProperty();
            widthProperty.bind(Bindings.selectDouble(mvMyVideo.sceneProperty(), "width"));
            heightProperty.bind(Bindings.selectDouble(mvMyVideo.sceneProperty(), "height"));

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    slrSeek.setValue(t1.toSeconds());
                }
            });

            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration totalDuration = mediaPlayer.getTotalDuration();
                    slrSeek.setMax(totalDuration.toSeconds());
                }
            });
        }else {
            path = null;
            lblTitle.setVisible(true);
            imgBackground.setDisable(false);
        }
    }

    public void btnPlayOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mvMyVideo.setVisible(true);
            lblTitle.setVisible(false);
            imgBackground.setDisable(true);
            mediaPlayer.play();
            mediaPlayer.setRate(1.0);
        } else {
            new Alert(Alert.AlertType.WARNING, "Choose a file to play").show();
        }
    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        mediaPlayer.stop();
        mvMyVideo.setVisible(false);
        lblTitle.setVisible(true);
        imgBackground.setDisable(false);
    }

    public void btnSlowOnAction(ActionEvent actionEvent) {
        mediaPlayer.setRate(0.5);
    }

    public void btnFastOnAction(ActionEvent actionEvent) {
        mediaPlayer.setRate(2.0);
    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    public void btnBackwardOnAction(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }

    public void slrSeekOnMousePressed(MouseEvent mouseEvent) {
        if (path != null) {
            mediaPlayer.seek(Duration.seconds(slrSeek.getValue()));
        }else {
            slrSeek.setValue(0);
        }
    }

    public void slrSeekOnMouseDragged(MouseEvent mouseEvent) {

        if (path != null) {
            mediaPlayer.seek(Duration.seconds(slrSeek.getValue()));
        }else {
            slrSeek.setValue(0);
        }
    }

    public void slrVolumeOnMouseDragged(MouseEvent mouseEvent) {
        mediaPlayer.setVolume(slrVolume.getValue() / 100);
    }

    public void slrVolumeOnMousePressed(MouseEvent mouseEvent) {
        mediaPlayer.setVolume(slrVolume.getValue() / 100);
    }
}
