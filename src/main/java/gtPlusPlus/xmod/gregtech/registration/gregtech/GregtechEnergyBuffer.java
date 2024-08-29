package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_ZPM;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOreDictNames;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

public class GregtechEnergyBuffer {

    // Misc Items
    // public static Item itemBufferCore;

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Energy Buffer Blocks.");
        run1();
    }

    private static void run1() {

        // Energy Buffers
        GregtechItemList.Energy_Buffer_1by1_ULV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_ULV.ID,
                "energybuffer.tier.00",
                "Ultra Low Voltage Energy Buffer",
                0,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_LV.ID,
                "energybuffer.tier.01",
                "Low Voltage Energy Buffer",
                1,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_MV.ID,
                "energybuffer.tier.02",
                "Medium Voltage Energy Buffer",
                2,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_HV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_HV.ID,
                "energybuffer.tier.03",
                "High Voltage Energy Buffer",
                3,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_EV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_EV.ID,
                "energybuffer.tier.04",
                "Extreme Voltage Energy Buffer",
                4,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_IV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_IV.ID,
                "energybuffer.tier.05",
                "Insane Voltage Energy Buffer",
                5,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LuV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_LuV.ID,
                "energybuffer.tier.06",
                "Ludicrous Voltage Energy Buffer",
                6,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_ZPM.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_ZPM.ID,
                "energybuffer.tier.07",
                "ZPM Voltage Energy Buffer",
                7,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_UV.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_UV.ID,
                "energybuffer.tier.08",
                "Ultimate Voltage Energy Buffer",
                8,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MAX.set(
            new GregtechMetaEnergyBuffer(
                Energy_Buffer_1by1_MAX.ID,
                "energybuffer.tier.09",
                "MAX Voltage Energy Buffer",
                9,
                "",
                1).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_ULV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt08.get(Materials.Lead), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_LV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt08.get(Materials.Tin), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_MV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt08.get(Materials.AnyCopper), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_HV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt08.get(Materials.Gold), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_EV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt08.get(Materials.Aluminium), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_IV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt08.get(Materials.Tungsten), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_LuV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt08.get(Materials.Osmium), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_ZPM.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt08.get(Materials.Osmium), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_UV.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt08.get(Materials.Osmium), 'T',
                GregtechOreDictNames.buffer_core });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Energy_Buffer_1by1_MAX.get(1L, new Object[0]),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WTW", "WMW", 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt08.get(Materials.SuperconductorUHV), 'T', GregtechOreDictNames.buffer_core });
        /*
         * GT_ModHandler.addCraftingRecipe( GregtechItemList.Energy_Buffer_1by1_MAX.get(1L, new Object[0]),
         * GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE |
         * GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW",
         * Character.valueOf('M'), ItemList.Hull_MAX, Character.valueOf('W'),
         * OrePrefixes.wireGt08.get(Materials.Superconductor), Character.valueOf('T'), GregtechOreDictNames.buffer_core
         * });
         */

    }
}
