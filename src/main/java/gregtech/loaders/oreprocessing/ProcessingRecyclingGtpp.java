package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.VP;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.RECYCLE;
import static gregtech.api.util.GTRecipeConstants.UniversalArcFurnace;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTUtility;

/// Reproduces the retired gtPlusPlus `RecipeGenRecycling` for every material in [#ELIGIBLE]: for every
/// [#PREFIXES] shape the material carries, a macerator recipe into the best-fitting dust size, a universal arc
/// furnace recipe into ingot or nugget, and a fluid extractor recipe into the material's own fluid.
///
/// [#materialFluid] resolves the fluid extractor output by name ([GTMaterialProperties#LEGACY_FLUIDS] ->
/// [FluidNames#legacyGtppFluidName]) rather than through [MU]'s state-specific accessors, which cannot resolve
/// a gtPlusPlus-only material's fluid -- see [ProcessingAlloyBlastSmelter]'s class javadoc for the same
/// resolution.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingRecyclingGtpp {

    private ProcessingRecyclingGtpp() {}

    /// Every material the retired `RecipeGenRecycling` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value), `#generateOreMaterialWithAllExcessComponents`, or
    /// `#generateNuclearMaterial`/`#generateNuclearDusts` (which construct it unconditionally) -- excluding a
    /// `PURE_GAS`/`PURE_LIQUID`-state material reached only through `generate`, since it returns before
    /// constructing this generator for those two states (`Bromine`, `Krypton`). Membership here does not by
    /// itself mean a recipe registers -- see [#generate]'s runtime state gate, which also excludes several
    /// `PURE_LIQUID`/`GAS`-state nuclear materials reached through the unconditional nuclear path.
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
        Materials2Materials.LFTRFuel3);
    // spotless:on

    /// The retired generator's exact prefix scan order, minus `ingotHot`: every material in [#ELIGIBLE] is
    /// solid or liquid by the time it reaches [#generate]'s state gate, and the retired generator skipped
    /// `ingotHot` unconditionally regardless of whether it resolved.
    // spotless:off
    private static final OrePrefixes[] PREFIXES = { OrePrefixes.ingot, OrePrefixes.nugget, OrePrefixes.plate,
        OrePrefixes.plateDense, OrePrefixes.plateDouble, OrePrefixes.plateTriple, OrePrefixes.plateQuadruple,
        OrePrefixes.plateQuintuple, OrePrefixes.stick, OrePrefixes.stickLong, OrePrefixes.bolt, OrePrefixes.screw,
        OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.gearGt, OrePrefixes.gearGtSmall, OrePrefixes.block,
        OrePrefixes.cableGt01, OrePrefixes.cableGt02, OrePrefixes.cableGt04, OrePrefixes.cableGt08,
        OrePrefixes.cableGt12, OrePrefixes.wireFine, OrePrefixes.wireGt01, OrePrefixes.wireGt02,
        OrePrefixes.wireGt04, OrePrefixes.wireGt08, OrePrefixes.wireGt12, OrePrefixes.wireGt16, OrePrefixes.foil,
        OrePrefixes.frameGt, OrePrefixes.pipeHuge, OrePrefixes.pipeLarge, OrePrefixes.pipeMedium,
        OrePrefixes.pipeSmall, OrePrefixes.pipeTiny, OrePrefixes.dust };
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        if (MU.hasFlag(material, GTMaterialFlag.NO_RECYCLING)
            || MU.hasFlag(material, GTMaterialFlag.NO_RECYCLING_RECIPES)) {
            return;
        }
        String state = ProcessingOreMachine.gtppState(material);
        if (!"SOLID".equals(state) && !"LIQUID".equals(state)) return;

        for (OrePrefixes prefix : PREFIXES) {
            ItemStack input = ProcessingDustGeneration.stackOf(prefix, material, 1L);
            if (input == null) continue;
            boolean isDustInput = prefix == OrePrefixes.dust;
            long materialAmount = prefix.getMaterialAmount();

            if (!isDustInput) {
                registerMaceration(material, input, materialAmount);
                registerArcFurnace(material, input, materialAmount);
            }

            if (isDustInput && MU.blastFurnaceRequired(material)) continue;
            registerFluidExtraction(material, input, materialAmount);
        }
    }

    private static void registerMaceration(Material material, ItemStack input, long materialAmount) {
        ItemStack dust = recycledDust(material, materialAmount);
        if (dust == null) return;
        int duration = (int) Math.max(16L, (materialAmount * Math.max(1L, MU.mass(material))) / M);
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(dust)
            .duration(duration * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);
    }

    private static void registerArcFurnace(Material material, ItemStack input, long materialAmount) {
        ItemStack arcOutput = null;
        long ingotAmount = materialAmount / M;
        if (ingotAmount > 0) {
            arcOutput = ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, ingotAmount);
        }
        if (arcOutput == null) {
            long nuggetAmount = (materialAmount * 9) / M;
            if (nuggetAmount > 0) {
                arcOutput = ProcessingDustGeneration.stackOf(OrePrefixes.nugget, material, nuggetAmount);
            }
        }
        if (arcOutput == null) return;

        long tAmount = materialAmount * Math.max(1L, MU.mass(material));
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1L, input))
            .itemOutputs(arcOutput)
            .duration((int) Math.max(16L, tAmount / M))
            .eut((int) Math.max(30L, Math.min(120L, tAmount / 192L)))
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);
    }

    private static void registerFluidExtraction(Material material, ItemStack input, long materialAmount) {
        int fluidAmount = (int) ((materialAmount * INGOTS) / (M * input.stackSize));
        int duration = (int) Math.max(1L, (24 * materialAmount) / M);
        FluidStack fluidOutput = materialFluid(material, fluidAmount);
        if (fluidOutput == null) return;

        long powerUsage = Math.max(8L, voltageMultiplier(material));
        int powerTier = GTUtility.getTier(powerUsage);
        if (powerTier > 0 && powerTier < VP.length && powerUsage > VP[powerTier]) {
            powerUsage = VP[powerTier];
        }

        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .fluidOutputs(fluidOutput)
            .duration(duration)
            .eut((int) Math.min(Integer.MAX_VALUE, powerUsage))
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);
    }

    /// The best dust size resolving `materialAmount`/[GTValues#M] units of `material` without a remainder,
    /// preferring `dust`, then `dustSmall`, then `dustTiny` -- the retired `RecipeGenRecycling#getDust`.
    private static ItemStack recycledDust(Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack stack = null;
        if (materialAmount % M == 0 || materialAmount >= M * 16) {
            stack = ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, materialAmount / M);
        }
        if (stack == null && ((materialAmount * 4) % M == 0 || materialAmount >= M * 8)) {
            stack = ProcessingDustGeneration.stackOf(OrePrefixes.dustSmall, material, (materialAmount * 4) / M);
        }
        if (stack == null && (materialAmount * 9) >= M) {
            stack = ProcessingDustGeneration.stackOf(OrePrefixes.dustTiny, material, (materialAmount * 9) / M);
        }
        return stack;
    }

    private static FluidStack materialFluid(Material material, long amount) {
        return MU.legacyGtppFluid(material, amount);
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
