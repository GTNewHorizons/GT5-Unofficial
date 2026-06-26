package gregtech.common.gui.modularui.widget.settings;

import java.util.Arrays;

import net.minecraft.client.gui.FontRenderer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.EnumCycleButtonWidget;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record EnumCycleSettingRow<E extends Enum<E>> (IKey label, Class<E> clazz, IIntValue<?> value,
    WidgetConfigurator<EnumCycleButtonWidget<E>> configure) implements ISettingRow<EnumCycleButtonWidget<E>> {

    @Override
    public @NotNull Pair<IKey, EnumCycleButtonWidget<E>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        FontRenderer fontRenderer = TextRenderer.getFontRenderer();

        EnumCycleButtonWidget<E> button = new EnumCycleButtonWidget<>(clazz).value(value);

        if (configure != null) configure.configure(panel, syncManager, button);

        if (button.hasDefaultOverlay()) button.width(
            Arrays.stream(clazz.getEnumConstants())
                .map(Enum::toString)
                .mapToInt(fontRenderer::getStringWidth)
                .max()
                .getAsInt() + 10);

        return Pair.of(label, button);
    }
}
