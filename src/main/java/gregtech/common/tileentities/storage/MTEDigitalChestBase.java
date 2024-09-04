package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEDigitalChestBase extends MTETieredMachineBlock
    implements IMEMonitor<IAEItemStack>, IAddUIWidgets {

    protected boolean mVoidOverflow = false;
    protected boolean mDisableFilter;
    private Map<IMEMonitorHandlerReceiver<IAEItemStack>, Object> listeners = null;

    public MTEDigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "This Chest stores " + GTUtility.formatNumbers(commonSizeCompute(aTier)) + " Blocks",
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

    public MTEDigitalChestBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public MTEDigitalChestBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
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

    public static void registerAEIntegration() {
        appeng.api.AEApi.instance()
            .registries()
            .externalStorage()
            .addExternalStorageInterface(new AE2DigitalChestHandler());
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> imeMonitorHandlerReceiver, Object o) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.put(imeMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> imeMonitorHandlerReceiver) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.remove(imeMonitorHandlerReceiver);
    }

    @Override
    public appeng.api.config.AccessRestriction getAccess() {
        return appeng.api.config.AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEItemStack iaeItemStack) {
        ItemStack s = getItemStack();
        if (s == null || iaeItemStack == null) return false;
        return iaeItemStack.isSameType(s);
    }

    @Override
    public boolean canAccept(IAEItemStack iaeItemStack) {
        ItemStack s = getItemStack();
        if (s == null || iaeItemStack == null) return true;
        return iaeItemStack.isSameType(s);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true;
    }

    protected abstract ItemStack getItemStack();

    protected abstract void setItemStack(ItemStack s);

    @SuppressWarnings("unchecked")
    @Override
    public IItemList<IAEItemStack> getAvailableItems(final IItemList out, int iteration) {
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            AEItemStack s = AEItemStack.create(storedStack);
            s.setStackSize(getItemCount());
            out.add(s);
        }
        return out;
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        IItemList<IAEItemStack> res = new ItemList();
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            AEItemStack s = AEItemStack.create(storedStack);
            s.setStackSize(getItemCount());
            res.add(s);
        }
        return res;
    }

    protected abstract int getItemCount();

    @Override
    public abstract void setItemCount(int aCount);

    @Override
    public int getMaxItemCount() {
        return commonSizeCompute(mTier);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public IAEItemStack injectItems(final IAEItemStack input, final appeng.api.config.Actionable mode,
        final appeng.api.networking.security.BaseActionSource src) {
        if (getBaseMetaTileEntity() == null) return input;

        final ItemStack inputStack = input.getItemStack();
        final int maxCapacity = getMaxItemCount();
        final int itemCount = getItemCount();
        final long toAdd = input.getStackSize();
        final ItemStack storedStack = getItemStack();

        if (storedStack != null && !GTUtility.areStacksEqual(storedStack, inputStack)) {
            // Can't stack with existing item, just return the input.
            return input;
        }

        // Number of items not added because there's too much to add.
        final long notAdded = itemCount + toAdd - maxCapacity;

        if (mode == appeng.api.config.Actionable.MODULATE) {
            final int newCount = (int) Math.min((long) maxCapacity, itemCount + toAdd);

            if (storedStack == null) {
                setItemStack(inputStack.copy());
            }
            setItemCount(newCount);
            getBaseMetaTileEntity().markDirty();
        }
        if (mVoidOverflow || notAdded <= 0) {
            return null;
        } else {
            return input.copy()
                .setStackSize(notAdded);
        }
    }

    @Override
    public IAEItemStack extractItems(final IAEItemStack request, final appeng.api.config.Actionable mode,
        final appeng.api.networking.security.BaseActionSource src) {
        if (request.isSameType(getItemStack())) {
            if (getBaseMetaTileEntity() == null) return null;
            if (mode != appeng.api.config.Actionable.SIMULATE) getBaseMetaTileEntity().markDirty();
            if (request.getStackSize() >= getItemCount()) {
                AEItemStack result = AEItemStack.create(getItemStack());
                result.setStackSize(getItemCount());
                if (mode != appeng.api.config.Actionable.SIMULATE) setItemCount(0);
                return result;
            } else {
                if (mode != appeng.api.config.Actionable.SIMULATE)
                    setItemCount(getItemCount() - (int) request.getStackSize());
                return request.copy();
            }
        }
        return null;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
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

        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            if ((getItemCount() <= 0)) {
                setItemStack(null);
                setItemCount(0);
            }
            if (getItemStack() == null && mInventory[0] != null) {
                setItemStack(mInventory[0].copy());
            }
            int count = getItemCount();
            ItemStack stack = getItemStack();
            int savedCount = count;

            if ((mInventory[0] != null) && ((count < getMaxItemCount()) || mVoidOverflow)
                && GTUtility.areStacksEqual(mInventory[0], stack)) {
                count += mInventory[0].stackSize;
                if (count <= getMaxItemCount()) {
                    mInventory[0] = null;
                } else {
                    if (mVoidOverflow) {
                        mInventory[0] = null;
                    } else {
                        mInventory[0].stackSize = (count - getMaxItemCount());
                    }
                    count = getMaxItemCount();
                }
            }
            if (mInventory[1] == null && stack != null) {
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
                mInventory[2] = stack.copy();
                mInventory[2].stackSize = Math.min(stack.getMaxStackSize(), count);
            } else {
                mInventory[2] = null;
            }

            notifyListeners(count - savedCount, stack);
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
    public boolean isSimpleMachine() {
        return true;
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
            return new String[] { EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET, "Stored Items:",
                EnumChatFormatting.GOLD + "No Items" + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + "0"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + GTUtility.formatNumbers(getMaxItemCount())
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET, "Stored Items:",
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

    protected abstract String chestName();

    private void notifyListeners(int count, ItemStack stack) {
        if (listeners == null) {
            listeners = new HashMap<>();
            return;
        }
        if (count == 0 || stack == null) return;
        ItemList change = new ItemList();
        AEItemStack s = AEItemStack.create(stack);
        s.setStackSize(count);
        change.add(s);
        listeners.forEach((l, o) -> {
            if (l.isValid(o)) l.postChange(this, change, null);
            else removeListener(l);
        });
    }

    private boolean hasActiveMEConnection() {
        if (listeners == null || listeners.isEmpty()) return false;
        for (Map.Entry<IMEMonitorHandlerReceiver<IAEItemStack>, Object> e : listeners.entrySet()) {
            if ((e.getKey() instanceof appeng.api.parts.IPart)) {
                appeng.api.networking.IGridNode n = ((appeng.api.parts.IPart) e.getKey()).getGridNode();
                if (n != null && n.isActive()) return true;
            }
        }
        // if there are no active storage buses - clear the listeners
        listeners.clear();
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
        aNBT.setBoolean("mDisableFilter", mDisableFilter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount")) setItemCount(aNBT.getInteger("mItemCount"));
        if (aNBT.hasKey("mItemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack")));
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");
        mDisableFilter = aNBT.getBoolean("mDisableFilter");
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        if (aIndex != 0) return false;
        if ((mInventory[0] != null && !GTUtility.areStacksEqual(mInventory[0], aStack))) return false;
        if (mDisableFilter) return true;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side != aFacing) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_SCHEST),
            TextureFactory.builder()
                .addIcon(OVERLAY_SCHEST_GLOW)
                .glow()
                .build() };
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("itemType", Constants.NBT.TAG_COMPOUND)) {
            currenttip.add("Item Count: " + GTUtility.parseNumberToString(tag.getInteger("itemCount")));
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
        int realItemCount = getItemCount();
        if (GTUtility.isStackValid(mInventory[1]) && GTUtility.areStacksEqual(mInventory[1], is))
            realItemCount += mInventory[1].stackSize;
        tag.setInteger("itemCount", realItemCount);
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
}
