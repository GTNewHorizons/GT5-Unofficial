package gregtech.common.gui.modularui.widget;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.value.IValue;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Platform;
import com.cleanroommc.modularui.value.ObjectValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;

public class ResizableItemDisplayWidget extends Widget<ResizableItemDisplayWidget> {

    private IValue<ItemStack> value;
    private boolean displayAmount = false;

    @Override
    public boolean isValidSyncHandler(SyncHandler syncHandler) {
        if (syncHandler instanceof GenericSyncValue<?>genericSyncValue && genericSyncValue.isOfType(ItemStack.class)) {
            this.value = genericSyncValue.cast();
            return true;
        }
        return false;
    }

    @Override
    protected WidgetThemeEntry<?> getWidgetThemeInternal(ITheme theme) {
        return theme.getItemSlotTheme();
    }

    private int contentOffsetX = 1, contentOffsetY = 1;

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        ItemStack item = value.getValue();
        if (!Platform.isStackEmpty(item)) {
            GuiDraw.drawItem(
                item,
                contentOffsetX,
                contentOffsetY,
                getArea().width - 2 * contentOffsetX,
                getArea().height - 2 * contentOffsetY,
                context.getCurrentDrawingZ());
            if (this.displayAmount) {
                GuiDraw.drawStandardSlotAmountText(item.stackSize, null, getArea());
            }
        }
    }

    public ResizableItemDisplayWidget item(IValue<ItemStack> itemSupplier) {
        this.value = itemSupplier;
        setValue(itemSupplier);
        return this;
    }

    public ResizableItemDisplayWidget item(ItemStack itemStack) {
        return item(new ObjectValue<>(itemStack));
    }

    public ResizableItemDisplayWidget displayAmount(boolean displayAmount) {
        this.displayAmount = displayAmount;
        return this;
    }
}
