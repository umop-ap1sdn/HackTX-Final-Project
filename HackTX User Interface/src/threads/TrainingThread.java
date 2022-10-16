package threads;

import javaML.supervised.Network;
import userInterface.trainInterface.TrainRequest;

public class TrainingThread implements Runnable{
	
	Network rnn;
	int iterations;
	TrainRequest train;
	
	public TrainingThread(Network rnn, TrainRequest train, int iterations) {
		this.rnn = rnn;
		this.train = train;
		this.iterations = iterations;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for(int count = 0; count < iterations; count++) {
			rnn.train(true, true);
			if((count + 1) % 10 == 0 && count != 0) {
				train.updateError(rnn.getAverageLoss());
				train.updateProgress(10);
			}
		}
		
		train.finish();
	}

}
