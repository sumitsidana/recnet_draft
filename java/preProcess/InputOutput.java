package preProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class InputOutput {

	public static <K, V extends Comparable<? super V>> Map<K, V> 
	sortByValue( Map<K, V> map )
	{
		List<Map.Entry<K, V>> list =
				new LinkedList<>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return ( o2.getValue() ).compareTo( o1.getValue() );
			}
		} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}

	public static void getInteger(String inputFile,String outputFile) throws IOException{
		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replace("mv_", "").replace(".txt", "");
				line = StringUtils.stripStart(line,"0");
				printWriterTrain.println(line);
				// TODO Auto-generated method stub
			}
			printWriterTrain.close();
			br.close();
		}

	}

	public static void date2timeStamp(String inputFile,String outputFile) throws IOException, ParseException{
		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String date_Temp = array[array.length-1];
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = dateFormat.parse(date_Temp);	
				Date date = new java.sql.Date(utilDate.getTime());	
				long time = date.getTime();
				//				Timestamp timestamp = new Timestamp(time);
				for(int i = 0 ; i < array.length -1 ; i++)
					printWriterTrain.print(array[i]+",");
				printWriterTrain.println(time);
				// TODO Auto-generated method stub
			}
			printWriterTrain.close();
			br.close();
		}

	}

	public static void ignoreSingleRatingUsers(String inputFile1,String inputFile2,String outputFile,String delimiter) throws IOException{
		Set<String>singleRatingUsers = new HashSet<String>();
		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile1)))) {
			String line;
			while ((line = br.readLine()) != null) {
				singleRatingUsers.add(line);
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile2)))) {
			String line;
			while ((line = br.readLine()) != null) {
				String [] array = line.split(delimiter);
				if(singleRatingUsers.contains(array[0])) //expects userId at 1st position
					continue;
				printWriterTrain.println(line);
			}
			printWriterTrain.close();
			br.close();
		}
	}
	public static void replaceBinaryRatingType(String inputFile,String outputFile) throws IOException{
		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replace(",1,", ",4,");
				line = line.replace(",0,", ",1,");
				printWriterTrain.println(line);
			}
			printWriterTrain.close();
			br.close();
		}
	}

	public static void replaceExplicitRatingType(String inputFile,String outputFile) throws IOException{
		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			line = br.readLine(); //Expects Headers
			printWriterTrain.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				if(Double.parseDouble(array[2])>=4.0){
					printWriterTrain.println(array[0]+","+array[1]+","+"4"+","+array[3]);	
				}
				else{
					printWriterTrain.println(array[0]+","+array[1]+","+"1"+","+array[3]);
				}
			}
			printWriterTrain.close();
			br.close();
		}
	}

	//	public static void writeTabSeparatedFile(String inputFile, String outputFile, String header) throws IOException{
	//		String line;
	//		PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
	//		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
	//			line = br.readLine();
	//			printWriter.println(header);
	//			while ((line = br.readLine()) != null) {
	//				String [] array = line.split(",");
	//				String useridclicks = array[0];
	//				for(int i = 2  ; i  < array.length ; i++){
	//					if(i>=array.length - 4){
	//						continue;
	//					}
	//					printWriter.print(array[i]+",");
	//				}
	//				if(useridclicks.equals("null")){
	//					printWriter.print("0,");
	//				}
	//				else{
	//					printWriter.print("1,");
	//				}
	//				String timeStamp = array[array.length-1];
	//				java.sql.Timestamp ts = java.sql.Timestamp.valueOf(timeStamp);
	//				long tsTime = ts.getTime();
	//				printWriter.println(tsTime);
	//
	//			}
	//			// TODO Auto-generated method stub
	//		}
	//		printWriter.close();
	//
	//	}

	public static void writeTabSeparatedFile(String inputFile, String outputFile, String header) throws IOException{
		String line;
		PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			line = br.readLine();
			printWriter.println(header);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String useridclicks = array[0];
				for(int i = 2  ; i  < array.length - 1 ; i++){
					//					if(i==array.length - 2||i==array.length-4){
					//						continue;
					//					}
					printWriter.print(array[i]+"\t");
				}
				if(useridclicks.equals("null")){
					printWriter.print("1\t");
				}
				else{
					printWriter.print("4\t");
				}
				String timeStamp = array[array.length-1];
				printWriter.println(timeStamp);
			}
			// TODO Auto-generated method stub
		}
		printWriter.close();

	}

	public static void removeBrackets(String inputFile, String outputFile) throws IOException{

		PrintWriter printWriterTrain = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replace("[", "").replace("]", "");
				printWriterTrain.println(line);
				// TODO Auto-generated method stub
			}
			printWriterTrain.close();
			br.close();
		}

	}
	public static void milliseconds2seconds(String inputFile,String outputFile) throws NumberFormatException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile);
			line = br.readLine();
			printWriterTrain.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String milliseconds = array[3];
				double seconds = Double.parseDouble(milliseconds)/1000.0;
				String lineWritten = array[0]+","+array[1]+","+array[2]+","+seconds;
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
		}
	}

	public static void binarizeData(String inputFile,String outputFile) throws NumberFormatException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
			line = br.readLine();
			printWriter.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				int feedback = 0;
				double rating = Double.parseDouble(array[2]);
				if(rating>3)
					feedback = 2;
				else
					feedback = 1;
				String lineWritten = array[0]+","+array[1]+","+feedback+","+array[3];
				printWriter.println(lineWritten);
			}
			printWriter.close();
		}
	}


	public static void writeInputBPRMF(String inputFile, String outputFile) throws IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
			line = br.readLine();
			Map<Long,Map<Long,Long>> userItemRating = new LinkedHashMap<Long,Map<Long,Long>>();
			//			printWriter.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				int feedback = 0;
				long userId = Long.parseLong(array[0]);
				long itemId = Long.parseLong(array[1]);
				long rating = Long.parseLong(array[2]);
				if(userItemRating.containsKey(userId)){
					Map<Long,Long>itemRating = userItemRating.get(userId);
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
				else{
					Map<Long,Long>itemRating = new LinkedHashMap<Long,Long>();
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				Map<Long,Long> revSortedItemRating = sortByValue(itemRating);
				userItemRating.put(userId,revSortedItemRating);
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				printWriter.print(userId+"\t");
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				for(Map.Entry<Long,Long> internalEntry: itemRating.entrySet()){
					long itemId = internalEntry.getKey();
					long rating = internalEntry.getValue();
					printWriter.print(itemId+":"+rating+",");
				}
				printWriter.println();
			}

			printWriter.close();
		}

	}

	public static void writeTrainTestInputBPRMF(String inputFileTrain, String inputFileTest, String outputFileTrain, 
			String outputFileTest) throws IOException{
		Map<Long,Map<Long,Long>> userItemRating = new LinkedHashMap<Long,Map<Long,Long>>();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileTrain)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter(new BufferedWriter(new FileWriter(outputFileTrain, true)));
			line = br.readLine();

			//			printWriter.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				long userId = Long.parseLong(array[0]);
				long itemId = Long.parseLong(array[1]);
				long rating = Long.parseLong(array[2]);
				if(userItemRating.containsKey(userId)){
					Map<Long,Long>itemRating = userItemRating.get(userId);
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
				else{
					Map<Long,Long>itemRating = new LinkedHashMap<Long,Long>();
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				Map<Long,Long> revSortedItemRating = sortByValue(itemRating);
				userItemRating.put(userId,revSortedItemRating);
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				printWriterTrain.print(userId+"\t");
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				for(Map.Entry<Long,Long> internalEntry: itemRating.entrySet()){
					long itemId = internalEntry.getKey();
					long rating = internalEntry.getValue();
					printWriterTrain.print(itemId+":"+rating+",");
				}
				printWriterTrain.println();
			}

			printWriterTrain.close();
		}
		userItemRating.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileTest)))) {
			String line;
			PrintWriter printWriterTest = new PrintWriter(new BufferedWriter(new FileWriter(outputFileTest, true)));
			line = br.readLine();

			//			printWriter.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				long userId = Long.parseLong(array[0]);
				long itemId = Long.parseLong(array[1]);
				long rating = Long.parseLong(array[2]);
				if(userItemRating.containsKey(userId)){
					Map<Long,Long>itemRating = userItemRating.get(userId);
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
				else{
					Map<Long,Long>itemRating = new LinkedHashMap<Long,Long>();
					itemRating.put(itemId, rating);
					userItemRating.put(userId,itemRating);
				}
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				Map<Long,Long> revSortedItemRating = sortByValue(itemRating);
				userItemRating.put(userId,revSortedItemRating);
			}
			for(Map.Entry<Long, Map<Long,Long>> entry: userItemRating.entrySet()){
				long userId = entry.getKey();
				printWriterTest.print(userId+"\t");
				Map<Long,Long> itemRating = (LinkedHashMap<Long,Long>)entry.getValue();
				for(Map.Entry<Long,Long> internalEntry: itemRating.entrySet()){
					long itemId = internalEntry.getKey();
					long rating = internalEntry.getValue();
					printWriterTest.print(itemId+":"+rating+",");
				}
				printWriterTest.println();
			}
			printWriterTest.close();
		}

	}

	public static void stringIndexer(String inputFile,String outputFile) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile);
			Map<String,Long>userIndex = new LinkedHashMap<String,Long>();
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String user = array[0];
				long index;
				if(userIndex.containsKey(user)){
					index = userIndex.get(user);
				}
				else{
					index = userIndex.size()+1;
					userIndex.put(user, index);
				}
				String lineWritten = index+","+array[1]+","+array[2]+","+array[3];
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
		}

	}

	public static void stringIndexerWithIndex(String inputFile,String outputFile1, String outputFile2) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile1);
			PrintWriter printWriterIndex = new PrintWriter (outputFile2);
			Map<String,Long>userIndex = new LinkedHashMap<String,Long>();
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String user = array[0];
				long index;
				if(userIndex.containsKey(user)){
					index = userIndex.get(user);
				}
				else{
					index = userIndex.size()+1;
					printWriterIndex.println(user+","+index);
					userIndex.put(user, index);
				}
				String lineWritten = index+","+array[1]+","+array[2]+","+array[3];
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
			printWriterIndex.close();
		}

	}

	public static void offerStringIndexer(String inputFile,String outputFile) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile);
			//			line = br.readLine();
			//			printWriterTrain.println(line);
			Map<String,Long>offerIndex = new LinkedHashMap<String,Long>();

			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String offer = array[1];
				long index;
				if(offerIndex.containsKey(offer)){
					index = offerIndex.get(offer);
				}
				else{
					index = offerIndex.size()+1;
					offerIndex.put(offer, index);
				}
				String lineWritten = array[0]+","+index+","+array[2]+","+array[3];
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
		}

	}

	public static void offerStringIndexerWithIndex(String inputFile,String outputFile1, String outputFile2) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile1);
			PrintWriter printWriterIndex = new PrintWriter (outputFile2);
			//			line = br.readLine();
			//			printWriterTrain.println(line);
			Map<String,Long>offerIndex = new LinkedHashMap<String,Long>();

			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String offer = array[1];
				long index;
				if(offerIndex.containsKey(offer)){
					index = offerIndex.get(offer);
				}
				else{
					index = offerIndex.size()+1;
					printWriterIndex.println(offer+","+index);
					offerIndex.put(offer, index);
				}
				String lineWritten = array[0]+","+index+","+array[2]+","+array[3];
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
			printWriterIndex.close();
		}

	}

	public static void genreIndexer(String inputFile , String outputFile1, String outputFile2) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile1);
			PrintWriter printWriterTest = new PrintWriter (outputFile2);
			//			line = br.readLine();
			//			printWriterTrain.println(line);
			Map<String,Long>genreIndex = new LinkedHashMap<String,Long>();

			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String genre = array[1];
				long index;
				if(genreIndex.containsKey(genre)){
					index = genreIndex.get(genre);
				}
				else{
					index = genreIndex.size()+1;
					genreIndex.put(genre, index);
					printWriterTest.println(genre+","+index);
				}
				String lineWritten = array[0]+","+index;
				printWriterTrain.println(lineWritten);
			}
			printWriterTrain.close();
			printWriterTest.close();
		}
	}

	public static void writeGenresMovieLens(String inputFile, String outputFile) throws IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile);
			while ((line = br.readLine()) != null) {
				String [] array = line.split("::");
				String movie = array[0];
				String genres = array[2];
				String [] internalArray = genres.split("\\|");
				for(int i = 0 ; i < internalArray.length ; i++){
					String lineWritten = movie+","+internalArray[i];
					printWriterTrain.println(lineWritten);
				}
			}
			printWriterTrain.close();
		}
	}

	public static void removeDuplicatesInEachLine(String inputFile,String outputFile) throws IOException{

		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			PrintWriter printWriterTrain = new PrintWriter (outputFile);
			List<Integer> items = new ArrayList<Integer>();
			while ((line = br.readLine()) != null) {
				items = new ArrayList<Integer>();
				String [] array = line.split(" ");
				String user = array[0];
				printWriterTrain.print(user+" ");
				for(int i = 1 ; i < array.length ; i++){
					int item = Integer.parseInt(array[i]);
					if(items.contains(item)){
						continue;
					}
					else{
						printWriterTrain.print(item+" ");
						items.add(item);
					}
				}
				printWriterTrain.println();
			}
			printWriterTrain.close();
		}

	}

	//	public static void writeEvaluationFile(String inputFile, String outputFile, String evalMetric, String countriesList) throws IOException{
	//
	//		/*
	//		 * java -cp . postProcess.Operations 
	//		 * ../../data/output/fmcountryfilesv3/vectors/len1/em/evalMetrics 
	//		 * ../../data/output/fmcountryfilesv3/vectors/len1/em/dat.meanaverageprecision Mean\ Average\ Precision
	//		 */
	//		//"at","be","br","ch","cz","de","dk",
	//		// fr,it,nb
	//		List<String> countryCodes = Arrays.asList(countriesList.split(","));
	//		PrintWriter printWriter = new PrintWriter (outputFile);
	//		Map<String,Double>countryMetrics = new LinkedHashMap<String,Double>();
	//		for(int i = 0 ; i < countryCodes.size() ; i++){
	//			try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile+"_"+countryCodes.get(i))))) {
	//				String line;
	//				while ((line = br.readLine()) != null) {
	//					if(line.contains(evalMetric)){
	//						countryMetrics.put(countryCodes.get(i), Double.parseDouble(line.replace(evalMetric+": ", "")));
	//						//						printWriter.println(countryCodes.get(i)+" "+line.replace(evalMetric+": ", ""));
	//					}
	//				}
	//			}
	//		}
	//		Map<String,Double>reverseSortedMap = sortByValue(countryMetrics);
	//		for(Map.Entry<String,Double>entry:reverseSortedMap.entrySet()){
	//			printWriter.println(entry.getKey()+" "+entry.getValue());
	//		}
	//		printWriter.close();
	//	}

	public static void getRankedListGroundTruthForUserLightFM(String groundTruth, String inputFile, String ofGT, String ofPR) throws NumberFormatException, IOException{
		/*
		 *
		 */
		Map<String,Map<String,Double>> userOfferProbability = new LinkedHashMap<String,Map<String,Double>>();
		Map<String,Map<String,Long>> userOfferGroundTruth = new LinkedHashMap<String,Map<String,Long>>();

		BufferedReader gtBr = new BufferedReader(new FileReader(groundTruth));
		BufferedReader inBr = new BufferedReader(new FileReader(inputFile));
		Map<String,Map<String,Double>>revSortedUserOfferProbability = new LinkedHashMap<String,Map<String,Double>>();
		gtBr.readLine();
		while(true){
			String partOne = gtBr.readLine();
			String partTwo = inBr.readLine();
			if (partOne == null || partTwo == null)
				break;
			String [] array = partOne.split("\t");
			String user = array[0];
			String offer = array[1];
			String gt = "1";
			String pred = partTwo;

			if(userOfferGroundTruth.containsKey(user)){
				Map<String,Long>offerGroundTruth = userOfferGroundTruth.get(user);
				offerGroundTruth.put(offer, Long.parseLong(gt));
				userOfferGroundTruth.put(user, offerGroundTruth);			
			}
			else{
				Map<String,Long>offerGroundTruth = new LinkedHashMap<String,Long>();
				offerGroundTruth.put(offer, Long.parseLong(gt));		
				userOfferGroundTruth.put(user, offerGroundTruth);
			}

			if(userOfferProbability.containsKey(user)){
				Map<String,Double>offerProbability = userOfferProbability.get(user);
				if(offerProbability.containsKey(offer)){
					offerProbability.put(offer, Double.parseDouble(pred));
				}
				offerProbability.put(offer, Double.parseDouble(pred));
				userOfferProbability.put(user, offerProbability);
			}
			else{
				Map<String,Double>offerProbability = new LinkedHashMap<String,Double>();
				offerProbability.put(offer, Double.parseDouble(pred));
				userOfferProbability.put(user, offerProbability);
			}
		}

		for (Map.Entry<String, Map<String,Double>> entry : userOfferProbability.entrySet()) {
			String userId = entry.getKey();
			Map<String,Double> offerProbability = (LinkedHashMap<String, Double>) entry.getValue();
			Map<String,Double>revSortedOfferProbability = sortByValue(offerProbability);
			revSortedUserOfferProbability.put(userId,revSortedOfferProbability);
		}
		/*
		 * Write revSortedUserOfferProbability
		 * and
		 * UserOfferGroundTruth
		 */

		PrintWriter pWGT = new PrintWriter (ofGT);
		PrintWriter pWPR = new PrintWriter (ofPR);

		for(Entry<String, Map<String,Long>> entry: userOfferGroundTruth.entrySet()){
			String user = entry.getKey();
			pWGT.print(user+" ");
			Map<String,Long>offerGroundTruth =  entry.getValue();
			for(Entry<String,Long>innerEntry: offerGroundTruth.entrySet()){
				pWGT.print(innerEntry.getKey()+" ");
			}
			pWGT.println();
		}
		for(Entry<String, Map<String,Double>> entry: revSortedUserOfferProbability.entrySet()){
			String user = entry.getKey();
			pWPR.print(user+" ");
			Map<String,Double>offerProbability =  entry.getValue();

			for(Entry<String,Double>innerEntry: offerProbability.entrySet()){
				pWPR.print(innerEntry.getKey()+" ");
			}
			pWPR.println();
		}
		gtBr.close();
		inBr.close();
		pWGT.close();
		pWPR.close();
	}
	public static void writeEvaluationFile(String inputFile, String outputFile, String evalMetric)
			throws IOException{

		//List<String> countryCodes = Arrays.asList("0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9");
		//		List<String> countryCodes = Arrays.asList("10","20","30","50","75","100");
		//		List<String> countryCodes = Arrays.asList("1","2","3","5","7_5","10");

		List<String> countryCodes1 = Arrays.asList("ml100k","ml1m");
		//List<String> countryCodes1 = Arrays.asList("kasandr","ml100k","ml1m");
		//List<String> countryCodes2 = Arrays.asList("bprmf","cofactor","lightfm","recnet","pop");
		List<String> countryCodes2 = Arrays.asList("bprmf","cofactor","lightfm");
		//		List<String> countryCodes2 = Arrays.asList("cofactor");
		//		List<String> countryCodes5 = Arrays.asList("atleast_five_o_two_c", "atleast_ten_o_two_c",  "atleast_ten_o_two_c_v2",  "default");
		List<String> countryCodes3 = Arrays.asList("one","five","ten");
		List<String> countryCodes4 = Arrays.asList("01","10","11");


		PrintWriter printWriter = new PrintWriter (outputFile);
		Map<String,Double>countryMetrics = new LinkedHashMap<String,Double>();
		for(int i = 0 ; i <countryCodes1.size() ; i++){
			for(int j = 0 ; j < countryCodes2.size() ; j++){
				for(int k = 0; k < countryCodes3.size(); k++){
					for(int l = 0 ; l < countryCodes4.size() ; l++){
						if(!(countryCodes2.get(j).equals("recnet"))){
							try (BufferedReader br = new BufferedReader
									(new FileReader(new File(inputFile+"/"+countryCodes1.get(i)+"/"+
											"/"+countryCodes2.get(j)+
											"/"+countryCodes3.get(k)
											+"/em/evalMetrics_"+ countryCodes1.get(i))))) {
								String line;
								while ((line = br.readLine()) != null) {
									if(line.contains(evalMetric)){
										countryMetrics.put(countryCodes1.get(i)+countryCodes2.get(j)+countryCodes3.get(k)
										,
										Double.parseDouble(line.replace(evalMetric+": ", "")));
									}
								}
							}
						}
						else{
							try (BufferedReader br = new BufferedReader
									(new FileReader(new File(inputFile+"/"+countryCodes1.get(i)+"/"+countryCodes2.get(j)+
											"/"+countryCodes3.get(k)
											+"/em/evalMetrics_"+ countryCodes1.get(i)+"_"+countryCodes4.get(l))))) {
								String line;
								while ((line = br.readLine()) != null) {
									if(line.contains(evalMetric)){
										countryMetrics.put(countryCodes1.get(i)+countryCodes2.get(j)+countryCodes3.get(k)+
												countryCodes4.get(l),
												Double.parseDouble(line.replace(evalMetric+": ", "")));
										//						printWriter.println(countryCodes.get(i)+
										//								" "+line.replace(evalMetric+": ", ""));
									}
								}
							}
						}
					}

				}
			}

		}
		//		Map<String,Double>reverseSortedMap = sortByValue(countryMetrics);
		//		for(Map.Entry<String,Double>entry:reverseSortedMap.entrySet()){
		//			printWriter.println(entry.getKey()+" "+entry.getValue());
		//		}
		//		Map<String,Double>reverseSortedMap = sortByValue(countryMetrics);
		for(Map.Entry<String,Double>entry:countryMetrics.entrySet()){
			printWriter.println(entry.getKey()+" "+entry.getValue());
		}

		printWriter.close();
	}
	public static void mergeFilesOrderInputFile2(String inputFile1,String inputFile2,String outputFile) throws NumberFormatException, IOException{
		/*
		 * Use Case:
		 * java -cp . postProcess.Operations ~/thesis_project/trunk/figs/gnuplot/dat.meanaverageprecisionfm 
		 * ~/thesis_project/trunk/figs/gnuplot/dat.MAPPastInteractionsPopularity 
		 * ~/thesis_project/trunk/figs/gnuplot/dat.mapfmpastinteractionspopularity
		 */
		PrintWriter printWriter = new PrintWriter (outputFile);
		Map<String,Double>countryValue = new TreeMap<String,Double>();
		System.out.println(inputFile1);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile1))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String [] array = line.split(" ");
				String countryCode = array[0];
				double val = Double.parseDouble(array[1]);
				countryValue.put(countryCode, val);
				//				System.out.println(countryValue);
			}
			br.close();
		}
		//		System.out.println(countryValue);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile2))) {
			String line;
			while ((line = br.readLine()) != null) {
				//				System.out.println(line);
				String [] array = line.split(" ");
				String countryCode = array[0];
				printWriter.print(countryCode+" ");
				for(int i = 1 ; i < array.length ; i++){
					printWriter.print(array[i]+" ");
				}

				//				System.out.println(countryValue.get(countryCode));
				printWriter.println(countryValue.get(countryCode));
			}
			br.close();
			printWriter.close();
		}
	}

	public static void writeTrainandTestPerUser(String inputFile, String outputFileTrain, String outputFileTest) throws IOException{
		Map<String,List<String>> userItemRatingTS = new LinkedHashMap<String,List<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String user = array[0];
				String item = array[1];
				String rating = array[2];
				String timestamp = array[3];
				if(userItemRatingTS.containsKey(user)){
					List<String> itemRatingTS = userItemRatingTS.get(user);
					itemRatingTS.add(item+","+rating+","+timestamp);
					userItemRatingTS.put(user, itemRatingTS);
				}
				else{
					List<String> itemRatingTS = new ArrayList<String>();
					itemRatingTS.add(item+","+rating+","+timestamp);
					userItemRatingTS.put(user, itemRatingTS);
				}
			}
			PrintWriter outTrain = new PrintWriter (outputFileTrain);
			PrintWriter outTest = new PrintWriter (outputFileTest);
			for(Entry<String, List<String>> entry: userItemRatingTS.entrySet()){
				String user = entry.getKey();
				List<String> itemRatingTS = entry.getValue();
				int size = itemRatingTS.size();
				int trainSize = (int) Math.floor(0.2*size);
				int testSize = size - trainSize;
				int iterator = 0;
				for(String elements: itemRatingTS){
					if(iterator <= trainSize){
						outTrain.println(user+","+elements);
					}
					else{
						outTest.println(user+","+elements);
					}
					iterator++;
				}
			}
			outTrain.close();
			outTest.close();
		}

	}
	public static void writeGroundTruth(String inputFile, String outputFile) throws IOException{
		Map<String,List<String>> userItem = new LinkedHashMap<String,List<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				if(Integer.parseInt(array[2]) < 4){
					continue;
				}
				String user = array[0];
				if(userItem.containsKey(user)){
					List<String> items = userItem.get(user);
					items.add(array[1]);
					userItem.put(user, items);
				}
				else{
					List<String> items = new ArrayList<String> ();
					items.add(array[1]);
					userItem.put(user,items);
				}
			}
			PrintWriter gtTest = new PrintWriter (outputFile);
			for(Entry<String, List<String>> entry: userItem.entrySet()){
				String user = entry.getKey();
				List<String> items = entry.getValue();
				gtTest.print(user+" ");
				for(String elements: items){
					gtTest.print(elements+" ");
				}
				gtTest.println();
			}
			gtTest.close();
		}
	}

	public static void writePredictorForCofactor(String inputFile1, String inputFile2, String outputFile) throws IOException{
		Map<Long, List<String>>userItemList = new TreeMap<Long,List<String>>();
		PrintWriter printWriter = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile1))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String timestamp = array[0];
				long user = Long.parseLong(array[1]);
				String item = array[2];
				List<String>itemList = new ArrayList<String>();
				if(userItemList.containsKey(user)){
					itemList = userItemList.get(user);
					itemList.add(item);
				}
				else{
					itemList = new ArrayList<String>();
					itemList.add(item);
				}
				userItemList.put(user, itemList);
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile2))) {
			String line;
			br.readLine();
			while((line = br.readLine())!=null) {
				String [] array = line.split(" ");
				long user = Long.parseLong(array[0]);
				List<String> itemList = null;
				if(userItemList.containsKey(user)){
					printWriter.print(user+" ");
					itemList = userItemList.get(user);
					for(int i = 1 ; i < array.length ; i++){
						String item = array[i];	
						if(itemList.contains(item)){
							printWriter.print(item+" ");

						}
					}
					printWriter.println();
				}



			}
		}
		printWriter.close();
	}
	public static void writeGroundTruthForCofactor(String inputFile, String outputFile) throws IOException{
		Map<Long, List<String>>userItemList = new TreeMap<Long,List<String>>();
		PrintWriter gtTest = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String timestamp = array[0];
				long user = Long.parseLong(array[1]);
				String item = array[2];
				List<String>itemList = new ArrayList<String>();
				if(userItemList.containsKey(user)){
					itemList = userItemList.get(user);
					itemList.add(item);
				}
				else{
					itemList = new ArrayList<String>();
					itemList.add(item);
				}
				userItemList.put(user, itemList);
			}
		}
		for(Entry<Long, List<String>> entry: userItemList.entrySet()){
			long user = entry.getKey();
			List<String> items = entry.getValue();
			gtTest.print(user+" ");
			for(String elements: items){
				gtTest.print(elements+" ");
			}
			gtTest.println();
		}
		gtTest.close();
	}

	public static void writeGroundTruthForPopularity(String inputFile, String outputFile) throws IOException{
		Map<Long, List<String>>userItemList = new TreeMap<Long,List<String>>();
		PrintWriter gtTest = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				int rating = Integer.parseInt(array[2]);
				long user = Long.parseLong(array[0]);
				String item = array[1];
				List<String>itemList = new ArrayList<String>();
				if(userItemList.containsKey(user)){
					itemList = userItemList.get(user);
					if(rating >= 4)
						itemList.add(item);
				}
				else{
					itemList = new ArrayList<String>();
					if(rating >= 4)
						itemList.add(item);
				}
				userItemList.put(user, itemList);
			}
		}
		for(Entry<Long, List<String>> entry: userItemList.entrySet()){
			long user = entry.getKey();
			List<String> items = entry.getValue();
			gtTest.print(user+" ");
			for(String elements: items){
				gtTest.print(elements+" ");
			}
			gtTest.println();
		}
		gtTest.close();
	}

	public static void writeFileCofactorFormat(String inputFile, String outputFile, int score) throws IOException{
		PrintWriter printWriter = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			printWriter.println("timestamp,uid,sid");
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String user = array[0];
				String item = array[1];
				long rating = Long.parseLong(array[2]);
				long timestamp = Long.parseLong(array[3]);
				if(rating >= score){
					//					System.out.println(rating);
					printWriter.println(timestamp+","+user+","+item);
				}
			}
			printWriter.close();
		}
	}


	public static void indexTrainAndTestFiles(String inputFile1, String inputFile2, String inputFile3, String outputFile1, String
			outputFile2, String outputFile3) throws IOException{
		PrintWriter printWriter1 = new PrintWriter (outputFile1);
		PrintWriter printWriter2 = new PrintWriter (outputFile2);
		PrintWriter printWriter3 = new PrintWriter (outputFile3);
		Map<Long,Long> userIndex = new LinkedHashMap<Long,Long>();
		Map<Long,Long> itemIndex = new LinkedHashMap<Long,Long>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile1))) {
			String line;
			line = br.readLine();
			printWriter1.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				long user = Long.parseLong(array[1]);
				long item = Long.parseLong(array[2]);
				long indexUser;
				if(userIndex.containsKey(user)){
					indexUser = userIndex.get(user);
				}
				else{
					indexUser = userIndex.size();
					userIndex.put(user, indexUser);
				}
				long indexItem;
				if(itemIndex.containsKey(item)){
					indexItem = itemIndex.get(item);
				}
				else{
					indexItem = itemIndex.size();
					itemIndex.put(item, indexItem);
				}
				String lineWritten = array[0]+","+indexUser+","+indexItem;
				printWriter1.println(lineWritten);				
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile2))) {
			String line;
			line = br.readLine();
			printWriter2.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				long user = Long.parseLong(array[1]);
				long item = Long.parseLong(array[2]);
				long indexUser;
				if(userIndex.containsKey(user)){
					indexUser = userIndex.get(user);
				}
				else{
					indexUser = userIndex.size();
					userIndex.put(user, indexUser);
				}
				long indexItem;
				if(itemIndex.containsKey(item)){
					indexItem = itemIndex.get(item);
				}
				else{
					indexItem = itemIndex.size();
					itemIndex.put(item, indexItem);
				}
				String lineWritten = array[0]+","+indexUser+","+indexItem;
				printWriter2.println(lineWritten);				
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile3))) {
			String line;
			line = br.readLine();
			printWriter3.println(line);
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				long user = Long.parseLong(array[1]);
				long item = Long.parseLong(array[2]);
				long indexUser;
				if(userIndex.containsKey(user)){
					indexUser = userIndex.get(user);
				}
				else{
					indexUser = userIndex.size();
					userIndex.put(user, indexUser);
				}
				long indexItem;
				if(itemIndex.containsKey(item)){
					indexItem = itemIndex.get(item);
				}
				else{
					indexItem = itemIndex.size();
					itemIndex.put(item, indexItem);
				}
				String lineWritten = array[0]+","+indexUser+","+indexItem;
				printWriter3.println(lineWritten);				
			}
		}
		printWriter1.close();
		printWriter2.close();
		printWriter3.close();
	}
	public static void writeGroundTruthForBPRMF(String inputFile, String outputFile) throws IOException{
		PrintWriter gtTest = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			//			br.readLine();
			while ((line = br.readLine()) != null) {
				String [] array = line.split("\t");
				long user = Long.parseLong(array[0]);
				gtTest.print(user+" ");
				String itemList = array[1];
				String [] itemArray = itemList.split(",");
				for(int i = 0 ; i < itemArray.length ; i++){
					String [] itemScore = itemArray[i].split(":");
					String item = itemScore[0];
					String score = itemScore[1];
					if(score.equals("4")){
						gtTest.print(item+" ");
					}
				}
				gtTest.println();

			}
		}
		gtTest.close();
	}

	public static void writePredictorForBPRMF(String inputFile1, String inputFile2, String outputFile) throws NumberFormatException, IOException{
		Map<Long, List<String>>userItemList = new TreeMap<Long,List<String>>();
		PrintWriter printWriter = new PrintWriter (outputFile);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile1))) {
			String line;
			//			br.readLine();
			while ((line = br.readLine()) != null) {
				String [] array = line.split("\t");
				long user = Long.parseLong(array[0]);
				String itemListScore = array[1];
				String [] itemArray = itemListScore.split(",");
				List<String>itemList = new ArrayList<String>();
				for(int i = 0 ; i < itemArray.length ; i++){
					String []itemScore = itemArray[i].split(":");
					itemList.add(itemScore[0]);
				}
				userItemList.put(user, itemList);
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile2))) {
			String line;
			//			br.readLine();
			while((line = br.readLine())!=null) {
				String [] array = line.split("\t");
				long user = Long.parseLong(array[0]);
				String itemLine = array[1];
				itemLine = itemLine.replace("[","").replace("]", "");
				List<String> itemList = null;
				printWriter.print(user+" ");
				if(userItemList.containsKey(user)){
					itemList = userItemList.get(user);
					String [] itemRatingConfidenceArray = itemLine.split(",");
					for(int i = 1 ; i < itemRatingConfidenceArray.length ; i++){
						String itemRatingConfidence = itemRatingConfidenceArray[i];
						String [] itemRatingConfidenceSingleElem = itemRatingConfidence.split(":");
						String item = itemRatingConfidenceSingleElem[0];
						if(itemList.contains(item)){
							printWriter.print(item+" ");
						}
					}
					printWriter.println();
				}
			}
		}
		printWriter.close();
	}
	public static void copyMultipleLines(String fileName,long num) throws IOException{
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
			line = br.readLine();
			// TODO Auto-generated method stub
		}
		long numberofTimestoBeCopied = num;
		PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		for(int i = 0 ; i < numberofTimestoBeCopied ; i++){
			printWriter.println(line);
		}

		printWriter.close();

	}
	
	public static void writePredictedValuesForPop(String inputFile, String outputFile) throws IOException{

		PrintWriter printWriter = new PrintWriter (outputFile);
		Map<String,List<String>> userOfferMap = new  LinkedHashMap<String,List<String>>();


		try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				String [] array = line.split(",");
				String user = array[0];
				String item = array[1];
				if(userOfferMap.containsKey(user)){
					ArrayList<String>offers = (ArrayList<String>) userOfferMap.get(user);
					offers.add(item);
					userOfferMap.put(user, offers);
				}
				else{
					List<String>offers = new ArrayList<String>();
					offers.add(item);
					userOfferMap.put(user, offers);
				}


				// TODO Auto-generated method stub
			}
			for(Entry<String, List<String>> entry: userOfferMap.entrySet()){
				String user = entry.getKey();
				List<String> offers = entry.getValue();
				printWriter.print(user+" ");
				for(String offer: offers){
					printWriter.print(offer+" ");
				}
				printWriter.println();
			}

			printWriter.close();
			br.close();
		}
	}
}
