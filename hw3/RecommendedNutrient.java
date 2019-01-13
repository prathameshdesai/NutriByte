package hw3;

//PRATHAMESH DESAI  psdesai

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {

	private final StringProperty nutrientCode = new SimpleStringProperty();
	private final FloatProperty nutrientQuantity = new SimpleFloatProperty();



	//Default Constructor
	public RecommendedNutrient() {
		super();
		nutrientCode.set("");
		nutrientQuantity.set(0);
	}

	//Non-Default Constructor
	public RecommendedNutrient(String nutrientCode, Float nutrientQuantity) {
		super();
		this.nutrientCode.set(nutrientCode);
		this.nutrientQuantity.set(nutrientQuantity);
	}

	//Getters and Setters
	public StringProperty nutrientCodeProperty() {
		return nutrientCode;
	}
	public FloatProperty nutrientQuantityProperty() {
		return nutrientQuantity;
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}
	public void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}

	public Float getNutrientQuantity() {
		return nutrientQuantity.get();
	}
	public void setNutrientQuantity(Float nutrientQuantity) {
		this.nutrientQuantity.set(nutrientQuantity);
	}

}
