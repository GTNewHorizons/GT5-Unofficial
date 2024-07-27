package com.github.technus.tectech.loader.thing;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.tectechTexturePage1;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.GodforgeGlassBlock;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.ReactorSimBlock;
import com.github.technus.tectech.thing.block.TileEyeOfHarmony;
import com.github.technus.tectech.thing.block.TileForgeOfGods;
import com.github.technus.tectech.thing.casing.GT_Block_CasingsBA0;
import com.github.technus.tectech.thing.casing.GT_Block_CasingsTT;
import com.github.technus.tectech.thing.casing.GodforgeCasings;
import com.github.technus.tectech.thing.casing.SpacetimeCompressionFieldCasing;
import com.github.technus.tectech.thing.casing.StabilisationFieldCasing;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.casing.TimeAccelerationFieldCasing;
import com.github.technus.tectech.thing.item.AstralArrayFabricator;
import com.github.technus.tectech.thing.item.EnderFluidLinkCover;
import com.github.technus.tectech.thing.item.EuMeterGT;
import com.github.technus.tectech.thing.item.ParametrizerMemoryCard;
import com.github.technus.tectech.thing.item.PowerPassUpgradeCover;
import com.github.technus.tectech.thing.item.TeslaCoilCapacitor;
import com.github.technus.tectech.thing.item.TeslaCoilComponent;
import com.github.technus.tectech.thing.item.TeslaCoilCover;
import com.github.technus.tectech.thing.item.TeslaStaff;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {

    @Override
    public void run() {

        GameRegistry.registerTileEntity(TileEyeOfHarmony.class, MODID + ":EyeOfHarmonyRenderBlock");
        GameRegistry.registerTileEntity(TileForgeOfGods.class, MODID + ":ForgeOfGodsRenderBlock");

        if (Textures.BlockIcons.casingTexturePages[tectechTexturePage1] == null) {
            Textures.BlockIcons.casingTexturePages[tectechTexturePage1] = new ITexture[128];
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

        if (!NewHorizonsCoreMod.isModLoaded()) {

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
