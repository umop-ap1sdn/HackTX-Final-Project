package javaML.supervised.structures.networkElements.recurrent;

import javaML.supervised.Network;
import javaML.supervised.structures.Matrix;
import javaML.supervised.structures.Vector;
import javaML.supervised.structures.networkElements.NeuronLayer;
import javaML.supervised.structures.networkElements.ffLayerTypes.HiddenLayer;

public class RecurrentLayer extends HiddenLayer{
	
	RecurrentConnectionLayer rConnect;
	
	public RecurrentLayer(int layerSize, int memoryLength, int activationCode, boolean bias) {
		super(layerSize, memoryLength, activationCode, bias);
	}
	
	protected void setRConnect(RecurrentConnectionLayer rConnect) {
		this.rConnect = rConnect;
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
		
		conT = Matrix.transpose(rConnect.getMatrix());
		
		Vector retroErrors = Matrix.multiply(conT, currErrors).getAsVector();
		retroErrors = super.removeBias(retroErrors);
		retroErrors = Matrix.linearMultiply(retroErrors, this.getDerivatives(memoryLength - 2)).getAsVector();
		
		super.adjustRecentErrors(retroErrors);
		super.putErrors(currErrors);
	}

	@Override
	public void runActivation() {
		this.conIn.forwardPass();
		this.rConnect.forwardPass();
		this.activate();
	}
	
	@Override
	protected int getMemoryLength() {
		return this.memoryLength;
	}
	
	@Override
	protected Vector getValues(int index) {
		return super.getValues(index);
	}
	
	@Override
	public String toString() {
		return String.format("%d,%d,%d,%d\n", Network.RECURRENT, layerSize, activationCode, bias ? 1 : 0);
	}
	
}
