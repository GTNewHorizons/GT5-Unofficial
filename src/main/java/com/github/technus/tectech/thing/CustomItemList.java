package com.github.technus.tectech.thing;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.W;

public enum CustomItemList implements IItemContainer {
    Casing_UEV,Casing_UIV,Casing_UMV,Casing_UXV,Casing_OPV,Casing_MAXV,
    Hull_UEV,Hull_UIV,Hull_UMV,Hull_UXV,Hull_OPV,Hull_MAXV,
    Transformer_UEV_UHV,Transformer_UIV_UEV,Transformer_UMV_UIV,
    Transformer_UXV_UMV,Transformer_OPV_UXV,Transformer_MAXV_OPV,
    WetTransformer_LV_ULV,WetTransformer_MV_LV,WetTransformer_HV_MV,WetTransformer_EV_HV,
    WetTransformer_IV_EV,WetTransformer_LuV_IV,WetTransformer_ZPM_LuV,WetTransformer_UV_ZPM,
    WetTransformer_UHV_UV,WetTransformer_UEV_UHV,WetTransformer_UIV_UEV,WetTransformer_UMV_UIV,
    WetTransformer_UXV_UMV,WetTransformer_OPV_UXV,WetTransformer_MAXV_OPV,

    Transformer_HA_UEV_UHV,Transformer_HA_UIV_UEV,Transformer_HA_UMV_UIV,Transformer_HA_UXV_UMV,
    Transformer_HA_OPV_UXV,Transformer_HA_MAXV_OPV,

    hatch_CreativeMaitenance,hatch_CreativeData,
    Machine_OwnerDetector,Machine_DataReader,
    Machine_BuckConverter_IV,Machine_BuckConverter_LuV,Machine_BuckConverter_ZPM,
    Machine_BuckConverter_UV,Machine_BuckConverter_UHV,Machine_BuckConverter_UEV,
    Machine_BuckConverter_UIV,Machine_BuckConverter_UMV, Machine_BuckConverter_UXV,
    Machine_DebugWriter,Machine_DebugGenny,
    EMpipe, DATApipe, rack_Hatch, holder_Hatch, capacitor_Hatch,

    eM_dynamomulti4_IV, eM_dynamomulti16_IV, eM_dynamomulti64_IV,
    eM_dynamomulti4_LuV, eM_dynamomulti16_LuV, eM_dynamomulti64_LuV,
    eM_dynamomulti4_ZPM, eM_dynamomulti16_ZPM, eM_dynamomulti64_ZPM,
    eM_dynamomulti4_UV, eM_dynamomulti16_UV, eM_dynamomulti64_UV,
    eM_dynamomulti4_UHV, eM_dynamomulti16_UHV, eM_dynamomulti64_UHV,
    eM_dynamomulti4_UEV, eM_dynamomulti16_UEV, eM_dynamomulti64_UEV,
    eM_dynamomulti4_UIV, eM_dynamomulti16_UIV, eM_dynamomulti64_UIV,
    eM_dynamomulti4_UMV, eM_dynamomulti16_UMV, eM_dynamomulti64_UMV,
    eM_dynamomulti4_UXV, eM_dynamomulti16_UXV, eM_dynamomulti64_UXV,

    eM_energymulti4_IV, eM_energymulti16_IV, eM_energymulti64_IV,
    eM_energymulti4_LuV, eM_energymulti16_LuV, eM_energymulti64_LuV,
    eM_energymulti4_ZPM, eM_energymulti16_ZPM, eM_energymulti64_ZPM,
    eM_energymulti4_UV, eM_energymulti16_UV, eM_energymulti64_UV,
    eM_energymulti4_UHV, eM_energymulti16_UHV, eM_energymulti64_UHV,
    eM_energymulti4_UEV, eM_energymulti16_UEV, eM_energymulti64_UEV,
    eM_energymulti4_UIV, eM_energymulti16_UIV, eM_energymulti64_UIV,
    eM_energymulti4_UMV, eM_energymulti16_UMV, eM_energymulti64_UMV,
    eM_energymulti4_UXV, eM_energymulti16_UXV, eM_energymulti64_UXV,

    eM_in_UV, eM_in_UHV, eM_in_UEV, eM_in_UIV, eM_in_UMV, eM_in_UXV,
    eM_out_UV, eM_out_UHV, eM_out_UEV, eM_out_UIV, eM_out_UMV, eM_out_UXV,
    eM_muffler_UV, eM_muffler_UHV, eM_muffler_UEV, eM_muffler_UIV, eM_muffler_UMV, eM_muffler_UXV,
    Parametrizer_Hatch, ParametrizerX_Hatch, Uncertainty_Hatch, UncertaintyX_Hatch, dataIn_Hatch, dataOut_Hatch, dataInAss_Hatch, dataOutAss_Hatch,
    eM_Containment, eM_Containment_Field, eM_Containment_Advanced, eM_Coil, eM_Teleportation, eM_Dimensional, eM_Ultimate_Containment, eM_Ultimate_Containment_Advanced, eM_Ultimate_Containment_Field, eM_Spacetime, eM_Computer_Casing, eM_Computer_Bus, eM_Computer_Vent, eM_Hollow, eM_Power,
    debugBlock,
    Machine_Multi_Microwave, Machine_Multi_teslaCoil,
    Machine_Multi_Transformer,
    Machine_Multi_Computer, Machine_Multi_Switch, Machine_Multi_Research, Machine_Multi_DataBank,
    Machine_Multi_MatterToEM, Machine_Multi_EMToMatter, Machine_Multi_EMjunction, Machine_Multi_Scanner,
    Machine_Multi_EssentiaToEM, Machine_Multi_EMToEssentia,
    Machine_Multi_Infuser, Machine_Multi_Collider,
    Machine_Multi_Decay, Machine_Multi_EMmachine,
    Machine_Multi_Stabilizer, Machine_Multi_EMCrafter,
    Machine_Multi_Wormhole, Machine_Multi_Annihilation,
    Machine_Multi_BHG,
    hint_0,hint_1,hint_2,hint_3,hint_4,hint_5,hint_6,hint_7,hint_8,hint_9,hint_10,hint_11, hint_general,hint_air,hint_noAir,hint_error,

    scanContainer,parametrizerMemory;


    private ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    //public static Fluid sOilExtraHeavy, sOilHeavy, sOilMedium, sOilLight, sNaturalGas;

    @Override
    public IItemContainer set(Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) {
            return this;
        }
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public IItemContainer set(ItemStack aStack) {
        mHasNotBeenSet = false;
        mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public Item getItem() {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return null;
        }
        return mStack.getItem();
    }

    @Override
    public Block getBlock() {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        return GT_Utility.getBlockFromStack(new ItemStack(getItem()));
    }

    @Override
    public final boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    @Override
    public boolean isStackEqual(Object aStack) {
        return isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        }
        return GT_Utility.areUnificationsEqual((ItemStack) aStack, aWildcard ? getWildcard(1) : get(1), aIgnoreNBT);
    }

    @Override
    public ItemStack get(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, W, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage() - 1, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) {
            return null;
        }
        rStack.setStackDisplayName(aDisplayName);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) {
            return null;
        }
        GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public IItemContainer registerOre(Object... aOreNames) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        for (Object tOreName : aOreNames) {
            GT_OreDictUnificator.registerOre(tOreName, get(1));
        }
        return this;
    }

    @Override
    public IItemContainer registerWildcardAsOre(Object... aOreNames) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        for (Object tOreName : aOreNames) {
            GT_OreDictUnificator.registerOre(tOreName, getWildcard(1));
        }
        return this;
    }
}
