package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import appeng.api.AEApi;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipeBuilder;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static final ProcessingLens INSTANCE = new ProcessingLens();

    private ProcessingLens() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        AEApi.instance()
            .registries()
            .blockingModeIgnoreItem()
            .register(stack);

        switch (MaterialUtils.internalName(material)) {
            case "Diamond", "Glass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.findStack(material, Shapes.plate, 1))
                    .itemOutputs(
                        MaterialLibAPI.findStack(material, Shapes.lens, 1),
                        MaterialLibAPI.findStack(material, Shapes.dustSmall, 1))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(latheRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1))
                    .itemOutputs(
                        MaterialLibAPI.findStack(material, Shapes.lens, 3),
                        MaterialLibAPI.findStack(material, Shapes.dust, 1))
                    .duration(2 * MINUTES)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(latheRecipes);
            }
            case "ChromaticGlass" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.findStack(material, Shapes.dust, 16))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.lens, 1))
                    .duration(10 * MINUTES)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(compressorRecipes);
            }
            default -> {
                if (MaterialLibAPI.findStack(material, Shapes.plate, 1) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(MaterialLibAPI.findStack(material, Shapes.plate, 1));
                    if (MaterialLibAPI.findStack(material, Shapes.dustSmall, 1) == null) {
                        recipeBuilder.itemOutputs(MaterialLibAPI.findStack(material, Shapes.lens, 1));
                    } else {
                        recipeBuilder.itemOutputs(
                            MaterialLibAPI.findStack(material, Shapes.lens, 1),
                            MaterialLibAPI.findStack(material, Shapes.dustSmall, 1));
                    }
                    recipeBuilder.duration(1 * MINUTES)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(latheRecipes);
                }
                if (MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1) != null) {
                    GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                    recipeBuilder.itemInputs(MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1));
                    if (MaterialLibAPI.findStack(material, Shapes.dust, 1) == null) {
                        recipeBuilder.itemOutputs(MaterialLibAPI.findStack(material, Shapes.lens, 1));
                    } else {
                        recipeBuilder.itemOutputs(
                            MaterialLibAPI.findStack(material, Shapes.lens, 1),
                            MaterialLibAPI.findStack(material, Shapes.dust, 2));
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
