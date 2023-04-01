package gregtech.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_Utility;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Tanks
 */
public class GT_Container_BasicTank extends GT_ContainerMetaTile_Machine {

    public int mContent = 0;
    protected int oContent = 0;

    public GT_Container_BasicTank(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    /**
     * Subclasses must ensure third slot (aSlotIndex==2) is drainable fluid display item slot. Otherwise, subclasses
     * must intercept the appropriate the slotClick event and call super.slotClick(2, xxx) if necessary
     */
    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 0, 80, 17));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 1, 80, 53));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 59, 42));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex == 2 && aMouseclick < 2) {
            GT_MetaTileEntity_BasicTank tTank = (GT_MetaTileEntity_BasicTank) mTileEntity.getMetaTileEntity();
            if (mTileEntity.isClientSide()) {
                /*
                 * While a logical client don't really need to process fluid cells upon click (it could have just wait
                 * for server side to send the result), doing so would result in every fluid interaction having a
                 * noticeable delay between clicking and changes happening even on single player. I'd imagine this lag
                 * to become only more severe when playing MP over ethernet, which would have much more latency than a
                 * memory connection
                 */
                Slot slot = (Slot) inventorySlots.get(aSlotIndex);
                tTank.setDrainableStack(GT_Utility.getFluidFromDisplayStack(slot.getStack()));
            }
            IFluidAccess tDrainableAccess = constructFluidAccess(tTank, false);
            return handleFluidSlotClick(
                    tDrainableAccess,
                    aPlayer,
                    aMouseclick == 0,
                    true,
                    !tTank.isDrainableStackSeparate());
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
        if (((GT_MetaTileEntity_BasicTank) mTileEntity.getMetaTileEntity()).mFluid != null)
            mContent = ((GT_MetaTileEntity_BasicTank) mTileEntity.getMetaTileEntity()).mFluid.amount;
        else mContent = 0;
        sendProgressBar();
        oContent = mContent;
    }

    public void sendProgressBar() {
        for (ICrafting crafter : this.crafters) {
            ICrafting player = crafter;
            if (mTimer % 500 == 0 || oContent != mContent) {
                player.sendProgressBarUpdate(this, 100, mContent & 65535);
                player.sendProgressBarUpdate(this, 101, mContent >>> 16);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        switch (id) {
            case 100 -> mContent = mContent & 0xffff0000 | value & 0x0000ffff;
            case 101 -> mContent = mContent & 0xffff | value << 16;
        }
    }

    @Override
    public int getSlotCount() {
        return 2;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 1;
    }

    protected IFluidAccess constructFluidAccess(GT_MetaTileEntity_BasicTank aTank, boolean aIsFillableStack) {
        return new BasicTankFluidAccess(aTank, aIsFillableStack);
    }

    static class BasicTankFluidAccess implements IFluidAccess {

        protected final GT_MetaTileEntity_BasicTank mTank;
        protected final boolean mIsFillableStack;

        public BasicTankFluidAccess(GT_MetaTileEntity_BasicTank aTank, boolean aIsFillableStack) {
            this.mTank = aTank;
            this.mIsFillableStack = aIsFillableStack;
        }

        @Override
        public void set(FluidStack stack) {
            if (mIsFillableStack) mTank.setFillableStack(stack);
            else mTank.setDrainableStack(stack);
        }

        @Override
        public FluidStack get() {
            return mIsFillableStack ? mTank.getFillableStack() : mTank.getDrainableStack();
        }

        @Override
        public int getCapacity() {
            return mTank.getCapacity();
        }
    }
}
