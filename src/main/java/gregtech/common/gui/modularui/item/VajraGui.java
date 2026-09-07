package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
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
        ModularPanel panel = ModularPanel.defaultPanel("vajra_configuration", 264, 100);
        BooleanSyncValue silkTouch = new BooleanSyncValue(
            () -> ToolVajra.isSilkTouchEnabled(data.getUsedItemStack()),
            enabled -> ToolVajra.setSilkTouchEnabled(data.getUsedItemStack(), enabled)).allowC2S();
        DoubleSyncValue breakCooldown = new DoubleSyncValue(
            () -> ToolVajra.getCreativeBreakCooldown(data.getUsedItemStack()),
            cooldown -> ToolVajra.setCreativeBreakCooldown(data.getUsedItemStack(), (int) cooldown)).allowC2S();
        BooleanSyncValue rightClick = new BooleanSyncValue(
            () -> ToolVajra.isRightClickEnabled(data.getUsedItemStack()),
            enabled -> ToolVajra.setRightClickEnabled(data.getUsedItemStack(), enabled)).allowC2S();
        syncManager.syncValue("silkTouch", silkTouch);
        syncManager.syncValue("breakCooldown", breakCooldown);
        syncManager.syncValue("rightClick", rightClick);

        panel.child(
            IKey.lang("gt.vajra.gui.title")
                .asWidget()
                .size(248, 12)
                .top(8)
                .left(8)
                .textAlign(Alignment.Center));
        panel.child(
            Flow.column()
                .size(240, 62)
                .top(27)
                .left(12)
                .childPadding(4)
                .child(
                    createSettingRow(
                        IKey.lang("gt.vajra.gui.creative_break_cooldown"),
                        createCooldownSlider(breakCooldown)))
                .child(createSettingRow(IKey.lang("gt.vajra.gui.silk_touch"), createToggleControls(silkTouch)))
                .child(
                    createSettingRow(
                        IKey.lang("gt.vajra.gui.right_click_breaking"),
                        createToggleControls(rightClick))));

        return panel;
    }

    private Flow createSettingRow(IKey label, IWidget controls) {
        return Flow.row()
            .size(240, 18)
            .childPadding(4)
            .child(
                label.asWidget()
                    .size(120, 18)
                    .textAlign(Alignment.CenterLeft))
            .child(controls);
    }

    private Flow createToggleControls(BooleanSyncValue value) {
        return Flow.row()
            .size(116, 18)
            .childPadding(4)
            .child(createButton(LinkedBoolValue.of(value, false), IKey.lang("GT5U.gui.button.feature_disabled"), 56))
            .child(createButton(LinkedBoolValue.of(value, true), IKey.lang("GT5U.gui.button.feature_enabled"), 56));
    }

    private IWidget createCooldownSlider(DoubleSyncValue value) {
        return new ParentWidget<>().size(116, 18)
            .child(
                new SliderWidget().size(116, 18)
                    .background(GuiTextures.MC_BUTTON)
                    .bounds(0, 20)
                    .stopper(1)
                    .value(value))
            .child(
                IKey.dynamic(() -> String.valueOf((int) value.getDoubleValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .size(116, 18)
                    .textAlign(Alignment.Center));
    }

    private SelectButton createButton(IBoolValue<?> value, IKey label, int width) {
        SelectButton button = new SelectButton();
        button.value(value)
            .size(width, 18)
            .overlay(label);
        return button;
    }
}
