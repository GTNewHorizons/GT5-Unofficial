package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
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
        Materials2Materials.Selenium, Materials2Materials.Iodine, Materials2Materials.Rhenium,
        Materials2Materials.Thallium, Materials2Materials.Germanium, Materials2Materials.Technetium,
        Materials2Materials.Lithium7, Materials2Materials.Uranium232, Materials2Materials.Uranium233,
        Materials2Materials.AdvancedNitinol, Materials2Materials.AstralTitanium, Materials2Materials.CelestialTungsten,
        Materials2Materials.Hypogen, Materials2Materials.ChromaticGlass, Materials2Materials.BlackMetal,
        Materials2Materials.Dragonblood, Materials2Materials.SiliconCarbide, Materials2Materials.ZirconiumCarbide,
        Materials2Materials.TantalumCarbide, Materials2Materials.NiobiumCarbide, Materials2Materials.TungstenTitaniumCarbide,
        Materials2Materials.EnergyCrystal, Materials2Materials.BloodSteel, Materials2Materials.Zeron100,
        Materials2Materials.Tumbaga, Materials2Materials.Potin, Materials2Materials.Staballoy,
        Materials2Materials.Tantalloy60, Materials2Materials.Tantalloy61, Materials2Materials.Inconel625,
        Materials2Materials.Inconel690, Materials2Materials.Inconel792, Materials2Materials.EglinSteel,
        Materials2Materials.MaragingSteel250, Materials2Materials.MaragingSteel300, Materials2Materials.MaragingSteel350,
        Materials2Materials.WatertightSteel, Materials2Materials.Nitinol60, Materials2Materials.Stellite,
        Materials2Materials.Talonite, Materials2Materials.HastelloyW, Materials2Materials.HastelloyX,
        Materials2Materials.HastelloyC276, Materials2Materials.HastelloyN, Materials2Materials.Incoloy020,
        Materials2Materials.IncoloyDS, Materials2Materials.IncoloyMA956, Materials2Materials.Grisium,
        Materials2Materials.HG1223, Materials2Materials.TriniumTitaniumAlloy, Materials2Materials.TriniumNaquadahAlloy,
        Materials2Materials.TriniumNaquadahCarbonite, Materials2Materials.ArceusAlloy2B, Materials2Materials.HeLiCoPtEr,
        Materials2Materials.LafiumCompound, Materials2Materials.CinobiteA243, Materials2Materials.Pikyonium64B,
        Materials2Materials.AbyssalAlloy, Materials2Materials.Laurenium, Materials2Materials.Botmium,
        Materials2Materials.HS188A, Materials2Materials.Titansteel, Materials2Materials.Arcanite,
        Materials2Materials.Octiron, Materials2Materials.BabbitAlloy, Materials2Materials.BlackTitanium,
        Materials2Materials.Indalloy140, Materials2Materials.Rhugnor, Materials2Materials.Quantum,
        Materials2Materials.AncientGranite, Materials2Materials.Runite);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        long voltage = voltageMultiplier(material);

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

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
