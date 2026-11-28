package gregtech.api.util;

import java.util.Collection;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.MetaTileEntity;

public class ValidMTEList<E extends Collection<T>, T extends MetaTileEntity> implements Iterable<T> {

    private final E collection;

    public ValidMTEList(E collection) {
        this.collection = collection;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new SelfCleaningIterator<>(collection.iterator()) {

            @Override
            protected boolean isValid(T object) {
                return object.isValid();
            }
        };
    }
}
