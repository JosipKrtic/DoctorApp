package doctor.app.doctorapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        double a = 2;
        System.out.println(String.format("%1$,.2f", a));
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Fxml/LoginPage.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("DoctorApp");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}