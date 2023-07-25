package gregtech.api.logic.interfaces;

import static com.google.common.primitives.Ints.saturatedCast;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import gregtech.api.enums.InventoryType;
import gregtech.api.logic.FluidInventoryLogic;

public interface FluidInventoryLogicHost extends IFluidHandler {

    /**
     * To be used for single blocks or when directly interacting with the controller
     * 
     * @param side The side from where fluids are being inputted or extracted from
     * @param type The type of inventory being accessed. For inputting its Input, For outputting its Output.
     * @return The Fluid Logic responsible for said type.
     */
    @Nullable
    FluidInventoryLogic getFluidLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type);

    /**
     * Only to be used by MultiBlockPart for accessing the Controller Inventory
     * 
     * @param type Type of inventory, is it Input or Output
     * @param id   ID of the locked inventory. A null id is all inventories of said controller of said type
     * @return The Fluid Logic responsible for everything that should be done with said inventory
     */
    @Nullable
    default FluidInventoryLogic getFluidLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return getFluidLogic(ForgeDirection.UNKNOWN, type);
    }

    @Override
    default boolean canDrain(ForgeDirection from, Fluid fluid) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        return logic != null;
    }

    @Override
    default boolean canFill(ForgeDirection from, Fluid fluid) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Input);
        return logic != null;
    }

    @Override
    default FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        if (logic == null) return null;
        return logic.drain(resource.getFluid(), resource.amount, !doDrain);
    }

    @Override
    default FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        if (logic == null) return null;
        return logic.drain(maxDrain, !doDrain);
    }

    @Override
    default int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Input);
        if (logic == null) return 0;
        return saturatedCast(logic.fill(resource.getFluid(), resource.amount, !doFill));
    }

    @Override
    default FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return null;
    }
}
