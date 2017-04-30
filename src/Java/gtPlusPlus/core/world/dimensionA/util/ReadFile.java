package gtPlusPlus.core.world.dimensionA.util;

import java.io.*;

public class ReadFile {
	
	private static int[] fileData;
	
	private static int numberOfLinesInFile;

	public static void findFileAndRead(String filePath){
		File file = new File(filePath);
		if(file.exists() == true){
			System.out.println("file Found @ " + file.getAbsolutePath());
			try {
				countLines(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			readFileStrings(file);
		}else{
			System.out.println("Can not find File");
		}
	}

	/**
	 * Count the Nukber of Lnes in the file.
	 * @param file
	 * @return int numberOfLines
	 * @throws IOException
	 */
	private static int countLines(File file) throws IOException{
		FileReader reader = new FileReader(file);
		BufferedReader buffedreader = new BufferedReader(reader);
		@SuppressWarnings("unused")
		String aLine;
		numberOfLinesInFile = 0;
		while((aLine = buffedreader.readLine()) != null){
			numberOfLinesInFile++;
		}
		buffedreader.close();
		return numberOfLinesInFile;
	}

	/**
	 * Reads a File as Strings.
	 */
	public static void readFileStrings(File file){
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffedreader = new BufferedReader(reader);
			String[] fileData = new String[numberOfLinesInFile];
			for(int i = 0; i < numberOfLinesInFile; i++){
				fileData[i] = buffedreader.readLine();
				System.out.println(fileData[i]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a File as ints.
	 */
	public static void readFileInts(File file){
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffedreader = new BufferedReader(reader);
			
			fileData = new int[numberOfLinesInFile];
			for(int i = 0; i < numberOfLinesInFile; i++){
				int inits = Integer.parseInt(buffedreader.readLine());
				fileData[i] = inits;
				System.out.println(fileData[i]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Get ints from the file **/
	public static int[] getIntList(){
		return fileData;
	}
}
