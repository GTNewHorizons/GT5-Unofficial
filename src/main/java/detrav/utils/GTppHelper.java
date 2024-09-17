package detrav.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

public class GTppHelper {

    private static boolean initialized;
    private static final HashMap<Short, Material> decodeoresGTpp = new HashMap<>();
    private static final HashMap<Material, Short> encodeoresGTpp = new HashMap<>();

    private static void generate_OreIDs() {
        short n = 0;
        final Field[] fields = MaterialsOres.class.getFields();
        for (; n < fields.length; ++n) {
            try {
                final Object o = fields[n].get(null);
                if (o instanceof Material m) {
                    Short i = (short) (n + 1);
                    decodeoresGTpp.put(i, m);
                    encodeoresGTpp.put(m, i);
                }
            } catch (Exception e) {
                GTMod.GT_FML_LOGGER
                    .error("Exception caught when trying to generate GT++ ore ids for detrav ore scanner", e);
            }
        }
        // Manually add ores from other places than the ore class
        // Fluorite
        decodeoresGTpp.put((short) (++n + 1), MaterialsFluorides.FLUORITE);
        encodeoresGTpp.put(MaterialsFluorides.FLUORITE, (short) (n + 1));
        // Rare Earths
        decodeoresGTpp.put((short) (++n + 1), MaterialMisc.RARE_EARTH_LOW);
        encodeoresGTpp.put(MaterialMisc.RARE_EARTH_LOW, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), MaterialMisc.RARE_EARTH_MID);
        encodeoresGTpp.put(MaterialMisc.RARE_EARTH_MID, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), MaterialMisc.RARE_EARTH_HIGH);
        encodeoresGTpp.put(MaterialMisc.RARE_EARTH_HIGH, (short) (n + 1));
        // Koboldite
        decodeoresGTpp.put((short) (++n + 1), MaterialsAlloy.KOBOLDITE);
        encodeoresGTpp.put(MaterialsAlloy.KOBOLDITE, (short) (n + 1));
        // Runite
        decodeoresGTpp.put((short) (++n + 1), MaterialsElements.STANDALONE.RUNITE);
        encodeoresGTpp.put(MaterialsElements.STANDALONE.RUNITE, (short) (n + 1));
        // Ancient granite
        decodeoresGTpp.put((short) (++n + 1), MaterialsElements.STANDALONE.GRANITE);
        encodeoresGTpp.put(MaterialsElements.STANDALONE.GRANITE, (short) (n + 1));
    }

    public static short getMetaFromBlock(Block block) {
        if (!initialized) {
            generate_OreIDs();
            initialized = true;
        }
        return (short) (GTppHelper.encodeoresGTpp.get(((BlockBaseOre) block).getMaterialEx()) + 7000);
    }

    public static Material getMatFromMeta(int meta) {
        if (!initialized) {
            generate_OreIDs();
            initialized = true;
        }
        return GTppHelper.decodeoresGTpp.get((short) (meta - 7000));
    }

    public static boolean isGTppBlock(Block block) {
        return block instanceof BlockBaseOre;
    }

    public static String getGTppVeinName(Block block) {
        return block.getLocalizedName();
    }

}
