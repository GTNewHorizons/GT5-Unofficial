package gregtech.api.recipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Unmodifiable;

import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Manages list of recipes. Its functionalities are split
 * between {@link RecipeMapBackend} and {@link RecipeMapFrontend}.
 *
 * @param <B> Type of {@link RecipeMapBackend}
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class RecipeMap<B extends RecipeMapBackend> implements IRecipeMap {

    /**
     * All the recipemap instances. key=unlocalized name, value=instance.
     */
    public static final Map<String, RecipeMap<?>> ALL_RECIPE_MAPS = new HashMap<>();

    private final B backend;
    private final RecipeMapFrontend frontend;

    /**
     * Unique unlocalized name of this recipemap. Used for identifier, localization key for NEI tab name, etc.
     */
    public final String unlocalizedName;

    private final RecipeCategory defaultRecipeCategory;

    /**
     * Use {@link RecipeMapBuilder} to instantiate.
     */
    RecipeMap(String unlocalizedName, B backend, RecipeMapFrontend frontend) {
        this.unlocalizedName = unlocalizedName;
        this.backend = backend;
        this.frontend = frontend;
        this.defaultRecipeCategory = new RecipeCategory(this);
        backend.setRecipeMap(this);
        if (ALL_RECIPE_MAPS.containsKey(unlocalizedName)) {
            throw new IllegalArgumentException(
                "Cannot register recipemap with duplicated unlocalized name: " + unlocalizedName);
        }
        ALL_RECIPE_MAPS.put(unlocalizedName, this);
    }

    public B getBackend() {
        return backend;
    }

    public RecipeMapFrontend getFrontend() {
        return frontend;
    }

    /**
     * @return All the recipes belonging to this recipemap.
     */
    @Unmodifiable
    public Collection<GT_Recipe> getAllRecipes() {
        return backend.getAllRecipes();
    }

    /**
     * @return List of registered recipe categories associated with this recipemap.
     */
    public List<RecipeCategory> getAssociatedCategories() {
        return RecipeCategory.ALL_RECIPE_CATEGORIES.values()
            .stream()
            .filter(category -> category.recipeMap == this)
            .collect(Collectors.toList());
    }

    public RecipeCategory getDefaultRecipeCategory() {
        return defaultRecipeCategory;
    }

    /**
     * @return Amperage of this recipemap. Note that recipes store EU/t with amperage included,
     *         e.g. Arc Furnace recipe with 90 EU/t means 30 EU/t (LV) with 3 amperage.
     */
    public int getAmperage() {
        return frontend.getUIProperties().amperage;
    }

    @Override
    public void addDownstream(IRecipeMap downstream) {
        backend.addDownstream(downstream);
    }

    // region add recipe

    @Nullable
    public GT_Recipe addRecipe(boolean aOptimize, @Nullable ItemStack[] aInputs, @Nullable ItemStack[] aOutputs,
        @Nullable Object aSpecial, @Nullable int[] aOutputChances, @Nullable FluidStack[] aFluidInputs,
        @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    @Nullable
    public GT_Recipe addRecipe(@Nullable int[] aOutputChances, @Nullable FluidStack[] aFluidInputs,
        @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                false,
                null,
                null,
                null,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            false,
            false,
            false);
    }

    @Nullable
    public GT_Recipe addRecipe(boolean aOptimize, @Nullable ItemStack[] aInputs, @Nullable ItemStack[] aOutputs,
        @Nullable Object aSpecial, @Nullable FluidStack[] aFluidInputs, @Nullable FluidStack[] aFluidOutputs,
        int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    @Nullable
    public GT_Recipe addRecipe(GT_Recipe aRecipe) {
        return addRecipe(aRecipe, true, false, false);
    }

    @Nullable
    public GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe, boolean aHidden) {
        aRecipe.mHidden = aHidden;
        aRecipe.mFakeRecipe = aFakeRecipe;
        if (aRecipe.mFluidInputs.length < backend.properties.minFluidInputs
            && aRecipe.mInputs.length < backend.properties.minItemInputs) return null;
        if (aCheckForCollisions && backend.checkCollision(aRecipe)) return null;
        return backend.compileRecipe(aRecipe);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe won't find fake Recipes, containsInput WILL find fake Recipes
     */
    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, @Nullable ItemStack[] aInputs,
        @Nullable ItemStack[] aOutputs, @Nullable Object aSpecial, @Nullable int[] aOutputChances,
        @Nullable FluidStack[] aFluidInputs, @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe won't find fake Recipes, containsInput WILL find fake Recipes
     */
    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, @Nullable ItemStack[] aInputs,
        @Nullable ItemStack[] aOutputs, @Nullable Object aSpecial, @Nullable FluidStack[] aFluidInputs,
        @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, @Nullable ItemStack[] aInputs,
        @Nullable ItemStack[] aOutputs, @Nullable Object aSpecial, @Nullable FluidStack[] aFluidInputs,
        @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue, boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            hidden);
    }

    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, @Nullable ItemStack[] aInputs,
        @Nullable ItemStack[] aOutputs, @Nullable Object aSpecial, @Nullable FluidStack[] aFluidInputs,
        @Nullable FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue, ItemStack[][] aAlt,
        boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe.GT_Recipe_WithAlt(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue,
                aAlt),
            hidden);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe won't find fake Recipes, containsInput WILL find fake Recipes
     */
    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
        return addRecipe(aRecipe, aCheckForCollisions, true, false);
    }

    @Nullable
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe, boolean hidden) {
        return addRecipe(aRecipe, aCheckForCollisions, true, hidden);
    }

    @Nonnull
    @Override
    public Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder) {
        return backend.doAdd(builder);
    }

    public GT_Recipe add(GT_Recipe aRecipe) {
        return backend.compileRecipe(aRecipe);
    }

    // endregion

    /**
     * @return if this Item is a valid Input for any for the Recipes
     */
    public boolean containsInput(@Nullable ItemStack aStack) {
        return aStack != null && backend.containsInput(aStack);
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(@Nullable FluidStack aFluid) {
        return aFluid != null && containsInput(aFluid.getFluid());
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(@Nullable Fluid aFluid) {
        return aFluid != null && backend.containsInput(aFluid);
    }

    // region find recipe

    @Nullable
    public GT_Recipe findRecipe(@Nullable IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated, long aVoltage,
        @Nullable FluidStack[] aFluids, @Nullable ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public GT_Recipe findRecipe(@Nullable IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, @Nullable FluidStack[] aFluids, @Nullable ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public GT_Recipe findRecipe(@Nullable IHasWorldObjectAndCoords aTileEntity, @Nullable GT_Recipe aRecipe,
        boolean aNotUnificated, long aVoltage, @Nullable FluidStack[] aFluids, @Nullable ItemStack aSpecialSlot,
        @Nullable ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, false, aVoltage, aFluids, aSpecialSlot, aInputs);
    }

    @Nullable
    public GT_Recipe findRecipe(@Nullable IHasWorldObjectAndCoords aTileEntity, @Nullable GT_Recipe aRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, @Nullable FluidStack[] aFluids,
        @Nullable ItemStack aSpecialSlot, @Nullable ItemStack... aInputs) {
        return findRecipeFirst(
            aInputs != null ? aInputs : new ItemStack[0],
            aFluids != null ? aFluids : new FluidStack[0],
            aSpecialSlot,
            aVoltage,
            aRecipe,
            aNotUnificated,
            aDontCheckStackSizes);
    }

    /**
     * Finds the first matched recipe from given requirements, or null if not found.
     *
     * @param items               Item inputs.
     * @param fluids              Fluid inputs.
     * @param specialSlot         Content of the special slot. Normal recipemaps don't need this, but some do.
     *                            Set {@link RecipeMapBuilder#specialSlotSensitive} to make it actually functional.
     * @param voltage             Recipes that exceed this voltage won't match. It will be automatically
     *                            multiplied by amperage of this recipemap.
     * @param cachedRecipe        If this is not null, this method tests it before all other recipes.
     * @param notUnificated       If this is set to true, item inputs will be unificated.
     * @param dontCheckStackSizes If this is set to false, this method won't check item count and fluid amount
     *                            for the matched recipe.
     * @return Result of the recipe search
     */
    @Nullable
    public GT_Recipe findRecipeFirst(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        long voltage, @Nullable GT_Recipe cachedRecipe, boolean notUnificated, boolean dontCheckStackSizes) {
        return backend.findRecipe(
            items,
            fluids,
            specialSlot,
            recipe -> voltage * getAmperage() >= recipe.mEUt,
            cachedRecipe,
            notUnificated,
            dontCheckStackSizes);
    }

    /**
     * Returns all the matched recipes in the form of Stream, without any additional check for matches.
     *
     * @param items               Item inputs.
     * @param fluids              Fluid inputs.
     * @param specialSlot         Content of the special slot. Normal recipemaps don't need this, but some do.
     *                            Set {@link RecipeMapBuilder#specialSlotSensitive} to make it actually functional.
     * @param cachedRecipe        If this is not null, this method tests it before all other recipes.
     * @param notUnificated       If this is set to true, item inputs will be unificated.
     * @param dontCheckStackSizes If this is set to false, this method won't check item count and fluid amount
     *                            for the matched recipe.
     * @return Stream of matches recipes.
     */
    public Stream<GT_Recipe> findRecipeMatches(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GT_Recipe cachedRecipe, boolean notUnificated, boolean dontCheckStackSizes) {
        return backend.matchRecipeStream(items, fluids, specialSlot, cachedRecipe, notUnificated, dontCheckStackSizes);
    }

    // endregion

    @Override
    public String toString() {
        return "RecipeMap{" + "unlocalizedName='"
            + unlocalizedName
            + '\''
            + ", ownerMod="
            + defaultRecipeCategory.ownerMod.getModId()
            + '}';
    }

    private static final Pattern LEGACY_IDENTIFIER_PATTERN = Pattern.compile("(.+)_[0-9]+_[0-9]+_[0-9]+_[0-9]+_[0-9]+");

    /**
     * Gets recipemap instance from old mUniqueIdentifier format. This is only for backward compat, where tiles
     * saved recipemap with mUniqueIdentifier.
     *
     * @param legacyIdentifier mUniqueIdentifier, in %s_%d_%d_%d_%d_%d format
     * @return Found recipemap, can be null
     */
    @Nullable
    public static RecipeMap<?> getFromOldIdentifier(String legacyIdentifier) {
        Matcher matcher = LEGACY_IDENTIFIER_PATTERN.matcher(legacyIdentifier);
        if (!matcher.find()) {
            // It can be new format
            return ALL_RECIPE_MAPS.get(legacyIdentifier);
        }
        return ALL_RECIPE_MAPS.get(matcher.group(1));
    }
}
