package gregtech.api.recipe.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.extensions.ArrayExt;

/**
 * Used by machines that are locked to a single recipe, for faster recipe checking.
 * <p>
 * The typical computation times are:
 * <ul>
 * <li>Normal recipe check:
 * <ul>
 * <li>{@link gregtech.api.recipe.FindRecipeQuery#find Find recipe from recipemap}: O(NCR)
 * <ul>
 * <li>N = number of machine inputs</li>
 * <li>C = average amount of recipe candidates for a specific input</li>
 * <li>R = time to execute {@link GTRecipe#isRecipeInputEqual}</li>
 * </ul>
 * </li>
 * <li>{@link GTRecipe#isRecipeInputEqual Check if inputs match recipe}: O(NM)
 * <ul>
 * <li>N = number of machine inputs</li>
 * <li>M = number of recipe inputs</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>{@link #checkRecipeInputs Single recipe check}: O(N + M), where N = number of machine inputs and M = number of
 * recipe inputs.</li>
 * </ul>
 */
public class SingleRecipeCheck {

    @Nonnull
    private final GTRecipe recipe;
    @Nonnull
    private final RecipeMap<?> recipeMap;
    @Nonnull
    private final ImmutableMap<ItemId, Integer> itemCost;
    @Nonnull
    private final ImmutableMap<Fluid, Integer> fluidCost;

    private final int totalItemCost;
    private final int totalFluidCost;

    /**
     * Private constructor.
     *
     * @param recipe    the recipe to check
     * @param recipeMap the recipe map from which the recipe originates
     * @param itemCost  an immutable map of item costs (required item amounts)
     * @param fluidCost an immutable map of fluid costs (required fluid amounts)
     */
    private SingleRecipeCheck(@Nonnull GTRecipe recipe, @Nonnull RecipeMap<?> recipeMap,
        @Nonnull ImmutableMap<ItemId, Integer> itemCost, @Nonnull ImmutableMap<Fluid, Integer> fluidCost) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
        this.itemCost = itemCost;
        this.fluidCost = fluidCost;

        this.totalItemCost = itemCost.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
        this.totalFluidCost = fluidCost.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    /**
     * Returns the recipe associated with this check.
     *
     * @return the GTRecipe instance
     */
    @Nonnull
    public GTRecipe getRecipe() {
        return recipe;
    }

    /**
     * Returns the RecipeMap from which this recipe originates.
     *
     * @return the RecipeMap instance
     */
    @Nonnull
    public RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    /**
     * Checks the available inputs against the recipe cost and returns the number of parallel recipes that can be
     * executed. Returns 0 if the recipe is not satisfied at all.
     * <p>
     * Optionally, the method can consume (subtract) the used inputs.
     *
     * @param consumeInputs if true, subtract the inputs used for the recipe from the provided arrays
     * @param maxParallel   the maximum number of parallel executions to attempt
     * @param itemInputs    an array of available item stacks
     * @param fluidInputs   an array of available fluid stacks
     * @return the number of parallel recipes that can be executed, or 0 if inputs are insufficient
     */
    public int checkRecipeInputs(boolean consumeInputs, int maxParallel, ItemStack[] itemInputs,
        FluidStack[] fluidInputs) {
        int currentParallel = maxParallel;

        if (totalItemCost > 0) {
            // Create a map for items to their stored amounts.
            Map<ItemId, Integer> itemMap = new HashMap<>();
            for (ItemStack itemStack : itemInputs) {
                if (itemStack == null) continue;
                itemMap.merge(ItemId.createNoCopy(itemStack), itemStack.stackSize, Integer::sum);
            }

            // For each item cost, update the maximum parallel executions possible.
            for (Map.Entry<ItemId, Integer> costEntry : itemCost.entrySet()) {
                currentParallel = Math
                    .min(currentParallel, itemMap.getOrDefault(costEntry.getKey(), 0) / costEntry.getValue());
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        if (totalFluidCost > 0) {
            // Create a map for fluids to their stored amounts.
            Map<Fluid, Integer> fluidMap = new HashMap<>();
            for (FluidStack fluidStack : fluidInputs) {
                if (fluidStack == null) continue;
                fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }

            // For each fluid cost, update the maximum parallel executions possible.
            for (Map.Entry<Fluid, Integer> costEntry : fluidCost.entrySet()) {
                currentParallel = Math
                    .min(currentParallel, fluidMap.getOrDefault(costEntry.getKey(), 0) / costEntry.getValue());
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        final int finalParallel = currentParallel;
        if (consumeInputs) {
            if (totalItemCost > 0) {
                int remainingItemCost = totalItemCost * finalParallel;
                Map<ItemId, Integer> runningItemCost = itemCost.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() * finalParallel));

                for (ItemStack itemStack : itemInputs) {
                    if (itemStack == null) continue;
                    ItemId key = ItemId.createNoCopy(itemStack);
                    int runningCost = runningItemCost.getOrDefault(key, 0);
                    int paid = Math.min(itemStack.stackSize, runningCost);
                    itemStack.stackSize -= paid;
                    runningItemCost.put(key, runningCost - paid);

                    remainingItemCost -= paid;
                    // If all item costs are paid, break out of the loop.
                    if (remainingItemCost <= 0) {
                        break;
                    }
                }
            }

            if (totalFluidCost > 0) {
                int remainingFluidCost = totalFluidCost * finalParallel;
                Map<Fluid, Integer> runningFluidCost = fluidCost.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() * finalParallel));

                for (FluidStack fluidStack : fluidInputs) {
                    if (fluidStack == null) continue;
                    Fluid key = fluidStack.getFluid();
                    int runningCost = runningFluidCost.getOrDefault(key, 0);
                    int paid = Math.min(fluidStack.amount, runningCost);
                    fluidStack.amount -= paid;
                    runningFluidCost.put(key, runningCost - paid);

                    remainingFluidCost -= paid;
                    // If all fluid costs are paid, break out of the loop.
                    if (remainingFluidCost <= 0) {
                        break;
                    }
                }
            }
        }

        return finalParallel;
    }

