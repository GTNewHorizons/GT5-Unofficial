package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchNonConsumableBaseGui;
import gregtech.common.tileentities.machines.ISmartInputHatch;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;

public abstract class MTEHatchNonConsumableBase extends MTEHatch
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, ISmartInputHatch {

    private ItemStack itemStack = null;
    private int itemCount = 0;
    private boolean isOutputSlotLocked = true;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this);
    private boolean changed = false;

    public MTEHatchNonConsumableBase(int ID, String name, String nameRegional, int tier, String description) {
        super(ID, name, nameRegional, tier, 3, new String[] { description, "Will keep its contents when broken" });
    }

    public MTEHatchNonConsumableBase(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 3, description, textures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack stack) {
        if (itemStack != stack) {
            changed = true;
        }

        itemStack = stack;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public void setItemCount(int amount) {
        if (itemCount != amount) {
            changed = true;
        }

        itemCount = amount;
    }

    public boolean hasChanged() {
        return changed;
    }

    public void unmarkChanged() {
        changed = false;
    }

    public boolean isOutputSlotLocked() {
        return isOutputSlotLocked;
    }

    public void setOutputSlotLocked(boolean outputSlotLocked) {
        isOutputSlotLocked = outputSlotLocked;
    }

    public abstract int getItemCapacity();

    @Override
    public abstract boolean isValidItem(ItemStack item);

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return isValidItem(itemStack);
    }

    @Override
    public ItemStack getExtraItemStack() {
        return mInventory[1];
    }

    @Override
    public void setExtraItemStack(ItemStack stack) {
        mInventory[1] = stack;
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add("Capacity: " + EnumChatFormatting.GOLD + getItemCapacity() + EnumChatFormatting.GRAY + " Items");
        if (ItemStackNBT.hasKey(stack, "itemStack")) {
            final ItemStack tContents = ItemStack
                .loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack"));
            final int tSize = stack.stackTagCompound.getInteger("itemCount");
            if (tContents != null && tSize > 0) {
                tooltip.add(
                    StatCollector.translateToLocalFormatted("gt.tileentity.chest_info", tContents.getDisplayName()));
                tooltip.add(StatCollector.translateToLocalFormatted("gt.tileentity.chest_amount", formatNumber(tSize)));
            }
        }
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver meMonitorHandlerReceiver, Object o) {
        meInventoryHandler.addListener(meMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver meMonitorHandlerReceiver) {
        meInventoryHandler.removeListener(meMonitorHandlerReceiver);
    }

    @Override
    public AccessRestriction getAccess() {
        return meInventoryHandler.getAccess();
    }

    @Override
    public boolean isPrioritized(IAEItemStack aeItemStack) {
        return meInventoryHandler.isPrioritized(aeItemStack);
    }

    @Override
    public boolean canAccept(IAEItemStack aeItemStack) {
        return meInventoryHandler.canAccept(aeItemStack);
    }

    @Override
    public int getPriority() {
        return meInventoryHandler.getPriority();
    }

    @Override
    public int getSlot() {
        return meInventoryHandler.getSlot();
    }

    @Override
    public boolean validForPass(int i) {
        return meInventoryHandler.validForPass(i);
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(final IItemList<IAEItemStack> out, int iteration) {
        return meInventoryHandler.getAvailableItems(out, iteration);
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        return meInventoryHandler.getStorageList();
    }

    @Override
    public IAEItemStack injectItems(final IAEItemStack input, final Actionable mode, final BaseActionSource src) {
        return meInventoryHandler.injectItems(input, mode, src);
    }

    @Override
    public IAEItemStack extractItems(final IAEItemStack request, final Actionable mode, final BaseActionSource src) {
        return meInventoryHandler.extractItems(request, mode, src);
    }

    @Override
    public StorageChannel getChannel() {
        return meInventoryHandler.getChannel();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {

        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            if ((getItemCount() <= 0)) {
                setItemStack(null);
                setItemCount(0);
            }
            if (getItemStack() == null && mInventory[0] != null && isValidItem(mInventory[0])) {
                setItemStack(mInventory[0].copy());
            }
            int count = getItemCount();
            ItemStack stack = getItemStack();
            int savedCount = count;

            if ((mInventory[0] != null) && ((count < getItemCapacity()))
                && GTUtility.areStacksEqual(mInventory[0], stack)) {
                count += mInventory[0].stackSize;
                if (count <= getItemCapacity()) {
                    mInventory[0] = null;
                } else {
                    mInventory[0].stackSize = (count - getItemCapacity());
                    count = getItemCapacity();
                }
            }
            if (mInventory[1] == null && stack != null && !isOutputSlotLocked) {
                mInventory[1] = stack.copy();
                mInventory[1].stackSize = Math.min(stack.getMaxStackSize(), count);
                count -= mInventory[1].stackSize;
            } else if ((count > 0) && GTUtility.areStacksEqual(mInventory[1], stack)
                && mInventory[1].getMaxStackSize() > mInventory[1].stackSize
                && !isOutputSlotLocked) {
                    int tmp = Math.min(count, mInventory[1].getMaxStackSize() - mInventory[1].stackSize);
                    mInventory[1].stackSize += tmp;
                    count -= tmp;
                }
            setItemCount(count);
            if (stack != null) {
                mInventory[2] = stack.copy();
                mInventory[2].stackSize = Math.min(stack.getMaxStackSize(), count);
            } else {
                mInventory[2] = null;
            }

            meInventoryHandler.notifyListeners(count - savedCount, stack);
            if (count != savedCount) {
                getBaseMetaTileEntity().markDirty();
                notifyWatchers();
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        NBTTagList invData = new NBTTagList();
        boolean hasInvData = false;
        for (int i = 0; i < 3; i++) {
            if (mInventory[i] != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                tNBT.setByte("count", (byte) mInventory[i].stackSize);
                tNBT.setShort("damage", (short) mInventory[i].getItemDamage());
                tNBT.setShort("id", (short) Item.getIdFromItem(mInventory[i].getItem()));
                tNBT.setInteger("intSlot", i);
                if (mInventory[i].hasTagCompound()) {
                    tNBT.setTag("tag", mInventory[i].getTagCompound());
                }
                invData.appendTag(tNBT);
                hasInvData = true;
            }
        }
        if (getItemStack() != null) aNBT.setTag("itemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        if (hasInvData) aNBT.setTag("inventory", invData);
        if (getItemCount() > 0) aNBT.setInteger("itemCount", getItemCount());

        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("itemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("itemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("outputLocked", isOutputSlotLocked);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("itemCount")) setItemCount(aNBT.getInteger("itemCount"));
        if (aNBT.hasKey("itemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("itemStack")));
        if (aNBT.hasKey("outputLocked")) isOutputSlotLocked = aNBT.getBoolean("outputLocked");
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && meInventoryHandler.hasActiveMEConnection()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && meInventoryHandler.hasActiveMEConnection()) return false;
        if (aIndex != 0) return false;
        if ((mInventory[0] != null && !GTUtility.areStacksEqual(mInventory[0], aStack))) return false;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchNonConsumableBaseGui(this).build(guiData, syncManager, uiSettings);
    }

    public int getSlotIndex() {
        return 2;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == getSlotIndex() ? 1 : super.getSlotLimit(slot);
    }
}
