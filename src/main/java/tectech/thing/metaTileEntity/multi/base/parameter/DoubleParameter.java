package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class DoubleParameter extends NumericParameter<Double> {

    public DoubleParameter(Double value, String langKey, Supplier<Double> min, Supplier<Double> max) {
        super(value, langKey, min, max);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new DoubleSyncValue(this::getValue, this::setValue);
    }

    public double validateValue(double num) {
        return Math.max(min.get(), Math.min(num, max.get()));
    }

    /**
     * Loads the value of the parameter at {@code key} from {@code nbt} into {@code parameterMap}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link DoubleParameter}
     * and be already present.
     */
    public static void loadValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        ((DoubleParameter) parameterMap.get(key)).setValue(nbt.getDouble(key));
    }

    /**
     * Saves the value of the parameter at {@code key} from {@code parameterMap} into {@code nbt}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link DoubleParameter}
     * and be already present.
     */
    public static void saveValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        nbt.setDouble(key, ((DoubleParameter) parameterMap.get(key)).getValue());
    }
}
