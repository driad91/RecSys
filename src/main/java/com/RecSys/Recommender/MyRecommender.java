package com.RecSys.Recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	/**
	 * This Method prints the recommendations based on the input file and the parameters that are defined in the inner class MyRecommenderBuilder
	 * @param ratedFileName The path of the file that contains the sessionid, productid and ratings
	 * @author Daniel and Robert
	 */
	public static void printRecommendations (String ratedFileName) throws IOException, TasteException{
		
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
	 */
	public static double evaluateRecommender(String ratedFileName) throws IOException, TasteException {
		
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
