package gregtech.common.gui.modularui.item;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.net.PacketTeleportPlayer;

public class ChaosLocatorGui {

    private final GuiData guiData;

    public ChaosLocatorGui(GuiData guiData) {
        this.guiData = guiData;
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("chaos_locator");
        IntSyncValue xSyncer = new IntSyncValue(
            () -> guiData.getMainHandItem()
                .getTagCompound() != null ? guiData.getMainHandItem()
                    .getTagCompound()
                    .getInteger("xCoordinate") : 0,
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
                .getTagCompound() != null ? guiData.getMainHandItem()
                    .getTagCompound()
                    .getInteger("zCoordinate") : 0,
            (z) -> {
                if (guiData.getMainHandItem()
                    .getTagCompound() == null)
                    guiData.getMainHandItem()
                        .setTagCompound(new NBTTagCompound());
                NBTTagCompound tag = guiData.getMainHandItem()
                    .getTagCompound();
                tag.setInteger("zCoordinate", z);
            });

        panel.resizer()
            .size(100, 100);

        panel.child(
            new TextWidget<>(IKey.lang("gt.item.chaos_locator.coords")).sizeRel(1f, 0.2f)
                .posRel(0f, 0.1f)
                .alignment(Alignment.Center)
                .color(10227735));

        panel.child(
            Flow.row()
                .child(
                    Flow.column()
                        .widthRel(0.4f)
                        .coverChildrenHeight()
                        .posRel(0.05f, 0.45f)
                        .child(
                            new TextFieldWidget().widthRel(1f)
                                .height(18)
                                .setTextAlignment(Alignment.Center)
                                .setTextColor(10227735)
                                .setFormatAsInteger(true)
                                .setNumbers(-1000, 1000)
                                .value(xSyncer)
                                .setDefaultNumber(0))
                        .child(
                            new TextWidget<>(IKey.lang("gt.item.chaos_locator.x")).widthRel(1f)
                                .height(9)
                                .alignment(Alignment.Center)
                                .color(10227735))
                        .childPadding(2))
                .child(
                    Flow.column()
                        .widthRel(0.4f)
                        .coverChildrenHeight()
                        .posRel(0.95f, 0.45f)
                        .child(
                            new TextFieldWidget().widthRel(1f)
                                .height(18)
                                .setTextAlignment(Alignment.Center)
                                .setTextColor(10227735)
                                .setFormatAsInteger(true)
                                .setNumbers(-1000, 1000)
                                .value(zSyncer)
                                .setDefaultNumber(0))
                        .child(
                            new TextWidget<>(IKey.lang("gt.item.chaos_locator.z")).widthRel(1f)
                                .height(9)
                                .alignment(Alignment.Center)
                                .color(10227735))
                        .childPadding(2)));

        panel.child(
            new ButtonWidget<>().sizeRel(0.4f, 0.2f)
                .posRel(0.5f, 0.85f)
                .overlay(
                    IKey.lang("gt.item.chaos_locator.warp")
                        .color(10227735))
                .onMousePressed(mouseButton -> {
                    int xToTeleportTo = guiData.getMainHandItem()
                        .getTagCompound()
                        .getInteger("xCoordinate") * 10_000;
                    int zToTeleportTo = guiData.getMainHandItem()
                        .getTagCompound()
                        .getInteger("zCoordinate") * 10_000;

                    GTValues.NW.sendToServer(new PacketTeleportPlayer(1, xToTeleportTo, 200, zToTeleportTo, true));
                    return true;
                })
                .widgetTheme(GTWidgetThemes.BUTTON_BLACK));

        return panel.widgetTheme(GTWidgetThemes.BACKGROUND_CHAOS_LOCATOR);
    }
}
