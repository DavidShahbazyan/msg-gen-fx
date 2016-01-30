package arm.davsoft.msgman.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 1/28/16 <br/>
 * <b>Time:</b> 4:55 PM <br/>
 */
public class ProcessIndicator extends ImageView {
    private final Timeline _tl = new Timeline();

    public ProcessIndicator(String url, boolean autoPlay) {
        this(new Image(url), autoPlay);
    }

    public ProcessIndicator(Image image, boolean autoPlay) {
        super(image);
        init();
        if (autoPlay) {
            play();
        }
    }

    private void init() {
        _tl.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), new KeyValue(rotateProperty(), 0.0)),
                new KeyFrame(new Duration(1000), new KeyValue(rotateProperty(), 360.0))
        );
        _tl.setCycleCount(Animation.INDEFINITE);
    }

    public void play() {
        _tl.play();
    }

    public void stop() {
        _tl.stop();
    }
}
