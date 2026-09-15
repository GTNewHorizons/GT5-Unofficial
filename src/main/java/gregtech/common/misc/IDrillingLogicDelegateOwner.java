package gregtech.common.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/** @author Relvl on 27.01.2022 */
public interface IDrillingLogicDelegateOwner extends IMetaTileEntity {

    /** Returns the machine actual tier. */
    int getMachineTier();

    /** Returns the machine current processing speed. */
    int getMachineSpeed();

    /** Pulls (or check can pull) items from an input slots. */
    boolean pullInputs(Item item, int count, boolean simulate);

    /** Pushes (or check can push) item to output slots. */
    boolean pushOutputs(ItemStack stack, int count, boolean simulate, boolean allowInputSlots);
}
