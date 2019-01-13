package hw3;

//PRATHAMESH DESAI  psdesai

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {

	private final StringProperty nutrientCode = new SimpleStringProperty();
	private final StringProperty nutrientName = new SimpleStringProperty();
	private final StringProperty nutrientUom = new SimpleStringProperty();

	//Default Constructor
	protected Nutrient() {
		nutrientCode.set("");
		nutrientName.set("");
		nutrientUom.set("");
	}

	//Non-Default Constructor
	protected Nutrient(String nutrientCode, String nutrientName, String nutrientUom) {
		super();
		this.nutrientCode.set(nutrientCode);
		this.nutrientName.set(nutrientName);
		this.nutrientUom.set(nutrientUom);
	}



	//Getters and Setters
	public StringProperty nutrientCodeProperty() {
		return nutrientCode;
	}
	public StringProperty nutrientNameProperty() {
		return nutrientName;
	}
	public StringProperty nutrientUomProperty() {
		return nutrientUom;
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}
	public void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}
	public String getNutrientName() {
		return nutrientName.get();
	}
	public void setNutrientName(String nutrientName) {
		this.nutrientName.set(nutrientName);
	}
	public String getNutrientUom() {
		return nutrientUom.get();
	}
	public void setNutrientUom(String nutrientUom) {
		this.nutrientUom.set(nutrientUom);
	}


}
