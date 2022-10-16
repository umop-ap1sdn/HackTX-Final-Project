package javaML.supervised;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Scanner;

import javaML.supervised.structures.networkElements.ConnectionLayer;
import javaML.supervised.structures.networkElements.NeuronLayer;
import javaML.supervised.structures.networkElements.ffLayerTypes.HiddenLayer;
import javaML.supervised.structures.networkElements.ffLayerTypes.InputLayer;
import javaML.supervised.structures.networkElements.ffLayerTypes.OutputLayer;
import javaML.supervised.structures.networkElements.recurrent.RecurrentConnectionLayer;
import javaML.supervised.structures.networkElements.recurrent.RecurrentLayer;

/**
 * NetworkBuilder is a class to be declared by the Client program<br>
 * This class is responsible for creating a network to the specifications of the user<br>
 * The use of this file allows the creation of networks to be simple and readable on Client side
 * @author Caleb Devon<br>
 * created on 10/14/2022
 *
 */
public class NetworkBuilder {
	
	private boolean allowInput, allowHidden, allowOutput, allowFinalize;
	private int memoryLength;
	private int numRecurrent;
	
	private InputLayer input;
	private OutputLayer output;
	private ArrayList<NeuronLayer> hiddenLayers;
	
	/**
	 * Standard constructor to be used in Client side code
	 * @param memoryLength Declared to specify how deep each neuron layer will remember inputs for.<br>
	 * This will be used during backpropagation to determine how many iterations will be run during
	 * a backpropagation routine
	 */
	public NetworkBuilder(int memoryLength) {
		this.memoryLength = memoryLength;
		allowInput = true;
		allowHidden = false;
		allowOutput = false;
		allowFinalize = false;
		
		numRecurrent = 0;
		
		hiddenLayers = new ArrayList<>();
		
	}
	
	/**
	 * Function to add a new layer to the in progress network
	 * @param layerType Declares what type of layer to be created<br>
	 * Use constants from the network class to define which layer type to create<br>
	 * Use Network.INPUT, Network.HIDDEN, Network.OUTPUT to declare different layers
	 * @param layerSize Declares the size of the layer not including a bias the user may or may not 
	 * choose to employ
	 * @param activation Declares the activation function to be used by the layer<br>
	 * This parameter is ignored when declaring Input layers since input layers can only use linear 
	 * activations<br>
	 * Use constants from the network class to define which activation function to be used
	 * These constants consist of Network.LINEAR, Network.RELU, Network.SIGMOID, and Network.TANH
	 * @param bias boolean value for whether to include a bias in the the layer<br>
	 * Bias nodes are nodes which connect to the layer ahead of itself<br>
	 * If the layer type is an output layer this parameter is ignored because biases are irrelevant
	 * for output layers
	 * @return Returns true if the layer was successfully created<br>
	 * In order for layers to be successful it must follow a few rules:<br>
	 * <ul>
	 * <li>First layer MUST be an Input layer</li>
	 * <li>After first layer is created, no more input layers can be created</li>
	 * <li>Network can support as many hidden layers as desired, but no more layers can be added after
	 * after an output layer</li>
	 * <li>After the output layer is created, Network MUST be finalized</li>
	 * </ul>
	 * 
	 */
	public boolean putLayer(int layerType, int layerSize, int activation, boolean bias) {
		switch(layerType) {
		case Network.INPUT:
			return putInputLayer(layerSize, bias);
		case Network.HIDDEN:
			return putHiddenLayer(layerSize, activation, bias);
		case Network.RECURRENT:
			return putRecurrentLayer(layerSize, activation, bias);
		case Network.OUTPUT:
			return putOutputLayer(layerSize, activation);
		}
		
		return false;
	}
	
	/**
	 * Private function that is called by the putLayer() function when the layerType is declared as
	 * an input layer
	 * @param layerSize Size of the layer, not including potential bias
	 * @param bias boolean for whether a bias is included
	 * @return true if the layer was successfully created
	 */
	private boolean putInputLayer(int layerSize, boolean bias) {
		if(!allowInput) return false;
		
		input = new InputLayer(layerSize, memoryLength, bias);
		
		allowHidden = true;
		allowOutput = true;
		allowInput = false;
		
		return true;
	}
	
	/**
	 * Private function that is called by the putLayer() function when the layerType is declared as
	 * a hidden layer
	 * @param layerSize Size of the layer, not including potential bias
	 * @param activation Activation function to be used by the layer
	 * @param bias boolean for whether a bias is included
	 * @return true if the layer was successfully created
	 */
	private boolean putHiddenLayer(int layerSize, int activation, boolean bias) {
		if(!allowHidden) return false;
		
		hiddenLayers.add(new HiddenLayer(layerSize, memoryLength, activation, bias));
		
		return true;
	}
	
	private boolean putRecurrentLayer(int layerSize, int activation, boolean bias) {
		if(!allowHidden) return false;
		
		numRecurrent++;
		hiddenLayers.add(new RecurrentLayer(layerSize, memoryLength, activation, bias));
		
		return true;
	}
	
	/**
	 * Private function that is called by the putLayer() function when the layerType is declared as
	 * an output layer
	 * @param layerSize Size of the layer, not including potential bias
	 * @param activation Activation function to be used by the layer
	 * @return true if the layer was successfully created
	 */
	private boolean putOutputLayer(int layerSize, int activation) {
		if(!allowOutput) return false;
		
		output = new OutputLayer(layerSize, memoryLength, activation);
		
		allowHidden = false;
		allowOutput = false;
		allowFinalize = true;
		
		return true;
	}
	
