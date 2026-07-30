package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

/// The block-to-ingot extruder decompression recipe, for every material in [#ELIGIBLE]. Only that direction:
/// the block/plate/ring/gear/small-gear/rod/bolt/rotor extruder recipes come from the canonical autogen
/// (`ProcessingShaping`, dispatched by `gregtech.loaders.shapeconsumers`), which does not itself provide
/// `block -> ingot`.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingExtruderGtpp {

    private ProcessingExtruderGtpp() {}

    /// The frozen set of materials this pass covers -- nuclear materials included. Notably excludes the
    /// pure-gas and pure-liquid materials (`Bromine`, `Krypton`).
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
            .duration((int) Math.max(MaterialUtils.mass(material) * 2L, 1L))
            .eut(MaterialUtils.voltageMultiplier(material))
            .addTo(extruderRecipes);
    }
}
