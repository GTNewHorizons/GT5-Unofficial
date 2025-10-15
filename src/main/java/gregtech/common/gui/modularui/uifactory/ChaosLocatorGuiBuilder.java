package gregtech.common.gui.modularui.uifactory;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public class ChaosLocatorGuiBuilder {

    private final GuiData guiData;
    private final PanelSyncManager guiSyncManager;

    public ChaosLocatorGuiBuilder(GuiData guiData, PanelSyncManager guiSyncManager) {
        this.guiData = guiData;
        this.guiSyncManager = guiSyncManager;
    }

    public ModularPanel build() {
        int textColor = Color.rgb(255, 255, 255);
        ModularPanel panel = ModularPanel.defaultPanel("chaos_locator");
        IntSyncValue xSyncer = new IntSyncValue(
            () -> guiData.getMainHandItem()
                .getTagCompound()
                .getInteger("xCoordinate"),
            (x) -> {
                if (guiData.getMainHandItem()
                    .getTagCompound() == null)
                    guiData.getMainHandItem()
                        .setTagCompound(new NBTTagCompound());
                NBTTagCompound tag = guiData.getMainHandItem()
                    .getTagCompound();
                tag.setInteger("xCoordinate", x);
            });

        IntSyncValue zSyncer = new IntSyncValue(
            () -> guiData.getMainHandItem()
                .getTagCompound()
                .getInteger("zCoordinate"),
            (z) -> {
                if (guiData.getMainHandItem()
                    .getTagCompound() == null)
                    guiData.getMainHandItem()
                        .setTagCompound(new NBTTagCompound());
                NBTTagCompound tag = guiData.getMainHandItem()
                    .getTagCompound();
                tag.setInteger("zCoordinate", z);
            });

        panel.flex()
            .size(100, 100)
            .align(Alignment.Center);
        panel.child(
            new TextWidget<>("X Coordinate").widthRel(0.5f)
                .color(textColor)
                .alignment(Alignment.Center)
                .posRel(0f, 0.5f));
        panel.child(
            new TextWidget<>("Z Coordinate").widthRel(0.5f)
                .color(textColor)
                .alignment(Alignment.Center)
                .posRel(0.5f, 0.5f));
        panel.child(
            new TextFieldWidget().widthRel(0.2f)
                .posRel(0.25f, 0.25f)
                .setTextAlignment(Alignment.Center)
                .setFormatAsInteger(true)
                .setFocusOnGuiOpen(true)
                .value(xSyncer)
                .setDefaultNumber(0));
        panel.child(
            new TextFieldWidget().widthRel(0.2f)
                .posRel(0.75f, 0.25f)
                .setTextAlignment(Alignment.Center)
                .setFormatAsInteger(true)
                .setFocusOnGuiOpen(true)
                .value(zSyncer)
                .setDefaultNumber(0));

        return panel;
    }
}
