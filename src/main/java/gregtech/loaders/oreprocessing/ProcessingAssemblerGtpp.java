package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Set;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.SubstituteFluidStack;

/// Reproduces the retired gtPlusPlus `RecipeGenAssembler` for every material in [#ELIGIBLE]: the rod-to-frame
/// and plate/ring-to-rotor assembler recipes.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingAssemblerGtpp {

    private ProcessingAssemblerGtpp() {}

    /// Every material the retired `RecipeGenAssembler` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value) or `#generateOreMaterialWithAllExcessComponents`, both of which construct
    /// it unconditionally -- excluding a `PURE_GAS`/`PURE_LIQUID`-state material, since `generate` returns
    /// before constructing it for those two states (`Bromine`, `Krypton`). No `generateNuclearMaterial` call
    /// ever passes `generatePlates=true`, the only path that would have added a nuclear material here too.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials.Selenium, Materials.Iodine, Materials.Rhenium,
        Materials.Thallium, Materials.Germanium, Materials.Technetium,
        Materials.Lithium7, Materials.Uranium232, Materials.Uranium233,
        Materials.AdvancedNitinol, Materials.AstralTitanium, Materials.CelestialTungsten,
        Materials.Hypogen, Materials.ChromaticGlass, Materials.BlackMetal,
        Materials.Dragonblood, Materials.SiliconCarbide, Materials.ZirconiumCarbide,
        Materials.TantalumCarbide, Materials.NiobiumCarbide, Materials.TungstenTitaniumCarbide,
        Materials.EnergyCrystal, Materials.BloodSteel, Materials.Zeron100,
        Materials.Tumbaga, Materials.Potin, Materials.Staballoy,
        Materials.Tantalloy60, Materials.Tantalloy61, Materials.Inconel625,
        Materials.Inconel690, Materials.Inconel792, Materials.EglinSteel,
        Materials.MaragingSteel250, Materials.MaragingSteel300, Materials.MaragingSteel350,
        Materials.WatertightSteel, Materials.Nitinol60, Materials.Stellite,
        Materials.Talonite, Materials.HastelloyW, Materials.HastelloyX,
        Materials.HastelloyC276, Materials.HastelloyN, Materials.Incoloy020,
        Materials.IncoloyDS, Materials.IncoloyMA956, Materials.Grisium,
        Materials.HG1223, Materials.TriniumTitaniumAlloy, Materials.TriniumNaquadahAlloy,
        Materials.TriniumNaquadahCarbonite, Materials.ArceusAlloy2B, Materials.HeLiCoPtEr,
        Materials.LafiumCompound, Materials.CinobiteA243, Materials.Pikyonium64B,
        Materials.AbyssalAlloy, Materials.Laurenium, Materials.Botmium,
        Materials.HS188A, Materials.Titansteel, Materials.Arcanite,
        Materials.Octiron, Materials.BabbitAlloy, Materials.BlackTitanium,
        Materials.Indalloy140, Materials.Rhugnor, Materials.Quantum,
        Materials.AncientGranite, Materials.Runite);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        long voltage = MaterialUtils.voltageMultiplier(material);

        ItemStack rod = ProcessingDustGeneration.stackOf(OrePrefixes.stick, material, 1L);
        ItemStack frameBox = ProcessingDustGeneration.stackOf(OrePrefixes.frameGt, material, 1L);
        if (rod != null && frameBox != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.stick, material, 4L))
                .circuit(4)
                .itemOutputs(frameBox)
                .duration(3 * SECONDS)
                .eut(voltage)
                .addTo(assemblerRecipes);
        }

        ItemStack plate = ProcessingDustGeneration.stackOf(OrePrefixes.plate, material, 1L);
        ItemStack ring = ProcessingDustGeneration.stackOf(OrePrefixes.ring, material, 1L);
        ItemStack rotor = ProcessingDustGeneration.stackOf(OrePrefixes.rotor, material, 1L);
        if (plate != null && ring != null && rotor != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.plate, material, 4L), ring)
                .itemOutputs(rotor)
                .fluidInputs(SubstituteFluidStack.soldering(NUGGETS))
                .duration(240)
                .eut(voltage)
                .addTo(assemblerRecipes);
        }
    }
}
