package com.synisys.msggen;

import com.synisys.msggen.enums.ErrorCodes;
import com.synisys.msggen.utils.AppSpecUncaughtExceptionHandler;
import com.synisys.msggen.utils.FXMLFactory;
import com.synisys.msggen.utils.ResourceManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/20/15 <br/>
 * <b>Time:</b> 1:46 AM <br/>
 */
public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    private static Stage primaryStage;

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new AppSpecUncaughtExceptionHandler());
        try {
            launch(args);
        } catch (Exception ex) {
            Logger.getLogger(Main.class).error(ex.getMessage(), ex);
        }
    }

    @Override
    public void init() {
        try {
            Thread.sleep(5000);
            initLogger();
            Logger.getLogger(Main.class).info("Application init completed!");
        } catch (Exception ex) {
            Logger.getLogger(Main.class).error("Error occurred in main init method: ", ex);
            Platform.exit();
        }
    }

    private void initLogger() throws IOException {
        String loggingFileName = "logs/" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "/log";
        Properties props = new Properties();
        InputStream input = getClass().getResourceAsStream("/properties/log4j.properties");
        if (input == null) {
            throw new FileNotFoundException(ErrorCodes.LOG4J_PROP_MISSING.getErrorMessage());
        }
        props.load(input);
        props.setProperty("log4j.appender.fileAppender.File", loggingFileName);
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
        LOGGER.info("Logging has been successfully initialized.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Thread.currentThread().setUncaughtExceptionHandler(new AppSpecUncaughtExceptionHandler());

//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setResources(ResourceManager.getBundle("properties/messages"));
//        fxmlLoader.setResources(ResourceBundle.getBundle("properties/messages", Locale.ENGLISH));
//        fxmlLoader.setResources(ResourceBundle.getBundle("properties/messages", Locale.CHINESE));
//        Parent root = fxmlLoader.load(this.getClass().getResource("/screens/mainScreen.fxml").openStream());

        primaryStage.setTitle(ResourceManager.getMessage("title.window.main"));
        primaryStage.setScene(new Scene(FXMLFactory.getFXMLParent("/screens/mainScreen.fxml"), 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setOnCloseRequest(event -> {
            Logger.getLogger(Main.class).info("Application terminated by user.");
            Platform.exit();
        });
        primaryStage.setFullScreenExitHint("");

        primaryStage.getScene().getStylesheets().add("css/style.css");
        primaryStage.getScene().setFill(Color.web("DDF", 0.75));
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.requestFocus();
    }

    private void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
