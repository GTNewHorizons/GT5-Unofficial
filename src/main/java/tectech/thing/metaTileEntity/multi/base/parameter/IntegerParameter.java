package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.IntSyncValue;

public class IntegerParameter extends NumericParameter<Integer, IntSyncValue> {

    public IntegerParameter(Integer value, String langKey, String nbtKey, Supplier<Integer> min, Supplier<Integer> max,
        Object... langArgs) {
        super(value, langKey, nbtKey, min, max, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setInteger(this.getNbtKey(), this.getValue());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        this.setValue(tag.getInteger(this.getNbtKey()));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "integer");
        tag.setInteger("value", this.getValue());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(tag.getInteger("value"));
    }

    @Override
    protected IntSyncValue createSyncHandler() {
        return new IntSyncValue(this::getValue, this::setValue).allowC2S();
    }

    @Override
    public Integer validate(Integer value) {
        return Math.clamp(value, this.getMin(), this.getMax());
    }
}
