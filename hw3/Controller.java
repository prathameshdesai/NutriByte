package hw3;

//PRATHAMESH DESAI  psdesai

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import hw3.Product.ProductNutrient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	ObservableList<Product> tempProductList = FXCollections.observableArrayList(); // temporary list to store products. Populates the dietproducts tableview

	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			float physicalActivityValue = 0;
			boolean toCheck = true;

			// Handles the genderComboBox is empty condition
			try
			{
				if(NutriByte.view.genderComboBox.getSelectionModel().isEmpty()) throw new InvalidProfileException("Missing gender information");
			}
			catch(InvalidProfileException e)
			{
				toCheck = false;	
			}

			// Reads the textfield for age and handles exceptions

			float age = (float) 0;
			if(toCheck == true)
			{
				try {
					if(NutriByte.view.ageTextField.getText().isEmpty()) throw new InvalidProfileException("Missing age information");
					else
					{
						age = Float.parseFloat(NutriByte.view.ageTextField.getText());	
					}
					if(age<0) throw new InvalidProfileException("Age must be positive number");
				}
				catch(InvalidProfileException e)
				{
					toCheck = false;	
				}
				catch (NumberFormatException e)
				{
					toCheck = false;	
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("NutriByte 3.0");
					alert.setHeaderText("Profile Data Error" );
					alert.setContentText("Incorrect age input. Must be a number");
					alert.showAndWait();	
				}
			}

			// Reads the textfield for weight and handles exceptions

			float weight = 0;
			if(toCheck == true)
			{
				try
				{
					if(NutriByte.view.weightTextField.getText().isEmpty()) throw new InvalidProfileException("Missing weight information");
					else
					{
						weight = Float.parseFloat(NutriByte.view.weightTextField.getText());	
					}
					if(weight<0) throw new InvalidProfileException("Weight must be positive number");
				}
				catch(InvalidProfileException e)
				{
					toCheck = false;	

				}
				catch (NumberFormatException e)
				{
					toCheck = false;	
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("NutriByte 3.0");
					alert.setHeaderText("Profile Data Error" );
					alert.setContentText("Incorrect weight input. Must be a number");
					alert.showAndWait();	
				}
			}

			// Reads the textfield for height and handles exceptions

			float height = 0;
			if(toCheck == true)
			{
				try
				{
					if(NutriByte.view.heightTextField.getText().isEmpty()) throw new InvalidProfileException("Missing height information");
					else
					{
						height = Float.parseFloat(NutriByte.view.heightTextField.getText());	
					}
					if(height<0) throw new InvalidProfileException("Height must be positive number");

				}
				catch(InvalidProfileException e)
				{
					toCheck = false;	
				}
				catch (NumberFormatException e)
				{
					toCheck = false;	
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("NutriByte 3.0");
					alert.setHeaderText("Profile Data Error" );
					alert.setContentText("Incorrect height input. Must be a number");
					alert.showAndWait();	
				}
			}


			// Handles the physicalActivityComboBox is empty condition
			if(NutriByte.view.physicalActivityComboBox.getSelectionModel().isEmpty() )
			{
				physicalActivityValue = NutriProfiler.PhysicalActivityEnum.SEDENTARY.getPhysicalActivityLevel();
			}

			//Checks if there was any selection made on genderComboBox
			if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex() >= 0 && !NutriByte.view.genderComboBox.getSelectionModel().isEmpty())
			{
				//Sets the physicalActivityValue by comparing with PhysicalActivityEnum
				if(NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() >= 0)
				{
					for(NutriProfiler.PhysicalActivityEnum physicalAct : NutriProfiler.PhysicalActivityEnum.values())
					{
						if(NutriByte.view.physicalActivityComboBox.getValue().equals(physicalAct.getName()))
						{
							physicalActivityValue = physicalAct.getPhysicalActivityLevel();
						}
					}
				}

				// If gender is Male, creates Male Object
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem() == "Male" && toCheck == true)
				{
					NutriByte.person = new Male( age , weight, height,physicalActivityValue,NutriByte.view.ingredientsToWatchTextArea.getText());

					//Clearing the list for any previous objects
					NutriByte.person.recommendedNutrientsList.clear();

					//Creates the profile of the person
					NutriProfiler.createNutriProfile(NutriByte.person);

					//Bind the recommendedNutrientsList to the Tableview
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

					NutriByte.view.recommendedNutrientsTableView.getSelectionModel().selectLast();
				}


				// If gender is Female, creates Female Object
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem() == "Female" && toCheck == true)
				{
					NutriByte.person = new Female( age , weight, height,physicalActivityValue,NutriByte.view.ingredientsToWatchTextArea.getText());

					//Clearing the list for any previous objects
					NutriByte.person.recommendedNutrientsList.clear();

					//Creates the profile of the person
					NutriProfiler.createNutriProfile(NutriByte.person);

					//Bind the recommendedNutrientsList to the Tableview
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

					NutriByte.view.recommendedNutrientsTableView.getSelectionModel().selectLast();
				}
			}
		}
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			Stage stage = null;

			//Uses the FileChooser to choose the files
			FileChooser filechooser = new FileChooser();
			filechooser.setTitle("Select File");
			filechooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
			File file = null;

			NutriByte.view.root.setBottom(NutriByte.view.nutriTrackerPane);

			//If user makes a valid file selection, runs this
			if((file = filechooser.showOpenDialog(stage)) != null)
			{
				NutriByte.view.root.setCenter(NutriByte.view.nutriProfilerGrid);
				NutriByte.view.initializePrompts();

				boolean filefound = NutriByte.model.readProfile(file.getAbsolutePath());

				//Populates the TextFields and ComboBoxes
				if(filefound == true)
				{
					NutriByte.view.ageTextField.setText(String.format("%.2f",NutriByte.person.age));
					NutriByte.view.weightTextField.setText(String.format("%.2f",NutriByte.person.weight));
					NutriByte.view.heightTextField.setText(String.format("%.2f",NutriByte.person.height));
					NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);

					//Checks if the person is Male or Female
					if(NutriByte.person instanceof Male)
					{
						NutriByte.view.genderComboBox.setValue("Male");
					}
					if(NutriByte.person instanceof Female)
					{
						NutriByte.view.genderComboBox.setValue("Female");
					}

					ObservableList<Product> temp = FXCollections.observableArrayList();

					temp = NutriByte.person.dietProductsList;

					//Sets the physical Activity level
					for(NutriProfiler.PhysicalActivityEnum pAct : NutriProfiler.PhysicalActivityEnum.values())
					{
						if(pAct.getPhysicalActivityLevel() == NutriByte.person.physicalActivityLevel)
						{
							NutriByte.view.physicalActivityComboBox.setValue(pAct.getName());
						}
					}

					NutriByte.person.dietProductsList = temp;
					NutriByte.model.searchResultsList.clear();

					// Populates the searchresultslist
					for(int i=0; i<NutriByte.person.dietProductsList.size();i++)
					{
						NutriByte.model.searchResultsList.add(Model.productsMap.get(NutriByte.person.dietProductsList.get(i).getNdbNumber()));
					}

					//Attach the searchresultlist to productsComboBox
					NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
					NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");
					NutriByte.view.productsComboBox.getSelectionModel().selectFirst();

					tempProductList.clear();
					tempProductList.addAll(NutriByte.person.dietProductsList);

					//Attach the tempProductList to dietProductsTableView
					NutriByte.view.dietProductsTableView.setItems(tempProductList);

					//Clears all the components at the end of OpenMenuHandler
					NutriByte.person.dietProductsList.clear();	
					NutriByte.person.dietProductsList.addAll(tempProductList);
					NutriByte.person.populateDietNutrientsMap();
					NutriByte.view.nutriChart.updateChart();
					NutriByte.view.productSearchTextField.clear();
					NutriByte.view.nutrientSearchTextField.clear();
					NutriByte.view.ingredientSearchTextField.clear();
					NutriByte.view.dietServingSizeTextField.clear();
					NutriByte.view.dietHouseholdSizeTextField.clear();

				}
				//If the file data was not valid, clears up all the previous fields on the display
				else
				{
					NutriByte.view.productSearchTextField.clear();
					NutriByte.view.nutrientSearchTextField.clear();
					NutriByte.view.ingredientSearchTextField.clear();
					NutriByte.model.searchResultsList.clear();
					NutriByte.view.searchResultSizeLabel.setText("");
					NutriByte.view.productNutrientsTableView.getItems().clear();
					NutriByte.view.productIngredientsTextArea.clear();
					NutriByte.view.servingSizeLabel.setText("0.00");
					NutriByte.view.householdSizeLabel.setText("0.00");
					NutriByte.view.dietServingUomLabel.setText("");
					NutriByte.view.dietHouseholdUomLabel.setText("");

					NutriByte.view.recommendedNutrientsTableView.getItems().clear();
					NutriByte.view.dietProductsTableView.getItems().clear();
					NutriByte.view.nutriChart.clearChart();

				}
			}
		}
	}

	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			//Sets the center to nutriProfilerGrid, initializes prompts and clears all the previous fields on the display
			NutriByte.view.root.setCenter(NutriByte.view.nutriProfilerGrid);
			NutriByte.view.root.setBottom(NutriByte.view.nutriTrackerPane);
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();

			NutriByte.view.dietProductsTableView.getItems().clear();
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.nutriChart.clearChart();	
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.model.searchResultsList.clear();
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.dietServingSizeTextField.clear();
			NutriByte.view.dietHouseholdSizeTextField.clear();

			NutriByte.view.initializePrompts();

		}
	}

	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}



	class SaveMenuItemHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent event) {

			//Local variables used for comparison
			String gender = null;
			float age = 0;
			float weight = 0;
			float height = 0;

			try
			{
				//Check gender and handles its exceptions
				gender = NutriByte.view.genderComboBox.getValue();
				if(gender == null) throw new InvalidProfileException("Missing gender information") ;

				//Check age and handles its exceptions
				if(NutriByte.view.ageTextField.getText().isEmpty()) throw new NullPointerException();
				age = Float.parseFloat(NutriByte.view.ageTextField.getText());
				if(age < 0) throw new InvalidProfileException("Age must be a positve number");

				//Check weight and handles its exceptions
				if(NutriByte.view.weightTextField.getText().isEmpty()) throw new NullPointerException();
				weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
				if(weight < 0) throw new InvalidProfileException("Weight must be a positve number");

				//Check height and handles its exceptions
				if(NutriByte.view.heightTextField.getText().isEmpty()) throw new NullPointerException();
				height = Float.parseFloat(NutriByte.view.heightTextField.getText());
				if(height < 0) throw new InvalidProfileException("Height must be a positve number");


				//Uses the FileChooser to choose the files
				Stage stage = null;
				FileChooser filechooser = new FileChooser();
				filechooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
				filechooser.setTitle("Select File");
				File file = null;

				//If there are no exceptions, calls the writeprofile method
				if((file = filechooser.showSaveDialog(stage)) != null)
				{
					NutriByte.model.writeProfile(file.getName());
				}
			}
			catch(NullPointerException e)
			{
				//Catches all Nullpointer Exceptions and shows pop-ups for that particular exception
				try 
				{
					if(age == 0) throw new InvalidProfileException("Missing Age Information");
					else if(weight == 0) throw new InvalidProfileException("Missing Weight Information");
					else if(height == 0) throw new InvalidProfileException("Missing Height Information");	
				}
				catch(InvalidProfileException e1)
				{}
			}
			catch(NumberFormatException e)
			{
				//Catches all NumberFormatException and shows pop-ups for that particular exception
				try {
					if(age == 0) throw new InvalidProfileException("Incorrect age input. Must be a number");
					else if(weight == 0) throw new InvalidProfileException("Incorrect Weight input. Must be a number");
					else if(height == 0) throw new InvalidProfileException("Incorrect Height input. Must be a number");	
				}
				catch(InvalidProfileException e1)
				{}
			}
			catch(InvalidProfileException e)
			{}
		}
	}


	class SearchButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {

			//Clears up any previous data
			NutriByte.model.searchResultsList.clear();
			NutriByte.view.productIngredientsTextArea.clear();

			String pname = NutriByte.view.productSearchTextField.getText();

			//Searches master product to find product based on input in productSearchTextField
			for(Map.Entry<String, Product> p : Model.productsMap.entrySet())
			{
				if(p.getValue().getProductName().toLowerCase().contains(pname.toLowerCase()))
				{
					NutriByte.model.searchResultsList.add(p.getValue());
				}
			}

			String nutrientname = NutriByte.view.nutrientSearchTextField.getText();

			//Searches master nutrient to find product based on input in nutrientSearchTextField 
			if(!nutrientname.isEmpty())
			{
				Iterator<Product> iter = NutriByte.model.searchResultsList.iterator();

				while(iter.hasNext())
				{
					boolean foundNutrient = false;
					Product pr = iter.next();

					for(Map.Entry<String, ProductNutrient> prnut : pr.getProductNutrients().entrySet())
					{
						String nutricode = prnut.getValue().getNutrientCode();

						if(Model.nutrientsMap.get(nutricode).getNutrientName().toLowerCase().contains(nutrientname.toLowerCase()))
						{
							foundNutrient = true;
							break;
						}
					}

					//Removes those products which did not have mentioned nutrients
					if(foundNutrient == false)
					{
						iter.remove();
					}
				}
			}

			//Searches for ingredients in that product and removes if the product does not have ingredients mentioned in ingredients search textfield
			String ingred = NutriByte.view.ingredientSearchTextField.getText();

			if(!ingred.isEmpty())
			{
				Iterator<Product> iter1 = NutriByte.model.searchResultsList.iterator();
				while(iter1.hasNext())
				{
					Product pr = iter1.next();

					if(!pr.getIngrediants().toLowerCase().contains(ingred.toLowerCase()))
					{
						iter1.remove();
					}	
				}	
			}

			// Sets the searchResultSizeLabel
			NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");

			//Attaches the list to the combo Box
			NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);	
			NutriByte.view.productsComboBox.getSelectionModel().selectFirst();
		}	
	}


	class ClearButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {

			//Clears up all the fields or sets default values
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.model.searchResultsList.clear();
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
		}
	}


	class AddDietButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {

			//Created a dummy product to store user input values in  servingsize textarea and household size textarea
			//and adds it to the temporary list
			Product p = new Product();

			//handles the conditon where dietServingSizeTextField and dietHouseholdSizeTextField  are empty
			if(NutriByte.view.dietServingSizeTextField.textProperty().isEmpty().getValue() && NutriByte.view.dietHouseholdSizeTextField.textProperty().isEmpty().getValue())
			{
				p.setNdbNumber(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber());
				p.setProductName(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductName());
				p.setManufacturer(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getManufacturer());
				p.setIngredients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngrediants());
				p.setServingSize(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize());
				p.setServingUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				p.setHouseholdSize(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize());
				p.setHouseholdUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				p.setProductNutrients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients());

				tempProductList.add(p);
			}

			//handles the conditon where dietServingSizeTextField is not empty and dietHouseholdSizeTextField is empty
			else if(!NutriByte.view.dietServingSizeTextField.textProperty().isEmpty().getValue() && NutriByte.view.dietHouseholdSizeTextField.textProperty().isEmpty().getValue() )
			{
				p.setNdbNumber(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber());
				p.setProductName(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductName());
				p.setManufacturer(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getManufacturer());
				p.setIngredients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngrediants());

				float servesize = Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText());
				float hhsize =  NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize();

				p.setServingSize(servesize);
				p.setServingUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				p.setHouseholdSize((servesize/hhsize) * NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize());
				p.setHouseholdUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				p.setProductNutrients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients());

				tempProductList.add(p);
			}

	    	//handles the conditon where dietServingSizeTextField  is empty and dietHouseholdSizeTextField is not empty

			else if(NutriByte.view.dietServingSizeTextField.textProperty().isEmpty().getValue() && !NutriByte.view.dietHouseholdSizeTextField.textProperty().isEmpty().getValue() )
			{
				p.setNdbNumber(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber());
				p.setProductName(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductName());
				p.setManufacturer(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getManufacturer());
				p.setIngredients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngrediants());

				float hhsize = Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText());
				float servesize = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize();

				if(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize() != 0)
				{
				p.setServingSize((hhsize / NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize())*servesize);
				}
				else
				{
					p.setServingSize((hhsize * servesize));
				}
				p.setServingUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				p.setHouseholdSize(hhsize);
				p.setHouseholdUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				p.setProductNutrients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients());

				tempProductList.add(p);
			}
			
			//handles the conditon where dietServingSizeTextField and dietHouseholdSizeTextField both are not empty

			else if(!NutriByte.view.dietServingSizeTextField.textProperty().isEmpty().getValue() && !NutriByte.view.dietHouseholdSizeTextField.textProperty().isEmpty().getValue() )	
			{
				p.setNdbNumber(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber());
				p.setProductName(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductName());
				p.setManufacturer(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getManufacturer());
				p.setIngredients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngrediants());

				float servesize = Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText());
				float hhsize =  NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize();

				p.setServingSize(servesize);
				p.setServingUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				p.setHouseholdSize((servesize/hhsize)*NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize());
				p.setHouseholdUom(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
				p.setProductNutrients(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients());

				tempProductList.add(p);
			}

			//attaches temporary list todietProductsTableView
			NutriByte.view.dietProductsTableView.setItems(tempProductList);
			
			//checks if there was person created and only then updates the chart
			if(NutriByte.person != null)
			{
				NutriByte.person.dietProductsList.clear();
				NutriByte.person.dietProductsList.addAll(tempProductList);
				NutriByte.person.populateDietNutrientsMap();
				NutriByte.view.nutriChart.updateChart();
				if(NutriByte.person.dietProductsList.isEmpty())
				{
					NutriByte.view.nutriChart.clearChart();	
				}
			}
		}
	}


	class CloseProfileItemHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) {

			//Clears up the fields and sets the homescreen on the display
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			NutriByte.view.dietProductsTableView.getItems().clear();
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.nutriChart.clearChart();	

			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
		}

	}

	class RemoveDietButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			if(NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex()>=0)
			{
				int i = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex(); 

				//removes the product from the temporary list
				tempProductList.remove(i);
				NutriByte.view.dietProductsTableView.setItems(tempProductList);

				//Updates the chart
				NutriByte.person.dietProductsList.clear();
				NutriByte.person.dietProductsList.addAll(tempProductList);
				NutriByte.person.populateDietNutrientsMap();
				NutriByte.view.nutriChart.updateChart();
				
				//clears the chart
				if(NutriByte.person.dietProductsList.isEmpty())
				{
					NutriByte.view.nutriChart.clearChart();	
				}
			}
		}
	}

	class ProductsComboBoxListener implements ChangeListener<Product>
	{

		@Override
		public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {

			if(NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0)
			{
				Product p =  newValue;

				NutriByte.view.productIngredientsTextArea.setText("Product Ingredients: " + p.getIngrediants());

				ObservableList<ProductNutrient> prdnutrientlist = FXCollections.observableArrayList();

				Map<String,ProductNutrient> prdmap = p.getProductNutrients();

				// Takes all the productnutrients from the map and puts it into list
				for(Map.Entry<String, ProductNutrient> prdNut : prdmap.entrySet())
				{
					prdnutrientlist.add(prdNut.getValue());
				}

				//Attaches the prdnutrientslist to populate the productNutrientsTableView
				NutriByte.view.productNutrientsTableView.setItems(prdnutrientlist);

				NutriByte.view.productNutrientsTableView.getSelectionModel().selectFirst();

				// Sets the labels
				NutriByte.view.servingSizeLabel.setText(String.format("%.2f %s", NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize(),NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom()));
				NutriByte.view.householdSizeLabel.setText(String.format("%.2f %s", NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize(),NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom()));
				NutriByte.view.dietServingUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
			}
		}
	}
}
