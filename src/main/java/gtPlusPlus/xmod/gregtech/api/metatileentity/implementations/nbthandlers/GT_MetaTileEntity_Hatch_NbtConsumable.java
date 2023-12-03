package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public abstract class GT_MetaTileEntity_Hatch_NbtConsumable extends GT_MetaTileEntity_Hatch
        implements IAddGregtechLogo {

    private final int mInputslotCount;
    private final int mTotalSlotCount;
    private final boolean mAllowDuplicateUsageTypes;

    public GT_MetaTileEntity_Hatch_NbtConsumable(int aID, String aName, String aNameRegional, int aTier,
            int aInputSlots, String aDescription, boolean aAllowDuplicateTypes) {
        super(aID, aName, aNameRegional, aTier, aInputSlots * 2, aDescription);
        mInputslotCount = getInputSlotCount();
        mTotalSlotCount = getInputSlotCount() * 2;
        mAllowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    public GT_MetaTileEntity_Hatch_NbtConsumable(String aName, int aTier, int aInputSlots, String[] aDescription,
            boolean aAllowDuplicateTypes, ITexture[][][] aTextures) {
        super(aName, aTier, aInputSlots * 2, aDescription, aTextures);
        mInputslotCount = getInputSlotCount();
        mTotalSlotCount = getInputSlotCount() * 2;
        mAllowDuplicateUsageTypes = aAllowDuplicateTypes;
    }

    @Override
    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    @Override
    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    public abstract int getInputSlotCount();

    @Override
    public final boolean isSimpleMachine() {
        return true;
    }

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
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
        for (int i = 0; i <= getSlotID_LastInput(); i++) {
            for (int j = i + 1; j <= getSlotID_LastInput(); j++) {
                if (mInventory[j] != null
                        && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j]))) {
                    GT_Utility.moveStackFromSlotAToSlotB(
                            getBaseMetaTileEntity(),
                            getBaseMetaTileEntity(),
                            j,
                            i,
                            (byte) 64,
                            (byte) 1,
                            (byte) 64,
                            (byte) 1);
                }
            }
        }
    }

    public final void tryFillUsageSlots() {
        int aSlotSpace = (mInputslotCount - getContentUsageSlots().size());
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
        return mInputslotCount - 1;
    }

    private int getSlotID_FirstUsage() {
        return mInputslotCount;
    }

    private int getSlotID_LastUsage() {
        return mTotalSlotCount - 1;
    }

    public final AutoMap<ItemStack> getContentUsageSlots() {
        AutoMap<ItemStack> aItems = new AutoMap<>();
        for (int i = mInputslotCount; i < mTotalSlotCount; i++) {
            if (mInventory[i] != null) {
                aItems.add(mInventory[i]);
            }
        }
        return aItems;
    }

    public final boolean moveItemFromStockToUsageSlots(ItemStack aStack) {
        return moveItemFromStockToUsageSlots(aStack, mAllowDuplicateUsageTypes);
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
                        if (GT_Utility.areStacksEqual(aStackToMove, mInventory[i], true)) {
                            Logger.INFO("Found matching stack in slot " + i + ".");
                            aFoundMatching = true;
                            break;
                        }
                    }
                }
                // Then Move stack to Usage slots
                for (int i = getSlotID_FirstUsage(); i <= getSlotID_LastUsage(); i++) {
                    if (mInventory[i] == null) {
                        if ((aFoundMatching && aAllowMultiOfSameTypeInUsageSlots) || !aFoundMatching) {
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
                && aIndex < mInputslotCount;
    }

    /**
     * Items that get compared when checking for Usage Slot validity. Can return an empty map if
     * isItemValidForUsageSlot() is overridden.
     * 
     * @return
     */
    public abstract AutoMap<ItemStack> getItemsValidForUsageSlots();

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
                if (GT_Utility.areStacksEqual(aStack, aValid, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        switch (mTotalSlotCount) {
            case 8, 18 -> builder.widget(
                    new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo()).setSize(17, 17)
                            .setPos(152, 63));
            case 32 -> builder.widget(
                    new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo()).setSize(17, 17)
                            .setPos(79, 35));
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mTotalSlotCount) {
            case 8 -> {
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 2).startFromSlot(0).endAtSlot(3).build()
                                .setPos(25, 25));
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 2).startFromSlot(4).endAtSlot(7).canInsert(false)
                                .build().setPos(115, 25));
                builder.widget(new TextWidget("Stock").setDefaultColor(COLOR_TEXT_GRAY.get()).setPos(25, 16))
                        .widget(new TextWidget("Active").setDefaultColor(COLOR_TEXT_GRAY.get()).setPos(115, 16));
            }
            case 18 -> {
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 3).startFromSlot(0).endAtSlot(8).build()
                                .setPos(25, 19));
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 3).startFromSlot(9).endAtSlot(17).canInsert(false)
                                .build().setPos(97, 19));
                builder.widget(new TextWidget("Stock").setDefaultColor(COLOR_TEXT_GRAY.get()).setPos(25, 14))
                        .widget(new TextWidget("Active").setDefaultColor(COLOR_TEXT_GRAY.get()).setPos(15, 14));
            }
            case 32 -> {
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 4).startFromSlot(0).endAtSlot(15).build()
                                .setPos(7, 7));
                builder.widget(
                        SlotGroup.ofItemHandler(inventoryHandler, 4).startFromSlot(16).endAtSlot(31).canInsert(false)
                                .build().setPos(96, 7));
            }
        }
    }
}
