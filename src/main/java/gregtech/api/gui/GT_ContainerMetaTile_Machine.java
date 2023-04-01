package gregtech.api.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my MetaTileEntities
 */
public class GT_ContainerMetaTile_Machine extends GT_Container {

    public int mActive = 0, mMaxProgressTime = 0, mProgressTime = 0, mEnergy = 0, mSteam = 0, mSteamStorage = 0,
            mStorage = 0, mOutput = 0, mInput = 0, mID = 0, mDisplayErrorCode = 0;
    public long mEnergyLong = 0, mStorageLong = 0;
    private int oActive = 0, oMaxProgressTime = 0, oProgressTime = 0, oEnergy = 0, oSteam = 0, oSteamStorage = 0,
            oStorage = 0, oOutput = 0, oInput = 0, oID = 0, oDisplayErrorCode = 0;
    private long oEnergyLong = 0, oStorageLong = 0;
    protected int mTimer = 0;
    protected Runnable circuitSlotClickCallback;

    public GT_ContainerMetaTile_Machine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);

        mTileEntity = aTileEntity;

        if (mTileEntity != null && mTileEntity.getMetaTileEntity() != null) {
            addSlots(aInventoryPlayer);
            if (doesBindPlayerInventory()) bindPlayerInventory(aInventoryPlayer);
            detectAndSendChanges();
        } else {
            aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
        }
    }

    public GT_ContainerMetaTile_Machine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity,
            boolean doesBindInventory) {
        super(aInventoryPlayer, aTileEntity);
        mTileEntity = aTileEntity;

        if (mTileEntity != null && mTileEntity.getMetaTileEntity() != null) {
            addSlots(aInventoryPlayer);
            if (doesBindPlayerInventory() && doesBindInventory) bindPlayerInventory(aInventoryPlayer);
            detectAndSendChanges();
        } else {
            aInventoryPlayer.player.openContainer = aInventoryPlayer.player.inventoryContainer;
        }
    }

    protected void addCircuitSlot() {
        if (mTileEntity.getMetaTileEntity() instanceof IConfigurationCircuitSupport ccs) {
            GT_Slot_Render slotCircuit = new GT_Slot_Render(
                    mTileEntity,
                    ccs.getCircuitSlot(),
                    ccs.getCircuitSlotX(),
                    ccs.getCircuitSlotY());
            addSlotToContainer(slotCircuit);
            slotCircuit.setEnabled(ccs.allowSelectCircuit());
        }
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addCircuitSlot();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
        mStorage = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getEUCapacity());
        mStorageLong = mTileEntity.getEUCapacity();
        mEnergy = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getStoredEU());
        mEnergyLong = mTileEntity.getStoredEU();
        mSteamStorage = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getSteamCapacity());
        mSteam = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getStoredSteam());
        mOutput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getOutputVoltage());
        mInput = (int) Math.min(Integer.MAX_VALUE, mTileEntity.getInputVoltage());
        mDisplayErrorCode = mTileEntity.getErrorDisplayID();
        mProgressTime = mTileEntity.getProgress();
        mMaxProgressTime = mTileEntity.getMaxProgress();
        mActive = mTileEntity.isActive() ? 1 : 0;
        mTimer++;

        for (ICrafting crafter : this.crafters) {
            ICrafting player = crafter;
            if (mTimer % 500 == 10 || oEnergy != mEnergy) {
                player.sendProgressBarUpdate(this, 0, mEnergy & 65535);
                player.sendProgressBarUpdate(this, 1, mEnergy >>> 16);
            }
            if (mTimer % 500 == 10 || oStorage != mStorage) {
                player.sendProgressBarUpdate(this, 2, mStorage & 65535);
                player.sendProgressBarUpdate(this, 3, mStorage >>> 16);
            }
            if (mTimer % 500 == 10 || oOutput != mOutput) {
                player.sendProgressBarUpdate(this, 4, mOutput);
            }
            if (mTimer % 500 == 10 || oInput != mInput) {
                player.sendProgressBarUpdate(this, 5, mInput);
            }
            if (mTimer % 500 == 10 || oDisplayErrorCode != mDisplayErrorCode) {
                player.sendProgressBarUpdate(this, 6, mDisplayErrorCode);
            }
            if (mTimer % 500 == 10 || oProgressTime != mProgressTime) {
                player.sendProgressBarUpdate(this, 11, mProgressTime & 65535);
                player.sendProgressBarUpdate(this, 12, mProgressTime >>> 16);
            }
            if (mTimer % 500 == 10 || oMaxProgressTime != mMaxProgressTime) {
                player.sendProgressBarUpdate(this, 13, mMaxProgressTime & 65535);
                player.sendProgressBarUpdate(this, 14, mMaxProgressTime >>> 16);
            }
            if (mTimer % 500 == 10 || oID != mID) {
                player.sendProgressBarUpdate(this, 15, mID);
            }
            if (mTimer % 500 == 10 || oActive != mActive) {
                player.sendProgressBarUpdate(this, 16, mActive);
            }
            if (mTimer % 500 == 10 || oSteam != mSteam) {
                player.sendProgressBarUpdate(this, 17, mSteam & 65535);
                player.sendProgressBarUpdate(this, 18, mSteam >>> 16);
            }
            if (mTimer % 500 == 10 || oSteamStorage != mSteamStorage) {
                player.sendProgressBarUpdate(this, 19, mSteamStorage & 65535);
                player.sendProgressBarUpdate(this, 20, mSteamStorage >>> 16);
            }
            if (mTimer % 500 == 10 || oEnergyLong != mEnergyLong) {
                player.sendProgressBarUpdate(this, 21, (int) mEnergyLong);
                player.sendProgressBarUpdate(this, 22, (int) (mEnergyLong >>> 32));
            }
            if (mTimer % 500 == 10 || oStorageLong != mStorageLong) {
                player.sendProgressBarUpdate(this, 23, (int) mStorageLong);
                player.sendProgressBarUpdate(this, 24, (int) (mStorageLong >>> 32));
            }
        }

        oID = mID;
        oSteam = mSteam;
        oInput = mInput;
        oActive = mActive;
        oOutput = mOutput;
        oEnergy = mEnergy;
        oEnergyLong = mEnergyLong;
        oStorage = mStorage;
        oStorageLong = mStorageLong;
        oSteamStorage = mSteamStorage;
        oProgressTime = mProgressTime;
        oMaxProgressTime = mMaxProgressTime;
        oDisplayErrorCode = mDisplayErrorCode;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        switch (id) {
            case 0 -> mEnergy = mEnergy & 0xffff0000 | value & 0x0000ffff;
            case 1 -> mEnergy = mEnergy & 0x0000ffff | value << 16;
            case 2 -> mStorage = mStorage & 0xffff0000 | value & 0x0000ffff;
            case 3 -> mStorage = mStorage & 0x0000ffff | value << 16;
            case 4 -> mOutput = value;
            case 5 -> mInput = value;
            case 6 -> mDisplayErrorCode = value;
            case 11 -> mProgressTime = mProgressTime & 0xffff0000 | value;
            case 12 -> mProgressTime = mProgressTime & 0x0000ffff | value << 16;
            case 13 -> mMaxProgressTime = mMaxProgressTime & 0xffff0000 | value & 0x0000ffff;
            case 14 -> mMaxProgressTime = mMaxProgressTime & 0x0000ffff | value << 16;
            case 15 -> mID = value;
            case 16 -> mActive = value;
            case 17 -> mSteam = mSteam & 0xffff0000 | value & 0x0000ffff;
            case 18 -> mSteam = mSteam & 0x0000ffff | value << 16;
            case 19 -> mSteamStorage = mSteamStorage & 0xffff0000 | value & 0x0000ffff;
            case 20 -> mSteamStorage = mSteamStorage & 0x0000ffff | value << 16;
            case 21 -> mEnergyLong = mEnergyLong & 0xffffffff00000000L | value & 0x00000000ffffffffL;
            case 22 -> mEnergyLong = mEnergyLong & 0x00000000ffffffffL | (long) value << 32;
            case 23 -> mStorageLong = mStorageLong & 0xffffffff00000000L | value & 0x00000000ffffffffL;
            case 24 -> mStorageLong = mStorageLong & 0x00000000ffffffffL | (long) value << 32;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return mTileEntity.isUseableByPlayer(player);
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }

    public void setCircuitSlotClickCallback(Runnable circuitSlotClickCallback) {
        this.circuitSlotClickCallback = circuitSlotClickCallback;
    }

    @Override
    public ItemStack slotClick(int aSlotNumber, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (mTileEntity.getMetaTileEntity() instanceof IConfigurationCircuitSupport) {
            IMetaTileEntity machine = mTileEntity.getMetaTileEntity();
            IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) machine;
            if (ccs.allowSelectCircuit() && aSlotNumber == ccs.getCircuitGUISlot() && aMouseclick < 2) {
                ItemStack newCircuit;
                if (aShifthold == 1) {
                    if (aMouseclick == 0) {
                        if (circuitSlotClickCallback != null) circuitSlotClickCallback.run();
                        return null;
                    } else {
                        // clear
                        newCircuit = null;
                    }
                } else {
                    ItemStack cursorStack = aPlayer.inventory.getItemStack();
                    List<ItemStack> tCircuits = ccs.getConfigurationCircuits();
                    int index = GT_Utility.findMatchingStackInList(tCircuits, cursorStack);
                    if (index < 0) {
                        int curIndex = GT_Utility.findMatchingStackInList(
                                tCircuits,
                                machine.getStackInSlot(ccs.getCircuitSlot())) + 1;
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
                mTileEntity.setInventorySlotContents(ccs.getCircuitSlot(), newCircuit);
                return newCircuit;
            }
        }
        return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
    }
}
