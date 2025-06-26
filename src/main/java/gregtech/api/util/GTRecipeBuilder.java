package gregtech.api.util;

import static gregtech.api.util.GTRecipeMapUtil.SPECIAL_VALUE_ALIASES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    public static final boolean DEBUG_MODE_COLLISION;

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
    public static final int HALF_INGOTS = INGOTS / 2;
    public static final int QUARTER_INGOTS = INGOTS / 4;
    public static final int EIGHTH_INGOTS = INGOTS / 8;
    public static final int NUGGETS = INGOTS / 9;
    public static final int STACKS = 64 * INGOTS;

    /** @deprecated Use {@code INGOTS} or quantities in liters instead. */
    @Deprecated
    public static final int BUCKETS = 1_000;

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

    protected ItemStack[] inputsBasic = GTValues.emptyItemStackArray;
    protected Object[] inputsOreDict;
    protected ItemStack[] outputs = GTValues.emptyItemStackArray;
    protected ItemStack[][] alts;
    protected FluidStack[] fluidInputs = GTValues.emptyFluidStackArray;
    protected FluidStack[] fluidOutputs = GTValues.emptyFluidStackArray;
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
        @Nullable IRecipeMetadataStorage metadataStorage, boolean checkForCollision, boolean skip, boolean valid) {
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
        this.metadataStorage = metadataStorage;
        if (this.metadataStorage != null) {
            this.metadataStorage = this.metadataStorage.copy();
        }
        this.checkForCollision = checkForCollision;
        this.skip = skip;
        this.valid = valid;
    }

    // region helper methods

    /**
     * Returns a copy of the array without the null elements.
     * Returns the singleton {@link gregtech.api.enums.GTValues#emptyFluidStackArray} if the array is empty.
     */
    @Nonnull
    static FluidStack[] removeNullFluids(@Nonnull FluidStack[] fluids) {
        if (fluids.length == 0) return GTValues.emptyFluidStackArray;
        int count = 0;
        for (final FluidStack f : fluids) {
            if (f != null) count++;
        }
        if (count == 0) return GTValues.emptyFluidStackArray;
        final FluidStack[] a = new FluidStack[count];
        int i = 0;
        for (final FluidStack f : fluids) {
            if (f != null) {
                a[i] = f;
                i++;
            }
        }
        return a;
    }

    /**
     * Returns a copy of the array without any null elements at the end.
     * Returns the singleton {@link gregtech.api.enums.GTValues#emptyItemStackArray} if the array is empty.
     */
    @Nonnull
    static ItemStack[] removeTrailingNulls(@Nonnull ItemStack[] array) {
        if (array.length == 0) return GTValues.emptyItemStackArray;
        int nullIndex = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) {
                nullIndex = i;
            } else {
                break;
            }
        }
        if (nullIndex == -1) {
            // array has no trailing null
            final ItemStack[] a = new ItemStack[array.length];
            System.arraycopy(array, 0, a, 0, array.length);
            return a;
        } else if (nullIndex == 0) {
            // array has no element
            return GTValues.emptyItemStackArray;
        } else {
            // array has trailing nulls that needs removing
            final ItemStack[] a = new ItemStack[nullIndex];
            System.arraycopy(array, 0, a, 0, nullIndex);
            return a;
        }
    }

    /**
     * Returns a copy of the array without any null elements at the end.
     * Returns the singleton {@link gregtech.api.enums.GTValues#emptyFluidStackArray} if the array is empty.
     */
    @Nonnull
    static FluidStack[] removeTrailingNulls(@Nonnull FluidStack[] array) {
        if (array.length == 0) return GTValues.emptyFluidStackArray;
        int nullIndex = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) {
                nullIndex = i;
            } else {
                break;
            }
        }
        if (nullIndex == -1) {
            // array has no trailing null
            final FluidStack[] a = new FluidStack[array.length];
            System.arraycopy(array, 0, a, 0, array.length);
            return a;
        } else if (nullIndex == 0) {
            // array has no element
            return GTValues.emptyFluidStackArray;
        } else {
            // array has trailing nulls that needs removing
            final FluidStack[] a = new FluidStack[nullIndex];
            System.arraycopy(array, 0, a, 0, nullIndex);
            return a;
        }
    }

    private static ItemStack[] fixItemArray(ItemStack[] inputs, boolean aUnsafe) {
        return GTOreDictUnificator.setStackArray(true, aUnsafe, removeTrailingNulls(inputs));
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
        if (arr == null) return true;
        for (final Object o : arr) {
            if (o == null) return true;
        }
        return false;
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

    public static void handleInvalidRecipeLowFluids() {
        if (!DEBUG_MODE_INVALID && !PANIC_MODE_INVALID) {
            return;
        }
        // place a breakpoint here to catch all these issues
        GTLog.err.println("invalid recipe: not enough input fluids");
        new IllegalArgumentException().printStackTrace(GTLog.err);
        if (PANIC_MODE_INVALID) {
            throw new IllegalArgumentException("invalid recipe");
        }
    }

    public static void handleInvalidRecipeLowItems() {
        if (!DEBUG_MODE_INVALID && !PANIC_MODE_INVALID) {
            return;
        }
        // place a breakpoint here to catch all these issues
        GTLog.err.println("invalid recipe: not enough input items");
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
        inputsBasic = removeTrailingNulls(inputs);
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
        inputsBasic = fixItemArray(inputs, false);
        inputsOreDict = null;
        alts = null;
        return this;
    }

    /**
     * Non-OreDicted item inputs, allowing stack sizes greater than 64. Assumes input is not unified
     */
    public GTRecipeBuilder itemInputsUnsafe(ItemStack... inputs) {
        if (skip) return this;
        if (debugNull() && containsNull(inputs)) handleNullRecipeComponents("itemInputs");
        inputsBasic = fixItemArray(inputs, true);
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
                ArrayList<ItemStack> ores = GTOreDictUnificator.getOres(arr[0]);
                if (ores.isEmpty()) continue;
                int size = ((Number) arr[1]).intValue();
                ArrayList<ItemStack> list = new ArrayList<>(ores.size());
                // noinspection ForLoopReplaceableByForEach
                for (int j = 0, oresSize = ores.size(); j < oresSize; j++) {
                    ItemStack itemStack = GTUtility.copyAmount(size, ores.get(j));
                    if (GTUtility.isStackValid(itemStack)) list.add(itemStack);
                }
                alts[i] = list.toArray(new ItemStack[0]);
            } else if (input == null) {
                handleNullRecipeComponents("recipe oredict input");
                alts[i] = GTValues.emptyItemStackArray;
            } else {
                throw new IllegalArgumentException("index " + i + ", unexpected type: " + input.getClass());
            }
        }
        ArrayList<ItemStack> list = new ArrayList<>(alts.length);
        for (final ItemStack[] ss : alts) list.add(ss.length > 0 ? ss[0] : null);
        inputsBasic = list.isEmpty() ? GTValues.emptyItemStackArray : list.toArray(new ItemStack[0]);
        return this;
    }

    public GTRecipeBuilder itemOutputs(ItemStack... outputs) {
        if (skip) return this;
        if (debugNull() && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
        this.outputs = ArrayExt.isArrayEmpty(outputs) ? GTValues.emptyItemStackArray : outputs;
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
        this.outputs = ArrayExt.isArrayEmpty(outputs) ? GTValues.emptyItemStackArray : outputs;
        this.chances = chances;
        if (chances != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        return this;
    }

    public GTRecipeBuilder fluidInputs(FluidStack... fluidInputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidInputs)) handleNullRecipeComponents("fluidInputs");
        this.fluidInputs = removeNullFluids(fluidInputs);
        return this;
    }

    public GTRecipeBuilder fluidOutputs(FluidStack... fluidOutputs) {
        if (skip) return this;
        if (debugNull() && containsNull(fluidOutputs)) handleNullRecipeComponents("fluidOutputs");
        this.fluidOutputs = removeNullFluids(fluidOutputs);
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
        for (final Mods mod : mods) {
            if (!mod.isModLoaded()) {
                skip = true;
                return this;
            }
        }
        skip = false;
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
            ArrayExt.copyItemsIfNonEmpty(inputsBasic),
            copy(inputsOreDict),
            ArrayExt.copyItemsIfNonEmpty(outputs),
            copy(alts),
            ArrayExt.copyFluidsIfNonEmpty(fluidInputs),
            ArrayExt.copyFluidsIfNonEmpty(fluidOutputs),
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
            ArrayExt.copyItemsIfNonEmpty(inputsBasic),
            copy(inputsOreDict),
            ArrayExt.copyItemsIfNonEmpty(outputs),
            copy(alts),
            ArrayExt.copyFluidsIfNonEmpty(fluidInputs),
            ArrayExt.copyFluidsIfNonEmpty(fluidOutputs),
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
        outputs = null;
        special = null;
        specialValue = 0;
        skip = false;
        valid = true;
        return this;
    }
}
