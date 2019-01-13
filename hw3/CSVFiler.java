package hw3;

import java.io.BufferedWriter;

//PRATHAMESH DESAI  psdesai

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class CSVFiler extends DataFiler {

	@Override
	public boolean readFile(String filename) {

		Scanner sc;
		try {
			sc = new Scanner(new File(filename));
			int a = 0;
			StringBuilder sb = new StringBuilder();
			while(!sc.hasNextLine())
			{
				return false;
			}

			// Loads the StringBuilder
			while(sc.hasNextLine())
			{
				if(a==0)
				{
					sb.append(sc.nextLine());
					a++;
				}
				else
				{
					sb.append("\n" + sc.nextLine());
				}
			}
	
			String[] alldata = sb.toString().split("\n");

			String firstrow = alldata[0];

			CSVFiler csv = new CSVFiler();

			//Calls the validatePersonData method and gets the person object
			try {
				NutriByte.person = csv.validatePersonData(firstrow);
				if(NutriByte.person == null)
				{
					throw new NullPointerException();
				}
			}
			finally {}

			//Calls the validateproductData method and gets the person object
			for(int i=1; i<alldata.length; i++)
			{
				try {
					Product p = csv.validateProductData(alldata[i]);
					if(p != null)
					{
						NutriByte.person.dietProductsList.add(p);
					}
				}
				catch(NullPointerException e)
				{}
			}

			sc.close();
			return true;
		} 

		catch (FileNotFoundException e) {
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}

	@Override
	public void writeFile(String filename) {

		//local variables for comparison
		String gender = null;
		float age = 0;
		float weight = 0;
		float height = 0;
		String activityname = null;
		float physicalActivityLevel = 0;

		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename));) {

			// handles all exceptions for age, weight, height
			gender = NutriByte.view.genderComboBox.getValue();

			age = Float.parseFloat(NutriByte.view.ageTextField.getText());
			if(age < 0) throw new InvalidProfileException("Age must be a positve number");

			weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
			if(weight < 0) throw new InvalidProfileException("Weight must be a positve number");

			height = Float.parseFloat(NutriByte.view.heightTextField.getText());
			if(height < 0) throw new InvalidProfileException("Height must be a positve number");

			activityname = NutriByte.view.physicalActivityComboBox.getValue();

			for(NutriProfiler.PhysicalActivityEnum phy : NutriProfiler.PhysicalActivityEnum.values())
			{
				if(phy.getName().equalsIgnoreCase(activityname))
				{
					physicalActivityLevel = phy.getPhysicalActivityLevel();
				}
			}

			String ingrediantsinfo = NutriByte.view.ingredientsToWatchTextArea.getText();

			//writes person information into the file 
			bw.write(String.format("%s, %f, %f, %f, %f, %s%n",gender,age,weight,height,physicalActivityLevel,ingrediantsinfo));

			//obtains the product, servingsize and household size values to write
			for(int row=0; row<NutriByte.person.dietProductsList.size();row++)
			{
				String ndbnum = NutriByte.person.dietProductsList.get(row).getNdbNumber();

				float servingsize= (float) NutriByte.view.dietProductsTableView.getColumns().get(1).getCellObservableValue(row).getValue();

				float householdsize = (float) NutriByte.view.dietProductsTableView.getColumns().get(3).getCellObservableValue(row).getValue();

				bw.write(String.format("%s, %.1f, %.1f%n",ndbnum,servingsize,householdsize));
			}

			bw.close();

		}

		catch(NullPointerException e)
		{
			try 
			{
				//handles all the nullpointer exceptions
				if(!gender.equals("Male") && !gender.equals("Female")) throw new InvalidProfileException("Missing gender information");
				else if(age == 0) throw new InvalidProfileException("Missing Age Information");
				else if(weight == 0) throw new InvalidProfileException("Missing Weight Information");
				else if(height == 0) throw new InvalidProfileException("Missing Height Information");	
				else if(activityname.isEmpty())
				{
					physicalActivityLevel = 1.0f;
				}
			}

			catch(InvalidProfileException e1)
			{}
		}
		catch(NumberFormatException e)
		{
			//handles all the numberformat exceptions
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
		catch (IOException e) {
			throw new InvalidProfileException("Could not save profile data");
		}
	}





	public Person validatePersonData(String firstrow)
	{
		int validperson = 0; //used to check if there were any exceptions
		String[] temp = firstrow.split(",");

		// Temporary variables to load in the constructor
		String gender = null;
		float age = 0;
		float weight = 0;
		float height = 0;
		float physicalActivityLevel = 0;

		try {
			//handles all the age , weight, height , gender , physicalactivity level exceptions
			gender = temp[0].trim();
			age = Float.parseFloat(temp[1].trim());

			if(age < 0) throw new InvalidProfileException("Age must be a positve number");

			weight = Float.parseFloat(temp[2].trim());
			if(weight < 0) throw new InvalidProfileException("Weight must be a positve number");

			height = Float.parseFloat(temp[3].trim());
			if(height < 0) throw new InvalidProfileException("Height must be a positve number");

			physicalActivityLevel = Float.parseFloat(temp[4].trim());

			if(physicalActivityLevel != 1.0f && physicalActivityLevel != 1.1f && physicalActivityLevel != 1.25f && physicalActivityLevel != 1.48f) throw new InvalidProfileException("Invalid physical activity level: " + temp[4].trim() + "\n Must be: 1.0, 1.1, 1.25, 1.48"); //("Could not read profile data");

			String ingredientsToWatch = null;

			int firstcount = 0;

			// Populates the Ingredients to Watch 
			for(int i=5 ; i<temp.length ; i++)
			{
				if(firstcount == 0)
				{
					ingredientsToWatch = temp[i].trim();
					firstcount++;
				}
				else
				{
					ingredientsToWatch += ", " + temp[i].trim();
				}
			}

			//Creates Male or Female object based on gender 
			switch(gender)
			{
			case "Male" : NutriByte.person = new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch); break;

			case "Female" : NutriByte.person = new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch); break;

			default: break;

			}
		}
		catch ( NullPointerException| NumberFormatException e )
		{
			validperson++;

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("NutriByte 3.0");
			alert.setHeaderText("Profile Data Error" );

			try 
			{
				if(!gender.equals("Male") && !gender.equals("Female")) throw new InvalidProfileException("The profile must have gender: female or  male as first word");

				else if(age == 0) throw new InvalidProfileException("Invalid data for Age: " + temp[1].trim() + "\n Age must be a number");	

				else if(weight == 0) throw new InvalidProfileException("Invalid data for Weight: " + temp[2].trim() + "\n Weight must be a number");	

				else if(height == 0) throw new InvalidProfileException("Invalid data for Height: " + temp[3].trim() + "\n Height must be a number");	
			}

			catch(InvalidProfileException e1)
			{
				alert.setContentText("Could not read profile data");
				alert.showAndWait();	
			}
		}
		catch(InvalidProfileException e1)
		{
			validperson++;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("NutriByte 3.0");
			alert.setHeaderText("Profile Data Error" );
			alert.setContentText("Could not read profile data");
			alert.showAndWait();			
		}

		//if there were no exceptions only then return the person object
		if(validperson == 0)
		{
			return NutriByte.person;
		}
		else
			return null;
	}


	public Product validateProductData(String data)
	{
		try {
			String[] temp = data.split(",");

			//checks if product ndbnumber is present in master products map
			if(!Model.productsMap.containsKey(temp[0].trim()))
			{
				NutriByte.view.productsComboBox.getSelectionModel().clearSelection();
                NutriByte.view.productSearchTextField.clear();
				NutriByte.view.nutrientSearchTextField.clear();
                NutriByte.view.ingredientSearchTextField.clear();
                NutriByte.view.productIngredientsTextArea.clear();
                NutriByte.view.productNutrientsTableView.getItems().clear();
                NutriByte.view.dietProductsTableView.getItems().clear();
                NutriByte.view.searchResultSizeLabel.setText("");
                NutriByte.view.servingSizeLabel.setText("0.00");
                NutriByte.view.householdSizeLabel.setText("0.00");
                NutriByte.view.dietServingUomLabel.setText("");
                NutriByte.view.dietHouseholdUomLabel.setText("");
                NutriByte.view.nutriChart.clearChart();
				
				throw new InvalidProfileException("No product found with this code " + temp[0]);
			}
				

			float servingsize = Float.parseFloat(temp[1].trim());
			float householdsize = Float.parseFloat(temp[2].trim());

			//creates a dummy product object to return
			Product product = new Product();

			//gets the original products object
			Product p  = Model.productsMap.get(temp[0].trim());

			//sets the required values
			product.setNdbNumber(p.getNdbNumber());
			product.setProductName(p.getProductName());		
			product.setManufacturer(p.getManufacturer());
			product.setIngredients(p.getIngrediants());
			product.setServingUom(p.getServingUom());
			product.setHouseholdUom(p.getHouseholdUom());
			product.setProductNutrients(p.getProductNutrients());

			product.setServingSize(servingsize);
			product.setHouseholdSize(householdsize);

			return product;

		}
		catch (InvalidProfileException | NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			try {
				if(e.getClass().getName().contains("NumberFormatException") || e.getClass().getName().contains("ArrayIndexOutOfBoundsException")) throw new InvalidProfileException("Cannot read: " + data + "\n The data must be - String, number, number - for ndb number,\n serving size, household size");
			}
			catch(InvalidProfileException e1)
			{}
		}
		return null;
	}
}