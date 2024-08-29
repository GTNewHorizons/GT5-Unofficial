package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Buffer_Dynamo_ZPM;
import static gtPlusPlus.core.recipe.common.CI.bitsd;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DynamoBuffer;

public class GregtechBufferDynamos {

    public static void run() {
        run2();
    }

    private static void run2() {
        GregtechItemList.Hatch_Buffer_Dynamo_ULV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_ULV.ID,
                "hatch.dynamo.buffer.tier.00",
                "ULV Dynamo Hatch [Buffered]",
                0).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_LV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_LV.ID,
                "hatch.dynamo.buffer.tier.01",
                "LV Dynamo Hatch [Buffered]",
                1).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_MV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_MV.ID,
                "hatch.dynamo.buffer.tier.02",
                "MV Dynamo Hatch [Buffered]",
                2).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_HV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_HV.ID,
                "hatch.dynamo.buffer.tier.03",
                "HV Dynamo Hatch [Buffered]",
                3).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_EV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_EV.ID,
                "hatch.dynamo.buffer.tier.04",
                "EV Dynamo Hatch [Buffered]",
                4).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_IV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_IV.ID,
                "hatch.dynamo.buffer.tier.05",
                "IV Dynamo Hatch [Buffered]",
                5).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_LuV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_LuV.ID,
                "hatch.dynamo.buffer.tier.06",
                "LuV Dynamo Hatch [Buffered]",
                6).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_ZPM.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_ZPM.ID,
                "hatch.dynamo.buffer.tier.07",
                "ZPM Dynamo Hatch [Buffered]",
                7).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_UV.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_UV.ID,
                "hatch.dynamo.buffer.tier.08",
                "UV Dynamo Hatch [Buffered]",
                8).getStackForm(1L));
        GregtechItemList.Hatch_Buffer_Dynamo_MAX.set(
            new GT_MetaTileEntity_Hatch_DynamoBuffer(
                Hatch_Buffer_Dynamo_MAX.ID,
                "hatch.dynamo.buffer.tier.09",
                "UHV Dynamo Hatch [Buffered]",
                9).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_ULV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_ULV, 'T', CI.getTieredCircuit(0), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_LV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_LV, 'T', CI.getTieredCircuit(1), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_MV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_MV, 'T', CI.getTieredCircuit(2), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_HV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_HV, 'T', CI.getTieredCircuit(3), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_EV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_EV, 'T', CI.getTieredCircuit(4), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_IV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_IV, 'T', CI.getTieredCircuit(5), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Tungsten) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_LuV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_LuV, 'T', CI.getTieredCircuit(6), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.VanadiumGallium) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_ZPM.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_ZPM, 'T', CI.getTieredCircuit(7), 'C',
                OrePrefixes.cableGt04.get((Object) Materials.Naquadah) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_UV.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_UV, 'T', CI.getTieredCircuit(8), 'C',
                OrePrefixes.wireGt12.get((Object) Materials.NaquadahAlloy) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Buffer_Dynamo_MAX.get(1L),
            bitsd,
            new Object[] { "TMC", 'M', ItemList.Hatch_Dynamo_UHV, 'T', CI.getTieredCircuit(9), 'C',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUHV) });
    }
}
