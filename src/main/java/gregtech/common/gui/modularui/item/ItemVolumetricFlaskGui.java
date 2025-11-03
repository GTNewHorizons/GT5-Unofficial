package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiThemes;
import gregtech.common.items.ItemVolumetricFlask;

public class ItemVolumetricFlaskGui {

    private final ItemStack flask;
    private final ItemVolumetricFlask flaskItem;

    public ItemVolumetricFlaskGui(GuiData guiData) {

        this.flask = guiData.getMainHandItem();
        flaskItem = (ItemVolumetricFlask) flask.getItem();
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("volumetric_flask", 120, 50);
        panel.applyTheme(GTGuiThemes.STANDARD.getMuiTheme());

        Flow mainColumn = Flow.column()
            .sizeRel(1)
            .padding(4);
        Flow capacityFieldRow = Flow.row()
            .widthRel(1)
            .height(18)
            .marginBottom(2);

        IntSyncValue capacitySyncer = new IntSyncValue(
            () -> flaskItem.getCapacity(flask),
            value -> flaskItem.setCapacity(flask, value));
        IntSyncValue maxCapacitySyncer = new IntSyncValue(flaskItem::getMaxCapacity);

        capacityFieldRow.child(
            new TextFieldWidget().setNumbers(0, maxCapacitySyncer.getIntValue())
                .setDefaultNumber(maxCapacitySyncer.getIntValue())
                .value(capacitySyncer)
                .height(18)
                .marginRight(2));

        capacityFieldRow.child(
            IKey.lang("GT5U.gui.text.volumetric_flask.capacity")
                .asWidget());

        Flow closeRow = Flow.row()
            .widthRel(1)
            .height(18);
        closeRow.child(
            createPanelCloseButton().size(14)
                .tooltip(t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.text.volumetric_flask.confirm"))));

        mainColumn.child(capacityFieldRow);
        mainColumn.child(closeRow);

        panel.child(mainColumn);
        return panel;
    }

    // a slightly altered default close button implementation, this one is aligned left as opposed to positioned
    private static ButtonWidget<?> createPanelCloseButton() {
        ButtonWidget<?> buttonWidget = new ButtonWidget<>();
        return buttonWidget.widgetTheme(IThemeApi.CLOSE_BUTTON)
            .align(Alignment.CenterLeft)
            .overlay(GuiTextures.CROSS_TINY)
            .onMousePressed(mouseButton -> {
                if (mouseButton == 0 || mouseButton == 1) {
                    buttonWidget.getPanel()
                        .closeIfOpen();
                    return true;
                }
                return false;
            });
    }
}
