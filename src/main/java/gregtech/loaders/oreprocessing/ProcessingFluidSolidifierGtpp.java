package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;

/// Reproduces the retired gtPlusPlus `RecipeGenFluids` for every material in [#ELIGIBLE]: the mold-plus-fluid
/// solidifier recipes for every shape the material carries (ingot, plate, nugget, gear, small gear, block, rod,
/// long rod, bolt, screw, ring, rotor).
///
/// [#materialFluid] resolves the input fluid by name ([GTMaterialProperties#LEGACY_FLUIDS] ->
/// [FluidNames#legacyGtppFluidName]) rather than through [gregtech.api.material.MU]'s state-specific
/// accessors, which cannot resolve a gtPlusPlus-only material's fluid -- see
/// [ProcessingAlloyBlastSmelter]'s class javadoc for the same resolution.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingFluidSolidifierGtpp {

    private ProcessingFluidSolidifierGtpp() {}

    /// Every material the retired `RecipeGenFluids` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value), `#generateOreMaterialWithAllExcessComponents`, `#generateDusts`, or
    /// `#generateNuclearMaterial`/`#generateNuclearDusts` (all of which construct it unconditionally) --
    /// excluding a `PURE_GAS`/`PURE_LIQUID`-state material reached only through `generate`, since it returns
    /// before constructing this generator for those two states (`Bromine`, `Krypton`).
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
        Materials2Materials.AncientGranite, Materials2Materials.Runite,
        Materials2Materials.Polonium, Materials2Materials.Radium, Materials2Materials.Protactinium,
        Materials2Materials.Curium, Materials2Materials.Neptunium, Materials2Materials.Fermium,
        Materials2Materials.Plutonium238, Materials2Materials.AmmoniumBifluoride, Materials2Materials.BerylliumHydroxide,
        Materials2Materials.BerylliumFluoride, Materials2Materials.LithiumFluoride, Materials2Materials.ThoriumTetrafluoride,
        Materials2Materials.ThoriumHexafluoride, Materials2Materials.UraniumTetrafluoride, Materials2Materials.UraniumHexafluoride,
        Materials2Materials.ZirconiumTetrafluoride, Materials2Materials.NeptuniumHexafluoride, Materials2Materials.TechnetiumHexafluoride,
        Materials2Materials.SeleniumHexafluoride, Materials2Materials.LFTRFuel1, Materials2Materials.LFTRFuel2,
        Materials2Materials.LFTRFuel3, Materials2Materials.EglinSteelBaseCompound);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        if (materialFluid(material, 1) == null) return;

        solidify(material, OrePrefixes.ingot, ItemList.Shape_Mold_Ingot, 1, 144, 3 * SECONDS);
        solidify(material, OrePrefixes.plate, ItemList.Shape_Mold_Plate, 1, 144, 3 * SECONDS);
        solidify(material, OrePrefixes.nugget, ItemList.Shape_Mold_Nugget, 1, 16, 3 * SECONDS);
        solidify(material, OrePrefixes.gearGt, ItemList.Shape_Mold_Gear, 1, 576, 12 * SECONDS);
        solidify(material, OrePrefixes.gearGtSmall, ItemList.Shape_Mold_Gear_Small, 1, 144, 3 * SECONDS);
        solidify(material, OrePrefixes.block, ItemList.Shape_Mold_Block, 1, 144 * 9, 25 * SECONDS);
        solidify(material, OrePrefixes.stick, ItemList.Shape_Mold_Rod, 1, 72, 15 * SECONDS);
        solidify(material, OrePrefixes.stickLong, ItemList.Shape_Mold_Rod_Long, 1, 144, 30 * SECONDS);
        solidify(material, OrePrefixes.bolt, ItemList.Shape_Mold_Bolt, 1, 18, 5 * SECONDS);
        solidify(material, OrePrefixes.screw, ItemList.Shape_Mold_Screw, 1, 18, 5 * SECONDS);
        solidify(material, OrePrefixes.ring, ItemList.Shape_Mold_Ring, 1, 36, 10 * SECONDS);
        solidify(material, OrePrefixes.rotor, ItemList.Shape_Mold_Rotor, 1, 612, 10 * SECONDS);
    }

    private static void solidify(Material material, OrePrefixes shapePrefix, ItemList mold, long shapeAmount,
        int fluidAmount, int duration) {
        ItemStack output = ProcessingDustGeneration.stackOf(shapePrefix, material, shapeAmount);
        if (output == null) return;
        FluidStack fluid = materialFluid(material, fluidAmount);
        if (fluid == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(mold.get(0))
            .itemOutputs(output)
            .fluidInputs(fluid)
            .duration(duration)
            .eut(voltageMultiplier(material))
            .addTo(fluidSolidifierRecipes);
    }

    private static FluidStack materialFluid(Material material, long amount) {
        return MU.legacyGtppFluid(material, amount);
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
