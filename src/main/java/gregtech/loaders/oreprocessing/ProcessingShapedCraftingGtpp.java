package gregtech.loaders.oreprocessing;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.util.GTModHandler;

/// Reproduces the retired gtPlusPlus `RecipeGenShapedCrafting` for every material in [#ELIGIBLE]: the
/// crafting-table shape conversions (plate, double plate, ring, frame box, bolt, fine wire, foil, rod, long
/// rod, rotor, gear, screw).
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingShapedCraftingGtpp {

    private ProcessingShapedCraftingGtpp() {}

    /// Every material the retired `RecipeGenShapedCrafting` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value) or `#generateOreMaterialWithAllExcessComponents`, both of which construct
    /// it unconditionally -- excluding a `PURE_GAS`/`PURE_LIQUID`-state material, since `generate` returns
    /// before constructing it for those two states (`Bromine`, `Krypton`). Membership here does not by itself
    /// mean a recipe registers -- see [#generate]'s runtime tier gate.
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
        if (voltageMultiplier(material) > TierEU.RECIPE_IV) return;
        boolean radioactive = isRadioactive(material);

        ItemStack ingot = ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, 1L);
        ItemStack plate = ProcessingDustGeneration.stackOf(OrePrefixes.plate, material, 1L);
        ItemStack plateDouble = ProcessingDustGeneration.stackOf(OrePrefixes.plateDouble, material, 1L);
        ItemStack rod = ProcessingDustGeneration.stackOf(OrePrefixes.stick, material, 1L);
        ItemStack longRod = ProcessingDustGeneration.stackOf(OrePrefixes.stickLong, material, 1L);
        ItemStack ring = ProcessingDustGeneration.stackOf(OrePrefixes.ring, material, 1L);
        ItemStack frameBox = ProcessingDustGeneration.stackOf(OrePrefixes.frameGt, material, 1L);
        ItemStack bolt = ProcessingDustGeneration.stackOf(OrePrefixes.bolt, material, 1L);
        ItemStack screw = ProcessingDustGeneration.stackOf(OrePrefixes.screw, material, 1L);
        ItemStack foil = ProcessingDustGeneration.stackOf(OrePrefixes.foil, material, 1L);
        ItemStack fineWire = ProcessingDustGeneration.stackOf(OrePrefixes.wireFine, material, 1L);
        ItemStack rotor = ProcessingDustGeneration.stackOf(OrePrefixes.rotor, material, 1L);
        ItemStack gear = ProcessingDustGeneration.stackOf(OrePrefixes.gearGt, material, 1L);

        if (plate != null && ingot != null) {
            craft(plate, new Object[] { "h", "B", "I", 'I', ingot, 'B', ingot });
        }
        if (plateDouble != null && plate != null) {
            craft(plateDouble, new Object[] { "I", "B", "h", 'I', plate, 'B', plate });
        }
        if (!radioactive && ring != null && rod != null) {
            craft(ring, new Object[] { "h ", "fR", 'R', rod });
        }
        if (!radioactive && frameBox != null && rod != null) {
            craft(
                ProcessingDustGeneration.stackOf(OrePrefixes.frameGt, material, 2L),
                new Object[] { "RRR", "RwR", "RRR", 'R', rod });
        }
        if (!radioactive && bolt != null && rod != null) {
            craft(
                ProcessingDustGeneration.stackOf(OrePrefixes.bolt, material, 2L),
                new Object[] { "s ", " R", 'R', rod });
        }
        if (!radioactive && foil != null && fineWire != null) {
            craft(fineWire, new Object[] { "Fx", 'F', foil });
        }
        if (foil != null && plate != null) {
            craft(ProcessingDustGeneration.stackOf(OrePrefixes.foil, material, 2L), new Object[] { "hP", 'P', plate });
        }
        if (rod != null && ingot != null) {
            craft(rod, new Object[] { "f ", " I", 'I', ingot });
        }
        if (rod != null && longRod != null) {
            craft(
                ProcessingDustGeneration.stackOf(OrePrefixes.stick, material, 2L),
                new Object[] { "s", "L", 'L', longRod });
        }
        if (longRod != null && rod != null) {
            craft(longRod, new Object[] { "RhR", 'R', rod });
        }
        if (!radioactive && rotor != null && ring != null && plate != null && screw != null) {
            craft(rotor, new Object[] { "PhP", "SRf", "PdP", 'P', plate, 'S', screw, 'R', ring });
        }
        if (!radioactive && gear != null && plate != null && rod != null) {
            craft(gear, new Object[] { "RPR", "PwP", "RPR", 'P', plate, 'R', rod });
        }
        if (!radioactive && screw != null && bolt != null) {
            craft(screw, new Object[] { "fB", "B ", 'B', bolt });
        }
    }

    private static void craft(ItemStack output, Object[] pattern) {
        GTModHandler.addCraftingRecipe(
            output,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            pattern);
    }

    private static boolean isRadioactive(Material material) {
        return Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.IS_RADIOACTIVE));
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
