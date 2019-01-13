package hw3;

//PRATHAMESH DESAI psdesai
import hw3.Product.ProductNutrient;

//PRATHAMESH DESAI  psdesai

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers 

	/**Uncomment the following three lines if you want to try out the full-size data files */
		static final String PRODUCT_FILE = "data/Products.csv";
		static final String NUTRIENT_FILE = "data/Nutrients.csv";
		static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
//	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image 

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	//Person Binding for realtime updating of recommendedNutrients table
	ObjectBinding<Person> personBinding = new ObjectBinding<Person>() {
		{
			super.bind(NutriByte.view.ageTextField.textProperty(), NutriByte.view.weightTextField.textProperty(), NutriByte.view.heightTextField.textProperty(), NutriByte.view.genderComboBox.valueProperty(),NutriByte.view.physicalActivityComboBox.valueProperty());
		}

		@Override
		protected Person computeValue() {
			String gender = "";
			float age = 0, weight = 0, height = 0;
			float physicalActivityLevel = 0;
			String ingredientsToWatch = "";
			TextField textField = NutriByte.view.ageTextField;

			try {
				//Handling all the exceptions for gender, age, weight, height and turning the values red for invalid input
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex() >= 0 )
				{
					gender = NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem();
				}

				NutriByte.view.ageTextField.setStyle("-fx-text-inner-color: black;");
				age = Float.parseFloat(textField.getText().trim());
				if(age<0){NutriByte.view.ageTextField.setStyle("-fx-text-inner-color: red;");}
				
				
				textField = NutriByte.view.weightTextField;
				NutriByte.view.weightTextField.setStyle("-fx-text-inner-color: black;");
				weight = Float.parseFloat(textField.getText().trim());
				if(weight<0){NutriByte.view.weightTextField.setStyle("-fx-text-inner-color: red;");}
					

				textField = NutriByte.view.heightTextField;
				NutriByte.view.heightTextField.setStyle("-fx-text-inner-color: black;");
				height = Float.parseFloat(textField.getText().trim());
				if(height<0){NutriByte.view.heightTextField.setStyle("-fx-text-inner-color: red;");}
					

				if(NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() < 0 )
				{
					physicalActivityLevel = 1.0f;
				}
				else
				{
					String activityname = NutriByte.view.physicalActivityComboBox.getValue();

					for(NutriProfiler.PhysicalActivityEnum phy : NutriProfiler.PhysicalActivityEnum.values())
					{
						if(phy.getName().equalsIgnoreCase(activityname))
						{
							physicalActivityLevel = phy.getPhysicalActivityLevel();
						}
					}
				}

				if(!NutriByte.view.ingredientsToWatchTextArea.getSelectedText().isEmpty())
				{
					ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getSelectedText();
				}

				boolean personcreated = false;

				switch(gender)
				{
				case "Male" :  NutriByte.person = new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch); personcreated = true; break;

				case "Female" :  NutriByte.person = new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch); personcreated = true; break;

				default: break;
				}

				if(personcreated == true)
				{
					return NutriByte.person;
				}
				else
				{
					return null;
				}

			} catch (NumberFormatException e) {
				textField.setStyle("-fx-text-inner-color: red;");
				return null;
			} 
		}
	};


	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
		setupBindings();
		stage.setTitle("NutriByte 2.0");
		stage.setScene(scene);

		//added  lisener to the personbinding for realtime update
		personBinding.addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
			{
			person = newValue;
				if(person.age > 0 && person.weight > 0 && person.height > 0)
				{
					NutriByte.person.recommendedNutrientsList.clear();

					NutriProfiler.createNutriProfile(NutriByte.person);

					//Bind the recommendedNutrientsList to the Tableview
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

					NutriByte.view.recommendedNutrientsTableView.getSelectionModel().selectLast();
				}
			}
		});

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);		
	}

	void setupBindings() {
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseProfileItemHandler());

		//All the cellvaluefactory
		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);
		view.productNutrientNameColumn.setCellValueFactory(productdNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productdNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productdNutrientUomCallback);
		
		//All the butttonhandlers
		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.productsComboBox.valueProperty().addListener(controller.new ProductsComboBoxListener());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());
	}


	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	// Populates the Recommended Nutrients column of the tableview
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {


			String code = arg0.getValue().getNutrientCode();

			// checks in the recommendedNutrientsList for nutrient code of the object passed and returns the quantity of the object found
			for(int i=0; i<NutriByte.person.recommendedNutrientsList.size();i++)
			{
				if(code.equals(NutriByte.person.recommendedNutrientsList.get(i).getNutrientCode()))
				{
					return new SimpleStringProperty(String.format("%.2f", NutriByte.person.recommendedNutrientsList.get(i).getNutrientQuantity()));
				}
			}
			return null;		
		}
	};


	// Populates the UOM column of the tableview by checking the nutrientCode of the object passed, and returns the UOM of the object found in the list
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {

			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};
	
	
	//Populates the ProductNutrient name column of productnutrients tableview
	Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> productdNutrientNameCallback = new Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> () {

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			
			String nutricode = param.getValue().getNutrientCode();
			Nutrient nutrient = Model.nutrientsMap.get(nutricode);
			return nutrient.nutrientNameProperty();
		}	
	};
	
	//Populates the ProductNutrient quantity column of productnutrients tableview
	Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> productdNutrientQuantityCallback = new Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> () {

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			
		float nutriquantity = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients().get(param.getValue().getNutrientCode()).getNutrientQuantity();
		
		return new SimpleStringProperty(String.format("%.2f", nutriquantity ));
		}	
	};

	//Populates the ProductNutrient Uom column of productnutrients tableview
	Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> productdNutrientUomCallback = new Callback<CellDataFeatures<ProductNutrient,String>, ObservableValue<String>> () {

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			
			Nutrient nutrient = Model.nutrientsMap.get(param.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}	
	};
	
}
