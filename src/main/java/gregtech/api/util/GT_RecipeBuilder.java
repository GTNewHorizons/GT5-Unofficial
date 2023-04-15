package gregtech.api.util;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.IGT_RecipeMap;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.util.extensions.ArrayExt;

public class GT_RecipeBuilder {

    // debug mode expose problems. panic mode help you check nothing is wrong-ish without you actively monitoring
    private static final boolean DEBUG_MODE;
    private static final boolean PANIC_MODE;

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
        if (System.getProperties()
            .containsKey("gt.recipebuilder.debug")) {
            DEBUG_MODE = Boolean.getBoolean("gt.recipebuilder.debug");
        } else {
            // turn on debug by default in dev mode
            DEBUG_MODE = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        }
        PANIC_MODE = DEBUG_MODE && Boolean.getBoolean("gt.recipebuilder.panic");
    }

    protected ItemStack[] inputsBasic;
    protected Object[] inputsOreDict;
    protected ItemStack[] outputs;
    protected ItemStack[][] alts;
    protected FluidStack[] fluidInputs;
    protected FluidStack[] fluidOutputs;
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
    protected String[] neiDesc;
    protected boolean optimize = true;
    protected Map<MetadataIdentifier<?>, Object> additionalData = new HashMap<>();
    protected boolean valid = true;

    GT_RecipeBuilder() {}

    private GT_RecipeBuilder(ItemStack[] inputsBasic, Object[] inputsOreDict, ItemStack[] outputs, ItemStack[][] alts,
        FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int[] chances, Object special, int duration, int eut,
        int specialValue, boolean enabled, boolean hidden, boolean fakeRecipe, boolean mCanBeBuffered,
        boolean mNeedsEmptyOutput, String[] neiDesc, boolean optimize,
        Map<MetadataIdentifier<?>, Object> additionalData) {
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
        this.neiDesc = neiDesc;
        this.optimize = optimize;
        this.additionalData.putAll(additionalData);
    }

    private static FluidStack[] fix(FluidStack[] fluidInputs) {
        return Arrays.stream(fluidInputs)
            .filter(Objects::nonNull)
            .map(GT_FluidStack::new)
            .toArray(FluidStack[]::new);
    }

    private static ItemStack[] fix(ItemStack[] inputs) {
        return GT_OreDictUnificator.setStackArray(true, ArrayExt.withoutTrailingNulls(inputs, ItemStack[]::new));
    }

    public static GT_RecipeBuilder builder() {
        return new GT_RecipeBuilder();
    }

    private static boolean containsNull(Object[] arr) {
        return arr == null || Arrays.stream(arr)
            .anyMatch(Objects::isNull);
    }

    private static void handleNullRecipeComponents(String componentType) {
        if (PANIC_MODE) {
            throw new IllegalArgumentException("null in argument");
        } else {
            // place a breakpoint here to catch all these issues
            GT_Log.err.print("null detected in ");
            GT_Log.err.println(componentType);
            new NullPointerException().printStackTrace(GT_Log.err);
        }
    }

    /**
     * Non-OreDicted item inputs. Assumes input is unified.
     */
    public GT_RecipeBuilder itemInputsUnified(ItemStack... inputs) {
        if (DEBUG_MODE && containsNull(inputs)) handleNullRecipeComponents("itemInputUnified");
        inputsBasic = ArrayExt.withoutTrailingNulls(inputs, ItemStack[]::new);
        inputsOreDict = null;
        alts = null;
        return this;
    }

    /**
     * Non-OreDicted item inputs. Assumes input is not unified.
     */
    public GT_RecipeBuilder itemInputs(ItemStack... inputs) {
        if (DEBUG_MODE && containsNull(inputs)) handleNullRecipeComponents("itemInputs");
        inputsBasic = fix(inputs);
        inputsOreDict = null;
        alts = null;
        return this;
    }

    /**
     * OreDicted item inputs. Currently only used for assline recipes adder.
     */
    public GT_RecipeBuilder itemInputs(Object... inputs) {
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

    /**
     * Same as itemInputs(), but make it clear that no item inputs is intended, instead of a mistake.
     */
    public GT_RecipeBuilder noItemInputs() {
        // this does not call into one of the itemInputs, to make it clear what is the expected behavior here.
        inputsBasic = new ItemStack[0];
        inputsOreDict = null;
        alts = null;
        return this;
    }

    public GT_RecipeBuilder itemOutputs(ItemStack... outputs) {
        if (DEBUG_MODE && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
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
        if (DEBUG_MODE && containsNull(outputs)) handleNullRecipeComponents("itemOutputs");
        this.outputs = outputs;
        this.chances = chances;
        if (chances != null && chances.length != outputs.length) {
            throw new IllegalArgumentException("Output chances array and items array length differs");
        }
        return this;
    }

    public GT_RecipeBuilder noItemOutputs() {
        return itemOutputs();
    }

    public GT_RecipeBuilder fluidInputs(FluidStack... fluidInputs) {
        if (DEBUG_MODE && containsNull(fluidInputs)) handleNullRecipeComponents("fluidInputs");
        this.fluidInputs = fix(fluidInputs);
        return this;
    }

    public GT_RecipeBuilder noFluidInputs() {
        return fluidInputs == null ? fluidInputs() : this;
    }

    public GT_RecipeBuilder fluidOutputs(FluidStack... fluidOutputs) {
        if (DEBUG_MODE && containsNull(fluidOutputs)) handleNullRecipeComponents("fluidOutputs");
        this.fluidOutputs = fix(fluidOutputs);
        return this;
    }

    public GT_RecipeBuilder noFluidOutputs() {
        return fluidOutputs();
    }

    public GT_RecipeBuilder noOutputs() {
        return noFluidOutputs().noItemOutputs();
    }

    public GT_RecipeBuilder outputChances(int... chances) {
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

    /**
     * Really just {@link #special(Object)}, but with a different signature to make it less confusing. WARNING: only for
     * legacy recipe map. do not abuse.
     */
    public GT_RecipeBuilder specialItem(ItemStack specialItem) {
        return special(specialItem);
    }

    public GT_RecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public GT_RecipeBuilder eut(int eut) {
        this.eut = eut;
        return this;
    }

    public GT_RecipeBuilder eut(long eut) {
        this.eut = (int) eut;
        return this;
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

    public GT_RecipeBuilder setNEIDesc(String... neiDesc) {
        this.neiDesc = neiDesc;
        return this;
    }

    /**
     * Prevent the resulting recipe from optimizing recipe, which is a process that reduce recipe batch size.
     */
    public GT_RecipeBuilder noOptimize() {
        this.optimize = false;
        return this;
    }

    public <T> GT_RecipeBuilder metadata(MetadataIdentifier<T> key, T value) {
        additionalData.put(key, value);
        return this;
    }

    public <T> T getMetadata(MetadataIdentifier<T> key) {
        return key.cast(additionalData.get(key));
    }

    public <T> T getMetadata(MetadataIdentifier<T> key, T defaultValue) {
        return key.cast(additionalData.getOrDefault(key, defaultValue));
    }

    public GT_RecipeBuilder requiresCleanRoom() {
        return metadata(GT_RecipeConstants.CLEANROOM, true);
    }

    public GT_RecipeBuilder requiresLowGravity() {
        return metadata(GT_RecipeConstants.LOW_GRAVITY, true);
    }

    private static <T> T[] copy(T[] arr) {
        return arr == null ? null : arr.clone();
    }

    private static int[] copy(int[] arr) {
        return arr == null ? null : arr.clone();
    }

    /**
     * produce a deep copy of current values. anything unset will remain unset. IMPORTANT: If metadata contains mutable
     * value, they will not be cloned!
     *
     * checkout docs/RecipeBuilder.md for more info on whether to copy or not.
     */
    public GT_RecipeBuilder copy() {
        return new GT_RecipeBuilder(
            copy(inputsBasic),
            copy(inputsOreDict),
            copy(outputs),
            copy(alts),
            copy(fluidInputs),
            copy(fluidOutputs),
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
            neiDesc,
            optimize,
            additionalData);
    }

    /**
     * produce a deep copy of current values. anything unset will remain unset. discard all existing metadata
     */
    public GT_RecipeBuilder copyNoMetadata() {
        return new GT_RecipeBuilder(
            copy(inputsBasic),
            copy(inputsOreDict),
            copy(outputs),
            copy(alts),
            copy(fluidInputs),
            copy(fluidOutputs),
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
            neiDesc,
            optimize,
            Collections.emptyMap());
    }

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
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoInput() {
        return GT_Utility.isArrayEmptyOrNull(inputsBasic) ? noItemInputs() : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoInputFluid() {
        return GT_Utility.isArrayEmptyOrNull(fluidInputs) ? noFluidInputs() : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoOutput() {
        return GT_Utility.isArrayEmptyOrNull(outputs) ? noItemInputs() : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateNoOutputFluid() {
        return GT_Utility.isArrayEmptyOrNull(fluidOutputs) ? noFluidOutputs() : invalidate();
    }

    /**
     * Validate if input item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateInputCount(int min, int max) {
        if (inputsBasic == null) return min < 0 ? noItemInputs() : invalidate();
        return isArrayValid(inputsBasic, min, max) ? this : invalidate();
    }

    /**
     * Validate if input fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateInputFluidCount(int min, int max) {
        if (fluidInputs == null) return min < 0 ? noItemInputs() : invalidate();
        return isArrayValid(fluidInputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output item match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateOutputCount(int min, int max) {
        if (outputs == null) return min < 0 ? noItemInputs() : invalidate();
        return isArrayValid(outputs, min, max) ? this : invalidate();
    }

    /**
     * Validate if output fluid match requirement. Return as invalidated if fails prereq. Specify -1 as min to allow
     * unset. Both bound inclusive. Only supposed to be called by IGT_RecipeMap and not client code.
     */
    public GT_RecipeBuilder validateOutputFluidCount(int min, int max) {
        if (fluidOutputs == null) return min < 0 ? noItemInputs() : invalidate();
        return isArrayValid(fluidOutputs, min, max) ? this : invalidate();
    }

    public GT_RecipeBuilder validateAnyInput() {
        if (fluidInputs != null && isArrayValid(fluidInputs, 1, Integer.MAX_VALUE)) {
            return inputsBasic == null ? noItemInputs() : this;
        }
        if (inputsBasic != null && isArrayValid(inputsBasic, 1, Integer.MAX_VALUE)) {
            return fluidInputs == null ? noFluidInputs() : this;
        }
        return invalidate();
    }

    public GT_RecipeBuilder validateAnyOutput() {
        if (fluidOutputs != null && isArrayValid(fluidOutputs, 1, Integer.MAX_VALUE)) {
            return outputs == null ? noItemOutputs() : this;
        }
        if (outputs != null && isArrayValid(outputs, 1, Integer.MAX_VALUE)) {
            return fluidOutputs == null ? noFluidOutputs() : this;
        }
        return invalidate();
    }

    public Optional<GT_Recipe> build() {
        if (!valid) return Optional.empty();
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
                    neiDesc)));
    }

    public GT_RecipeBuilder forceOreDictInput() {
        if (inputsOreDict != null || inputsBasic == null) return this;
        return itemInputs((Object[]) inputsBasic);
    }

    public Optional<GT_Recipe.GT_Recipe_WithAlt> buildWithAlt() {
        if (inputsOreDict == null) {
            throw new UnsupportedOperationException();
        }
        if (!valid) return Optional.empty();
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
                    neiDesc,
                    alts)));
    }

    private void preBuildChecks() {
        if (inputsBasic == null) throw new IllegalStateException("no itemInputs");
        if (outputs == null) throw new IllegalStateException("no itemOutputs");
        if (fluidInputs == null) throw new IllegalStateException("no fluidInputs");
        if (fluidOutputs == null) throw new IllegalStateException("no fluidOutputs");
        if (duration == -1) throw new IllegalStateException("no duration");
        if (eut == -1) throw new IllegalStateException("no eut");
    }

    private void optimize() {
        if (optimize) {
            ArrayList<ItemStack> l = new ArrayList<>();
            l.addAll(Arrays.asList(inputsBasic));
            l.addAll(Arrays.asList(outputs));
            for (int i = 0; i < l.size(); i++) if (l.get(i) == null) l.remove(i--);

            for (byte i = (byte) Math.min(64, duration / 16); i > 1; i--) if (duration / i >= 16) {
                boolean temp = true;
                for (ItemStack stack : l) if (stack.stackSize % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack fluidInput : fluidInputs) if (fluidInput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack fluidOutput : fluidOutputs) if (fluidOutput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) {
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
        r.mFakeRecipe = fakeRecipe;
        r.mEnabled = enabled;
        if (neiDesc != null) r.setNeiDesc(neiDesc);
        return r;
    }

    public Collection<GT_Recipe> addTo(IGT_RecipeMap recipeMap) {
        return recipeMap.doAdd(this);
    }

    public GT_RecipeBuilder reset() {
        additionalData.clear();
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
        neiDesc = null;
        optimize = true;
        outputs = null;
        special = null;
        specialValue = 0;
        valid = true;
        return this;
    }

    public final static class MetadataIdentifier<T> {

        private static final Map<MetadataIdentifier<?>, MetadataIdentifier<?>> allIdentifiers = Collections
            .synchronizedMap(new HashMap<>());
        private final Class<T> clazz;
        private final String identifier;

        private MetadataIdentifier(Class<T> clazz, String identifier) {
            this.clazz = clazz;
            this.identifier = identifier;
        }

        public static <T> MetadataIdentifier<T> create(Class<T> clazz, String identifier) {
            MetadataIdentifier<T> key = new MetadataIdentifier<>(clazz, identifier);
            return (MetadataIdentifier<T>) allIdentifiers.computeIfAbsent(key, Function.identity());
        }

        public T cast(Object o) {
            return clazz.cast(o);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MetadataIdentifier<?> that = (MetadataIdentifier<?>) o;

            if (!clazz.equals(that.clazz)) return false;
            return identifier.equals(that.identifier);
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + identifier.hashCode();
            return result;
        }
    }
}
