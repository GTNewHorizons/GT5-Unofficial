package gregtech.api.structure;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.GTMod;
import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;

/**
 * A wrapper that helps reduce structure check boilerplate. This should only be created in the prototype MTE, then
 * shared among the instance MTEs.
 *
 * Definitions: Prototype MTE: An MTE that does not exist in the world and whose main purpose is to make instance MTEs
 * Instance MTE: An MTE that exists in the world
 */
public class StructureWrapper<MTE extends MTEMultiBlockBase & IAlignment & IStructureProvider<MTE>> {

    public static final String STRUCTURE_SHAPE_MAIN = "main";

    public final IStructureProvider<MTE> provider;

    public IStructureDefinition<MTE> structureDefinition;

    public Vec3Impl controllerOffset, minSize, maxSize;
    public Char2ObjectArrayMap<CasingInfo<MTE>> casings;
    public Char2IntArrayMap minCasingCounts, maxCasingCounts;

    public Function<MTE, IStructureInstance<MTE>> instanceExtractor;

    public StructureWrapper(IStructureProvider<MTE> provider) {
        this.provider = provider;
    }

    public void loadStructure() {
        structureDefinition = null;
        casings = new Char2ObjectArrayMap<>();
        minCasingCounts = new Char2IntArrayMap();
        maxCasingCounts = new Char2IntArrayMap();
        controllerOffset = null;

        try {
            String[][] definitionText = provider.getDefinition();

            analyzeMinDefinition(definitionText);

            analyzeMaxDefinition();

            structureDefinition = provider.compile(definitionText);
        } catch (Exception t) {
            GTMod.GT_FML_LOGGER.error("Could not compile structure", t);
        }
    }

    private void analyzeMinDefinition(String[][] definitionText) {
        // find the controller offset, calculate the multi size, and calculate the min/default casing counts
        int width = 0;
        int height = 0;
        int length = definitionText.length;

        int z = 0;
        for (String[] a : definitionText) {
            int y = 0;
            height = Math.max(height, a.length);
            for (String b : a) {
                width = Math.max(width, b.length());
                for (int x = 0; x < b.length(); x++) {
                    char c = b.charAt(x);
                    if (c == ' ' || c == '-' || c == '+') continue;

                    minCasingCounts.mergeInt(c, 1, Integer::sum);

                    if (c == '~') {
                        if (controllerOffset != null) {
                            throw new IllegalStateException(
                                "Structure definition for " + provider + " contains two tildes");
                        }

                        controllerOffset = new Vec3Impl(x, y, z);
                    }
                }
                y++;
            }
            z++;
        }

        minSize = new Vec3Impl(width, height, length);

        if (controllerOffset == null) {
            throw new IllegalStateException(
                "Structure definition for " + provider
                    + " did not contain a tilde! This is required so that the wrapper knows where the controller is.");
        }
    }

    private void analyzeMaxDefinition() {
        // calculate the max casing counts
        String[][] maxDefinitionText = provider.getMaxDefinition();

        int width = 0;
        int height = 0;
        int length = maxDefinitionText.length;

        for (String[] a : maxDefinitionText) {
            height = Math.max(height, a.length);
            for (String b : a) {
                width = Math.max(width, b.length());
                for (char c : b.toCharArray()) {
                    if (c == ' ' || c == '-' || c == '+' || c == '~') continue;

                    maxCasingCounts.mergeInt(c, 1, Integer::sum);
                }
            }
        }

        maxSize = new Vec3Impl(width, height, length);
    }

    private void ensureStructureLoaded() {
        if (structureDefinition == null) {
            loadStructure();
        }
    }

    // #region Structure checks/building boilerplate

    public IStructureDefinition<MTE> getStructureDefinition() {
        return structureDefinition;
    }

    public Vec3Impl getControllerOffset() {
        return controllerOffset;
    }

    public Vec3Impl getMinSize() {
        return minSize;
    }

    public Vec3Impl getMaxSize() {
        return maxSize;
    }

    public boolean checkStructure(MTE instance) {
        return checkStructure(instance, STRUCTURE_SHAPE_MAIN, null);
    }

    /**
     * Checks if the given piece exists at the given offset. The offset's coordinate system is in multi space, not world
     * space.
     */
    public boolean checkStructure(MTE instance, String piece, Vec3Impl pieceOffset) {
        ensureStructureLoaded();

        if (!GTValues.DEVENV) {
            return checkStructureImpl(instance, piece, pieceOffset);
        } else {
            try {
                return checkStructureImpl(instance, piece, pieceOffset);
            } catch (NoSuchMethodError e) {
                GTMod.GT_FML_LOGGER.info("Caught an exception that was probably caused by a hotswap.", e);

                loadStructure();

                return checkStructureImpl(instance, piece, pieceOffset);
            }
        }
    }

