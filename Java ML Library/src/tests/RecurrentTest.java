package tests;

import java.util.Arrays;

import javaML.supervised.Network;
import javaML.supervised.NetworkBuilder;

public class RecurrentTest {
	public static void main(String[] args) {
		NetworkBuilder bob = new NetworkBuilder(5);
		bob.putLayer(Network.INPUT, 5, Network.LINEAR, true);
		bob.putLayer(Network.RECURRENT, 5, Network.SIGMOID, true);
		bob.putLayer(Network.RECURRENT, 5, Network.SIGMOID, true);
		bob.putLayer(Network.RECURRENT, 5, Network.SIGMOID, true);
		bob.putLayer(Network.HIDDEN, 5, Network.SIGMOID, true);
		bob.putLayer(Network.RECURRENT, 5, Network.SIGMOID, true);
		bob.putLayer(Network.OUTPUT, 2, Network.SIGMOID, true);
		
		Network net = bob.build(5);
		
		System.out.println(Arrays.toString(net.test(1, 2, 3, 4, 5)));
		System.out.println(Arrays.toString(net.test(1, 2, 3, 4, 5)));
		System.out.println(Arrays.toString(net.test(1, 2, 3, 4, 5)));
		System.out.println(Arrays.toString(net.test(1, 2, 3, 4, 5)));
		
	}
}
