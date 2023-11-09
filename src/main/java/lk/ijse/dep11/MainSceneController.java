package lk.ijse.dep11;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MainSceneController {
    public AnchorPane root;
    public Button btnBrowse;
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
    public Label lblVolume;
    private String path;
    private MediaPlayer mediaPlayer;

    File file;

    public void initialize() {
        slrVolume.setValue(100.0);
        imgBackground.setDisable(false);
        btnBrowse.setTooltip(new Tooltip("Browse media files"));
        btnPlay.setTooltip(new Tooltip("Play media file"));
        btnPause.setTooltip(new Tooltip("Pause media file"));
        btnStop.setTooltip(new Tooltip("Stop media file"));
        btnSlow.setTooltip(new Tooltip("Play at 0.5x speed"));
        btnFast.setTooltip(new Tooltip("Play at 2.0x speed"));
        btnForward.setTooltip(new Tooltip("Skip 10 seconds forward"));
        btnBackward.setTooltip(new Tooltip("Skip 10 seconds backward"));
        slrSeek.setTooltip(new Tooltip("Seeker"));
        slrVolume.setTooltip(new Tooltip("Volume"));
    }
    public void btnChooseFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wave Files", "*.wav"));
        file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null) {
            path = file.toURI().toString();
            mvMyVideo.setVisible(true);
            imgBackground.setDisable(true);
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mvMyVideo.setMediaPlayer(mediaPlayer);
            mediaPlayer.setVolume(slrVolume.getValue() * 100);

            setVideoOnScreen();

        }else {
            path = null;
            imgBackground.setDisable(false);
        }

        root.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        root.setOnDragDropped(event -> {
            List<File> files = event.getDragboard().getFiles();
            if (!files.isEmpty()) {
                File file1 = files.get(0);
                path = file1.toURI().toString();
                mvMyVideo.setVisible(true);
                imgBackground.setDisable(true);
                Media media = new Media(path);
                mediaPlayer = new MediaPlayer(media);
                mvMyVideo.setMediaPlayer(mediaPlayer);
                mediaPlayer.setVolume(slrVolume.getValue() / 100);

                setVideoOnScreen();
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void btnPlayOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mvMyVideo.setVisible(true);
            imgBackground.setDisable(true);
            mediaPlayer.play();
            mediaPlayer.setRate(1.0);
        } else {
            new Alert(Alert.AlertType.WARNING, "Choose a file to play").show();
        }
    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        if (path != null){
            mediaPlayer.pause();
        }
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mediaPlayer.stop();
            mvMyVideo.setVisible(false);
            imgBackground.setDisable(false);
        }
    }

    public void btnSlowOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mediaPlayer.setRate(0.5);
        }
    }

    public void btnFastOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mediaPlayer.setRate(2.0);
        }
    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
        }
    }

    public void btnBackwardOnAction(ActionEvent actionEvent) {
        if (path != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
        }
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
        if (path != null) {
            mediaPlayer.setVolume(slrVolume.getValue() / 100);
            lblVolume.setText(String.format("%.0f",slrVolume.getValue()).concat("%"));
        }

    }

    public void slrVolumeOnMousePressed(MouseEvent mouseEvent) {
        if (path != null) {
            mediaPlayer.setVolume(slrVolume.getValue() / 100);
            lblVolume.setText(String.format("%.0f",slrVolume.getValue()).concat("%"));
        }
    }

    public void rootOnDragDropped(DragEvent dragEvent) throws FileNotFoundException {
        List<File> files = dragEvent.getDragboard().getFiles();
        if (!files.isEmpty()) {
            File file1 = files.get(0);
            path = file1.toURI().toString();
            mvMyVideo.setVisible(true);
            imgBackground.setDisable(true);
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mvMyVideo.setMediaPlayer(mediaPlayer);
            mediaPlayer.setVolume(slrVolume.getValue() / 100);

            setVideoOnScreen();
            btnPlay.fire();
        }
    }

    public void rootOnDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    private void setVideoOnScreen() {
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
    }

}
