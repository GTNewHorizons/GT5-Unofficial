package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Function;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.ToggleButton;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

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

    @SuppressWarnings("unchecked")
    @Override
    public void addToSettingsPanel(SettingsPanelBuilder builder, IKey label, WidgetConfigurator<?> configure,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        builder.addToggleButton(label, this.getSyncHandler(), (panel, syncManager, widget) -> {
            widget.overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
            if (configure != null) ((WidgetConfigurator<ToggleButton>) configure).configure(panel, syncManager, widget);
        });
    }
}
