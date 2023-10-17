package gtPlusPlus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import gregtech.api.objects.XSTR;

public class GenerateDictionaries {

    public static void main(String[] args) {

        File aMainDictionary = new File("proguard/DictionaryMain.txt");
        File aMethodDict = new File("proguard/method-dict.txt");
        File aClassDict = new File("proguard/class-dict.txt");

        if (Utils.doesFileExist(aMainDictionary)) {
            Utils.log("Found Main Dictionary");
            List<String> aLines = Utils.readLines(aMainDictionary);

            ArrayList<String> aLinesToWriteMethods = new ArrayList<String>();
            ArrayList<String> aLinesToWriteClasses = new ArrayList<String>();

            if (aLines != null && aLines.size() > 0) {
                Utils.log("Main Dictionary has > 0 keywords (" + aLines.size() + ")");
                HashSet<Integer> aUsedIndicies = new HashSet<Integer>();
                int aCount = aLines.size() / 5;

                Utils.log("Mapping " + aCount + " to each dict.");
                // Map New Method Names
                for (int i = 0; aLinesToWriteMethods.size() < aCount; i++) {
                    Integer aIndex = Utils.randInt(0, aLines.size() - 1);
                    if (!aUsedIndicies.contains(aIndex)) {
                        String aLineAtIndex = aLines.get(aIndex);
                        if (aLineAtIndex != null && aLineAtIndex.length() > 0) {
                            aLinesToWriteMethods.add(aLineAtIndex);
                            aUsedIndicies.add(aIndex);
                        }
                    }
                    if (i >= aCount * 5) {
                        break;
                    }
                }

                // Map New Class Names
                for (int i = 0; aLinesToWriteClasses.size() < aCount; i++) {
                    Integer aIndex = Utils.randInt(0, aLines.size() - 1);
                    if (!aUsedIndicies.contains(aIndex)) {
                        String aLineAtIndex = aLines.get(aIndex);
                        if (aLineAtIndex != null && aLineAtIndex.length() > 0) {
                            aLinesToWriteClasses.add(aLineAtIndex);
                            aUsedIndicies.add(aIndex);
                        }
                    }
                    if (i >= aCount * 5) {
                        break;
                    }
                }
            }

            // Remove old generated Dicts
            if (Utils.doesFileExist(aMethodDict)) {
                aMethodDict.delete();
                Utils.log("Removed old Method-Dict");
            }
            if (Utils.doesFileExist(aClassDict)) {
                aClassDict.delete();
                Utils.log("Removed old Class-Dict");
            }

            // Create new empty dict files
            if (!Utils.doesFileExist(aMethodDict)) {
                Utils.createFile(aMethodDict);
            }
            if (!Utils.doesFileExist(aClassDict)) {
                Utils.createFile(aClassDict);
            }

            Utils.log("Writing new Dictionaries.");
            // Write
            Utils.appendListToFile(aMethodDict, aLinesToWriteMethods);
            Utils.appendListToFile(aClassDict, aLinesToWriteClasses);

            Utils.log("Finished all generation of new Dictionaries.");
        }
    }

    private static final class Utils {

        private static final Charset utf8 = StandardCharsets.UTF_8;

        private static final void log(String s) {
            System.out.println("[GTPP-Proguard] " + s);
        }

        public static int randInt(final int min, final int max) {
            return XSTR.XSTR_INSTANCE.nextInt((max - min) + 1) + min;
        }

        public static boolean doesFileExist(File f) {
            if (f != null && f.exists() && !f.isDirectory()) {
                return true;
            }
            return false;
        }

        public static File createFile(File aFile) {
            boolean blnCreated = false;
            log("Trying to use relative path " + aFile.getPath());
            try {
                // log("Trying to use path "+aFile.getCanonicalPath());
                // log("Trying to use absolute path "+aFile.getAbsolutePath());
                blnCreated = aFile.createNewFile();
            } catch (IOException ioe) {
                log("Error while creating a new empty file :" + ioe);
                return null;
            }
            return blnCreated ? aFile : null;
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
            } catch (IOException e) {}
            return false;
        }

        /**
         * Reads the contents of a file line by line to a List of Strings using the default encoding for the VM. The
         * file is always closed.
         *
         * @param file the file to read, must not be {@code null}
         * @return the list of Strings representing each line in the file, never {@code null}
         * @throws IOException in case of an I/O error
         * @since 1.3
         */
        public static List<String> readLines(File file) {
            try {
                return org.apache.commons.io.FileUtils.readLines(file, utf8);
            } catch (IOException e) {
                return new ArrayList<String>();
            }
        }
    }
}
