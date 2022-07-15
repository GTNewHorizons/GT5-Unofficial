package gregtech.api.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import gnu.trove.TIntCollection;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.common.blocks.GT_Item_Machines;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;

public class HatchElementBuilder<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> {
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
    private Block mFallbackBlock;
    private int mFallbackMeta = -1;

    private HatchElementBuilder() {
    }

    public static <T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> HatchElementBuilder<T> builder() {
        return new HatchElementBuilder<>();
    }

    // region composite

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * TODO add doc
     */
    @SafeVarargs
    public final HatchElementBuilder<T> anyOf(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return adder(Arrays.stream(elements).map(IHatchElement::adder).reduce(IGT_HatchAdder::orElse).get())
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
    public final HatchElementBuilder<T> atLeast(IHatchElement<? super T>... elements) {
        if (elements == null || elements.length == 0) throw new IllegalArgumentException();
        return atLeast(Arrays.stream(elements).collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())));
    }

    /**
     * Set all of adder, hint and hatchItemFilter. Provide a reasonable default for shouldSkip.
     * TODO add doc
     */
    public final HatchElementBuilder<T> atLeast(Map<IHatchElement<? super T>, ? extends Number> elements) {
        if (elements == null || elements.isEmpty() || elements.containsKey(null) || elements.containsValue(null))
            throw new IllegalArgumentException();
        List<Class<? extends IMetaTileEntity>> list = elements.keySet().stream().map(IHatchElement::mteClasses).flatMap(Collection::stream).collect(Collectors.toList());
        // map cannot be null or empty, so assert Optional isPresent
        return adder(elements.keySet().stream().map(IHatchElement::adder).reduce(IGT_HatchAdder::orElse).orElseThrow(AssertionError::new))
            .hatchItemFilter(obj -> GT_StructureUtility.filterByMTEClass(elements.entrySet().stream()
                .filter(entry -> entry.getKey().count(obj) < entry.getValue().longValue())
                .flatMap(entry -> entry.getKey().mteClasses().stream())
                .collect(Collectors.toList())))
            .shouldSkipInternal((BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null && list.stream().anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())))
            .cacheHint(() -> elements.keySet().stream().map(IHatchElement::name).collect(Collectors.joining(" or ", "of type ", "")));
    }
    //endregion

    //region primitives

    public HatchElementBuilder<T> adder(IGT_HatchAdder<? super T> aAdder) {
        if (mAdder != null) throw new IllegalStateException();
        if (aAdder == null) throw new IllegalArgumentException();
        mAdder = aAdder;
        return this;
    }

    public HatchElementBuilder<T> casingIndex(int aCasingIndex) {
        if (mCasingIndex != -1) throw new IllegalStateException();
        if (aCasingIndex <= 0) throw new IllegalArgumentException();
        mCasingIndex = aCasingIndex;
        return this;
    }

    public HatchElementBuilder<T> dot(int aDot) {
        if (mDot != -1) throw new IllegalStateException();
        if (aDot <= 0) throw new IllegalArgumentException();
        mDot = aDot;
        return this;
    }

    public HatchElementBuilder<T> shouldSkip(BiPredicate<? super T, ? super IGregTechTileEntity> aShouldSkip) {
        if (!(aShouldSkip instanceof Builtin) || mShouldSkip != null) {
            if (!(mShouldSkip instanceof Builtin) && mShouldSkip != null) throw new IllegalStateException();
            if (aShouldSkip == null) throw new IllegalArgumentException();
        }
        mShouldSkip = aShouldSkip;
        return this;
    }

    /*
	avoid loooooong lines like
	shouldSkip((BiPredicate<.....> & Builtin) (c, t) -> ....)
	 */
    private <P extends BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin> HatchElementBuilder<T> shouldSkipInternal(P aShouldSkip) {
        if (mShouldSkip == null) mShouldSkip = aShouldSkip;
        return this;
    }

    public HatchElementBuilder<T> shouldReject(Predicate<? super T> aShouldReject) {
        if (mReject != null) throw new IllegalStateException();
        if (aShouldReject == null) throw new IllegalArgumentException();
        mReject = aShouldReject;
        return this;
    }

    public HatchElementBuilder<T> hatchItemFilter(Function<? super T, ? extends Predicate<ItemStack>> aHatchItemFilter) {
        if (mHatchItemFilter != null) throw new IllegalStateException();
        if (aHatchItemFilter == null) throw new IllegalArgumentException();
        mHatchItemFilter = aHatchItemFilter;
        return this;
    }

    /**
     * convenience method for ofChain(builder().......build(), ofBlock(aFallbackBlock, aFallbackMeta))
     */
    public HatchElementBuilder<T> fallback(Block aFallbackBlock, int aFallbackMeta) {
        if (mFallbackBlock != null) throw new IllegalStateException();
        if (aFallbackBlock == null || aFallbackMeta < 0) throw new IllegalArgumentException();
        mFallbackBlock = aFallbackBlock;
        mFallbackMeta = aFallbackMeta;
        return this;
    }

    // region hint
    public HatchElementBuilder<T> hint(Supplier<String> aSupplier) {
        if (aSupplier == null) throw new IllegalArgumentException();
        mHatchItemType = aSupplier;
        mCacheHint = false;
        return this;
    }

    public HatchElementBuilder<T> cacheHint(Supplier<String> aSupplier) {
        if (aSupplier == null) throw new IllegalArgumentException();
        mHatchItemType = aSupplier;
        mCacheHint = true;
        return this;
    }

    public HatchElementBuilder<T> cacheHint() {
        if (mHatchItemType == null) throw new IllegalStateException();
        mCacheHint = true;
        return this;
    }
    // endregion
    // endregion

    // region intermediate
    public HatchElementBuilder<T> hatchClass(Class<? extends IMetaTileEntity> clazz) {
        return hatchItemFilter(c -> is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)))
            .hint(() -> "of class " + clazz.getSimpleName());
    }

    @SafeVarargs
    public final HatchElementBuilder<T> hatchClasses(Class<? extends IMetaTileEntity>... classes) {
        return hatchClasses(Arrays.asList(classes));
    }

    public final HatchElementBuilder<T> hatchClasses(List<? extends Class<? extends IMetaTileEntity>> classes) {
        List<? extends Class<? extends IMetaTileEntity>> list = new ArrayList<>(classes);
        return hatchItemFilter(obj -> GT_StructureUtility.filterByMTEClass(list))
            .cacheHint(() -> list.stream().map(Class::getSimpleName).collect(Collectors.joining(" or ", "of class ", "")))
            .shouldSkipInternal((BiPredicate<? super T, ? super IGregTechTileEntity> & Builtin) (c, t) -> t != null && list.stream().anyMatch(clazz -> clazz.isInstance(t.getMetaTileEntity())));
    }

    public HatchElementBuilder<T> hatchId(int aId) {
        return hatchItemFilter(c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines && is.getItemDamage() == aId)
            .cacheHint(() -> "of id " + aId);
    }

    public HatchElementBuilder<T> hatchIds(int... aIds) {
        if (aIds == null || aIds.length == 0) throw new IllegalArgumentException();
        if (aIds.length == 1) return hatchId(aIds[0]);
        TIntCollection coll = aIds.length < 16 ? new TIntArrayList(aIds) : new TIntHashSet(aIds);
        return hatchItemFilter(c -> is -> GT_Utility.isStackValid(is) && is.getItem() instanceof GT_Item_Machines && coll.contains(is.getItemDamage()))
            .cacheHint(() -> Arrays.stream(coll.toArray()).mapToObj(String::valueOf).collect(Collectors.joining(" or ", "of id ", "")));
    }

    //endregion

    public IStructureElement<T> build() {
        IStructureElement<T> ret = buildMain();
        if (mFallbackBlock != null)
            return ofChain(ret, ofBlock(mFallbackBlock, mFallbackMeta));
        return ret;
    }

    public IStructureElement<T> buildMain() {
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
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor) {
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
                    actor.addChatMessage(new ChatComponentText("Suggested to have " + type + " but none was found"));
                    return PlaceResult.REJECT;
                }
                return StructureUtility.survivalPlaceBlock(taken, ItemStackPredicate.NBTMode.IGNORE, null, true, world, x, y, z, s, actor) == PlaceResult.ACCEPT ? PlaceResult.ACCEPT_STOP : PlaceResult.REJECT;
            }
        };
    }
}
