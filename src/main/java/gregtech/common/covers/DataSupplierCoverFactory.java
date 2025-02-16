package gregtech.common.covers;

import java.util.function.Supplier;

import gregtech.api.covers.CoverFactoryBase;
import gregtech.api.util.ISerializableObject;

public class DataSupplierCoverFactory<T extends ISerializableObject> extends CoverFactoryBase<T> {

    private final Supplier<T> supplier;

    public DataSupplierCoverFactory(Supplier<T> dataSupplier) {
        this.supplier = dataSupplier;
    }

    @Override
    public T createDataObject() {
        return supplier.get();
    }
}
