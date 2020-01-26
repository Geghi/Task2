package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Aggregator {
	private SimpleStringProperty name;
	private SimpleIntegerProperty num;

	public Aggregator(String t, int n) {
		name = new SimpleStringProperty(t);
		num = new SimpleIntegerProperty(n);
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public void setNum(SimpleIntegerProperty num) {
		this.num = num;
	}

	public String getName() {
		return name.get();
	}

	public int getNum() {
		return num.get();
	}
}


