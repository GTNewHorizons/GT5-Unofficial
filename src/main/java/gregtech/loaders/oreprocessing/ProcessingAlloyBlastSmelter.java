package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloyBlastSmelterRecipes;

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
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemIntegratedCircuit;

/// Alloy blast smelter recipes that melt a solid material into its molten fluid.
///
/// - [#generateComposites] mixes the component dusts of a material's [MaterialUtils#reducedComposition]. A
/// component without a dust goes in as its fluid.
/// - [#generateSingleDust] melts a material's own dust.
/// - [#generateFromExistingBlastFurnaceRecipes] mirrors registered blast furnace recipes, skipping duplicates of
/// recipes already present. It runs after the other two.
///
/// The per-material recipes output the fluid [MaterialUtils#anyFluid] picks, molten first.
public class ProcessingAlloyBlastSmelter {

    private ProcessingAlloyBlastSmelter() {}

    /// The materials that get a composite recipe.
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

    /// Registers [#COMPOSITE]'s recipes.
    public static void generateComposites() {
        for (Material material : COMPOSITE) {
            List<MaterialRefStack> composites = MaterialUtils.reducedComposition(material);
            if (composites.isEmpty()) continue;
            int totalParts = (int) composites.stream()
                .mapToLong(MaterialRefStack::amount)
                .sum();
            int tier = Math.max(1, MaterialUtils.tier(material));
            int duration = tier <= 4 ? 20 * tier * 10 : 120 * tier * 10;
            composite(material, composites, totalParts, GTValues.VP[tier], duration);
        }
    }

    /// The materials that may get a single-dust recipe. See [#generateSingleDust] for the checks.
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

    /// Registers [#SINGLE_DUST]'s recipes for the materials that declare `BLAST_REQUIRED` and have a fluid.
    public static void generateSingleDust() {
        for (Material material : SINGLE_DUST) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.BLAST_REQUIRED))) continue;
            ItemStack dust = MaterialLibAPI.getStack(material, Shapes.dust, 1);
            FluidStack fluidOutput = MaterialUtils.anyFluid(material, 144);
            if (fluidOutput == null) continue;

            int tier = Math.max(1, MaterialUtils.tier(material));
            int duration = tier <= 4 ? 20 * tier * 10 : 120 * tier * 10;
            int totalParts = (int) MaterialUtils.reducedComposition(material)
                .stream()
                .mapToLong(MaterialRefStack::amount)
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

    private static void composite(Material material, List<MaterialRefStack> composites, int totalParts, long voltage,
        int duration) {
        int count = composites.size();
        ItemStack[] items = new ItemStack[count];
        FluidStack componentFluid = null;
        for (int i = 0; i < count; i++) {
            Material component = composites.get(i)
                .material()
                .resolve();
            int parts = (int) composites.get(i)
                .amount();
            if (component.hasShape(Shapes.dust)) {
                items[i] = MaterialLibAPI.getStack(component, Shapes.dust, parts);
            } else if (parts > 0 && parts <= 100) {
                componentFluid = MaterialUtils.anyFluid(component, parts * 1000L);
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
        builder.fluidOutputs(MaterialUtils.anyFluid(material, 144L * totalParts))
            .eut(ebf ? voltage : voltage / 2)
            .duration(duration)
            .addTo(alloyBlastSmelterRecipes);
    }

    private static Map<String, GTRecipe> ingotMeltingRecipes;
    private static Map<String, String> hotToCold;

    /// Copies each enabled [RecipeMaps#blastFurnaceRecipes] recipe of at most 3600 K into `alloyBlastSmelterRecipes`,
    /// outputting the molten fluid of its ingot and skipping duplicates. The fluid is looked up in
    /// [RecipeMaps#fluidExtractionRecipes], and hot ingots through [RecipeMaps#vacuumFreezerRecipes]. Valid only
    /// after every other mod's recipes are registered.
    public static void generateFromExistingBlastFurnaceRecipes() {
        ingotMeltingRecipes = new HashMap<>();
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
                    ingotMeltingRecipes.put(uniqueId(input), recipe);
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
            GTRecipe meltingRecipe = null;
            if (ArrayUtils.isNotEmpty(outputs) && outputs[0] != null) {
                meltingRecipe = meltingRecipeFromIngot(outputs[0]);
                if (meltingRecipe != null)
                    molten = new FluidStack(meltingRecipe.mFluidOutputs[0], outputs[0].stackSize * 144);
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
                .requiresEnvironment(
                    recipe.getEnvironmentRequirements()
                        .and(meltingRecipe.getEnvironmentRequirements()))
                .recipeCategory(
                    baseItemCount == 1 ? RecipeCategories.absNonAlloyRecipes
                        : alloyBlastSmelterRecipes.getDefaultRecipeCategory());
            if (!circuitFound) builder.circuit(inputs.length);
            builder.addTo(alloyBlastSmelterRecipes);
        }

        ingotMeltingRecipes = null;
        hotToCold = null;
    }

    private static GTRecipe meltingRecipeFromIngot(ItemStack ingot) {
        String id = uniqueId(ingot);
        GTRecipe direct = ingotMeltingRecipes.get(id);
        if (direct != null) return direct;
        String cold = hotToCold.get(id);
        return cold != null ? ingotMeltingRecipes.get(cold) : null;
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

    /// Rounds to the nearest half, then truncates toward zero.
    /// [#generateFromExistingBlastFurnaceRecipes]'s durations are pinned to this non-standard rounding.
    private static int roundToClosestInt(double value) {
        return (int) (Math.round(value * 2) / 2.0);
    }
}
