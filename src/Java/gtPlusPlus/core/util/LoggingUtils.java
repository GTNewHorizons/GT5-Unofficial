package gtPlusPlus.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LoggingUtils {

	public static void profileLog(Object o){    	
    	try {
			String content;
			File file = new File("GregtechTimingsTC.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("============================================================");
				bw.write(System.lineSeparator());
				bw.close();
			}			
			if (o instanceof String){
				content = (String) o;
    		}
    		else {
    	    content = o.toString();
    		}		
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.write(System.lineSeparator());
			bw.close();
			System.out.println("Data Logged.");

		} catch (IOException e) {
			System.out.println("Data logging failed.");
		}
    }
	
	public static boolean logCurrentSystemTime(String message){
		Date date = new Date(System.currentTimeMillis());
    	try {
		profileLog(message+" | "+date.toString());
		return true;
    	}
    	catch (Throwable r) {
    		return false;
    	}
    	
	}
	
}
