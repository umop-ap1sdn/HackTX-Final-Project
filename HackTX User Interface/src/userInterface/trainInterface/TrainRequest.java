package userInterface.trainInterface;

import javax.swing.JFrame;
import javax.swing.JLabel;

import formatting.ArrayFormatting;
import javaML.supervised.Network;
import javaML.supervised.NetworkBuilder;
import stockdata.DataLoader;
import threads.TrainingThread;

public class TrainRequest {
	JFrame frame;
	JLabel error;
	JLabel progress;
	
	String dataFile;
	String stock;
	
	final boolean[] trainingPoints = {true, false, false, false, false, false, false, false, false, false, false, false};
	final int ioSize = 1;
	
	Network rnn;
	double[][][] dataset;
	
	final int frameW = 400, frameH = 400;
	final int errorX = 50, errorY = 80, errorW = 300, errorH = 20;
	final int progX = 50, progY = 200, progW = 300, progH = 40;
	
	int iterations;
	int prog;
	
	TrainingThread train;
	Thread thread;
	
	public TrainRequest(String dataFile, int iterations, double lr) {
		this.dataFile = dataFile;
		rnn = buildNetwork();
		rnn.setLearningRate(lr);
		
		frame = new JFrame();
		error = new JLabel();
		progress = new JLabel();
		
		prog = 0;
		this.iterations = iterations;
		this.dataset = null;
		
		initialize();
		initializeFrame();
		
		rnn.uploadDataset(dataset);
		
		train = new TrainingThread(rnn, this, iterations);
		thread = new Thread(train);
	}
	
	public TrainRequest(String presetPath, String dataFile, int iterations, double lr) {
		this.dataFile = dataFile;
		rnn = NetworkBuilder.buildFromFile(presetPath);
		rnn.setLearningRate(lr);
		
		frame = new JFrame();
		error = new JLabel();
		progress = new JLabel();
		
		prog = 0;
		this.iterations = iterations;
		
		this.dataset = null;
		
		initialize();
		initializeFrame();
		
		rnn.uploadDataset(dataset);
		
		train = new TrainingThread(rnn, this, iterations);
		thread = new Thread(train);
	}
	
	protected void run() {
		frame.setVisible(true);
		thread.start();
	}
	
	private void initializeFrame() {
		frame.setLayout(null);
		frame.setBounds(0, 0, frameW, frameH);
		frame.setTitle("Training Network");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(false);
		
		error.setBounds(errorX, errorY, errorW, errorH);
		updateError(0);
		progress.setBounds(progX, progY, progW, progH);
		updateProgress(0);
		
		frame.add(error);
		frame.add(progress);
		
	}
	
	public void updateError(double loss) {
		error.setText(String.format("Error for %s Network: %.5f", stock, loss));
	}
	
	public void updateProgress(int increment) {
		prog += increment;
		
		double ratio = 100 * ((double)prog / iterations);
		
		progress.setText(String.format("<html><body>%d / %d iterations<br>%.2f%% complete.</body></html>", prog, iterations, ratio));
	}
	
	public void finish() {
		NetworkBuilder.writeFile(rnn, stock, true);
	}
	
	private void initialize() {
		stock = dataFile.substring(0, dataFile.length() - 4);
		double[][] dataset = null;
		
		try{
			dataset = DataLoader.getData(stock, trainingPoints);
			this.dataset = ArrayFormatting.createDataset(dataset);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Network buildNetwork() {
		NetworkBuilder bob = new NetworkBuilder(25);
		bob.putLayer(Network.INPUT, ioSize, Network.LINEAR, true);
		//bob.putLayer(Network.RECURRENT, 30, Network.SIGMOID, true);
		//bob.putLayer(Network.RECURRENT, 30, Network.SIGMOID, true);
		bob.putLayer(Network.RECURRENT, 30, Network.SIGMOID, true);
		bob.putLayer(Network.RECURRENT, 30, Network.SIGMOID, true);
		bob.putLayer(Network.OUTPUT, ioSize, Network.SIGMOID, false);
		return bob.build(15);
	}
}
