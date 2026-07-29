package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidNames;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.FluidNames;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

/// Reproduces the retired gtPlusPlus `RecipeGenPlasma` for every material in [#ELIGIBLE]: the plasma-cell
/// plasma-turbine fuel recipe and the plasma-cell-to-cell vacuum freezer cooldown recipe, for whichever
/// material in [#ELIGIBLE] carries a plasma fluid.
///
/// [#materialPlasma] resolves the plasma fluid by name ([gregtech.api.enums.materials2.Materials2FluidNames]'s `plasma`
/// slot)
/// rather than through [MU]'s state-specific accessors, which cannot resolve a gtPlusPlus-only material's
/// fluid -- see [ProcessingAlloyBlastSmelter]'s class javadoc for the same resolution. The cooldown recipe's
/// cell output resolves through [ProcessingOreMachine#cellStack] rather than a plain `cell` shape lookup: every
/// material here carries a molten fluid, so its single cell-eligible shape is `cellMolten`, not `cell`.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingPlasmaGtpp {

    private ProcessingPlasmaGtpp() {}

    /// Every material the retired `RecipeGenPlasma` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value), `#generateOreMaterialWithAllExcessComponents`, or
    /// `#generateNuclearMaterial`/`#generateNuclearDusts` (which construct it unconditionally) -- excluding a
    /// `PURE_GAS`/`PURE_LIQUID`-state material reached only through `generate`, since it returns before
    /// constructing this generator for those two states (`Bromine`, `Krypton`).
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

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
        generateBromine();
    }

    /// Bromine's canner and vacuum-freezer recipes, handled separately from [#ELIGIBLE]: `RecipeGenPlasma`
    /// itself never reached Bromine (excluded by `generate`'s own `PURE_LIQUID` state gate -- see [#ELIGIBLE]'s
    /// javadoc), so these came from elsewhere in the retired gtpp bootstrap rather than from that generator.
    /// Bromine's legacy cell item is gone with the rest of `gtPlusPlus.core.item.base.cell`
    /// (`GtppItemCutoverTable` already carries Postea rows migrating it), so this ports the recipes onto the
    /// canonical `cellMolten`/`cellPlasmaLight` shapes Bromine already generates.
    private static void generateBromine() {
        Material bromine = Materials2Materials.Bromine;
        ItemStack liquidCell = ProcessingDustGeneration.stackOf(OrePrefixes.cellMolten, bromine, 1L);
        FluidStack molten = MaterialUtils.molten(bromine, 1000);
        if (liquidCell == null || molten == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(liquidCell)
            .fluidInputs(molten)
            .duration(molten.amount / 62)
            .eut(1)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, liquidCell))
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidOutputs(molten)
            .duration(molten.amount / 62)
            .eut(1)
            .addTo(cannerRecipes);

        ItemStack plasmaCell = ProcessingDustGeneration.stackOf(OrePrefixes.cellPlasma, bromine, 1L);
        FluidStack plasma = materialPlasma(bromine, 1000);
        int cooldownDuration = (int) Math.max(MaterialUtils.mass(bromine) * 2L, 1L);
        if (plasmaCell != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(plasmaCell)
                .itemOutputs(GTUtility.copyAmount(1, liquidCell))
                .duration(cooldownDuration)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);
        }
        if (plasma != null) {
            GTValues.RA.stdBuilder()
                .fluidInputs(plasma)
                .fluidOutputs(molten)
                .duration(cooldownDuration)
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);
        }
    }

    private static void generate(Material material) {
        if (materialPlasma(material, 1) == null) return;

        ItemStack plasmaCell = ProcessingDustGeneration.stackOf(OrePrefixes.cellPlasma, material, 1L);
        ItemStack cell = ProcessingOreMachine.cellStack(material, 1L);
        if (plasmaCell == null) return;

        ItemStack containerItem = GTUtility.getFluidForFilledItem(plasmaCell, true) == null
            ? GTUtility.getContainerItem(plasmaCell, true)
            : ItemList.Cell_Empty.get(1);
        if (containerItem != null) {
            int fuelValue;
            if (material == Materials2Materials.Runite) {
                fuelValue = 350_000;
            } else if (material == Materials2Materials.CelestialTungsten) {
                fuelValue = 720_000;
            } else {
                fuelValue = (int) Math.max(1024L, 1024L * MaterialUtils.mass(material));
            }
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1L, plasmaCell))
                .itemOutputs(containerItem)
                .metadata(FUEL_VALUE, fuelValue)
                .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.PlasmaTurbine.ordinal())
                .duration(0)
                .eut(0)
                .addTo(GTRecipeConstants.Fuel);
        }

        if (cell != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(plasmaCell)
                .itemOutputs(cell)
                .duration((int) Math.max(MaterialUtils.mass(material) * 2L, 1L))
                .eut(TierEU.RECIPE_MV)
                .addTo(vacuumFreezerRecipes);
        }
    }

    private static FluidStack materialPlasma(Material material, long amount) {
        FluidNames fluids = Materials2FluidNames.of(material.getName());
        String name = fluids == null || fluids.plasma() == null ? null
            : fluids.plasma()
                .name();
        Fluid fluid = name == null ? null : FluidRegistry.getFluid(name);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }
}
