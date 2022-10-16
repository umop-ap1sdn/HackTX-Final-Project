package javaML.supervised.structures.networkElements;

import java.util.LinkedList;
import javaML.supervised.Network;
import javaML.supervised.structures.*;

public abstract class NeuronLayer {
	
	protected int memoryLength;
	protected int activationCode;
	protected int layerSize;
	protected int trueSize;
	
	protected boolean bias;
	
	private LinkedList<Vector> activations;
	private LinkedList<Vector> derivatives;
	private LinkedList<Vector> errors;
	
	Vector unactivated;
	
	ActivationFunctions function;
	
	protected NeuronLayer(int layerSize, int memoryLength, int activationCode, boolean bias) {
		this.layerSize = layerSize;
		this.memoryLength = memoryLength;
		this.activationCode = activationCode;
		this.bias = bias;
		
		this.trueSize = layerSize + (bias ? 1 : 0);
		
		initializeLists();
		initializeActivation();
	}
	
	
	protected abstract void calculateErrors(Vector errorVec);
	public abstract void runActivation();
	public abstract String toString();
	
	private void initializeLists() {
		
		activations = new LinkedList<>();
		derivatives = new LinkedList<>();
		errors = new LinkedList<>();
		
		for(int index = 0; index < memoryLength; index++) {
			activations.add(new Vector(layerSize, Matrix.FILL_ZERO));
			derivatives.add(new Vector(layerSize, Matrix.FILL_ZERO));
			errors.add(new Vector(layerSize, Matrix.FILL_ZERO));
		}
	}
	
	private void initializeActivation() {
		switch(activationCode) {
		case Network.LINEAR:
			function = new Linear();
			break;
		case Network.RELU:
			function = new ReLU();
			break;
		case Network.SIGMOID:
			function = new Sigmoid();
			break;
		case Network.TANH:
			function = new Tanh();
			break;
		default:
				function = new Linear();
			break;
		}
		
		unactivated = new Vector(layerSize, Matrix.FILL_ZERO);
	}
	
	public void reset() {
		this.initializeLists();
	}
	
	protected void pushValues(Vector values) {
		unactivated = Matrix.add(unactivated, values).getAsVector();
	}
	
	public void activate() {
		double[] arr = unactivated.getVector();
		
		derivatives.addLast(new Vector(ActivationFunctions.derivative(function, arr)));
		activations.addLast(new Vector(ActivationFunctions.activate(function, arr)));
		
		activations.pollFirst();
		derivatives.pollFirst();
		
		unactivated = new Vector(layerSize, Matrix.FILL_ZERO);
		
	}
	
	public void calculateErrors(double[] errors) {
		this.calculateErrors(new Vector(errors));
	}
	
	protected Vector getValues(int index) {
		if(bias) return padBias(activations.get(index));
		else return activations.get(index);
	}
	
	protected Vector getRecentValues() {
		if(bias) return padBias(activations.getLast());
		else return activations.getLast();
	}
	
	private Vector padBias(Vector vec) {
		double[] vector = new double[trueSize];
		double[] vecArr = vec.getVector();
		
		for(int index = 0; index < vecArr.length; index++) {
			vector[index] = vecArr[index];
		}
		
		vector[layerSize] = 1;
		
		return new Vector(vector);
	}
	
	protected Vector removeBias(Vector vec) {
		if(!bias) return vec;
		
		Vector newVec = new Vector(layerSize, Vector.FILL_ZERO);
		for(int index = 0; index < layerSize; index++) {
			newVec.setValue(vec.getValue(index), index);
		}
		
		return newVec;
	}
	
	protected void putErrors(Vector errorVec) {
		errors.addLast(errorVec);
		errors.pollFirst();
	}
	
	public Vector getErrors(int index) {
		return errors.get(index);
	}
	
	public Vector getRecentErrors() {
		return errors.getLast();
	}
	
	protected void adjustRecentErrors(Vector errorVec) {
		int lastIndex = memoryLength - 1;
		errors.set(lastIndex, Vector.add(errorVec, errors.getLast()));
	}
	
	protected Vector getDerivatives(int index) {
		return derivatives.get(index);
	}
	
	protected Vector getRecentDerivatives() {
		return derivatives.getLast();
	}
	
	protected int getLayerSize() {
		return this.layerSize;
	}
	
	protected int getTrueSize() {
		return this.trueSize;
	}
	
	protected int getMemoryLength() {
		return this.memoryLength;
	}
}
