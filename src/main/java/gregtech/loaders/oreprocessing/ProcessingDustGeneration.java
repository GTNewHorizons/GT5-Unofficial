package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Crafting-table dust-size conversions, the four packager dust/dustSmall/dustTiny conversions, and the
/// dust -> ingot furnace/alloy-smelter/blast-furnace recipe, for every material in [#ELIGIBLE].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes`. The late dispatch is deliberate: several of
/// these materials' stacks only resolve once the rest of gtPlusPlus's postInit has
/// run, so dispatching any earlier -- MaterialLib's own postInit, or a `ShapeConsumerSupport` shape consumer
/// -- silently drops or misresolves recipes. This is a plain static pass rather than a
/// [gregtech.api.interfaces.IOreRecipeRegistrator] for that reason. See [#stackOf] for how a stack resolves.
public class ProcessingDustGeneration {

    private ProcessingDustGeneration() {}

    /// The frozen set of materials this pass covers.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        // MaterialsElements / MaterialsElements.STANDALONE
        Materials.Selenium, Materials.Bromine, Materials.Krypton,
        Materials.Iodine, Materials.Rhenium, Materials.Thallium,
        Materials.Germanium, Materials.Technetium, Materials.Polonium,
        Materials.Radium, Materials.Protactinium, Materials.Curium,
        Materials.Neptunium, Materials.Fermium, Materials.Lithium7,
        Materials.Uranium232, Materials.Uranium233, Materials.Plutonium238,
        Materials.AdvancedNitinol, Materials.AstralTitanium, Materials.CelestialTungsten,
        Materials.Hypogen, Materials.ChromaticGlass, Materials.BlackMetal,
        Materials.AncientGranite, Materials.Runite, Materials.Dragonblood,
        Materials.Rhugnor, Materials.InfusedAir, Materials.InfusedFire,
        Materials.InfusedEarth, Materials.InfusedWater,
        // MaterialsAlloy
        Materials.SiliconCarbide, Materials.ZirconiumCarbide, Materials.TantalumCarbide,
        Materials.NiobiumCarbide, Materials.TungstenTitaniumCarbide, Materials.EnergyCrystal,
        Materials.BloodSteel, Materials.Zeron100, Materials.Tumbaga,
        Materials.Potin, Materials.Staballoy, Materials.Tantalloy60,
        Materials.Tantalloy61, Materials.Inconel625, Materials.Inconel690,
        Materials.Inconel792, Materials.EglinSteel, Materials.MaragingSteel250,
        Materials.MaragingSteel300, Materials.MaragingSteel350, Materials.WatertightSteel,
        Materials.Nitinol60, Materials.Stellite, Materials.Talonite,
        Materials.HastelloyW, Materials.HastelloyX, Materials.HastelloyC276,
        Materials.HastelloyN, Materials.Incoloy020, Materials.IncoloyDS,
        Materials.IncoloyMA956, Materials.Grisium, Materials.HG1223,
        Materials.TriniumTitaniumAlloy, Materials.TriniumNaquadahAlloy, Materials.TriniumNaquadahCarbonite,
        Materials.ArceusAlloy2B, Materials.HeLiCoPtEr, Materials.LafiumCompound,
        Materials.CinobiteA243, Materials.Pikyonium64B, Materials.AbyssalAlloy,
        Materials.Laurenium, Materials.Botmium, Materials.HS188A,
        Materials.Titansteel, Materials.Arcanite, Materials.Octiron,
        Materials.BabbitAlloy, Materials.BlackTitanium, Materials.Indalloy140,
        Materials.Quantum,
        // MaterialsFluorides
        Materials.AmmoniumBifluoride, Materials.BerylliumHydroxide, Materials.BerylliumFluoride,
        Materials.LithiumFluoride, Materials.ThoriumTetrafluoride, Materials.ThoriumHexafluoride,
        Materials.UraniumTetrafluoride, Materials.UraniumHexafluoride, Materials.ZirconiumTetrafluoride,
        Materials.NeptuniumHexafluoride, Materials.TechnetiumHexafluoride, Materials.SeleniumHexafluoride,
        Materials.SodiumFluoride,
        // MaterialsNuclides
        Materials.LFTRFuel1, Materials.LFTRFuel2, Materials.LFTRFuel3,
        // MaterialMisc / RecipesSeleniumProcessing
        Materials.PotassiumNitrate, Materials.SodiumNitrate, Materials.StrontiumOxide,
        Materials.StrontiumHydroxide, Materials.CyanoaceticAcid, Materials.SodiumCyanide,
        Materials.CopperIISulfate, Materials.CopperIISulfatePentahydrate, Materials.SeleniumDioxide,
        Materials.WoodsGlass);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        ItemStack normalDust = stackOf(OrePrefixes.dust, material, 1L);
        ItemStack smallDust = stackOf(OrePrefixes.dustSmall, material, 1L);
        ItemStack tinyDust = stackOf(OrePrefixes.dustTiny, material, 1L);

        if (tinyDust != null && normalDust != null) {
            GTModHandler.addCraftingRecipe(
                normalDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
            GTModHandler.addCraftingRecipe(
                stackOf(OrePrefixes.dustTiny, material, 9L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "D  ", "   ", "   ", 'D', normalDust });
        }

        if (smallDust != null && normalDust != null) {
            GTModHandler.addCraftingRecipe(
                normalDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
            GTModHandler.addCraftingRecipe(
                stackOf(OrePrefixes.dustSmall, material, 4L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " D ", "   ", "   ", 'D', normalDust });
        }

        if (smallDust != null) {
            generatePackagerRecipes(material);
        }

        ItemStack ingot = stackOf(OrePrefixes.ingot, material, 1L);
        if (normalDust != null && ingot != null) {
            addFurnaceRecipe(material, normalDust, ingot);
        }
    }

    private static void generatePackagerRecipes(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(stackOf(OrePrefixes.dustSmall, material, 4L), ItemList.Schematic_Dust.get(0L))
            .itemOutputs(stackOf(OrePrefixes.dust, material, 1L))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        ItemStack tinyDust = stackOf(OrePrefixes.dustTiny, material, 1L);
        if (tinyDust != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(stackOf(OrePrefixes.dustTiny, material, 9L), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(stackOf(OrePrefixes.dust, material, 1L))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(stackOf(OrePrefixes.dust, material, 1L), ItemList.Schematic_Dust_Small.get(0L))
            .itemOutputs(stackOf(OrePrefixes.dustSmall, material, 4L))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        if (tinyDust != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(stackOf(OrePrefixes.dust, material, 1L), ItemList.Schematic_Dust.get(0L))
                .itemOutputs(stackOf(OrePrefixes.dustTiny, material, 9L))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);
        }
    }

    private static void addFurnaceRecipe(Material material, ItemStack dust, ItemStack ingot) {
        if (MaterialUtils.blastFurnaceRequired(material)) {
            ItemStack hotIngot = stackOf(OrePrefixes.ingotHot, material, 1L);
            if (hotIngot != null) {
                addBlastFurnaceRecipe(material, dust, hotIngot);
            }
        } else {
            GTModHandler.addSmeltingAndAlloySmeltingRecipe(dust, ingot, false);
        }
    }

    private static void addBlastFurnaceRecipe(Material material, ItemStack input, ItemStack output) {
        int tier = MaterialUtils.tier(material);
        int timeTaken = tier <= 4 ? 25 * tier * 10 : 125 * tier * 10;

        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .duration(timeTaken)
            .eut(MaterialUtils.voltageMultiplier(material))
            .metadata(COIL_HEAT, MaterialUtils.meltingPoint(material))
            .addTo(blastFurnaceRecipes);
    }

    /// A material's stack for `prefix`: the MaterialLib-backed one when the material generates that shape,
    /// falling back to the ore dictionary's unification target otherwise. [GTOreDictUnificator#get] alone is
    /// not enough -- a material with no legacy sub-id never enters the unificator's generated-item pass, so
    /// for those it keeps returning a legacy item even after the shape exists. Public: every
    /// gtPlusPlus-originated pass in `gregtech.loaders.oreprocessing` resolves stacks this same way, so they
    /// share this one instead of each declaring their own copy, and outside consumers use it wherever the
    /// oredict fallback matters (notably `ore`, which has no MaterialLib shape to fall back from).
    public static ItemStack stackOf(OrePrefixes prefix, Material material, long amount) {
        ItemStack cutover = MaterialParts.stack(prefix, material, amount);
        return cutover != null ? cutover : GTOreDictUnificator.get(prefix, material, amount);
    }
}
