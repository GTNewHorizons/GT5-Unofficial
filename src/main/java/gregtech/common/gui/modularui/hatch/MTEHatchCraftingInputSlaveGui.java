package gregtech.common.gui.modularui.hatch;

import java.util.Locale;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave.RecipeOrder;

public final class MTEHatchCraftingInputSlaveGui {

    private MTEHatchCraftingInputSlaveGui() {}

    public static CycleButtonWidget createRecipeOrderButton(@Nullable MetaTileEntity originMTE) {
        MTEHatchCraftingInputSlave proxy = originMTE instanceof MTEHatchCraftingInputSlave slave ? slave : null;

        EnumSyncValue<RecipeOrder, ?> orderSyncer = new EnumSyncValue<>(
            RecipeOrder.class,
            () -> proxy == null ? RecipeOrder.NORMAL : proxy.getRecipeOrder(),
            order -> { if (proxy != null) proxy.setRecipeOrder(order); }).allowC2S();

        CycleButtonWidget button = new CycleButtonWidget().value(orderSyncer);
        for (RecipeOrder order : RecipeOrder.values()) {
            button.stateOverlay(order.ordinal(), getOrderIcon(order));
            button.tooltipBuilder(order.ordinal(), tooltip -> {
                tooltip.addLine(
                    IKey.lang(
                        "GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order.current",
                        IKey.lang(order.getLangKey())
                            .get()));
                tooltip.addLine(IKey.lang(getOrderDescriptionKey(order)));
                if (order == RecipeOrder.RANDOM) {
                    tooltip.addLine(IKey.lang("GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order.scanner"));
                }
            });
        }

        return button;
    }

    private static UITexture getOrderIcon(RecipeOrder order) {
        return switch (order) {
            case NORMAL -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_NORMAL;
            case REVERSED -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_REVERSED;
            case RANDOM -> GTGuiTextures.OVERLAY_BUTTON_CRIB_ORDER_RANDOM;
        };
    }

    private static String getOrderDescriptionKey(RecipeOrder order) {
        return "GT5U.gui.tooltip.hatch.crafting_input_slave.recipe_order." + order.name()
            .toLowerCase(Locale.ROOT);
    }
}
