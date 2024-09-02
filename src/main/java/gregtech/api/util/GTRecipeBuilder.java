package gregtech.api.util;

import static gregtech.api.util.GTRecipeMapUtil.SPECIAL_VALUE_ALIASES;
import static gregtech.api.util.GTUtility.copyFluidArray;
import static gregtech.api.util.GTUtility.copyItemArray;

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

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.recipe.metadata.RecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class GTRecipeBuilder {

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

    GTRecipeBuilder() {}

    private GTRecipeBuilder(ItemStack[] inputsBasic, Object[] inputsOreDict, ItemStack[] outputs, ItemStack[][] alts,
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
        return GTOreDictUnificator.setStackArray(true, ArrayExt.withoutTrailingNulls(inputs, ItemStack[]::new));
    }

    public static GTRecipeBuilder builder() {
        return new GTRecipeBuilder();
    }

    /**
     * Creates empty builder where only duration and EU/t are set to 0.
     */
    public static GTRecipeBuilder empty() {
        return new GTRecipeBuilder().duration(0)
            .eut(0);
    }

    private static boolean containsNull(Object[] arr) {
        return arr == null || Arrays.stream(arr)
            .anyMatch(Objects::isNull);
    }

    private static void handleNullRecipeComponents(String componentType) {
        // place a breakpoint here to catch all these issues
        GTLog.err.print("null detected in ");
        GTLog.err.println(componentType);
        new NullPointerException().printStackTrace(GTLog.err);
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
        GTLog.err.print("invalid recipe");
        new IllegalArgumentException().printStackTrace(GTLog.err);
        if (PANIC_MODE_INVALID) {
            throw new IllegalArgumentException("invalid recipe");
        }
    }

    public static void handleRecipeCollision(String details) {
        if (!DEBUG_MODE_COLLISION && !PANIC_MODE_COLLISION) {
            return;
        }
        GTLog.err.print("Recipe collision resulting in recipe loss detected with ");
        GTLog.err.println(details);
        if (PANIC_MODE_COLLISION) {
            throw new IllegalArgumentException("Recipe Collision");
        } else {
            // place a breakpoint here to catch all these issues
            new IllegalArgumentException().printStackTrace(GTLog.err);
        }
    }

    public static void onConfigLoad() {
        PANIC_MODE_NULL |= GTMod.gregtechproxy.crashOnNullRecipeInput;
    }

    // endregion

    // region setter

    /**
     * Non-OreDicted item inputs. Assumes input is unified.
     */
    public GTRecipeBuilder itemInputsUnified(ItemStack... inputs) {
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
    public GTRecipeBuilder itemInputs(ItemStack... inputs) {
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
    public GTRecipeBuilder itemInputs(Object... inputs) {
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
                List<ItemStack> ores = GTOreDictUnificator.getOres(arr[0]);
                if (ores.isEmpty()) continue;
                int size = ((Number) arr[1]).intValue();
                alts[i] = ores.stream()
                    .map(s -> GTUtility.copyAmount(size, s))
                    .filter(GTUtility::isStackValid)
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

    public GTRecipeBuilder itemOutputs(ItemStack... outputs) {
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
    public GTRecipeBuilder itemOutputs(ItemStack[] outputs, int[] chances) {
        if (skip) return this;
        if (debugNull() && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
        this.outputs = outputs;
        this.chances = chances;
        if (chances != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        return this;
    }

    public GTRecipeBuilder fluidInputs(FluidStack... fluidInputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidInputs)) handleNullRecipeComponents("fluidInputs");
        this.fluidInputs = fix(fluidInputs);
        return this;
    }

    public GTRecipeBuilder fluidOutputs(FluidStack... fluidOutputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidOutputs)) handleNullRecipeComponents("fluidOutputs");
        this.fluidOutputs = fix(fluidOutputs);
        return this;
    }

    public GTRecipeBuilder outputChances(int... chances) {
        if (skip) return this;
        if (outputs != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        this.chances = chances;
        return this;
    }

    public GTRecipeBuilder special(Object special) {
        this.special = special;
        return this;
    }

    public GTRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public GTRecipeBuilder duration(long duration) {
        this.duration = (int) duration;
        return this;
    }

    public GTRecipeBuilder eut(int eut) {
        if (DEBUG_MODE_FULL_ENERGY) {
            // Ignores ULV voltage
            for (int i = 1; i < GTValues.VP.length; i++) {
                if (eut <= GTValues.V[i] && eut > GTValues.VP[i]) {
                    GTLog.err.println(
                        "EUt > Practical Voltage detected. EUt: " + eut + ", Practical Voltage: " + GTValues.VP[i]);
                    new IllegalArgumentException().printStackTrace(GTLog.err);
                    break;
                }
            }
        }
        this.eut = eut;
        return this;
    }

    public GTRecipeBuilder eut(long eut) {
        return eut((int) eut);
    }

    /**
     * prefer to use metadata over this. should only use when the target recipe map does not yet support metadata
     * system, or it's to bridge legacy code and modern code.
     */
    public GTRecipeBuilder specialValue(int specialValue) {
        this.specialValue = specialValue;
        return this;
    }

    // I don't expect anyone to actually call this...
    public GTRecipeBuilder disabled() {
        this.enabled = false;
        return this;
    }

    public GTRecipeBuilder hidden() {
        this.hidden = true;
        return this;
    }

    public GTRecipeBuilder fake() {
        this.fakeRecipe = true;
        return this;
    }

    public GTRecipeBuilder noBuffer() {
        this.mCanBeBuffered = false;
        return this;
    }

    public GTRecipeBuilder needsEmptyOutput() {
        this.mNeedsEmptyOutput = true;
        return this;
    }

    public GTRecipeBuilder nbtSensitive() {
        this.nbtSensitive = true;
        return this;
    }

    public GTRecipeBuilder setNEIDesc(String... neiDesc) {
        this.neiDesc = neiDesc;
        return this;
    }

    public GTRecipeBuilder recipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        return this;
    }

    /**
     * Prevent the resulting recipe from optimizing recipe, which is a process that reduce recipe batch size.
     */
    public GTRecipeBuilder noOptimize() {
        this.optimize = false;
        return this;
    }

    /**
     * Prevents checking collision with existing recipes when adding the built recipe.
     */
    public GTRecipeBuilder ignoreCollision() {
        this.checkForCollision = false;
        return this;
    }

    /**
     * Sets metadata of the recipe. It can be used for recipe emitter to do special things, or for being stored in the
     * built recipe and used for actual recipe processing.
     * <p>
     * {@link GTRecipeConstants} has a series of metadata keys. Or you can create one by yourself.
     */
    public <T> GTRecipeBuilder metadata(RecipeMetadataKey<T> key, T value) {
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
    public GTRecipeBuilder requireMods(Mods... mods) {
        skip = Stream.of(mods)
            .anyMatch(mod -> !mod.isModLoaded());
        return this;
    }

    public GTRecipeBuilder requiresCleanRoom() {
        return metadata(GTRecipeConstants.CLEANROOM, true);
    }

    public GTRecipeBuilder requiresLowGravity() {
        return metadata(GTRecipeConstants.LOW_GRAVITY, true);
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
    public GTRecipeBuilder copy() {
        return new GTRecipeBuilder(
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
    public GTRecipeBuilder copyNoMetadata() {
        return new GTRecipeBuilder(
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

    public GTRecipeBuilder clearInvalid() {
        valid = true;
        return this;
    }

    public GTRecipeBuilder invalidate() {
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
    public GTRecipeBuilder validateNoInput() {
        if (skip) return this;
        return GTUtility.isArrayEmptyOrNull(inputsBasic) ? this : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateNoInputFluid() {
        if (skip) return this;
        return GTUtility.isArrayEmptyOrNull(fluidInputs) ? this : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateNoOutput() {
        if (skip) return this;
        return GTUtility.isArrayEmptyOrNull(outputs) ? this : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateNoOutputFluid() {
        if (skip) return this;
        return GTUtility.isArrayEmptyOrNull(fluidOutputs) ? this : invalidate();
    }

    /**
     * Validate if input item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateInputCount(int min, int max) {
        if (skip) return this;
        if (inputsBasic == null) return min < 0 ? this : invalidate();
        return isArrayValid(inputsBasic, min, max) ? this : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateInputFluidCount(int min, int max) {
        if (skip) return this;
        if (fluidInputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(fluidInputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateOutputCount(int min, int max) {
        if (skip) return this;
        if (outputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(outputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IRecipeMap and not client code.
     */
    public GTRecipeBuilder validateOutputFluidCount(int min, int max) {
        if (skip) return this;
        if (fluidOutputs == null) return min < 0 ? this : invalidate();
        return isArrayValid(fluidOutputs, min, max) ? this : invalidate();
    }

    public GTRecipeBuilder validateAnyInput() {
        if (skip) return this;
        if (fluidInputs != null && isArrayValid(fluidInputs, 1, Integer.MAX_VALUE)) {
            return this;
        }
        if (inputsBasic != null && isArrayValid(inputsBasic, 1, Integer.MAX_VALUE)) {
            return this;
        }
        return invalidate();
    }

    public GTRecipeBuilder validateAnyOutput() {
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
    public Optional<GTRecipe> build() {
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
                new GTRecipe(
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

    public GTRecipeBuilder forceOreDictInput() {
        if (inputsOreDict != null || inputsBasic == null) return this;
        return itemInputs((Object[]) inputsBasic);
    }

    public Optional<GTRecipe.GTRecipe_WithAlt> buildWithAlt() {
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
                new GTRecipe.GTRecipe_WithAlt(
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

    private <T extends GTRecipe> T decorate(T r) {
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

    private void applyDefaultSpecialValues(GTRecipe recipe) {
        if (recipe.mSpecialValue != 0) return;

        int specialValue = 0;
        if (getMetadataOrDefault(GTRecipeConstants.LOW_GRAVITY, false)) specialValue -= 100;
        if (getMetadataOrDefault(GTRecipeConstants.CLEANROOM, false)) specialValue -= 200;
        for (RecipeMetadataKey<Integer> ident : SPECIAL_VALUE_ALIASES) {
            Integer metadata = getMetadataOrDefault(ident, null);
            if (metadata != null) {
                specialValue = metadata;
                break;
            }
        }
        recipe.mSpecialValue = specialValue;
    }

    public Collection<GTRecipe> addTo(IRecipeMap recipeMap) {
        if (skip) {
            return Collections.emptyList();
        }
        return recipeMap.doAdd(this);
    }

    public GTRecipeBuilder reset() {
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
