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

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTUtility;

/// Reproduces the retired gtPlusPlus `RecipeGenRecycling` for every material in [#ELIGIBLE]: for every
/// [#PREFIXES] shape the material carries, a macerator recipe into the best-fitting dust size, a universal arc
/// furnace recipe into ingot or nugget, and a fluid extractor recipe into the material's own fluid.
///
/// [#materialFluid] resolves the fluid extractor output by name ([gregtech.api.enums.materials2.Materials2FluidNames]
/// ->
/// [FluidNames#legacyGtppFluidName]) rather than through [MaterialParts]'s state-specific accessors, which cannot
/// resolve
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
        Materials.LFTRFuel3);
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
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECYCLING)
            || MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECYCLING_RECIPES)) {
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

            if (isDustInput && MaterialUtils.blastFurnaceRequired(material)) continue;
            registerFluidExtraction(material, input, materialAmount);
        }
    }

    private static void registerMaceration(Material material, ItemStack input, long materialAmount) {
        ItemStack dust = recycledDust(material, materialAmount);
        if (dust == null) return;
        int duration = (int) Math.max(16L, (materialAmount * Math.max(1L, MaterialUtils.mass(material))) / M);
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

        long tAmount = materialAmount * Math.max(1L, MaterialUtils.mass(material));
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
        FluidStack fluidOutput = MaterialUtils.legacyGtppFluid(material, fluidAmount);
        if (fluidOutput == null) return;

        long powerUsage = Math.max(8L, MaterialUtils.voltageMultiplier(material));
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

}
