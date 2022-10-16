package tests;

import javaML.supervised.NetworkBuilder;
import java.util.Arrays;
import javaML.supervised.Network;

/**
 * 
 * Use a Neural Network to predict whether a point on a graph is above, below, or inside of the 2
 * following lines 
 * y = (1 + sin(x * 2pi)) / 2
 * y = (1 + cos(x * 2pi)) / 2
 * 
 * Data points will be within domain and range of [0, 1]
 * 2 input nodes (x, y)
 * 3 output nodes (above, inside, below)
 * 
 * hidden size is user defined
 * 
 * @author Caleb Devon
 *
 */

public class DatasetTest {
	public static void main(String[]args) {
		
		NetworkBuilder constructor = new NetworkBuilder(15);
		constructor.putLayer(Network.INPUT, 2, Network.LINEAR, true);
		constructor.putLayer(Network.HIDDEN, 16, Network.SIGMOID, true);
		constructor.putLayer(Network.HIDDEN, 16, Network.SIGMOID, true);
		//constructor.putLayer(Network.HIDDEN, 32, Network.TANH, true);
		//constructor.putLayer(Network.HIDDEN, 32, Network.RELU, true);
		constructor.putLayer(Network.OUTPUT, 3, Network.SIGMOID, false);
		
		
		Network nn = constructor.build(10);
		
		double[][][] dataset = createDatapoints(100);
		double[][][] testset = createDatapoints(100);
		
		nn.setLearningRate(0.5);
		
		train(nn, 20000, dataset);
		/*
		
		double[][][] dataset = createDatapoints(5);
		for(double[][] data: dataset) {
			System.out.println(Arrays.toString(data[0]));
			System.out.println(Arrays.toString(data[1]));
			
		}
		
		*/
		
		testRandom(nn, testset);
	}
	
	public static double[][][] createDatapoints(int size){
		
		double[][][] ret = new double[size][2][];
		for(int index = 0; index < size; index++) {
			double[] inputs = {Math.random(), Math.random()};
			double[] outputs = test(inputs[0], inputs[1]);
			
			ret[index][0] = inputs;
			ret[index][1] = outputs;
		}
		
		return ret;
	}
	
	private static double[] test(double x, double y) {
		double y1 = (1 + Math.sin(2 * x * Math.PI)) / 2;
		double y2 = (1 + Math.cos(2 * x * Math.PI)) / 2;
		
		if(y >= y1 && y >= y2)
			return new double[] {1, 0, 0};
		else if(y < y1 && y < y2)
			return new double[] {0, 0, 1};
		else return new double[] {0, 1, 0};
		
	}
	
	public static void train(Network nn, int iterations, double[][][] dataset) {
		nn.uploadDataset(dataset);
		
		boolean backProp = false;
		for(int count = 0; count < iterations; count++) {
			backProp = !nn.train(backProp, false);
			System.out.println(nn.getAverageLoss());
			
		}
		
		System.out.println("\n");
	}
	
	public static void testRandom(Network nn, double[][][] dataset) {
		for(double[][] data: dataset) {
			System.out.println(Arrays.toString(nn.test(data[0])) + " " + Arrays.toString(data[1]));
		}
		
		System.out.println("\n");
	}
	
	public static void testAll(Network nn) {
		
	}
}