    private boolean checkStructureImpl(MTE instance, String piece, Vec3Impl pieceOffset) {
        final IGregTechTileEntity tTile = instance.getBaseMetaTileEntity();
        return structureDefinition.check(
            instance,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controllerOffset.get0() + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controllerOffset.get1() + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controllerOffset.get2() + (pieceOffset == null ? 0 : pieceOffset.get2()),
            !instance.mMachine);
    }

    public void construct(MTE instance, ItemStack trigger, boolean hintsOnly) {
        construct(instance, trigger, hintsOnly, STRUCTURE_SHAPE_MAIN, null);
    }

    /**
     * Creatively constructs the given piece at the given offset. The offset's coordinate system is in multi space, not
     * world space.
     */
    public void construct(MTE instance, ItemStack trigger, boolean hintsOnly, String piece, Vec3Impl pieceOffset) {
        ensureStructureLoaded();

        if (!GTValues.DEVENV) {
            constructImpl(instance, trigger, hintsOnly, piece, pieceOffset);
        } else {
            try {
                constructImpl(instance, trigger, hintsOnly, piece, pieceOffset);
            } catch (NoSuchMethodError e) {
                GTMod.GT_FML_LOGGER.info("Caught an exception that was probably caused by a hotswap.", e);

                loadStructure();

                constructImpl(instance, trigger, hintsOnly, piece, pieceOffset);
            }
        }
    }

    private void constructImpl(MTE instance, ItemStack trigger, boolean hintsOnly, String piece, Vec3Impl pieceOffset) {
        final IGregTechTileEntity tTile = instance.getBaseMetaTileEntity();
        structureDefinition.buildOrHints(
            instance,
            trigger,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controllerOffset.get0() + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controllerOffset.get1() + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controllerOffset.get2() + (pieceOffset == null ? 0 : pieceOffset.get2()),
            hintsOnly);
    }

    public int survivalConstruct(MTE instance, ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalConstruct(instance, trigger, elementBudget, env, STRUCTURE_SHAPE_MAIN, null);
    }

    /**
     * Survival constructs the given piece at the given offset. The offset's coordinate system is in multi space, not
     * world space.
     */
    public int survivalConstruct(MTE instance, ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env,
        String piece, Vec3Impl pieceOffset) {
        if (instance.mMachine) return -1;

        ensureStructureLoaded();

        if (!GTValues.DEVENV) {
            return survivalConstructImpl(instance, trigger, elementBudget, env, piece, pieceOffset);
        } else {
            try {
                return survivalConstructImpl(instance, trigger, elementBudget, env, piece, pieceOffset);
            } catch (NoSuchMethodError e) {
                GTMod.GT_FML_LOGGER.info("Caught an exception that was probably caused by a hotswap.", e);

                loadStructure();

                return survivalConstructImpl(instance, trigger, elementBudget, env, piece, pieceOffset);
            }
        }
    }

    private int survivalConstructImpl(MTE instance, ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env,
        String piece, Vec3Impl pieceOffset) {
        final IGregTechTileEntity tTile = instance.getBaseMetaTileEntity();
        int built = structureDefinition.survivalBuild(
            instance,
            trigger,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controllerOffset.get0() + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controllerOffset.get1() + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controllerOffset.get2() + (pieceOffset == null ? 0 : pieceOffset.get2()),
            elementBudget,
            env,
            false);

        if (built > 0) instance.checkStructure(true, tTile);

        return built;
    }

    // #endregion

    @SuppressWarnings("unchecked")
    public IStructureElement<MTE> getStructureElement(char c) {
        CasingInfo<MTE> casing = casings.get(c);

        IStructureElement<MTE> element;

        if (casing.elementOverride != null) {
            element = casing.elementOverride.apply(casing.casingGroup);
        } else if (casing.maxHatches != 0) {
            Objects.requireNonNull(casing.casing, "CasingInfo.casing cannot be null");

            element = onElementPass(
                instance -> instance.getStructureInstance()
                    .onCasingEncountered(c),
                casing.casing.asElement(casing));

            IHatchElement<? super MTE>[] hatches = casing.hatches;

            if (casing.casing.isTiered()) {
                // If the casing is tiered, we want to keep track of valid hatches so that we can update their
                // background texture to the correct index after the casing tier is established.
                hatches = GTDataUtils
                    .mapToArray(casing.hatches, IHatchElement[]::new, hatch -> new HatchInterceptor<>(casing, hatch));
            }

            element = HatchElementBuilder.<MTE>builder()
                .atLeast(hatches)
                .casingIndex(casing.casing.getTextureId())
                .hint(casing.dot)
                .buildAndChain(element);
        } else {
            element = casing.casing.asElement(casing);
        }

        if (casing.channel != null) {
            element = casing.channel.use(element);
        }

        if (casing.elementWrapper != null) {
            element = casing.elementWrapper.apply(element);
        }

        return element;
    }

