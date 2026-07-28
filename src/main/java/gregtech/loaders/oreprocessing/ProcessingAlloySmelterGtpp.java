package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;

/// Reproduces the retired gtPlusPlus `RecipeGenAlloySmelter` for every material in [#ELIGIBLE]: the
/// ingot-to-nugget, ingot-to-gear, and nugget-to-ingot alloy smelter conversions.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingAlloySmelterGtpp {

    private ProcessingAlloySmelterGtpp() {}

    /// Every material the retired `RecipeGenAlloySmelter` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value) or `#generateOreMaterialWithAllExcessComponents`, both of which construct
    /// it unconditionally -- excluding a `PURE_GAS`/`PURE_LIQUID`-state material, since `generate` returns
    /// before constructing it for those two states (`Bromine`, `Krypton`).
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
        long duration = Math.max(MU.mass(material) * 2L, 1L);
        long voltage = MU.voltageMultiplier(material);

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
