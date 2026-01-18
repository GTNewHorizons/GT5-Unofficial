package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;

import java.util.List;

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

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.capability.item.ItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.AbstractInventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
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
import gregtech.api.implementation.items.SimpleItemIO;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEDigitalChestBase extends MTETieredMachineBlock
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, IAddUIWidgets {

    protected boolean mVoidOverflow = false;
    protected boolean mDisableFilter;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this);

    private int lastTrueCount;

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
    public void addListener(IMEMonitorHandlerReceiver imeMonitorHandlerReceiver, Object o) {
        meInventoryHandler.addListener(imeMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver imeMonitorHandlerReceiver) {
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        mVoidOverflow = !mVoidOverflow;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal(
                mVoidOverflow ? "GT5U.machines.digitalchest.voidoverflow.enabled"
                    : "GT5U.machines.digitalchest.voidoverflow.disabled"));
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
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
            final int availableSpace = mVoidOverflow ? getMaxItemCount() : getMaxItemCount() - savedCount;

            if ((mInventory[0] != null) && availableSpace > 0 && GTUtility.areStacksEqual(mInventory[0], stack)) {
                final int stackToMove = Math.min(mInventory[0].stackSize, availableSpace);
                count = (int) Math.min((long) count + stackToMove, getMaxItemCount());
                mInventory[0].stackSize -= stackToMove;
                if (mInventory[0].stackSize <= 0) {
                    mInventory[0] = null;
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

            int extraCount = 0;
            if (mInventory[1] != null) {
                if (GTUtility.areStacksEqual(mInventory[1], stack)) {
                    extraCount = mInventory[1].stackSize;
                } else if (stack == null || stack.stackSize <= 0) {
                    extraCount = mInventory[1].stackSize;
                    stack = mInventory[1];
                }
            }

            // notifyListeners has a null check on the stack arg
            meInventoryHandler.notifyListeners(count + extraCount - lastTrueCount, stack);
            lastTrueCount = count + extraCount;
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
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
        lastTrueCount = getItemCount();
        if (GTUtility.areStacksEqual(getItemStack(), mInventory[1])) {
            lastTrueCount += mInventory[1].stackSize;
        }
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
        if (mDisableFilter) return true;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    protected ItemSink getItemSink(ForgeDirection side) {
        return new ItemIOImpl();
    }

    @Override
    protected ItemSource getItemSource(ForgeDirection side) {
        return new ItemIOImpl();
    }

    @Override
    protected ItemIO getItemIO(ForgeDirection side) {
        return new ItemIOImpl();
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
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.digital_chest.count",
                    GTUtility.formatNumbers(tag.getLong("itemCount"))));
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.digital_chest.type",
                    ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itemType"))
                        .getDisplayName()));
        } else {
            currenttip.add(StatCollector.translateToLocal("GT5U.waila.digital_chest.empty"));
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
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.item_amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
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

    class ItemIOImpl extends SimpleItemIO {

        private static final int[] SLOTS = { 0, 1 };

        @Override
        protected @NotNull InventoryIterator iterator(int[] allowedSlots) {
            return new AbstractInventoryIterator(SLOTS, allowedSlots) {

                @Override
                protected ItemStack getStackInSlot(int slot) {
                    switch (slot) {
                        case 0 -> {
                            return GTUtility.copyAmountUnsafe(getItemCount(), getItemStack());
                        }
                        case 1 -> {
                            return GTUtility.copy(mInventory[1]);
                        }
                        default -> {
                            return null;
                        }
                    }
                }

                @Override
                public ItemStack extract(int amount, boolean forced) {
                    switch (getCurrentSlot()) {
                        case 0 -> {
                            int toExtract = Math.min(amount, getItemCount());

                            if (toExtract <= 0) return null;

                            ItemStack extracted = GTUtility.copyAmountUnsafe(toExtract, getItemStack());

                            setItemCount(getItemCount() - toExtract);

                            if (getItemCount() <= 0) {
                                setItemStack(null);
                            }

                            meInventoryHandler.notifyListeners(-toExtract, extracted);

                            MTEDigitalChestBase.this.markDirty();

                            return extracted;
                        }
                        case 1 -> {
                            ItemStack inSlot = mInventory[1];

                            if (inSlot == null) return null;

                            int toExtract = Math.min(amount, inSlot.stackSize);

                            ItemStack extracted = decrStackSize(1, toExtract);

                            MTEDigitalChestBase.this.markDirty();

                            return extracted;
                        }
                        default -> {
                            return null;
                        }
                    }
                }

                @Override
                public int insert(ImmutableItemStack stack, boolean forced) {
                    int remaining = stack.getStackSize();

                    if (getCurrentSlot() == 1) {
                        int max = getStackSizeLimit(1, stack.toStackFast());

                        ItemStack stored = mInventory[1];

                        int storedAmount = stored == null ? 0 : stored.stackSize;

                        int toInsert = Math.min(stack.getStackSize(), max - storedAmount);

                        if (stored == null) mInventory[1] = stack.toStackFast(0);

                        mInventory[1].stackSize += toInsert;
                        remaining -= toInsert;
                    }

                    if (!ItemUtil.isStackEmpty(getItemStack()) && !stack.matches(getItemStack())) {
                        return remaining;
                    }

                    int insertable = Math
                        .min((forced ? Integer.MAX_VALUE : getItemCapacity()) - getItemCount(), remaining);

                    if (ItemUtil.isStackEmpty(getItemStack())) {
                        setItemStack(stack.toStack(0));
                    }

                    setItemCount(getItemCount() + insertable);
                    remaining -= insertable;

                    meInventoryHandler.notifyListeners(insertable, getItemStack());

                    MTEDigitalChestBase.this.markDirty();

                    return remaining;
                }
            };
        }
    }
}
