package gregtech.api.modularui2;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.Icon;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ToolboxSlot;
import gregtech.api.net.GTPacketToolboxEvent;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuBuilder;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuTheme;
import gregtech.common.items.ItemGTToolbox;
import gregtech.common.items.toolbox.ToolboxItemStackHandler;
import gregtech.crossmod.backhand.Backhand;

/**
 * GUI for selecting the active tool.
 * <p>
 * <b>NOTE:</b> This GUI may not always open depending on the situation that the toolbox finds itself in (e.g. only one
 * tool inside.) See {@link ItemGTToolbox#onPickBlock(ItemStack, EntityPlayer)} for more information.
 *
 * @see gregtech.common.gui.modularui.item.ToolboxInventoryGui
 */
public class ToolboxSelectGuiFactory extends AbstractUIFactory<GuiData> {

    public static final ToolboxSelectGuiFactory INSTANCE = new ToolboxSelectGuiFactory();

    private ToolboxSelectGuiFactory() {
        super("gregtech:toolbox_select");
    }

    public void open(EntityPlayer player) {
        if (player.getEntityWorld().isRemote) {
            GuiManager.openFromClient(this, new GuiData(player));
        }
    }

    @Override
    public @NotNull IGuiHolder<GuiData> getGuiHolder(final GuiData data) {
        return SelectGuiHolder.INSTANCE;
    }

    @Override
    public void writeGuiData(final GuiData guiData, final PacketBuffer buffer) {}

    @Override
    public @NotNull GuiData readGuiData(final EntityPlayer player, final PacketBuffer buffer) {
        return new GuiData(player);
    }

    static class SelectGuiHolder implements IGuiHolder<GuiData> {

        static final SelectGuiHolder INSTANCE = new SelectGuiHolder();

        private SelectGuiHolder() {}

        @Override
        public ModularScreen createScreen(final GuiData data, final ModularPanel mainPanel) {
            return new ModularScreen(GregTech.ID, mainPanel);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public ModularPanel buildUI(final GuiData data, final PanelSyncManager syncManager, final UISettings settings) {
            final int inventorySlot = getToolboxSlot(data.getPlayer()).orElseThrow(
                () -> new IllegalArgumentException("Tried to open the toolbox radial UI without equipping a toolbox"));
            final ItemStack toolbox = data.getPlayer().inventory.getStackInSlot(inventorySlot);
            final ToolboxItemStackHandler itemHandler = new ToolboxItemStackHandler(toolbox);

            final ModularPanel panel = ModularPanel.defaultPanel("ToolboxSelectUI");
            panel.fullScreenInvisible();
            panel.child(
                new RadialMenuBuilder("gt5:toolbox:select_gui", syncManager).innerIcon(toolbox)
                    .theme(
                        new RadialMenuTheme(new float[] { 0f, 0f, 0f, 0.4f }, new float[] { 0.25f, 0.25f, 0.25f, 1f }))
                    .pipe(builder -> {
                        builder.innerIcon(toolbox)
                            .option()
                            .label(IKey.lang("GT5U.gui.text.toolbox.radial.unselect"))
                            .onClicked(onClick(inventorySlot, ItemGTToolbox.NO_TOOL_SELECTED))
                            .done();

                        int count = 0;
                        for (ToolboxSlot slot : ToolboxSlot.TOOL_SLOTS) {
                            final ItemStack toolStack = itemHandler.getStackInSlot(slot.getSlotID());
                            if (toolStack != null) {
                                builder.option()
                                    .label(new ItemDrawable(toolStack).asIcon())
                                    .onClicked(onClick(inventorySlot, slot.getSlotID()))
                                    .done();
                                count++;
                            } else {
                                final Optional<UITexture> overlay = slot.getOverlay();
                                final Icon icon;

                                if (overlay.isPresent()) {
                                    icon = IDrawable.of(
                                        slot.getOverlay()
                                            .get(),
                                        GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                        .asIcon();
                                } else {
                                    icon = IDrawable.of(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                        .asIcon();
                                }

                                builder.option()
                                    .label(icon.size(16, 16))
                                    .onClicked(() -> false)
                                    .done();
                            }
                        }

                        if (count == 0) {
                            throw new RuntimeException("Tried to create the toolbox radial menu with no valid options");
                        }
                    })
                    .build()
                    .relativeToScreen()
                    .top(0)
                    .bottom(0)
                    .left(0)
                    .right(0));
            return panel;
        }

        private static Optional<Integer> getToolboxSlot(EntityPlayer player) {
            if (checkIfSlotIsToolbox(player, player.inventory.currentItem)) {
                return Optional.of(player.inventory.currentItem);
            } else if (checkIfSlotIsToolbox(player, Backhand.getOffhandSlot(player))) {
                return Optional.of(Backhand.getOffhandSlot(player));
            } else {
                // Included for players with slippery scroll wheels. Check the entire hotbar if the current item or
                // offhand both lack a toolbox and use the first one we find.
                for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
                    if (i == player.inventory.currentItem) {
                        continue;
                    }

                    if (checkIfSlotIsToolbox(player, i)) {
                        return Optional.of(i);
                    }
                }
            }

            return Optional.empty();
        }

        private static boolean checkIfSlotIsToolbox(final EntityPlayer player, final int slot) {
            if (slot == -1) {
                return false;
            }
            return player.inventory.getStackInSlot(slot) != null && player.inventory.getStackInSlot(slot)
                .getItem() instanceof ItemGTToolbox;
        }

        static BooleanSupplier onClick(final int inventorySlot, final int selection) {
            return () -> {
                GTValues.NW.sendToServer(
                    new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL, inventorySlot, selection));
                return true;
            };
        }
    }
}
