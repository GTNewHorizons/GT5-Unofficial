package gregtech.common.gui.modularui.hatch;

import java.util.Locale;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave.RecipeOrder;

/** Adds the recipe order button of a Crafting Input Proxy to the GUI of its Crafting Input Buffer/Bus. */
public final class MTEHatchCraftingInputSlaveGui {

    private static final int BUTTON_SIZE = 18;
    private static final int BUTTON_MARGIN = 5;
    /** The button row of that GUI is 86 pixels above the bottom edge of the panel. */
    private static final int BUTTON_ROW_BOTTOM_MARGIN = 86;

    private MTEHatchCraftingInputSlaveGui() {}

    /** @param originMTE the proxy this GUI was opened with, which is only known on the server side */
    public static void addRecipeOrderButton(ModularPanel panel, PanelSyncManager syncManager,
        @Nullable MetaTileEntity originMTE) {
        MTEHatchCraftingInputSlave proxy = originMTE instanceof MTEHatchCraftingInputSlave slave ? slave : null;

        EnumSyncValue<RecipeOrder, ?> orderSyncer = new EnumSyncValue<>(
            RecipeOrder.class,
            () -> proxy == null ? RecipeOrder.NORMAL : proxy.getRecipeOrder(),
            order -> {
                if (proxy != null) proxy.setRecipeOrder(order);
            }).allowC2S();

        CycleButtonWidget button = new CycleButtonWidget().value(orderSyncer);
        for (RecipeOrder order : RecipeOrder.values()) {
            button.stateOverlay(order.ordinal(), getOrderIcon(order));
            button.tooltipBuilder(order.ordinal(), tooltip -> {
                tooltip.addLine(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order.current",
                        StatCollector.translateToLocal(MTEHatchCraftingInputSlave.getRecipeOrderLangKey(order))));
                tooltip.addLine(
                    StatCollector
                        .translateToLocal(getOrderDescriptionKey(order)));
                if (order == RecipeOrder.RANDOM) {
                    tooltip.addLine(
                        StatCollector.translateToLocal(
                            "GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order.scanner"));
                }
            });
        }

        panel.child(
            button.size(BUTTON_SIZE)
                .relative(panel)
                .leftRelOffset(0, -BUTTON_SIZE - BUTTON_MARGIN)
                .bottomRelOffset(0, BUTTON_ROW_BOTTOM_MARGIN));
    }

    private static UITexture getOrderIcon(RecipeOrder order) {
        return switch (order) {
            case NORMAL -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_NORMAL;
            case REVERSED -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_REVERSED;
            case RANDOM -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_RANDOM;
        };
    }

    private static String getOrderDescriptionKey(RecipeOrder order) {
        return "GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order."
            + order.name().toLowerCase(Locale.ROOT);
    }
}
