package userInterface.dataInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import threads.DataWriter;

public class DataWindow implements ActionListener {
	
	Thread thread;
	DataWriter writer;
	
	JFrame frame;
	JTextField prompt;
	JLabel text;
	JLabel progress;
	JButton submit;
	
	boolean allowThread;
	
	final int frameW = 300, frameH = 160;
	final int buttonX = 200, buttonY = 40, buttonW = 80, buttonH = 30;
	final int fieldX = 20, fieldY = 40, fieldW = 180, fieldH = 30;
	final int textX = 90, textY = 20, textW = 120, textH = 20;
	final int progX = 20, progY = 90, progW = 260, progH = 20;
	
	public DataWindow() {
		frame = new JFrame();
		prompt = new JTextField();
		text = new JLabel();
		submit = new JButton();
		progress = new JLabel();
		
		writer = new DataWriter(this);
		thread = new Thread(writer);
		allowThread = true;
		
		initializeFrame();
	}
	
	private void initializeFrame() {
		frame.setLayout(null);
		frame.setBounds(0, 0, frameW, frameH);
		frame.setTitle("Dataset Creator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(false);
		frame.setResizable(false);
		
		prompt.setBounds(fieldX, fieldY, fieldW, fieldH);
		prompt.setText("AAPL");
		
		text.setBounds(textX, textY, textW, textH);
		text.setText("Enter Stock Code");
		
		submit.setBounds(buttonX, buttonY, buttonW, buttonH);
		submit.setFocusable(false);
		submit.setText("Submit");
		submit.addActionListener(this);
		
		progress.setBounds(progX, progY, progW, progH);
		progress.setText("");
		progress.setVisible(true);
		
		frame.add(prompt);
		frame.add(text);
		frame.add(submit);
		frame.add(progress);
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void hide() {
		frame.setVisible(false);
	}
	
	public void updateText(String text) {
		this.progress.setText(text);
	}
	
	public void resetThread() {
		allowThread = true;
		thread = new Thread(writer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(allowThread) {
			writer.setStock(prompt.getText());
			thread.start();
			allowThread = false;
		}
		
	}
}
