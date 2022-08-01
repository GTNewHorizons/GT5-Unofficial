package gregtech.api.interfaces;

import com.gtnewhorizon.structurelib.structure.IStructureElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.IGT_HatchAdder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.ToLongFunction;

public interface IHatchElement<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> {
    List<? extends Class<? extends IMetaTileEntity>> mteClasses();

    IGT_HatchAdder<? super T> adder();

    String name();

    long count(T t);

    default IHatchElement<T> withMteClass(Class<? extends IMetaTileEntity> aClass) {
        if (aClass == null) throw new IllegalArgumentException();
        return withMteClasses(Collections.singletonList(aClass));
    }

    @SuppressWarnings("unchecked") // can't set SafeVarargs :(
    default IHatchElement<T> withMteClasses(Class<? extends IMetaTileEntity>... aClasses) {
        if (aClasses == null) throw new IllegalArgumentException();
        return withMteClasses(Arrays.asList(aClasses));
    }

    default IHatchElement<T> withMteClasses(List<Class<? extends IMetaTileEntity>> aClasses) {
        if (aClasses == null) throw new IllegalArgumentException();
        return new HatchElement<>(aClasses, null, null, null, this);
    }

    default IHatchElement<T> withAdder(IGT_HatchAdder<T> aAdder) {
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
            .build();
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot, BiPredicate<? super T2, ? super IGregTechTileEntity> aShouldSkip) {
        if (aCasingIndex < 0 || aDot < 0 || aShouldSkip == null) throw new IllegalArgumentException();
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .shouldSkip(aShouldSkip)
            .build();
    }
}

class HatchElement<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> implements IHatchElement<T> {
    private final List<Class<? extends IMetaTileEntity>> mClasses;
    private final IGT_HatchAdder<? super T> mAdder;
    private final String mName;
    private final IHatchElement<? super T> mBacking;
    private final ToLongFunction<? super T> mCount;

    public HatchElement(List<Class<? extends IMetaTileEntity>> aMteClasses, IGT_HatchAdder<? super T> aAdder, String aName, ToLongFunction<? super T> aCount, IHatchElement<? super T> aBacking) {
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
    public IHatchElement<T> withMteClasses(List<Class<? extends IMetaTileEntity>> aClasses) {
        if (aClasses == null ) throw new IllegalArgumentException();
        return new HatchElement<>(aClasses, mAdder, mName, mCount, mBacking);
    }

    @Override
    public IHatchElement<T> withAdder(IGT_HatchAdder<T> aAdder) {
        if (aAdder == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, aAdder, mName, mCount, mBacking);
    }

    @Override
    public IHatchElement<T> withName(String aName) {
        if (aName == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, mAdder, aName, mCount, mBacking);
    }

    @Override
    public  <T2 extends T> IHatchElement<T2> withCount(ToLongFunction<T2> aCount) {
        if (aCount == null) throw new IllegalArgumentException();
        return new HatchElement<>(mClasses, mAdder, mName, aCount, mBacking);
    }
}
