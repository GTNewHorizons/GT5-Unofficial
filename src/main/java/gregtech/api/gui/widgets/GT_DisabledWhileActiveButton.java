package gregtech.api.gui.widgets;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.tileentity.IMachineProgress;

public class GT_DisabledWhileActiveButton extends ButtonWidget {

    @NotNull
    private final WeakReference<IMachineProgress> machineRef;

    public GT_DisabledWhileActiveButton(@NotNull IMachineProgress machine, @NotNull ModularWindow.Builder builder) {
        super();
        machineRef = new WeakReference<>(machine);

        super.attachSyncer(
            new FakeSyncWidget.BooleanSyncer(() -> isMachineActive().orElse(true), a -> {}),
            builder,
            (widget, aBoolean) -> widget.notifyTooltipChange());

        super.dynamicTooltip(this::generateTooltip);
    }

    @NotNull
    @Override
    public ButtonWidget setOnClick(@NotNull BiConsumer<ClickData, Widget> clickAction) {
        return super.setOnClick((clickData, widget) -> isMachineActive().ifPresent(isActive -> {
            if (!isActive) {
                clickAction.accept(clickData, widget);
            }
        }));
    }

    @NotNull
    @Override
    public Widget setBackground(@NotNull IDrawable... drawables) {
        return super.setBackground(() -> appendLockedOverlay(drawables));
    }

    @NotNull
    @Override
    public Widget setBackground(@NotNull Supplier<IDrawable[]> drawablesSupplier) {
        return super.setBackground(() -> appendLockedOverlay(drawablesSupplier.get()));
    }

    @NotNull
    @Override
    public Widget dynamicTooltip(@NotNull Supplier<List<String>> dynamicTooltip) {
        return super.dynamicTooltip(() -> {
            ImmutableList.Builder<String> tooltips = ImmutableList.<String>builder()
                .addAll(dynamicTooltip.get());
            tooltips.addAll(generateTooltip());

            return tooltips.build();
        });
    }

    @NotNull
    private Optional<Boolean> isMachineActive() {
        return Optional.ofNullable(machineRef.get())
            .map(IMachineProgress::isActive);
    }

    @NotNull
    private IDrawable[] appendLockedOverlay(@NotNull IDrawable[] drawables) {
        return isMachineActive().map(isActive -> {
            if (isActive) {
                final IDrawable[] copy = Arrays.copyOf(drawables, drawables.length + 1);
                copy[drawables.length] = GT_UITextures.OVERLAY_BUTTON_LOCKED;
                return copy;
            }
            return drawables;
        })
            .orElse(drawables);
    }

    @NotNull
    private List<String> generateTooltip() {
        return isMachineActive().map(isActive -> {
            if (isActive) {
                return ImmutableList.of(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
            }
            return ImmutableList.<String>of();
        })
            .orElse(ImmutableList.of());
    }
}
