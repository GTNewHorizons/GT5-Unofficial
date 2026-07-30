package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

/// The ingot-to-nugget, ingot-to-gear, and nugget-to-ingot alloy smelter conversions, for every material in
/// [#ELIGIBLE].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingAlloySmelterGtpp {

    private ProcessingAlloySmelterGtpp() {}

    /// The frozen set of materials this pass covers. Notably excludes the pure-gas and pure-liquid materials
    /// (`Bromine`, `Krypton`).
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
        long duration = Math.max(MaterialUtils.mass(material) * 2L, 1L);
        long voltage = MaterialUtils.voltageMultiplier(material);

        ItemStack ingot = ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, 1L);
        ItemStack nugget = ProcessingDustGeneration.stackOf(OrePrefixes.nugget, material, 1L);
        ItemStack gear = ProcessingDustGeneration.stackOf(OrePrefixes.gearGt, material, 1L);

        if (ingot != null && nugget != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingot, ItemList.Shape_Mold_Nugget.get(0))
                .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.nugget, material, 9L))
                .duration(duration)
                .eut(voltage)
                .addTo(alloySmelterRecipes);
        }

        if (ingot != null && gear != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ProcessingDustGeneration.stackOf(OrePrefixes.ingot, material, 8L),
                    ItemList.Shape_Mold_Gear.get(0))
                .itemOutputs(gear)
                .duration(duration)
                .eut(voltage)
                .addTo(alloySmelterRecipes);
        }

        if (ingot != null && nugget != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ProcessingDustGeneration.stackOf(OrePrefixes.nugget, material, 9L),
                    ItemList.Shape_Mold_Ingot.get(0))
                .itemOutputs(ingot)
                .duration(duration)
                .eut(voltage)
                .addTo(alloySmelterRecipes);
        }
    }
}
