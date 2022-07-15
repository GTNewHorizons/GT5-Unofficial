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

public interface IHatchElement<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> {
    List<? extends Class<? extends IMetaTileEntity>> mteClasses();

    IGT_HatchAdder<T> adder();

    String name();

    long count(T t);

    default IHatchElement<T> withMteClass(Class<? extends IMetaTileEntity> classes) {
        return withMteClasses(Collections.singletonList(classes));
    }

    @SuppressWarnings("unchecked") // can't set SafeVarargs :(
    default IHatchElement<T> withMteClasses(Class<? extends IMetaTileEntity>... classes) {
        return withMteClasses(Arrays.asList(classes));
    }

    default IHatchElement<T> withMteClasses(List<Class<? extends IMetaTileEntity>> classes) {
        return new HatchElement<>(classes, null, null, this);
    }

    default IHatchElement<T> withAdder(IGT_HatchAdder<T> adder) {
        return new HatchElement<>(null, adder, null, this);
    }

    default IHatchElement<T> withName(String name) {
        return new HatchElement<>(null, null, name, this);
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot) {
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .build();
    }

    default <T2 extends T> IStructureElement<T2> newAny(int aCasingIndex, int aDot, BiPredicate<? super T2, ? super IGregTechTileEntity> aShouldSkip) {
        return GT_StructureUtility.<T2>buildHatchAdder()
            .anyOf(this)
            .casingIndex(aCasingIndex)
            .dot(aDot)
            .shouldSkip(aShouldSkip)
            .build();
    }
}

class HatchElement<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> implements IHatchElement<T> {
    private final List<Class<? extends IMetaTileEntity>> classes;
    private final IGT_HatchAdder<T> adder;
    private final String name;
    private final IHatchElement<T> backing;

    public HatchElement(List<Class<? extends IMetaTileEntity>> mteClasses, IGT_HatchAdder<T> adder, String name, IHatchElement<T> backing) {
        this.classes = mteClasses;
        this.adder = adder;
        this.name = name;
        this.backing = backing;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return classes == null ? backing.mteClasses() : classes;
    }

    @Override
    public IGT_HatchAdder<T> adder() {
        return adder == null ? backing.adder() : adder;
    }

    @Override
    public String name() {
        return name == null ? backing.name() : name;
    }

    @Override
    public long count(T t) {
        return backing.count(t);
    }

    @Override
    public IHatchElement<T> withMteClasses(List<Class<? extends IMetaTileEntity>> classes) {
        return new HatchElement<>(classes, adder, name, backing);
    }

    @Override
    public IHatchElement<T> withAdder(IGT_HatchAdder<T> adder) {
        return new HatchElement<>(classes, adder, name, backing);
    }

    @Override
    public IHatchElement<T> withName(String name) {
        return new HatchElement<>(classes, adder, name, backing);
    }
}
