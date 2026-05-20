package gregtech.nei.dumper;

import java.io.PrintWriter;
import java.util.List;

final class DumperUtils {

    private DumperUtils() {}

    static String formatDouble(double v) {
        if (v == Math.floor(v) && !Double.isInfinite(v)) return (long) v + ".0";
        return String.valueOf(v);
    }

    static String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\")
            .replace("\"", "\\\"") + "\"";
    }

    static void printLines(PrintWriter w, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            w.print(lines.get(i));
            if (i < lines.size() - 1) w.print(",");
            w.println();
        }
    }
}
