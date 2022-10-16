package javaML.supervised.structures.networkElements.ffLayerTypes;

import javaML.supervised.Network;
import javaML.supervised.structures.Matrix;
import javaML.supervised.structures.Vector;
import javaML.supervised.structures.networkElements.ConnectionLayer;
import javaML.supervised.structures.networkElements.NeuronLayer;

public class InputLayer extends NeuronLayer {
	
	ConnectionLayer conOut;
	
	public InputLayer(int layerSize, int memoryLength, int activationCode, boolean bias) {
		super(layerSize, memoryLength, Network.LINEAR, bias);
	}
	
	public InputLayer(int layerSize, int memoryLength, boolean bias) {
		super(layerSize, memoryLength, Network.LINEAR, bias);
	}
	
	public void setConnectionOut(ConnectionLayer conOut) {
		this.conOut = conOut;
	}
	
	@Override
	public void runActivation() {
		//Nothing special because there are no input matrix to an input layer
		this.activate();
	}
	
	@Override
	protected void calculateErrors(Vector errorVec) {
		//By Definition the input layer does not have an error
		Vector error = new Vector(errorVec.getRows(), Matrix.FILL_ZERO);
		super.putErrors(error);
	}
	
	public void setInputs(double[] inputs) {
		setInputs(new Vector(inputs));
	}
	
	private void setInputs(Vector inputs) {
		this.pushValues(inputs);
	}

	@Override
	public String toString() {
		return String.format("%d,%d,%d,%d\n", Network.INPUT, layerSize, activationCode, bias ? 1 : 0);
	}
	
}
