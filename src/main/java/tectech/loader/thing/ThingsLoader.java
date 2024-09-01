package tectech.loader.thing;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {

    @Override
    public void run() {

        GameRegistry.registerTileEntity(TileEyeOfHarmony.class, Reference.MODID + ":EyeOfHarmonyRenderBlock");
        GameRegistry.registerTileEntity(TileForgeOfGods.class, Reference.MODID + ":ForgeOfGodsRenderBlock");

        if (Textures.BlockIcons.casingTexturePages[TecTech.tectechTexturePage1] == null) {
            Textures.BlockIcons.casingTexturePages[TecTech.tectechTexturePage1] = new ITexture[128];
        }

        if (Textures.BlockIcons.casingTexturePages[7] == null) {
            Textures.BlockIcons.casingTexturePages[7] = new ITexture[128];
        }

        TecTech.LOGGER.info("Added texture page if was null");

        TT_Container_Casings.sBlockCasingsTT = new GT_Block_CasingsTT();
        TecTech.LOGGER.info("Elemental Casing registered");
        TT_Container_Casings.sBlockCasingsBA0 = new GT_Block_CasingsBA0();
        TecTech.LOGGER.info("Nikolai's Casing registered");

        TT_Container_Casings.SpacetimeCompressionFieldGenerators = new SpacetimeCompressionFieldCasing();
        TecTech.LOGGER.info("Spacetime Compression Field Casings registered.");

        TT_Container_Casings.TimeAccelerationFieldGenerator = new TimeAccelerationFieldCasing();
        TecTech.LOGGER.info("Time Acceleration Field Casings registered.");

        TT_Container_Casings.StabilisationFieldGenerators = new StabilisationFieldCasing();

        if (tectech.TecTech.configTecTech.ENABLE_GOD_FORGE) {
            TT_Container_Casings.GodforgeCasings = new GodforgeCasings();
            TecTech.LOGGER.info("Godforge blocks registered.");

            GodforgeGlassBlock.run();
            TecTech.LOGGER.info("Godforge Glass registered");
        }

        QuantumGlassBlock.run();
        TecTech.LOGGER.info("Quantum Glass registered");

        ReactorSimBlock.run();
        TecTech.LOGGER.info("Reactor Simulator registered");

        ParametrizerMemoryCard.run();
        EuMeterGT.run();
        TeslaStaff.run();
        TeslaCoilCover.run();
        TeslaCoilCapacitor.run();
        EnderFluidLinkCover.run();
        PowerPassUpgradeCover.run();
        TecTech.LOGGER.info("Useful Items registered");

        TeslaCoilComponent.run();
        AstralArrayFabricator.run();
        TecTech.LOGGER.info("Crafting Components registered");

        TecTech.LOGGER.info("Debug Items registered");
    }
}
