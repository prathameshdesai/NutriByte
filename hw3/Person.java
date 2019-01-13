package hw3;


import java.util.Set;

//PRATHAMESH DESAI  psdesai

import hw3.NutriProfiler.NutriEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();  

	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();

	ObservableMap<String, RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();


	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();

	//remove this default constructor once you have defined the child's constructor

	//Non-Default Constructor
	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {

		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;

		for(NutriProfiler.AgeGroupEnum group : NutriProfiler.AgeGroupEnum.values())
		{
			if(age < group.getAge())
			{
				this.ageGroup = group;
				break;
			}
		}

	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the 
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc. 


	//Uses the nutriConstantsTable populated by the Male or Female class to determine the constant factor
	//and calculates Nutrient Requirement on basis of that.
	float[] calculateNutriRequirement() {

		float[] nutriRequirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];

		for(NutriProfiler.NutriEnum nutri :  NutriEnum.values())
		{
			if(nutri.equals(nutri.PROTEIN))
			{

				nutriRequirement[nutri.getNutriIndex()] =   weight * nutriConstantsTable[nutri.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}
			else if((nutri.equals(nutri.CARBOHYDRATE)||(nutri.equals( nutri.FIBER))))
			{
				nutriRequirement[nutri.getNutriIndex()] =  nutriConstantsTable[nutri.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}
			else
			{
				nutriRequirement[nutri.getNutriIndex()] =  weight * nutriConstantsTable[nutri.getNutriIndex()][ageGroup.getAgeGroupIndex()]/1000;
			}
		}

		return nutriRequirement;
	}



	void populateDietNutrientsMap()
	{
		//Clears the map for any previous values
       dietNutrientsMap.clear();

       //Takes each product in dietProductsList
		for(Product p : dietProductsList)
		{
			Set<String> Nutrientkeys  =	p.getProductNutrients().keySet();
			
			//takes all the keys in productnutrients map and checks if dietNutrientsMap already has that nutrient or not and adds the quantities to previous values
			for(String key : Nutrientkeys)
			{
				if(dietNutrientsMap.containsKey(key))
				{
					float multipyingfactor  =  p.getServingSize()/100;
					float actualquantity = p.getProductNutrients().get(key).getNutrientQuantity() * multipyingfactor;
					dietNutrientsMap.get(key).setNutrientQuantity(dietNutrientsMap.get(key).getNutrientQuantity() +  actualquantity);
				}
				else
				{
					float multipyingfactor  =  p.getServingSize()/100;
					float actualquantity = p.getProductNutrients().get(key).getNutrientQuantity() * multipyingfactor;
					RecommendedNutrient reco = new RecommendedNutrient(key, actualquantity);
					dietNutrientsMap.put(key, reco);
				}
			}
		}
	}
}
