package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

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
                GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                if (aMaterial.mStandardMoltenFluid != null) {
                    if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(576L))
                            .duration(6 * SECONDS + 8 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)) {
                    switch (aMaterial.mName) {
                        case "Wood" -> GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "SPS", "PsP", "SPS", 'P', OrePrefixes.plank.get(aMaterial), 'S',
                                OrePrefixes.stick.get(aMaterial) });
                        case "Stone" -> GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "SPS", "PfP", "SPS", 'P', OrePrefixes.stoneSmooth, 'S',
                                new ItemStack(Blocks.stone_button, 1, 32767) });
                        default -> {
                            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                                GT_ModHandler.addCraftingRecipe(
                                    GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial, 1L),
                                    GT_Proxy.tBits,
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
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Gear_Small.get(0L))
                            .itemOutputs(GT_Utility.copyAmount(1L, aStack))
                            .fluidInputs(aMaterial.getMolten(144L))
                            .duration(16 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8))
                            .addTo(fluidSolidifierRecipes);
                    }
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)) {
                    switch (aMaterial.mName) {
                        case "Wood" -> GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "P ", " s", 'P', OrePrefixes.plank.get(aMaterial) });
                        case "Stone" -> GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "P ", " f", 'P', OrePrefixes.stoneSmooth });
                        default -> {
                            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                                GT_ModHandler.addCraftingRecipe(
                                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L),
                                    GT_Proxy.tBits,
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
