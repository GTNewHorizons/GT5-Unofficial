package gregtech.api.util;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementChain;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;

import gnu.trove.TIntCollection;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_HatchElementBuilder<T> {

    private interface Builtin {
    }

    private IGT_HatchAdder<? super T> mAdder;
    private int mCasingIndex = -1;
    private int mDot = -1;
    private BiPredicate<? super T, ? super IGregTechTileEntity> mShouldSkip;
    private BiFunction<? super T, ItemStack, ? extends Predicate<ItemStack>> mHatchItemFilter;
    private Supplier<String> mHatchItemType;
    private Predicate<? super T> mReject;
    private boolean mCacheHint;
    private boolean mNoStop;
    private boolean mExclusive;
    private EnumSet<ForgeDirection> mDisallowedDirection = EnumSet.noneOf(ForgeDirection.class);

    private GT_HatchElementBuilder() {}

    public static <T> GT_HatchElementBuilder<T> builder() {
        return new GT_HatchElementBuilder<>();
    }

    // region composite

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip. TODO add doc
     */
    @SafeVarargs
    public final GT_HatchElementBuilder<T> anyOf(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return adder(
            Arrays.stream(elements)
                .map(
                    e -> e.adder()
                        .rebrand())
                .reduce(IGT_HatchAdder::orElse)
                .get()).hatchClasses(
                    Arrays.stream(elements)
                        .map(IHatchElement::mteClasses)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                    .cacheHint(
                        () -> Arrays.stream(elements)
                            .map(IHatchElement::name)
                            .sorted()
                            .collect(Collectors.joining(" or ", "of type ", "")));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * <p>
     * Will rotate through all elements TODO add doc
     */
    @SafeVarargs
    public final GT_HatchElementBuilder<T> atLeast(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return atLeast(
            Arrays.stream(elements)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * <p>
     * Will rotate through all elements TODO add doc
     */
    public final GT_HatchElementBuilder<T> atLeastList(List<IHatchElement<? super T>> elements) {
        if (elements == null || elements.isEmpty()) throw new IllegalArgumentException();
        return atLeast(
            elements.stream()
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip. TODO add doc
     */
    public final GT_HatchElementBuilder<T> atLeast(Map<IHatchElement<? super T>, ? extends Number> elements) {
        if (elements == null || elements.isEmpty() || elements.containsKey(null) || elements.containsValue(null))
            throw new IllegalArgumentException();
        List<Class<? extends IMetaTileEntity>> list = elements.keySet()
            .stream()
            .map(IHatchElement::mteClasses)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        // map cannot be null or empty, so assert Optional isPresent
        return adder(
            elements.keySet()
                .stream()
                .map(
                    e -> e.adder()
                        .rebrand())
                .reduce(IGT_HatchAdder::orElse)
                .orElseThrow(AssertionError::new))
                    .hatchItemFilter(
                        obj -> GT_StructureUtility.filterByMTEClass(
                            elements.entrySet()
                                .stream()
                                .filter(
                                    entry -> entry.getKey()
                                        .count(obj)
                                        < entry.getValue()
                                            .longValue())
                                .flatMap(
                                    entry -> entry.getKey()
                                        .mteClasses()
                                        .stream())
                                .collect(Collectors.toList())))
                    .shouldReject(
                        obj -> elements.entrySet()
                            .stream()
                            .allMatch(
                                e -> e.getKey()
                                    .count(obj)
                                    >= e.getValue()
                                        .longValue()))
                    .shouldSkip(
                        (BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c,
                            t) -> t != null && list.stream()
                                .anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())))
                    .cacheHint(
                        () -> elements.keySet()
                            .stream()
                            .map(IHatchElement::name)
                            .sorted()
                            .collect(Collectors.joining(" or ", "of type ", "")));
    }
    // endregion

    // region primitives

    /**
     * Mark this hatch element as the only candidate of given structure element. (e.g. muffler hatch on top of EBF)
     * Currently, this will make the built IStructureElement to ignore gt_no_hatch directive from player
     *
     * Do note that {@link #buildAndChain(IStructureElement[])} and its overloads will force the resulting structure
     * element
     * to be non-exclusive.
     */
    public GT_HatchElementBuilder<T> exclusive() {
        mExclusive = true;
        return this;
    }

    public GT_HatchElementBuilder<T> adder(IGT_HatchAdder<? super T> aAdder) {
        if (aAdder == null) throw new IllegalArgumentException();
        mAdder = aAdder;
        return this;
    }

    public GT_HatchElementBuilder<T> casingIndex(int aCasingIndex) {
        if (aCasingIndex <= 0) throw new IllegalArgumentException();
        mCasingIndex = aCasingIndex;
        return this;
    }

    public GT_HatchElementBuilder<T> dot(int aDot) {
        if (aDot <= 0) throw new IllegalArgumentException();
        mDot = aDot;
        return this;
    }

    public GT_HatchElementBuilder<T> shouldSkip(BiPredicate<? super T, ? super IGregTechTileEntity> aShouldSkip) {
        if (!(aShouldSkip instanceof Builtin) || mShouldSkip != null) {
            if (!(mShouldSkip instanceof Builtin) && mShouldSkip != null) throw new IllegalStateException();
            if (aShouldSkip == null) throw new IllegalArgumentException();
        }
        mShouldSkip = aShouldSkip;
        return this;
    }

    public GT_HatchElementBuilder<T> shouldReject(Predicate<? super T> aShouldReject) {
        if (aShouldReject == null) throw new IllegalArgumentException();
        mReject = aShouldReject;
        return this;
    }

    public GT_HatchElementBuilder<T> hatchItemFilter(
        Function<? super T, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        mHatchItemFilter = (t, s) -> aHatchItemFilter.apply(t);
        return this;
    }

    public GT_HatchElementBuilder<T> hatchItemFilterAnd(
        Function<? super T, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        BiFunction<? super T, ItemStack, ? extends Predicate<ItemStack>> tOldFilter = mHatchItemFilter;
        mHatchItemFilter = (t, s) -> tOldFilter.apply(t, s)
            .and(aHatchItemFilter.apply(t));
        return this;
    }

    public GT_HatchElementBuilder<T> hatchItemFilter(
        BiFunction<? super T, ItemStack, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        mHatchItemFilter = aHatchItemFilter;
        return this;
    }

    public GT_HatchElementBuilder<T> hatchItemFilterAnd(
        BiFunction<? super T, ItemStack, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        BiFunction<? super T, ItemStack, ? extends Predicate<ItemStack>> tOldFilter = mHatchItemFilter;
        mHatchItemFilter = (t, s) -> tOldFilter.apply(t, s)
            .and(aHatchItemFilter.apply(t, s));
        return this;
    }

    // region hint
    public GT_HatchElementBuilder<T> hint(Supplier<String> aSupplier) {
        if (aSupplier == null) throw new IllegalArgumentException();
        mHatchItemType = aSupplier;
        mCacheHint = false;
        return this;
    }

    public GT_HatchElementBuilder<T> cacheHint(Supplier<String> aSupplier) {
        if (aSupplier == null) throw new IllegalArgumentException();
        mHatchItemType = aSupplier;
        mCacheHint = true;
        return this;
    }

    public GT_HatchElementBuilder<T> cacheHint() {
        if (mHatchItemType == null) throw new IllegalStateException();
        mCacheHint = true;
        return this;
    }
    // endregion

    public GT_HatchElementBuilder<T> continueIfSuccess() {
        mNoStop = true;
        return this;
    }

    public GT_HatchElementBuilder<T> stopIfSuccess() {
        mNoStop = false;
        return this;
    }

    /**
     * Help automatic hatch side determination code by ruling out some directions. Note the automatic hatch side
     * determination code will choose to use the default facing if the final allowed facing set is empty.
     * <p>
     * This will clear the sides set by previous call to this or {@link #allowOnly(ForgeDirection...)}
     * <p>
     * Usually mandatory for multis with multiple slices, and otherwise not needed if it contains a single slice only.
     *
     * @param facings disallowed direction in ABC coordinate system
     */
    public GT_HatchElementBuilder<T> disallowOnly(ForgeDirection... facings) {
        if (facings == null) throw new IllegalArgumentException();
        mDisallowedDirection = EnumSet.copyOf(Arrays.asList(facings));
        return this;
    }

    /**
     * Help automatic hatch side determination code by allowing only some directions. Note the automatic hatch side
     * determination code will choose to use the default facing if the final allowed facing set is empty.
     * <p>
     * This will clear the sides set by previous call to this or {@link #disallowOnly(ForgeDirection...)}
     * <p>
     * Usually mandatory for multis with multiple slices, and otherwise not needed if it contains a single slice only.
     *
     * @param facings allowed direction in ABC coordinate system
     */
    public GT_HatchElementBuilder<T> allowOnly(ForgeDirection... facings) {
        if (facings == null) throw new IllegalArgumentException();
        mDisallowedDirection = EnumSet.complementOf(EnumSet.copyOf(Arrays.asList(facings)));
        mDisallowedDirection.remove(ForgeDirection.UNKNOWN);
        return this;
    }
    // endregion

    // region intermediate
    public GT_HatchElementBuilder<T> hatchClass(Class<? extends IMetaTileEntity> clazz) {
        return hatchItemFilter(c -> is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)))
            .cacheHint(() -> "of class " + clazz.getSimpleName())
            .shouldSkip(
                (BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> clazz
                    .isInstance(t.getMetaTileEntity()));
    }

    @SafeVarargs
    public final GT_HatchElementBuilder<T> hatchClasses(Class<? extends IMetaTileEntity>... classes) {
        return hatchClasses(Arrays.asList(classes));
    }

    public final GT_HatchElementBuilder<T> hatchClasses(List<? extends Class<? extends IMetaTileEntity>> classes) {
        List<? extends Class<? extends IMetaTileEntity>> list = new ArrayList<>(classes);
        return hatchItemFilter(obj -> GT_StructureUtility.filterByMTEClass(list)).cacheHint(
            () -> list.stream()
                .map(Class::getSimpleName)
                .sorted()
                .collect(Collectors.joining(" or ", "of class ", "")))
            .shouldSkip(
                (BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null && list.stream()
                    .anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())));
    }

    public GT_HatchElementBuilder<T> hatchId(int aId) {
        return hatchItemFilter(
            c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines
                && is.getItemDamage() == aId).cacheHint(() -> "of id " + aId)
                    .shouldSkip(
                        (BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null
                            && t.getMetaTileID() == aId);
    }

    public GT_HatchElementBuilder<T> hatchIds(int... aIds) {
        if (aIds == null || aIds.length == 0) throw new IllegalArgumentException();
        if (aIds.length == 1) return hatchId(aIds[0]);
        TIntCollection coll = aIds.length < 16 ? new TIntArrayList(aIds) : new TIntHashSet(aIds);
        return hatchItemFilter(
            c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines
                && coll.contains(is.getItemDamage())).cacheHint(
                    () -> Arrays.stream(coll.toArray())
                        .sorted()
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" or ", "of id ", "")))
                    .shouldSkip(
                        (BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null
                            && coll.contains(t.getMetaTileID()));
    }

    // endregion

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final IStructureElementChain<T> buildAndChain(IStructureElement<T>... elements) {
        // just in case
        mExclusive = false;
        List<IStructureElement<T>> l = new ArrayList<>();
        l.add(build());
        l.addAll(Arrays.asList(elements));
        IStructureElement<T>[] array = l.toArray(new IStructureElement[0]);
        return () -> array;
    }

    public final IStructureElementChain<T> buildAndChain(Block block, int meta) {
        return buildAndChain(ofBlock(block, meta));
    }

    public IStructureElement<T> build() {
        if (mAdder == null || mCasingIndex == -1 || mDot == -1) {
            throw new IllegalArgumentException();
        }
        if (mHatchItemFilter == null) {
            // no item filter -> no placement
            return new IStructureElementNoPlacement<>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    return tileEntity instanceof IGregTechTileEntity
                        && mAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) mCasingIndex);
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), mDot - 1);
                    return true;
                }
            };
        }
        return new IStructureElement<>() {

            private String mHint = mHatchItemType == null ? "unspecified GT hatch" : mHatchItemType.get();

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity
                    && mAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) mCasingIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), mDot - 1);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int i, int i1, int i2, ItemStack itemStack) {
                // TODO
                return false;
            }

            private String getHint() {
                if (mHint != null) return mHint;
                String tHint = mHatchItemType.get();
                if (tHint == null) return "?";
                // TODO move this to some .lang instead of half ass it into the crappy gt lang file
                tHint = GT_LanguageManager.addStringLocalization("Hatch_Type_" + tHint.replace(' ', '_'), tHint);
                if (mCacheHint) {
                    mHint = tHint;
                    if (mHint != null)
                        // yeet the getter, since its product is retrieved and cached
                        mHatchItemType = null;
                }
                return tHint;
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(mHatchItemFilter.apply(t, trigger));
            }

            @Deprecated
            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (mShouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity
                        && mShouldSkip.test(t, (IGregTechTileEntity) tileEntity)) return PlaceResult.SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor()))
                    return PlaceResult.REJECT;
                if (mReject != null && mReject.test(t)) return PlaceResult.REJECT;
                if (ChannelDataAccessor.hasSubChannel(trigger, "gt_no_hatch") && !mExclusive) {
                    String type = getHint();
                    env.getChatter()
                        .accept(new ChatComponentTranslation("GT5U.autoplace.error.no_hatch", type));
                    return PlaceResult.REJECT;
                }
                ItemStack taken = env.getSource()
                    .takeOne(mHatchItemFilter.apply(t, trigger), true);
                if (GT_Utility.isStackInvalid(taken)) {
                    String type = getHint();
                    env.getChatter()
                        .accept(new ChatComponentTranslation("GT5U.autoplace.error.no_hatch", type));
                    return PlaceResult.REJECT;
                }
                if (StructureUtility.survivalPlaceBlock(
                    taken,
                    ItemStackPredicate.NBTMode.IGNORE,
                    null,
                    true,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor()) != PlaceResult.ACCEPT) {
                    return PlaceResult.REJECT;
                }
                // try to infer facing
                EnumSet<ForgeDirection> allowed = EnumSet.noneOf(ForgeDirection.class);
                // first find which face of block is not contained in structure
                if (env.getAPILevel() == AutoPlaceEnvironment.APILevel.Legacy) {
                    // a legacy decorator isn't passing down necessary information
                    // in that case, we just assume all facing is allowed
                    allowed.addAll(Arrays.asList(ForgeDirection.VALID_DIRECTIONS));
                } else {
                    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                        // as noted on getWorldDirection Y axis should be flipped before use
                        if (env.isContainedInPiece(direction.offsetX, -direction.offsetY, direction.offsetZ)) continue;
                        // explicitly rejected, probably obstructed by another slice
                        if (mDisallowedDirection.contains(direction)) continue;
                        ForgeDirection rotated = env.getFacing()
                            .getWorldDirection(
                                (direction.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0
                                    ? direction.getOpposite()
                                    : direction);
                        allowed.add(rotated);
                    }
                }
                if (!allowed.isEmpty()) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity) {
                        ForgeDirection result = null;
                        // find the first facing available, but prefer a facing that isn't up/down
                        for (ForgeDirection facing : allowed) {
                            result = facing;
                            if ((facing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0) break; // Horizontal
                        }
                        assert result != null;
                        ((IGregTechTileEntity) tileEntity).setFrontFacing(result);
                    }
                }
                return mNoStop ? PlaceResult.ACCEPT : PlaceResult.ACCEPT_STOP;
            }
        };
    }
}
