package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class DoubleParameter extends NumericParameter<Double> {

    public DoubleParameter(Double value, String langKey, String nbtKey, Supplier<Double> min, Supplier<Double> max,
        Object... langArgs) {
        super(value, langKey, nbtKey, min, max, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setDouble(this.getNbtKey(), this.getValue());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        this.setValue(tag.getDouble(this.getNbtKey()));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "double");
        tag.setDouble("value", this.getValue());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(tag.getDouble("value"));
    }

    @Override
    public SyncHandler createSyncHandler() {
        return new DoubleSyncValue(this::getValue, this::setValue);
    }

    public double validateValue(double num) {
        return Math.max(this.getMin(), Math.min(num, this.getMax()));
    }
}
