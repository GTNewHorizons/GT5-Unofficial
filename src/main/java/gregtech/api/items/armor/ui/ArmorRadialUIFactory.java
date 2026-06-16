package gregtech.api.items.armor.ui;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SortableListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.items.armor.ArmorAction;
import gregtech.api.items.armor.ArmorActionManager;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuBuilder;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuTheme;

public class ArmorRadialUIFactory {

    private static final int ROW_WIDTH = 120;
    private static final int ROW_HEIGHT = 18;
    private static final int ICON_SIZE = 18;
    private static final int PANEL_PADDING = 4;
    private static final int LIST_ITEM_HEIGHT = 20;
    private static final int MIN_POPUP_HEIGHT = 100;

    public static ModularPanel createMainPanel(ArmorRadialMenuSession session) {
        ModularPanel panel = ModularPanel.defaultPanel("Armor Settings Panel");

        Flow radialContainer = new Flow(GuiAxis.X).relativeToScreen()
            .top(0)
            .bottom(0)
            .left(0)
            .right(0);

        session.setUiUpdateCallback(() -> updateRadialMenu(radialContainer, session));

        updateRadialMenu(radialContainer, session);

        IPanelHandler popupPanel = session.getSyncManager()
            .syncedPanel("popup_settings", false, (syncManager, ignored) -> createPopup(session));

        panel.fullScreenInvisible();
        panel.child(radialContainer);

        panel.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            popupPanel.togglePanel();
            return true;
        }));

        return panel;
    }

    private static void updateRadialMenu(Flow radialContainer, ArmorRadialMenuSession session) {
        radialContainer.getChildren()
            .clear();

        RadialMenuBuilder builder = new RadialMenuBuilder("armor_radial", session.getSyncManager())
            .theme(new RadialMenuTheme(new float[] { 0f, 0f, 0f, 0.4f }, new float[] { 0.25f, 0.25f, 0.25f, 1f }));

        for (String actionId : session.getCurrentOrder()) {
            ArmorAction action = ArmorActionManager.getAction(actionId);
            if (action == null) continue;

            if (!action.isToggle() || session.isActive(actionId)) {
                builder.option()
                    .label(IKey.str(action.getDisplayName()))
                    .onClicked(
                        () -> session.getActionTriggerSync()
                            .setStringValue(action.getId()))
                    .done();
            }
        }

        radialContainer.child(
            builder.build()
                .relativeToScreen()
                .top(0)
                .bottom(0)
                .left(0)
                .right(0));
    }

    private static ModularPanel createPopup(ArmorRadialMenuSession session) {
        ModularPanel panel = new ModularPanel("Settings") {

            @Override
            public boolean isDraggable() {
                return false;
            }

            @Override
            public void onInit() {
                super.onInit();

                int dynamicHeight = Math.max(
                    40,
                    session.getCurrentOrder()
                        .size() * LIST_ITEM_HEIGHT);
                SortableListWidget<String> list = new SortableListWidget<String>().size(ROW_WIDTH, dynamicHeight)
                    .pos(PANEL_PADDING, PANEL_PADDING);

                list.onChange(session::updateOrder);

                for (String actionId : session.getCurrentOrder()) {
                    list.child(buildActionListItem(actionId, session));
                }

                this.child(list);
            }
        };

        panel.size(
            128,
            Math.max(
                MIN_POPUP_HEIGHT,
                session.getCurrentOrder()
                    .size() * LIST_ITEM_HEIGHT + 10))
            .pos(0, 18);

        return panel;
    }

    private static SortableListWidget.Item<String> buildActionListItem(String actionId,
        ArmorRadialMenuSession session) {
        ArmorAction action = ArmorActionManager.getAction(actionId);
        if (action == null) return null;

        Flow flow = Flow.row()
            .size(ROW_WIDTH, ROW_HEIGHT);

        BoolValue.Dynamic localToggle = new BoolValue.Dynamic(
            () -> !session.isActive(actionId),
            (newState) -> session.toggleActiveState(actionId, !newState));

        ToggleButton toggle = new ToggleButton().value(localToggle);

        flow.child(toggle);

        // TODO: Replace placeholder Rectangle for an Icon (accessible with action.getIcon())
        flow.child(
            new Rectangle().asWidget()
                .size(ICON_SIZE, ICON_SIZE));

        flow.child(
            new TextWidget<>(action.getDisplayName()).color(0xFFFFFF)
                .paddingLeft(2));

        return new SortableListWidget.Item<>(actionId).child(flow);
    }
}
