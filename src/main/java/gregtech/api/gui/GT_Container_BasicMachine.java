package gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class GT_Container_BasicMachine extends GT_Container_BasicTank {

    public boolean
            mFluidTransfer = false,
            mItemTransfer = false,
            mStuttering = false;

    private Runnable circuitSlotClickCallback;
    GT_Slot_Render slotCircuit;

    public GT_Container_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 107, 63));
        GT_MetaTileEntity_BasicMachine machine = (GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity();
        addSlotToContainer(slotCircuit = new GT_Slot_Render(mTileEntity, machine.getCircuitSlot(), 153, 63));

        int tStartIndex = machine.getInputSlot();

        switch (machine.mInputSlotCount) {
            case 0:
                break;
            case 1:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                break;
            case 2:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                break;
            case 3:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                break;
            case 4:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 34));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 34));
                break;
            case 5:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 34));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 34));
                break;
            case 6:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 16));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 34));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 34));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 34));
                break;
            case 7:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 43));
                break;
            case 8:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 43));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 43));
                break;
            default:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 43));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 43));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 43));
                break;
        }

        tStartIndex = machine.getOutputSlot();

        switch (machine.mOutputItems.length) {
            case 0:
                break;
            case 1:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                break;
            case 2:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 25));
                break;
            case 3:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 25));
                break;
            case 4:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 34));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 34));
                break;
            case 5:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 34));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 34));
                break;
            case 6:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 16));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 34));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 34));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 34));
                break;
            case 7:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 43));
                break;
            case 8:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 43));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 43));
                break;
            default:
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 7));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 25));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 107, 43));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 125, 43));
                addSlotToContainer(new GT_Slot_Output(mTileEntity, tStartIndex++, 143, 43));
                break;
        }

        addSlotToContainer(new Slot(mTileEntity, 1, 80, 63));
        addSlotToContainer(new Slot(mTileEntity, 3, 125, 63));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, tStartIndex++, 53, 63));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (mTileEntity.getMetaTileEntity() == null) return null;
        GT_MetaTileEntity_BasicMachine machine = (GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity();
        if (machine == null) return null;
        ItemStack tResultStack;
        switch (aSlotIndex) {
            case 0:
                machine.mFluidTransfer = !machine.mFluidTransfer;
                return null;
            case 1:
                machine.mItemTransfer = !machine.mItemTransfer;
                return null;
            case 3:
                if (machine.allowSelectCircuit() && aMouseclick < 2) {
                    ItemStack newCircuit;
                    if (aShifthold == 1) {
                        if (aMouseclick == 0) {
                            if (circuitSlotClickCallback != null)
                                circuitSlotClickCallback.run();
                            return null;
                        } else {
                            // clear
                            newCircuit = null;
                        }
                    } else {
                        ItemStack cursorStack = aPlayer.inventory.getItemStack();
                        List<ItemStack> tCircuits = machine.getConfigurationCircuits();
                        int index = GT_Utility.findMatchingStackInList(tCircuits, cursorStack);
                        if (index < 0) {
                            int curIndex = GT_Utility.findMatchingStackInList(tCircuits, machine.getStackInSlot(machine.getCircuitSlot())) + 1;
                            if (aMouseclick == 0) {
                                curIndex += 1;
                            } else {
                                curIndex -= 1;
                            }
                            curIndex = Math.floorMod(curIndex, tCircuits.size() + 1) - 1;
                            newCircuit = curIndex < 0 ? null : tCircuits.get(curIndex);
                        } else {
                            // set to whatever it is
                            newCircuit = tCircuits.get(index);
                        }
                    }
                    mTileEntity.setInventorySlotContents(machine.getCircuitSlot(), newCircuit);
                    return newCircuit;
                }
                return null;
            default:
                if (aSlotIndex == OTHER_SLOT_COUNT + 1 + machine.mInputSlotCount + machine.mOutputItems.length && aMouseclick < 2) {
                    if (mTileEntity.isClientSide()) {
                        // see parent class slotClick for an explanation on why doing this
                        GT_MetaTileEntity_BasicTank tTank = (GT_MetaTileEntity_BasicTank) mTileEntity.getMetaTileEntity();
                        tTank.setFillableStack(GT_Utility.getFluidFromDisplayStack(tTank.getStackInSlot(2)));
                    }
                    GT_MetaTileEntity_BasicTank tTank = (GT_MetaTileEntity_BasicTank) mTileEntity.getMetaTileEntity();
                    IFluidAccess tFillableAccess = IFluidAccess.from(tTank, true);
                    ItemStack tToken = handleFluidSlotClick(tFillableAccess, aPlayer, aMouseclick == 0, true, true);
                    if (mTileEntity.isServerSide() && tToken != null)
                        mTileEntity.markInventoryBeenModified();
                    return tToken;
                } else {
                    return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
                }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;

        mFluidTransfer = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mFluidTransfer;
        mItemTransfer = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mItemTransfer;
        mStuttering = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mStuttering;

        for (Object crafter : this.crafters) {
            ICrafting var1 = (ICrafting) crafter;
            var1.sendProgressBarUpdate(this, 102, mFluidTransfer ? 1 : 0);
            var1.sendProgressBarUpdate(this, 103, mItemTransfer ? 1 : 0);
            var1.sendProgressBarUpdate(this, 104, mStuttering ? 1 : 0);
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 102:
                mFluidTransfer = (par2 != 0);
                break;
            case 103:
                mItemTransfer = (par2 != 0);
                break;
            case 104:
                mStuttering = (par2 != 0);
                break;
        }
    }

    @Override
    public int getSlotStartIndex() {
        return 4;
    }

    @Override
    public int getShiftClickStartIndex() {
        return 4;
    }

    @Override
    public int getSlotCount() {
        return getShiftClickSlotCount() + ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mOutputItems.length + 2;
    }

    @Override
    public int getShiftClickSlotCount() {
        return ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mInputSlotCount;
    }

    public void setCircuitSlotClickCallback(Runnable circuitSlotClickCallback) {
        this.circuitSlotClickCallback = circuitSlotClickCallback;
    }
}
