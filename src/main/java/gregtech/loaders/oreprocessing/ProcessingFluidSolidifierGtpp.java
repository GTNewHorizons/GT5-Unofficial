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
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

/// Reproduces the retired gtPlusPlus `RecipeGenFluids` for every material in [#ELIGIBLE]: the mold-plus-fluid
/// solidifier recipes for every shape the material carries (ingot, plate, nugget, gear, small gear, block, rod,
/// long rod, bolt, screw, ring, rotor).
///
/// [#materialFluid] resolves the input fluid by name ([MaterialFluidNames] ->
/// [FluidNames#legacyGtppFluidName]) rather than through [gregtech.api.material.MaterialUtils]'s state-specific
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
        Materials.AncientGranite, Materials.Runite,
        Materials.Polonium, Materials.Radium, Materials.Protactinium,
        Materials.Curium, Materials.Neptunium, Materials.Fermium,
        Materials.Plutonium238, Materials.AmmoniumBifluoride, Materials.BerylliumHydroxide,
        Materials.BerylliumFluoride, Materials.LithiumFluoride, Materials.ThoriumTetrafluoride,
        Materials.ThoriumHexafluoride, Materials.UraniumTetrafluoride, Materials.UraniumHexafluoride,
        Materials.ZirconiumTetrafluoride, Materials.NeptuniumHexafluoride, Materials.TechnetiumHexafluoride,
        Materials.SeleniumHexafluoride, Materials.LFTRFuel1, Materials.LFTRFuel2,
        Materials.LFTRFuel3, Materials.EglinSteelBaseCompound);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        if (MaterialUtils.legacyGtppFluid(material, 1) == null) return;

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
        FluidStack fluid = MaterialUtils.legacyGtppFluid(material, fluidAmount);
        if (fluid == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(mold.get(0))
            .itemOutputs(output)
            .fluidInputs(fluid)
            .duration(duration)
            .eut(MaterialUtils.voltageMultiplier(material))
            .addTo(fluidSolidifierRecipes);
    }

}
