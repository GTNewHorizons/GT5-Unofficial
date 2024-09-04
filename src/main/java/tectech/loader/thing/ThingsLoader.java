package tectech.loader.thing;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.block.BlockReactorSim;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.thing.block.TileEntityForgeOfGods;
import tectech.thing.casing.BlockGTCasingsBA0;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.BlockGodforgeCasings;
import tectech.thing.casing.SpacetimeCompressionFieldCasing;
import tectech.thing.casing.StabilisationFieldCasing;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.casing.TimeAccelerationFieldCasing;
import tectech.thing.item.ItemAstralArrayFabricator;
import tectech.thing.item.ItemEnderFluidLinkCover;
import tectech.thing.item.ItemEuMeterGT;
import tectech.thing.item.ItemParametrizerMemoryCard;
import tectech.thing.item.ItemPowerPassUpgradeCover;
import tectech.thing.item.ItemTeslaCoilCapacitor;
import tectech.thing.item.ItemTeslaCoilComponent;
import tectech.thing.item.ItemTeslaCoilCover;
import tectech.thing.item.ItemTeslaStaff;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {

    @Override
    public void run() {

        GameRegistry.registerTileEntity(TileEntityEyeOfHarmony.class, Reference.MODID + ":EyeOfHarmonyRenderBlock");
        GameRegistry.registerTileEntity(TileEntityForgeOfGods.class, Reference.MODID + ":ForgeOfGodsRenderBlock");

        if (Textures.BlockIcons.casingTexturePages[TecTech.tectechTexturePage1] == null) {
            Textures.BlockIcons.casingTexturePages[TecTech.tectechTexturePage1] = new ITexture[128];
        }

        if (Textures.BlockIcons.casingTexturePages[7] == null) {
            Textures.BlockIcons.casingTexturePages[7] = new ITexture[128];
        }

        TecTech.LOGGER.info("Added texture page if was null");

        TTCasingsContainer.sBlockCasingsTT = new BlockGTCasingsTT();
        TecTech.LOGGER.info("Elemental Casing registered");
        TTCasingsContainer.sBlockCasingsBA0 = new BlockGTCasingsBA0();
        TecTech.LOGGER.info("Nikolai's Casing registered");

        TTCasingsContainer.SpacetimeCompressionFieldGenerators = new SpacetimeCompressionFieldCasing();
        TecTech.LOGGER.info("Spacetime Compression Field Casings registered.");

        TTCasingsContainer.TimeAccelerationFieldGenerator = new TimeAccelerationFieldCasing();
        TecTech.LOGGER.info("Time Acceleration Field Casings registered.");

        TTCasingsContainer.StabilisationFieldGenerators = new StabilisationFieldCasing();

        if (tectech.TecTech.configTecTech.ENABLE_GOD_FORGE) {
            TTCasingsContainer.GodforgeCasings = new BlockGodforgeCasings();
            TecTech.LOGGER.info("Godforge blocks registered.");

            BlockGodforgeGlass.run();
            TecTech.LOGGER.info("Godforge Glass registered");
        }

        BlockQuantumGlass.run();
        TecTech.LOGGER.info("Quantum Glass registered");

        BlockReactorSim.run();
        TecTech.LOGGER.info("Reactor Simulator registered");

        ItemParametrizerMemoryCard.run();
        ItemEuMeterGT.run();
        ItemTeslaStaff.run();
        ItemTeslaCoilCover.run();
        ItemTeslaCoilCapacitor.run();
        ItemEnderFluidLinkCover.run();
        ItemPowerPassUpgradeCover.run();
        TecTech.LOGGER.info("Useful Items registered");

        ItemTeslaCoilComponent.run();
        ItemAstralArrayFabricator.run();
        TecTech.LOGGER.info("Crafting Components registered");

        TecTech.LOGGER.info("Debug Items registered");
    }
}
