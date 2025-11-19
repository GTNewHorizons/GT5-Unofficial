package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchNbtConsumableGui;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public abstract class MTEHatchNbtConsumable extends MTEHatch implements IAddGregtechLogo {

    private final int inputSlotCount;
    private final int totalSlotCount;
    private final boolean allowDuplicateUsageTypes;

    public MTEHatchNbtConsumable(int aID, String aName, String aNameRegional, int aTier, int aInputSlots,
        String aDescription, boolean aAllowDuplicateTypes) {
        super(aID, aName, aNameRegional, aTier, aInputSlots * 2, aDescription);
        inputSlotCount = getInputSlotCount();
        totalSlotCount = getInputSlotCount() * 2;
        allowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    public MTEHatchNbtConsumable(String aName, int aTier, int aInputSlots, String[] aDescription,
        boolean aAllowDuplicateTypes, ITexture[][][] aTextures) {
        super(aName, aTier, aInputSlots * 2, aDescription, aTextures);
        inputSlotCount = getInputSlotCount();
        totalSlotCount = getInputSlotCount() * 2;
        allowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    @Override
    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    @Override
    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    public abstract int getInputSlotCount();

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public final boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    @Override
    public final boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    public abstract String getNameGUI();

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        validateUsageSlots();
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
            tryFillUsageSlots();
        }
    }

    public final void updateSlots() {
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) {
                mInventory[i] = null;
            }
            // Only moves items in the first four slots
            if (i <= getSlotID_LastInput()) {
                fillStacksIntoFirstSlots();
            }
        }
    }

    protected void validateUsageSlots() {
        for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
            if (mInventory[i] != null && mInventory[i].stackSize < 1) {
                mInventory[i] = null;
                this.markDirty();
            }
        }
    }

    // Only moves items in the first four slots
    protected final void fillStacksIntoFirstSlots() {
        GTUtility.compactInventory(this, 0, getSlotID_LastInput() + 1);
    }

    public final void tryFillUsageSlots() {
        int aSlotSpace = (inputSlotCount - getContentUsageSlots().size());
        if (aSlotSpace > 0) {
            Logger.INFO("We have empty usage slots. " + aSlotSpace);
            for (int i = getSlotID_FirstInput(); i <= getSlotID_LastInput(); i++) {
                ItemStack aStackToTryMove = mInventory[i];
                if (aStackToTryMove != null && isItemValidForUsageSlot(aStackToTryMove)) {
                    Logger.INFO("Trying to move stack from input slot " + i);
                    if (moveItemFromStockToUsageSlots(aStackToTryMove)) {
                        Logger.INFO("Updating Slots.");
                        updateSlots();
                    }
                }
            }
        }
    }

    private int getSlotID_FirstInput() {
        return 0;
    }

    private int getSlotID_LastInput() {
        return inputSlotCount - 1;
    }

    private int getSlotID_FirstUsage() {
        return inputSlotCount;
    }

    private int getSlotID_LastUsage() {
        return totalSlotCount - 1;
    }

    public final ArrayList<ItemStack> getContentUsageSlots() {
        ArrayList<ItemStack> aItems = new ArrayList<>();
        for (int i = inputSlotCount; i < totalSlotCount; i++) {
            if (mInventory[i] != null) {
                aItems.add(mInventory[i]);
            }
        }
        return aItems;
    }

    public final boolean moveItemFromStockToUsageSlots(ItemStack aStack) {
        return moveItemFromStockToUsageSlots(aStack, allowDuplicateUsageTypes);
    }

    public final boolean moveItemFromStockToUsageSlots(ItemStack aStack, boolean aAllowMultiOfSameTypeInUsageSlots) {
        if (aStack != null) {
            if (aStack.stackSize > 0) {

                if (!isItemValidForUsageSlot(aStack)) {
                    Logger.INFO("Stack not valid: " + ItemUtils.getItemName(aStack));
                    return false;
                }

                // Copy the input stack into a new object
                ItemStack aStackToMove = aStack.copy();
                // Set stack size of stack to move to 1.
                aStackToMove.stackSize = 1;
                // Did we set a stack in the usage slots?
                boolean aDidSet = false;
                // Did we find another of this item already in the usage slots?
                boolean aFoundMatching = false;
                // Continue processing with our new stack
                // First check for duplicates
                for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
                    if (mInventory[i] != null) {
                        if (GTUtility.areStacksEqual(aStackToMove, mInventory[i], true)) {
                            Logger.INFO("Found matching stack in slot " + i + ".");
                            aFoundMatching = true;
                            break;
                        }
                    }
                }
                // Then Move stack to Usage slots
                for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
                    if (mInventory[i] == null) {
                        if (!aFoundMatching || aAllowMultiOfSameTypeInUsageSlots) {
                            mInventory[i] = aStackToMove;
                            aDidSet = true;
                            Logger.INFO("Moving new stack to usage slots.");
                            break;
                        }
                    }
                }
                if (aDidSet) {
                    Logger.INFO("Depleting input stack size by 1.");
                    // Depleted one from the original input stack
                    aStack.stackSize--;
                }
                return aDidSet;
            }
        }
        return false;
    }

    @Override
    public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing() && isItemValidForUsageSlot(aStack)
            && aIndex < inputSlotCount;
    }

    /**
     * Items that get compared when checking for Usage Slot validity. Can return an empty map if
     * isItemValidForUsageSlot() is overridden.
     *
     * @return
     */
    public abstract ArrayList<ItemStack> getItemsValidForUsageSlots();

    /**
     * Checks if the given item is valid for Usage Slots. Can be overridden for easier handling if you already have
     * methods to check this.
     *
     * @param aStack
     * @return
     */
    public boolean isItemValidForUsageSlot(ItemStack aStack) {
        if (aStack != null) {
            for (ItemStack aValid : getItemsValidForUsageSlots()) {
                if (GTUtility.areStacksEqual(aStack, aValid, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchNbtConsumableGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }
}
