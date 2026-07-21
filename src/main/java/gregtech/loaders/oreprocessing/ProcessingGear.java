package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingGear implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingGear INSTANCE;

    public ProcessingGear() {
        INSTANCE = this;
        OrePrefixes.gearGt.add(this);
        OrePrefixes.gearGtSmall.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        switch (prefix.getName()) {
            case "gearGt" -> {
                GTModHandler.removeRecipeByOutputDelayed(stack);
                if (material.mStandardMoltenFluid != null) {
                    if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(prefix, material, 1L))
                            .fluidInputs(material.getMolten(4 * INGOTS))
                            .duration(6 * SECONDS + 8 * TICKS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (material.mUnifiable && (material.mMaterialInto == material)
                    && !MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    switch (material.mName) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "SPS", "PsP", "SPS", 'P', OrePrefixes.plank.ingredient(material), 'S',
                                OrePrefixes.stick.ingredient(material) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "SPS", "PfP", "SPS", 'P', OrePrefixes.stoneSmooth, 'S',
                                new ItemStack(Blocks.stone_button, 1, 32767) });
                        default -> {
                            if (material.getProcessingMaterialTierEU() < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "SPS", "PwP", "SPS", 'P', OrePrefixes.plate.ingredient(material),
                                        'S', OrePrefixes.stick.ingredient(material) });
                            }
                        }
                    }
                }
            }
            case "gearGtSmall" -> {
                if (material.mStandardMoltenFluid != null) {
                    if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear_Small.get(0L))
                            .itemOutputs(GTUtility.copyAmount(1, stack))
                            .fluidInputs(material.getMolten(1 * INGOTS))
                            .duration(16 * TICKS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (material.mUnifiable && (material.mMaterialInto == material)
                    && !MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    switch (material.mName) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "P ", " s", 'P', OrePrefixes.plank.ingredient(material) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "P ", " f", 'P', OrePrefixes.stoneSmooth });
                        default -> {
                            if (material.getProcessingMaterialTierEU() < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { " S ", "hPx", " S ", 'S', OrePrefixes.stick.ingredient(material),
                                        'P', OrePrefixes.plate.ingredient(material) });
                            }
                        }
                    }
                }
            }
            default -> {}
        }
    }
}
