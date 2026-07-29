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
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Reproduces gtPlusPlus's retired `RecipeGenDustGeneration` in full: crafting-table dust-size conversions, the
/// four packager dust/dustSmall/dustTiny conversions, and the dust -> ingot furnace/alloy-smelter/blast-furnace
/// recipe, for every material in [#ELIGIBLE].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes`, the exact drain point the retired
/// generator's own `run()` used (queued through `MaterialGenerator#mRecipeMapsToGenerate`). The late dispatch
/// is deliberate: several of these materials' stacks only resolve once the rest of gtPlusPlus's postInit has
/// run, so dispatching any earlier -- MaterialLib's own postInit, or a `ShapeConsumerSupport` shape consumer
/// -- silently drops or misresolves recipes. This is a plain static pass rather than a
/// [gregtech.api.interfaces.IOreRecipeRegistrator] for that reason. See [#stackOf] for how a stack resolves.
public class ProcessingDustGeneration {

    private ProcessingDustGeneration() {}

    /// The exact materials the retired `RecipeGenDustGeneration` reached: every material passed to
    /// `MaterialGenerator.generate`, `generateNuclearMaterial`/`generateNuclearDusts`,
    /// `generateOreMaterialWithAllExcessComponents`, or `ItemUtils.generateSpecialUseDusts` (the latter reached
    /// from `MaterialUtils.generateSpecialDustAndAssignToAMaterial` and the direct `ModItems` special-dust
    /// sites).
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        // MaterialsElements / MaterialsElements.STANDALONE
        Materials2Materials.Selenium, Materials2Materials.Bromine, Materials2Materials.Krypton,
        Materials2Materials.Iodine, Materials2Materials.Rhenium, Materials2Materials.Thallium,
        Materials2Materials.Germanium, Materials2Materials.Technetium, Materials2Materials.Polonium,
        Materials2Materials.Radium, Materials2Materials.Protactinium, Materials2Materials.Curium,
        Materials2Materials.Neptunium, Materials2Materials.Fermium, Materials2Materials.Lithium7,
        Materials2Materials.Uranium232, Materials2Materials.Uranium233, Materials2Materials.Plutonium238,
        Materials2Materials.AdvancedNitinol, Materials2Materials.AstralTitanium, Materials2Materials.CelestialTungsten,
        Materials2Materials.Hypogen, Materials2Materials.ChromaticGlass, Materials2Materials.BlackMetal,
        Materials2Materials.AncientGranite, Materials2Materials.Runite, Materials2Materials.Dragonblood,
        Materials2Materials.Rhugnor, Materials2Materials.InfusedAir, Materials2Materials.InfusedFire,
        Materials2Materials.InfusedEarth, Materials2Materials.InfusedWater,
        // MaterialsAlloy
        Materials2Materials.SiliconCarbide, Materials2Materials.ZirconiumCarbide, Materials2Materials.TantalumCarbide,
        Materials2Materials.NiobiumCarbide, Materials2Materials.TungstenTitaniumCarbide, Materials2Materials.EnergyCrystal,
        Materials2Materials.BloodSteel, Materials2Materials.Zeron100, Materials2Materials.Tumbaga,
        Materials2Materials.Potin, Materials2Materials.Staballoy, Materials2Materials.Tantalloy60,
        Materials2Materials.Tantalloy61, Materials2Materials.Inconel625, Materials2Materials.Inconel690,
        Materials2Materials.Inconel792, Materials2Materials.EglinSteel, Materials2Materials.MaragingSteel250,
        Materials2Materials.MaragingSteel300, Materials2Materials.MaragingSteel350, Materials2Materials.WatertightSteel,
        Materials2Materials.Nitinol60, Materials2Materials.Stellite, Materials2Materials.Talonite,
        Materials2Materials.HastelloyW, Materials2Materials.HastelloyX, Materials2Materials.HastelloyC276,
        Materials2Materials.HastelloyN, Materials2Materials.Incoloy020, Materials2Materials.IncoloyDS,
        Materials2Materials.IncoloyMA956, Materials2Materials.Grisium, Materials2Materials.HG1223,
        Materials2Materials.TriniumTitaniumAlloy, Materials2Materials.TriniumNaquadahAlloy, Materials2Materials.TriniumNaquadahCarbonite,
        Materials2Materials.ArceusAlloy2B, Materials2Materials.HeLiCoPtEr, Materials2Materials.LafiumCompound,
        Materials2Materials.CinobiteA243, Materials2Materials.Pikyonium64B, Materials2Materials.AbyssalAlloy,
        Materials2Materials.Laurenium, Materials2Materials.Botmium, Materials2Materials.HS188A,
        Materials2Materials.Titansteel, Materials2Materials.Arcanite, Materials2Materials.Octiron,
        Materials2Materials.BabbitAlloy, Materials2Materials.BlackTitanium, Materials2Materials.Indalloy140,
        Materials2Materials.Quantum,
        // MaterialsFluorides
        Materials2Materials.AmmoniumBifluoride, Materials2Materials.BerylliumHydroxide, Materials2Materials.BerylliumFluoride,
        Materials2Materials.LithiumFluoride, Materials2Materials.ThoriumTetrafluoride, Materials2Materials.ThoriumHexafluoride,
        Materials2Materials.UraniumTetrafluoride, Materials2Materials.UraniumHexafluoride, Materials2Materials.ZirconiumTetrafluoride,
        Materials2Materials.NeptuniumHexafluoride, Materials2Materials.TechnetiumHexafluoride, Materials2Materials.SeleniumHexafluoride,
        Materials2Materials.SodiumFluoride,
        // MaterialsNuclides
        Materials2Materials.LFTRFuel1, Materials2Materials.LFTRFuel2, Materials2Materials.LFTRFuel3,
        // MaterialMisc / RecipesSeleniumProcessing
        Materials2Materials.PotassiumNitrate, Materials2Materials.SodiumNitrate, Materials2Materials.StrontiumOxide,
        Materials2Materials.StrontiumHydroxide, Materials2Materials.CyanoaceticAcid, Materials2Materials.SodiumCyanide,
        Materials2Materials.CopperIISulfate, Materials2Materials.CopperIISulfatePentahydrate, Materials2Materials.SeleniumDioxide,
        Materials2Materials.WoodsGlass);
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
    /// `gregtech.loaders.oreprocessing` pass ported from a retired gtPlusPlus generator resolves stacks this
    /// same way, so they share this one instead of each declaring their own copy, and outside consumers use it
    /// wherever the oredict fallback matters (e.g. `ore`, whose retired `Material#getOre` never consulted a
    /// MaterialLib shape at all).
    public static ItemStack stackOf(OrePrefixes prefix, Material material, long amount) {
        ItemStack cutover = MU.stack(prefix, material, amount);
        return cutover != null ? cutover : GTOreDictUnificator.get(prefix, material, amount);
    }
}
