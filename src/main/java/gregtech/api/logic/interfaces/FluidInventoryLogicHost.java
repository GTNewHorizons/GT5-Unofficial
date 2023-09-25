package gregtech.api.logic.interfaces;

import static com.google.common.primitives.Ints.saturatedCast;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
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
     * @return The Fluid Logic responsible for said type. Can return null if the side is invalid
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
    @Nonnull
    default FluidInventoryLogic getFluidLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return Objects.requireNonNull(getFluidLogic(ForgeDirection.UNKNOWN, type));
    }

    /**
     * Returns an empty set if the type is {@link InventoryType#Both} or when the machine isn't a controller.
     */
    @Nonnull
    default Set<Entry<UUID, FluidInventoryLogic>> getAllFluidInventoryLogics(@Nonnull InventoryType type) {
        return new HashSet<>();
    }

    @Override
    default boolean canDrain(@Nonnull ForgeDirection from, Fluid fluid) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        return logic != null;
    }

    @Override
    default boolean canFill(@Nonnull ForgeDirection from, Fluid fluid) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Input);
        return logic != null;
    }

    @Override
    @Nullable
    default FluidStack drain(@Nonnull ForgeDirection from, @Nonnull FluidStack resource, boolean doDrain) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        if (logic == null) return null;
        return logic.drain(resource.getFluid(), resource.amount, !doDrain);
    }

    @Override
    @Nullable
    default FluidStack drain(@Nonnull ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Output);
        if (logic == null) return null;
        return logic.drain(maxDrain, !doDrain);
    }

    @Override
    default int fill(@Nonnull ForgeDirection from, @Nonnull FluidStack resource, boolean doFill) {
        FluidInventoryLogic logic = getFluidLogic(from, InventoryType.Input);
        if (logic == null) return 0;
        return saturatedCast(logic.fill(resource.getFluid(), resource.amount, !doFill));
    }

    @Override
    @Nullable
    default FluidTankInfo[] getTankInfo(@Nonnull ForgeDirection from) {
        return null;
    }
}
