package gtPlusPlus.core.util.minecraft;

import java.util.HashMap;

import gregtech.api.enums.Mods;
import gregtech.common.WorldgenGTOreLayer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;

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

        if (Mods.GalacticraftCore.isModLoaded()) {
            if (mMoonID == -99) {
                mMoonID = ConfigManagerCore.idDimensionMoon;
            }
            if (mMarsID == -99) {
                mMarsID = ConfigManagerMars.dimensionIDMars;
            }
            if (mCometsID == -99) {
                mCometsID = ConfigManagerAsteroids.dimensionIDAsteroids;
            }
        }

        // Clear Cache
        Ores_Overworld.clear();
        Ores_Nether.clear();
        Ores_End.clear();
        Ores_Misc.clear();

        for (WorldgenGTOreLayer gtOreLayer : WorldgenGTOreLayer.sList) {
            if (gtOreLayer.mEnabled) {
                if (gtOreLayer.mOverworld) {
                    Ores_Overworld.put(gtOreLayer);
                }
                if (gtOreLayer.mNether) {
                    Ores_Nether.put(gtOreLayer);
                }
                if (gtOreLayer.mEnd || gtOreLayer.mEndAsteroid) {
                    Ores_End.put(gtOreLayer);
                }
                if (gtOreLayer.mOverworld || gtOreLayer.mNether || (gtOreLayer.mEnd || gtOreLayer.mEndAsteroid)) {
                    continue;
                }
                Ores_Misc.put(gtOreLayer);
            } else {
                Ores_Comets.put(gtOreLayer);
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
