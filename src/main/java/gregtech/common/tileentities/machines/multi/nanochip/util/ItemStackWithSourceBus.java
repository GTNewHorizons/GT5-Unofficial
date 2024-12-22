package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;

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
