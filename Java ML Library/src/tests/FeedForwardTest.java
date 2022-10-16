package tests;

import javaML.supervised.NetworkBuilder;
import java.util.Arrays;
import javaML.supervised.Network;

public class FeedForwardTest {
	public static void main(String[]args) {
		
		NetworkBuilder constructor = new NetworkBuilder(4);
		System.out.println(constructor.putLayer(Network.INPUT, 2, Network.LINEAR, true));
		System.out.println(constructor.putLayer(Network.HIDDEN, 16, Network.RELU, true));
		System.out.println(constructor.putLayer(Network.HIDDEN, 16, Network.SIGMOID, true));
		//System.out.println(constructor.putLayer(Network.HIDDEN, 16, Network.SIGMOID, true));
		//System.out.println(constructor.putLayer(Network.HIDDEN, 16, Network.SIGMOID, true));
		System.out.println(constructor.putLayer(Network.OUTPUT, 1, Network.SIGMOID, false));
		
		Network test = constructor.build(4);
		
		System.out.println(test == null);
		
		System.out.println(Arrays.toString(test.test(1, 2)));
		System.out.println(Arrays.toString(test.test(1, 2, 3, 4, 5)) + "\n\n\n");
		
		test.reset();
		
		logicGate(test);
		
		NetworkBuilder.writeFile(test, "hello", false);
		Network copy = NetworkBuilder.buildFromFile("files//networks//hello.nn");
		
		System.out.println("\n\n");
		
		System.out.println(copy.test(0, 0)[0]);
		System.out.println(copy.test(0, 1)[0]);
		System.out.println(copy.test(1, 0)[0]);
		System.out.println(copy.test(1, 1)[0]);
		
		System.out.println(copy);
		System.out.println(test);
	}
	
	public static void logicGate(Network neuralNet) {
		double[][][] dataSet = {{{0, 0}, {0}}, {{0, 1}, {1}}, {{1, 0}, {1}}, {{1, 1}, {0}}};
		neuralNet.uploadDataset(dataSet);
		neuralNet.setLearningRate(.5);
		
		for(int count = 0; count < 5000; count++) {
			neuralNet.train(true, true);
			System.out.println(neuralNet.getAverageLoss());
		}
		
		System.out.println("\n\n");
		
		System.out.println(neuralNet.test(0, 0)[0]);
		System.out.println(neuralNet.test(0, 1)[0]);
		System.out.println(neuralNet.test(1, 0)[0]);
		System.out.println(neuralNet.test(1, 1)[0]);
		
	}
}
