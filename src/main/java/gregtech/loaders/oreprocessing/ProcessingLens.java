package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.RecipeBuilder.MINUTES;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.OreDictUnificator;
import gregtech.api.util.RecipeBuilder;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingLens() {
        OrePrefixes.lens.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aMaterial.mName) {
            case "Diamond", "Glass" -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .itemOutputs(
                        OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                        OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(latheRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L))
                    .itemOutputs(
                        OreDictUnificator.get(OrePrefixes.lens, aMaterial, 3L),
                        OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(2 * MINUTES)
                    .eut(16)
                    .addTo(latheRecipes);
            }
            case "ChromaticGlass" -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(OreDictUnificator.get(OrePrefixes.dust, aMaterial, 16L))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L))
                    .duration(10 * MINUTES)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(compressorRecipes);
            }
            default -> {
                if (OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                    RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                    recipeBuilder.itemInputs(OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L));
                    if (OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) == null) {
                        recipeBuilder.itemOutputs(OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                            OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L));
                    }
                    recipeBuilder.duration(1 * MINUTES)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(latheRecipes);
                }
                if (OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L) != null) {
                    RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                    recipeBuilder.itemInputs(OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L));
                    if (OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) == null) {
                        recipeBuilder.itemOutputs(OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                            OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L));
                    }
                    recipeBuilder.duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(latheRecipes);
                }
                final ITexture lensCoverTexture = TextureFactory
                    .of(Textures.BlockIcons.OVERLAY_LENS, aMaterial.mRGBa, false);
                GregTech_API.registerCover(
                    aStack,
                    TextureFactory.of(Textures.BlockIcons.MACHINE_CASINGS[2][0], lensCoverTexture),
                    new gregtech.common.covers.GT_Cover_Lens(aMaterial.mColor.mIndex, lensCoverTexture));
            }
        }
    }
}
