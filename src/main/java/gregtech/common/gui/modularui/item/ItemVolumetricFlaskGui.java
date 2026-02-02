package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.items.ItemVolumetricFlask;

public class ItemVolumetricFlaskGui {

    private final ItemStack flask;
    private final ItemVolumetricFlask flaskItem;
    private int capacity;
    private PlayerInventoryGuiData guiData;

    public ItemVolumetricFlaskGui(PlayerInventoryGuiData guiData) {

        this.guiData = guiData;
        this.flask = this.guiData.getUsedItemStack();
        this.flaskItem = (ItemVolumetricFlask) flask.getItem();
        this.capacity = this.flaskItem.getCapacity(flask);
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("volumetric_flask", 140, 50);

        Flow mainColumn = Flow.column()
            .sizeRel(1)
            .padding(4);
        Flow capacityFieldRow = Flow.row()
            .widthRel(1)
            .height(18)
            .marginBottom(2);

        IntValue.Dynamic capacitySyncer = new IntValue.Dynamic(() -> capacity, value -> {
            flaskItem.setCapacity(flask, capacity = value);
            guiData.setUsedItemStack(flask);
        });

        IntSyncValue maxCapacitySyncer = new IntSyncValue(flaskItem::getMaxCapacity);

        capacityFieldRow.child(
            new TextFieldWidget().setNumbers(1, maxCapacitySyncer.getIntValue())
                .setDefaultNumber(capacity)
                .setFormatAsInteger(true)
                .value(capacitySyncer)
                .height(18)
                .width(80)
                .marginRight(2));

        capacityFieldRow.child(
            IKey.lang("GT5U.gui.text.volumetric_flask.capacity")
                .asWidget());

        Flow closeRow = Flow.row()
            .widthRel(1)
            .height(18);

        closeRow.child(createPanelCloseButton(capacitySyncer));

        mainColumn.child(capacityFieldRow);
        mainColumn.child(closeRow);

        panel.child(mainColumn);
        return panel;
    }

    // a slightly altered default close button implementation, this one is aligned left as opposed to positioned
    private ButtonWidget<?> createPanelCloseButton(IntValue.Dynamic capacitySyncer) {
        ButtonWidget<?> buttonWidget = new ButtonWidget<>();
        return buttonWidget.widgetTheme(IThemeApi.CLOSE_BUTTON)
            .align(Alignment.CenterLeft)
            .height(14)
            .width(80)
            .overlay(IKey.lang("GT5U.gui.text.volumetric_flask.confirm"))
            .onMousePressed(mouseButton -> {
                if (mouseButton == 0 || mouseButton == 1) {
                    capacitySyncer.setIntValue(capacitySyncer.getIntValue());
                    buttonWidget.getPanel()
                        .closeIfOpen();
                    return true;
                }
                return false;
            });
    }
}
