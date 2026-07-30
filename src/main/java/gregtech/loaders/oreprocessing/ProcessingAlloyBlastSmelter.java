package gregtech.loaders.oreprocessing;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.materials.LegacyGTPPComposites;
import gregtech.api.enums.materials.LegacyGTPPComposites.Component;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemIntegratedCircuit;

/// Alloy blast smelter recipes that turn a solid material into its molten fluid, ported from the retired
/// gtPlusPlus `RecipeGenBlastSmelter`/`RecipeGenBlastSmelterGTNH`.
///
/// - [#generateComposites] reproduces a [LegacyGTPPComposites] entry's component dusts (or, for a
/// non-solid component, its fluid) combining into the output's molten fluid, mirroring the ratio the retired
/// mixer-recipe generator also read from the same `Material#getComposites`/`vSmallestRatio` fields -- see
/// [LegacyGTPPComposites]'s class javadoc for how [#COMPOSITE]'s eligibility differs from the mixer's own.
/// - [#generateSingleDust] reproduces a material's own dust blasting directly into its own molten fluid, the
/// retired generator's other recipe shape (`if (Material#requiresBlastFurnace())`, unconditional on composite
/// status).
///
/// Both shapes need a material's molten fluid, which [MaterialParts]'s state-specific accessors
/// (`molten`/`gas`/`fluid`)
/// cannot resolve for a gtPlusPlus-only material: those read a per-state
/// [MaterialFluidNames]
/// slot or a [com.ruling_0.materiallib.api.Shape], and a gtPlusPlus-only material carries neither.
/// [#materialFluid] resolves it instead through [MaterialUtils#legacyGtppFluid]'s name-priority lookup over the same
/// [MaterialFluidNames] data, exactly as gtpp's own `Material#getFluidStack` did (a
/// null result
/// here reproduces a null there, which is why some [#SINGLE_DUST] members -- carrying `BLAST_REQUIRED` but no
/// fluid gtpp ever wired up -- correctly register nothing rather than needing a hand-picked exclusion list).
///
/// [#generateFromExistingBlastFurnaceRecipes] is a separate, non-per-material pass: it mirrors every
/// already-registered blast furnace recipe that resolves a molten fluid, deduping against a snapshot of
/// `alloyBlastSmelterRecipes` taken before its own loop runs (matching the legacy generator's own dedup, which
/// is why [#generateComposites] and [#generateSingleDust] must run first -- see their call site in
/// `GTplusplus`).
public class ProcessingAlloyBlastSmelter {

    private ProcessingAlloyBlastSmelter() {}

    /// The exact materials the retired `RecipeGenBlastSmelter` registered a composite recipe for -- every
    /// material reachable through `MaterialGenerator.generate`'s `generateBlastSmelterRecipes` parameter left
    /// enabled (or `generateOreMaterialWithAllExcessComponents`, which does not take that parameter) whose
    /// `Material#getComposites` held more than one part. See [LegacyGTPPComposites]'s class javadoc for how
    /// this differs from the mixer's own eligibility over the same table.
    // spotless:off
    private static final Set<Material> COMPOSITE = Set.of(
        Materials.AbyssalAlloy, Materials.AncientGranite, Materials.Arcanite,
        Materials.ArceusAlloy2B, Materials.BabbitAlloy, Materials.BlackMetal,
        Materials.BlackTitanium, Materials.CinobiteA243, Materials.EglinSteel,
        Materials.EnergyCrystal, Materials.Grisium, Materials.HS188A,
        Materials.HastelloyC276, Materials.HastelloyN, Materials.HastelloyW,
        Materials.HastelloyX, Materials.HeLiCoPtEr, Materials.Incoloy020,
        Materials.IncoloyDS, Materials.IncoloyMA956, Materials.Inconel625,
        Materials.Inconel690, Materials.Inconel792, Materials.LafiumCompound,
        Materials.Laurenium, Materials.MaragingSteel250, Materials.MaragingSteel300,
        Materials.MaragingSteel350, Materials.NiobiumCarbide, Materials.Octiron,
        Materials.Pikyonium64B, Materials.Potin, Materials.Quantum,
        Materials.SiliconCarbide, Materials.Staballoy, Materials.Stellite,
        Materials.Talonite, Materials.Tantalloy60, Materials.Tantalloy61,
        Materials.TantalumCarbide, Materials.Titansteel, Materials.TriniumNaquadahAlloy,
        Materials.TriniumNaquadahCarbonite, Materials.TriniumTitaniumAlloy, Materials.Tumbaga,
        Materials.TungstenTitaniumCarbide, Materials.WatertightSteel, Materials.Zeron100,
        Materials.ZirconiumCarbide);
    // spotless:on

