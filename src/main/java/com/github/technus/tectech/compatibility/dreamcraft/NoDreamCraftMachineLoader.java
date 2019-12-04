package com.github.technus.tectech.compatibility.dreamcraft;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_TT_Transformer;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_WetTransformer;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static gregtech.api.GregTech_API.METATILEENTITIES;
import static net.minecraft.util.StatCollector.translateToLocal;

public class NoDreamCraftMachineLoader implements Runnable {
    public final static String imagination = EnumChatFormatting.RESET +
            "You just need " + EnumChatFormatting.DARK_PURPLE +
            "I" + EnumChatFormatting.LIGHT_PURPLE +
            "m" + EnumChatFormatting.DARK_RED +
            "a" + EnumChatFormatting.RED +
            "g" + EnumChatFormatting.YELLOW +
            "i" + EnumChatFormatting.GREEN +
            "n" + EnumChatFormatting.AQUA +
            "a" + EnumChatFormatting.DARK_AQUA +
            "t" + EnumChatFormatting.BLUE +
            "i" + EnumChatFormatting.DARK_BLUE +
            "o" + EnumChatFormatting.DARK_PURPLE +
            "n" + EnumChatFormatting.RESET +
            " to use this.";

    @Override
    public void run() {
        try {
            CustomItemList.WetTransformer_LV_ULV.set(new GT_MetaTileEntity_WetTransformer(
                    12000, "wettransformer.tier.00", "Ultra Low Voltage Power Transformer", 0).getStackForm(1L));//LV -> ULV (Use Soft Mallet to invert)
        } catch (IllegalArgumentException e) {
            System.out.println(METATILEENTITIES[12000].getClass().getCanonicalName());
            TecTech.LOGGER.error(e);
            e.printStackTrace();
            throw new Error(METATILEENTITIES[12000].getClass().getCanonicalName(), e);
        }

        CustomItemList.WetTransformer_MV_LV.set(new GT_MetaTileEntity_WetTransformer(
                12001, "wetransformer.tier.01", "Low Voltage Power Transformer", 1).getStackForm(1L));//MV -> LV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_HV_MV.set(new GT_MetaTileEntity_WetTransformer(
                12002, "wettransformer.tier.02", "Medium Voltage Power Transformer", 2).getStackForm(1L));//HV -> MV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_EV_HV.set(new GT_MetaTileEntity_WetTransformer(
                12003, "wettransformer.tier.03", "High Voltage Power Transformer", 3).getStackForm(1L));//EV -> HV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_IV_EV.set(new GT_MetaTileEntity_WetTransformer(
                12004, "wettransformer.tier.04", "Extreme Power Transformer", 4).getStackForm(1L));//IV -> EV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_LuV_IV.set(new GT_MetaTileEntity_WetTransformer(
                12005, "wettransformer.tier.05", "Insane Power Transformer", 5).getStackForm(1L));//LuV -> IV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_ZPM_LuV.set(new GT_MetaTileEntity_WetTransformer(
                12006, "wettransformer.tier.06", "Ludicrous Power Transformer", 6).getStackForm(1L));//ZPM -> LuV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UV_ZPM.set(new GT_MetaTileEntity_WetTransformer(
                12007, "wettransformer.tier.07", "ZPM Voltage Power Transformer", 7).getStackForm(1L));//UV -> ZPM (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UHV_UV.set(new GT_MetaTileEntity_WetTransformer(
                12008, "wettransformer.tier.08", "Ultimate Power Transformer", 8).getStackForm(1L));//UHV -> UV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UEV_UHV.set(new GT_MetaTileEntity_WetTransformer(
                12009, "wettransformer.tier.09", "Highly Ultimate Power Transformer", 9).getStackForm(1L));//UEV -> UHV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UIV_UEV.set(new GT_MetaTileEntity_WetTransformer(
                12010, "wettransformer.tier.10", "Extremely Ultimate Power Transformer", 10).getStackForm(1L));//UIV -> UEV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UMV_UIV.set(new GT_MetaTileEntity_WetTransformer(
                12011, "wettransformer.tier.11", "Insanely Ultimate Power Transformer", 11).getStackForm(1L));//UMV -> UIV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_UXV_UMV.set(new GT_MetaTileEntity_WetTransformer(
                12012, "wettransformer.tier.12", "Mega Ultimate Power Transformer", 12).getStackForm(1L));//UXV -> UMV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_OPV_UXV.set(new GT_MetaTileEntity_WetTransformer(
                12013, "wettransformer.tier.13", "Extended Mega Ultimate Power Transformer", 13).getStackForm(1L));//OPV -> UXV (Use Soft Mallet to invert)

        CustomItemList.WetTransformer_MAXV_OPV.set(new GT_MetaTileEntity_WetTransformer(
                12014, "wettransformer.tier.14", "Overpowered Power Transformer", 14).getStackForm(1L));//MAX -> OPV (Use Soft Mallet to invert)

        try {
            MetaTileEntity temp;
            Method method = null;
            Field field = null;
            Object iTexture = new ITexture[0];
            if (GT_Values.GT.isClientSide()) {
                method = GT_MetaTileEntity_TieredMachineBlock.class.getMethod("getTextureSet", ITexture[].class);
                field = GT_MetaTileEntity_TieredMachineBlock.class.getField("mTextures");
                field.setAccessible(true);
            }

            temp = new GT_MetaTileEntity_BasicHull(
                    11230, "hull.tier.10", "UEV Machine Hull", 10,
                    imagination);
            Util.setTier(10, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_UEV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_BasicHull(
                    11231, "hull.tier.11", "UIV Machine Hull", 11,
                    imagination);
            Util.setTier(11, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_UIV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_BasicHull(
                    11232, "hull.tier.12", "UMV Machine Hull", 12,
                    imagination);
            Util.setTier(12, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_UMV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_BasicHull(
                    11233, "hull.tier.13", "UXV Machine Hull", 13,
                    imagination);
            Util.setTier(13, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_UXV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_BasicHull(
                    11234, "hull.tier.14", "OPV Machine Hull", 14,
                    imagination);
            Util.setTier(14, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_OPV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_BasicHull(
                    11235, "hull.tier.15", "MAX Machine Hull", 15,
                    imagination);
            Util.setTier(15, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Hull_MAXV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11220, "tt.transformer.tier.09", "Highly Ultimate Transformer", 9);//UEV -> UHV (Use Soft Mallet to invert)
            CustomItemList.Transformer_UEV_UHV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11221, "tt.transformer.tier.10", "Extremely Ultimate Transformer", 10);//UIV -> UEV (Use Soft Mallet to invert)
            Util.setTier(10, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Transformer_UIV_UEV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11222, "tt.transformer.tier.11", "Insanely Ultimate Transformer", 11);//UMV -> UIV (Use Soft Mallet to invert)
            Util.setTier(11, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Transformer_UMV_UIV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11223, "tt.transformer.tier.12", "Mega Ultimate Transformer", 12);//UXV -> UMV (Use Soft Mallet to invert)
            Util.setTier(12, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Transformer_UXV_UMV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11224, "tt.transformer.tier.13", "Extended Mega Ultimate Transformer", 13);//OPV -> UXV (Use Soft Mallet to invert)
            Util.setTier(13, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Transformer_OPV_UXV.set(temp.getStackForm(1L));

            temp = new GT_MetaTileEntity_TT_Transformer(
                    11225, "tt.transformer.tier.14", "Overpowered Transformer", 14);//MAX -> OPV (Use Soft Mallet to invert)
            Util.setTier(14, temp);
            if (GT_Values.GT.isClientSide()) {
                field.set(temp, method.invoke(temp, iTexture));
            }
            CustomItemList.Transformer_MAXV_OPV.set(temp.getStackForm(1L));


            if (Loader.isModLoaded(Reference.GTPLUSPLUS)) {
                Class clazz = Class.forName("gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp");
                Constructor<MetaTileEntity> constructor = clazz.getConstructor(int.class, String.class, String.class, int.class, String.class);

                temp = constructor.newInstance(
                        11989, "transformer.ha.tier.09", "Highly Ultimate Hi-Amp Transformer", 9,
                        //UEV -> UHV (Use Soft Mallet to invert
                        translateToLocal("gt.blockmachines.transformer.ha.tier.09.desc"));
                CustomItemList.Transformer_HA_UEV_UHV.set(temp.getStackForm(1));

                temp = constructor.newInstance(
                        11910, "transformer.ha.tier.10", "Extremely Ultimate Hi-Amp Transformer", 10,
                        //UIV -> UEV (Use Soft Mallet to invert)
                        translateToLocal("gt.blockmachines.transformer.ha.tier.10.desc"));
                Util.setTier(10, temp);
                if (GT_Values.GT.isClientSide()) {
                    field.set(temp, method.invoke(temp, iTexture));
                }
                CustomItemList.Transformer_HA_UIV_UEV.set(temp.getStackForm(1));

                temp = constructor.newInstance(
                        11911, "transformer.ha.tier.11", "Insanely Ultimate Hi-Amp Transformer", 11,
                        //UMV -> UIV (Use Soft Mallet to invert)
                        translateToLocal("gt.blockmachines.transformer.ha.tier.11.desc"));
                Util.setTier(11, temp);
                if (GT_Values.GT.isClientSide()) {
                    field.set(temp, method.invoke(temp, iTexture));
                }
                CustomItemList.Transformer_HA_UMV_UIV.set(temp.getStackForm(1));

                temp = constructor.newInstance(
                        11912, "transformer.ha.tier.12", "Mega Ultimate Hi-Amp Transformer", 12,
                        //UXV -> UMV (Use Soft Mallet to invert)
                        translateToLocal("gt.blockmachines.transformer.ha.tier.12.desc"));
                Util.setTier(12, temp);
                if (GT_Values.GT.isClientSide()) {
                    field.set(temp, method.invoke(temp, iTexture));
                }
                CustomItemList.Transformer_HA_UXV_UMV.set(temp.getStackForm(1));

                temp = constructor.newInstance(
                        11913, "transformer.ha.tier.13", "Extended Mega Ultimate Hi-Amp Transformer", 13,
                        //OPV -> UXV (Use Soft Mallet to invert)
                        translateToLocal("gt.blockmachines.transformer.ha.tier.13.desc"));
                Util.setTier(13, temp);
                if (GT_Values.GT.isClientSide()) {
                    field.set(temp, method.invoke(temp, iTexture));
                }
                CustomItemList.Transformer_HA_OPV_UXV.set(temp.getStackForm(1));

                temp = constructor.newInstance(
                        11914, "transformer.ha.tier.14", "Overpowered Hi-Amp Transformer", 14,
                        //MAX -> OPV (Use Soft Mallet to invert)
                        translateToLocal("gt.blockmachines.transformer.ha.tier.14.desc"));
                Util.setTier(14, temp);
                if (GT_Values.GT.isClientSide()) {
                    field.set(temp, method.invoke(temp, iTexture));
                }
                CustomItemList.Transformer_HA_MAXV_OPV.set(temp.getStackForm(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}