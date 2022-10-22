package gregtech.crossmod;

import com.pam.harvestcraft.BlockPamFruitingLog;
import com.pam.harvestcraft.BlockRegistry;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class Harvestcraft {
    public static void init() {
        if (!Loader.isModLoaded("harvestcraft")) return;

        ((BlockPamFruitingLog) BlockRegistry.pamPaperbark).setDropItem(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1));
    }
}
