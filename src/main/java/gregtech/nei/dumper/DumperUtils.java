package gregtech.nei.dumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

final class DumperUtils {

    static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .create();

    private DumperUtils() {}

    static String formatDouble(double v) {
        if (v == Math.floor(v) && !Double.isInfinite(v)) return (long) v + ".0";
        return String.valueOf(v);
    }
}
