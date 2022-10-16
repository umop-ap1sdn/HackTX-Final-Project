package javaML.supervised.structures.networkElements;

import javaML.supervised.structures.*;
import javaML.supervised.structures.networkElements.ffLayerTypes.*;
import javaML.supervised.structures.networkElements.recurrent.RecurrentLayer;

public class ConnectionLayer {
	
	protected int destSize, sourceSize;
	
	protected NeuronLayer source;
	protected NeuronLayer destination;
	protected Matrix layer;
	
	public ConnectionLayer(InputLayer source, HiddenLayer destination) {
		this.source = source;
		this.destination = destination;
		initialize();
		
		source.setConnectionOut(this);
		destination.setConnectionIn(this);
	}
	
	public ConnectionLayer(HiddenLayer source, HiddenLayer destination) {
		this.source = source;
		this.destination = destination;
		initialize();
		
		source.setConnectionOut(this);
		destination.setConnectionIn(this);
	}
	
	public ConnectionLayer(HiddenLayer source, OutputLayer destination) {
		this.source = source;
		this.destination = destination;
		initialize();
		
		source.setConnectionOut(this);
		destination.setConnectionIn(this);
	}
	
	protected ConnectionLayer(RecurrentLayer layer) {
		this.source = layer;
		this.destination = layer;
		initialize();
	}
	
	public ConnectionLayer(InputLayer source, OutputLayer destination) {
		this.source = source;
		this.destination = destination;
		initialize();
		
		source.setConnectionOut(this);
		destination.setConnectionIn(this);
	}
	
	private void initialize() {
		destSize = destination.getLayerSize();
		sourceSize = source.getTrueSize();
		
		//System.out.printf("destSize: %d, sourceSize: %d\n", destSize, sourceSize);
		//System.out.printf("Matrix: %d * %d, Multiplied by Vector: %d * 1 = %d * 1\n", destSize, sourceSize, sourceSize, destSize);
		
		layer = new Matrix(destSize, sourceSize, Matrix.FILL_RANDOM);
	}
	
	public void forwardPass() {
		Vector result = Matrix.multiply(layer, source.getRecentValues()).getAsVector();
		destination.pushValues(result);
	}
	
	public void adjustWeights(final double lr) {
		Matrix gradients = new Matrix(layer.getRows(), layer.getColumns(), Matrix.FILL_ZERO);
		
		for(int row = 0; row < destSize; row++) {
			for(int col = 0; col < sourceSize; col++) {
				double gradient = 0;
				for(int count = 0; count < source.getMemoryLength(); count++) {
					gradient += source.getValues(count).getValue(col) * destination.getErrors(count).getValue(row);
				}
				
				gradient *= -lr;
				gradients.setValue(gradient, row, col);
			}
		}
		
		layer = Matrix.add(layer, gradients);
	}
	
	public NeuronLayer getSource() {
		return this.source;
	}
	
	public NeuronLayer getDestination() {
		return this.destination;
	}
	
	public Matrix getMatrix() {
		return this.layer;
	}
	
	public void setMatrix(double[][] arr) {
		this.layer = new Matrix(arr);
	}
	
	@Override
	public String toString() {
		return layer.toString();
	}
}
