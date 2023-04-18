package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

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

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public abstract class GT_MetaTileEntity_DigitalChestBase extends GT_MetaTileEntity_TieredMachineBlock
    implements appeng.api.storage.IMEMonitor<appeng.api.storage.data.IAEItemStack>, IAddUIWidgets {

    protected boolean mVoidOverflow = false;
    protected boolean mDisableFilter;
    private Map<appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack>, Object> listeners = null;

    public GT_MetaTileEntity_DigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "This Chest stores " + GT_Utility.formatNumbers(commonSizeCompute(aTier)) + " Blocks",
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

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
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
                    GT_LanguageManager.addStringLocalization(
                        "TileEntity_CHEST_INFO",
                        "Contains Item: ",
                        !GregTech_API.sPostloadFinished) + EnumChatFormatting.YELLOW
                        + tContents.getDisplayName()
                        + EnumChatFormatting.GRAY);
                tooltip.add(
                    GT_LanguageManager.addStringLocalization(
                        "TileEntity_CHEST_AMOUNT",
                        "Item Amount: ",
                        !GregTech_API.sPostloadFinished) + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(tSize)
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
    public void addListener(
        appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack> imeMonitorHandlerReceiver,
        Object o) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.put(imeMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(
        appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack> imeMonitorHandlerReceiver) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.remove(imeMonitorHandlerReceiver);
    }

    @Override
    public appeng.api.config.AccessRestriction getAccess() {
        return appeng.api.config.AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(appeng.api.storage.data.IAEItemStack iaeItemStack) {
        ItemStack s = getItemStack();
        if (s == null || iaeItemStack == null) return false;
        return iaeItemStack.isSameType(s);
    }

    @Override
    public boolean canAccept(appeng.api.storage.data.IAEItemStack iaeItemStack) {
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

    @Override
    public appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> getAvailableItems(
        final appeng.api.storage.data.IItemList out) {
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(storedStack);
            s.setStackSize(getItemCount());
            out.add(s);
        }
        return out;
    }

    @Override
    public appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> getStorageList() {
        appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> res = new appeng.util.item.ItemList();
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(storedStack);
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
    public appeng.api.storage.data.IAEItemStack injectItems(final appeng.api.storage.data.IAEItemStack input,
        final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        final ItemStack inputStack = input.getItemStack();
        if (inputStack == null) return null;
        if (getBaseMetaTileEntity() == null) return input;
        if (mode != appeng.api.config.Actionable.SIMULATE) getBaseMetaTileEntity().markDirty();
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            if (GT_Utility.areStacksEqual(storedStack, inputStack)) {
                if (input.getStackSize() + getItemCount() > getMaxItemCount()) {
                    if (mVoidOverflow) {
                        if (mode != appeng.api.config.Actionable.SIMULATE) setItemCount(getMaxItemCount());
                        return null;
                    }
                    return createOverflowStack(input.getStackSize() + getItemCount(), mode);
                }
                if (mode != appeng.api.config.Actionable.SIMULATE)
                    setItemCount(getItemCount() + (int) input.getStackSize());
                return null;
            }
            return input;
        }
        if (mode != appeng.api.config.Actionable.SIMULATE) setItemStack(inputStack.copy());
        if (input.getStackSize() > getMaxItemCount()) return createOverflowStack(input.getStackSize(), mode);
        if (mode != appeng.api.config.Actionable.SIMULATE) setItemCount((int) input.getStackSize());
        return null;
    }

    private appeng.api.storage.data.IAEItemStack createOverflowStack(long size, appeng.api.config.Actionable mode) {
        final appeng.api.storage.data.IAEItemStack overflow = appeng.util.item.AEItemStack.create(getItemStack());
        overflow.setStackSize(size - getMaxItemCount());
        if (mode != appeng.api.config.Actionable.SIMULATE) setItemCount(getMaxItemCount());
        return overflow;
    }

    @Override
    public appeng.api.storage.data.IAEItemStack extractItems(final appeng.api.storage.data.IAEItemStack request,
        final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        if (request.isSameType(getItemStack())) {
            if (getBaseMetaTileEntity() == null) return null;
            if (mode != appeng.api.config.Actionable.SIMULATE) getBaseMetaTileEntity().markDirty();
            if (request.getStackSize() >= getItemCount()) {
                appeng.util.item.AEItemStack result = appeng.util.item.AEItemStack.create(getItemStack());
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
    public appeng.api.storage.StorageChannel getChannel() {
        return appeng.api.storage.StorageChannel.ITEMS;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mVoidOverflow = !mVoidOverflow;
        GT_Utility.sendChatToPlayer(
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
        GT_Utility.sendChatToPlayer(
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
                && GT_Utility.areStacksEqual(mInventory[0], stack)) {
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
            } else if ((count > 0) && GT_Utility.areStacksEqual(mInventory[1], stack)
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

            if (GregTech_API.mAE2) notifyListeners(count - savedCount, stack);
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
                    + GT_Utility.formatNumbers(getMaxItemCount())
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET, "Stored Items:",
            EnumChatFormatting.GOLD + getItemStack().getDisplayName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(getItemCount())
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxItemCount())
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
        appeng.util.item.ItemList change = new appeng.util.item.ItemList();
        appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(stack);
        s.setStackSize(count);
        change.add(s);
        listeners.forEach((l, o) -> {
            if (l.isValid(o)) l.postChange(this, change, null);
            else removeListener(l);
        });
    }

    private boolean hasActiveMEConnection() {
        if (listeners == null || listeners.isEmpty()) return false;
        for (Map.Entry<appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack>, Object> e : listeners
            .entrySet()) {
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
        if (GregTech_API.mAE2 && GT_Values.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GregTech_API.mAE2 && GT_Values.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        if (aIndex != 0) return false;
        if ((mInventory[0] != null && !GT_Utility.areStacksEqual(mInventory[0], aStack))) return false;
        if (mDisableFilter) return true;
        if (getItemStack() == null) return mInventory[1] == null || GT_Utility.areStacksEqual(mInventory[1], aStack);
        return GT_Utility.areStacksEqual(getItemStack(), aStack);
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
            currenttip.add("Item Count: " + GT_Utility.parseNumberToString(tag.getInteger("itemCount")));
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
        if (GT_Utility.isStackInvalid(is)) return;
        int realItemCount = getItemCount();
        if (GT_Utility.isStackValid(mInventory[1]) && GT_Utility.areStacksEqual(mInventory[1], is))
            realItemCount += mInventory[1].stackSize;
        tag.setInteger("itemCount", realItemCount);
        tag.setTag("itemType", is.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, 0)
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                SlotWidget.phantom(inventoryHandler, 2)
                    .disableInteraction()
                    .setBackground(GT_UITextures.TRANSPARENT)
                    .setPos(59, 42))
            .widget(
                new TextWidget("Item Amount").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> GT_Utility.parseNumberToString(
                            this instanceof GT_MetaTileEntity_QuantumChest
                                ? ((GT_MetaTileEntity_QuantumChest) this).mItemCount
                                : 0))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30));
    }
}
