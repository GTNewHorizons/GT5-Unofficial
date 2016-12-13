package com.detrav;

import com.detrav.enums.DetravItemList;
import com.detrav.items.DetravMetaGeneratedItem01;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.items.processing.*;
import com.detrav.tileentities.Detrav_MetaTileEntity_AdvMiner2;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_High;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_Low;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_Medium;
import com.detrav.utils.DetravRepairRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class Detrav_AfterGTPreload_Loader implements Runnable {
    @Override
    public void run() {

        //items
        new DetravMetaGeneratedItem01();
        new DetravMetaGeneratedTool01();

        //recipes and etc
        new ProcessingDetravToolProPick();
        new ProcessingDetravShaping();
        new ProcessingDetravPortableCharger();
        new ProcessingDetravSmartPlunger();
        //new Detrav_MetaGenerated_Tool_01();
        //new ProcessingDetravToolProPick();


        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(2, 1, Materials.Iron, Materials._NULL,null)
                , GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IBI", " I ", "III", Character.valueOf('I'), OrePrefixes.ingot.get(Materials.Iron), Character.valueOf('B'), OrePrefixes.block.get(Materials.Iron)});

        //ItemList.Machine_Bronze_Boiler_Solar.set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_Low.set(new Detrav_MetaTileEntity_Boiler_Solar_Low(2051,"boiler.bronze.solar", "Bronze Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_Medium.set(new Detrav_MetaTileEntity_Boiler_Solar_Medium(2052,"boiler.steel.solar", "Steel Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_High.set(new Detrav_MetaTileEntity_Boiler_Solar_High(2053,"boiler.stainless.steel.solar", "Stainless Steel Solar Boiler").getStackForm(1L));
        DetravItemList.DetravAdvancedMiner2.set(new Detrav_MetaTileEntity_AdvMiner2(2054, "multimachine.advminer2.detrav", "Detrav Advanced Miner II").getStackForm(1));
        //DetravItemList.Anvil.set()

       // GameRegistry.addRecipe(new DetravRepairRecipe());

    }
}