	/**
	 * Function to be called in the client side code to finalize the construction of a network
	 * @param batchSize value that will be set as the default for the batch that will be trained anytime
	 * the train() function from the Network class is called 
	 * @return returns the newly built network if the network is valid<br>
	 * If the network is not ready to be finalized (no input/output currently exists) then a null will
	 * be returned
	 * 
	 */
	public Network build(int batchSize) {
		if(!allowFinalize) return null;
		
		
		//Put hidden layers into an array
		NeuronLayer[] hidden = new NeuronLayer[hiddenLayers.size()];
		
		for(int index = 0; index < hidden.length; index++) {
			hidden[index] = hiddenLayers.get(index);
		}
		
		//Initialize the array for connection Layers
		ConnectionLayer[] cLayers = new ConnectionLayer[hidden.length + 1];
		
		//Create rConnects array
		RecurrentConnectionLayer[] rConnects = new RecurrentConnectionLayer[numRecurrent];
		
		if(hidden.length > 0) {
			
			//Builds connection layers between each layer
			cLayers[0] = new ConnectionLayer(input, (HiddenLayer) hidden[0]);
			
			int rIndex = 0;
			if(hidden[0] instanceof RecurrentLayer) 
				rConnects[rIndex++] = new RecurrentConnectionLayer((RecurrentLayer)hidden[0]);
			
			for(int index = 1; index < hidden.length; index++) {
				cLayers[index] = new ConnectionLayer((HiddenLayer) hidden[index - 1], (HiddenLayer) hidden[index]);
				
				if(hidden[index] instanceof RecurrentLayer)
					rConnects[rIndex++] = new RecurrentConnectionLayer((RecurrentLayer)hidden[index]);
					
			}
			
			
			int last = hidden.length - 1;
			cLayers[last + 1] = new ConnectionLayer((HiddenLayer) hidden[last], output);
			
		} else cLayers[0] = new ConnectionLayer(input, output); 
		//If network is created with no hidden layers
		// a connection layer is created between input and output instead
		
		return new Network(input, output, hidden, cLayers, rConnects, batchSize, memoryLength);
	}
	
	public static boolean writeFile(Network network, String fileName, boolean avoidOverwriting) {
		try {
			File folder = new File("files//networks//");
			if(!folder.exists()) folder.mkdirs();
			
			String path = String.format("files//networks//%s", fileName);
			String extension = ".nn";
			
			int addition = 0;
			
			File file;
			
			if(avoidOverwriting) {
				do {
					file = new File(String.format("%s%d%s", path, addition++, extension));
				} while(file.exists());
			} else {
				file = new File(path + extension);
			}
			
			Formatter fileWriter = new Formatter(file);
			
			fileWriter.format("%s", network);
			
			fileWriter.close();
			
			if(file.exists()) return true;
			
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
	
	public static Network buildFromFile(String path) {
		try {
			File file = new File(path);
			Scanner sc = new Scanner(file);
			
			LinkedList<String> instructions = new LinkedList<>();
			
			while(sc.hasNextLine()) {
				instructions.addLast(sc.nextLine());
			}
			
			sc.close();
			
			//line 1 - memoryLength and batchSize
			String[] start = instructions.pollFirst().split(",");
			NetworkBuilder bob = new NetworkBuilder(Integer.parseInt(start[0]));
			int batchSize = Integer.parseInt(start[1]);
			
			//Lines 2 until "-"
			//Networkbuilder to build layers
			buildLayers(instructions, bob);
			Network nn = bob.build(batchSize);
			
			//Next lines until "--" connection layers
			instructions.pollFirst();
			buildConnectionLayers(instructions, nn, false);
			instructions.pollFirst();
			buildConnectionLayers(instructions, nn, true);
			
			return nn;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static void buildLayers(LinkedList<String> instructions, NetworkBuilder bob) {
		while(!instructions.get(0).equals("-")) {
			String[] arr = instructions.pollFirst().split(",");
			
			int type = Integer.parseInt(arr[0]);
			int size = Integer.parseInt(arr[1]);
			int activation = Integer.parseInt(arr[2]);
			int bias = Integer.parseInt(arr[3]);
			
			bob.putLayer(type, size, activation, bias == 1);
		}
	}
	
	private static void buildConnectionLayers(LinkedList<String> instructions, Network nn, boolean rConnects) {
		ArrayList<double[]> vals = new ArrayList<>();
		
		int index = 0;
		
		while(!instructions.get(0).equals("--") && instructions.size() > 0) {
			String data = instructions.pollFirst();
			if(data.equals("-")) {
				if(rConnects) nn.getRLayers()[index++].setMatrix(buildArray(vals));
				else nn.getCLayers()[index++].setMatrix(buildArray(vals));
				
				vals = new ArrayList<>();
			}
			else vals.add(StringtoDub(data.split(",")));
		}
		
		
	}
	
	private static double[] StringtoDub(String[] arr) {
		double[] ret = new double[arr.length];
		for(int index = 0; index < arr.length; index++) {
			ret[index] = Double.parseDouble(arr[index]);
		}
		
		return ret;
	}
	
	private static double[][] buildArray(ArrayList<double[]> list){
		double[][] ret = new double[list.size()][list.get(0).length];
		
		for(int index = 0; index < ret.length; index++) {
			ret[index] = list.get(index);
		}
		
		return ret;
	}
}
