package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public class DoubleParameter extends NumericParameter<Double, DoubleSyncValue> {

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
    protected DoubleSyncValue createSyncHandler() {
        return new DoubleSyncValue(this::getValue, this::setValue).allowC2S();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addToSettingsPanel(SettingsPanelBuilder builder, IKey label, WidgetConfigurator<?> configure,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        builder.addDoubleEditor(
            label,
            this.getSyncHandler(),
            this::validate,
            (WidgetConfigurator<TextFieldWidget>) configure);
    }

    @Override
    public Double validate(Double value) {
        return Math.clamp(value, this.getMin(), this.getMax());
    }
}
