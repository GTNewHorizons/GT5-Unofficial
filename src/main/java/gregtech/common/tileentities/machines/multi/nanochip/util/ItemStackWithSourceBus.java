package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;

public class ItemStackWithSourceBus {

    @NotNull
    public final ItemStack stack;
    @NotNull
    public final GT_MetaTileEntity_Hatch_InputBus bus;

    public ItemStackWithSourceBus(@NotNull ItemStack stack, @NotNull GT_MetaTileEntity_Hatch_InputBus bus) {
        this.stack = stack;
        this.bus = bus;
    }
}
