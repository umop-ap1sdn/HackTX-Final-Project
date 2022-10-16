package javaML.supervised.structures;

public class Vector extends Matrix {
	
	double[] vector;
	
	public Vector(int size, int fillCode){
		super(size, 1, fillCode);
		this.vector = ArrayFunctions.transpose1D(matrix);
	}
	
	public Vector(double[] vector){
		super(ArrayFunctions.transpose(vector));
		this.vector = vector;
		
	}
	
	protected Vector(double[][] matrix){
		super(matrix);
		this.vector = ArrayFunctions.transpose1D(matrix);
		
	}
	
	public static Vector add(Vector v1, Vector v2) {
		Matrix vec = Matrix.add(v1, v2);
		return new Vector(vec.matrix);
	}
	
	public static Vector scale(Vector v1, double scalar) {
		Matrix vec = Matrix.scale(v1, scalar);
		return new Vector(vec.matrix);
	}
	
	public double[] getVector() {
		return this.vector;
	}
	
	public void setVector(double[] vector) {
		this.vector = vector;
		super.setMatrix(ArrayFunctions.transpose(vector));
	}
	
	public double getValue(int row) {
		return super.getValue(row, 0);
	}
	
	public void setValue(double value, int row) {
		super.setValue(value, row, 0);
	}
	
	@Override
	public void simplePrint() {
		for(int row = 0; row < rows; row++) {
			System.out.printf("[%.2f]%n", vector[row]);
		}
		
		System.out.println();
	}
	
	
	
}
