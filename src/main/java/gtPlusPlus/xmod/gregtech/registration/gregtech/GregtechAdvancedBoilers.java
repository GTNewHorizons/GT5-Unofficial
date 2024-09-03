package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_MV;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.recipe.RecipesMachineComponents;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEBoilerHV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEBoilerLV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEBoilerMV;

public class GregtechAdvancedBoilers {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Advanced Boilers.");
        run1();
    }

    private static void run1() {
        // Boilers
        GregtechItemList.Boiler_Advanced_LV
            .set(new MTEBoilerLV(Boiler_Advanced_LV.ID, "Advanced Boiler [LV]", 1).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_MV
            .set(new MTEBoilerMV(Boiler_Advanced_MV.ID, "Advanced Boiler [MV]", 2).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_HV
            .set(new MTEBoilerHV(Boiler_Advanced_HV.ID, "Advanced Boiler [HV]", 3).getStackForm(1L));

        ItemStack chassisT1 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 0, 1);
        ItemStack chassisT2 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 1, 1);
        ItemStack chassisT3 = ItemUtils
            .getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 2, 1);

        // Make the Coil in each following recipe a hammer and a Screwdriver.

        // Chassis Recipes
        GTModHandler.addCraftingRecipe(
            chassisT1,
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_ULV, 'P',
                OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.ULV), 'W',
                OrePrefixes.plate.get(Materials.Lead), 'G', OrePrefixes.pipeSmall.get(Materials.Copper) });

        GTModHandler.addCraftingRecipe(
            chassisT2,
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel),
                'C', OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.plate.get(Materials.Steel), 'G',
                OrePrefixes.pipeSmall.get(Materials.Bronze) });

        GTModHandler.addCraftingRecipe(
            chassisT3,
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WCW", "GMG", "WPW", 'M', ItemList.Hull_MV, 'P',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.plate.get(Materials.Aluminium), 'G', OrePrefixes.pipeSmall.get(Materials.Steel) });

        ItemStack pipeTier1 = ItemUtils.getItemStackOfAmountFromOreDict(RecipesMachineComponents.pipeTier7, 1);
        ItemStack pipeTier2 = ItemUtils.getItemStackOfAmountFromOreDict(RecipesMachineComponents.pipeTier8, 1);
        ItemStack pipeTier3 = ItemUtils.getItemStackOfAmountFromOreDict(RecipesMachineComponents.pipeTier9, 1);

        // Boiler Recipes
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_LV, 'P', pipeTier1, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', chassisT1, 'G', OrePrefixes.gear.get(Materials.Steel) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_MV, 'P', pipeTier2, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', chassisT2, 'G',
                MaterialsAlloy.SILICON_CARBIDE.getGear(1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "dCw", "WMW", "GPG", 'M', ItemList.Hull_HV, 'P', pipeTier3, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', chassisT3, 'G',
                MaterialsAlloy.SILICON_CARBIDE.getGear(1) });
    }
}
