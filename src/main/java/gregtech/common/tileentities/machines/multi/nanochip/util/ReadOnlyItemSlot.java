package gregtech.common.tileentities.machines.multi.nanochip.util;

import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReadOnlyItemSlot extends PhantomItemSlot {
    @Override
    public boolean onMouseScroll(ModularScreen.UpOrDown scrollDirection, int amount) {
        return true;
    }
    @Override

    public boolean handleDragAndDrop(@NotNull ItemStack draggedStack, int button) {
        return true;
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        return Result.SUCCESS;
    }
}
