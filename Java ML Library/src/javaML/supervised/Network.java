package javaML.supervised;

import javaML.supervised.structures.networkElements.*;
import javaML.supervised.structures.networkElements.ffLayerTypes.*;
import javaML.supervised.structures.networkElements.recurrent.RecurrentConnectionLayer;

/**
 * 
 * The network class is the motherboard of the supervised learning package<br>
 * This class is responsible for running all of the routines including:
 * forward pass, loss calculation, error propagation, and backpropagation<br><br>
 * 
 * This class can only be constructed by the NetworkBuilder class and can be
 * attained by the user by following the steps of the NetworkBuilder class up to the build(int) function
 * 
 * @author Caleb Devon<br>
 * Created 10/14/2022
 * 
 */

public class Network {
	
	/**
	 * Publicly accessible constant for activation type
	 */
	public static final int LINEAR = 1, RELU = 2, TANH = 3, SIGMOID = 4;
	
	
	/**
	 * Publicly accessible constant for layer type
	 */
	public static final int INPUT = 5, HIDDEN = 6, RECURRENT = 7, OUTPUT = 8;
	private double learning_rate = 0.02;
	
	private InputLayer input;
	private OutputLayer output;
	private NeuronLayer[] hiddenLayers;
	private ConnectionLayer[] cLayers;
	private RecurrentConnectionLayer[] rLayers;
	
	private int batchSize;
	private int memoryLength;
	
	private double[][][] dataset;
	private int dataIndex = 0;
	
	private double totalLoss = 0, averageLoss = 0;
	
	/**
	 * Constructor for use only by the NetworkBuilder class
	 * @param input Input Layer
	 * @param output Output Layer
	 * @param hiddenLayers Array for Hidden Layers
	 * @param cLayers Array for Connection Layers
	 * @param batchSize value for number of training steps between pauses
	 * @param memoryLength value for how deep the memory of neuron layers go
	 */
	protected Network(InputLayer input, OutputLayer output, NeuronLayer[] hiddenLayers, ConnectionLayer[] cLayers, RecurrentConnectionLayer[] rLayers, int batchSize, int memoryLength) {
		this.input = input;
		this.output = output;
		this.hiddenLayers = hiddenLayers;
		this.cLayers = cLayers;
		this.rLayers = rLayers;
		this.batchSize = batchSize;
		this.memoryLength = memoryLength;
		
		this.dataset = null;
	}
	
	/**
	 * Sets a new value to the learning rate<br>
	 * Defaults to 0.02 upon construction
	 * @param lr new learning rate
	 */
	public void setLearningRate(double lr) {
		this.learning_rate = lr;
	}
	
	/**
	 * Use this function to upload a user made dataset<br>
	 * In order for training to work properly data should be formatted such that
	 * { { {Input Data 2}, {Target Output Data 1} }, { {Input Data 2}, {Target Output Data 2} }, ... }
	 * @param dataset dataset to be loaded
	 */
	public void uploadDataset(double[][][] dataset) {
		this.dataset = dataset;
		this.dataIndex = 0;
	}
	
	/**
	 * Primary algorithm to be called by the user to train the network<br>
	 * Network will run through the dataset uploaded to run the forwardPass(), propagateErrors(),
	 * and backpropagate() functions
	 * @param backProp set to true if backpropagation is desired
	 * @param dependency if set to true, backpropagation will be restricted if memory has not been filled
	 * with relevant data<br>
	 * Will be used with RNN's that should only be backpropagated if the memory is full
	 * @param batchSize the amount of training steps to run in a particular train routine
	 * @return Returns true if an overflow occured when reading through the data<br>
	 * Will be useful for RNN's
	 */
	public boolean train(boolean backProp, boolean dependency, int batchSize) {
		if(dataset == null) return false;
		
		totalLoss = 0;
		averageLoss = 0;
		
		boolean overflow = false;
		
		double[] output;
		
		//Run through the dataset
		for(int index = 0; index < batchSize; index++) {
			
			//Test data, calculate errors, add errors to loss value
			output = test(dataset[dataIndex][0]);
			propagateError(dataset[dataIndex][1]);
			calculateLoss(dataset[dataIndex][1], output);
			
			//ensure dataIndex never reaches out of bounds for the dataset array
			dataIndex = (dataIndex + 1) % dataset.length;
			
			//When data is reset an overflow has occured
			if(dataIndex == 0 && index < batchSize - 1) overflow = true;
		}
		
		//Backpropagate if and only if desired
		if(backProp && (!dependency || dataIndex >= memoryLength || dataIndex == 0)) {
			backpropagate();
		}
		
		averageLoss = totalLoss / batchSize;
		
		return overflow;
	}
	
