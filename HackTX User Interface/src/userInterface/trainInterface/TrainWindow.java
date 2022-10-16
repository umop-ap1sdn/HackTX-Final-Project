package userInterface.trainInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TrainWindow implements ActionListener {
	
	//4 Items total Currently + Button
	
	JFrame frame;
	JComboBox<String> dataInput;
	JComboBox<String> presetInput;
	JCheckBox presetCheck;
	JTextField iterationsInput;
	JTextField lrInput;
	JButton submit;
	
	JLabel data, preset, iterations, lr;
	
	final int frameW = 400, frameH = 500;
	final int labelX = 40, labelW = 120, labelH = 20;
	final int itemX = 180, itemW = 140, itemH = 20;
	
	TrainRequest train;
	
	public TrainWindow() {
		frame = new JFrame();
		presetCheck = new JCheckBox();
		iterationsInput = new JTextField();
		lrInput = new JTextField();
		submit = new JButton();
		
		data = new JLabel();
		preset = new JLabel();
		iterations = new JLabel();
		lr = new JLabel();
		
		initializeFrame();
		
		train = null;
	}
	
	private void initializeFrame() {
		frame.setBounds(0, 0, frameW, frameH);
		frame.setLayout(null);
		frame.setTitle("Training Window");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(false);
		
		setupDataset();
		frame.add(data);
		frame.add(dataInput);
		
		setupPreset();
		frame.add(preset);
		frame.add(presetInput);
		frame.add(presetCheck);
		
		setupIterations();
		frame.add(iterations);
		frame.add(iterationsInput);
		
		setupLR();
		frame.add(lr);
		frame.add(lrInput);
		
		submit.setBounds(itemX + 120, 300, 80, 40);
		submit.setText("Submit");
		submit.setFocusable(false);
		submit.addActionListener(this);
		frame.add(submit);
	}
	
	private void setupDataset() {
		String[] options = loadOptions("files//data//");
		
		dataInput = new JComboBox<>(options);
		data.setBounds(labelX, 20, labelW, labelH);
		data.setText("Select Stock");
		dataInput.setBounds(itemX, 20, itemW, itemH);
		dataInput.setFocusable(false);
		
		dataInput.addActionListener(this);
	}
	
	private void setupPreset() {
		String stock = (String)dataInput.getSelectedItem();
		stock = stock.substring(0, stock.length() - 4);
		String[] options = loadOptions("files//networks//" + stock + "//");
		
		presetInput = new JComboBox<>(options);
		preset.setText("Select Preset");
		preset.setBounds(labelX, 60, labelW, labelH);
		presetInput.setBounds(itemX, 60, itemW, itemH);
		presetCheck.setBounds(itemX + 160, 60, 20, 20);
		
		presetInput.setFocusable(false);
		presetCheck.setFocusable(false);
	}
	
	private void setupIterations() {
		iterations.setBounds(labelX, 100, labelW, labelH);
		iterations.setText("Enter Iterations");
		
		iterationsInput.setBounds(itemX, 100, itemW, itemH);
		iterationsInput.setFocusable(true);
		iterationsInput.setText("10000");
	}
	
	private String[] loadOptions(String file) {
		String[] list = {};
		
		File file1 = new File(file);
		if(file1.exists()) list = file1.list();
		
		String[] options = new String[list.length + 1];
		options[0] = "Select";
		
		for(int index = 0; index < list.length; index++) options[index + 1] = list[index];
		
		return options;
	}
	
	private void setupLR() {
		lr.setBounds(labelX, 140, labelW, labelH);
		lr.setText("Enter Learning Rate");
		
		lrInput.setBounds(itemX, 140, itemW, itemH);
		lrInput.setFocusable(true);
		lrInput.setText("0.02");
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void hide() {
		frame.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == submit) {
			trySubmit();			
			if(train != null) {
				train.run();
			}
		}
		if(e.getSource() == dataInput) reloadPreset();
	}
	
	private void trySubmit() {
		train = null;
		
		try {
			String trainFile = (String)dataInput.getSelectedItem();
			
			if(trainFile.equals("Select")) return;
			
			
			int iterations = Integer.parseInt(iterationsInput.getText());
			double lr = Double.parseDouble(lrInput.getText());
			
			if(iterations <= 0|| lr <= 0) return;
			
			String presetNN = (String)presetInput.getSelectedItem();
			boolean pres = presetCheck.isSelected();
			
			if(presetNN != null && !presetNN.equals("Select") && pres) {
				String path = "files//networks//" + presetNN;
				train = new TrainRequest(path, trainFile, iterations, lr);
			} else {
				train = new TrainRequest(trainFile, iterations, lr);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return;
		}
	}
	
	public void reloadFiles() {
		dataInput.removeAllItems();
		String[] options = loadOptions("files//data//");
		
		for(String s: options) dataInput.addItem(s);
	}
	
	private void reloadPreset() {
		String stock = (String)dataInput.getSelectedItem();
		if(stock == null) return;
		
		stock = stock.substring(0, stock.length() - 4);
		String[] options = loadOptions("files//networks//");
		
		presetInput.removeAllItems();
		
		for(String s: options) presetInput.addItem(s);
	}
}