    /**
     * Serializes the recipe check data to an NBTTagCompound.
     * <p>
     * The serialized data includes recipe inputs, outputs, chances, energy usage (EUT), duration, special value, and
     * the computed item/fluid costs.
     *
     * @return a new NBTTagCompound containing the serialized recipe check data
     */
    public NBTTagCompound writeToNBT() {
        // Here we encode recipe input, output and all other important values.
        // At load time we do a recipe check again, so in case the recipe is gone, we can stop tracking.
        // Of course the next step would be auto migrating to new recipe (if any), but given
        // we don't yet have a mean to uniquely name a recipe, this will have to make do.
        // Consider move serialization code to GTRecipe once this has been proven to work
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("recipemap", recipeMap.unlocalizedName);
        if (recipe.mInputs != null) {
            tag.setTag("inputs", writeList(recipe.mInputs, GTUtility::saveItem));
        }
        if (recipe.mOutputs != null) {
            tag.setTag("outputs", writeList(recipe.mOutputs, GTUtility::saveItem));
        }
        if (recipe.mInputChances != null) {
            tag.setIntArray("inputChances", recipe.mInputChances);
        }
        if (recipe.mOutputChances != null) {
            tag.setIntArray("chances", recipe.mOutputChances);
        }
        if (recipe.mFluidInputChances != null) {
            tag.setIntArray("fluidInputChances", recipe.mFluidInputChances);
        }
        if (recipe.mOutputChances != null) {
            tag.setIntArray("fluidOutputChances", recipe.mFluidOutputChances);
        }
        if (recipe.mFluidInputs != null) {
            tag.setTag(
                "fInputs",
                writeList(
                    recipe.mFluidInputs,
                    s -> s == null ? new NBTTagCompound() : s.writeToNBT(new NBTTagCompound())));
        }
        if (recipe.mFluidOutputs != null) {
            tag.setTag(
                "fOutputs",
                writeList(
                    recipe.mFluidOutputs,
                    s -> s == null ? new NBTTagCompound() : s.writeToNBT(new NBTTagCompound())));
        }
        tag.setInteger("eut", recipe.mEUt);
        tag.setInteger("duration", recipe.mDuration);
        tag.setInteger("specialValue", recipe.mSpecialValue);
        tag.setTag("itemCost", writeList(itemCost.entrySet(), e -> {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setTag(
                "id",
                e.getKey()
                    .writeToNBT());
            ret.setInteger("count", e.getValue());
            return ret;
        }));
        tag.setTag("fluidCost", writeList(fluidCost.entrySet(), e -> {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setString(
                "id",
                e.getKey()
                    .getName());
            ret.setInteger("count", e.getValue());
            return ret;
        }));
        return tag;
    }

