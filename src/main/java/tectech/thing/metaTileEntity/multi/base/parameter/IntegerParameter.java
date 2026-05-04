package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class IntegerParameter extends Parameter<Integer> {

    public IntegerParameter(Integer value, String langKey, Supplier<Integer> min, Supplier<Integer> max) {
        super(value, langKey, min, max);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new IntSyncValue(this::getValue, this::setValue);
    }

    /**
     * Loads the value of the parameter at {@code key} from {@code nbt} into {@code parameterMap}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link IntegerParameter}
     * and be already present.
     */
    public static void loadValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        ((IntegerParameter) parameterMap.get(key)).setValue(nbt.getInteger(key));
    }

    /**
     * Saves the value of the parameter at {@code key} from {@code parameterMap} into {@code nbt}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link IntegerParameter}
     * and be already present.
     */
    public static void saveValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        nbt.setInteger(key, ((IntegerParameter) parameterMap.get(key)).getValue());
    }
}