	/**
	 * Basic Train Function that defaults to the specified batch size when training
	 * @param backProp
	 * @param dependency
	 * @return
	 */
	public boolean train(boolean backProp, boolean dependency) {
		return train(backProp, dependency, batchSize);
	}
	
	/**
	 * Function to test a particular set of inputs
	 * @param inputs Input vector of size equal to input layer size
	 * @return returns the array of outputs produced by the output layer through forward propagation
	 */
	public double[] test(double... inputs) {
		forwardPass(inputs);
		return output.getOutputs();
	}
	
	/**
	 * Function to reset the values of each Neuron Layer<br>
	 * This will be useful for RNN's whose outputs are influenced by what is already stored in Neuron
	 * Layer memory
	 */
	public void reset() {
		input.reset();
		output.reset();
		for(int index = 0; index < hiddenLayers.length; index++) {
			hiddenLayers[index].reset();
		}
	}
	
	/**
	 * Function to be called only natively by the Network class<br>
	 * Runs the forward propagation algorithm for all layers
	 * @param inputs array of inputs to be given to the input layer
	 */
	private void forwardPass(double[] inputs) {
		input.setInputs(inputs);
		input.runActivation();
		for(int index = 0; index < hiddenLayers.length; index++) {
			hiddenLayers[index].runActivation();
		}
		output.runActivation();
	}
	
	/**
	 * Function to be called only natively by the Network class<br>
	 * Runs the calculateError function for each Neuron Layer
	 * @param target array for the target values for a particular set of inputs
	 */
	private void propagateError(double[] target) {
		output.calculateErrors(target);
		for(int index = hiddenLayers.length - 1; index >= 0; index--) {
			hiddenLayers[index].calculateErrors(target);
		}
		input.calculateErrors(target);
	}
	
	/**
	 * Function to be called only natively by the Network class<br>
	 * Runs the adjustWeights function for each layer of connections
	 */
	private void backpropagate() {
		for(int index = 0; index < cLayers.length; index++) {
			cLayers[index].adjustWeights(learning_rate);
		}
		
		for(int index = 0; index < rLayers.length; index++) {
			rLayers[index].adjustWeights(learning_rate);
		}
	}
	
	/**
	 * Function to be called only natively by the Network class<br>
	 * Runs the MSE (Mean Squared Error) algorithm to calculate overall loss of a batch
	 * @param target array of target values
	 * @param output array of output values
	 */
	private void calculateLoss(double[] target, double[] output) {
		//1/n (t - y)^2
		//n = number of output neurons
		//t is the target
		//y is the output
		double sum = 0;
		for(int index = 0; index < target.length; index++) {
			sum += Math.pow(target[index] - output[index], 2);
		}
		
		sum /= target.length;
		
		totalLoss += sum;
	}
	
	/**
	 * Function to get the overall loss of a batch
	 * @return calculated loss
	 */
	public double getTotalLoss() {
		return this.totalLoss;
	}
	
	/**
	 * Function to get the average loss per element in a batch
	 * @return totalLoss / batchSize
	 */
	public double getAverageLoss() {
		return this.averageLoss;
	}
	
	protected ConnectionLayer[] getCLayers() {
		return this.cLayers;
	}
	
	protected RecurrentConnectionLayer[] getRLayers() {
		return this.rLayers;
	}
	
	@Override
	public String toString() {
		String ret = String.format("%s,%s\n", memoryLength, batchSize);
		
		ret += input;
		for(NeuronLayer n: hiddenLayers) ret += n;
		ret += output;
		ret += "-";
		
		for(ConnectionLayer c: cLayers) ret += "\n" + c + "\n-";
		ret += "\n-";
		for(RecurrentConnectionLayer c: rLayers) ret += "-\n" + c + "\n";
		ret += "-\n--";
		
		return ret;
	}
}