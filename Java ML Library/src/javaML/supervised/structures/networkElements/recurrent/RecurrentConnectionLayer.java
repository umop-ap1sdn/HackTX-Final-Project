package javaML.supervised.structures.networkElements.recurrent;

import javaML.supervised.structures.Matrix;
import javaML.supervised.structures.networkElements.ConnectionLayer;

public class RecurrentConnectionLayer extends ConnectionLayer{
	
	RecurrentLayer rLayer;
	
	public RecurrentConnectionLayer(RecurrentLayer layer) {
		super(layer);
		this.rLayer = layer;
		rLayer.setRConnect(this);
	}
	
	@Override
	public void adjustWeights(double lr) {
		Matrix gradients = new Matrix(layer.getRows(), layer.getColumns(), Matrix.FILL_ZERO);
		
		for(int row = 0; row < destSize; row++) {
			for(int col = 0; col < sourceSize; col++) {
				double gradient = 0;
				for(int count = 0; count < rLayer.getMemoryLength() - 1; count++) {
					gradient += rLayer.getValues(count).getValue(col) * rLayer.getErrors(count + 1).getValue(row);
				}
				
				gradient *= -lr;
				gradients.setValue(gradient, row, col);
			}
		}
		
		layer = Matrix.add(layer, gradients);
	}
}
