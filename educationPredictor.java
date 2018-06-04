import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.util.Arrays.*;
/**
* This program uses the K-NN algorithm to
* predict a user's education level based on
* hourly income.
* Data is taken from the Bureau of Labor Statistics
*/
public class educationPredictor{
	/**
	 * Main method creates Career objects and updates them with 
	 * relation to the user's data after collecting salary from 
	 * the user. It uses this to create an array of 
	 * similarity measures, which are mapped back
	 * to the careers to find their classification.
	 * Fills array with careers; uses that to fill
	 * array with similarity measurements. 
	 * This array is used in kNN function.
	 * The result of this function, an array
	 * of k similarity measures, is matched against
	 * the Career array to see which careers match 
	 * the similarity measures in the kNN array.
	*/
	public static void main(String[]args){
		
		double userSalary = 0;
		int numCareers = 6; //to be changed as needed
		int k = 3; //to be changed as needed
		int collegeTally = 0; //NN requires college
		int noCollegeTally = 0; //NN doesn't require college
		boolean isCollegeEducated = false;


		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your hourly salary: ");
		userSalary = scanner.nextDouble();
		Career aeroEngineer = new Career('Y', 51.84, 0); //0 is a placeholder since unable to self-reference 
    //Career salary data taken from BLS 
		Career registeredNurse = new Career('Y', 32.45, 0);
		Career programmer = new Career('Y', 38.24, 0);
		Career electrician = new Career('N', 24.94, 0);
		Career waitress = new Career('N', 9.25, 0);
		Career cook = new Career('N', 10.44, 0); 
		Career [] arrayOfCareers = {aeroEngineer, registeredNurse, programmer, electrician, waitress, cook};
		Career [] nearestNeighbors = new Career [k];
		double [] arrayOfSimMeasures = new double [numCareers];
		/* Update careers with similarity measure. */
		for (int i = 0; i < numCareers; i++){
			arrayOfCareers[i].updateSimilarity(Math.round((findSimilarity(arrayOfCareers[i], userSalary)*100000.0)/100000.0));
		}
		/* Fill array of similarity measures. */
		for (int i = 0; i < numCareers; i++){
			arrayOfSimMeasures[i] = arrayOfCareers[i].similarity;
		}

		double[] nearestNeighborsSimMeasures = getKNearestNeighbors(arrayOfSimMeasures, k); //contains k nearest neighbor similarity values

		//Iterate through the Career array; if any of them have the same similarity measure
		//as in the kNN array, then add them to the 
		// nearest neighbors Career array.
		for (int i = 0; i< numCareers; i++){
			for (int j = 0; j < k; j++){
				if (arrayOfCareers[i].similarity == nearestNeighborsSimMeasures[j]){
					nearestNeighbors[j] = arrayOfCareers[i];
				}
			}
		}

		//Look at nearest neighbors.
		//If collegeRequired == 'Y', collegeTally increases;
		//If collegeTally > noCollegeTally, classify the user
		//as college educated. Else, classify as not college educated.
		for (int i = 0; i < k; i++){
			if (nearestNeighbors[i].collegeRequired == 'Y'){
				collegeTally++;
			}
			else{
				noCollegeTally++;
			}
		}

		if(collegeTally > noCollegeTally){
			isCollegeEducated = true;
		}
		else{
			isCollegeEducated = false;
		}

		if (isCollegeEducated == true){
			System.out.println("You have at least a bachelor's degree.");
		}
		if (isCollegeEducated != true){
			System.out.println("You do not have a college degree.");
		}
		
	}

	/**
	 * Career objects to be used for classification.
	 * @param collegeRequired char 'Y' indicates yes, 'N' indicates no.
	 * @param hourlySalary double indicates salary
	 * @param similarity double indicates similarity between the career and user.
	 */
	public static class Career{
		char collegeRequired;
		double hourlySalary;
		double similarity;

		/**
		 * Updates the similarity between the career and the user
		 */
		public void updateSimilarity(double similarityMeasure){
			similarity = similarityMeasure;
		}
  
	    /*class constructor*/
		public Career (char collegeRequired, double hourlySalary, double similarity){
			this.collegeRequired = collegeRequired;
			this.hourlySalary = hourlySalary;
			this.similarity = similarity;
		}
		/** default constructor*/
		public Career(){
			this.collegeRequired = 'N';
			this.hourlySalary = 0.00;
			this.similarity = 0;
		}
	}
	/**
	 * Calculates similarity using Euclidean distance.
	 * @param career Career object 
	 */
	public static double findSimilarity(Career career, double userSalary){
		double similarity = 0;
		double squaredDiff = 0;
		double careerSalary = career.hourlySalary;
		squaredDiff = Math.pow((careerSalary - userSalary),2);
		similarity = Math.sqrt(squaredDiff);
		return similarity;
	}

	/**
	 * Sorts similarity measures from least to greatest;
	 * those closest to zero are most similar.
	 * @param similarityMeasures double [] created from careers
	 * @param k int of neighbors for classification purposes
	 */
	public static double [] getKNearestNeighbors(double [] similarityMeasures, int k){
		Arrays.sort(similarityMeasures);
		double [] kNNSimMeasures = new double [k];
		for (int i = 0; i < k; i++){
			kNNSimMeasures[i] = similarityMeasures[i];
		}
		return kNNSimMeasures;
	}

}
