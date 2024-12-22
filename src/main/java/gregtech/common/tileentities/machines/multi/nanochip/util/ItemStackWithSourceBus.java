package gregtech.common.tileentities.machines.multi.nanochip.util;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;


public class ItemStackWithSourceBus {

    @NotNull
    public final ItemStack stack;
    @NotNull
    public final MTEHatchInputBus bus;

    public ItemStackWithSourceBus(@NotNull ItemStack stack, @NotNull MTEHatchInputBus bus) {
        this.stack = stack;
        this.bus = bus;
    }
}
