package gregtech.api.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.*;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import gnu.trove.TIntCollection;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.common.blocks.GT_Item_Machines;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;

public class GT_HatchElementBuilder<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> {
    private interface Builtin {
    }

    private IGT_HatchAdder<? super T> mAdder;
    private int mCasingIndex = -1;
    private int mDot = -1;
    private BiPredicate<? super T, ? super IGregTechTileEntity> mShouldSkip;
    private Function<? super T, ? extends Predicate<ItemStack>> mHatchItemFilter;
    private Supplier<String> mHatchItemType;
    private Predicate<? super T> mReject, mBuiltinReject;
    private boolean mCacheHint;

    private GT_HatchElementBuilder() {
    }

    public static <T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> GT_HatchElementBuilder<T> builder() {
        return new GT_HatchElementBuilder<>();
    }

    // region composite

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * TODO add doc
     */
    @SafeVarargs
    public final GT_HatchElementBuilder<T> anyOf(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return adder(Arrays.stream(elements).map(e -> e.adder().rebrand()).reduce(IGT_HatchAdder::orElse).get())
            .hatchClasses(Arrays.stream(elements).map(IHatchElement::mteClasses).flatMap(Collection::stream).collect(Collectors.toList()))
            .cacheHint(() -> Arrays.stream(elements).map(IHatchElement::name).collect(Collectors.joining(" or ", "of type ", "")));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * <p>
     * Will rotate through all elements
     * TODO add doc
     */
    @SafeVarargs
    public final GT_HatchElementBuilder<T> atLeast(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return atLeast(Arrays.stream(elements).collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * <p>
     * Will rotate through all elements
     * TODO add doc
     */
    public final GT_HatchElementBuilder<T> atLeastList(List<IHatchElement<? super T>> elements) {
        if (elements == null || elements.isEmpty()) throw new IllegalArgumentException();
        return atLeast(elements.stream().collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * TODO add doc
     */
    public final GT_HatchElementBuilder<T> atLeast(Map<IHatchElement<? super T>, ? extends Number> elements) {
        if (elements == null || elements.isEmpty() || elements.containsKey(null) || elements.containsValue(null))
            throw new IllegalArgumentException();
        List<Class<? extends IMetaTileEntity>> list = elements.keySet().stream().map(IHatchElement::mteClasses).flatMap(Collection::stream).collect(Collectors.toList());
        // map cannot be null or empty, so assert Optional isPresent
        return adder(elements.keySet().stream().map(e -> e.adder().rebrand()).reduce(IGT_HatchAdder::orElse).orElseThrow(AssertionError::new))
            .hatchItemFilter(obj -> GT_StructureUtility.filterByMTEClass(elements.entrySet().stream()
                .filter(entry -> entry.getKey().count(obj) < entry.getValue().longValue())
                .flatMap(entry -> entry.getKey().mteClasses().stream())
                .collect(Collectors.toList())))
            .shouldReject(obj -> elements.entrySet().stream().allMatch(e-> e.getKey().count(obj) >= e.getValue().longValue()))
            .shouldSkip((BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null && list.stream().anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())))
            .cacheHint(() -> elements.keySet().stream().map(IHatchElement::name).collect(Collectors.joining(" or ", "of type ", "")));
    }
    //endregion

    //region primitives

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

	// avoid loooooong lines like
	// shouldSkip((BiPredicate<.....> & Builtin) (c, t) -> ....)
    // private <P extends BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin> GT_HatchElementBuilder<T> shouldSkipInternal(P aShouldSkip) {
    //     if (mShouldSkip == null) mShouldSkip = aShouldSkip;
    //     return this;
    // }
    // turns out javac doesn't like this... so...

    public GT_HatchElementBuilder<T> shouldReject(Predicate<? super T> aShouldReject) {
        if (aShouldReject == null) throw new IllegalArgumentException();
        mReject = aShouldReject;
        return this;
    }

    public GT_HatchElementBuilder<T> hatchItemFilter(Function<? super T, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        mHatchItemFilter = aHatchItemFilter;
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
    // endregion

    // region intermediate
    public GT_HatchElementBuilder<T> hatchClass(Class<? extends IMetaTileEntity> clazz) {
        return hatchItemFilter(c -> is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)))
            .cacheHint(() -> "of class " + clazz.getSimpleName());
    }

    @SafeVarargs
    public final GT_HatchElementBuilder<T> hatchClasses(Class<? extends IMetaTileEntity>... classes) {
        return hatchClasses(Arrays.asList(classes));
    }

    public final GT_HatchElementBuilder<T> hatchClasses(List<? extends Class<? extends IMetaTileEntity>> classes) {
        List<? extends Class<? extends IMetaTileEntity>> list = new ArrayList<>(classes);
        return hatchItemFilter(obj -> GT_StructureUtility.filterByMTEClass(list))
            .cacheHint(() -> list.stream().map(Class::getSimpleName).collect(Collectors.joining(" or ", "of class ", "")))
            .shouldSkip((BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null && list.stream().anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())));
    }

    public GT_HatchElementBuilder<T> hatchId(int aId) {
        return hatchItemFilter(c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines && is.getItemDamage() == aId)
            .cacheHint(() -> "of id " + aId);
    }

    public GT_HatchElementBuilder<T> hatchIds(int... aIds) {
        if (aIds == null || aIds.length == 0) throw new IllegalArgumentException();
        if (aIds.length == 1) return hatchId(aIds[0]);
        TIntCollection coll = aIds.length < 16 ? new TIntArrayList(aIds) : new TIntHashSet(aIds);
        return hatchItemFilter(c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines && coll.contains(is.getItemDamage()))
            .cacheHint(() -> Arrays.stream(coll.toArray()).mapToObj(String::valueOf).collect(Collectors.joining(" or ", "of id ", "")));
    }

    //endregion

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final IStructureElementChain<T> buildAndChain(IStructureElement<T>... elements) {
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
            return new IStructureElementNoPlacement<T>() {
                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    return tileEntity instanceof IGregTechTileEntity && mAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) mCasingIndex);
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), mDot - 1);
                    return true;
                }
            };
        }
        return new IStructureElement<T>() {
            private String mHint = mHatchItemType == null ? "unspecified GT hatch" : mHatchItemType.get();

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && mAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) mCasingIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), mDot);
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
                if (mCacheHint)
                    mHint = tHint;
                return tHint;
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (mShouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity && mShouldSkip.test(t, (IGregTechTileEntity) tileEntity))
                        return PlaceResult.SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor))
                    return PlaceResult.REJECT;
                if (mReject != null && mReject.test(t)) return PlaceResult.REJECT;
                ItemStack taken = s.takeOne(mHatchItemFilter.apply(t), true);
                if (GT_Utility.isStackInvalid(taken)) {
                    String type = getHint();
                    chatter.accept(new ChatComponentTranslation("GT5U.autoplace.error.no_hatch", type));
                    return PlaceResult.REJECT;
                }
                return StructureUtility.survivalPlaceBlock(taken, ItemStackPredicate.NBTMode.IGNORE, null, true, world, x, y, z, s, actor) == PlaceResult.ACCEPT ? PlaceResult.ACCEPT_STOP : PlaceResult.REJECT;
            }
        };
    }
}
