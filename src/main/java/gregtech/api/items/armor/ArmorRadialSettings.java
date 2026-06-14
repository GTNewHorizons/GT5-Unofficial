package gregtech.api.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SortableListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuBuilder;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuTheme;

public class ArmorRadialSettings extends AbstractUIFactory<GuiData> {

    public static final ArmorRadialSettings INSTANCE = new ArmorRadialSettings();

    public void open(EntityPlayer player) {
        if (player.getEntityWorld().isRemote) {
            GuiManager.openFromClient(this, new GuiData(player));
        } else if (player instanceof net.minecraft.entity.player.EntityPlayerMP playerMP) {
            GuiManager.open(this, new GuiData(player), playerMP);
        }
    }

    private ArmorRadialSettings() {
        super("gregtech:armor_settings_menu");
    }

    @Override
    public @NotNull IGuiHolder<GuiData> getGuiHolder(final GuiData data) {
        return ArmorRadialSettings.SelectGuiHolder.INSTANCE;
    }

    @Override
    public void writeGuiData(final GuiData guiData, final PacketBuffer buffer) {}

    @Override
    public @NotNull GuiData readGuiData(final EntityPlayer player, final PacketBuffer buffer) {
        return new GuiData(player);
    }

    @Override
    public ModularScreen createScreen(final GuiData data, final ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }

    static class SelectGuiHolder implements IGuiHolder<GuiData> {

        @Override
        public ModularScreen createScreen(final GuiData data, final ModularPanel mainPanel) {
            return new ModularScreen(GregTech.ID, mainPanel);
        }

        static final ArmorRadialSettings.SelectGuiHolder INSTANCE = new ArmorRadialSettings.SelectGuiHolder();

        private SelectGuiHolder() {}

        private NBTTagCompound getSettingsNBT(EntityPlayer player) {
            NBTTagCompound forgeData = player.getEntityData();
            if (!forgeData.hasKey("PlayerPersisted")) {
                forgeData.setTag("PlayerPersisted", new NBTTagCompound());
            }
            NBTTagCompound persisted = forgeData.getCompoundTag("PlayerPersisted");
            if (!persisted.hasKey("ArmorRadialSettings")) {
                persisted.setTag("ArmorRadialSettings", new NBTTagCompound());
            }
            return persisted.getCompoundTag("ArmorRadialSettings");
        }

