package gregtech.common.modularui2.widget.builder;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

/**
 * Creates a row wrapping a series of buttons that are bound to the enum.
 * <p>
 * In order to add tooltip, you can either call {@link #tooltip(IKey...)} or implement {@link KeyProvider} for the enum.
 */
public class EnumRowBuilder<E extends Enum<E>> {

    private EnumSyncValue<E> syncValue;
    private @NotNull final Class<E> enumClass;
    private IDrawable[] overlay;
    private IKey[] tooltip;

    public EnumRowBuilder(@NotNull Class<E> enumClass) {
        this.enumClass = Objects.requireNonNull(enumClass);
    }

    /**
     * This method wraps supplied value with {@link LinkedBoolValue} to be bound to each button, so you still need to
     * manually register it to {@link com.cleanroommc.modularui.value.sync.PanelSyncManager}.
     */
    public EnumRowBuilder<E> value(EnumSyncValue<E> syncValue) {
        this.syncValue = syncValue;
        return this;
    }

    public EnumRowBuilder<E> overlay(IDrawable... overlay) {
        this.overlay = overlay;
        return this;
    }

    public EnumRowBuilder<E> overlay(int size, IDrawable... overlay) {
        this.overlay = new IDrawable[overlay.length];
        for (int i = 0; i < overlay.length; i++) {
            this.overlay[i] = overlay[i].asIcon()
                .size(size);
        }
        return this;
    }

    public EnumRowBuilder<E> tooltip(IKey... tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public Flow build() {
        if (this.syncValue == null) {
            throw new IllegalArgumentException("Sync value cannot be null");
        }
        if (this.overlay != null && this.overlay.length != enumClass.getEnumConstants().length) {
            throw new IllegalArgumentException("Number of overlays must be " + enumClass.getEnumConstants().length);
        }
        if (this.tooltip != null && this.tooltip.length != enumClass.getEnumConstants().length) {
            throw new IllegalArgumentException("Number of tooltips must be " + enumClass.getEnumConstants().length);
        }
        Flow row = Flow.row()
            .childPadding(2)
            .coverChildren();
        for (E enumVal : this.enumClass.getEnumConstants()) {
            ToggleButton button = new SelectButton().value(LinkedBoolValue.of(this.syncValue, enumVal))
                .size(16);
            if (this.overlay != null) {
                button.overlay(this.overlay[enumVal.ordinal()]);
            }
            if (this.tooltip != null) {
                button.addTooltipLine(this.tooltip[enumVal.ordinal()]);
            }
            if (enumVal instanceof KeyProvider keyProvider) {
                button.addTooltipLine(IKey.lang(keyProvider.getKey()));
            }
            row.child(button);
        }

        return row;
    }
}
