package gregtech.api.util;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.MetaTileEntity;

public class UncheckedValidMTEList<T> implements Iterable<T> {

    private final Iterable<T> collection;

    public UncheckedValidMTEList(Iterable<T> collection) {
        this.collection = collection;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new SelfCleaningIterator<>(collection.iterator()) {

            @Override
            protected boolean isValid(T object) {
                return ((MetaTileEntity) object).isValid();
            }
        };
    }
}
