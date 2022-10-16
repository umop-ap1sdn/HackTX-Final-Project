package tests;

import javaML.supervised.Network;
import javaML.supervised.NetworkBuilder;

public class SineDataTest {
	public static void main(String[]args) {
		NetworkBuilder bob = new NetworkBuilder(20);
		bob.putLayer(Network.INPUT, 1, Network.LINEAR, true);
		bob.putLayer(Network.RECURRENT, 18, Network.TANH, true);
		bob.putLayer(Network.RECURRENT, 18, Network.TANH, true);
		bob.putLayer(Network.RECURRENT, 18, Network.SIGMOID, false);
		bob.putLayer(Network.OUTPUT, 1, Network.TANH, true);
		
		Network net = bob.build(10);
		
		double[][][] dataset = createSineDataset(100);
		
		net.uploadDataset(dataset);
		net.setLearningRate(0.01);
		
		for(int count = 0; count < 10000; count++) {
			net.train(true, true);
			System.out.println(net.getAverageLoss());
		}
		
		System.out.println("\n\n");
		
		net.reset();
		
		double output = 0;
		for(int index = 0; index < dataset.length; index++) {
			output = net.test(dataset[index][0])[0];
		}
		
		for(int index = 0; index < 100; index++) {
			output = net.test(output)[0];
			System.out.println(output);
		}
		
		System.out.println(NetworkBuilder.writeFile(net, "test", false));
		
		Network nn = NetworkBuilder.buildFromFile("files//networks//test.nn");
		
		System.out.println("\n\n");
		
		for(int index = 0; index < dataset.length; index++) {
			output = nn.test(dataset[index][0])[0];
		}
		
		for(int index = 0; index < 100; index++) {
			output = nn.test(output)[0];
			System.out.println(output);
		}
		
	}
	
	public static double[][][] createSineDataset(int size){
		
		double[][][] ret = new double[size][2][1];
		
		for(int index = 0; index < ret.length; index++) {
			double test = Math.sin((double)index / 4);
			double targ = Math.sin(((double)index / 4) + 0.25);
			
			ret[index][0][0] = test;
			ret[index][1][0] = targ;
		}
		
		return ret;
	}
}
