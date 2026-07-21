package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingLens INSTANCE;

    public ProcessingLens() {
        INSTANCE = this;
        OrePrefixes.lens.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        // Blacklist materials which are handled by Werkstoff loader
        if (material == Materials.Salt || material == Materials.RockSalt || material == Materials.Spodumene) return;

        AEApi.instance()
            .registries()
            .blockingModeIgnoreItem()
            .register(stack);

        switch (material.mName) {
            case "Diamond", "Glass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.lens, material, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(latheRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1L))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.lens, material, 3L),
                        GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
                    .duration(2 * MINUTES)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(latheRecipes);
            }
            case "ChromaticGlass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 16L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.lens, material, 1L))
                    .duration(10 * MINUTES)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(compressorRecipes);
            }
            default -> {
                if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L));
                    if (GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L) == null) {
                        recipeBuilder.itemOutputs(GTOreDictUnificator.get(OrePrefixes.lens, material, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.lens, material, 1L),
                            GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L));
                    }
                    recipeBuilder.duration(1 * MINUTES)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(latheRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1L) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1L));
                    if (GTOreDictUnificator.get(OrePrefixes.dust, material, 1L) == null) {
                        recipeBuilder.itemOutputs(GTOreDictUnificator.get(OrePrefixes.lens, material, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.lens, material, 1L),
                            GTOreDictUnificator.get(OrePrefixes.dust, material, 2L));
                    }
                    recipeBuilder.duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(latheRecipes);
                }
                final ITexture lensCoverTexture = TextureFactory.of(Textures.BlockIcons.OVERLAY_LENS, material.mRGBa);
                CoverRegistry.registerDecorativeCover(
                    stack,
                    TextureFactory.of(Textures.BlockIcons.MACHINE_CASINGS[2][0], lensCoverTexture));
            }
        }
    }
}
