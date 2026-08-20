package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        switch (prefix.getName()) {
            case "gearGt" -> {
                GTModHandler.removeRecipeByOutputDelayed(stack);
                if (MaterialUtils.hasMolten(material)) {
                    if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(prefix, material, 1L))
                            .fluidInputs(MaterialUtils.molten(material, 4 * INGOTS))
                            .duration(6 * SECONDS + 8 * TICKS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    switch (MaterialUtils.internalName(material)) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "SPS", "PsP", "SPS", 'P',
                                MaterialParts.craftIngredient(OrePrefixes.plank, material), 'S',
                                MaterialParts.craftIngredient(OrePrefixes.stick, material) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "SPS", "PfP", "SPS", 'P', OrePrefixes.stoneSmooth, 'S',
                                new ItemStack(Blocks.stone_button, 1, 32767) });
                        default -> {
                            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGt, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "SPS", "PwP", "SPS", 'P',
                                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'S',
                                        MaterialParts.craftIngredient(OrePrefixes.stick, material) });
                            }
                        }
                    }
                }
            }
            case "gearGtSmall" -> {
                if (MaterialUtils.hasMolten(material)) {
                    if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear_Small.get(0L))
                            .itemOutputs(GTUtility.copyAmount(1, stack))
                            .fluidInputs(MaterialUtils.molten(material, 1 * INGOTS))
                            .duration(16 * TICKS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    switch (MaterialUtils.internalName(material)) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "P ", " s", 'P',
                                MaterialParts.craftIngredient(OrePrefixes.plank, material) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "P ", " f", 'P', OrePrefixes.stoneSmooth });
                        default -> {
                            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { " S ", "hPx", " S ", 'S',
                                        MaterialParts.craftIngredient(OrePrefixes.stick, material), 'P',
                                        MaterialParts.craftIngredient(OrePrefixes.plate, material) });
                            }
                        }
                    }
                }
            }
            default -> {}
        }
    }
}
