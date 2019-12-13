package main.resources;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	ObservableList<String> analyticsValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<String> filtersValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<Integer> voteValues = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	@FXML
	private Button voteBtn;
	@FXML
	private ComboBox<Integer> voteBox;
	@FXML
	private ComboBox<String> filters;
	@FXML
	private Menu Analytics;
	@FXML
	private ComboBox<String> analyticsSelection;
	@FXML
	private TableView<String> FilmTable;
	@FXML
	private TableColumn<String, String> FilmColumn;
	@FXML
	private PieChart pieChart;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button searchBtn;
	@FXML
	private TextField searchText;
	@FXML
	private Button logoutBtn;
	@FXML
	private Button addBtn;

	@FXML
	private void addFilm(ActionEvent event) {
		System.out.println("Testing");
	}

	@FXML
	private void logoutAction(ActionEvent event) throws IOException {
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
	}

	@FXML
	private void initializeBox() {
		analyticsSelection.setValue("Item 1");
		analyticsSelection.setItems(analyticsValues);
		filters.setValue("Item 1");
		filters.setItems(filtersValues);
		voteBox.setValue(1);
		voteBox.setItems(voteValues);
	}

	@FXML
	public void deleteFilm(ActionEvent event) {
		System.out.println("test");
	}

	@FXML
	public void searchFilm(ActionEvent event) {
		System.out.println("Test2");
	}

	@FXML
	public void addVote(ActionEvent event) {
		SingleSelectionModel<Integer> x = voteBox.getSelectionModel();
		System.out.println(x.getSelectedItem());
	}

	public void inizializePieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("Horror", 50),
				new PieChart.Data("Action", 30), new PieChart.Data("Fantasy", 90), new PieChart.Data("Anime", 10));
		pieChart.setData(pieChartData);
		pieChart.setStartAngle(90);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeBox();
		FilmColumn.setCellValueFactory(new PropertyValueFactory<String, String>("First Film"));
		inizializePieChart();
	}
}