    /**
     * A hatch proxy that adds valid hatches to the structure's pending hatch list.
     */
    static class HatchInterceptor<T> extends GTStructureUtility.ProxyHatchElement<T> {

        private final CasingInfo<T> casing;

        public HatchInterceptor(CasingInfo<T> casing, IHatchElement<? super T> element) {
            super(element);
            this.casing = casing;
        }

        @Override
        public IGTHatchAdder<? super T> adder() {
            IGTHatchAdder<? super T> realAdder = super.adder();

            return (t, hatch, textureId) -> {
                if (realAdder.apply(t, hatch, textureId)) {
                    if (hatch.getMetaTileEntity() instanceof MTEHatch hatch2) {
                        casing.getInstance(t)
                            .addTieredHatch(hatch2, casing.casing, casing);
                    }

                    return true;
                } else {
                    return false;
                }
            };
        }
    }

    public int getCasingMin(char c) {
        CasingInfo<?> casing = casings.get(c);

        if (casing.maxHatches < 0) return 0;

        return minCasingCounts.get(c) - casing.maxHatches;
    }

    public int getCasingMin(ICasing casing) {
        int sum = 0;

        for (var e : casings.char2ObjectEntrySet()) {
            if (e.getValue().casing == casing) {
                sum += getCasingMin(e.getCharKey());
            }
        }

        return sum;
    }

    public int getCasingMax(char c) {
        return maxCasingCounts.get(c);
    }

    public int getCasingMax(ICasing casing) {
        int sum = 0;

        for (var e : casings.char2ObjectEntrySet()) {
            if (e.getValue().casing == casing) {
                sum += getCasingMax(e.getCharKey());
            }
        }

        return sum;
    }

    public CasingBuilder addCasing(char c, @Nonnull ICasing casing) {
        CasingInfo<MTE> casingInfo = new CasingInfo<>();

        casingInfo.casing = casing;
        casingInfo.casingGroup = ICasingGroup.ofCasing(casing);
        casingInfo.instanceExtractor = instanceExtractor;

        casings.put(c, casingInfo);

        return new CasingBuilder(c, casingInfo);
    }

    /**
     * Sets up a builder but doesn't build it. Useful if you need to modify it in some non-standard way.
     */
    public StructureDefinition.Builder<MTE> getStructureBuilder(List<Pair<String, String[][]>> shapes) {
        StructureDefinition.Builder<MTE> builder = StructureDefinition.builder();

        for (char casing : casings.keySet()) {
            builder.addElement(casing, getStructureElement(casing));
        }

        for (var shape : shapes) {
            builder.addShape(shape.left(), shape.right());
        }

        return builder;
    }

    /**
     * Creates a structure definition with a single shape called {@code main}.
     */
    public IStructureDefinition<MTE> buildStructure(String[][] definition) {
        return getStructureBuilder(Arrays.asList(Pair.of(StructureWrapper.STRUCTURE_SHAPE_MAIN, definition))).build();
    }

    @SuppressWarnings("FieldCanBeLocal")
    public class CasingBuilder {

        private final char c;
        private final CasingInfo<MTE> casingInfo;

        public CasingBuilder(char c, CasingInfo<MTE> casingInfo) {
            this.c = c;
            this.casingInfo = casingInfo;
        }

        @SuppressWarnings("unchecked")
        public CasingBuilder withUnlimitedHatches(int dot, List<IHatchElement<? super MTE>> hatches) {
            Objects.requireNonNull(hatches);

            casingInfo.dot = dot;
            casingInfo.maxHatches = -1;
            casingInfo.hatches = hatches.toArray(new IHatchElement[0]);

            return this;
        }

        @SuppressWarnings("unchecked")
        public CasingBuilder withHatches(int dot, int maxHatches, List<IHatchElement<? super MTE>> hatches) {
            Objects.requireNonNull(hatches);

            casingInfo.dot = dot;
            casingInfo.maxHatches = maxHatches;
            casingInfo.hatches = hatches.toArray(new IHatchElement[0]);

            return this;
        }

        public CasingBuilder withChannel(IStructureChannels channel) {
            casingInfo.channel = channel;

            return this;
        }

        public CasingBuilder withElement(IStructureElement<MTE> element) {
            casingInfo.elementOverride = ignored -> element;

            return this;
        }

        public CasingBuilder withElement(Function<ICasingGroup, IStructureElement<MTE>> element) {
            casingInfo.elementOverride = element;

            return this;
        }

        public CasingBuilder wrapElement(Function<IStructureElement<MTE>, IStructureElement<MTE>> wrapper) {
            casingInfo.elementWrapper = wrapper;

            return this;
        }

        public CasingBuilder withCasingGroup(ICasingGroup group) {
            casingInfo.casingGroup = group;

            return this;
        }
    }
}
