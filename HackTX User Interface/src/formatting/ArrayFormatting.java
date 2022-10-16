package formatting;

public class ArrayFormatting {
	
	public static double[][][] createDataset(double[][] data){
		//For time constraints assume data[].length = 1
		double max = 0;
		double min = data[0][0];
		
		for(double[] arr: data) {
			if(arr[0] > max) max = arr[0];
			if(arr[0] < min) min = arr[0];
		}
		
		for(int index = 0; index < data.length; index++) {
			data[index][0] = normalize(data[index][0], max, min, 1, 0);
		}
		
		double[][][] ret = new double[data.length - 1][2][1];
		
		for(int index = 0; index < ret.length; index++) {
			ret[index][0][0] = data[index][0];
			ret[index][1][0] = data[index + 1][0];
			
		}
		
		return ret;
	}
	
	private static double normalize(double value, double max, double min, double high, double low) {
		return (((value - min) / (max - min)) * high) - low;
	}
}
