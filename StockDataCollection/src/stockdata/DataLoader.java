package stockdata;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;

public class DataLoader {
	
	/**
	 * Function to turn data file into double[][] array based on what data is requested
	 * @param stockCode Stock identifier such as "AAPL"
	 * @param allow boolean array that decides which pieces of data to keep
	 * @return 2D array consisting of the items from the file
	 * @throws Exception
	 */
	public static double[][] getData(String stockCode, boolean[] allow) throws Exception {
		
		//Find total count of elements that will need to be kept
		int totalSize = 0;
		for(boolean val: allow) totalSize += val ? 1 : 0;
		
		//Data is formatted in newest first order
		//Want data in oldest first order
		//To reverse order, store into a stack and pop from the stack
		Stack<double[]> dataStack = new Stack<>();
		
		//Load file and scanner to read from file
		File file = new File(String.format("files//data//%s.dat", stockCode));
		Scanner sc = new Scanner(file);
		
		//First line is just identifications
		sc.nextLine();
		while(sc.hasNextLine()) {
			//Fill stack
			dataStack.push(format(sc.nextLine(), allow, totalSize));
		}
		
		sc.close();
		
		//Initialize return array
		double[][] ret = new double[dataStack.size()][totalSize];
		int retIndex = 0;
		
		//Reverse data from stack into array
		while(dataStack.size() > 0) ret[retIndex++] = dataStack.pop();
		
		
		return ret;
	}
	
	/**
	 * Function to turn a single line of (comma separated) data into a 1D array of double values
	 * @param data String to read from
	 * @param allow data to keep
	 * @param totalSize size of return array
	 * @return 1D array of the line turned into usable data
	 */
	private static double[] format(String data, boolean[] allow, int totalSize) {
		
		//Split into elements (comma separated)
		String[] arr = data.split(",");
		double[] ret = new double[totalSize];
		
		//Parse data into double values
		int retIndex = 0;
		for(int index = 1; index < arr.length; index++) {
			if(allow[index - 1]) ret[retIndex++] = Double.parseDouble(arr[index]);
		}
		
		return ret;
	}
}
