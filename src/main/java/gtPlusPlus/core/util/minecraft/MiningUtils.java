package gtPlusPlus.core.util.minecraft;

import java.util.HashMap;

import gregtech.common.WorldgenGTOreLayer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class MiningUtils {

    public static int mMoonID = -99;
    public static int mMarsID = -99;
    public static int mCometsID = -99;

    public static void iterateAllOreTypes() {
        HashMap<String, Integer> M = new HashMap<>();
        String aTextWorldGen;
        if (MiningUtils.findAndMapOreTypesFromGT()) {
            int mapKey = 0;
            for (AutoMap<WorldgenGTOreLayer> g : MiningUtils.mOreMaps) {
                for (WorldgenGTOreLayer h : g) {

                    try {
                        aTextWorldGen = (String) ReflectionUtils.getField(WorldgenGTOreLayer.class, "aTextWorldgen")
                            .get(h);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        aTextWorldGen = h.mWorldGenName;
                    }

                    M.put(aTextWorldGen + h.mWorldGenName, mapKey);
                    Logger.INFO("Found Vein type: " + aTextWorldGen + h.mWorldGenName + " in map with key: " + mapKey);
                }
                mapKey++;
            }
        }
    }

    public static AutoMap<WorldgenGTOreLayer>[] mOreMaps = new AutoMap[7];
    private static final AutoMap<WorldgenGTOreLayer> Ores_Overworld = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_Nether = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_End = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_Moon = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_Mars = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_Comets = new AutoMap<>();
    private static final AutoMap<WorldgenGTOreLayer> Ores_Misc = new AutoMap<>();

    public static boolean findAndMapOreTypesFromGT() {
        // Gets Moon ID

        boolean aEndAsteroids;
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore") != null
                && mMoonID == -99) {
                mMoonID = ReflectionUtils
                    .getField(
                        ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore"),
                        "idDimensionMoon")
                    .getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Gets Mars ID
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars") != null
                && mMarsID == -99) {
                mMarsID = ReflectionUtils
                    .getField(
                        ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars"),
                        "dimensionIDMars")
                    .getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Get Comets ID
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids")
                != null && mCometsID == -99) {
                mCometsID = ReflectionUtils
                    .getField(
                        ReflectionUtils
                            .getClass("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids"),
                        "dimensionIDAsteroids")
                    .getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Clear Cache
        Ores_Overworld.clear();
        Ores_Nether.clear();
        Ores_End.clear();
        Ores_Misc.clear();

        for (WorldgenGTOreLayer x : WorldgenGTOreLayer.sList) {
            if (x.mEnabled) {

                try {
                    aEndAsteroids = ReflectionUtils.getField(WorldgenGTOreLayer.class, "mEndAsteroid")
                        .getBoolean(x);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    aEndAsteroids = false;
                }

                if (x.mOverworld) {
                    Ores_Overworld.put(x);
                }
                if (x.mNether) {
                    Ores_Nether.put(x);
                }
                if (x.mEnd || aEndAsteroids) {
                    Ores_End.put(x);
                }
                if (x.mOverworld || x.mNether || (x.mEnd || aEndAsteroids)) {
                    continue;
                }

                Ores_Misc.put(x);
            } else {
                Ores_Comets.put(x);
            }
        }

        mOreMaps[0] = Ores_Overworld;
        mOreMaps[1] = Ores_Nether;
        mOreMaps[2] = Ores_End;
        mOreMaps[3] = Ores_Moon;
        mOreMaps[4] = Ores_Mars;
        mOreMaps[5] = Ores_Comets;
        mOreMaps[6] = Ores_Misc;
        return true;
    }
}
