package gregtech.api.util;

import static gregtech.api.util.GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES;
import static gregtech.api.util.GT_Utility.copyFluidArray;
import static gregtech.api.util.GT_Utility.copyItemArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Contract;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.recipe.metadata.RecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class GT_RecipeBuilder {

    // debug mode expose problems. panic mode help you check nothing is wrong-ish without you actively monitoring
    private static final boolean DEBUG_MODE_NULL;
    // Any stable release should be tested at least once with this: -Dgt.recipebuilder.panic.null=true
    private static boolean PANIC_MODE_NULL;
    private static final boolean DEBUG_MODE_INVALID;
    private static final boolean DEBUG_MODE_FULL_ENERGY;
    // Any stable release should be tested at least once with this: -Dgt.recipebuilder.panic.invalid=true
    private static final boolean PANIC_MODE_INVALID;
    private static final boolean DEBUG_MODE_COLLISION;

    // Any stable release should be tested at least once with this: -Dgt.recipebuilder.panic.collision=true
    private static final boolean PANIC_MODE_COLLISION;

    // This should only be enabled in non stable instances only with -Dgt.recipebuilder.recipe_collision_check=true
    public static final boolean ENABLE_COLLISION_CHECK;

    public static final int WILDCARD = 32767;

    // time units
    public static final int HOURS = 20 * 60 * 60;
    public static final int MINUTES = 20 * 60;
    public static final int SECONDS = 20;
    public static final int TICKS = 1;

    // fluid units
    public static final int INGOTS = 144;
    public static final int HALF_INGOT = 72;
    public static final int QUARTER_INGOT = 36;
    public static final int EIGHTH_INGOT = 18;
    public static final int NUGGETS = 16;
    public static final int BUCKETS = 1000;

    static {
        final boolean debugAll;
        if (System.getProperties()
            .containsKey("gt.recipebuilder.debug")) {
            debugAll = Boolean.getBoolean("gt.recipebuilder.debug");
        } else {
            // turn on debug by default in dev mode
            debugAll = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        }
        DEBUG_MODE_NULL = debugAll || Boolean.getBoolean("gt.recipebuilder.debug.null");
        DEBUG_MODE_INVALID = debugAll || Boolean.getBoolean("gt.recipebuilder.debug.invalid");
        DEBUG_MODE_COLLISION = debugAll || Boolean.getBoolean("gt.recipebuilder.debug.collision");
        DEBUG_MODE_FULL_ENERGY = debugAll || Boolean.getBoolean("gt.recipebuilder.debug.fullenergy");

        final boolean panicAll = Boolean.getBoolean("gt.recipebuilder.panic");
        PANIC_MODE_NULL = panicAll || Boolean.getBoolean("gt.recipebuilder.panic.null");
        PANIC_MODE_INVALID = panicAll || Boolean.getBoolean("gt.recipebuilder.panic.invalid");
        PANIC_MODE_COLLISION = panicAll || Boolean.getBoolean("gt.recipebuilder.panic.collision");
        ENABLE_COLLISION_CHECK = Boolean.getBoolean("gt.recipebuilder.recipe_collision_check");
    }

    protected ItemStack[] inputsBasic = new ItemStack[0];
    protected Object[] inputsOreDict;
    protected ItemStack[] outputs = new ItemStack[0];
    protected ItemStack[][] alts;
    protected FluidStack[] fluidInputs = new FluidStack[0];
    protected FluidStack[] fluidOutputs = new FluidStack[0];
    protected int[] chances;
    protected Object special;
    protected int duration = -1;
    protected int eut = -1;
    protected int specialValue;
    protected boolean enabled = true;
    protected boolean hidden = false;
    protected boolean fakeRecipe = false;
    protected boolean mCanBeBuffered = true;
    protected boolean mNeedsEmptyOutput = false;
    protected boolean nbtSensitive = false;
    protected String[] neiDesc;
    protected RecipeCategory recipeCategory;
    protected boolean optimize = true;
    @Nullable
    protected IRecipeMetadataStorage metadataStorage;
    protected boolean checkForCollision = true;
    /**
     * If recipe addition should be skipped.
     */
    protected boolean skip = false;
    protected boolean valid = true;

    GT_RecipeBuilder() {}

    private GT_RecipeBuilder(ItemStack[] inputsBasic, Object[] inputsOreDict, ItemStack[] outputs, ItemStack[][] alts,
        FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int[] chances, Object special, int duration, int eut,
        int specialValue, boolean enabled, boolean hidden, boolean fakeRecipe, boolean mCanBeBuffered,
        boolean mNeedsEmptyOutput, boolean nbtSensitive, String[] neiDesc, RecipeCategory recipeCategory,
        boolean optimize, @Nullable IRecipeMetadataStorage metadataStorage, boolean checkForCollision, boolean skip,
        boolean valid) {
        this.inputsBasic = inputsBasic;
        this.inputsOreDict = inputsOreDict;
        this.outputs = outputs;
        this.alts = alts;
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.chances = chances;
        this.special = special;
        this.duration = duration;
        this.eut = eut;
        this.specialValue = specialValue;
        this.enabled = enabled;
        this.hidden = hidden;
        this.fakeRecipe = fakeRecipe;
        this.mCanBeBuffered = mCanBeBuffered;
        this.mNeedsEmptyOutput = mNeedsEmptyOutput;
        this.nbtSensitive = nbtSensitive;
        this.neiDesc = neiDesc;
        this.recipeCategory = recipeCategory;
        this.optimize = optimize;
        this.metadataStorage = metadataStorage;
        if (this.metadataStorage != null) {
            this.metadataStorage = this.metadataStorage.copy();
        }
        this.checkForCollision = checkForCollision;
        this.skip = skip;
        this.valid = valid;
    }

    // region helper methods

    private static FluidStack[] fix(FluidStack[] fluidInputs) {
        return Arrays.stream(fluidInputs)
            .filter(Objects::nonNull)
            .map(FluidStack::copy)
            .toArray(FluidStack[]::new);
    }

    private static ItemStack[] fix(ItemStack[] inputs) {
        return GT_OreDictUnificator.setStackArray(true, ArrayExt.withoutTrailingNulls(inputs, ItemStack[]::new));
    }

    public static GT_RecipeBuilder builder() {
        return new GT_RecipeBuilder();
    }

    /**
     * Creates empty builder where only duration and EU/t are set to 0.
     */
    public static GT_RecipeBuilder empty() {
        return new GT_RecipeBuilder().duration(0)
            .eut(0);
    }

    private static boolean containsNull(Object[] arr) {
        return arr == null || Arrays.stream(arr)
            .anyMatch(Objects::isNull);
    }

    private static void handleNullRecipeComponents(String componentType) {
        // place a breakpoint here to catch all these issues
        GT_Log.err.print("null detected in ");
        GT_Log.err.println(componentType);
        new NullPointerException().printStackTrace(GT_Log.err);
        if (PANIC_MODE_NULL) {
            throw new IllegalArgumentException("null in argument");
        }
    }

    private static boolean debugNull() {
        return DEBUG_MODE_NULL || PANIC_MODE_NULL;
    }

    public static void handleInvalidRecipe() {
        if (!DEBUG_MODE_INVALID && !PANIC_MODE_INVALID) {
            return;
        }
        // place a breakpoint here to catch all these issues
        GT_Log.err.print("invalid recipe");
        new IllegalArgumentException().printStackTrace(GT_Log.err);
        if (PANIC_MODE_INVALID) {
            throw new IllegalArgumentException("invalid recipe");
        }
    }

    public static void handleRecipeCollision(String details) {
        if (!DEBUG_MODE_COLLISION && !PANIC_MODE_COLLISION) {
            return;
        }
        GT_Log.err.print("Recipe collision resulting in recipe loss detected with ");
        GT_Log.err.println(details);
        if (PANIC_MODE_COLLISION) {
            throw new IllegalArgumentException("Recipe Collision");
        } else {
            // place a breakpoint here to catch all these issues
            new IllegalArgumentException().printStackTrace(GT_Log.err);
        }
    }

    public static void onConfigLoad() {
        PANIC_MODE_NULL |= GT_Mod.gregtechproxy.crashOnNullRecipeInput;
    }

    // endregion

    // region setter

    /**
     * Non-OreDicted item inputs. Assumes input is unified.
     */
    public GT_RecipeBuilder itemInputsUnified(ItemStack... inputs) {
        if (skip) return this;
        if (debugNull() && containsNull(inputs)) handleNullRecipeComponents("itemInputUnified");
        inputsBasic = ArrayExt.withoutTrailingNulls(inputs, ItemStack[]::new);
        inputsOreDict = null;
        alts = null;
        return this;
    }

    /**
     * Non-OreDicted item inputs. Assumes input is not unified.
     */
    public GT_RecipeBuilder itemInputs(ItemStack... inputs) {
        if (skip) return this;
        if (debugNull() && containsNull(inputs)) handleNullRecipeComponents("itemInputs");
        inputsBasic = fix(inputs);
        inputsOreDict = null;
        alts = null;
        return this;
    }

    /**
     * OreDicted item inputs. Currently only used for assline recipes adder.
     */
    public GT_RecipeBuilder itemInputs(Object... inputs) {
        if (skip) return this;
        inputsOreDict = inputs;
        alts = new ItemStack[inputs.length][];
        for (int i = 0, inputsLength = inputs.length; i < inputsLength; i++) {
            Object input = inputs[i];
            if (input instanceof ItemStack) {
                alts[i] = new ItemStack[] { (ItemStack) input };
            } else if (input instanceof ItemStack[]) {
                alts[i] = ((ItemStack[]) input).clone();
            } else if (input instanceof Object[]arr) {
                if (arr.length != 2) continue;
                List<ItemStack> ores = GT_OreDictUnificator.getOres(arr[0]);
                if (ores.isEmpty()) continue;
                int size = ((Number) arr[1]).intValue();
                alts[i] = ores.stream()
                    .map(s -> GT_Utility.copyAmount(size, s))
                    .filter(GT_Utility::isStackValid)
                    .toArray(ItemStack[]::new);
            } else if (input == null) {
                handleNullRecipeComponents("recipe oredict input");
                alts[i] = new ItemStack[0];
            } else {
                throw new IllegalArgumentException("index " + i + ", unexpected type: " + input.getClass());
            }
        }
        inputsBasic = Arrays.stream(alts)
            .map(ss -> ss.length > 0 ? ss[0] : null)
            .toArray(ItemStack[]::new);
        // optimize cannot handle recipes with alts
        return noOptimize();
    }

    public GT_RecipeBuilder itemOutputs(ItemStack... outputs) {
        if (skip) return this;
        if (debugNull() && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
        this.outputs = outputs;
        if (chances != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        return this;
    }

    /**
     * Not intended to be used by recipe authors.
     * Intended for recipe rewrite middlewares.
     */
    public GT_RecipeBuilder itemOutputs(ItemStack[] outputs, int[] chances) {
        if (skip) return this;
        if (debugNull() && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
        this.outputs = outputs;
        this.chances = chances;
        if (chances != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        return this;
    }

    public GT_RecipeBuilder fluidInputs(FluidStack... fluidInputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidInputs)) handleNullRecipeComponents("fluidInputs");
        this.fluidInputs = fix(fluidInputs);
        return this;
    }

    public GT_RecipeBuilder fluidOutputs(FluidStack... fluidOutputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidOutputs)) handleNullRecipeComponents("fluidOutputs");
        this.fluidOutputs = fix(fluidOutputs);
        return this;
    }

    public GT_RecipeBuilder outputChances(int... chances) {
        if (skip) return this;
        if (outputs != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        this.chances = chances;
        return this;
    }

    public GT_RecipeBuilder special(Object special) {
        this.special = special;
        return this;
    }

    public GT_RecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public GT_RecipeBuilder duration(long duration) {
        this.duration = (int) duration;
        return this;
    }

    public GT_RecipeBuilder eut(int eut) {
        if (DEBUG_MODE_FULL_ENERGY) {
            // Ignores ULV voltage
            for (int i = 1; i < GT_Values.VP.length; i++) {
                if (eut <= GT_Values.V[i] && eut > GT_Values.VP[i]) {
                    GT_Log.err.println(
                        "EUt > Practical Voltage detected. EUt: " + eut + ", Practical Voltage: " + GT_Values.VP[i]);
                    new IllegalArgumentException().printStackTrace(GT_Log.err);
                    break;
                }
            }
        }
        this.eut = eut;
        return this;
    }

    public GT_RecipeBuilder eut(long eut) {
        return eut((int) eut);
    }

    /**
     * prefer to use metadata over this. should only use when the target recipe map does not yet support metadata
     * system, or it's to bridge legacy code and modern code.
     */
    public GT_RecipeBuilder specialValue(int specialValue) {
        this.specialValue = specialValue;
        return this;
    }

    // I don't expect anyone to actually call this...
    public GT_RecipeBuilder disabled() {
        this.enabled = false;
        return this;
    }

    public GT_RecipeBuilder hidden() {
        this.hidden = true;
        return this;
    }

    public GT_RecipeBuilder fake() {
        this.fakeRecipe = true;
        return this;
    }

    public GT_RecipeBuilder noBuffer() {
        this.mCanBeBuffered = false;
        return this;
    }

    public GT_RecipeBuilder needsEmptyOutput() {
        this.mNeedsEmptyOutput = true;
        return this;
    }

    public GT_RecipeBuilder nbtSensitive() {
        this.nbtSensitive = true;
        return this;
    }

    public GT_RecipeBuilder setNEIDesc(String... neiDesc) {
        this.neiDesc = neiDesc;
        return this;
    }

    public GT_RecipeBuilder recipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        return this;
    }

    /**
     * Prevent the resulting recipe from optimizing recipe, which is a process that reduce recipe batch size.
     */
    public GT_RecipeBuilder noOptimize() {
        this.optimize = false;
        return this;
    }

    /**
     * Prevents checking collision with existing recipes when adding the built recipe.
     */
    public GT_RecipeBuilder ignoreCollision() {
        this.checkForCollision = false;
        return this;
    }

    /**
     * Sets metadata of the recipe. It can be used for recipe emitter to do special things, or for being stored in the
     * built recipe and used for actual recipe processing.
     * <p>
     * {@link GT_RecipeConstants} has a series of metadata keys. Or you can create one by yourself.
     */
    public <T> GT_RecipeBuilder metadata(RecipeMetadataKey<T> key, T value) {
        if (skip) return this;
        if (metadataStorage == null) {
            metadataStorage = new RecipeMetadataStorage();
        }
        metadataStorage.store(key, value);
        return this;
    }

    /**
     * Gets metadata already set for this builder. Can return null. Use
     * {@link #getMetadataOrDefault(RecipeMetadataKey, Object)}
     * if you want to specify default value.
     */
    @Nullable
    public <T> T getMetadata(RecipeMetadataKey<T> key) {
        if (metadataStorage == null) {
            return null;
        }
        return key.cast(metadataStorage.getMetadata(key));
    }

    /**
     * Gets metadata already set for this builder with default value. Does not return null unless default value is null.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public <T> T getMetadataOrDefault(RecipeMetadataKey<T> key, T defaultValue) {
        if (metadataStorage == null) {
            return defaultValue;
        }
        return key.cast(metadataStorage.getMetadataOrDefault(key, defaultValue));
    }

    /**
     * Specifies mods required to add the recipe. If any of the mods is not loaded, all the operations for this builder
     * will be ignored.
     *
     * @param mods Mod(s) required for the recipe.
     */
    public GT_RecipeBuilder requireMods(Mods... mods) {
        skip = Stream.of(mods)
            .anyMatch(mod -> !mod.isModLoaded());
        return this;
    }

    public GT_RecipeBuilder requiresCleanRoom() {
        return metadata(GT_RecipeConstants.CLEANROOM, true);
    }

    public GT_RecipeBuilder requiresLowGravity() {
        return metadata(GT_RecipeConstants.LOW_GRAVITY, true);
    }

    // endregion

    private static <T> T[] copy(T[] arr) {
        return arr == null ? null : arr.clone();
    }

    private static int[] copy(int[] arr) {
        return arr == null ? null : arr.clone();
    }

    /**
     * produce a deep copy of current values. anything unset will remain unset. IMPORTANT: If metadata contains mutable
     * value, they will not be cloned!
     * <p>
     * checkout docs/RecipeBuilder.md for more info on whether to copy or not.
     */
    public GT_RecipeBuilder copy() {
        return new GT_RecipeBuilder(
            copyItemArray(inputsBasic),
            copy(inputsOreDict),
            copyItemArray(outputs),
            copy(alts),
            copyFluidArray(fluidInputs),
            copyFluidArray(fluidOutputs),
            copy(chances),
            special,
            duration,
            eut,
            specialValue,
            enabled,
            hidden,
            fakeRecipe,
            mCanBeBuffered,
            mNeedsEmptyOutput,
            nbtSensitive,
            copy(neiDesc),
            recipeCategory,
            optimize,
            metadataStorage,
            checkForCollision,
            skip,
            valid);
    }

    /**
     * produce a deep copy of current values. anything unset will remain unset. discard all existing metadata
     */
    public GT_RecipeBuilder copyNoMetadata() {
        return new GT_RecipeBuilder(
            copyItemArray(inputsBasic),
            copy(inputsOreDict),
            copyItemArray(outputs),
            copy(alts),
            copyFluidArray(fluidInputs),
            copyFluidArray(fluidOutputs),
            copy(chances),
            special,
            duration,
            eut,
            specialValue,
            enabled,
            hidden,
            fakeRecipe,
            mCanBeBuffered,
            mNeedsEmptyOutput,
            nbtSensitive,
            copy(neiDesc),
            recipeCategory,
            optimize,
            null,
            checkForCollision,
            skip,
            valid);
    }

    // region getter

    public ItemStack getItemInputBasic(int index) {
        return index < inputsBasic.length ? inputsBasic[index] : null;
    }

    public Object getItemInputOreDict(int index) {
        return index < inputsOreDict.length ? inputsOreDict[index] : null;
    }

    public ItemStack getItemOutput(int index) {
        return index < outputs.length ? outputs[index] : null;
    }

    public FluidStack getFluidInput(int index) {
        return index < fluidInputs.length ? fluidInputs[index] : null;
    }

    public FluidStack getFluidOutput(int index) {
        return index < fluidOutputs.length ? fluidOutputs[index] : null;
    }

    public ItemStack[] getItemInputsBasic() {
        return inputsBasic;
    }

    public Object[] getItemInputsOreDict() {
        return inputsOreDict;
    }

    public ItemStack[] getItemOutputs() {
        return outputs;
    }

    public FluidStack[] getFluidInputs() {
        return fluidInputs;
    }

    public FluidStack[] getFluidOutputs() {
        return fluidOutputs;
    }

    public int getDuration() {
        return duration;
    }

    public int[] getChances() {
        return chances;
    }

    public int getEUt() {
        return eut;
    }

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public boolean isCheckForCollision() {
        return checkForCollision;
    }

    // endregion

    // region validator

    public GT_RecipeBuilder clearInvalid() {
        valid = true;
        return this;
    }

    public GT_RecipeBuilder invalidate() {
        valid = false;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    private static boolean isArrayValid(@Nonnull Object[] arr, int min, int max) {
        int count = 0;
        for (Object o : arr) {
            if (o != null) count += 1;
        }
        return min <= count && max >= count;
    }

    /**
     * Validate if input item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoInput() {
        if (skip) return this;
        return GT_Utility.isArrayEmptyOrNull(inputsBasic) ? this : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoInputFluid() {
        if (skip) return this;
        return GT_Utility.isArrayEmptyOrNull(fluidInputs) ? this : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoOutput() {
        if (skip) return this;
        return GT_Utility.isArrayEmptyOrNull(outputs) ? this : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoOutputFluid() {
        if (skip) return this;
        return GT_Utility.isArrayEmptyOrNull(fluidOutputs) ? this : invalidate();
    }

    /**
     * Validate if input item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateInputCount(int min, int max) {
        if (skip) return this;
        if (inputsBasic == null) return min < 0 ? this : invalidate();
        return isArrayValid(inputsBasic, min, max) ? this : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateInputFluidCount(int min, int max) {
        if (skip) return this;
        if (fluidInputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(fluidInputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateOutputCount(int min, int max) {
        if (skip) return this;
        if (outputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(outputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GT_RecipeBuilder validateOutputFluidCount(int min, int max) {
        if (skip) return this;
        if (fluidOutputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(fluidOutputs, min, max) ? this : invalidate();
    }

    public GT_RecipeBuilder validateAnyInput() {
        if (skip) return this;
        if (fluidInputs != null && isArrayValid(fluidInputs, 1, Integer.MAX_VALUE)) {
            return this;
        }
        if (inputsBasic != null && isArrayValid(inputsBasic, 1, Integer.MAX_VALUE)) {
            return this;
        }
        return invalidate();
    }

    public GT_RecipeBuilder validateAnyOutput() {
        if (skip) return this;
        if (fluidOutputs != null && isArrayValid(fluidOutputs, 1, Integer.MAX_VALUE)) {
            return this;
        }
        if (outputs != null && isArrayValid(outputs, 1, Integer.MAX_VALUE)) {
            return this;
        }
        return invalidate();
    }

    // endregion

    /**
     * Builds new recipe, without custom behavior of recipemaps. For adding recipe to recipemap,
     * use {@link #addTo} instead.
     *
     * @return Built recipe. Returns empty if failed to build.
     */
    public Optional<GT_Recipe> build() {
        if (skip) {
            return Optional.empty();
        }
        if (!valid) {
            handleInvalidRecipe();
            return Optional.empty();
        }
        preBuildChecks();
        optimize();
        return Optional.of(
            decorate(
                new GT_Recipe(
                    inputsBasic,
                    outputs,
                    fluidInputs,
                    fluidOutputs,
                    chances,
                    special,
                    duration,
                    eut,
                    specialValue,
                    enabled,
                    hidden,
                    fakeRecipe,
                    mCanBeBuffered,
                    mNeedsEmptyOutput,
                    nbtSensitive,
                    neiDesc,
                    metadataStorage,
                    recipeCategory)));
    }

    public GT_RecipeBuilder forceOreDictInput() {
        if (inputsOreDict != null || inputsBasic == null) return this;
        return itemInputs((Object[]) inputsBasic);
    }

    public Optional<GT_Recipe.GT_Recipe_WithAlt> buildWithAlt() {
        if (skip) {
            return Optional.empty();
        }
        if (inputsOreDict == null) {
            throw new UnsupportedOperationException();
        }
        if (!valid) {
            handleInvalidRecipe();
            return Optional.empty();
        }
        preBuildChecks();
        // no optimize.
        return Optional.of(
            decorate(
                new GT_Recipe.GT_Recipe_WithAlt(
                    inputsBasic,
                    outputs,
                    fluidInputs,
                    fluidOutputs,
                    chances,
                    special,
                    duration,
                    eut,
                    specialValue,
                    enabled,
                    hidden,
                    fakeRecipe,
                    mCanBeBuffered,
                    mNeedsEmptyOutput,
                    nbtSensitive,
                    neiDesc,
                    metadataStorage,
                    recipeCategory,
                    alts)));
    }

    private void preBuildChecks() {
        if (duration == -1) throw new IllegalStateException("no duration");
        if (eut == -1) throw new IllegalStateException("no eut");
    }

    private void optimize() {
        if (optimize) {
            ArrayList<ItemStack> l = new ArrayList<>();
            l.addAll(Arrays.asList(inputsBasic));
            l.addAll(Arrays.asList(outputs));
            for (int i = 0; i < l.size(); i++) if (l.get(i) == null) l.remove(i--);

            outer: for (byte i = (byte) Math.min(64, duration / 16); i > 1; i--) {
                if (duration / i >= 16) {
                    for (ItemStack stack : l) {
                        if (stack.stackSize % i != 0) continue outer;
                    }
                    for (FluidStack fluidInput : fluidInputs) {
                        if (fluidInput.amount % i != 0) continue outer;
                    }
                    for (FluidStack fluidOutput : fluidOutputs) {
                        if (fluidOutput.amount % i != 0) continue outer;
                    }
                    for (ItemStack itemStack : l) itemStack.stackSize /= i;
                    for (FluidStack fluidInput : fluidInputs) fluidInput.amount /= i;
                    for (FluidStack fluidOutput : fluidOutputs) fluidOutput.amount /= i;
                    duration /= i;
                }
            }
            optimize = false;
        }
    }

    private <T extends GT_Recipe> T decorate(T r) {
        r.mHidden = hidden;
        r.mCanBeBuffered = mCanBeBuffered;
        r.mNeedsEmptyOutput = mNeedsEmptyOutput;
        r.isNBTSensitive = nbtSensitive;
        r.mFakeRecipe = fakeRecipe;
        r.mEnabled = enabled;
        if (neiDesc != null) r.setNeiDesc(neiDesc);
        applyDefaultSpecialValues(r);
        return r;
    }

    private void applyDefaultSpecialValues(GT_Recipe recipe) {
        if (recipe.mSpecialValue != 0) return;

        int specialValue = 0;
        if (getMetadataOrDefault(GT_RecipeConstants.LOW_GRAVITY, false)) specialValue -= 100;
        if (getMetadataOrDefault(GT_RecipeConstants.CLEANROOM, false)) specialValue -= 200;
        for (RecipeMetadataKey<Integer> ident : SPECIAL_VALUE_ALIASES) {
            Integer metadata = getMetadataOrDefault(ident, null);
            if (metadata != null) {
                specialValue = metadata;
                break;
            }
        }
        recipe.mSpecialValue = specialValue;
    }

    public Collection<GT_Recipe> addTo(IRecipeMap recipeMap) {
        if (skip) {
            return Collections.emptyList();
        }
        return recipeMap.doAdd(this);
    }

    public GT_RecipeBuilder reset() {
        metadataStorage = null;
        alts = null;
        chances = null;
        duration = -1;
        enabled = true;
        eut = -1;
        fakeRecipe = false;
        fluidInputs = null;
        fluidOutputs = null;
        hidden = false;
        inputsBasic = null;
        inputsOreDict = null;
        mCanBeBuffered = true;
        mNeedsEmptyOutput = false;
        nbtSensitive = false;
        neiDesc = null;
        recipeCategory = null;
        optimize = true;
        outputs = null;
        special = null;
        specialValue = 0;
        skip = false;
        valid = true;
        return this;
    }
}
