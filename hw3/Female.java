package hw3;

//PRATHAMESH DESAI  psdesai

public class Female extends Person {

	float[][] nutriConstantsTableFemale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.71f, 0.8f, 0.8f, 0.8f}, //0: Protein constants
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //1: Carbohydrate
		{19, 19, 19, 19, 25, 26, 26, 25, 25, 21},  //2: Fiber constants
		{36, 36, 32, 21, 16, 15, 14, 14, 14, 14}, 	//3: Histidine
		{88, 88, 43, 28, 22, 21, 19, 19, 19, 19}, 	//4: isoleucine
		{156, 156, 93, 63, 49, 47, 44 , 42, 42, 42},//5: leucine
		{107, 107, 89, 58, 46, 43, 40, 38, 38, 38}, //6: lysine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//7: methionine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//8: cysteine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //9: phenylalanine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //10: phenylalanine
		{73, 73, 49, 32, 24, 22, 21, 20, 20, 20}, 	//11: threonine
		{28, 28, 13, 8, 6, 6, 5, 5, 5, 5}, 			//12: tryptophan
		{87, 87, 58, 37, 28, 27, 24, 24, 24, 24	}  	//13: valine
	};

	//Non-Default Constructor
	Female(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
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
			float[] constant = {135.3f, 30.8f, 10f, 934};

			return  constant[0] - (constant[1] * age) + physicalActivityLevel * (constant[2]*weight + constant[3]*height/100) + 20;

		}

		//Checks if age group falls between 8-10
		if(ageGroup.getAgeGroupIndex() < 10)
		{
			float[] constant = {354, 6.91f, 9.36f, 726};

			return  constant[0] - (constant[1] * age) + physicalActivityLevel * (constant[2]*weight + constant[3]*height/100);

		}

		return 0;
	}

	//Initializes Parent's nutriConstantsTable
	@Override
	void initializeNutriConstantsTable() {

		nutriConstantsTable = nutriConstantsTableFemale;

	}
}
