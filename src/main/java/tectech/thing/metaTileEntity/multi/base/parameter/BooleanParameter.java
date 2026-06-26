package tectech.thing.metaTileEntity.multi.base.parameter;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;

public class BooleanParameter extends Parameter<Boolean, BooleanSyncValue> {

    public BooleanParameter(Boolean value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setBoolean(this.getNbtKey(), this.getValue());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        setValue(tag.getBoolean(this.getNbtKey()));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "boolean");
        tag.setBoolean("value", this.getValue());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(tag.getBoolean("value"));
    }

    @Override
    protected BooleanSyncValue createSyncHandler() {
        return new BooleanSyncValue(this::getValue, this::setValue).allowC2S();
    }
}
