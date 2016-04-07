package com.detrav.items.processing;

import com.detrav.enums.DetravSimpleItems;
import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.blocks.GT_Item_Machines;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class ProcessingDetravPortableCharger  implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDetravPortableCharger()
    {
        OrePrefixes.cableGt01.add(this);
    }

    @Override
    public void registerOre(OrePrefixes orePrefixes, Materials materials, String s, String s1, ItemStack itemStack) {

        boolean bEC = !GT_Mod.gregtechproxy.mHardcoreCables;
        long loss = 0;
        long voltage = 0;
        if (materials == Materials.Cobalt) {
            loss = bEC ? 2L : 2L;
            voltage = gregtech.api.enums.GT_Values.V[1];
        } else if (materials == Materials.Lead) {
            loss = bEC ? 2L : 2L;
            voltage = gregtech.api.enums.GT_Values.V[1];
        } else if (materials == Materials.Tin) {
            loss = bEC ? 1L : 1L;
            voltage = gregtech.api.enums.GT_Values.V[1];
        } else if (materials == Materials.Zinc) {
            loss = bEC ? 1L : 1L;
            voltage = gregtech.api.enums.GT_Values.V[1];
        } else if (materials == Materials.SolderingAlloy) {
            loss = bEC ? 1L : 1L;
            voltage = gregtech.api.enums.GT_Values.V[1];
        } else if (materials == Materials.Iron) {
            loss = bEC ? 3L : 4L;
            voltage = gregtech.api.enums.GT_Values.V[2];
        } else if (materials == Materials.Nickel) {
            loss = bEC ? 3L : 5L;
            voltage = gregtech.api.enums.GT_Values.V[2];
        } else if (materials == Materials.Cupronickel) {
            loss = bEC ? 3L : 4L;
            voltage = gregtech.api.enums.GT_Values.V[2];
        } else if (materials == Materials.Copper) {
            loss = bEC ? 2L : 3L;
            voltage = gregtech.api.enums.GT_Values.V[2];
        } else if (materials == Materials.AnnealedCopper) {
            loss = bEC ? 1L : 2L;
            voltage = gregtech.api.enums.GT_Values.V[2];
        } else if (materials == Materials.Kanthal) {
            loss = bEC ? 3L : 8L;
            voltage = gregtech.api.enums.GT_Values.V[3];
        } else if (materials == Materials.Gold) {
            loss = bEC ? 2L : 6L;
            voltage = gregtech.api.enums.GT_Values.V[3];
        } else if (materials == Materials.Electrum) {
            loss = bEC ? 2L : 5L;
            voltage = gregtech.api.enums.GT_Values.V[3];
        } else if (materials == Materials.Silver) {
            loss = bEC ? 1L : 4L;
            voltage = gregtech.api.enums.GT_Values.V[3];
        } else if (materials == Materials.BlueAlloy) {
            loss = bEC ? 1L : 4L;
            voltage = gregtech.api.enums.GT_Values.V[3];
        } else if (materials == Materials.Nichrome) {
            loss = bEC ? 4L : 32L;
            voltage = gregtech.api.enums.GT_Values.V[4];
        } else if (materials == Materials.Steel) {
            loss = bEC ? 2L : 16L;
            voltage = gregtech.api.enums.GT_Values.V[4];
        } else if (materials == Materials.TungstenSteel) {
            loss = bEC ? 2L : 14L;
            voltage = gregtech.api.enums.GT_Values.V[4];
        } else if (materials == Materials.Tungsten) {
            loss = bEC ? 2L : 12L;
            voltage = gregtech.api.enums.GT_Values.V[4];
        } else if (materials == Materials.Aluminium) {
            loss = bEC ? 1L : 8L;
            voltage = gregtech.api.enums.GT_Values.V[4];
        } else if (materials == Materials.Graphene) {
            loss = bEC ? 1L : 16L;
            voltage = gregtech.api.enums.GT_Values.V[5];
        } else if (materials == Materials.Osmium) {
            loss = bEC ? 2L : 32L;
            voltage = gregtech.api.enums.GT_Values.V[5];
        } else if (materials == Materials.Platinum) {
            loss = bEC ? 1L : 16L;
            voltage = gregtech.api.enums.GT_Values.V[5];
        } else if (materials == Materials.Naquadah) {
            loss = bEC ? 1L : 64L;
            voltage = gregtech.api.enums.GT_Values.V[6];
        } else if (materials == Materials.NiobiumTitanium) {
            loss = bEC ? 2L : 128L;
            voltage = gregtech.api.enums.GT_Values.V[6];
        } else if (materials == Materials.VanadiumGallium) {
            loss = bEC ? 2L : 128L;
            voltage = gregtech.api.enums.GT_Values.V[6];
        } else if (materials == Materials.YttriumBariumCuprate) {
            loss = bEC ? 4L : 256L;
            voltage = gregtech.api.enums.GT_Values.V[6];
        } else if (materials == Materials.RedAlloy) {
            loss = 0L;
            voltage = gregtech.api.enums.GT_Values.V[0];
        } else if (materials == Materials.Superconductor) {
            loss = 1L;
            voltage = gregtech.api.enums.GT_Values.V[9];
        } else {
            loss = -1;
            voltage = -1;
        }
        if (loss < 0) return;
        switch ((int) voltage) {
            case 32:
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.StainlessSteel, new long[]{4 * 100000L, 32L, 1L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('B'), ItemList.Battery_RE_LV_Lithium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.StainlessSteel, new long[]{4 * 75000L, 32L, 1L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('B'), ItemList.Battery_RE_LV_Cadmium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.StainlessSteel, new long[]{4 * 50000L, 32L, 1L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('B'), ItemList.Battery_RE_LV_Sodium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                break;
            case 128:
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.Titanium, new long[]{4 * 400000L, 128L, 2L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('B'), ItemList.Battery_RE_MV_Lithium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.Titanium, new long[]{4 * 300000L, 128L, 2L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('B'), ItemList.Battery_RE_MV_Cadmium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.Titanium, new long[]{4 * 200000L, 128L, 2L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('B'), ItemList.Battery_RE_MV_Sodium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                break;
            case 512:
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.TungstenSteel, new long[]{4 * 1600000L, 512L, 3L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('B'), ItemList.Battery_RE_HV_Lithium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.TungstenSteel, new long[]{4 * 1200000L, 512L, 3L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('B'), ItemList.Battery_RE_HV_Cadmium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStatsPlus(106, 1, materials, Materials.TungstenSteel, new long[]{4 * 800000L, 512L, 3L, -3L}, loss), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WBW", "BCB", "WBW", Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('B'), ItemList.Battery_RE_HV_Sodium.get(1L, new Object[0]), Character.valueOf('W'), itemStack});
                break;
        }
    }
}
