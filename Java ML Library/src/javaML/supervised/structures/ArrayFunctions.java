package javaML.supervised.structures;

public class ArrayFunctions {
	
	protected static double[][] transpose(double[][] matrix){
		int rows = matrix.length;
		int columns = matrix[0].length;
		
		double[][] ret = new double[columns][rows];
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				ret[col][row] = matrix[row][col];
			}
		}
		
		return ret;
	}
	
	protected static double[][] transpose(double[] vector){
		int rows = vector.length;
		double[][] ret = new double[1][rows];
		ret[0] = vector;
		return transpose(ret);
	}
	
	protected static double[] transpose1D(double[][] matrix) {
		if(matrix[0].length != 1) return null;
		
		int rows = matrix.length;
		
		double[] ret = new double[rows];
		for(int row = 0; row < rows; row++) {
			ret[row] = matrix[row][0];
		}
		
		return ret;
	}
}
