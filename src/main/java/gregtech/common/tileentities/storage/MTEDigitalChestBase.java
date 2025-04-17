package gregtech.common.tileentities.storage;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTUtility.moveMultipleItemStacks;
import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
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
import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITESRProvider;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.render.DigitalStorageRenderer;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEDigitalChestBase extends MTETieredMachineBlock
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, IAddUIWidgets, IItemLockable, ITESRProvider {

    public boolean mOutputItem = false, mLockItem = false, mAllowInputFromOutputSide = true;
    protected boolean mVoidOverflow = false;
    protected boolean mVoidFull = false;
    protected boolean mDisableFilter;
    public boolean mFacingHasBeenUpdated = false;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this);
    public ForgeDirection mMainFacing = UNKNOWN;
    protected ItemStack lockedItem = null;
    private ItemStack displayItemCache = null;
    private int displayCountCache = 0;
    /**
     * Note this value may have incorrect item count.
     */
    @SideOnly(Side.CLIENT)
    public ItemStack displayItem = null;
    @SideOnly(Side.CLIENT)
    public int displayItemCount = 0;

    private final IntSyncValue itemCountSyncHandler = new IntSyncValue(
        this::getItemCount,
        value -> clientItemCount = value);
    private final ModularSlot ghost = new ModularSlot(inventoryHandler, 2);
    private final BooleanSyncValue autoOutputHandler = new BooleanSyncValue(() -> mOutputItem, value -> mOutputItem = value);

    private final BooleanSyncValue lockItemHandler = new BooleanSyncValue(() -> mLockItem, value -> {
        mLockItem = value;
        if (getBaseMetaTileEntity().isServerSide()) {
            if (mLockItem) {
                setLockedItem(getItemStack());
            } else {
                clearLock();
            }
        }
    });

    private final BooleanSyncValue allowInputFromOutputSideHandler = new BooleanSyncValue(
        () -> mAllowInputFromOutputSide,
        value -> mAllowInputFromOutputSide = value);

    private final BooleanSyncValue voidOverflowHandler = new BooleanSyncValue(() -> mVoidOverflow, value -> mVoidOverflow = value);

    private final BooleanSyncValue voidFullHandler = new BooleanSyncValue(() -> mVoidFull, value -> mVoidFull = value);

    public MTEDigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "Stores " + GTUtility.formatNumbers(commonSizeCompute(aTier)) + " items",
                "Use a screwdriver to enable", "voiding items on overflow", "Will keep its contents when harvested", });
    }

    protected static int commonSizeCompute(int tier) {
        return switch (tier) {
            case 1 -> 4000000;
            case 2 -> 8000000;
            case 3 -> 16000000;
            case 4 -> 32000000;
            case 5 -> 64000000;
            case 6 -> 128000000;
            case 7 -> 256000000;
            case 8 -> 512000000;
            case 9 -> 1024000000;
            case 10 -> 2147483640;
            default -> 0;
        };
    }

    public MTEDigitalChestBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public boolean renderInWorld(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {
        DigitalStorageRenderer.renderMachine(this, world, x, y, z, block, renderer);
        return true;
    }

    @Override
    public boolean renderInInventory(Block block, int meta, RenderBlocks renderer) {
        DigitalStorageRenderer.renderMachineInventory(this, null, 0, 0, 0, block, renderer);
        return true;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        DigitalStorageRenderer.renderChestStack(this, x, y, z, timeSinceLastTick);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = new NBTTagCompound();
        if(getItemStack() != null) {
            data.setInteger("count", getItemCount());
            getItemStack().writeToNBT(data);
        }
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        if(data.hasKey("count")) {
            displayItemCount = data.getInteger("count");
            displayItem =  ItemStack.loadItemStackFromNBT(data);
        }
        else {
            displayItemCount = 0;
            displayItem = null;
        }
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mMainFacing = ForgeDirection.getOrientation(aValue);
    }

    @Override
    public byte getUpdateData() {
        return (byte) mMainFacing.ordinal();
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();

        // Set up the correct facing (front towards player, output opposite) client-side before the server packet
        // arrives
        if (mMainFacing == UNKNOWN) {
            IGregTechTileEntity te = getBaseMetaTileEntity();
            if (te != null && te.getWorld().isRemote) {
                mMainFacing = te.getFrontFacing();
                te.setFrontFacing(te.getBackFacing());
            }
        }
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        mMainFacing = ForgeDirection.UNKNOWN;
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("mItemStack")) {
            final ItemStack tContents = ItemStack
                .loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("mItemStack"));
            final int tSize = stack.stackTagCompound.getInteger("mItemCount");
            if (tContents != null && tSize > 0) {
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_CHEST_INFO", "Contains Item: ")
                        + EnumChatFormatting.YELLOW
                        + tContents.getDisplayName()
                        + EnumChatFormatting.GRAY);
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_CHEST_AMOUNT", "Item Amount: ")
                        + EnumChatFormatting.GREEN
                        + GTUtility.formatNumbers(tSize)
                        + EnumChatFormatting.GRAY);
            }
        }
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> imeMonitorHandlerReceiver, Object o) {
        meInventoryHandler.addListener(imeMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> imeMonitorHandlerReceiver) {
        meInventoryHandler.removeListener(imeMonitorHandlerReceiver);
    }

    @Override
    public AccessRestriction getAccess() {
        return meInventoryHandler.getAccess();
    }

    @Override
    public boolean isPrioritized(IAEItemStack iaeItemStack) {
        return meInventoryHandler.isPrioritized(iaeItemStack);
    }

    @Override
    public boolean canAccept(IAEItemStack iaeItemStack) {
        return meInventoryHandler.canAccept(iaeItemStack);
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
    public abstract ItemStack getItemStack();

    @Override
    public abstract void setItemStack(ItemStack s);

    @Override
    public IItemList<IAEItemStack> getAvailableItems(final IItemList<IAEItemStack> out, int iteration) {
        return meInventoryHandler.getAvailableItems(out, iteration);
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        return meInventoryHandler.getStorageList();
    }

    @Override
    public abstract int getItemCount();

    @Override
    public abstract void setItemCount(int aCount);

    @Override
    public int getMaxItemCount() {
        return commonSizeCompute(mTier);
    }

    @Override
    public int getItemCapacity() {
        return getMaxItemCount();
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
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public IAEItemStack injectItems(final IAEItemStack input, final Actionable mode, final BaseActionSource src) {
        IAEItemStack returnStack = meInventoryHandler.injectItems(input, mode, src);
        if (mVoidOverflow) {
            return null;
        }
        return returnStack;
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mVoidOverflow = !mVoidOverflow;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal(
                mVoidOverflow ? "GT5U.machines.digitalchest.voidoverflow.enabled"
                    : "GT5U.machines.digitalchest.voidoverflow.disabled"));
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (super.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ)) return true;
        mDisableFilter = !mDisableFilter;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal(
                mDisableFilter ? "GT5U.machines.digitalchest.inputfilter.disabled"
                    : "GT5U.machines.digitalchest.inputfilter.enabled"));
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {

        if (getBaseMetaTileEntity().isServerSide()) {
            doDisplayThings();

            if ((getItemCount() <= 0) || mVoidFull) {
                if (getLockedItem() == null || (getLockedItem() != null && getItemStack() != null
                    && !getItemStack().isItemEqual(getLockedItem()))) {
                    setItemStack(null);
                }
                setItemCount(0);
            }
            if (getItemStack() == null && mInventory[0] != null) {
                setItemStack(mInventory[0].copy());
            }
            int count = getItemCount();
            ItemStack stack = getItemStack();
            int savedCount = count;
            final int availableSpace = mVoidOverflow ? getMaxItemCount() : getMaxItemCount() - savedCount;

            if ((mInventory[0] != null) && availableSpace > 0 && GTUtility.areStacksEqual(mInventory[0], stack)) {
                final int stackToMove = Math.min(mInventory[0].stackSize, availableSpace);
                count = (int) Math.min((long) count + stackToMove, getMaxItemCount());
                mInventory[0].stackSize -= stackToMove;
                if (mInventory[0].stackSize <= 0) {
                    mInventory[0] = null;
                }
            }
            if (mInventory[1] == null && stack != null && (count > 0)) {
                mInventory[1] = stack.copy();
                mInventory[1].stackSize = Math.min(stack.getMaxStackSize(), count);
                count -= mInventory[1].stackSize;
            } else if ((count > 0) && GTUtility.areStacksEqual(mInventory[1], stack)
                && mInventory[1].getMaxStackSize() > mInventory[1].stackSize) {
                    int tmp = Math.min(count, mInventory[1].getMaxStackSize() - mInventory[1].stackSize);
                    mInventory[1].stackSize += tmp;
                    count -= tmp;
                }
            setItemCount(count);
            if (stack != null) {
                if (mLockItem && getLockedItem() == null) {
                    setLockedItem(stack);
                }
                mInventory[2] = stack.copy();
                mInventory[2].stackSize = Math.min(stack.getMaxStackSize(), count);
            } else {
                mInventory[2] = null;
            }

            meInventoryHandler.notifyListeners(count - savedCount, stack);
            if (count != savedCount) getBaseMetaTileEntity().markDirty();

            if (mOutputItem && mInventory[1] != null && (aTimer % 20 == 0)) {
                final IInventory tTileEntity = aBaseMetaTileEntity
                    .getIInventoryAtSide(getBaseMetaTileEntity().getFrontFacing());
                if (tTileEntity != null) {
                    moveMultipleItemStacks(
                        aBaseMetaTileEntity,
                        tTileEntity,
                        getBaseMetaTileEntity().getFrontFacing(),
                        getBaseMetaTileEntity().getBackFacing(),
                        null,
                        false,
                        (byte) 64,
                        (byte) 1,
                        (byte) 64,
                        (byte) 1,
                        mInventory.length);
                    for (int i = 0; i < mInventory.length; i++)
                        if (mInventory[i] != null && mInventory[i].stackSize <= 0 && (i != 2 || !isLocked()))
                            mInventory[i] = null;
                }
            }

            boolean sameStack = displayItemCache == null ? stack == null : GTUtility.areStacksEqual(stack, displayItemCache);
            boolean sameStackSize = displayItemCache == null ? stack == null : displayCountCache == count;
            if(!sameStack || !sameStackSize) {
                if(stack != null) {
                    displayItemCache = stack;
                    displayCountCache = count;
                }
                else {
                    displayItemCache = null;
                    displayCountCache = 0;
                }
                getBaseMetaTileEntity().issueTileUpdate();
            }
        }
    }

    protected boolean isValidMainFacing(ForgeDirection side) {
        return (side.flag & UNKNOWN.flag) == 0;
    }

    protected void doDisplayThings() {
        if (!isValidMainFacing(mMainFacing) && isValidMainFacing(getBaseMetaTileEntity().getFrontFacing())) {
            mMainFacing = getBaseMetaTileEntity().getFrontFacing();
        }
        if (isValidMainFacing(mMainFacing) && !mFacingHasBeenUpdated) {
            mFacingHasBeenUpdated = true;
            getBaseMetaTileEntity().setFrontFacing(getBaseMetaTileEntity().getBackFacing());
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex != 2;
    }

    @Override
    public int getProgresstime() {
        return getItemCount() + (mInventory[0] == null ? 0 : mInventory[0].stackSize)
            + (mInventory[1] == null ? 0 : mInventory[1].stackSize);
    }

    @Override
    public int maxProgresstime() {
        return getMaxItemCount();
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {

        if (getItemStack() == null) {
            return new String[] { EnumChatFormatting.BLUE + localizedChestName() + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items"),
                EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items.empty")
                    + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + "0"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + GTUtility.formatNumbers(getMaxItemCount())
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + localizedChestName() + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items"),
            EnumChatFormatting.GOLD + getItemStack().getDisplayName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(getItemCount())
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getMaxItemCount())
                + EnumChatFormatting.RESET };
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }

    protected abstract String localizedChestName();

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        savePersistentNBTData(aNBT);
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        savePersistentNBTData(aNBT);

        aNBT.setBoolean("mFacingHasBeenUpdated", mFacingHasBeenUpdated);
        aNBT.setInteger("mMainFacing", mMainFacing.ordinal());
    }

    private void savePersistentNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
        aNBT.setBoolean("mDisableFilter", mDisableFilter);
        aNBT.setBoolean("mOutputItem", mOutputItem);
        aNBT.setBoolean("mLockItem", mLockItem);
        if (lockedItem != null) {
            aNBT.setTag("lockedItem", lockedItem.writeToNBT(new NBTTagCompound()));
        }
        aNBT.setBoolean("mAllowInputFromOutputSide", mAllowInputFromOutputSide);
        aNBT.setBoolean("mVoidFull", mVoidFull);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount")) setItemCount(aNBT.getInteger("mItemCount"));
        if (aNBT.hasKey("mItemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack")));
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");
        mDisableFilter = aNBT.getBoolean("mDisableFilter");
        mOutputItem = aNBT.getBoolean("mOutputItem");
        mLockItem = aNBT.getBoolean("mLockItem");
        if (aNBT.hasKey("lockedItem")) {
            lockedItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("lockedKey"));
        }
        if(aNBT.hasKey("mAllowInputFromOutputSide")) {
            mAllowInputFromOutputSide = aNBT.getBoolean("mAllowInputFromOutputSide");
        }
        else {
            mAllowInputFromOutputSide = true; //previous version has no output side
        }
        mVoidFull = aNBT.getBoolean("mVoidFull");
        if(aNBT.hasKey("mMainFacing")) {
            mMainFacing = ForgeDirection.getOrientation(aNBT.getInteger("mMainFacing"));
        }
        else {
            mMainFacing = UNKNOWN;
        }
        mFacingHasBeenUpdated = aNBT.getBoolean("mFacingHasBeenUpdated");
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
        if (lockedItem != null && !lockedItem.isItemEqual(aStack)) return false;
        if (!mAllowInputFromOutputSide && side == getBaseMetaTileEntity().getFrontFacing()) return false;
        if (aIndex != 0) return false;
        if ((mInventory[0] != null && !GTUtility.areStacksEqual(mInventory[0], aStack))) return false;
        if (mDisableFilter) return true;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return null;
    }

    @Override
    public void setLockedItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            clearLock();
        } else {
            lockedItem = ItemHandlerHelper.copyStackWithSize(itemStack, 1);
        }
    }

    @Nullable
    @Override
    public ItemStack getLockedItem() {
        return lockedItem;
    }

    @Override
    public void clearLock() {
        lockedItem = null;
    }

    @Override
    public boolean isLocked() {
        return lockedItem != null;
    }

    @Override
    public boolean acceptsItemLock() {
        return true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("itemType", Constants.NBT.TAG_COMPOUND)) {
            currenttip.add("Item Count: " + GTUtility.formatNumbers(tag.getLong("itemCount")));
            currenttip.add(
                "Item Type: " + ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itemType"))
                    .getDisplayName());
        } else {
            currenttip.add("Chest Empty");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        ItemStack is = getItemStack();
        if (GTUtility.isStackInvalid(is)) return;
        long realItemCount = getItemCount();
        if (GTUtility.isStackValid(mInventory[1]) && GTUtility.areStacksEqual(mInventory[1], is))
            realItemCount += mInventory[1].stackSize;
        tag.setLong("itemCount", realItemCount);
        tag.setTag("itemType", is.writeToNBT(new NBTTagCompound()));
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
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
                new TextWidget("Item Amount").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(clientItemCount))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> this instanceof MTEQuantumChest ? ((MTEQuantumChest) this).mItemCount : 0,
                    value -> clientItemCount = value));

    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        syncUIValues(syncManager);

        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
            .build();
        buildChestIO(panel);
        buildDigitalInterface(panel);
        buildToggleButtons(panel, syncManager);
        return panel;
    }

    private void syncUIValues(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup("item_inv", 0);
        syncManager.syncValue("digital_chest_amount", itemCountSyncHandler);
        syncManager.syncValue("digital_chest_ghost", new ItemSlotSH(ghost));
        syncManager.syncValue("auto_output", autoOutputHandler);
        syncManager.syncValue("lock_item", lockItemHandler);
        syncManager.syncValue("allow_input_from_output_side", allowInputFromOutputSideHandler);
        syncManager.syncValue("void_overflow", voidOverflowHandler);
        syncManager.syncValue("void_full", voidFullHandler);
    }

    private void buildChestIO(ModularPanel panel) {
        panel.child(
            new ItemSlot().slot(
                new ModularSlot(inventoryHandler, 0).slotGroup("item_inv")
                    .filter(
                        itemStack -> !lockItemHandler.getBoolValue() || ghost.getStack() == null
                            || ghost.getStack()
                                .isItemEqual(itemStack)))
                .pos(79, 16)
                .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN))
            .child(
                new ItemSlot().slot(
                    new ModularSlot(inventoryHandler, 1).slotGroup("item_inv")
                        .accessibility(false, true))
                    .pos(79, 52)
                    .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT));
    }

    private void buildDigitalInterface(ModularPanel panel) {
        panel.child(
            new IDrawable.DrawableWidget(GTGuiTextures.PICTURE_SCREEN_BLACK).pos(7, 16)
                .size(71, 45))
            .child(
                IKey.lang("GT5U.gui.text.item.amount")
                    .color(COLOR_TEXT_WHITE.get())
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .pos(10, 20))
            .child(
                IKey.dynamic(() -> numberFormat.format(clientItemCount))
                    .color(COLOR_TEXT_WHITE.get())
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .pos(10, 30)
                    .size(60, 10))
            .child(
                IKey.lang(
                    () -> (lockItemHandler.getBoolValue() && ghost.getStack() != null) ? "GT5U.gui.text.locked" : "")
                    .color(COLOR_TEXT_WHITE.get())
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .pos(10, 40)
                    .size(60, 10))
            .child(
                new DynamicDrawable(() -> new ItemDrawable().setItem(ghost.getStack())).asIcon()
                    .asWidget()
                    .tooltipBuilder(richTooltip -> {
                        if (ghost.getStack() != null) {
                            richTooltip.addFromItem(ghost.getStack());
                        }
                        richTooltip.markDirty();
                    })
                    .pos(59, 42));
    }

    private void buildToggleButtons(ModularPanel panel, PanelSyncManager syncManager) {
        panel
            .child(
                new ToggleButton()
                    .value(
                        new BoolValue.Dynamic(
                            autoOutputHandler::getBoolValue,
                            val -> {
                                autoOutputHandler.setBoolValue(!autoOutputHandler.getBoolValue());
                                syncManager.getPlayer()
                                    .addChatMessage(
                                        new ChatComponentText(
                                            StatCollector.translateToLocal(
                                                mOutputItem ? "GT5U.machines.digitalchest.autooutput.enabled"
                                                    : "GT5U.machines.digitalchest.autooutput.disabled")));
                            }))
                    .tooltip(
                        false,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.item_transfer.tooltip").text))
                    .tooltip(
                        true,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.item_transfer.tooltip").text))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .stateBackground(GTGuiTextures.BUTTON_STANDARD_TOGGLE)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM)
                    .pos(7, 63)
                    .size(18, 18))
            .child(
                new ToggleButton()
                    .value(
                        new BoolValue.Dynamic(
                            lockItemHandler::getBoolValue,
                            val -> {
                                lockItemHandler.setBoolValue(!lockItemHandler.getBoolValue());
                                syncManager.getPlayer()
                                    .addChatMessage(
                                        new ChatComponentText(
                                            !mLockItem ? StatCollector.translateToLocal("GT5U.machines.digitalchest.lockItem.disabled")
                                                : displayItem != null
                                                ? StatCollector.translateToLocal("GT5U.machines.digitalchest.lockItem.enabled")
                                                : StatCollector.translateToLocal("GT5U.machines.digitalchest.lockItem.none")));
                            }))
                    .tooltip(
                        false,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.digitalchest.lockItem.tooltip").text))
                    .tooltip(
                        true,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.digitalchest.lockItem.tooltip").text))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .stateBackground(GTGuiTextures.BUTTON_STANDARD_TOGGLE)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .pos(25, 63)
                    .size(18, 18))
            .child(
                new ToggleButton()
                    .value(
                        new BoolValue.Dynamic(
                            allowInputFromOutputSideHandler::getBoolValue,
                            val -> {
                                allowInputFromOutputSideHandler
                                    .setBoolValue(!allowInputFromOutputSideHandler.getBoolValue());
                                syncManager.getPlayer()
                                    .addChatMessage(
                                        new ChatComponentText(
                                            mAllowInputFromOutputSide ? GTUtility.trans("095", "Input from Output Side allowed")
                                                : GTUtility.trans("096", "Input from Output Side forbidden")));
                            }))
                    .tooltip(
                        false,
                        richTooltip -> richTooltip.addStringLines(
                            mTooltipCache.getData("GT5U.machines.digitalchest.inputfromoutput.tooltip").text))
                    .tooltip(
                        true,
                        richTooltip -> richTooltip.addStringLines(
                            mTooltipCache.getData("GT5U.machines.digitalchest.inputfromoutput.tooltip").text))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .stateBackground(GTGuiTextures.BUTTON_STANDARD_TOGGLE)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                    .pos(43, 63)
                    .size(18, 18))
            .child(
                new ToggleButton()
                    .value(
                        new BoolValue.Dynamic(
                            voidOverflowHandler::getBoolValue,
                            val -> {
                                voidOverflowHandler.setBoolValue(!voidOverflowHandler.getBoolValue());
                                syncManager.getPlayer()
                                    .addChatMessage(
                                        new ChatComponentText(
                                            mVoidOverflow ? GTUtility.trans("268", "Overflow Voiding Mode Enabled")
                                                : GTUtility.trans("267", "Overflow Voiding Mode Disabled")));
                            }))
                    .tooltip(
                        false,
                        richTooltip -> richTooltip.addStringLines(
                            mTooltipCache.getData("GT5U.machines.digitalchest.voidoverflow.tooltip").text))
                    .tooltip(
                        true,
                        richTooltip -> richTooltip.addStringLines(
                            mTooltipCache.getData("GT5U.machines.digitalchest.voidoverflow.tooltip").text))
                    .stateBackground(GTGuiTextures.BUTTON_STANDARD_TOGGLE)
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                    .pos(98, 63)
                    .size(18, 18))
            .child(
                new ToggleButton()
                    .value(
                        new BoolValue.Dynamic(
                            voidFullHandler::getBoolValue,
                            val -> {
                                voidFullHandler.setBoolValue(!voidFullHandler.getBoolValue());
                                syncManager.getPlayer()
                                    .addChatMessage(
                                        new ChatComponentText(
                                            mVoidFull ? GTUtility.trans("270", "Void Full Mode Enabled")
                                                : GTUtility.trans("269", "Void Full Mode Disabled")));
                            }))
                    .tooltip(
                        false,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.digitalchest.voidfull.tooltip").text))
                    .tooltip(
                        true,
                        richTooltip -> richTooltip
                            .addStringLines(mTooltipCache.getData("GT5U.machines.digitalchest.voidfull.tooltip").text))
                    .stateBackground(GTGuiTextures.BUTTON_STANDARD_TOGGLE)
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .pos(116, 63)
                    .size(18, 18));
    }
}
