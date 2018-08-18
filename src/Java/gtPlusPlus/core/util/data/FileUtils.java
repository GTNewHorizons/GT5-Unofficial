package gtPlusPlus.core.util.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;

public class FileUtils {

	private static final Charset utf8 = StandardCharsets.UTF_8;

	public static boolean doesFileExist(File f) {
		if (f != null && f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	public static File createFile(String path, String filename, String extension) {
		File file = new File(Utils.getMcDir(), path + filename + extension);
		boolean blnCreated = false;
		Logger.INFO("Trying to use path "+file.getPath());
		try {
			Logger.INFO("Trying to use path "+file.getCanonicalPath());
			Logger.INFO("Trying to use absolute path "+file.getAbsolutePath());
			blnCreated = file.createNewFile();
		} catch (IOException ioe) {
			Logger.INFO("Error while creating a new empty file :" + ioe);
			return null;
		}
		return blnCreated ? file : null;
	}

	public static File getFile(String filename, String extension) {
		return getFile("", filename, extension);
	}

	public static File getFile(String path, String filename, String extension) {
		if (path == null || path.length() <= 0) {
			path = "";
		}
		else {
			path = path + "/";
		}
		if (filename == null || filename.length() <= 0) {
			return null;
		}
		if (extension == null || extension.length() <= 0) {
			extension = ".txt";
		}
		else {
			extension = "." + extension;
		}
		File file = new File(Utils.getMcDir(), path + filename + extension);
		boolean doesExist = doesFileExist(file);

		if (doesExist) {
			Logger.INFO("Found File: " + file.getAbsolutePath());
			return file;
		} else {
			Logger.INFO("Creating file, as it was not found.");
			return createFile(path, filename, extension);	
		}
	}

	public static boolean appendListToFile(File file, List<String> content) {
		try {
			long oldSize;
			long newSize;
			if (doesFileExist(file)) {
				Path p = Paths.get(file.getPath());		
				if (p != null && Files.isWritable(p)) {
					oldSize = Files.size(p);	
					try {	
						Files.write(p, content, utf8, StandardOpenOption.APPEND);
					} catch (IOException e) {
						e.printStackTrace();
					}
					newSize = Files.size(p);
					return newSize > oldSize;
				}
			}
		} catch (IOException e) {
		}
		return false;
	}
}
