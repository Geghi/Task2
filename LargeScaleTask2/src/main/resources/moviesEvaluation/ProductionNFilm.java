package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductionNFilm {
	private SimpleStringProperty productionHouse;
	private SimpleIntegerProperty NFilm;
	
	public ProductionNFilm(String pH, int nF){
		productionHouse = new SimpleStringProperty(pH);
		NFilm = new SimpleIntegerProperty(nF);
	}
	public String getProductionHouse() {
		return productionHouse.get();
	}
	public int getNFilm() {
		return NFilm.get();
	}
}
