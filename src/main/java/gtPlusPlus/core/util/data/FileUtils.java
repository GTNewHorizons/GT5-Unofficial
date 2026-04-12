package gtPlusPlus.core.util.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import gtPlusPlus.core.util.Utils;

public class FileUtils {

    public static boolean doesFileExist(File f) {
        return f != null && f.exists() && !f.isDirectory();
    }

    public static File createFile(String path, String filename, String extension) {
        File file = new File(Utils.getMcDir(), path + filename + extension);
        return createFile(file);
    }

    public static File createFile(File aFile) {
        boolean blnCreated;
        try {
            blnCreated = aFile.createNewFile();
        } catch (IOException ioe) {
            return null;
        }
        return blnCreated ? aFile : null;
    }

    public static File getFile(String path, String filename, String extension) {
        if (path == null || path.length() == 0) {
            path = "";
        } else {
            path = path + "/";
        }
        if (filename == null || filename.length() == 0) {
            return null;
        }
        if (extension == null || extension.length() == 0) {
            extension = ".txt";
        } else {
            extension = "." + extension;
        }
        File file = new File(Utils.getMcDir(), path + filename + extension);
        boolean doesExist = doesFileExist(file);

        if (doesExist) {
            return file;
        } else {
            return createFile(path, filename, extension);
        }
    }

    public static void appendListToFile(File file, List<String> content) {
        if (doesFileExist(file)) {
            Path p = Paths.get(file.getPath());
            if (Files.isWritable(p)) {
                try {
                    Files.write(p, content, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
