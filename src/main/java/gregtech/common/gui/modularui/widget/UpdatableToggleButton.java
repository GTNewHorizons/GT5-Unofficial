package gregtech.common.gui.modularui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.widgets.ToggleButton;

/**
 * A button that updates dynamicHandler when being clicked.
 */
public class UpdatableToggleButton extends ToggleButton {

    List<DynamicSyncHandler> handler = new ArrayList<>();

    public UpdatableToggleButton(DynamicSyncHandler... handlers) {
        super();
        this.handler.addAll(Arrays.asList(handlers));
    }

    @Override
    @NotNull
    public Interactable.@NotNull Result onMousePressed(int mouseButton) {
        Result result = super.onMousePressed(mouseButton);
        handler.forEach(var -> {
            if (var == null) return;
            var.notifyUpdate(packet -> {});
        });
        return result;
    }
}
