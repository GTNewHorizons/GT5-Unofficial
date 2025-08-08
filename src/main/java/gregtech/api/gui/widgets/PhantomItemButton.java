package gregtech.api.gui.widgets;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IItemLockable;

/**
 * Creates a phantom item in a GUI. Useful for filtering.
 */
public class PhantomItemButton extends SlotWidget {

    public static final IDrawable[] FILTER_BACKGROUND = { ModularUITextures.ITEM_SLOT,
        GTUITextures.OVERLAY_SLOT_FILTER };

    public PhantomItemButton(final IItemLockable delegate) {
        super(BaseSlot.phantom(new PhantomItemDelegate(delegate), 0));
        controlsAmount = false;
    }

    @Override
    public List<Text> getTooltip() {
        return ImmutableList.of(new Text(StatCollector.translateToLocal("GT5U.bus.filterTooltip.empty")));
    }

    @Override
    public List<String> getExtraTooltip() {
        return ImmutableList.of(StatCollector.translateToLocal("GT5U.bus.filterTooltip.full"));
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class PhantomItemDelegate implements IItemHandlerModifiable {

        private final IItemLockable delegate;

        public PhantomItemDelegate(final IItemLockable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack itemStack) {
            delegate.setLockedItem(itemStack);
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return delegate.getLockedItem();
        }

        @Nullable
        @Override
        public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
            delegate.setLockedItem(itemStack);
            return null;
        }

        @Nullable
        @Override
        public ItemStack extractItem(int var1, int var2, boolean var3) {
            throw new NotImplementedException("Extract item is disabled for GhostItemButtons.");
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}
