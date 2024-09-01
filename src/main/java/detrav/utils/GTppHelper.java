package detrav.utils;

import java.util.HashMap;

import net.minecraft.block.Block;

import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

/**
 * Created by bartimaeusnek on 19.04.2018.
 */
public class GTppHelper {

    public static final HashMap<Short, Material> decodeoresGTpp = new HashMap<>();
    public static final HashMap<Material, Short> encodeoresGTpp = new HashMap<>();

    public static void generate_OreIDs() {
        short n = 0;
        for (; n < MaterialsOres.class.getFields().length; ++n) {
            try {
                Short i = (short) (n + 1);
                Material m = ((Material) MaterialsOres.class.getFields()[n].get(MaterialsOres.class.getFields()[n]));
                decodeoresGTpp.put(i, m);
                encodeoresGTpp.put(m, i);
            } catch (Exception ignored) {}
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
