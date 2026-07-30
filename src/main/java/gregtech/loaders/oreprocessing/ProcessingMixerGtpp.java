package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;

import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.LegacyGTPPComposites;
import gregtech.api.enums.materials2.LegacyGTPPComposites.Component;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;

/// Reproduces gtPlusPlus's `RecipeGenDustGeneration` mixer-recipe generation (ratio of component dusts ->
/// alloy dust) for every material in [#ELIGIBLE]. Dispatched once per eligible material by
/// [gregtech.loaders.shapeconsumers.ConsumerMixerGtpp]. Ratio data is shared with
/// [ProcessingAlloyBlastSmelter] through [LegacyGTPPComposites], but eligibility is this class's own: some
/// table entries never reached the legacy mixer block (they are alloy-blast-smelter-only), and some materials
/// the mixer block reached are excluded from the alloy blast smelter, so table membership alone cannot gate
/// either consumer's dispatch -- see [LegacyGTPPComposites]'s class javadoc.
public class ProcessingMixerGtpp implements IOreRecipeRegistrator {

    public static final ProcessingMixerGtpp INSTANCE = new ProcessingMixerGtpp();

    private ProcessingMixerGtpp() {}

    /// The exact materials the retired `RecipeGenDustGeneration` mixer generator reached -- see
    /// [LegacyGTPPComposites]'s class javadoc for the construction paths that determined this set.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials.Arcanite, Materials.BabbitAlloy, Materials.BlackMetal,
        Materials.BloodSteel, Materials.Botmium, Materials.EglinSteel,
        Materials.EglinSteelBaseCompound, Materials.EnergyCrystal, Materials.Incoloy020,
        Materials.IncoloyDS, Materials.IncoloyMA956, Materials.Inconel690,
        Materials.Inconel792, Materials.NiobiumCarbide, Materials.Nitinol60,
        Materials.Potin, Materials.SiliconCarbide, Materials.Staballoy,
        Materials.Stellite, Materials.Talonite, Materials.Tantalloy60,
        Materials.Tantalloy61, Materials.TantalumCarbide, Materials.Titansteel,
        Materials.TriniumNaquadahAlloy, Materials.TriniumNaquadahCarbonite,
        Materials.TriniumTitaniumAlloy, Materials.Tumbaga, Materials.TungstenTitaniumCarbide,
        Materials.WoodsGlass, Materials.ZirconiumCarbide);
    // spotless:on

    public static boolean isEligible(Material material) {
        return ELIGIBLE.contains(material);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        List<Component> composites = LegacyGTPPComposites.composites(material);
        if (composites.isEmpty()) return;

        // A composite whose component is a gas or liquid has no mixer recipe: the table is shared with
        // ProcessingAlloyBlastSmelter, whose own eligible set does include such composites.
        ItemStack[] inputs = new ItemStack[composites.size()];
        int total = 0;
        for (int i = 0; i < composites.size(); i++) {
            Component component = composites.get(i);
            if (!component.material()
                .hasShape(Shapes.dust)) return;
            inputs[i] = MaterialLibAPI.getStack(component.material(), Shapes.dust, component.parts());
            total += component.parts();
        }
        ItemStack output = MaterialLibAPI.getStack(material, Shapes.dust, total);

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
        if (material == Materials.EglinSteelBaseCompound) return inputCount <= 3 ? 20 : -1;
        return switch (inputCount) {
            case 1 -> 11;
            case 2 -> 12;
            case 3 -> 13;
            default -> -1;
        };
    }
}
