package gregtech.common.tileentities.machines.multi.nanochip.util;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

public class ReadOnlyItemSlot extends ItemSlot {

    @Override
    public boolean onMouseScroll(ModularScreen.UpOrDown scrollDirection, int amount) {
        return true;
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        return Result.SUCCESS;
    }
}