    /**
     * Writes an array of elements into an NBTTagList using the provided serializer function.
     *
     * @param arr   the array of elements to write
     * @param ser   a function that serializes each element into an NBTBase instance
     * @param <T>   the type of the elements in the array
     * @param <NBT> the type of NBTBase that the elements are serialized to
     * @return an NBTTagList containing the serialized elements
     */
    private static <T, NBT extends NBTBase> NBTTagList writeList(T[] arr, Function<T, NBT> ser) {
        return writeList(Arrays.asList(arr), ser);
    }

    /**
     * Writes a collection of elements into an NBTTagList using the provided serializer function.
     *
     * @param arr   the collection of elements to write
     * @param ser   a function that serializes each element into an NBTBase instance
     * @param <T>   the type of the elements in the collection
     * @param <NBT> the type of NBTBase that the elements are serialized to
     * @return an NBTTagList containing the serialized elements
     */
    private static <T, NBT extends NBTBase> NBTTagList writeList(Collection<T> arr, Function<T, NBT> ser) {
        NBTTagList l = new NBTTagList();
        for (T t : arr) {
            l.appendTag(ser.apply(t));
        }
        return l;
    }

    /**
     * Attempts to load a SingleRecipeCheck instance from the given NBTTagCompound.
     *
     * @param recipeMap the RecipeMap to use as a fallback in case the stored map is not found
     * @param tag       the NBTTagCompound containing the serialized recipe check data
     * @return a SingleRecipeCheck instance if loading is successful, or null if loading fails
     */
    @Nullable
    public static SingleRecipeCheck tryLoad(RecipeMap<?> recipeMap, NBTTagCompound tag) {
        if (tag == null || tag.hasNoTags()) return null;

        RecipeMap<?> mapToUse;
        if (tag.hasKey("recipemap")) {
            String mapName = tag.getString("recipemap");
            RecipeMap<?> foundMap = RecipeMap.ALL_RECIPE_MAPS.get(mapName);
            if (foundMap != null) {
                mapToUse = foundMap;
            } else {
                mapToUse = recipeMap;
            }
        } else {
            mapToUse = recipeMap;
        }
        if (mapToUse == null) {
            return null;
        }

        GTRecipe foundRecipe = tryFindRecipe(mapToUse, tag);
        if (foundRecipe == null) return null;
        return new SingleRecipeCheck(foundRecipe, mapToUse, loadItemCost(tag), loadFluidCost(tag));
    }

    /**
     * Loads the fluid cost map from the provided NBTTagCompound.
     *
     * @param tag the NBTTagCompound containing the fluid cost data
     * @return an immutable map of Fluid to Integer representing the fluid costs
     */
    private static ImmutableMap<Fluid, Integer> loadFluidCost(NBTTagCompound tag) {
        return GTUtility.streamCompounds(tag.getTagList("fluidCost", Constants.NBT.TAG_COMPOUND))
            .collect(
                GTUtility
                    .toImmutableMapSerial(t -> FluidRegistry.getFluid(t.getString("id")), t -> t.getInteger("count")));
    }

    /**
     * Loads the item cost map from the provided NBTTagCompound.
     *
     * @param tag the NBTTagCompound containing the item cost data
     * @return an immutable map of ItemId to Integer representing the item costs
     */
    private static ImmutableMap<ItemId, Integer> loadItemCost(NBTTagCompound tag) {
        return GTUtility.streamCompounds(tag.getTagList("itemCost", Constants.NBT.TAG_COMPOUND))
            .collect(
                GTUtility.toImmutableMapSerial(t -> ItemId.create(t.getCompoundTag("id")), t -> t.getInteger("count")));
    }

