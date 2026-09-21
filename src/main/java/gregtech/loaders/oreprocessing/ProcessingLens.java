package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import appeng.api.AEApi;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipeBuilder;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingLens INSTANCE;

    public ProcessingLens() {
        INSTANCE = this;
        OrePrefixes.lens.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == Materials.Salt || material == Materials.RockSalt || material == Materials.Spodumene) return;

        AEApi.instance()
            .registries()
            .blockingModeIgnoreItem()
            .register(stack);

        switch (MaterialUtils.internalName(material)) {
            case "Diamond", "Glass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialParts.stack(OrePrefixes.plate, material, 1L))
                    .itemOutputs(
                        MaterialParts.stack(OrePrefixes.lens, material, 1L),
                        MaterialParts.stack(OrePrefixes.dustSmall, material, 1L))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(latheRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialParts.stack(OrePrefixes.gemExquisite, material, 1L))
                    .itemOutputs(
                        MaterialParts.stack(OrePrefixes.lens, material, 3L),
                        MaterialParts.stack(OrePrefixes.dust, material, 1L))
                    .duration(2 * MINUTES)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(latheRecipes);
            }
            case "ChromaticGlass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialParts.stack(OrePrefixes.dust, material, 16L))
                    .itemOutputs(MaterialParts.stack(OrePrefixes.lens, material, 1L))
                    .duration(10 * MINUTES)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(compressorRecipes);
            }
            default -> {
                if (MaterialParts.stack(OrePrefixes.plate, material, 1L) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(MaterialParts.stack(OrePrefixes.plate, material, 1L));
                    if (MaterialParts.stack(OrePrefixes.dustSmall, material, 1L) == null) {
                        recipeBuilder.itemOutputs(MaterialParts.stack(OrePrefixes.lens, material, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            MaterialParts.stack(OrePrefixes.lens, material, 1L),
                            MaterialParts.stack(OrePrefixes.dustSmall, material, 1L));
                    }
                    recipeBuilder.duration(1 * MINUTES)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(latheRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.gemExquisite, material, 1L) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(MaterialParts.stack(OrePrefixes.gemExquisite, material, 1L));
                    if (MaterialParts.stack(OrePrefixes.dust, material, 1L) == null) {
                        recipeBuilder.itemOutputs(MaterialParts.stack(OrePrefixes.lens, material, 1L));
                    } else {
                        recipeBuilder.itemOutputs(
                            MaterialParts.stack(OrePrefixes.lens, material, 1L),
                            MaterialParts.stack(OrePrefixes.dust, material, 2L));
                    }
                    recipeBuilder.duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(latheRecipes);
                }
                final ITexture lensCoverTexture = GTMaterialTextures
                    .stored(Textures.BlockIcons.OVERLAY_LENS, () -> MaterialUtils.rgba(material));
                CoverRegistry.registerDecorativeCover(
                    stack,
                    TextureFactory.of(Textures.BlockIcons.MACHINE_CASINGS[2][0], lensCoverTexture));
            }
        }
    }
}
