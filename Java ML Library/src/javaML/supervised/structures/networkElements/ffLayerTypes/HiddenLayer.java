package javaML.supervised.structures.networkElements.ffLayerTypes;

import javaML.supervised.Network;
import javaML.supervised.structures.Matrix;
import javaML.supervised.structures.Vector;
import javaML.supervised.structures.networkElements.ConnectionLayer;
import javaML.supervised.structures.networkElements.NeuronLayer;

public class HiddenLayer extends NeuronLayer {
	
	protected ConnectionLayer conIn;
	protected ConnectionLayer conOut;
	
	public HiddenLayer(int layerSize, int memoryLength, int activationCode, boolean bias) {
		super(layerSize, memoryLength, activationCode, bias);
	}
	
	public void setConnectionIn(ConnectionLayer conIn) {
		this.conIn = conIn;
	}
	
	public void setConnectionOut(ConnectionLayer conOut) {
		this.conOut = conOut;
	}
	
	@Override
	protected void calculateErrors(Vector errorVec) {
		//dE/dY of a hidden layer by the chain rule will be equal to
		//i is a node of the hidden layer
		//j is a node of the next layer
		//dYj * Wij * activationDerivative
		
		NeuronLayer nextStep = conOut.getDestination();
		
		Vector nextErrors = nextStep.getRecentErrors();
		Matrix conT = Matrix.transpose(conOut.getMatrix());
		
		Vector currErrors = Matrix.multiply(conT, nextErrors).getAsVector();
		currErrors = super.removeBias(currErrors);
		
		currErrors = Matrix.linearMultiply(currErrors, this.getRecentDerivatives()).getAsVector();
		
		super.putErrors(currErrors);
	}
	
	@Override
	public void runActivation() {
		this.conIn.forwardPass();
		this.activate();
	}

	@Override
	public String toString() {
		return String.format("%d,%d,%d,%d\n", Network.HIDDEN, layerSize, activationCode, bias ? 1 : 0);
	}
	
}
