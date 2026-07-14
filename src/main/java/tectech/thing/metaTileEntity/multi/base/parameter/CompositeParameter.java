package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public class CompositeParameter extends Parameter<@UnmodifiableView List<Parameter<?, ?>>, SyncHandler<?>> {

    public CompositeParameter(List<Parameter<?, ?>> value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void setValue(@UnmodifiableView List<Parameter<?, ?>> value) {
        throw new UnsupportedOperationException("Parameters must be set directly!");
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        NBTTagCompound parameters = new NBTTagCompound();

        for (Parameter<?, ?> parameter : getValue()) parameter.saveNBT(parameters);
        tag.setTag(getNbtKey(), parameters);
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        NBTTagCompound parameters = tag.getCompoundTag(getNbtKey());
        for (Parameter<?, ?> parameter : getValue()) parameter.loadNBT(parameters);
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "composite");

        NBTTagList parameters = new NBTTagList();
        for (Parameter<?, ?> parameter : getValue()) {
            NBTTagCompound parameterTag = new NBTTagCompound();
            parameter.saveToParameterCard(parameterTag);
            parameters.appendTag(parameterTag);
        }

        tag.setTag("value", parameters);
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        NBTTagList parameterTags = tag.getTagList("value", Constants.NBT.TAG_COMPOUND);
        List<Parameter<?, ?>> parameters = getValue();

        for (int i = 0; i < parameters.size(); i++) parameters.get(i)
            .loadFromParameterCard(parameterTags.getCompoundTagAt(i));
    }

    @Override
    protected SyncHandler<?> createSyncHandler() {
        throw new UnsupportedOperationException("This parameter type does not have a sync handler!");
    }

    @Override
    protected void registerSyncValue(PanelSyncManager syncManager, String prefix) {
        for (Parameter<?, ?> parameter : getValue())
            parameter.registerSyncValue(syncManager, prefix + getNbtKey() + ".");
    }

    @Override
    public void addToSettingsPanel(SettingsPanelBuilder builder, IKey label, WidgetConfigurator<?> configure,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        builder.addButton(label, compositeParameterConfigurator(this, prefix, configurator));
    }

    private static WidgetConfigurator<ButtonWidget<?>> compositeParameterConfigurator(CompositeParameter parameter,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        return (_, syncManager, widget) -> {
            IPanelHandler parameterEditPanel = syncManager.syncedPanel(
                "parameterEditPanel_" + prefix + parameter.getNbtKey(),
                true,
                (p_syncManager, _) -> openParameterEditPanel(widget, parameter, p_syncManager, prefix, configurator));

            widget.width(80)
                .overlay(IKey.lang("tt.gui.tooltip.open_editor"))
                .onMousePressed(_ -> {
                    if (!parameterEditPanel.isPanelOpen()) {
                        parameterEditPanel.openPanel();
                    } else {
                        parameterEditPanel.closePanel();
                    }
                    return true;
                })
                .tooltip(
                    t -> t.addStringLines(
                        parameter.getValue()
                            .stream()
                            .map(p -> StatCollector.translateToLocalFormatted(p.getLangKey(), p.getLangArgs()))
                            .collect(Collectors.toList())));
        };
    }

    private static @NotNull ModularPanel openParameterEditPanel(ButtonWidget<?> parameterEditButton,
        CompositeParameter parameter, PanelSyncManager syncManager, String prefix,
        Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        ModularPanel panel = new ModularPanel("parameterEditPanel_" + prefix + parameter.getNbtKey()).coverChildren()
            .relative(parameterEditButton)
            .topRel(1)
            .leftRel(0)
            .child(ButtonWidget.panelCloseButton());

        panel.child(
            Flow.column()
                .coverChildren()
                .padding(4)
                .childPadding(10)
                .child(
                    IKey.lang(parameter.getLangKey(), parameter.getLangArgs())
                        .asWidget())
                .child(
                    SettingsPanelParameterCompat
                        .addSettingsForParameters(
                            SettingsPanel.builder(),
                            parameter.getValue(),
                            configurator,
                            prefix + parameter.getNbtKey() + ".")
                        .build(panel, syncManager, 100)));

        return panel;
    }
}
