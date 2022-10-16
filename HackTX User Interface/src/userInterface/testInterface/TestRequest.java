package userInterface.testInterface;

import javax.swing.JFrame;

import formatting.ArrayFormatting;
import javaML.supervised.Network;
import javaML.supervised.NetworkBuilder;
import stockdata.DataLoader;

public class TestRequest {
	
	JFrame frame;
	
	Network rnn;
	double[][][] data;
	
	int dataLength;
	int underflowLength;
	
	double max, min;
	
	final boolean[] trainingPoints = {true, false, false, false, false, false, false, false, false, false, false, false};
	
	double[] graphData;
	
	public static final int sizeX = 720;
	public static final int sizeY = 540;
	
	String stock;
	
	GraphPanel graph;
	
	public TestRequest(String networkFile, String dataFile, int dataLength) {
		this.rnn = NetworkBuilder.buildFromFile(networkFile);
		stock = dataFile.substring(0, dataFile.length() - 4);
		
		buildDataset();
		
		this.underflowLength = dataLength / 4;
		
		graphData = new double[dataLength + underflowLength];
		fillGraphData();
		
		graph = new GraphPanel(graphData, dataLength, underflowLength, min, max);
		
		frame = new JFrame();
		setupFrame();
	}
	
	private void setupFrame() {
		//frame.setLayout(null);
		frame.setTitle("Testing Results for " + stock);
		frame.setBounds(0, 0, sizeX, sizeY);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(false);
		
		frame.add(graph);
	}
	
	public void run() {
		frame.setVisible(true);
	}
	
	private void buildDataset() {
		double[][] dataset = null;
		try {
			dataset = DataLoader.getData(stock, trainingPoints);
			findMaxMin(dataset);
			this.data = ArrayFormatting.createDataset(dataset);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void findMaxMin(double[][] dataset) {
		max = 0;
		min = dataset[0][0];
		
		for(double[] data: dataset) {
			if(data[0] > max) max = data[0];
			if(data[0] < min) min = data[0];
		}
	}
	
	private void fillGraphData() {
		double output = 0;
		for(double[][] d: data) {
			output = rnn.test(d[1])[0];
		}
		
		int index;
		for(index = 0; index < underflowLength; index++) {
			graphData[index] = data[(data.length - underflowLength) + index][1][0];
		}
		
		for(index = underflowLength; index < graphData.length; index++) {
			graphData[index] = output;
			output = rnn.test(output)[0];
			
		}
	}
}
