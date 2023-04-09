package com.github.technus.tectech.loader.thing;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.tectechTexturePage1;
import static gregtech.api.enums.Mods.OpenModularTurrets;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretbases.TurretBaseEM;
import com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads.TurretHeadEM;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.ReactorSimBlock;
import com.github.technus.tectech.thing.block.TileEyeOfHarmony;
import com.github.technus.tectech.thing.casing.*;
import com.github.technus.tectech.thing.item.*;

import cpw.mods.fml.common.Loader;
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

        QuantumGlassBlock.run();
        TecTech.LOGGER.info("Quantum Glass registered");

        if (OpenModularTurrets.isModLoaded()) {
            TurretHeadEM.run();
            TecTech.LOGGER.info("TurretHeadEM registered");
            TurretBaseEM.run();
            TecTech.LOGGER.info("TurretBaseEM registered");
        }

        ReactorSimBlock.run();
        TecTech.LOGGER.info("Reactor Simulator registered");

        ParametrizerMemoryCard.run();
        ElementalDefinitionScanStorage_EM.run();
        EuMeterGT.run();
        TeslaStaff.run();
        TeslaCoilCover.run();
        TeslaCoilCapacitor.run();
        EnderFluidLinkCover.run();
        PowerPassUpgradeCover.run();
        TecTech.LOGGER.info("Useful Items registered");

        TeslaCoilComponent.run();
        TecTech.LOGGER.info("Crafting Components registered");

        ElementalDefinitionContainer_EM.run();
        DebugElementalInstanceContainer_EM.run();
        TecTech.LOGGER.info("Debug Items registered");
    }
}
