package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;

/// Reproduces the retired gtPlusPlus `RecipeGenExtruder` for every material in [#ELIGIBLE]: the block-to-ingot
/// extruder decompression recipe. The block/plate/ring/gear/small-gear/rod/bolt/rotor extruder recipes
/// formerly generated alongside it are covered by the canonical autogen (`ProcessingShaping`, dispatched by
/// `gregtech.loaders.shapeconsumers`), which does not itself provide this `block -> ingot` direction.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingExtruderGtpp {

    private ProcessingExtruderGtpp() {}

    /// Every material the retired `RecipeGenExtruder` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value), `#generateOreMaterialWithAllExcessComponents`, or
    /// `#generateNuclearMaterial`/`#generateNuclearDusts` (which construct it unconditionally, independent of
    /// `generatePlates`/`disableOptionalRecipes`) -- excluding a `PURE_GAS`/`PURE_LIQUID`-state material
    /// reached only through `generate`, since it returns before constructing this generator for those two
    /// states (`Bromine`, `Krypton`; the state check does not gate `generateNuclearMaterial`).
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
    }

    private static void generate(Material material) {
        ItemStack block = ProcessingDustGeneration.stackOf(OrePrefixes.block, material, 1L);
        ItemStack ingot = ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, 1L);
        if (block == null || ingot == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(block, ItemList.Shape_Extruder_Ingot.get(0))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, 9L))
            .duration((int) Math.max(MU.mass(material) * 2L, 1L))
            .eut(voltageMultiplier(material))
            .addTo(extruderRecipes);
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
