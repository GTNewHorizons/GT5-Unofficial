package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingGear implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingGear() {
        OrePrefixes.gearGt.add(this);
        OrePrefixes.gearGtSmall.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aPrefix) {
            case gearGt -> {
                GTModHandler.removeRecipeByOutputDelayed(aStack);
                if (aMaterial.mStandardMoltenFluid != null) {
                    if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(576L))
                            .duration(6 * SECONDS + 8 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)) {
                    switch (aMaterial.mName) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "SPS", "PsP", "SPS", 'P', OrePrefixes.plank.get(aMaterial), 'S',
                                OrePrefixes.stick.get(aMaterial) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "SPS", "PfP", "SPS", 'P', OrePrefixes.stoneSmooth, 'S',
                                new ItemStack(Blocks.stone_button, 1, 32767) });
                        default -> {
                            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                                    GTProxy.tBits,
                                    new Object[] { "SPS", "PwP", "SPS", 'P', OrePrefixes.plate.get(aMaterial), 'S',
                                        OrePrefixes.stick.get(aMaterial) });
                            }
                        }
                    }
                }
            }
            case gearGtSmall -> {
                if (aMaterial.mStandardMoltenFluid != null) {
                    if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear_Small.get(0L))
                            .itemOutputs(GTUtility.copyAmount(1, aStack))
                            .fluidInputs(aMaterial.getMolten(144L))
                            .duration(16 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)) {
                    switch (aMaterial.mName) {
                        case "Wood" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "P ", " s", 'P', OrePrefixes.plank.get(aMaterial) });
                        case "Stone" -> GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "P ", " f", 'P', OrePrefixes.stoneSmooth });
                        default -> {
                            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                                    GTProxy.tBits,
                                    new Object[] { " S ", "hPx", " S ", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                                        OrePrefixes.plate.get(aMaterial) });
                            }
                        }
                    }
                }
            }
            default -> {}
        }
    }
}
