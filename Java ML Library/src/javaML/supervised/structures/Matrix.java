package javaML.supervised.structures;

public class Matrix {
	
	public static final int FILL_ZERO = 1;
	public static final int FILL_RANDOM = 2;
	
	protected int rows, columns;
	protected double[][] matrix;
	
	public Matrix(int rows, int columns, int fillCode){
		this.rows = rows;
		this.columns = columns;
		
		this.matrix = new double[rows][columns];
		
		this.initialize(fillCode);
		
	}
	
	public Matrix(double[][] matrix){
		this.rows = matrix.length;
		this.columns = matrix[0].length;
		
		this.matrix = copyArray(matrix);
	}
	
	private double[][] copyArray(double[][] matrix){
		double[][] ret = new double[this.rows][this.columns];
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				ret[row][col] = matrix[row][col];
			}
		}
		
		return ret;
	}
	
	private void initialize(int fillCode) {
		switch(fillCode) {
		case FILL_ZERO:
			fill0();
			break;
		case FILL_RANDOM:
			fillRandom();
			break;
		}
	}
	
	private void fill0() {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				matrix[row][col] = 0;
			}
		}
	}
	
	private void fillRandom() {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				matrix[row][col] = (Math.random() * 2) - 1;
			}
		}
	}
	
	public static Matrix add(Matrix m1, Matrix m2) {
		if(m1.rows != m2.rows && m1.columns != m2.columns) return null;
		
		double[][] mat = new double[m1.rows][m1.columns];
		
		for(int row = 0; row < m1.rows; row++) {
			for(int col = 0; col < m1.columns; col++) {
				mat[row][col] = m1.getValue(row, col) + m2.getValue(row, col);
			}
		}
		
		return new Matrix(mat);
	}
	
	public static Matrix scale(Matrix m1, double scalar) {
		double[][] mat = new double[m1.rows][m1.columns];
		
		for(int row = 0; row < m1.rows; row++) {
			for(int col = 0; col < m1.columns; col++) {
				mat[row][col] = m1.getValue(row, col) * scalar;
			}
		}
		
		return new Matrix(mat);
	}
	
	public static Matrix linearMultiply(Matrix m1, Matrix m2) {
		if(m1.rows != m2.rows || m1.columns != m2.columns) return null;
		
		double[][] matrix = new double[m1.rows][m1.columns];
		
		for(int row = 0; row < m1.rows; row++) {
			for(int col = 0; col < m1.columns; col++) {
				matrix[row][col] = m1.getValue(row, col) * m2.getValue(row, col);
			}
		}
		
		return new Matrix(matrix);
	}
	
	public static Matrix transpose(Matrix m1) {
		double[][] mat = ArrayFunctions.transpose(m1.matrix);
		return new Matrix(mat);
	}
	
	public static Matrix multiply(Matrix m1, Matrix m2) {
		if(m1.columns != m2.rows) {
			if(m2.columns != m1.rows) return null;
			else return multiply(m2, m1);
		}
		
		double[][] matrix = new double[m1.rows][m2.columns];
		
		for(int row = 0; row < m1.rows; row++) {
			for(int col = 0; col < m2.columns; col++) {
				matrix[row][col] = multiplyCoord(m1, m2, row, col);
			}
		}
		
		return new Matrix(matrix);
	}
	
	private static double multiplyCoord(Matrix m1, Matrix m2, int row, int col) {
		
		double sum = 0;
		int length = m1.columns;
		
		for(int index = 0; index < length; index++) {
			sum += m1.getValue(row, index) * m2.getValue(index, col);
		}
		
		return sum;
	}
	
	public double[][] getMatrix(){
		return this.matrix;
	}
	
	public void setMatrix(double[][] matrix) {
		this.matrix = copyArray(matrix);
	}
	
	public void setValue(double value, int row, int col) {
		this.matrix[row][col] = value;
	}
	
	public double getValue(int row, int col) {
		return matrix[row][col];
	}
	
	public Vector getAsVector() {
		if(this.columns != 1) return null;
		return new Vector(this.matrix);
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public void simplePrint() {
		for(int row = 0; row < rows; row++) {
			System.out.print("[");
			for(int col = 0; col < columns; col++) {
				System.out.printf("%.4f", matrix[row][col]);
				
				if(col < columns - 1) System.out.print(", ");
			}
			
			System.out.println("]");
		}
		
		System.out.println();
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				ret += matrix[row][col];
				if(col < columns - 1) ret += ",";
			}
			
			if(row < rows - 1) ret += "\n";
		}
		
		return ret;
	}
}
