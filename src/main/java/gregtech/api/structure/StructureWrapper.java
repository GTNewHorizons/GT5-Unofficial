package gregtech.api.structure;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.coords.ControllerRelativeCoords;
import com.gtnewhorizon.structurelib.coords.Position;
import com.gtnewhorizon.structurelib.coords.StructureDefinitionCoords;
import com.gtnewhorizon.structurelib.coords.WorldCoords;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition.Builder;
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
import gregtech.api.util.GTStructureUtility.ProxyHatchElement;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.CharCharPair;

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

    public Vec3Impl minSize, maxSize;
    public Char2ObjectArrayMap<CasingInfo<MTE>> casings;
    public Char2IntArrayMap minCasingCounts, maxCasingCounts;

    public Function<MTE, IStructureInstance<MTE>> instanceExtractor;

    public final List<CharCharPair> sockets = new ArrayList<>();

    public ControllerPosition controllerPosSource = ControllerPosition.StructureLib;
    public Position<StructureDefinitionCoords> controllerOffset, manualControllerOffset;

    public enum ControllerPosition {
        /// The controller structure offset will come from the wrapper analyzing [IStructureProvider#getDefinition()].
        Wrapper,
        /// The controller structure offset will come from [IStructureDefinition#getControllerPosition(String)].
        StructureLib,
        /// The controller structure offset will be manually specified.
        Manual
    }

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
        } catch (Throwable t) {
            GTMod.GT_FML_LOGGER.error("Could not compile structure", t);
        }
    }

    private void analyzeMinDefinition(String[][] definitionText) {
        // find the controller offset, calculate the multi size, and calculate the min/default casing counts
        int width = 0;
        int height = 0;
        int length = definitionText.length;

        int z = 0;
        for (String[] layer : definitionText) {
            int y = 0;
            height = Math.max(height, layer.length);
            for (String row : layer) {
                width = Math.max(width, row.length());
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c == ' ' || c == '-' || c == '+') continue;

                    minCasingCounts.mergeInt(c, 1, Integer::sum);

                    if (c == '~') {
                        if (controllerOffset != null) {
                            throw new IllegalStateException(
                                "Structure definition for " + provider + " contains two tildes");
                        }

                        controllerOffset = new Position<>(x, y, z);
                    }
                }
                y++;
            }
            z++;
        }

        minSize = new Vec3Impl(width, height, length);
    }

    private void analyzeMaxDefinition() {
        // calculate the max casing counts
        String[][] maxDefinitionText = provider.getMaxDefinition();

        int width = 0;
        int height = 0;
        int length = maxDefinitionText.length;

        for (String[] layer : maxDefinitionText) {
            height = Math.max(height, layer.length);
            for (String row : layer) {
                width = Math.max(width, row.length());
                for (char c : row.toCharArray()) {
                    if (c == ' ' || c == '-' || c == '+' || c == '~') continue;

                    maxCasingCounts.mergeInt(c, 1, Integer::sum);
                }
            }
        }

        maxSize = new Vec3Impl(width, height, length);
    }

    public void ensureStructureLoaded() {
        if (structureDefinition == null) {
            loadStructure();
        }
    }

    // #region Structure checks/building boilerplate

    public IStructureDefinition<MTE> getStructureDefinition() {
        return structureDefinition;
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
        var controller = getControllerPosition(piece);

        return structureDefinition.check(
            instance,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controller.x + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controller.y + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controller.z + (pieceOffset == null ? 0 : pieceOffset.get2()),
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
        loadStructure();
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
        var controller = getControllerPosition(piece);

        structureDefinition.buildOrHints(
            instance,
            trigger,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controller.x + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controller.y + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controller.z + (pieceOffset == null ? 0 : pieceOffset.get2()),
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
        var controller = getControllerPosition(piece);

        int built = structureDefinition.survivalBuild(
            instance,
            trigger,
            piece,
            tTile.getWorld(),
            instance.getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            controller.x + (pieceOffset == null ? 0 : pieceOffset.get0()),
            controller.y + (pieceOffset == null ? 0 : pieceOffset.get1()),
            controller.z + (pieceOffset == null ? 0 : pieceOffset.get2()),
            elementBudget,
            env,
            false);

        if (built > 0) instance.checkStructure(true, tTile);

        return built;
    }

    public Position<StructureDefinitionCoords> getControllerPosition(String piece) {
        return switch (this.controllerPosSource) {
            case Wrapper -> new Position<>(controllerOffset);
            case StructureLib -> structureDefinition.getControllerPosition(piece);
            case Manual -> manualControllerOffset;
        };
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
                .dot(casing.dot)
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
    static class HatchInterceptor<T> extends ProxyHatchElement<T> {

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

    public void addSocket(char name, char replacement) {
        sockets.add(CharCharPair.of(name, replacement));
    }

    public void useWrapperControllerPosition() {
        this.controllerPosSource = ControllerPosition.Wrapper;
    }

    public void useManualControllerPosition(int x, int y, int z) {
        this.controllerPosSource = ControllerPosition.Manual;
        this.manualControllerOffset = new Position<>(x, y, z);
    }

    /**
     * Sets up a builder but doesn't build it. Useful if you need to modify it in some non-standard way.
     */
    public Builder<MTE> getStructureBuilder(List<Pair<String, String[][]>> shapes) {
        Builder<MTE> builder = StructureDefinition.builder();

        for (char casing : casings.keySet()) {
            builder.addElement(casing, getStructureElement(casing));
        }

        for (var shape : shapes) {
            builder.addShape(shape.left(), shape.right());
        }

        for (var socket : sockets) {
            builder.addSocket(socket.leftChar(), socket.rightChar());
        }

        return builder;
    }

    /**
     * Creates a structure definition with a single shape called {@code main}.
     */
    public IStructureDefinition<MTE> buildStructure(String[][] definition) {
        return getStructureBuilder(Arrays.asList(Pair.of(StructureWrapper.STRUCTURE_SHAPE_MAIN, definition))).build();
    }

    public Position<WorldCoords> getSocket(MTE mte, String piece, char name) {
        ensureStructureLoaded();

        IGregTechTileEntity igte = mte.getBaseMetaTileEntity();
        Objects.requireNonNull(igte, "Base tile must be valid: " + mte);

        var p1 = structureDefinition.getSocket(piece, name);
        var p2 = structureDefinition.getCoordinateSystem(piece)
            .translateInverse(p1);
        var p3 = mte.getExtendedFacing()
            .asCoordinateSystem()
            .translateInverse(p2);
        var p4 = ControllerRelativeCoords.translateInverse(p3, igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        return p4;
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
