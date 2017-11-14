package gtPlusPlus.core.util;

import java.io.*;
import java.util.Date;

public class LoggingUtils {

	public static void profileLog(final Object o) {
		try {
			String content;
			final File file = new File("GregtechTimingsTC.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
				final FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				final BufferedWriter bw = new BufferedWriter(fw);
				bw.write("============================================================");
				bw.write(System.lineSeparator());
				bw.close();
			}
			if (o instanceof String) {
				content = (String) o;
			} else {
				content = o.toString();
			}
			final FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			final BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.write(System.lineSeparator());
			bw.close();
			System.out.println("Data Logged.");

		} catch (final IOException e) {
			System.out.println("Data logging failed.");
		}
	}

	public static boolean logCurrentSystemTime(final String message) {
		final Date date = new Date(System.currentTimeMillis());
		try {
			profileLog(message + " | " + date.toString());
			return true;
		} catch (final Throwable r) {
			return false;
		}

	}

}
