package com.detrav;

import com.detrav.enums.DetravItemList;
import com.detrav.items.DetravMetaGeneratedItem01;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.items.processing.ProcessingDetravPortableCharger;
import com.detrav.items.processing.ProcessingDetravShaping;
import com.detrav.items.processing.ProcessingDetravToolProPick;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_High;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_Low;
import com.detrav.tileentities.Detrav_MetaTileEntity_Boiler_Solar_Medium;

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
        //new Detrav_MetaGenerated_Tool_01();
        //new ProcessingDetravToolProPick();

        //ItemList.Machine_Bronze_Boiler_Solar.set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_Low.set(new Detrav_MetaTileEntity_Boiler_Solar_Low(2051,"boiler.bronze.solar", "Bronze Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_Medium.set(new Detrav_MetaTileEntity_Boiler_Solar_Medium(2052,"boiler.steel.solar", "Steel Solar Boiler").getStackForm(1L));
        DetravItemList.Solar_Boiler_High.set(new Detrav_MetaTileEntity_Boiler_Solar_High(2053,"boiler.stainless.steel.solar", "Stainless Steel Solar Boiler").getStackForm(1L));
    }
}
