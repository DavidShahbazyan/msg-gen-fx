import arm.davsoft.msgman.components.ProcessIndicator;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 1/30/16 <br/>
 * <b>Time:</b> 11:23 AM <br/>
 */
public class TimelineTest extends Application {
    Timeline timeline = new Timeline();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(0, new Image("images/appLogo_dark.png"));
        ProcessIndicator processIndicator1 = new ProcessIndicator("images/icons/process/big/step_1@2x.png", true);
        ProcessIndicator processIndicator2 = new ProcessIndicator("images/icons/process/fs/step_1@2x.png", true);
        primaryStage.setScene(new Scene(new VBox(processIndicator1, processIndicator2)));
        primaryStage.show();
    }

    public static void main(String[] args) {
            launch();
        }
}
