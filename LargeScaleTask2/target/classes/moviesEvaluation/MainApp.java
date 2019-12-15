package main.resources.moviesEvaluation;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	public static MongoManager managerM;
	public static User user = null;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("MoviesEvaluations");

		managerM = new MongoManager(27017, "localhost");

		initLayout();

	}

//    Initializes the home layout.
	public void initLayout() {
		try {
			// Load home layout from fxml file.
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));

			// Show the scene containing the home layout.
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("css/DarkTheme.css").toExternalForm());
			primaryStage.getIcons()
					.add(new Image(getClass().getClassLoader().getResourceAsStream("Images/AppIcon.png")));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}