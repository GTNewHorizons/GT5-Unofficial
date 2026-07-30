package gregtech.loaders.oreprocessing;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

/// The crafting-table shape conversions (plate, double plate, ring, frame box, bolt, fine wire, foil, rod,
/// long rod, rotor, gear, screw), for every material in [#ELIGIBLE].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingShapedCraftingGtpp {

    private ProcessingShapedCraftingGtpp() {}

    /// The frozen set of materials this pass covers. Notably excludes the pure-gas and pure-liquid materials
    /// (`Bromine`, `Krypton`). Membership does not by itself mean a recipe registers -- see [#generate]'s
    /// runtime tier gate.
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
        if (MaterialUtils.voltageMultiplier(material) > TierEU.RECIPE_IV) return;
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
}
