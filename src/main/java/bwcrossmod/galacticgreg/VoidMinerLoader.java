package bwcrossmod.galacticgreg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.Werkstoff;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StoneType;
import gregtech.api.material.MU;
import gregtech.common.config.Gregtech;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.GTPPOreAdapter;
import gregtech.common.ores.OreInfo;
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
        OreInfo<com.ruling_0.materiallib.api.Material> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.isSmall = false;

        var weights = parseWeights(Gregtech.voidMiners.gregtechWeightsDD);

        // Materials.getAll() stays as the interim enumeration seam; the drop map and the GT ore adapter both key
        // on the MaterialLib material, so the descriptor and the entry added are ML-typed.
        for (Materials legacy : Materials.getAll()) {
            com.ruling_0.materiallib.api.Material mat = MU.material(legacy);
            if (mat == null) continue;

            info.material = mat;

            if (!GTOreAdapter.INSTANCE.supports(info)) continue;

            VoidMinerUtility.addMaterialToDimensionList(
                DimensionDef.DimNames.DEEPDARK,
                mat,
                weights.getFloat(legacy.getInternalName()));
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
        OreInfo<com.ruling_0.materiallib.api.Material> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.isSmall = false;

        var weights = parseWeights(Gregtech.voidMiners.gtppWeightsDD);

        for (com.ruling_0.materiallib.api.Material mat : MaterialLibAPI.getMaterials()) {
            info.material = mat;

            if (!GTPPOreAdapter.INSTANCE.supports(info)) continue;

            VoidMinerUtility.addMaterialToDimensionList(
                DimensionDef.DimNames.DEEPDARK,
                mat,
                weights.getFloat(MU.internalName(mat)));
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
