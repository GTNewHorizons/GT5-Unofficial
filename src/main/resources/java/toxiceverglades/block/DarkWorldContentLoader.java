package toxiceverglades.block;

import static toxiceverglades.dimension.DimensionEverglades.blockFluidLakes;
import static toxiceverglades.dimension.DimensionEverglades.blockPortalFrame;
import static toxiceverglades.dimension.DimensionEverglades.blockSecondLayer;
import static toxiceverglades.dimension.DimensionEverglades.blockTopLayer;
import static toxiceverglades.dimension.DimensionEverglades.portalBlock;
import static toxiceverglades.dimension.DimensionEverglades.portalItem;

import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.base.BlockBaseFluid;
import gtPlusPlus.core.util.Utils;
import toxiceverglades.item.ItemBlockToxicEverglades;
import toxiceverglades.item.ItemEvergladesPortalTrigger;

public class DarkWorldContentLoader {

    // Static Vars
    public static BlockDarkWorldSludgeFluid SLUDGE;

    public static synchronized void run() {
        initMisc();
        initItems();
        initBlocks();
    }

    public static synchronized void initMisc() {
        // Fluids
        SLUDGE = (BlockDarkWorldSludgeFluid) new BlockDarkWorldSludgeFluid("sludge", Utils.rgbtoHexValue(30, 130, 30))
            .setDensity(1800)
            .setGaseous(false)
            .setLuminosity(2)
            .setViscosity(25000)
            .setTemperature(300);
        FluidRegistry.registerFluid(SLUDGE);
    }

    public static synchronized void initItems() {
        portalItem = (ItemEvergladesPortalTrigger) (new ItemEvergladesPortalTrigger()
            .setUnlocalizedName("everglades.trigger"));
        GameRegistry.registerItem(portalItem, "everglades.trigger");
    }

    public static synchronized void initBlocks() {
        // Create Block Instances
        blockFluidLakes = new BlockBaseFluid("Sludge", SLUDGE, BlockDarkWorldSludgeFluid.SLUDGE).setLightLevel(2f)
            .setLightOpacity(1)
            .setBlockName("fluidSludge");
        portalBlock = new BlockEvergladesPortal();
        blockTopLayer = new BlockDarkWorldGround();
        blockSecondLayer = new BlockDarkWorldPollutedDirt();
        blockPortalFrame = new BlockDarkWorldPortalFrame();

        // Registry
        GameRegistry.registerBlock(portalBlock, ItemBlockToxicEverglades.class, "dimensionDarkWorld_portal");
        GameRegistry.registerBlock(blockTopLayer, ItemBlockToxicEverglades.class, "blockDarkWorldGround");
        GameRegistry.registerBlock(blockSecondLayer, ItemBlockToxicEverglades.class, "blockDarkWorldGround2");
        GameRegistry.registerBlock(blockPortalFrame, ItemBlockToxicEverglades.class, "blockDarkWorldPortalFrame");

        // Make Flammable
        Blocks.fire.setFireInfo(blockTopLayer, 30, 20);
    }
}
