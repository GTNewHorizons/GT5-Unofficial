package gregtech.api.interfaces.tileentity;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.fluid.IFluidStore;

/**
 * Machines implementing this interface can have logic to configure whether to void excess output or not.
 */
public interface IVoidable {

    /**
     * @return if this machine can prevent excess item and fluid from voiding.
     */
    boolean supportsVoidProtection();

    /**
     * @return if this machine is configured to not void excess item.
     */
    default boolean protectsExcessItem() {
        return supportsVoidProtection() && getVoidingMode().protectItem;
    }

    /**
     * @return if this machine is configured to not void excess fluid.
     */
    default boolean protectsExcessFluid() {
        return supportsVoidProtection() && getVoidingMode().protectFluid;
    }

    VoidingMode getVoidingMode();

    void setVoidingMode(VoidingMode mode);

    /**
     * @param toOutput List of items this machine is going to output.
     * @return List of slots available for item outputs. Null element represents empty slot.
     */
    List<ItemStack> getItemOutputSlots(ItemStack[] toOutput);

    /**
     * @param toOutput List of fluids this machine is going to output.
     * @return List of slots available for fluid outputs.
     */
    List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput);

    /**
     * @return If this machine has ability to dump item outputs to ME network.
     *         This doesn't need to check if it can actually dump to ME,
     *         as this might be called every tick and cause lag.
     */
    boolean canDumpItemToME();

    /**
     * @return If this machine has ability to dump fluid outputs to ME network.
     *         This doesn't need to check if it can actually dump to ME,
     *         as this might be called every tick and cause lag.
     */
    boolean canDumpFluidToME();
}
