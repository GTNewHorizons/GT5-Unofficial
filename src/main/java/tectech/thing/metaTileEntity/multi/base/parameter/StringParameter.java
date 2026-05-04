package tectech.thing.metaTileEntity.multi.base.parameter;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class StringParameter extends Parameter<String> {

    public StringParameter(String value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {

    }

    @Override
    public void loadNBT(NBTTagCompound tag) {

    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {

    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {

    }

    @Override
    public SyncHandler createSyncHandler() {
        return new StringSyncValue(this::getValue, this::setValue);
    }
}
