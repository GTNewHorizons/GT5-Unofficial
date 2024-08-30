package com.detrav.utils;

import java.util.HashMap;

import net.minecraft.block.Block;

import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.nuclear.FLUORIDES;

/**
 * Created by bartimaeusnek on 19.04.2018.
 */
public class GTppHelper {

    public static final HashMap<Short, Material> decodeoresGTpp = new HashMap<>();
    public static final HashMap<Material, Short> encodeoresGTpp = new HashMap<>();

    public static void generate_OreIDs() {
        short n = 0;
        for (; n < gtPlusPlus.core.material.ORES.class.getFields().length; ++n) {
            try {
                Short i = (short) (n + 1);
                Material m = ((Material) gtPlusPlus.core.material.ORES.class.getFields()[n]
                    .get(gtPlusPlus.core.material.ORES.class.getFields()[n]));
                decodeoresGTpp.put(i, m);
                encodeoresGTpp.put(m, i);
            } catch (Exception ignored) {}
        }
        // Manually add ores from other places than the ore class
        // Fluorite
        decodeoresGTpp.put((short) (++n + 1), FLUORIDES.FLUORITE);
        encodeoresGTpp.put(FLUORIDES.FLUORITE, (short) (n + 1));
        // Rare Earths
        decodeoresGTpp.put((short) (++n + 1), MISC_MATERIALS.RARE_EARTH_LOW);
        encodeoresGTpp.put(MISC_MATERIALS.RARE_EARTH_LOW, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), MISC_MATERIALS.RARE_EARTH_MID);
        encodeoresGTpp.put(MISC_MATERIALS.RARE_EARTH_MID, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), MISC_MATERIALS.RARE_EARTH_HIGH);
        encodeoresGTpp.put(MISC_MATERIALS.RARE_EARTH_HIGH, (short) (n + 1));
        // Koboldite
        decodeoresGTpp.put((short) (++n + 1), ALLOY.KOBOLDITE);
        encodeoresGTpp.put(ALLOY.KOBOLDITE, (short) (n + 1));
        // Runite
        decodeoresGTpp.put((short) (++n + 1), ELEMENT.STANDALONE.RUNITE);
        encodeoresGTpp.put(ELEMENT.STANDALONE.RUNITE, (short) (n + 1));
        // Ancient granite
        decodeoresGTpp.put((short) (++n + 1), ELEMENT.STANDALONE.GRANITE);
        encodeoresGTpp.put(ELEMENT.STANDALONE.GRANITE, (short) (n + 1));
    }

    public static boolean isGTppBlock(Block tBlock) {
        return tBlock instanceof BlockBaseOre;
    }

    public static short getGTppMeta(Block tBlock) {
        return (short) (GTppHelper.encodeoresGTpp.get(((BlockBaseOre) tBlock).getMaterialEx()) + 7000);
    }

    public static String getGTppVeinName(Block tBlock) {
        return tBlock.getLocalizedName();
    }

}
