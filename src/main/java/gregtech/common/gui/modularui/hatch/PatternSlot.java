package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import appeng.api.config.TerminalFontSize;
import appeng.api.storage.data.IAEStack;
import appeng.client.render.StackSizeRenderer;
import appeng.items.misc.ItemEncodedPattern;
import gregtech.api.modularui2.GTGuiTextures;

public class PatternSlot extends ItemSlot {

    PatternSlot() {}

    @Override
    public @Nullable IDrawable getCurrentBackground(ITheme theme, WidgetThemeEntry<?> widgetTheme) {
        IDrawable background = super.getCurrentBackground(theme, widgetTheme);
        return IDrawable.of(background, GTGuiTextures.OVERLAY_SLOT_PATTERN_ME);
    }

    @Override
    protected ItemStack getItemStackForRendering(ItemStack stack, boolean dragging) {
        if (dragging || stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
            return stack;
        }
        IAEStack<?> outputAE = patternItem.getOutputAE(stack);
        if (outputAE == null) {
            return stack;
        }
        ItemStack output = outputAE.getItemStackForNEI(0);
        return output != null ? output : stack;
    }

    @Override
    protected void drawSlotAmountText(int amount, String format) {
        ModularSlot slot = this.getSlot();
        ItemStack stack = slot.getStack();
        if (stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
            super.drawSlotAmountText(amount, format);
            return;
        }
        IAEStack<?> outputAE = patternItem.getOutputAE(stack);
        if (outputAE == null) {
            super.drawSlotAmountText(amount, format);
            return;
        }

        long stackSize = outputAE.getStackSize();
        if (stackSize > 1) {
            StackSizeRenderer.drawStackSize(1, 1, stackSize, getContext().getFontRenderer(), TerminalFontSize.SMALL);
        }
    }
}
