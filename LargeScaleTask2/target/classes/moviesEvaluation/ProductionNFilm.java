package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductionNFilm {
	private SimpleStringProperty productionHouse;
	private SimpleIntegerProperty NFilm;

	public ProductionNFilm(String pH, int nF) {
		productionHouse = new SimpleStringProperty(pH);
		NFilm = new SimpleIntegerProperty(nF);
	}

	public void setProductionHouse(SimpleStringProperty productionHouse) {
		this.productionHouse = productionHouse;
	}

	public void setNFilm(SimpleIntegerProperty nFilm) {
		NFilm = nFilm;
	}

	public String getProductionHouse() {
		return productionHouse.get();
	}

	public int getNFilm() {
		return NFilm.get();
	}
}
