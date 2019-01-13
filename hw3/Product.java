package hw3;

//PRATHAMESH DESAI  psdesai

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product {

	private final StringProperty ndbNumber = new SimpleStringProperty();
	private final StringProperty productName = new SimpleStringProperty();
	private final StringProperty manufacturer = new SimpleStringProperty();
	private final StringProperty ingredients = new SimpleStringProperty();
	private final FloatProperty servingSize = new SimpleFloatProperty();
	private final StringProperty servingUom = new SimpleStringProperty();
	private final FloatProperty householdSize = new SimpleFloatProperty();
	private final StringProperty householdUom = new SimpleStringProperty();

	private	ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();

	//Overriding toString
	public String toString()
	{
		return (productName.get() + " by " + manufacturer.get());
	}
	

	//Default Constructor
	public Product(){

		ndbNumber.set("");
		productName.set("");
		manufacturer.set("");
		ingredients.set("");
		servingSize.set(0);
		servingUom.set("");
		householdSize.set(0);
		householdUom.set("");
	}

	//Non-Default Constructor
	public Product(String ndbNumber, String productName, String manufacturer, String ingrediants)
	{
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manufacturer);
		this.ingredients.set(ingrediants);

	}


	//Getters and Setters
	public ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}


	public String getNdbNumber()
	{
		return ndbNumber.get();
	}
	public String getProductName()
	{
		return productName.get();
	}
	public String getManufacturer()
	{
		return manufacturer.get();
	}
	public String getIngrediants()
	{
		return ingredients.get();
	}
	public Float getServingSize() 
	{
		return servingSize.get();
	}
	public String getServingUom() 
	{
		return servingUom.get();
	}
	public Float getHouseholdSize() 
	{
		return householdSize.get();
	}
	public String getHouseholdUom()
	{
		return householdUom.get();
	}

	public void setNdbNumber(String ndbNumber)
	{
		this.ndbNumber.set(ndbNumber);
	}
	public void setProductName(String productName)
	{
		this.productName.set(productName);
	}
	public void setManufacturer(String manufacturer)
	{
		this.manufacturer.set(manufacturer);
	}
	public void setIngredients(String ingredients)
	{
		this.ingredients.set(ingredients);
	}
	public void setServingSize(Float servingSize) 
	{
		this.servingSize.set(servingSize);
	}
	public void setServingUom(String servingUom) 
	{
		this.servingUom.set(servingUom);
	}
	public void setHouseholdSize(Float householdSize) 
	{
		this.householdSize.set(householdSize);
	}
	public void setHouseholdUom(String householdUom) 
	{
		this.householdUom.set(householdUom);
	}

	public StringProperty ndbNumberProperty() {
		return ndbNumber;
	}
	public StringProperty productNameProperty() {
		return productName;
	}
	public StringProperty manufacturerProperty() {
		return manufacturer;
	}
	public StringProperty ingredientsProperty() {
		return ingredients;
	}
	public FloatProperty servingSizeProperty() {
		return servingSize;
	}
	public StringProperty servingUomProperty() {
		return servingUom;
	}
	public FloatProperty householdSizeProperty() {
		return householdSize;
	}
	public StringProperty householdUomProperty() {
		return householdUom;
	}








	//Inner class ProductNutrient
	public class ProductNutrient{

		private final StringProperty nutrientCode = new SimpleStringProperty();
		private final FloatProperty nutrientQuantity = new SimpleFloatProperty();

		//Default Constructor
		public ProductNutrient() {
			this.nutrientCode.set("");
			this.nutrientQuantity.set(0);
		}

		//Non-Default Constructor
		public ProductNutrient(String nutrientCode, float nutrientQuantity) {
			this.nutrientCode.set(nutrientCode);
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public StringProperty nutrientCodeProperty() {
			return nutrientCode;
		}
		public FloatProperty nutrientQuantityProperty() {
			return nutrientQuantity;
		}


		//Getters and Setters
		public String getNutrientCode() {
			return nutrientCode.get();
		}
		public float getNutrientQuantity() {
			return nutrientQuantity.get();
		}

		public void setNutrientCode(String nutrientCode){
			this.nutrientCode.set(nutrientCode);
		}
		public void setNutrientQuantity(float nutrientQuantity) 
		{
			this.nutrientQuantity.set(nutrientQuantity);
		}

	}
}