    /// Registers [#COMPOSITE]'s recipes. Call once, before [#generateFromExistingBlastFurnaceRecipes] (whose
    /// dedup check must see these).
    public static void generateComposites() {
        for (Material material : COMPOSITE) {
            List<Component> composites = LegacyGTPPComposites.composites(material);
            if (composites.isEmpty()) continue;
            int totalParts = composites.stream()
                .mapToInt(Component::parts)
                .sum();
            int tier = Math.max(1, MaterialUtils.tier(material));
            int duration = tier <= 4 ? 20 * tier * 10 : 120 * tier * 10;
            composite(material, composites, totalParts, GTValues.VP[tier], duration);
        }
    }

    /// The exact materials the retired `RecipeGenBlastSmelter` reached with `generateBlastSmelterRecipes` left
    /// enabled through `MaterialGenerator.generate` (or via `generateOreMaterialWithAllExcessComponents`, which
    /// does not take that parameter), minus the generator's own material blacklist (`ThoriumHexafluoride`,
    /// `ThoriumTetrafluoride`, `BloodSteel`, `LiFBeF2ThF4UF4`, `LiFBeF2ZrF4UF4`, `LiFBeF2ZrF4U235`, `Nitinol60` --
    /// none but `BloodSteel`/`Nitinol60` reach this dispatch anyway). Membership here does not by itself mean a
    /// recipe gets registered -- see [#generateSingleDust]'s runtime gate and [#materialFluid]'s javadoc.
    // spotless:off
    private static final Set<Material> SINGLE_DUST = Set.of(
        Materials.AbyssalAlloy, Materials.AdvancedNitinol, Materials.Arcanite,
        Materials.ArceusAlloy2B, Materials.AstralTitanium, Materials.BabbitAlloy,
        Materials.BlackTitanium, Materials.CelestialTungsten, Materials.CinobiteA243,
        Materials.Dragonblood, Materials.EnergyCrystal, Materials.Grisium,
        Materials.HS188A, Materials.HastelloyC276, Materials.HastelloyN,
        Materials.HastelloyW, Materials.HastelloyX, Materials.HeLiCoPtEr,
        Materials.Hypogen, Materials.Incoloy020, Materials.IncoloyDS,
        Materials.IncoloyMA956, Materials.Inconel625, Materials.Inconel690,
        Materials.Inconel792, Materials.LafiumCompound, Materials.Laurenium,
        Materials.MaragingSteel250, Materials.MaragingSteel300, Materials.MaragingSteel350,
        Materials.NiobiumCarbide, Materials.Octiron, Materials.Pikyonium64B,
        Materials.Quantum, Materials.Runite, Materials.Staballoy,
        Materials.Stellite, Materials.Talonite, Materials.Tantalloy60,
        Materials.Tantalloy61, Materials.TantalumCarbide, Materials.Titansteel,
        Materials.TriniumNaquadahCarbonite, Materials.TriniumTitaniumAlloy,
        Materials.TungstenTitaniumCarbide, Materials.WatertightSteel, Materials.Zeron100);
    // spotless:on

