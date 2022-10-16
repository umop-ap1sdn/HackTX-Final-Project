package threads;

import stockdata.NasdaqScraper;
import userInterface.dataInterface.*;

public class DataWriter implements Runnable {
	
	DataWindow window;
	String stock;
	
	public DataWriter(DataWindow window) {
		this.window = window;
		stock = "";
	}
	
	public void setStock(String stock) {
		this.stock = stock;
	}
	
	@Override
	public void run() {
		window.updateText("Writing file...");
		
		try {
			boolean success;
			success = NasdaqScraper.createFile(stock);
			
			if(success) window.updateText("File Successfully written");
			else window.updateText("File Write failed");
			
		} catch (Exception e) {
			window.updateText("File Write failed");
		}
		
		window.resetThread();
	}

}
