package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_MV;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.RECIPES_MachineComponents;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_Boiler_HV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_Boiler_LV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_Boiler_MV;

public class GregtechAdvancedBoilers {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Advanced Boilers.");
        run1();
    }

    private static void run1() {
        // Boilers
        GregtechItemList.Boiler_Advanced_LV
            .set(new GT_MetaTileEntity_Boiler_LV(Boiler_Advanced_LV.ID, "Advanced Boiler [LV]", 1).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_MV
            .set(new GT_MetaTileEntity_Boiler_MV(Boiler_Advanced_MV.ID, "Advanced Boiler [MV]", 2).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_HV
            .set(new GT_MetaTileEntity_Boiler_HV(Boiler_Advanced_HV.ID, "Advanced Boiler [HV]", 3).getStackForm(1L));

        ItemStack chassisT1 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 0, 1);
        ItemStack chassisT2 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 1, 1);
        ItemStack chassisT3 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 2, 1);

        // Make the Coil in each following recipe a hammer and a Screwdriver.

        // Chassis Recipes
        GT_ModHandler.addCraftingRecipe(
            chassisT1,
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_ULV, 'P',
                OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.ULV), 'W',
                OrePrefixes.plate.get(Materials.Lead), 'G', OrePrefixes.pipeSmall.get(Materials.Copper) });

        GT_ModHandler.addCraftingRecipe(
            chassisT2,
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel),
                'C', OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.plate.get(Materials.Steel), 'G',
                OrePrefixes.pipeSmall.get(Materials.Bronze) });

        GT_ModHandler.addCraftingRecipe(
            chassisT3,
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_MV, 'P',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.plate.get(Materials.Aluminium), 'G', OrePrefixes.pipeSmall.get(Materials.Steel) });

        ItemStack pipeTier1 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier7, 1);
        ItemStack pipeTier2 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier8, 1);
        ItemStack pipeTier3 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier9, 1);

        // Boiler Recipes
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_LV, 'P', pipeTier1, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', chassisT1, 'G', OrePrefixes.gear.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_MV, 'P', pipeTier2, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', chassisT2, 'G', ALLOY.SILICON_CARBIDE.getGear(1) });

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_HV, 'P', pipeTier3, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', chassisT3, 'G', ALLOY.SILICON_CARBIDE.getGear(1) });
    }
}
