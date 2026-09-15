package gregtech.common.gui.modularui.util;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class LimitedExtractionSlot extends ModularSlot {

    private final BooleanSupplier condition;

    public LimitedExtractionSlot(IItemHandler itemHandler, int index, BooleanSupplier condition) {
        super(itemHandler, index);
        this.condition = condition;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return condition.getAsBoolean() && super.canTakeStack(playerIn);
    }

    public static BiFunction<IItemHandler, Integer, LimitedExtractionSlot> supplier(BooleanSupplier condition) {
        return (itemHandler, index) -> new LimitedExtractionSlot(itemHandler, index, condition);
    }
}