    /**
     * Tries to find the corresponding recipe from the recipe map using the data stored in the NBTTagCompound. It checks
     * the inputs, outputs, fluid inputs, fluid outputs, chances, duration, EUT, and special value.
     *
     * @param recipeMap the RecipeMap to search for the recipe
     * @param tag       the NBTTagCompound containing the serialized recipe data
     * @return the matching GTRecipe, or null if no match is found or if the data does not match
     */
    private static GTRecipe tryFindRecipe(@Nonnull RecipeMap<?> recipeMap, NBTTagCompound tag) {
        ItemStack[] inputs = GTUtility.streamCompounds(tag.getTagList("inputs", Constants.NBT.TAG_COMPOUND))
            .map(GTUtility::loadItem)
            .toArray(ItemStack[]::new);
        ItemStack[] outputs = GTUtility.streamCompounds(tag.getTagList("outputs", Constants.NBT.TAG_COMPOUND))
            .map(GTUtility::loadItem)
            .toArray(ItemStack[]::new);
        FluidStack[] fInputs = GTUtility.streamCompounds(tag.getTagList("fInputs", Constants.NBT.TAG_COMPOUND))
            .map(FluidStack::loadFluidStackFromNBT)
            .toArray(FluidStack[]::new);
        FluidStack[] fOutputs = GTUtility.streamCompounds(tag.getTagList("fOutputs", Constants.NBT.TAG_COMPOUND))
            .map(FluidStack::loadFluidStackFromNBT)
            .toArray(FluidStack[]::new);
        int eut = tag.getInteger("eut");
        GTRecipe found = recipeMap.findRecipeQuery()
            .items(inputs)
            .fluids(fInputs)
            .voltage(GTValues.V[GTUtility.getTier(eut)])
            .find();
        // need call to ArrayExt.fixChancesArray for backward compat
        int[] inputChances = ArrayExt
            .fixChancesArray(tag.hasKey("inputChances") ? tag.getIntArray("inputChances") : null, -1);
        int[] outputChances = ArrayExt.fixChancesArray(tag.hasKey("chances") ? tag.getIntArray("chances") : null, -1);
        int[] fluidInputChances = ArrayExt
            .fixChancesArray(tag.hasKey("fluidInputChances") ? tag.getIntArray("fluidInputChances") : null, -1);
        int[] fluidOutputChances = ArrayExt
            .fixChancesArray(tag.hasKey("fluidOutputChances") ? tag.getIntArray("fluidOutputChances") : null, -1);
        if (found == null || !GTUtility.equals(inputs, found.mInputs)
            || !Arrays.equals(fInputs, found.mFluidInputs)
            || !GTUtility.equals(outputs, found.mOutputs)
            || !Arrays.equals(fOutputs, found.mFluidOutputs)
            || !Arrays.equals(inputChances, found.mInputChances)
            || !Arrays.equals(outputChances, found.mOutputChances)
            || !Arrays.equals(fluidInputChances, found.mFluidInputChances)
            || !Arrays.equals(fluidOutputChances, found.mFluidOutputChances)
            || found.mDuration != tag.getInteger("duration")
            || found.mEUt != eut
            || found.mSpecialValue != tag.getInteger("specialValue")) return null;
        return found;
    }

    /**
     * Builds an immutable map of item costs from an array of input ItemStacks.
     *
     * @param inputs an array of ItemStacks representing the inputs
     * @return an immutable map of ItemId to Integer representing the cost per item
     */
    private static ImmutableMap<ItemId, Integer> buildItemMap(ItemStack[] inputs) {
        Map<ItemId, Integer> itemMap = new HashMap<>();
        for (ItemStack itemStack : inputs) {
            if (itemStack == null) continue;
            itemMap.merge(ItemId.create(itemStack), itemStack.stackSize, Integer::sum);
        }
        return ImmutableMap.copyOf(itemMap);
    }

    /**
     * Builds an immutable map of fluid costs from an array of FluidStacks.
     *
     * @param fluids an array of FluidStacks representing the fluids
     * @return an immutable map of Fluid to Integer representing the cost per fluid
     */
    private static ImmutableMap<Fluid, Integer> buildFluidMap(FluidStack[] fluids) {
        Map<Fluid, Integer> fluidMap = new HashMap<>();
        for (FluidStack fluidStack : fluids) {
            if (fluidStack == null) continue;
            fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
        }
        return ImmutableMap.copyOf(fluidMap);
    }

    /**
     * Returns a new Builder instance for constructing a SingleRecipeCheck.
     *
     * @param recipeMap the RecipeMap that will be used in this recipe check
     * @return a new Builder instance
     */
    public static Builder builder(@Nonnull RecipeMap<?> recipeMap) {
        return new Builder(Objects.requireNonNull(recipeMap));
    }

