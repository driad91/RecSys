package com.RecSys.Recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class processData {

	public static void reduceDataset () throws Exception {

		/* to create a dataset that contains every xth session 
			--> change string reduced file name
			--> change line 37 (if counter != x)
			--> change line 45 (if counter == x)
		
		
		*/
		
		String clickFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\yoochoose-clicks.dat";
		String reducedFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\reduced10th.dat";
		String lastSession = ""; 
		
		
		int counter=0; 

		FileInputStream clickFile= new FileInputStream(new File(clickFileName));
		BufferedReader brClick = new BufferedReader(new InputStreamReader(clickFile));
		PrintWriter reducedFile = new PrintWriter (reducedFileName);

		String tempClick = brClick.readLine();

		while (tempClick != null) {		

			String[] tempClickArr=tempClick.split(",");		

			if (!lastSession.equals(tempClickArr[0])){
				if (counter!=10){
					counter++;
				}else{
					counter=0;
				}
			}

			if (counter==10){						
				reducedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3]);
			}
			
			lastSession = tempClickArr[0];
			tempClick=brClick.readLine();
		}
		brClick.close();
		reducedFile.close();
		
	}
	public static String convertFrom2dArrayToFile(String [][] arrayFile, String fileName) throws Exception
	{
		String startDir = System.getProperty("user.dir");
		
		
		String sortedFileName=startDir+"\\data\\YooChoose Dataset\\Sorted " + fileName.split("\\\\")[fileName.split("\\\\").length-1];

		PrintWriter sortedFile= new PrintWriter (sortedFileName);


		for (int i=0;i<arrayFile.length;i++)
		{
		   sortedFile.println(arrayFile[i][0]+","+arrayFile[i][1]+","+arrayFile[i][2]+"," +arrayFile[i][3]);
		   
		}

		sortedFile.close();
	return sortedFileName;
	}
	public static void aggregateBuys () throws Exception {
		
	  String buyFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\yoochoose-buys.dat";
	  String aggregatedFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\aggregatedBuysNOTIME.dat";
	  
	  String[] tempBuyArrNow;
	  String[] tempBuyArrLast;
	  int counter = 0; 
	  
	  FileInputStream buyFile= new FileInputStream(new File(buyFileName));
	  BufferedReader brBuy = new BufferedReader(new InputStreamReader(buyFile));

	  PrintWriter aggregatedFile = new PrintWriter (aggregatedFileName);
	  String tempBuy = brBuy.readLine();
	  tempBuyArrLast=tempBuy.split(",");

	  while (tempBuy !=null) {
		  tempBuyArrNow=tempBuy.split(",");

		  if (tempBuyArrNow[0].equals(tempBuyArrLast[0]) && tempBuyArrNow[2].equals(tempBuyArrLast[2])){
			  counter = counter + Integer.parseInt(tempBuyArrNow[4]);
		  }else{
			  aggregatedFile.println(tempBuyArrLast[0] + "," + tempBuyArrLast[2] + "," + tempBuyArrLast[3] + "," + counter);
			  counter = Integer.parseInt(tempBuyArrNow[4]);
		  }
		  tempBuyArrLast = tempBuyArrNow;
		  tempBuy=brBuy.readLine();
	  }
	  aggregatedFile.close();
	  brBuy.close();
	}

	
	public static void aggregateClicks () throws Exception {
		
		  String clickFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\reduced10000th.dat";
		  String aggregatedFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\aggregatedClicks10000thNOTIME.dat";
		  
		  String[] tempClickArrNow;
		  String[] tempClickArrLast;
		  int counter = 0; 
		  
		  FileInputStream clickFile= new FileInputStream(new File(clickFileName));
		  BufferedReader brClick = new BufferedReader(new InputStreamReader(clickFile));

		  PrintWriter aggregatedFile = new PrintWriter (aggregatedFileName);
		  String tempClick = brClick.readLine();
		  tempClickArrLast=tempClick.split(",");

		  while (tempClick !=null) {
			  tempClickArrNow=tempClick.split(",");

			  if (tempClickArrNow[0].equals(tempClickArrLast[0]) && tempClickArrNow[2].equals(tempClickArrLast[2])){
				  counter++;
			  }else{
				  aggregatedFile.println(tempClickArrLast[0] + "," + tempClickArrLast[2] + "," + tempClickArrLast[3] + "," + counter);
				  counter = 1;
			  }
			  tempClickArrLast = tempClickArrNow;
			  tempClick=brClick.readLine();
		  }
		  aggregatedFile.close();
		  brClick.close();
		}
	
	public static String  sortFile (String fileName)throws Exception
	{
		
		String[][] arrayFile=ConvertFileTo2dArray(fileName);
		arrayFile=sort2dArray(arrayFile);
		return  convertFrom2dArrayToFile(arrayFile, fileName);
		
		 
		
	}
	public static String[][] ConvertFileTo2dArray(String filePath) throws Exception
	{
		
		int noOfRows=getNoOfRows(filePath);
		String [][] arrayFile=new String[noOfRows][4];
    	
	    
    	
		  FileInputStream file= new FileInputStream(new File(filePath));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  String line=brFile.readLine();
		  
		  int arrayLine=0;
		  
		  
		 while (line!=null)
			 
		 {
			 arrayFile[arrayLine]=line.split(",");
			 
			 line=brFile.readLine();
		
			 arrayLine++;
			 
		 }
		 
		 
		 
		 
		 
		 return arrayFile;
		 
	}
	public static int getNoOfRows(String fileName) throws Exception
	{
		  FileInputStream file= new FileInputStream(new File(fileName));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  String line=brFile.readLine();
		  
		  int lineCounter=0;
		  
		  while(line!=null)
		  {
			  lineCounter+=1;
			  
			 line= brFile.readLine();
			  
			  
		  }
		  return lineCounter;
	}
	public static String [][] sort2dArray(String [][] file){
	 Arrays.sort(file, new Comparator<String[]>() {
		 public int compare(String[] array1, String[] array2) {
			  int x = Integer.valueOf(array1[0]);
		        int j = Integer.valueOf(array2[0]);
		        return Integer.compare(x, j);
		    }
	    });
	

	return file;
	
	}
	
	public static void joinDatasets(String buyFileName,String clickFileName) throws Exception{
			
		
//		  String buyFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\aggregatedBuysNOTIME.dat";
//		  String clickFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\\\ProcessedDatasets\\aggregatedClicks10000thNOTIME.dat";
		String startDir = System.getProperty("user.dir");
		  String mergedFileName = startDir+ "\\data\\YooChoose Dataset\\merged10000th.dat";
		  
		  FileReader clickFile = new FileReader(new File(clickFileName));
		  BufferedReader brClick = new BufferedReader(clickFile);
		  FileInputStream buyFile= new FileInputStream(new File(buyFileName));
		  BufferedReader brBuy = new BufferedReader(new InputStreamReader(buyFile));

		  PrintWriter mergedFile = new PrintWriter (mergedFileName);

		  boolean entryFound = false; 
		  int counter=0;
		  String tempBuy;
		  String tempClick = brClick.readLine();

		  while (tempClick !=null) {
			  String[] tempClickArr=tempClick.split(",");
			  buyFile.getChannel().position(0);
			  tempBuy=brBuy.readLine();

			  while (tempBuy != null) {
				  String[] tempBuyArr=tempBuy.split(",");	

				  //if sessionid and itemid are same, print sessionid, itemid, category, clicks, price, quantity
				  if (tempBuyArr[0].equals(tempClickArr[0]) && tempBuyArr[1].equals(tempClickArr[1])) {		  
					  mergedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3] + ","
							  + tempBuyArr[2] + "," + tempBuyArr[3]);
					  entryFound = true; 
				  }
				  tempBuy=brBuy.readLine();
			  }

			  if (!entryFound){
				  mergedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3] + ",0,0");
			  }
			  entryFound = false;
			  tempClick = brClick.readLine();
			  
			  counter++;
			  if (counter%100==0){
				  System.out.println(counter);
			  }
		  }
		  brClick.close();
		  brBuy.close();
		  mergedFile.close();
	}
}