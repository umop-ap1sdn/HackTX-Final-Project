package tests;

import javaML.supervised.Network;
import javaML.supervised.NetworkBuilder;

public class LoadFileTest {
	public static void main(String[]args) {
		Network nn = NetworkBuilder.buildFromFile("files//networks//test.nn");
		Network nn2 = NetworkBuilder.buildFromFile("files//networks//test.nn");
		
		double[][][] dataset = createSineDataset(100);
		
		double output = 0;
		double output2 = 0;
		
		for(double[][] data: dataset) {
			output = nn.test(data[0])[0];
			output2 = nn2.test(data[0])[0];
		}
		
		
		for(int index = 0; index < 100; index++) {
			output = nn.test(output)[0];
			output2 = nn2.test(output2)[0];
			System.out.println(output);
			System.out.println(output2);
			
			System.out.println();
		}
		
		
		
		System.out.println(nn.toString().equals(nn2.toString()));
		
		//System.out.println(nn);
		//System.out.println(NetworkBuilder.writeFile(nn, "test", true));
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
