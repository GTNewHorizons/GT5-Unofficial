package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;

import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2GtppComposites;
import gregtech.api.enums.materials2.Materials2GtppComposites.Component;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;

/// Reproduces gtPlusPlus's `RecipeGenDustGeneration` mixer-recipe generation (ratio of component dusts ->
/// alloy dust) for every material in [#ELIGIBLE]. Dispatched once per eligible material by
/// [gregtech.loaders.shapeconsumers.ConsumerMixerGtpp]. Ratio data is shared with
/// [ProcessingAlloyBlastSmelter] through [Materials2GtppComposites], but eligibility is this class's own: some
/// table entries never reached the legacy mixer block (they are alloy-blast-smelter-only), and some materials
/// the mixer block reached are excluded from the alloy blast smelter, so table membership alone cannot gate
/// either consumer's dispatch -- see [Materials2GtppComposites]'s class javadoc.
public class ProcessingMixerGtpp implements IOreRecipeRegistrator {

    public static final ProcessingMixerGtpp INSTANCE = new ProcessingMixerGtpp();

    private ProcessingMixerGtpp() {}

    /// The exact materials the retired `RecipeGenDustGeneration` mixer generator reached -- see
    /// [Materials2GtppComposites]'s class javadoc for the construction paths that determined this set.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials2Materials.Arcanite, Materials2Materials.BabbitAlloy, Materials2Materials.BlackMetal,
        Materials2Materials.BloodSteel, Materials2Materials.Botmium, Materials2Materials.EglinSteel,
        Materials2Materials.EglinSteelBaseCompound, Materials2Materials.EnergyCrystal, Materials2Materials.Incoloy020,
        Materials2Materials.IncoloyDS, Materials2Materials.IncoloyMA956, Materials2Materials.Inconel690,
        Materials2Materials.Inconel792, Materials2Materials.NiobiumCarbide, Materials2Materials.Nitinol60,
        Materials2Materials.Potin, Materials2Materials.SiliconCarbide, Materials2Materials.Staballoy,
        Materials2Materials.Stellite, Materials2Materials.Talonite, Materials2Materials.Tantalloy60,
        Materials2Materials.Tantalloy61, Materials2Materials.TantalumCarbide, Materials2Materials.Titansteel,
        Materials2Materials.TriniumNaquadahAlloy, Materials2Materials.TriniumNaquadahCarbonite,
        Materials2Materials.TriniumTitaniumAlloy, Materials2Materials.Tumbaga, Materials2Materials.TungstenTitaniumCarbide,
        Materials2Materials.WoodsGlass, Materials2Materials.ZirconiumCarbide);
    // spotless:on

    public static boolean isEligible(Material material) {
        return ELIGIBLE.contains(material);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        List<Component> composites = Materials2GtppComposites.composites(material);
        if (composites.isEmpty()) return;

        ItemStack[] inputs = new ItemStack[composites.size()];
        int total = 0;
        for (int i = 0; i < composites.size(); i++) {
            Component component = composites.get(i);
            inputs[i] = MaterialParts.stack(Materials2Shapes.dust, component.material(), component.parts());
            if (inputs[i] == null) return;
            total += component.parts();
        }
        ItemStack output = MaterialParts.stack(Materials2Shapes.dust, material, total);
        if (output == null) return;

        int circuit = circuitFor(material, composites.size());
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(output);
        if (circuit > 0) builder.circuit(circuit);
        builder.duration((int) Math.max(MaterialUtils.mass(material) * 2L, 1))
            .eut(MaterialUtils.voltageMultiplier(material))
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
}
