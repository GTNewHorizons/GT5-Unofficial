package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

import cpw.mods.fml.common.registry.GameRegistry;
import gcewing.architecture.common.item.ArchitectureItemBlock;
import gcewing.architecture.common.shape.Shape;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import team.chisel.carving.Carving;

public class ChiselBackend extends RecipeMapBackend {

    private static final int MAX_CIRCUIT_CONFIGURATION = 24;

    public ChiselBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected boolean doesOverwriteFindRecipe() {
        return true;
    }

    @Nullable
    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        int circuitConfiguration = getCircuitConfiguration(items);

        // Circuit mode: use circuit number to pick variant
        if (circuitConfiguration >= 1) {
            for (ItemStack item : items) {
                if (item == null || GTUtility.isAnyIntegratedCircuit(item) || isArchitectureTarget(item)) continue;
                GTRecipe recipe = generateChiselRecipe(item, circuitConfiguration);
                if (recipe != null) return recipe;
            }
            return null;
        }

        // Target block mode: transform input into the same chisel group as target
        GTRecipe chiselTargetRecipe = generateChiselTargetRecipe(items, specialSlot);
        if (chiselTargetRecipe != null) return chiselTargetRecipe;

        // Architecture mode
        return generateArchitectureRecipe(items, specialSlot);
    }

    @Nullable
    private static GTRecipe generateChiselTargetRecipe(ItemStack[] items, @Nullable ItemStack specialSlot) {
        ItemStack target = findChiselTarget(items, specialSlot);
        if (target == null) return null;

        for (ItemStack input : items) {
            if (input == null || GTUtility.isAnyIntegratedCircuit(input)) continue;
            if (GTUtility.areStacksEqual(input, target, true)) continue; // same item, skip
            GTRecipe recipe = generateChiselToTargetRecipe(input, target);
            if (recipe != null) return recipe;
        }
        return null;
    }

    @Nullable
    private static ItemStack findChiselTarget(ItemStack[] items, @Nullable ItemStack specialSlot) {
        if (isChiselTarget(specialSlot)) return specialSlot;
        for (ItemStack item : items) {
            if (isChiselTarget(item)) return item;
        }
        return null;
    }

    private static boolean isChiselTarget(@Nullable ItemStack stack) {
        if (stack == null) return false;
        return !getItemsForChiseling(stack).isEmpty();
    }

    @Nullable
    private static GTRecipe generateChiselToTargetRecipe(ItemStack input, ItemStack target) {
        List<ItemStack> results = getItemsForChiseling(input);
        for (int i = 0; i < results.size(); i++) {
            if (GTUtility.areStacksEqual(results.get(i), target, true)) {
                return GTRecipeBuilder.builder()
                    .itemInputs(GTUtility.copyAmount(1, input))
                    .itemOutputs(GTUtility.copyAmount(1, target))
                    .duration(20)
                    .eut(16)
                    .specialValue(0)
                    .noBuffer()
                    .build()
                    .orElse(null);
            }
        }
        return null;
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return item != null
            && (GTUtility.isAnyIntegratedCircuit(item) || hasChiselResults(item) || isArchitectureTarget(item));
    }

    @Override
    public Collection<GTRecipe> getAllRecipes() {
        return getNEIRecipes();
    }

    @Override
    public Collection<GTRecipe> getRecipesByCategory(RecipeCategory recipeCategory) {
        return getNEIRecipes();
    }

    @Override
    public Map<RecipeCategory, Collection<GTRecipe>> getRecipeCategoryMap() {
        return Collections.emptyMap();
    }

    private static boolean hasChiselResults(ItemStack from) {
        return !getItemsForChiseling(from).isEmpty();
    }

    private static List<ItemStack> getItemsForChiseling(ItemStack stack) {
        return Carving.chisel.getItemsForChiseling(stack);
    }

    @Nullable
    private static GTRecipe generateArchitectureRecipe(ItemStack[] items, @Nullable ItemStack specialSlot) {
        if (!Mods.ArchitectureCraft.isModLoaded()) return null;

        ItemStack target = findArchitectureTarget(items, specialSlot);
        if (target == null) return null;

        for (ItemStack input : items) {
            if (input == null || GTUtility.isAnyIntegratedCircuit(input) || isArchitectureTarget(input)) continue;
            GTRecipe recipe = generateArchitectureRecipe(input, target);
            if (recipe != null) return recipe;
        }
        return null;
    }

    @Nullable
    private static ItemStack findArchitectureTarget(ItemStack[] items, @Nullable ItemStack specialSlot) {
        if (isArchitectureTarget(specialSlot)) return specialSlot;

        for (ItemStack item : items) {
            if (isArchitectureTarget(item)) return item;
        }
        return null;
    }

    private static boolean isArchitectureTarget(@Nullable ItemStack stack) {
        return Mods.ArchitectureCraft.isModLoaded() && stack != null
            && stack.getItem() instanceof ArchitectureItemBlock;
    }

    @Nullable
    private static GTRecipe generateArchitectureRecipe(ItemStack input, ItemStack target) {
        ItemStack output = getArchitectureOutput(input, target);
        if (output == null || output.getTagCompound() == null) return null;

        Shape shape = Shape.forId(
            output.getTagCompound()
                .getInteger("Shape"));
        if (shape == null) return null;

        return GTRecipeBuilder.builder()
            .itemInputs(GTUtility.copyAmount(shape.materialUsed, input))
            .itemOutputs(GTUtility.copyAmount(shape.itemsProduced, output))
            .outputChances(10000)
            .duration(20)
            .eut(16)
            .specialValue(0)
            .noBuffer()
            .build()
            .orElse(null);
    }

    @Nullable
    private static ItemStack getArchitectureOutput(ItemStack input, ItemStack target) {
        if (!(input.getItem() instanceof ItemBlock) || input.getItem() instanceof ArchitectureItemBlock) return null;

        Block block = Block.getBlockFromItem(input.getItem());
        if (block == null) return null;
        if (block != Blocks.glass && block != Blocks.stained_glass
            && (!block.renderAsNormalBlock() || block.hasTileEntity())) {
            return null;
        }

        NBTTagCompound tag = target.getTagCompound();
        if (tag == null) return null;

        int inputDamage = input.getItemDamage();
        if (inputDamage < 0 || inputDamage > 15) return null;

        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        if (id == null) return null;

        ItemStack output = target.copy();
        NBTTagCompound outputTag = (NBTTagCompound) tag.copy();
        outputTag.setInteger("BaseData", inputDamage);
        outputTag.setInteger("Shape", tag.getInteger("Shape"));
        outputTag.setString("BaseName", id.toString());
        output.setTagCompound(outputTag);
        return output;
    }

    @Nullable
    private static GTRecipe generateChiselRecipe(ItemStack input, int circuitConfiguration) {
        if (input == null || circuitConfiguration < 1 || circuitConfiguration > MAX_CIRCUIT_CONFIGURATION) {
            return null;
        }
        List<ItemStack> results = getItemsForChiseling(input);
        if (circuitConfiguration > results.size()) return null;
        return buildChiselRecipe(input, results.get(circuitConfiguration - 1), circuitConfiguration);
    }

    @Nullable
    private static GTRecipe buildChiselRecipe(ItemStack input, ItemStack output, int circuitConfiguration) {
        if (input == null || output == null) return null;
        return GTRecipeBuilder.builder()
            .itemInputs(GTUtility.copyAmount(1, input))
            .circuit(circuitConfiguration)
            .itemOutputs(GTUtility.copyAmount(1, output))
            .duration(20)
            .eut(16)
            .specialValue(circuitConfiguration)
            .noBuffer()
            .build()
            .orElse(null);
    }

    @Nullable
    private static GTRecipe buildChiselNEIRecipe(ItemStack[] inputAlternatives, ItemStack output,
        int circuitConfiguration) {
        if (inputAlternatives.length == 0 || output == null) return null;
        return GTRecipeBuilder.builder()
            .itemInputs((Object) inputAlternatives)
            .circuit(circuitConfiguration)
            .itemOutputs(GTUtility.copyAmount(1, output))
            .duration(20)
            .eut(16)
            .specialValue(circuitConfiguration)
            .fake()
            .noBuffer()
            .buildWithAlt()
            .orElse(null);
    }

    private static int getCircuitConfiguration(ItemStack[] items) {
        for (ItemStack item : items) {
            if (GTUtility.isAnyIntegratedCircuit(item)) {
                return item.getItemDamage();
            }
        }
        return 0;
    }

    private static List<ItemStack> getVariationStacks(ICarvingGroup group) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ICarvingVariation variation : group.getVariations()) {
            stacks.add(CarvingUtils.getStack(variation));
        }
        String oreName = group.getOreName();
        if (oreName != null) {
            check: for (ItemStack ore : OreDictionary.getOres(oreName)) {
                for (ItemStack stack : stacks) {
                    if (GTUtility.areStacksEqual(stack, ore, true)) continue check;
                }
                stacks.add(ore.copy());
            }
        }
        return stacks;
    }

    private static Collection<GTRecipe> getNEIRecipes() {
        List<GTRecipe> recipes = new ArrayList<>();
        for (String name : Carving.chisel.getSortedGroupNames()) {
            ICarvingGroup group = Carving.chisel.getGroup(name);
            if (group == null || group.getVariations()
                .isEmpty()) {
                continue;
            }

            List<ItemStack> variations = getVariationStacks(group);
            ItemStack[] inputAlternatives = variations.stream()
                .map(stack -> GTUtility.copyAmount(1, stack))
                .toArray(ItemStack[]::new);
            int circuitCount = Math.min(variations.size(), MAX_CIRCUIT_CONFIGURATION);
            for (int circuit = 1; circuit <= circuitCount; circuit++) {
                GTRecipe recipe = buildChiselNEIRecipe(inputAlternatives, variations.get(circuit - 1), circuit);
                if (recipe != null) recipes.add(recipe);
            }
        }
        return recipes;
    }
}
