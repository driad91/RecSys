package com.RecSys.Recommender;

import java.io.*;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Hello world!
 *
 */
public class App {

	@SuppressWarnings("null")
	public static void getIntersectionClickBuySession() throws Exception {

		// DataModel buysModel = new FileDataModel(new
		// File("C:\\Users\\Daniel\\Desktop\\Yoochoose dataset\\yoochoose-buys.dat"));
		// DataModel clicksModel = new FileDataModel(new
		// File("C:\\Users\\Daniel\\Desktop\\Yoochoose dataset\\yoochoose-clicks.dat"));
		//
		//
		// System.out.println(buysModel.getNumUsers());
		//

		String buyFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\yoochoose-buys.dat";
		String clickFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\yoochoose-clicks.dat";
		String mergedFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\merged.dat";

		//
		// try {
		//
		// File buyFile = new File(buyFileName);
		// FileReader fileReader = new FileReader(buyFileName);
		// BufferedReader bufferedReader = new BufferedReader(fileReader);
		// StringBuffer stringBuffer = new StringBuffer();
		// String buyLine;
		// while ((buyLine = bufferedReader.readLine()) != null) {
		// stringBuffer.append(buyLine);
		// stringBuffer.append("\n");
		// }
		//
		// File clickFile = new File(clickFileName);
		// FileReader fileReaderTwo = new FileReader(clickFileName);
		// BufferedReader bufferedReaderTwo = new BufferedReader(fileReaderTwo);
		// StringBuffer stringBufferTwo = new StringBuffer();
		// String clickLine;
		// while ((clickLine = bufferedReaderTwo.readLine()) != null) {
		// stringBufferTwo.append(clickLine);
		// stringBufferTwo.append("\n");
		// }
		//
		// @SuppressWarnings("null")
		// String []buyFileLines = buyLine.split("\n");
		// String []clickFileLines=clickLine.split("\n");
		// for ( String line : buyFileLines) {
		// for(String clickLineTwo: clickFileLines){
		//
		// if (line.split(",")[0].equals((clickLineTwo.split(",")[0])))
		// {
		// System.out.println(line.split(",")[0] + line.split(",")[2]);
		//
		// }
		//
		// }
		// }
		// }
		// catch (IOException e) {
		// System.out.println(e.getMessage());
		// e.printStackTrace();
		// }

		FileReader buyFile = new FileReader(new File(buyFileName));
		BufferedReader brBuy = new BufferedReader(buyFile);
		FileInputStream clickFile = new FileInputStream(new File(clickFileName));
		BufferedReader brClick = new BufferedReader(new InputStreamReader(
				clickFile));

		PrintWriter mergedFile = new PrintWriter(mergedFileName);

		String tempBuy = brBuy.readLine();
		String tempClick = brClick.readLine();

		while (tempBuy != null) {
			String[] tempBuyArr = tempBuy.split(",");
			clickFile.getChannel().position(0);
			brClick = new BufferedReader(new InputStreamReader(clickFile));
			tempClick = brClick.readLine();

			while (tempClick != null) {
				String[] tempClickArr = tempClick.split(",");

				if (tempClickArr[0].equals(tempBuyArr[0])) {
					System.out.println("SessionID = " + tempClickArr[0]
							+ " and Click ProductID = " + tempClickArr[2]
							+ "and Buy Product ID" + tempBuyArr[2]);
					mergedFile.println(tempClickArr[0] + "," + tempClickArr[1]
							+ "," + tempClickArr[2] + "," + tempClickArr[3]
							+ "," + tempBuyArr[0] + "," + tempBuyArr[1] + ","
							+ tempBuyArr[2] + "," + tempBuyArr[3] + ","
							+ tempBuyArr[4]);
					mergedFile.close();
					return;
				}
				tempClick = brClick.readLine();
			}
			// brClick.close();
			tempBuy = brBuy.readLine();
		}
		// brBuy.close();
	}

	public static void main(String[] args) throws Exception {

		String startDir = System.getProperty("user.dir");

		String clicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\reduced1000th.csv";
		String buysFileName = startDir
				+ "\\data\\YooChoose Dataset\\yoochoose-buys.dat";
		String sortedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted reduced1000th.csv";
		String sortedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted yoochoose-buys.dat";
		String aggregatedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated reduced1000th NO TIME.csv";
		String aggregatedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated buys NO TIME.dat";
		String mergedFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated merged1000th NO TIME.csv";
		String ratedFileName =  startDir
				+ "\\data\\YooChoose Dataset\\Rated Sorted Aggregated merged1000th NO TIME.csv";
		
		//		processData.reduceDataset(1000);
//		 processData.sortFile(clicksFileName);
//		 processData.sortFile(buysFileName);	
//		 processData.aggregateClicks(sortedClicksFileName, aggregatedClicksFileName);
		// processData.aggregateBuys(sortedBuysFileName, aggregatedBuysFileName);
//		 processData.joinDatasets(aggregatedClicksFileName, aggregatedBuysFileName, mergedFileName);
//		processData.convertToRatings(mergedFileName);

		// processData.aggregateBuys();

		// processData.reduceDataset();

		// processData.aggregateClicks();

		// processData.joinDatasets();

		// getIntersectionClickBuySession();

		// ---------------------------------

 
//		 MyRecommender.printRecommendations (ratedFileName); 
		 
		 double accuracy = MyRecommender.evaluateRecommender(ratedFileName);
		 System.out.println(accuracy);
		 
	}
}
