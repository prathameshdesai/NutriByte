package hw3;

//PRATHAMESH DESAI  psdesai

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import hw3.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {

	static ObservableMap<String,Product> productsMap = FXCollections.observableHashMap(); // An ObservableMap with ndbNumber as the key and the Product object as value
	static ObservableMap<String,Nutrient> nutrientsMap = FXCollections.observableHashMap(); // An ObservableMap with nutrientCode as the key and the Nutrient object as value

	ObservableList<Product> searchResultsList = FXCollections.observableArrayList(); // searchlist to store the products searched

	public void readProducts(String productFile) {

		// Using the CSV Formatter to read the Products File and populate the productsMap
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(productFile), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				Product product = new Product(csvRecord.get(0), csvRecord.get(1),
						csvRecord.get(4), csvRecord.get(7));
				productsMap.put(csvRecord.get(0), product);
			}
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }	
	}

	public void readNutrients(String nutrientFile) {

		// Using the CSV Formatter to read the Nutrients File and populate the nutrientsMap
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(nutrientFile), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				nutrientsMap.put(csvRecord.get(1), new Nutrient(csvRecord.get(1), csvRecord.get(2), csvRecord.get(5)));

				//Checks in the productsMap to load only Unique Nutrients in the nutrientsMap
				if(productsMap.containsKey(csvRecord.get(0)))
				{
					Product pr = productsMap.get(csvRecord.get(0));
					if(!(Float.parseFloat(csvRecord.get(4)) == 0))
					{
						ProductNutrient pNut = pr.new ProductNutrient(csvRecord.get(1), Float.parseFloat(csvRecord.get(4)));
						ObservableMap<String, ProductNutrient> productNutrients = pr.getProductNutrients();
						productNutrients.put(csvRecord.get(1), pNut);
					}
				}
			}
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }	
	}



	public void readServingSizes(String servingSizeFile) {

		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(servingSizeFile), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				//Checks in the productsMap to match the key and load servingsize data for that particular product
				if(productsMap.containsKey(csvRecord.get(0)))
				{
					Product product = productsMap.get(csvRecord.get(0));

					if(!csvRecord.get(1).isEmpty())		
					{
						product.setServingSize(Float.parseFloat(csvRecord.get(1)));
						}

					product.setServingUom(csvRecord.get(2));

					if(!csvRecord.get(3).isEmpty())
					{
					product.setHouseholdSize(Float.parseFloat(csvRecord.get((3))));
					}

					product.setHouseholdUom(csvRecord.get(4));
				}
			}

		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }
		
		

	}


	public boolean readProfile(String filename)
	{

		// Checks if the file is a CSV file and sends the program to childclass
		if(filename.contains(".csv"))
		{
			DataFiler df = new CSVFiler();
			boolean readsuccessfully = df.readFile(filename);
			return readsuccessfully;
		}

		// Checks if the file is a XML file and sends teh program to childclass
		if(filename.contains(".xml"))
		{
			DataFiler df = new XMLFiler();
			boolean readsuccessfully = df.readFile(filename);
			return readsuccessfully;

		}
		return false;
	}

	//passes the program flow to writefile method of CSVFiler
	void writeProfile(String filename)
	{
		CSVFiler c = new CSVFiler();
		c.writeFile(filename);
	}		
}
