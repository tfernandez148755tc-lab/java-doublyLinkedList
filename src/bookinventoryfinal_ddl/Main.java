package bookinventoryfinal_ddl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookinventoryfinal_ddl/Main.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Book Inventory System");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) controller.saveToCsv();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}