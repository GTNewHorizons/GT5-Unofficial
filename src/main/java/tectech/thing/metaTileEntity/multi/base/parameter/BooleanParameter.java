package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class BooleanParameter extends Parameter<Boolean> {

    public BooleanParameter(Boolean value, String langKey) {
        super(value, langKey);
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new BooleanSyncValue(this::getValue, this::setValue);
    }

    /**
     * Loads the value of the parameter at {@code key} from {@code nbt} into {@code parameterMap}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link BooleanParameter}
     * and be already present.
     */
    public static void loadValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        ((BooleanParameter) parameterMap.get(key)).setValue(nbt.getBoolean(key));
    }

    /**
     * Saves the value of the parameter at {@code key} from {@code parameterMap} into {@code nbt}.
     * The element at {@code key} in {@code parameterMap} must be of type {@link BooleanParameter}
     * and be already present.
     */
    public static void saveValue(NBTTagCompound nbt, Map<String, Parameter<?>> parameterMap, String key) {
        nbt.setBoolean(key, ((BooleanParameter) parameterMap.get(key)).getValue());
    }
}
