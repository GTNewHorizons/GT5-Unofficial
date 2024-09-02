package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.everglades.dimension.DimensionEverglades;

public class RecipesLaserEngraver implements IOreRecipeRegistrator {

    public RecipesLaserEngraver() {
        OrePrefixes.crafting.add(this);
    }

    @Override
    public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
        final String aModName, final ItemStack aStack) {
        if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lithium, 2L),
                    GTUtility.copyAmount(0L, aStack))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("plateDoubleLithium7", 1))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3L),
                    GTUtility.copyAmount(0L, aStack))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1))
                .duration(2 * MINUTES)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);

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
            GTValues.RA.stdBuilder()
                .itemInputs(wireT1a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire1)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT1b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire1)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(laserEngraverRecipes);
            // T2
            GTValues.RA.stdBuilder()
                .itemInputs(wireT2a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire2)
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT2b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire2)
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            // T3
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3c, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            // T4
            GTValues.RA.stdBuilder()
                .itemInputs(wireT4a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire4)
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);

        } else if (aOreDictName.equals(OreDictNames.craftingLensOrange.toString())) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk), GTUtility.copyAmount(0L, aStack))
                .itemOutputs(ItemUtils.getSimpleStack(DimensionEverglades.portalItem))
                .duration(3 * HOURS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
        }
    }
}