    /**
     * Returns a human-friendly string representing the recipe. The caller can choose whether to include inputs and/or
     * outputs.
     *
     * @param recipe           GT recipe
     * @param includeInputs    if true, include item and fluid inputs.
     * @param includeOutputs   if true, include item and fluid outputs.
     * @param showStackAmounts if true, display the stack amounts for items/fluids; otherwise only their names are
     *                         shown.
     * @param multiLine        if true, format the recipe string over multiple lines; otherwise format it as a single
     *                         line.
     * @return the formatted recipe string.
     */
    public static String getDisplayString(GTRecipe recipe, boolean includeInputs, boolean includeOutputs,
        boolean showStackAmounts, boolean multiLine) {
        StringBuilder sb = new StringBuilder();

        if (multiLine) {
            // Process outputs first if requested.
            if (includeOutputs) {
                boolean hasOutput = false;
                if (recipe.mFluidOutputs != null && recipe.mFluidOutputs.length > 0) {
                    sb.append("Fluid Outputs:\n");
                    for (FluidStack fluid : recipe.mFluidOutputs) {
                        if (fluid == null) continue;
                        sb.append("- ");
                        sb.append(fluid.getLocalizedName());
                        if (showStackAmounts) {
                            sb.append(" (")
                                .append(fluid.amount)
                                .append("L)");
                        }
                        sb.append("\n");
                    }
                    hasOutput = true;
                }
                if (recipe.mOutputs != null && recipe.mOutputs.length > 0) {
                    sb.append("Item Outputs:\n");
                    for (ItemStack item : recipe.mOutputs) {
                        if (item == null) continue;
                        sb.append("- ");
                        sb.append(item.getDisplayName());
                        if (showStackAmounts) {
                            sb.append(" x")
                                .append(item.stackSize);
                        }
                        sb.append("\n");
                    }
                }
                if (hasOutput) {
                    sb.append("\n");
                }
            }

            // Process inputs if requested.
            if (includeInputs) {
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                    sb.append("Fluid Inputs:\n");
                    for (FluidStack fluid : recipe.mFluidInputs) {
                        if (fluid == null) continue;
                        sb.append("- ");
                        sb.append(fluid.getLocalizedName());
                        if (showStackAmounts) {
                            sb.append(" (")
                                .append(fluid.amount)
                                .append("L)");
                        }
                        sb.append("\n");
                    }
                    sb.append("\n");
                }
                if (recipe.mInputs != null && recipe.mInputs.length > 0) {
                    sb.append("Item Inputs:\n");
                    for (ItemStack item : recipe.mInputs) {
                        if (item == null) continue;
                        sb.append("- ");
                        sb.append(item.getDisplayName());
                        if (showStackAmounts) {
                            sb.append(" x")
                                .append(item.stackSize);
                        }
                        sb.append("\n");
                    }
                }
            }
        } else { // Single-line formatting.
            boolean addedSomething = false;
            if (includeInputs) {
                StringBuilder inputSb = new StringBuilder();
                if (recipe.mInputs != null && recipe.mInputs.length > 0) {
                    inputSb.append("Item Inputs: ");
                    inputSb.append(
                        Arrays.stream(recipe.mInputs)
                            .filter(Objects::nonNull)
                            .map(item -> item.getDisplayName() + (showStackAmounts ? " x" + item.stackSize : ""))
                            .collect(Collectors.joining(", ")));
                }
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                    if (inputSb.length() > 0) {
                        inputSb.append(" | ");
                    }
                    inputSb.append("Fluid Inputs: ");
                    inputSb.append(
                        Arrays.stream(recipe.mFluidInputs)
                            .filter(Objects::nonNull)
                            .map(
                                fluid -> fluid.getLocalizedName()
                                    + (showStackAmounts ? " (" + fluid.amount + "L)" : ""))
                            .collect(Collectors.joining(", ")));
                }
                if (inputSb.length() > 0) {
                    sb.append(inputSb);
                    addedSomething = true;
                }
            }
            if (includeOutputs) {
                StringBuilder outputSb = new StringBuilder();
                if (recipe.mOutputs != null && recipe.mOutputs.length > 0) {
                    outputSb.append("Item Outputs: ");
                    outputSb.append(
                        Arrays.stream(recipe.mOutputs)
                            .filter(Objects::nonNull)
                            .map(item -> item.getDisplayName() + (showStackAmounts ? " x" + item.stackSize : ""))
                            .collect(Collectors.joining(", ")));
                }
                if (recipe.mFluidOutputs != null && recipe.mFluidOutputs.length > 0) {
                    if (outputSb.length() > 0) {
                        outputSb.append(" | ");
                    }
                    outputSb.append("Fluid Outputs: ");
                    outputSb.append(
                        Arrays.stream(recipe.mFluidOutputs)
                            .filter(Objects::nonNull)
                            .map(
                                fluid -> fluid.getLocalizedName()
                                    + (showStackAmounts ? " (" + fluid.amount + "L)" : ""))
                            .collect(Collectors.joining(", ")));
                }
                if (outputSb.length() > 0) {
                    if (addedSomething) {
                        sb.append(" => ");
                    }
                    sb.append(outputSb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Builder class for constructing SingleRecipeCheck instances.
     */
    public static class Builder {

        private final RecipeMap<?> recipeMap;

        // Used to compute the amount of inputs consumed by comparing the before and after states.
        private Map<ItemId, Integer> beforeItems;
        private Map<Fluid, Integer> beforeFluids;
        private Map<ItemId, Integer> afterItems;
        private Map<Fluid, Integer> afterFluids;

        private GTRecipe recipe;

        /**
         * Private constructor for Builder.
         *
         * @param recipeMap the RecipeMap to use for the recipe check
         */
        private Builder(@Nonnull RecipeMap<?> recipeMap) {
            this.recipeMap = recipeMap;
        }

        /**
         * Sets the "before" state for the recipe check.
         *
         * @param inputs an array of item stacks before the recipe consumes inputs
         * @param fluids an array of fluid stacks before the recipe consumes inputs
         * @return this Builder instance for chaining
         */
        public Builder setBefore(ItemStack[] inputs, FluidStack[] fluids) {
            beforeItems = buildItemMap(inputs);
            beforeFluids = buildFluidMap(fluids);
            return this;
        }

        /**
         * Sets the "after" state for the recipe check.
         *
         * @param inputs an array of item stacks after the recipe has consumed inputs
         * @param fluids an array of fluid stacks after the recipe has consumed inputs
         * @return this Builder instance for chaining
         */
        public Builder setAfter(ItemStack[] inputs, FluidStack[] fluids) {
            afterItems = buildItemMap(inputs);
            afterFluids = buildFluidMap(fluids);
            return this;
        }

        /**
         * Sets the recipe that will be used for the recipe check.
         *
         * @param recipe the GTRecipe to be checked
         * @return this Builder instance for chaining
         */
        public Builder setRecipe(@Nonnull GTRecipe recipe) {
            this.recipe = recipe;
            return this;
        }

        /**
         * Computes and returns an immutable map of item costs by comparing the before and after item states.
         *
         * @return an immutable map of ItemId to Integer representing the cost per item
         */
        private ImmutableMap<ItemId, Integer> buildItemCost() {
            ImmutableMap.Builder<ItemId, Integer> itemCostBuilder = ImmutableMap.builder();
            for (Map.Entry<ItemId, Integer> entry : beforeItems.entrySet()) {
                int cost = entry.getValue() - afterItems.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    itemCostBuilder.put(entry.getKey(), cost);
                }
            }
            return itemCostBuilder.build();
        }

        /**
         * Computes and returns an immutable map of fluid costs by comparing the before and after fluid states.
         *
         * @return an immutable map of Fluid to Integer representing the cost per fluid
         */
        private ImmutableMap<Fluid, Integer> buildFluidCost() {
            ImmutableMap.Builder<Fluid, Integer> fluidCostBuilder = ImmutableMap.builder();
            for (Map.Entry<Fluid, Integer> entry : beforeFluids.entrySet()) {
                int cost = entry.getValue() - afterFluids.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    fluidCostBuilder.put(entry.getKey(), cost);
                }
            }
            return fluidCostBuilder.build();
        }

        /**
         * Builds and returns a new SingleRecipeCheck instance based on the data provided to the Builder.
         *
         * @return a new SingleRecipeCheck instance
         * @throws IllegalStateException if the recipe has not been set
         */
        public SingleRecipeCheck build() {
            if (recipe == null) {
                throw new IllegalStateException("recipe is not set");
            }
            return new SingleRecipeCheck(recipe, recipeMap, buildItemCost(), buildFluidCost());
        }

    }
}
