package com.RecSys.Recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
This class contains contains our recommendation logic.
@author Robert
*/
public class MyRecommender {
	
	String ratedFileName;
	String mergedFileName; 
	
	public MyRecommender (String ratedFileName, String mergedFileName){
		this.ratedFileName = ratedFileName;
		this.mergedFileName = mergedFileName;
	}

	/**
	 * Sees if the recommendationsFile corresponds to the mergedFile, i.e. if the recommended buys reflect the actual buys. Corretly predicted buys are printed in a new file. Incorrecct ones are shown in the console
	 * @param recommendationsFileName path of the file with the recommendations
	 * @return
	 * @throws Exception
	 * @author Robert
	 */
	public String evaluateRecommendationsFile (String recommendationsFileName) throws Exception{
		
		String fileNameSubstring = recommendationsFileName.split("\\\\")[recommendationsFileName.split("\\\\").length-1];
		String outputFileName = recommendationsFileName.replace(fileNameSubstring, "Evaluated " + fileNameSubstring);
		
		FileInputStream recFile = new FileInputStream(new File(recommendationsFileName));
		BufferedReader recFileReader = new BufferedReader(new InputStreamReader(recFile));	
		FileInputStream mergedFile = new FileInputStream(new File(mergedFileName));
		BufferedReader mergedFileReader = new BufferedReader(new InputStreamReader(mergedFile));

		PrintWriter evalFile = new PrintWriter(outputFileName);
		
		String userId;
		String productId;
		boolean recommendationCorrect = false; 
		
		String line = recFileReader.readLine();
		
		while(line !=null){
			
			userId = line.split(";")[0];
			productId = line.split(";")[1];
			String lineMerged = mergedFileReader.readLine();
			
			while(lineMerged != null){
				
				//check if recommendation was right, i.e. if there have been buys for the combination of userid and productid
				if (lineMerged.split(",")[0].equals(userId) 
						&& lineMerged.split(",")[1].equals(productId)
						&& Integer.parseInt(lineMerged.split(",")[5])>0){
					
					evalFile.println(userId + ";" + productId);
					recommendationCorrect = true; 
				}
				lineMerged = mergedFileReader.readLine();
			}
			
			if (!recommendationCorrect){
				System.out.println(userId + " + " + productId + " not correct");
			}
			
			recommendationCorrect=false;
			line = recFileReader.readLine();
			}
			
		
		recFileReader.close();
		mergedFileReader.close();
		evalFile.close();
		
		return outputFileName; 
		
	}
	
	
	/**
	 * This method creates a file that contains a certain amount of recommended products for some users. Only those users appear in the file which correspond to the defined neighbourhood threshold in Recommender Builder.
	 * @return
	 * @param numberOfRecommendations how many recommendations per user
	 */
	public String createRecommendationsFile (int numberOfRecommendations) throws IOException, TasteException{
		
		String fileNameSubstring = ratedFileName.split("\\\\")[ratedFileName.split("\\\\").length-1];
		String outputFileName = ratedFileName.replace(fileNameSubstring, "Recommendations " + fileNameSubstring);
		
		DataModel model = new FileDataModel(new File(ratedFileName));
		 
		MyRecommenderBuilder builder = new MyRecommenderBuilder();
		UserBasedRecommender recommender = builder.buildRecommender(model);
		 
		FileInputStream ratedFile = new FileInputStream(new File(ratedFileName));
		BufferedReader ratedFileReader = new BufferedReader(new InputStreamReader(ratedFile));	
		PrintWriter recFile = new PrintWriter(outputFileName);
		String currentUser = "";
		String lastUser="";
		 
		String line = ratedFileReader.readLine();
		
		while (line != null) {
			
			currentUser= line.split(",")[0];
			
			if (!lastUser.equals(currentUser)){
				
				List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(currentUser), numberOfRecommendations	);
				 
				 for (RecommendedItem recommendation : recommendations) {
					 recFile.println(currentUser + ";" + recommendation.getItemID());
				 }
			}
			
			lastUser=currentUser;
			line = ratedFileReader.readLine();
		 }
		 ratedFileReader.close();
		 recFile.close();
		
		return outputFileName;
	}
	
	
	/**
	 * This Method prints the recommendations based on the input file and the parameters that are defined in the inner class MyRecommenderBuilder
	 * @param ratedFileName The path of the file that contains the sessionid, productid and ratings
	 * @author Daniel and Robert
	 */
	public void printRecommendations () throws IOException, TasteException{
		
		 DataModel model = new FileDataModel(new File(ratedFileName));
		 
		 MyRecommenderBuilder builder = new MyRecommenderBuilder();
		 UserBasedRecommender recommender = builder.buildRecommender(model);
		 
		 FileInputStream ratedFile = new FileInputStream(new File(ratedFileName));
		 BufferedReader ratedFileReader = new BufferedReader(new InputStreamReader(ratedFile));		
		 String line = ratedFileReader.readLine();
		
		 while (line != null) {
			 List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(line.split(",")[0]), 1);
			 
			 for (RecommendedItem recommendation : recommendations) {
				 System.out.println("Recommendation for " + line.split(",")[0] + ":" + recommendation);
			 }
			
			 line = ratedFileReader.readLine();
		 }
		 ratedFileReader.close();
	}
	
	/**
	 * Returns the evaluation of the recommender that is defined in the inner Class MyRecommenderBuilder
	 * @param ratedFileName The path of the file that contains the sessionid, productid and ratings
	 * @return a double representing the average abolute difference between the actual rating and the prediction
	 * @throws IOException
	 * @throws TasteException
	 * @Author Robert
	 */
	public double evaluateRecommender() throws IOException, TasteException {
		
		DataModel model = new FileDataModel(new File(ratedFileName));
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		return result;
		
	}
	
	/**
	 * This inner class builds the recommender
	 * @author Robert
	 *
	 */
	static class MyRecommenderBuilder implements RecommenderBuilder{
		
		/**
		 * This method builds the recommender
		 * @author Robert
		 */
		public GenericUserBasedRecommender buildRecommender (DataModel dataModel) throws TasteException{
			
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.2, similarity, dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		}
	}
}
