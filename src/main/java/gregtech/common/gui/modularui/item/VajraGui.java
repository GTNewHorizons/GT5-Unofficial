package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.tools.ToolVajra;

public class VajraGui {

    private final PlayerInventoryGuiData data;
    private final PanelSyncManager syncManager;

    public VajraGui(PlayerInventoryGuiData data, PanelSyncManager syncManager) {
        ItemStack stack = data.getUsedItemStack();
        if (stack == null || !(stack.getItem() instanceof ToolVajra)) {
            throw new RuntimeException("Tried to open the Vajra GUI without a Vajra in hand");
        }
        this.data = data;
        this.syncManager = syncManager;
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("vajra_configuration", 224, 100);
        BooleanSyncValue silkTouch = new BooleanSyncValue(
            () -> ToolVajra.isSilkTouchEnabled(data.getUsedItemStack()),
            enabled -> ToolVajra.setSilkTouchEnabled(data.getUsedItemStack(), enabled)).allowC2S();
        BooleanSyncValue breakCooldown = new BooleanSyncValue(
            () -> ToolVajra.isCreativeBreakCooldownEnabled(data.getUsedItemStack()),
            enabled -> ToolVajra.setCreativeBreakCooldownEnabled(data.getUsedItemStack(), enabled)).allowC2S();
        BooleanSyncValue rightClick = new BooleanSyncValue(
            () -> ToolVajra.isRightClickEnabled(data.getUsedItemStack()),
            enabled -> ToolVajra.setRightClickEnabled(data.getUsedItemStack(), enabled)).allowC2S();
        syncManager.syncValue("silkTouch", silkTouch);
        syncManager.syncValue("breakCooldown", breakCooldown);
        syncManager.syncValue("rightClick", rightClick);

        panel.child(
            IKey.lang("gt.vajra.gui.title")
                .asWidget()
                .size(208, 12)
                .top(8)
                .left(8)
                .textAlign(Alignment.Center));
        panel.child(
            Flow.column()
                .size(200, 62)
                .top(27)
                .left(12)
                .childPadding(4)
                .child(
                    createSettingRow(
                        IKey.lang("gt.vajra.gui.silk_touch"),
                        createButton(
                            LinkedBoolValue.of(silkTouch, false),
                            IKey.lang("GT5U.gui.button.feature_disabled"),
                            56),
                        createButton(
                            LinkedBoolValue.of(silkTouch, true),
                            IKey.lang("GT5U.gui.button.feature_enabled"),
                            56)))
                .child(
                    createSettingRow(
                        IKey.lang("gt.vajra.gui.creative_break_cooldown"),
                        createButton(
                            LinkedBoolValue.of(breakCooldown, false),
                            IKey.lang("GT5U.gui.button.feature_disabled"),
                            56),
                        createButton(
                            LinkedBoolValue.of(breakCooldown, true),
                            IKey.lang("GT5U.gui.button.feature_enabled"),
                            56)))
                .child(
                    createSettingRow(
                        IKey.lang("gt.vajra.gui.right_click_breaking"),
                        createButton(
                            LinkedBoolValue.of(rightClick, false),
                            IKey.lang("GT5U.gui.button.feature_disabled"),
                            56),
                        createButton(
                            LinkedBoolValue.of(rightClick, true),
                            IKey.lang("GT5U.gui.button.feature_enabled"),
                            56))));

        return panel;
    }

    private Flow createSettingRow(IKey label, SelectButton... buttons) {
        Flow controls = Flow.row()
            .size(116, 18)
            .childPadding(4);
        for (SelectButton button : buttons) controls.child(button);
        return Flow.row()
            .size(200, 18)
            .childPadding(4)
            .child(
                label.asWidget()
                    .size(80, 18)
                    .textAlign(Alignment.CenterLeft))
            .child(controls);
    }

    private SelectButton createButton(IBoolValue<?> value, IKey label, int width) {
        SelectButton button = new SelectButton();
        button.value(value)
            .size(width, 18)
            .overlay(label);
        return button;
    }
}
