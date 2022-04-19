package gregtech.api.gui;

import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_Container_2by2_Fluid extends GT_ContainerMetaTile_Machine {

    public GT_Container_2by2_Fluid(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    /**
     * Subclasses must ensure third slot (aSlotIndex==2) is drainable fluid display item slot.
     * Otherwise, subclasses must intercept the appropriate the slotClick event and call super.slotClick(2, xxx) if necessary
     */
    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 0, 71, 26));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 1, 89, 26));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 71, 44));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 3, 89, 44));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 4 && aSlotIndex >= 0 && aMouseclick < 2) {
            if (mTileEntity.isClientSide()) {
                /*
                 * While a logical client don't really need to process fluid cells upon click (it could have just wait
                 * for server side to send the result), doing so would result in every fluid interaction having a
                 * noticeable delay between clicking and changes happening even on single player.
                 * I'd imagine this lag to become only more severe when playing MP over ethernet, which would have much more latency
                 * than a memory connection
                 */
                GT_MetaTileEntity_Hatch_MultiInput tTank = (GT_MetaTileEntity_Hatch_MultiInput) mTileEntity.getMetaTileEntity();
                tTank.setDrainableStack(GT_Utility.getFluidFromDisplayStack(tTank.getStackInSlot(2)));
            }
            GT_MetaTileEntity_Hatch_MultiInput tTank = (GT_MetaTileEntity_Hatch_MultiInput) mTileEntity.getMetaTileEntity();
            MultiFluidAccess tDrainableAccess = MultiFluidAccess.from(tTank, aSlotIndex);
            ItemStack tStackHeld = aPlayer.inventory.getItemStack();
            FluidStack tFluidHeld = GT_Utility.getFluidForFilledItem(tStackHeld, true);
            if (tDrainableAccess.isMatch(tFluidHeld, aSlotIndex))
                return handleFluidSlotClick(tDrainableAccess, aPlayer, aMouseclick == 0, true, !tTank.isDrainableStackSeparate());
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }

    static class MultiFluidAccess implements IFluidAccess {
        private final GT_MetaTileEntity_Hatch_MultiInput mTank;
        private final int mSlot;

        public MultiFluidAccess(GT_MetaTileEntity_Hatch_MultiInput aTank, int aSlot) {
            this.mTank = aTank;
            this.mSlot = aSlot;
        }

        public boolean isMatch(FluidStack stack, int slot) {
            if (!mTank.hasFluid(stack)) return true;
            if (stack == null) return true;
            return stack.equals(mTank.getFluid(slot));
        }

        @Override
        public void set(FluidStack stack) {
            mTank.setFluid(stack, mSlot);
        }

        @Override
        public FluidStack get() {
            return mTank.getFluid(mSlot);
        }

        @Override
        public int getCapacity() {
            return mTank.getCapacity();
        }

        static MultiFluidAccess from(GT_MetaTileEntity_Hatch_MultiInput aTank, int aSlot) {
            return new MultiFluidAccess(aTank, aSlot);
        }
    }
}
