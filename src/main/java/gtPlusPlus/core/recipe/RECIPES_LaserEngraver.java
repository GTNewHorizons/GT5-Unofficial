package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.everglades.dimension.Dimension_Everglades;

public class RECIPES_LaserEngraver implements IOreRecipeRegistrator {

    public RECIPES_LaserEngraver() {
        OrePrefixes.crafting.add(this);
    }

    @Override
    public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
            final String aModName, final ItemStack aStack) {
        if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {
            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lithium, 2L),
                            GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("plateDoubleLithium7", 1))
                    .duration(4 * MINUTES).eut(TierEU.RECIPE_EV).addTo(laserEngraverRecipes);
            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3L),
                            GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1)).duration(2 * MINUTES)
                    .eut(TierEU.RECIPE_EV).addTo(laserEngraverRecipes);

        } else if (aOreDictName.equals(OreDictNames.craftingLensLime.toString())) {
            // Coil Wires
            ItemStack coilWire1 = ItemUtils
                    .getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire", "coilWire1", 0, 1);
            ItemStack coilWire2 = ItemUtils
                    .getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:1", "coilWire2", 1, 1);
            ItemStack coilWire3 = ItemUtils
                    .getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:2", "coilWire3", 2, 1);
            ItemStack coilWire4 = ItemUtils
                    .getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:3", "coilWire4", 3, 1);

            // Simple Life
            String wire = "wireGt02";

            // Wires to Laser
            ItemStack wireT1a = ItemUtils.getItemStackOfAmountFromOreDict(wire + "Aluminium", 1);
            ItemStack wireT1b = ItemUtils.getItemStackOfAmountFromOreDict(wire + "Nichrome", 1);
            ItemStack wireT2a = ItemUtils.getItemStackOfAmountFromOreDict(wire + "Osmium", 1);
            ItemStack wireT2b = ItemUtils.getItemStackOfAmountFromOreDict(wire + "Platinum", 1);
            ItemStack wireT3a = ItemUtils.getItemStackOfAmountFromOreDict(wire + "VanadiumGallium", 1);
            ItemStack wireT3b = ItemUtils.getItemStackOfAmountFromOreDict(wire + "YttriumBariumCuprate", 1);
            ItemStack wireT3c = ItemUtils.getItemStackOfAmountFromOreDict(wire + "NiobiumTitanium", 1);
            ItemStack wireT4a = ItemUtils.getItemStackOfAmountFromOreDict(wire + "Naquadah", 1);

            // T1
            GT_Values.RA.stdBuilder().itemInputs(wireT1a, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire1)
                    .duration(10 * SECONDS).eut(TierEU.RECIPE_HV).addTo(laserEngraverRecipes);
            GT_Values.RA.stdBuilder().itemInputs(wireT1b, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire1)
                    .duration(10 * SECONDS).eut(TierEU.RECIPE_HV).addTo(laserEngraverRecipes);
            // T2
            GT_Values.RA.stdBuilder().itemInputs(wireT2a, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire2)
                    .duration(20 * SECONDS).eut(TierEU.RECIPE_EV).addTo(laserEngraverRecipes);
            GT_Values.RA.stdBuilder().itemInputs(wireT2b, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire2)
                    .duration(20 * SECONDS).eut(TierEU.RECIPE_EV).addTo(laserEngraverRecipes);
            // T3
            GT_Values.RA.stdBuilder().itemInputs(wireT3a, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire3)
                    .duration(30 * SECONDS).eut(TierEU.RECIPE_IV).addTo(laserEngraverRecipes);
            GT_Values.RA.stdBuilder().itemInputs(wireT3b, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire3)
                    .duration(30 * SECONDS).eut(TierEU.RECIPE_IV).addTo(laserEngraverRecipes);
            GT_Values.RA.stdBuilder().itemInputs(wireT3c, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire3)
                    .duration(30 * SECONDS).eut(TierEU.RECIPE_IV).addTo(laserEngraverRecipes);
            // T4
            GT_Values.RA.stdBuilder().itemInputs(wireT4a, GT_Utility.copyAmount(0L, aStack)).itemOutputs(coilWire4)
                    .duration(40 * SECONDS).eut(TierEU.RECIPE_LuV).addTo(laserEngraverRecipes);

        } else if (aOreDictName.equals(OreDictNames.craftingLensOrange.toString())) {
            GT_Values.RA.stdBuilder()
                    .itemInputs(ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemUtils.getSimpleStack(Dimension_Everglades.portalItem)).duration(3 * HOURS)
                    .eut(TierEU.RECIPE_IV).addTo(laserEngraverRecipes);
        }
    }
}
