package toxiceverglades.dimension;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

import gtPlusPlus.core.config.Configuration;
import toxiceverglades.block.BlockEvergladesPortal;
import toxiceverglades.item.ItemEvergladesPortalTrigger;
import toxiceverglades.world.WorldProviderMod;

public class DimensionEverglades {

    public Object instance;
    public static int DIMID = Configuration.worldgen.EVERGLADES_ID;
    public static BlockEvergladesPortal portalBlock;
    public static ItemEvergladesPortalTrigger portalItem;
    public static Block blockTopLayer;
    public static Block blockSecondLayer;
    public static Block blockMainFiller = Blocks.stone;
    public static Block blockSecondaryFiller;
    public static Block blockFluidLakes;
    public static Block blockPortalFrame;

    public void load() {
        DimensionManager.registerProviderType(DIMID, WorldProviderMod.class, false);
        DimensionManager.registerDimension(DIMID, DIMID);
    }
}
