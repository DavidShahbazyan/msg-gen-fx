import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 11/16/15 <br/>
 * <b>Time:</b> 7:41 PM <br/>
 */
public class TreeViewTest extends Application {
    /** {@inheritDoc} */
    @Override
    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene());
        TreeView<String> treeView = new TreeView<>();

        treeView.setRoot(new TreeItem<>("Settings"));


        TreeItem<String> ss1 = new TreeItem<>("Sub-setting 1");
        ss1.getChildren().add(new TreeItem<>("SS1-Option 1"));
        ss1.getChildren().add(new TreeItem<>("SS1-Option 2"));
        ss1.getChildren().add(new TreeItem<>("SS1-Option 3"));

        treeView.getRoot().getChildren().add(ss1);

        treeView.getRoot().getChildren().add(new TreeItem<>("Sub-setting 2"));

        TreeItem<String> ss3 = new TreeItem<>("Sub-setting 3");
        ss3.getChildren().add(new TreeItem<>("SS3-Option 1"));
        ss3.getChildren().add(new TreeItem<>("SS3-Option 2"));

        treeView.getRoot().getChildren().add(ss3);

        treeView.getRoot().getChildren().add(new TreeItem<>("Sub-setting 4"));


        AnchorPane root = new AnchorPane(treeView);
        AnchorPane.setTopAnchor(treeView, (double) 0);
        AnchorPane.setRightAnchor(treeView, (double) 0);
        AnchorPane.setBottomAnchor(treeView, (double) 0);
        AnchorPane.setLeftAnchor(treeView, (double) 0);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
