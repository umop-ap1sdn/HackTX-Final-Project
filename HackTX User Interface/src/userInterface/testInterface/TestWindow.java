package userInterface.testInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TestWindow implements ActionListener {
	
	//4 Items total Currently + Button
	
	JFrame frame;
	JComboBox<String> dataInput;
	JComboBox<String> presetInput;
	JTextField iterationsInput;
	JButton submit;
	
	JLabel data, preset, iterations;
	
	final int frameW = 400, frameH = 500;
	final int labelX = 40, labelW = 120, labelH = 20;
	final int itemX = 180, itemW = 140, itemH = 20;
	
	TestRequest test;
	
	public TestWindow() {
		frame = new JFrame();
		iterationsInput = new JTextField();
		submit = new JButton();
		
		data = new JLabel();
		preset = new JLabel();
		iterations = new JLabel();
		
		initializeFrame();
		
		test = null;
	}
	
	private void initializeFrame() {
		frame.setBounds(0, 0, frameW, frameH);
		frame.setLayout(null);
		frame.setTitle("Testing Window");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(false);
		
		setupDataset();
		frame.add(data);
		frame.add(dataInput);
		
		setupPreset();
		frame.add(preset);
		frame.add(presetInput);
		
		setupIterations();
		frame.add(iterations);
		frame.add(iterationsInput);
		
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
		preset.setText("Select Network");
		preset.setBounds(labelX, 60, labelW, labelH);
		presetInput.setBounds(itemX, 60, itemW, itemH);
		
		presetInput.setFocusable(false);
	}
	
	private void setupIterations() {
		iterations.setBounds(labelX, 100, labelW, labelH);
		iterations.setText("Enter Length");
		
		iterationsInput.setBounds(itemX, 100, itemW, itemH);
		iterationsInput.setFocusable(true);
		iterationsInput.setText("100");
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
			
			if(test != null) {
				test.run();
			}
		}
		if(e.getSource() == dataInput) reloadPreset();
	}
	
	private void trySubmit() {
		test = null;
		
		try {
			String trainFile = (String)dataInput.getSelectedItem();
			
			if(trainFile.equals("Select")) return;
			int iterations = Integer.parseInt(iterationsInput.getText());
			
			if(iterations <= 0) return;
			
			String presetNN = (String)presetInput.getSelectedItem();
			
			if(presetNN != null && !presetNN.equals("Select")) {
				String path = "files//networks//" + presetNN;
				test = new TestRequest(path, trainFile, iterations);
				
			} else return;
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
