package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import userInterface.dataInterface.DataWindow;
import userInterface.testInterface.TestWindow;
import userInterface.trainInterface.TrainWindow;

public class StartingWindow implements ActionListener{
	
	JFrame frame;
	JButton train;
	JButton test;
	JButton update;
	
	JLabel dataProgress;
	
	final int frameW = 300, frameH = 280;
	final int buttonW = 180, buttonH = 30, buttonX = 60;
	
	DataWindow data;
	TrainWindow training;
	TestWindow testing;
	
	public StartingWindow() {
		frame = new JFrame();
		train = new JButton();
		test = new JButton();
		update = new JButton();
		
		data = new DataWindow();
		training = new TrainWindow();
		testing = new TestWindow();
		
		initializeFrame();
	}
	
	private void initializeFrame() {
		frame.setTitle("Stock Price Prediction");
		frame.setBounds(0, 0, frameW, frameH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		
		train.setText("Train");
		train.setBounds(buttonX, 40, buttonW, buttonH);
		train.setFocusable(false);
		test.setText("Test");
		test.setBounds(buttonX, 100, buttonW, buttonH);
		test.setFocusable(false);
		update.setText("Build Dataset");
		update.setBounds(buttonX, 160, buttonW, buttonH);
		update.setFocusable(false);
		
		train.addActionListener(this);
		test.addActionListener(this);
		update.addActionListener(this);
		
		frame.add(train);
		frame.add(test);
		frame.add(update);
		
		frame.setVisible(false);
	}
	
	//Needs to have the ability to "Test", "Train", and "Update Dataset"
	public void show() {
		frame.setVisible(true);
	}
	
	//Close the window
	public void hide() {
		frame.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == update) data.show();
		if(e.getSource() == train) {
			training.reloadFiles();
			training.show();
		}
		if(e.getSource() == test) {
			testing.reloadFiles();
			testing.show();
		}
		
		
	}
}
