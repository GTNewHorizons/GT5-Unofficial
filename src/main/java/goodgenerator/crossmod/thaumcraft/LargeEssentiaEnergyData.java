package goodgenerator.crossmod.thaumcraft;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import thaumcraft.api.aspects.Aspect;

public class LargeEssentiaEnergyData {

    public static final HashMap<Aspect, FuelData> ASPECT_FUEL_DATA = new HashMap<>();

    public static String readJsonFile() {
        try {
            URL url = Thread.currentThread().getContextClassLoader()
                    .getResource("assets/goodgenerator/data/essentia.json");
            assert url != null;
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void processEssentiaData() {
        String data = readJsonFile();
        if (data == null) {
            return;
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(data);
        JsonArray jsonArray = jsonObject.get("Essentia").getAsJsonArray();
        for (JsonElement elm : jsonArray) {
            JsonObject essData = elm.getAsJsonObject();
            String aspectName = essData.get("name").getAsString();
            Aspect aspect = Aspect.getAspect(aspectName.toLowerCase());
            if (aspect != null) {
                int fuel = essData.get("fuelValue").getAsInt();
                String cate = essData.get("category").getAsString();
                float ceo = essData.get("consumeCeo").getAsFloat();
                ASPECT_FUEL_DATA.put(aspect, new FuelData(fuel, cate, ceo));
            }
        }
    }

    public static int getAspectTypeIndex(Aspect aspect) {
        if (ASPECT_FUEL_DATA.containsKey(aspect)) {
            return ASPECT_FUEL_DATA.get(aspect).getCategoryIndex();
        } else return -1;
    }

    public static String getAspectType(Aspect aspect) {
        if (ASPECT_FUEL_DATA.containsKey(aspect)) {
            return ASPECT_FUEL_DATA.get(aspect).getCategory();
        } else return null;
    }

    public static int getAspectFuelValue(Aspect aspect) {
        if (ASPECT_FUEL_DATA.containsKey(aspect)) {
            return ASPECT_FUEL_DATA.get(aspect).getFuelValue();
        } else return 0;
    }

    public static float getAspectCeo(Aspect aspect) {
        if (ASPECT_FUEL_DATA.containsKey(aspect)) {
            return ASPECT_FUEL_DATA.get(aspect).getConsumeSpeed();
        } else return 0;
    }
}

class FuelData {

    private final int fuelValue;
    private final String category;
    private final float consumeSpeed;

    FuelData(int basicValue, String cate, float ceo) {
        fuelValue = basicValue;
        category = cate;
        consumeSpeed = ceo;
    }

    public int getFuelValue() {
        return fuelValue;
    }

    public float getConsumeSpeed() {
        return consumeSpeed;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryIndex() {
        switch (category) {
            case "NORMAL":
                return 0;
            case "AIR":
                return 1;
            case "THERMAL":
                return 2;
            case "UNSTABLE":
                return 3;
            case "VICTUS":
                return 4;
            case "TAINTED":
                return 5;
            case "MECHANICS":
                return 6;
            case "SPRITE":
                return 7;
            case "RADIATION":
                return 8;
            case "ELECTRIC":
                return 9;
            default:
                return -1;
        }
    }
}
