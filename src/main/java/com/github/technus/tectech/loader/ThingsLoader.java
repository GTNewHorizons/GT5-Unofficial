package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.dreamcraft.NoDreamCraftBlockLoader;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.block.ReactorSimBlock;
import com.github.technus.tectech.thing.casing.GT_Block_CasingsTT;
import com.github.technus.tectech.thing.casing.GT_Block_HintTT;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.item.*;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import openmodularturrets.blocks.turretbases.TurretBaseEM;
import openmodularturrets.blocks.turretheads.TurretHeadEM;

import static com.github.technus.tectech.TecTech.tectechTexturePage1;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {
    @Override
    public void run() {
        if(Textures.BlockIcons.casingTexturePages[tectechTexturePage1]==null) {
            Textures.BlockIcons.casingTexturePages[tectechTexturePage1] = new ITexture[128];
        }
        if(!Loader.isModLoaded("dreamcraft")){
            TecTech.Logger.info("Adding basic casings");
            new NoDreamCraftBlockLoader().run();
        }
        TecTech.Logger.info("Added texture page if was null");
        TT_Container_Casings.sBlockCasingsTT = new GT_Block_CasingsTT();
        TecTech.Logger.info("Elemental Casing registered");
        TT_Container_Casings.sHintCasingsTT = new GT_Block_HintTT();
        TecTech.Logger.info("Hint Blocks registered");

        QuantumGlassBlock.run();
        TecTech.Logger.info("Quantum Glass registered");

        QuantumStuffBlock.run();
        TecTech.Logger.info("Quantum Stuff registered");

        if(Loader.isModLoaded("openmodularturrets")) {
            TurretHeadEM.run();
            TecTech.Logger.info("TurretHeadEM registered");
            TurretBaseEM.run();
            TecTech.Logger.info("TurretBaseEM registered");
        }

        ReactorSimBlock.run();
        TecTech.Logger.info("Reactor Simulator registered");

        ConstructableTriggerItem.run();
        FrontRotationTriggerItem.run();
        ParametrizerMemoryCard.run();
        ElementalDefinitionScanStorage_EM.run();
        EuMeterGT.run();
        TecTech.Logger.info("Useful Items registered");

        ElementalDefinitionContainer_EM.run();
        DebugElementalInstanceContainer_EM.run();
        TecTech.Logger.info("Debug Items registered");
    }
}
