package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public class ConnectionKeyPanel extends ModularPanel {

    public ConnectionKeyPanel(PanelSyncManager syncManager, ModularPanel parent) {
        super("connectionKey");
        this.relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 36)
            .child(
                Flow.column()
                    .sizeRel(1f)
                    .padding(2)
                    .childPadding(2)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_key")
                            .asWidget()
                            .alignment(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().height(16)
                            .widthRel(0.8f)
                            .tooltip(t -> t.add(IKey.lang("GT5U.gui.tooltip.drone_key")))
                            .value(syncManager.findSyncHandler("setkey", StringSyncValue.class))));
    }
}
