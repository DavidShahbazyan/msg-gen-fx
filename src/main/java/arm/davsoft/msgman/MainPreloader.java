package arm.davsoft.msgman;

import arm.davsoft.msgman.utils.ResourceManager;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/25/15 <br/>
 * <b>Time:</b> 12:25 AM <br/>
 */
public class MainPreloader extends Preloader {
    ProgressBar bar;
    Stage stage;
    Label applicationName;

    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        bar.setPrefHeight(3);
        bar.getStyleClass().add("white");

        Reflection reflection = new Reflection();
        reflection.setTopOpacity(0.3);

//        applicationName = new Label(ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.VERSION"));
//        applicationName.setStyle("-fx-font-size: 25px");
//        applicationName.setEffect(reflection);

//        ImageView splash = new ImageView("/images/splashScreen.png");
        ImageView splash = new ImageView("/images/splashScreen.jpg");
        AnchorPane p = new AnchorPane(splash, bar/*, applicationName*/);
        AnchorPane.setTopAnchor(splash, (double) 0);

        AnchorPane.setRightAnchor(splash, (double) 0);
        AnchorPane.setBottomAnchor(splash, (double) 0);
        AnchorPane.setLeftAnchor(splash, (double) 0);

        AnchorPane.setRightAnchor(bar, (double) 150);
        AnchorPane.setBottomAnchor(bar, (double) 290);
        AnchorPane.setLeftAnchor(bar, (double) 150);

//        AnchorPane.setLeftAnchor(applicationName, (double) 20);
//        AnchorPane.setTopAnchor(applicationName, (double) 110);

        p.setStyle("-fx-background-color: transparent;");
        return new Scene(p);
    }

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle(ResourceManager.getParam("APPLICATION.NAME"));
        stage.getIcons().add(ResourceManager.getAppLogo());
        stage.setScene(createPreloaderScene());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.getScene().getStylesheets().add(ResourceManager.getUIThemeStyle());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
