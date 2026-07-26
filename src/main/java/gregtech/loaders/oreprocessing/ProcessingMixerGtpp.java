package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2GtppComposites;
import gregtech.api.enums.materials2.Materials2GtppComposites.Component;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTRecipeBuilder;

/// Reproduces gtPlusPlus's `RecipeGenDustGeneration` mixer-recipe generation (ratio of component dusts ->
/// alloy dust) for every material [Materials2GtppComposites] declares. Dispatched once per declared material
/// by [gregtech.loaders.shapeconsumers.ConsumerMixerGtpp].
public class ProcessingMixerGtpp implements IOreRecipeRegistrator {

    public static final ProcessingMixerGtpp INSTANCE = new ProcessingMixerGtpp();

    private ProcessingMixerGtpp() {}

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        List<Component> composites = Materials2GtppComposites.composites(material);
        if (composites.isEmpty()) return;

        ItemStack[] inputs = new ItemStack[composites.size()];
        int total = 0;
        for (int i = 0; i < composites.size(); i++) {
            Component component = composites.get(i);
            inputs[i] = MU.stack(OrePrefixes.dust, component.material(), component.parts());
            if (inputs[i] == null) return;
            total += component.parts();
        }
        ItemStack output = MU.stack(OrePrefixes.dust, material, total);
        if (output == null) return;

        int circuit = circuitFor(material, composites.size());
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(output);
        if (circuit > 0) builder.circuit(circuit);
        builder.duration((int) Math.max(MU.mass(material) * 2L, 1))
            .eut(voltageMultiplier(material))
            .addTo(mixerRecipes);
    }

    /// `EglinSteelBaseCompound` is gtpp's one composite material dispatched through
    /// `RecipeGenDustGeneration#addMixerRecipe_Standalone` instead of the ordinary `generateRecipes` mixer
    /// block: a flat circuit 20 below 4 inputs, rather than a circuit keyed 11/12/13 by input count.
    private static int circuitFor(Material material, int inputCount) {
        if (material == Materials2Materials.EglinSteelBaseCompound) return inputCount <= 3 ? 20 : -1;
        return switch (inputCount) {
            case 1 -> 11;
            case 2 -> 12;
            case 3 -> 13;
            default -> -1;
        };
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
