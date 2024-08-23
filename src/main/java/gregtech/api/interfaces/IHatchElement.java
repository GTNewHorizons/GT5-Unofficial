package gregtech.api.interfaces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.ToLongFunction;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.IGT_HatchAdder;

public interface IHatchElement<T> {

    List<? extends Class<? extends IMetaTileEntity>> mteClasses();

    IGT_HatchAdder<? super T> adder();

    String name();

    long count(T t);

    default <T2 extends T> IHatchElement<T2> withMteClass(Class<? extends IMetaTileEntity> aClass) {
        if (aClass == null) throw new IllegalArgumentException();
        return withMteClasses(Collections.singletonList(aClass));
    }

    @SuppressWarnings("unchecked") // can't set SafeVarargs :(
    default <T2 extends T> IHatchElement<T2> withMteClasses(Class<? extends IMetaTileEntity>... aClasses) {
        if (aClasses == null) throw new IllegalArgumentException();
        return withMteClasses(Arrays.asList(aClasses));
    }

    default <T2 extends T> IHatchElement<T2> withMteClasses(List<Class<? extends IMetaTileEntity>> aClasses) {
        if (aClasses == null) throw new IllegalArgumentException();
        return new HatchElement<>(aClasses, null, null, null, this);
    }

    default <T2 extends T> IHatchElement<T2> withAdder(IGT_HatchAdder<T2> aAdder) {
        if (aAdder == null) throw new IllegalArgumentException();
        return new HatchElement<>(null, aAdder, null, null, this);
    }

    default IHatchElement<T> withName(String aName) {
        if (aName == null) throw new IllegalArgumentException();
        return new HatchElement<>(null, null, aName, null, this);
    }

    default <T2 extends T> IHatchElement<T2> withCount(ToLongFunction<T2> aCount) {
        if (aCount == null) throw new IllegalArgumentException();
        return new HatchElement<>(null, null, null, aCount, this);
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot) {
        if (aCasingIndex < 0 || aDot < 0) throw new IllegalArgumentException();
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .continueIfSuccess()
            .exclusive()
            .build();
    }

    default <T2 extends T> IStructureElement<T2> newAnyOrCasing(int aCasingIndex, int aDot, Block casingBlock,
        int casingMeta) {
        if (aCasingIndex < 0 || aDot < 0) throw new IllegalArgumentException();
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .continueIfSuccess()
            .buildAndChain(StructureUtility.ofBlock(casingBlock, casingMeta));
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot, ForgeDirection... allowedFacings) {
        if (aCasingIndex < 0 || aDot < 0) throw new IllegalArgumentException();
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .continueIfSuccess()
            .allowOnly(allowedFacings)
            .exclusive()
            .build();
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot,
        BiPredicate<? super T2, ? super IGregTechTileEntity> aShouldSkip) {
        if (aCasingIndex < 0 || aDot < 0 || aShouldSkip == null) throw new IllegalArgumentException();
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .shouldSkip(aShouldSkip)
            .continueIfSuccess()
            .build();
    }

    default <T2 extends T> IHatchElement<T2> or(IHatchElement<? super T2> fallback) {
        return new HatchElementEither<>(this, fallback);
    }
}

class HatchElementEither<T> implements IHatchElement<T> {

    private final IHatchElement<? super T> first, second;
    private ImmutableList<? extends Class<? extends IMetaTileEntity>> mMteClasses;
    private String name;

    HatchElementEither(IHatchElement<? super T> first, IHatchElement<? super T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        if (mMteClasses == null) mMteClasses = ImmutableList.<Class<? extends IMetaTileEntity>>builder()
            .addAll(first.mteClasses())
            .addAll(second.mteClasses())
            .build();
        return mMteClasses;
    }

    @Override
    public IGT_HatchAdder<? super T> adder() {
        return ((t, te, i) -> first.adder()
            .apply(t, te, i)
            || second.adder()
                .apply(t, te, i));
    }

    @Override
    public String name() {
        if (name == null) name = first.name() + " or " + second.name();
        return name;
    }

    @Override
    public long count(T t) {
        return first.count(t) + second.count(t);
    }
}

class HatchElement<T> implements IHatchElement<T> {

    private final List<Class<? extends IMetaTileEntity>> mClasses;
    private final IGT_HatchAdder<? super T> mAdder;
    private final String mName;
    private final IHatchElement<? super T> mBacking;
    private final ToLongFunction<? super T> mCount;

    public HatchElement(List<Class<? extends IMetaTileEntity>> aMteClasses, IGT_HatchAdder<? super T> aAdder,
        String aName, ToLongFunction<? super T> aCount, IHatchElement<? super T> aBacking) {
        this.mClasses = aMteClasses;
        this.mAdder = aAdder;
        this.mName = aName;
        this.mCount = aCount;
        this.mBacking = aBacking;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mClasses == null ? mBacking.mteClasses() : mClasses;
    }

    @Override
    public IGT_HatchAdder<? super T> adder() {
        return mAdder == null ? mBacking.adder() : mAdder;
    }

    @Override
    public String name() {
        return mName == null ? mBacking.name() : mName;
    }

    @Override
    public long count(T t) {
        return mCount == null ? mBacking.count(t) : mCount.applyAsLong(t);
    }

    @Override
    public <T2 extends T> IHatchElement<T2> withMteClasses(List<Class<? extends IMetaTileEntity>> aClasses) {
        if (aClasses == null) throw new IllegalArgumentException();
        return new HatchElement<>(aClasses, mAdder, mName, mCount, mBacking);
    }

    @Override
    public <T2 extends T> IHatchElement<T2> withAdder(IGT_HatchAdder<T2> aAdder) {
        if (aAdder == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, aAdder, mName, mCount, mBacking);
    }

    @Override
    public IHatchElement<T> withName(String aName) {
        if (aName == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, mAdder, aName, mCount, mBacking);
    }

    @Override
    public <T2 extends T> IHatchElement<T2> withCount(ToLongFunction<T2> aCount) {
        if (aCount == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, mAdder, mName, aCount, mBacking);
    }
}
