package stockdata;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Formatter;
import java.util.Scanner;

public class NasdaqScraper {
	
	/**
	 * Creates a file of historical data for a given stock identifier
	 * @param stockCode Stock identifier such as "AAPL"
	 * @return true if the file was successfully created
	 * @throws Exception
	 */
	public static boolean createFile(String stockCode) throws Exception {
		
		//Link to the Nasdaq dataset
		String link = String.format("https://data.nasdaq.com/api/v3/datasets/WIKI/%s.csv", stockCode);
		URL url = new URL(link);
		
		File folder = new File("files//data//");
		File file;
		
		//Opens connection to the nasdaq link
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		
		//Response code of 200 means the connection was successful
		if(con.getResponseCode() == 200) {
			if(!folder.exists()) folder.mkdirs();
			file = new File(String.format("files//data//%s.dat", stockCode));
			Formatter fileWriter = new Formatter(file);
			
			write(fileWriter, con);
			fileWriter.close();
			return true;
		}
		
		//return false if connection was unsuccessful (bad url or no internet connection)
		return false;
	}
	
	//Write file consisting of the data found on the nasdaq website
	private static void write(Formatter writer, HttpURLConnection webInfo) throws Exception {
		Scanner sc = new Scanner(webInfo.getInputStream());
		while(sc.hasNextLine()) writer.format("%s\n", sc.nextLine());
		sc.close();
	}
}
