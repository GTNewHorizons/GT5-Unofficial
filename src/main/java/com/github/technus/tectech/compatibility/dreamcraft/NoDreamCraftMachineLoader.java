package com.github.technus.tectech.compatibility.dreamcraft;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_WetTransformer;
import cpw.mods.fml.common.Loader;
import gregtech.api.metatileentity.MetaTileEntity;

import java.lang.reflect.Constructor;

public class NoDreamCraftMachineLoader implements Runnable{

    @Override
    public void run() {
        CustomItemList.WetTransformer_LV_ULV.set(new GT_MetaTileEntity_WetTransformer(
                12000, "wettransformer.tier.00", "Ultra Low Voltage Power Transformer", 0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_MV_LV.set(new GT_MetaTileEntity_WetTransformer(
                12001, "wetransformer.tier.01", "Low Voltage Power Transformer", 1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_HV_MV.set(new GT_MetaTileEntity_WetTransformer(
                12002, "wettransformer.tier.02", "Medium Voltage Power Transformer", 2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_EV_HV.set(new GT_MetaTileEntity_WetTransformer(
                12003, "wettransformer.tier.03", "High Voltage Power Transformer", 3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_IV_EV.set(new GT_MetaTileEntity_WetTransformer(
                12004, "wettransformer.tier.04", "Extreme Power Transformer", 4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_LuV_IV.set(new GT_MetaTileEntity_WetTransformer(
                12005, "wettransformer.tier.05", "Insane Power Transformer", 5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_ZPM_LuV.set(new GT_MetaTileEntity_WetTransformer(
                12006, "wettransformer.tier.06", "Ludicrous Power Transformer", 6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UV_ZPM.set(new GT_MetaTileEntity_WetTransformer(
                12007, "wettransformer.tier.07", "ZPM Voltage Power Transformer", 7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UHV_UV.set(new GT_MetaTileEntity_WetTransformer(
                12008, "wettransformer.tier.08", "Ultimate Power Transformer", 8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UEV_UHV.set(new GT_MetaTileEntity_WetTransformer(
                12009, "wettransformer.tier.09", "Highly Ultimate Power Transformer", 9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UIV_UEV.set(new GT_MetaTileEntity_WetTransformer(
                12010, "wettransformer.tier.10", "Extremely Ultimate Power Transformer",10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UMV_UIV.set(new GT_MetaTileEntity_WetTransformer(
                12011, "wettransformer.tier.11", "Insanely Ultimate Power Transformer",11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_UXV_UMV.set(new GT_MetaTileEntity_WetTransformer(
                12012, "wettransformer.tier.12", "Mega Ultimate Power Transformer",12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_OPV_UXV.set(new GT_MetaTileEntity_WetTransformer(
                12013, "wettransformer.tier.13", "Extended Mega Ultimate Power Transformer",13,
                "OPV -> UXV (Use Soft Mallet to invert)").getStackForm(1L));

        CustomItemList.WetTransformer_MAXV_OPV.set(new GT_MetaTileEntity_WetTransformer(
                12014, "wettransformer.tier.14", "Overpowered Power Transformer",14,
                "MAX -> OPV (Use Soft Mallet to invert)").getStackForm(1L));

        if (Loader.isModLoaded("miscutils")) {
            try {
                Class clazz = Class.forName("gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp");
                Constructor<MetaTileEntity> constructor=clazz.getConstructor(int.class,String.class,String.class,int.class,String.class);
                CustomItemList.Transformer_HA_UEV_UHV.set(
                        (constructor.newInstance(
                                11989, "transformer.ha.tier.09", "Highly Ultimate Hi-Amp Transformer", 9,
                                "UEV -> UHV (Use Soft Mallet to invert)")).getStackForm(1));
                CustomItemList.Transformer_HA_UIV_UEV.set(
                        (constructor.newInstance(
                                11910, "transformer.ha.tier.10", "Extremely Ultimate Hi-Amp Transformer", 10,
                                "UIV -> UEV (Use Soft Mallet to invert)")).getStackForm(1));
                CustomItemList.Transformer_HA_UMV_UIV.set(
                        (constructor.newInstance(
                                11911, "transformer.ha.tier.11", "Insanely Ultimate Hi-Amp Transformer", 11,
                                "UMV -> UIV (Use Soft Mallet to invert)")).getStackForm(1));
                CustomItemList.Transformer_HA_UXV_UMV.set(
                        (constructor.newInstance(
                                11912, "transformer.ha.tier.12", "Mega Ultimate Hi-Amp Transformer", 12,
                                "UXV -> UMV (Use Soft Mallet to invert)")).getStackForm(1));
                CustomItemList.Transformer_HA_OPV_UXV.set(
                        (constructor.newInstance(
                                11913, "transformer.ha.tier.13", "Extended Mega Ultimate Hi-Amp Transformer", 13,
                                "OPV -> UXV (Use Soft Mallet to invert)")).getStackForm(1));
                CustomItemList.Transformer_HA_MAXV_OPV.set(
                        (constructor.newInstance(
                                11914, "transformer.ha.tier.14", "Overpowered Hi-Amp Transformer", 14,
                                "MAX -> OPV (Use Soft Mallet to invert)")).getStackForm(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
