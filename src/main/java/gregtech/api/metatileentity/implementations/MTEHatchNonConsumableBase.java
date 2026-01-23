package gregtech.api.metatileentity.implementations;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;

public abstract class MTEHatchNonConsumableBase extends MTEHatch
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, IAddUIWidgets {

    private ItemStack itemStack = null;
    private int itemCount = 0;
    private boolean isOutputSlotLocked = true;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this);

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
        itemStack = stack;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public void setItemCount(int amount) {
        itemCount = amount;
    }

    @Override
    public abstract int getItemCapacity();

    @Override
    public abstract boolean isValidItem(ItemStack item);

    @Override
    public ItemStack getExtraItemStack() {
        return mInventory[1];
    }

    @Override
    public void setExtraItemStack(ItemStack stack) {
        mInventory[1] = stack;
    }

    protected int clientItemCount;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, 0)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                SlotWidget.phantom(inventoryHandler, 2)
                    .disableInteraction()
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(59, 42))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.item_amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(clientItemCount))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> { isOutputSlotLocked = !isOutputSlotLocked; })
                    .setBackground(
                        () -> new UITexture[] {
                            isOutputSlotLocked ? GTUITextures.BUTTON_STANDARD_PRESSED : GTUITextures.BUTTON_STANDARD,
                            isOutputSlotLocked ? GTUITextures.OVERLAY_BUTTON_RECIPE_LOCKED
                                : GTUITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED })
                    .addTooltip(translateToLocal("GT5U.gui.button.toggle_output_slot_lock"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(() -> isOutputSlotLocked, val -> isOutputSlotLocked = val),
                        builder)
                    .setPos(100, 52)
                    .setSize(18, 18))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> itemCount, value -> clientItemCount = value));

    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add("Capacity: " + EnumChatFormatting.GOLD + getItemCapacity() + EnumChatFormatting.GRAY + " Items");
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("itemStack")) {
            final ItemStack tContents = ItemStack
                .loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack"));
            final int tSize = stack.stackTagCompound.getInteger("itemCount");
            if (tContents != null && tSize > 0) {
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_CHEST_INFO", "Contains Item: ")
                        + EnumChatFormatting.YELLOW
                        + tContents.getDisplayName()
                        + EnumChatFormatting.GRAY);
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_CHEST_AMOUNT", "Item Amount: ")
                        + EnumChatFormatting.GREEN
                        + formatNumber(tSize)
                        + EnumChatFormatting.GRAY);
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
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
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
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("itemCount")) setItemCount(aNBT.getInteger("itemCount"));
        if (aNBT.hasKey("itemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("itemStack")));
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
}
