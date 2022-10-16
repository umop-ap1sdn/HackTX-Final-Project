package javaML.supervised.structures.networkElements.ffLayerTypes;

import javaML.supervised.Network;
import javaML.supervised.structures.Matrix;
import javaML.supervised.structures.Vector;
import javaML.supervised.structures.networkElements.ConnectionLayer;
import javaML.supervised.structures.networkElements.NeuronLayer;

public class OutputLayer extends NeuronLayer {
	
	ConnectionLayer conIn;
	
	public OutputLayer(int layerSize, int memoryLength, int activationCode, boolean bias) {
		super(layerSize, memoryLength, activationCode, false);
	}
	
	public OutputLayer(int layerSize, int memoryLength, int activationCode) {
		super(layerSize, memoryLength, activationCode, false);
	}
	
	public void setConnectionIn(ConnectionLayer conIn) {
		this.conIn = conIn;
	}
	
	public double[] getOutputs() {
		return this.getRecentValues().getVector();
	}
	
	//In the current version of the library, the Loss function will be predefined to be Mean Squared Error
	// 1/n * Summation[i = 0, n]((t-y)^2)
	@Override
	protected void calculateErrors(Vector errorVec) {
		//errorVec will contain the targetValues for a given test case
		
		//Via the chain rule the error of an output layer neuron is given by the 
		//Partial derivative of E in respect to y multiplied by the derivative of the activation of y
		
		//dE/dy = 2/n(y - t)
				
		double scalar = 2.0 / this.getLayerSize();
		Vector baseErrors = Matrix.scale(errorVec, -1).getAsVector();
		baseErrors = Matrix.add(this.getRecentValues(), baseErrors).getAsVector();
		baseErrors = Matrix.scale(baseErrors, scalar).getAsVector();
		
		baseErrors = Matrix.linearMultiply(baseErrors, this.getRecentDerivatives()).getAsVector();
		
		this.putErrors(baseErrors);
	}
	
	@Override
	public void runActivation() {
		this.conIn.forwardPass();
		this.activate();
	}

	@Override
	public String toString() {
		return String.format("%d,%d,%d,%d\n", Network.OUTPUT, layerSize, activationCode, bias ? 1 : 0);
	}
	
}
