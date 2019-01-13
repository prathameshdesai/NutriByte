package hw3;

//PRATHAMESH DESAI  psdesai

public class Male extends Person{
	float[][] nutriConstantsTableMale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.73f, 0.8f, 0.8f, 0.8f}, //Protein
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //Carbohydrate
		{19, 19, 19, 19, 25, 31, 38, 38, 38, 30},       //Fiber 
		{36, 36, 32, 21, 16, 17, 15, 14, 14, 14	},  //Histidine
		{88, 88, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//isoleucine
		{156, 156, 93, 63, 49, 49, 47, 42, 42, 42},//leucine
		{107, 107, 89, 58, 46, 46, 43, 38, 38, 38 },//lysine
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//methionine 
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//cysteine
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//phenylalanine 
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//tyrosine
		{73, 73, 49, 32, 24, 24, 22, 20, 20, 20}, 	//threonine
		{28, 28, 13, 8, 6, 6, 6, 5, 5, 5}, 			//tryptophan
		{87, 87, 58, 37, 28, 28, 27, 24, 24, 24}  	//valine
	};


	//Non-Default Constructor
	Male(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
		super( age,  weight,  height,  physicalActivityLevel,  ingredientsToAvoid);
		initializeNutriConstantsTable();
	}

	@Override
	float calculateEnergyRequirement() {

		//Checks if age group falls between first 4 types
		if(ageGroup.getAgeGroupIndex() < 4)
		{
			float[] constant = {-75, 44, 78, 80};

			return  89 * weight - constant[ageGroup.getAgeGroupIndex()];

		}

		//Checks if age group falls between type 5-7
		if(ageGroup.getAgeGroupIndex() < 7)
		{
			float[] constant = {88.5f, 61.9f, 26.7f, 903};

			return  constant[0] - constant[1] * age + physicalActivityLevel * (constant[2]*weight + constant[3]*height/100) + 20;
		}

		//Checks if age group falls between 8-10
		if(ageGroup.getAgeGroupIndex() < 10)
		{
			float[] constant = {662, 9.53f, 15.91f, 539.6f};

			return  constant[0] - (constant[1] * age) + physicalActivityLevel * (constant[2]*weight + constant[3]*height/100);

		}

		return 0;
	}

	//Initializes Parent's nutriConstantsTable
	@Override
	void initializeNutriConstantsTable() {

		nutriConstantsTable = nutriConstantsTableMale;

	}
}
