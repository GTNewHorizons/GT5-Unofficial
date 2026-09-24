package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Function;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public class StringParameter extends Parameter<String, StringSyncValue> {

    public StringParameter(String value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setString(this.getNbtKey(), this.getValue());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        this.setValue(tag.getString(this.getNbtKey()));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "string");
        tag.setString("value", this.getValue());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(tag.getString(this.getNbtKey()));
    }

    @Override
    protected StringSyncValue createSyncHandler() {
        return new StringSyncValue(this::getValue, this::setValue).allowC2S();
    }

    @Override
    public void addToSettingsPanel(SettingsPanelBuilder builder, IKey label, WidgetConfigurator<?> configure,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        builder.addStringEditor(label, this.getSyncHandler(), (WidgetConfigurator<TextFieldWidget>) configure);
    }
}
