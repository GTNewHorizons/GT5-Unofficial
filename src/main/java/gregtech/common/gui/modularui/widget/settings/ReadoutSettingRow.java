package gregtech.common.gui.modularui.widget.settings;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.github.bsideup.jabel.Desugar;

import it.unimi.dsi.fastutil.Pair;

@Desugar
record ReadoutSettingRow<S extends ValueSyncHandler<T, ?>, T> (IKey label, S value, Function<T, IKey> format)
    implements ISettingRow<TextWidget<?>> {

    @Override
    public @NotNull Pair<IKey, TextWidget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        syncManager.syncValue(settings.syncName, settings.nextSyncId++, value);

        return Pair.of(
            label,
            IKey.dynamicKey(() -> format.apply(value.getValue()))
                .asWidget());
    }
}