    /// Registers [#SINGLE_DUST]'s recipes, each gated at runtime on `BLAST_REQUIRED` and on [#materialFluid]
    /// resolving its molten fluid -- either failing means gtpp itself never registered this recipe either, so
    /// the material is silently skipped rather than excluded up front. Call once, at the same point as
    /// [#generateComposites] and before [#generateFromExistingBlastFurnaceRecipes].
    public static void generateSingleDust() {
        for (Material material : SINGLE_DUST) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.BLAST_REQUIRED))) continue;
            ItemStack dust = MaterialLibAPI.getStack(material, Shapes.dust, 1);
            FluidStack fluidOutput = MaterialUtils.legacyGtppFluid(material, 144);
            if (fluidOutput == null) continue;

            int tier = Math.max(1, MaterialUtils.tier(material));
            int duration = tier <= 4 ? 20 * tier * 10 : 120 * tier * 10;
            int totalParts = LegacyGTPPComposites.composites(material)
                .stream()
                .mapToInt(Component::parts)
                .sum();
            GTValues.RA.stdBuilder()
                .itemInputs(dust)
                .circuit(1)
                .fluidOutputs(fluidOutput)
                .duration(duration / Math.max(1, totalParts))
                .eut(GTValues.VP[tier])
                .recipeCategory(RecipeCategories.absNonAlloyRecipes)
                .addTo(alloyBlastSmelterRecipes);
        }
    }

    private static void composite(Material material, List<Component> composites, int totalParts, long voltage,
        int duration) {
        int count = composites.size();
        ItemStack[] items = new ItemStack[count];
        FluidStack componentFluid = null;
        for (int i = 0; i < count; i++) {
            Component component = composites.get(i);
            int parts = component.parts();
            boolean solid = "SOLID".equals(
                component.material()
                    .getProperty(GTMaterialProperties.GTPP_STATE))
                && component.material()
                    .hasShape(Shapes.dust);
            if (solid) {
                items[i] = MaterialLibAPI.getStack(component.material(), Shapes.dust, parts);
            } else if (parts > 0 && parts <= 100) {
                componentFluid = MaterialUtils.legacyGtppFluid(component.material(), parts * 1000L);
            }
        }

        List<ItemStack> inputs = new ArrayList<>(count);
        for (ItemStack item : items) {
            if (item != null) inputs.add(item);
        }

        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(inputs.toArray(new ItemStack[0]));
        if (count < 9) builder.circuit(count);
        if (componentFluid != null) builder.fluidInputs(componentFluid);
        boolean ebf = Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.BLAST_REQUIRED));
        builder.fluidOutputs(MaterialUtils.legacyGtppFluid(material, 144L * totalParts))
            .eut(ebf ? voltage : voltage / 2)
            .duration(duration)
            .addTo(alloyBlastSmelterRecipes);
    }

    /// A material's single registered fluid -- see [MaterialUtils#legacyGtppFluid].

    private static Map<String, FluidStack> ingotToFluid;
    private static Map<String, String> hotToCold;

    /// Mirrors every already-registered [RecipeMaps#blastFurnaceRecipes] recipe (GregTech's own or any other
    /// mod's) whose output resolves a molten fluid -- through [RecipeMaps#fluidExtractionRecipes]'s
    /// ingot-to-fluid recipes directly, or through [RecipeMaps#vacuumFreezerRecipes]'s hot-to-cold-ingot
    /// recipes chained into the same ingot-to-fluid map -- into `alloyBlastSmelterRecipes`, skipping any
    /// blast-furnace recipe hotter than 3600K and any duplicate of a recipe already present (from
    /// [#generateComposites]/[#generateSingleDust] or gtPlusPlus's hand-written loaders). Call once, after
    /// every other mod's recipes are registered, and after [#generateComposites]/[#generateSingleDust] --
    /// their recipes must already be in `alloyBlastSmelterRecipes` for this method's dedup snapshot to see.
    public static void generateFromExistingBlastFurnaceRecipes() {
        ingotToFluid = new HashMap<>();
        hotToCold = new HashMap<>();

        for (GTRecipe recipe : RecipeMaps.fluidExtractionRecipes.getAllRecipes()) {
            if (ArrayUtils.isEmpty(recipe.mInputs) || ArrayUtils.isEmpty(recipe.mFluidOutputs)) continue;
            ItemStack input = recipe.mInputs[0];
            for (int tag : OreDictionary.getOreIDs(input)) {
                String oreName = OreDictionary.getOreName(tag)
                    .toLowerCase();
                if (oreName.startsWith("ingot") && !oreName.contains("double")
                    && !oreName.contains("triple")
                    && !oreName.contains("quad")
                    && !oreName.contains("quintuple")) {
                    ingotToFluid.put(uniqueId(input), recipe.mFluidOutputs[0]);
                }
            }
        }

        for (GTRecipe recipe : RecipeMaps.vacuumFreezerRecipes.getAllRecipes()) {
            if (ArrayUtils.isNotEmpty(recipe.mInputs) && recipe.mInputs[0] != null
                && ArrayUtils.isNotEmpty(recipe.mOutputs)
                && recipe.mOutputs[0] != null) {
                hotToCold.put(uniqueId(recipe.mInputs[0]), uniqueId(recipe.mOutputs[0]));
            }
        }

        GTRecipe[] existing = alloyBlastSmelterRecipes.getAllRecipes()
            .stream()
            .filter(r -> r.mOutputs.length == 0 && r.mFluidOutputs.length == 1)
            .toArray(GTRecipe[]::new);

        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            if (recipe.mSpecialValue > 3600 || !recipe.mEnabled) continue;

            ItemStack[] outputs = recipe.mOutputs.clone();
            FluidStack molten = null;
            if (ArrayUtils.isNotEmpty(outputs) && outputs[0] != null) {
                FluidStack fluid = fluidFromIngot(outputs[0]);
                if (fluid != null) molten = new FluidStack(fluid, outputs[0].stackSize * 144);
            }
            ItemStack[] inputs = recipe.mInputs.clone();
            FluidStack[] fluidInputs = recipe.mFluidInputs.clone();
            if (inputs.length == 0 || outputs.length == 0 || molten == null) continue;

            boolean circuitFound = false;
            for (ItemStack item : inputs) {
                if (item != null && item.getItem() instanceof ItemIntegratedCircuit) {
                    circuitFound = true;
                    break;
                }
            }
            int baseItemCount = circuitFound ? inputs.length - 1 : inputs.length;

            FluidStack moltenOutput = molten;
            boolean duplicate = false;
            for (GTRecipe candidate : existing) {
                if (itemStacksMatch(candidate.mInputs, inputs) && fluidStacksMatch(candidate.mFluidInputs, fluidInputs)
                    && GTUtility.areFluidsEqual(candidate.mFluidOutputs[0], moltenOutput)) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) continue;

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .fluidInputs(fluidInputs)
                .fluidOutputs(molten)
                .duration(roundToClosestInt(recipe.mDuration * 0.8))
                .eut(recipe.mEUt)
                .recipeCategory(
                    baseItemCount == 1 ? RecipeCategories.absNonAlloyRecipes
                        : alloyBlastSmelterRecipes.getDefaultRecipeCategory());
            if (!circuitFound) builder.circuit(inputs.length);
            builder.addTo(alloyBlastSmelterRecipes);
        }

        ingotToFluid = null;
        hotToCold = null;
    }

    private static FluidStack fluidFromIngot(ItemStack ingot) {
        String id = uniqueId(ingot);
        FluidStack direct = ingotToFluid.get(id);
        if (direct != null) return direct;
        String cold = hotToCold.get(id);
        return cold != null ? ingotToFluid.get(cold) : null;
    }

    private static boolean itemStacksMatch(ItemStack[] a, ItemStack[] b) {
        List<ItemStack> left = withoutCircuits(a);
        List<ItemStack> right = withoutCircuits(b);
        if (left.size() != right.size()) return false;
        for (int i = 0; i < left.size(); i++) {
            if (!GTUtility.areStacksEqual(left.get(i), right.get(i))) return false;
        }
        return true;
    }

    private static List<ItemStack> withoutCircuits(ItemStack[] stacks) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && !(stack.getItem() instanceof ItemIntegratedCircuit)) result.add(stack);
        }
        return result;
    }

    private static boolean fluidStacksMatch(FluidStack[] a, FluidStack[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!GTUtility.areFluidsEqual(a[i], b[i])) return false;
        }
        return true;
    }

    private static String uniqueId(ItemStack stack) {
        Item item = stack.getItem();
        return Item.getIdFromItem(item) + "#" + stack.getItemDamage() + "#" + stack.stackSize;
    }

    /// Rounds to the nearest half, then truncates toward zero -- the retired `MathUtils#roundToClosestInt`'s
    /// exact (non-standard) rounding, reproduced here because [#generateFromExistingBlastFurnaceRecipes]'s
    /// duration must match it bit-for-bit.
    private static int roundToClosestInt(double value) {
        return (int) (Math.round(value * 2) / 2.0);
    }
}
