package bwcrossmod.galacticgreg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bartworks.system.material.Werkstoff;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StoneType;
import gregtech.common.config.Gregtech;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.GTPPOreAdapter;
import gregtech.common.ores.OreInfo;
import gtPlusPlus.core.material.Material;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;

public class VoidMinerLoader {

    private static final Logger LOGGER = LogManager.getLogger(VoidMinerLoader.class);

    public static void init() {
        initGT();
        initBW();
        initGTPP();
    }

    private static void initGT() {
        OreInfo<Materials> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.isSmall = false;

        var weights = parseWeights(Gregtech.voidMiners.gregtechWeightsDD);

        for (Materials mat : Materials.getAll()) {
            info.material = mat;

            if (!GTOreAdapter.INSTANCE.supports(info)) continue;

            VoidMinerUtility.addMaterialToDimensionList(
                DimensionDef.DimNames.DEEPDARK,
                mat,
                weights.getFloat(mat.getInternalName()));
        }

        info.release();
    }

    private static void initBW() {
        OreInfo<Werkstoff> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.isSmall = false;

        var weights = parseWeights(Gregtech.voidMiners.bartworksWeightsDD);

        for (Werkstoff mat : Werkstoff.werkstoffHashSet) {
            info.material = mat;

            if (!BWOreAdapter.INSTANCE.supports(info)) continue;

            VoidMinerUtility.addMaterialToDimensionList(
                DimensionDef.DimNames.DEEPDARK,
                mat,
                weights.getFloat(mat.getInternalName()));
        }

        info.release();
    }

    private static void initGTPP() {
        OreInfo<Material> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.isSmall = false;

        var weights = parseWeights(Gregtech.voidMiners.gtppWeightsDD);

        for (Material mat : Material.mMaterialMap) {
            info.material = mat;

            if (!GTPPOreAdapter.INSTANCE.supports(info)) continue;

            VoidMinerUtility.addMaterialToDimensionList(
                DimensionDef.DimNames.DEEPDARK,
                mat,
                weights.getFloat(mat.getInternalName()));
        }

        info.release();
    }

    private static Object2FloatMap<String> parseWeights(String[] lines) {
        Object2FloatMap<String> out = new Object2FloatOpenHashMap<>();

        out.defaultReturnValue(1f);

        for (String line : lines) {
            String[] pieces = line.split(":");

            if (pieces.length != 2) {
                LOGGER.error("Malformed weight config: \"{}\"", line);
                continue;
            }

            float weight;
            try {
                weight = Float.parseFloat(pieces[1]);
            } catch (NumberFormatException e) {
                LOGGER.error("Unable to parse weight: \"{}\"", line);
                continue;
            }

            // We should maybe check this since we actually do have an ore named "InfinityCatalyst"...
            if (Float.isNaN(weight) || Float.isInfinite(weight)) {
                LOGGER.error("Invalid weight: \"{}\"", line);
                continue;
            }

            out.put(pieces[0].trim(), weight);
        }

        return out;
    }
}
