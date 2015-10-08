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
    Label applicationVersion;

    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        bar.setPrefHeight(3);
        bar.getStyleClass().add("white");

        applicationName = new Label(ResourceManager.getParam("APPLICATION.NAME"));
        applicationName.getStyleClass().add("splash-screen-app-name");

        applicationVersion = new Label(ResourceManager.getParam("APPLICATION.RELEASE.VERSION"));
        applicationVersion.getStyleClass().add("splash-screen-app-ver");

//        ImageView splash = new ImageView("/images/splashScreen.png");
        ImageView splash = new ImageView("/images/background.jpg");
        splash.setFitWidth(500);
        splash.setFitHeight(500);

        ImageView appLogo = new ImageView(ResourceManager.getAppLogo());
        appLogo.setFitWidth(92);
        appLogo.setFitHeight(92);
        appLogo.setEffect(new Reflection(0, 1, 0.3, 0));

        ImageView davsoftLogo = new ImageView(ResourceManager.getDavsoftLogo());
        davsoftLogo.prefWidth(32);

        AnchorPane p = new AnchorPane(splash, appLogo, davsoftLogo, applicationName, bar, applicationVersion);

        AnchorPane.setTopAnchor(splash, (double) 0);
        AnchorPane.setRightAnchor(splash, (double) 0);
        AnchorPane.setBottomAnchor(splash, (double) 0);
        AnchorPane.setLeftAnchor(splash, (double) 0);

        AnchorPane.setTopAnchor(appLogo, (double) 20);
        AnchorPane.setLeftAnchor(appLogo, (double) 20);

        AnchorPane.setBottomAnchor(davsoftLogo, (double) 20);
        AnchorPane.setLeftAnchor(davsoftLogo, (double) 20);

        AnchorPane.setRightAnchor(applicationName, (double) 150);
        AnchorPane.setTopAnchor(applicationName, (double) 200);
        AnchorPane.setLeftAnchor(applicationName, (double) 150);

        AnchorPane.setRightAnchor(bar, (double) 150);
        AnchorPane.setTopAnchor(bar, (double) 220);
        AnchorPane.setLeftAnchor(bar, (double) 150);

        AnchorPane.setRightAnchor(applicationVersion, (double) 20);
        AnchorPane.setBottomAnchor(applicationVersion, (double) 15);

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
