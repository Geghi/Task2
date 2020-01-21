package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleStringProperty;

public class ProductionNFilm {
	private SimpleStringProperty productionHouse;
	private int NFilm;
	
	public ProductionNFilm(String pH, int nF){
		productionHouse = new SimpleStringProperty(pH);
		NFilm = nF;
	}
	public String getProductionHouse() {
		return productionHouse.get();
	}
	public int getNFilm() {
		return NFilm;
	}
}