        public ModularPanel buildPopup(StringSyncValue syncedActive, Set<String> activeSet, StringSyncValue syncedOrder,
            List<String> currentOrder, Runnable updateRadialMenu) {

            ModularPanel panel = new ModularPanel("Settings") {

                @Override
                public boolean isDraggable() {
                    return false;
                }

                @Override
                public void onInit() {
                    super.onInit();

                    int dynamicHeight = Math.max(40, currentOrder.size() * 20);
                    SortableListWidget<String> list = new SortableListWidget<String>().size(120, dynamicHeight)
                        .pos(4, 4);

                    list.onChange(newOrder -> {
                        currentOrder.clear();
                        currentOrder.addAll(newOrder);

                        StringBuilder orderStr = new StringBuilder();
                        for (String s : newOrder) orderStr.append(s)
                            .append(",");

                        syncedOrder.setStringValue(orderStr.toString());
                        updateRadialMenu.run();
                    });

                    for (String actionName : currentOrder) {
                        Flow flow = Flow.row()
                            .size(120, 18);

                        BoolValue.Dynamic localToggle = new BoolValue.Dynamic(
                            () -> activeSet.contains(actionName),
                            (newState) -> {
                                if (newState) activeSet.add(actionName);
                                else activeSet.remove(actionName);

                                StringBuilder activeStr = new StringBuilder();
                                for (String activeAction : activeSet) {
                                    activeStr.append(activeAction)
                                        .append(",");
                                }
                                syncedActive.setStringValue(activeStr.toString());
                            });

                        ToggleButton toggle = new ToggleButton() {

                            @Override
                            public @NotNull Result onMousePressed(int mouseButton) {
                                Result result = super.onMousePressed(mouseButton);
                                if (result == Result.SUCCESS) {
                                    updateRadialMenu.run();
                                }
                                return result;
                            }
                        }.value(localToggle);

                        flow.child(toggle);
                        flow.child(
                            new Rectangle().asWidget()
                                .size(18, 18));
                        flow.child(
                            new TextWidget<>(actionName).color(0xFFFFFF)
                                .paddingLeft(2));

                        list.child(new SortableListWidget.Item<String>(actionName).child(flow));
                    }

                    this.child(list);
                }
            };

            panel.size(128, Math.max(50, currentOrder.size() * 20 + 10))
                .pos(0, 18);
            return panel;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public ModularPanel buildUI(final GuiData data, final PanelSyncManager syncManager, final UISettings settings) {
            EntityPlayer player = data.getPlayer();
            NBTTagCompound nbt = getSettingsNBT(player);

            List<String> actionNames = Arrays
                .asList("Nightvision", "Jetpack", "Milk Infusions", "Step Assist", "Speed", "Jump Boost");

            String defaultOrder = String.join(",", actionNames) + ",";
            String defaultActive = String.join(",", actionNames) + ",";

            StringSyncValue syncedOrder = (StringSyncValue) new StringSyncValue(
                () -> nbt.hasKey("RadialOrder") ? nbt.getString("RadialOrder") : defaultOrder,
                (newOrder) -> nbt.setString("RadialOrder", newOrder)).allowC2S();

            StringSyncValue syncedActive = (StringSyncValue) new StringSyncValue(
                () -> nbt.hasKey("RadialActive") ? nbt.getString("RadialActive") : defaultActive,
                (newActive) -> nbt.setString("RadialActive", newActive)).allowC2S();

            syncManager.syncValue("orderSync", syncedOrder);
            syncManager.syncValue("activeSync", syncedActive);

            List<String> currentOrder = new ArrayList<>();
            for (String s : syncedOrder.getStringValue()
                .split(",")) {
                if (!s.isEmpty() && actionNames.contains(s)) {
                    currentOrder.add(s);
                }
            }
            for (String action : actionNames) {
                if (!currentOrder.contains(action)) {
                    currentOrder.add(action);
                }
            }

            Set<String> activeSet = new HashSet<>();
            for (String s : syncedActive.getStringValue()
                .split(",")) {
                if (!s.isEmpty()) {
                    activeSet.add(s);
                }
            }

            Flow radialContainer = new Flow(GuiAxis.X);
            radialContainer.relativeToScreen()
                .top(0)
                .bottom(0)
                .left(0)
                .right(0);

            Runnable rebuildRadialMenu = () -> {
                radialContainer.getChildren()
                    .clear();
                RadialMenuBuilder builder = new RadialMenuBuilder("armor_radial", syncManager).theme(
                    new RadialMenuTheme(new float[] { 0f, 0f, 0f, 0.4f }, new float[] { 0.25f, 0.25f, 0.25f, 1f }));

                for (String actionName : currentOrder) {
                    if (activeSet.contains(actionName)) {
                        builder.option()
                            .label(IKey.str(actionName))
                            .onClicked(() -> {})
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
            };

            rebuildRadialMenu.run();

            ModularPanel panel = ModularPanel.defaultPanel("Armor Settings Panel");

            IPanelHandler popupPanel = syncManager.syncedPanel(
                "popup_settings_ding",
                true,
                (panelSyncManager,
                    _) -> buildPopup(syncedActive, activeSet, syncedOrder, currentOrder, rebuildRadialMenu));

            panel.fullScreenInvisible();
            panel.child(radialContainer);

            panel.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                popupPanel.togglePanel();
                return true;
            }));

            return panel;
        }
    }
}
