package gregtech.api.interfaces;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.OutputHatchType;
import gregtech.api.util.GTUtility;

public interface IOutputHatch {

    /** Returns true when this Hatch can only accept specific fluids. */
    boolean isFiltered();

    /** Returns true when the given fluid id matches this Hatches filter exactly. */
    boolean isFilteredToFluid(GTUtility.FluidId id);

    /** Returns this Hatch's type. Used for output order sorting. */
    OutputHatchType getHatchType();

    default boolean storePartial(FluidStack stack) {
        return storePartial(stack, false);
    }

    /**
     * Attempt to store as many fluids as possible into the internal inventory of this output Hatch.
     * 
     * @param stack    The stack to insert. Will be modified by this method (will contain whatever fluids could not be
     *                 inserted; stackSize will be 0 when everything was inserted).
     * @param simulate When true this Hatch will not be modified.
     * @return True if the stack was fully inserted into the Hatch, false otherwise.
     */
    boolean storePartial(FluidStack stack, boolean simulate);

    /**
     * Creates a transaction from this output Hatch. The transaction copies this Hatch' state (inventory, etc) when
     * created, and subsequent calls on the transaction modify the copy of this Hatch. The transaction does not modify
     * this Hatch' state unless {@link IOutputHatchTransaction#commit()} is called.
     */
    IOutputHatchTransaction createTransaction();
}
