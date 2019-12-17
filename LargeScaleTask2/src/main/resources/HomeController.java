package main.resources;

import main.resources.moviesEvaluation.Film;
import main.resources.moviesEvaluation.MainApp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	ObservableList<String> analyticsValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<String> filtersValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<Integer> voteValues = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	@FXML
	private Button voteBtn, searchBtn, deleteBtn, addBtn, logoutBtn;
	@FXML
	private ComboBox<Integer> voteBox;
	@FXML
	private ComboBox<String> filters, analyticsSelection;
	@FXML
	private TableView<Film> filmHomeTable, filmAnalyticsTable, filmAdminTable;
	@FXML
	private TableColumn<Film, String> filmHomeColumn, filmAnalyticsColumn, filmAdminColumn;
	@FXML
	private PieChart pieChart;
	@FXML
	private TextField searchText;
	@FXML
	private TextArea jsonArea;
	@FXML
	private Label errorLabel;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab adminTab, analyticsTab, homeTab;

	@FXML
	private void addFilm(ActionEvent event) {
		errorLabel.setText("Be patient, we need to implement that function!");
		errorLabel.setVisible(true);

		MainApp.managerM.addFilms(jsonArea.getText());

		return;
	}

	@FXML
	private void logoutAction(ActionEvent event) throws IOException {
		MainApp.user = null;
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
		System.out.println("Logout completed.");
	}

	@FXML
	public void deleteFilm(ActionEvent event) {
		errorLabel.setText("Be patient, we need to implement that function!");
		errorLabel.setVisible(true);
		return;
	}

	@FXML
	public void searchFilm(ActionEvent event) {
		updateTable(filmHomeTable, filmHomeColumn, 0);
		errorLabel.setText("Be patient, we need to implement that function!");
		errorLabel.setVisible(true);
		return;
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
		if (!MainApp.user.getAdmin())
			tabPane.getTabs().remove(adminTab);
		else
			updateTable(filmAdminTable, filmAdminColumn, 0);
		initializeBox();
		inizializePieChart();
		updateTable(filmAnalyticsTable, filmAnalyticsColumn, 1);
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

	private void updateTable(TableView<Film> table, TableColumn<Film, String> column, int check) {
		final ObservableList<Film> films;
		List<Film> filmList = new ArrayList<>();
		String title = null;
		if (check == 0) {
			title = searchText.getText();
			if(!title.isEmpty()) {
				filmList.addAll(MainApp.managerM.searchFilm(title));
			}
			// TODO User and Admin query (Search)
			films = FXCollections.observableArrayList(new Film("Prova film user 1"), new Film("prova film user 2"),
					new Film("prova film user 3"));
			films.addAll(filmList);
			
		} else {
			// TODO Analytics query
			films = FXCollections.observableArrayList(new Film("Prova film analytics 1"),
					new Film("prova film analytics 2"), new Film("prova film analytics 3"));
		}
		
		column.setCellFactory(tc -> {
			TableCell<Film, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			text.wrappingWidthProperty().bind(column.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			text.getStyleClass().add("columnText");
			return cell;
		});

		column.setCellValueFactory(new PropertyValueFactory<Film, String>("title"));
		table.setItems(films);
	}

}
