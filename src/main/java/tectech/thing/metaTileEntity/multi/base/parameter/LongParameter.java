package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.LongSyncValue;

public class LongParameter extends NumericParameter<Long, LongSyncValue> {

    public LongParameter(Long value, String langKey, String nbtKey, Supplier<Long> min, Supplier<Long> max,
        Object... langArgs) {
        super(value, langKey, nbtKey, min, max, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setLong(this.getNbtKey(), this.getValue());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        this.setValue(tag.getLong(this.getNbtKey()));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "long");
        tag.setLong("value", this.getValue());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(tag.getLong("value"));
    }

    @Override
    protected LongSyncValue createSyncHandler() {
        return new LongSyncValue(this::getValue, this::setValue).allowC2S();
    }

    @Override
    public Long validate(Long value) {
        return Math.clamp(value, this.getMin(), this.getMax());
    }
}
