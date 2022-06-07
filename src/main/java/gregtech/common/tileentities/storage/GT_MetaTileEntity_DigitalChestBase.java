package gregtech.common.tileentities.storage;

import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_QuantumChest;
import gregtech.common.gui.GT_GUIContainer_QuantumChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.Map;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;

@Optional.Interface(iface = "appeng.api.storage.IMEMonitor", modid = "appliedenergistics2", striprefs = true)
public abstract class GT_MetaTileEntity_DigitalChestBase extends GT_MetaTileEntity_TieredMachineBlock implements appeng.api.storage.IMEMonitor<appeng.api.storage.data.IAEItemStack> {
    protected boolean mVoidOverflow = false;
    public boolean voidBreak;
    private Map<appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack>, Object> listeners = null;

    public GT_MetaTileEntity_DigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, new String[]{
                "This Chest stores " + GT_Utility.formatNumbers(commonSizeCompute(aTier)) + " Blocks",
                "Use a screwdriver to enable",
                "voiding items on overflow",
                "Can keep its contents when harvested",
                "Sneak when harvesting to void its contents"
        });
    }

    protected static int commonSizeCompute(int tier) {
        switch (tier) {
            case 1:
                return 4000000;
            case 2:
                return 8000000;
            case 3:
                return 16000000;
            case 4:
                return 32000000;
            case 5:
                return 64000000;
            case 6:
                return 128000000;
            case 7:
                return 256000000;
            case 8:
                return 512000000;
            case 9:
                return 1024000000;
            case 10:
                return 2147483640;
            default:
                return 0;
        }
    }

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Optional.Method(modid = "appliedenergistics2")
    public static void registerAEIntegration() {
        appeng.api.AEApi.instance().registries().externalStorage().addExternalStorageInterface(new AE2DigitalChestHandler());
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public void addListener(appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack> imeMonitorHandlerReceiver, Object o) {
        if (listeners == null)
            listeners = new HashMap<>();
        listeners.put(imeMonitorHandlerReceiver, o);
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public void removeListener(appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack> imeMonitorHandlerReceiver) {
        if (listeners == null)
            listeners = new HashMap<>();
        listeners.remove(imeMonitorHandlerReceiver);
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public appeng.api.config.AccessRestriction getAccess() {
        return appeng.api.config.AccessRestriction.READ_WRITE;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean isPrioritized(appeng.api.storage.data.IAEItemStack iaeItemStack) {
        ItemStack s = getItemStack();
        if (s == null || iaeItemStack == null)
            return false;
        return iaeItemStack.isSameType(s);
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean canAccept(appeng.api.storage.data.IAEItemStack iaeItemStack) {
        ItemStack s = getItemStack();
        if (s == null || iaeItemStack == null)
            return true;
        return iaeItemStack.isSameType(s);
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public int getPriority() {
        return 0;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public int getSlot() {
        return 0;
    }

    @Optional.Method(modid = "appliedenergistics2")
    @Override
    public boolean validForPass(int i) {
        return true;
    }

    protected abstract ItemStack getItemStack();

    protected abstract void setItemStack(ItemStack s);

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> getAvailableItems(final appeng.api.storage.data.IItemList out) {
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(storedStack);
            s.setStackSize(getItemCount());
            out.add(s);
        }
        return out;
    }

    @Optional.Method(modid = "appliedenergistics2")
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
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_QuantumChest(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_QuantumChest(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IAEItemStack injectItems(final appeng.api.storage.data.IAEItemStack input, final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        final ItemStack inputStack = input.getItemStack();
        if (inputStack == null)
            return null;
        if (mode != appeng.api.config.Actionable.SIMULATE)
            getBaseMetaTileEntity().markDirty();
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            if (GT_Utility.areStacksEqual(storedStack, inputStack)) {
                if (input.getStackSize() + getItemCount() > getMaxItemCount()) {
                    if (mVoidOverflow) {
                        if (mode != appeng.api.config.Actionable.SIMULATE)
                            setItemCount(getMaxItemCount());
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
        if (mode != appeng.api.config.Actionable.SIMULATE)
            setItemStack(inputStack.copy());
        if (input.getStackSize() > getMaxItemCount())
            return createOverflowStack(input.getStackSize(), mode);
        if (mode != appeng.api.config.Actionable.SIMULATE)
            setItemCount((int) input.getStackSize());
        return null;
    }

    @Optional.Method(modid = "appliedenergistics2")
    private appeng.api.storage.data.IAEItemStack createOverflowStack(long size, appeng.api.config.Actionable mode) {
        final appeng.api.storage.data.IAEItemStack overflow = appeng.util.item.AEItemStack.create(getItemStack());
        overflow.setStackSize(size - getMaxItemCount());
        if (mode != appeng.api.config.Actionable.SIMULATE)
            setItemCount(getMaxItemCount());
        return overflow;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IAEItemStack extractItems(final appeng.api.storage.data.IAEItemStack request, final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        if (request.isSameType(getItemStack())) {
            if (mode != appeng.api.config.Actionable.SIMULATE)
                getBaseMetaTileEntity().markDirty();
            if (request.getStackSize() >= getItemCount()) {
                appeng.util.item.AEItemStack result = appeng.util.item.AEItemStack.create(getItemStack());
                result.setStackSize(getItemCount());
                if (mode != appeng.api.config.Actionable.SIMULATE)
                    setItemCount(0);
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
    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.StorageChannel getChannel() {
        return appeng.api.storage.StorageChannel.ITEMS;
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mVoidOverflow = !mVoidOverflow;
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal(mVoidOverflow ? "GT5U.machines.voidoveflow.enabled" : "GT5U.machines.voidoveflow.disabled"));
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

            if ((mInventory[0] != null) && ((count < getMaxItemCount()) || mVoidOverflow) && GT_Utility.areStacksEqual(mInventory[0], stack)) {
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
            } else if ((count > 0) && GT_Utility.areStacksEqual(mInventory[1], stack) && mInventory[1].getMaxStackSize() > mInventory[1].stackSize) {
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

            if (GregTech_API.mAE2)
                notifyListeners(count - savedCount, stack);
            if (count != savedCount)
                getBaseMetaTileEntity().markDirty();
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aBaseMetaTileEntity.isClientSide()) aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        voidBreak = aPlayer.isSneaking();
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
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
        return getItemCount() + (mInventory[0] == null ? 0 : mInventory[0].stackSize) + (mInventory[1] == null ? 0 : mInventory[1].stackSize);
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
            return new String[]{
                    EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET,
                    "Stored Items:",
                    EnumChatFormatting.GOLD + "No Items" + EnumChatFormatting.RESET,
                    EnumChatFormatting.GREEN + "0" + EnumChatFormatting.RESET + " " +
                            EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getMaxItemCount()) + EnumChatFormatting.RESET
            };
        }
        return new String[]{
                EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET,
                "Stored Items:",
                EnumChatFormatting.GOLD + getItemStack().getDisplayName() + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(getItemCount()) + EnumChatFormatting.RESET + " " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getMaxItemCount()) + EnumChatFormatting.RESET
        };
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }

    protected abstract String chestName();

    @Optional.Method(modid = "appliedenergistics2")
    private void notifyListeners(int count, ItemStack stack) {
        if (listeners == null) {
            listeners = new HashMap<>();
            return;
        }
        if (count == 0 || stack == null)
            return;
        appeng.util.item.ItemList change = new appeng.util.item.ItemList();
        appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(stack);
        s.setStackSize(count);
        change.add(s);
        listeners.forEach((l, o) -> {
            if (l.isValid(o))
                l.postChange(this, change, null);
            else
                removeListener(l);
        });
    }

    @Optional.Method(modid = "appliedenergistics2")
    private boolean hasActiveMEConnection() {
        if (listeners == null || listeners.isEmpty())
            return false;
        for (Map.Entry<appeng.api.storage.IMEMonitorHandlerReceiver<appeng.api.storage.data.IAEItemStack>, Object> e : listeners.entrySet())
        {
            if ((e.getKey() instanceof appeng.api.parts.IPart))
            {
                appeng.api.networking.IGridNode n = ((appeng.api.parts.IPart) e.getKey()).getGridNode();
                if (n != null && n.isActive())
                    return true;
            }
        }
        // if there are no active storage buses - clear the listeners
        listeners.clear();
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", getItemCount());
        if (getItemStack() != null)
            aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount"))
            setItemCount(aNBT.getInteger("mItemCount"));
        if (aNBT.hasKey("mItemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack")));
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");

    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (GregTech_API.mAE2 && GT_Values.disableDigitalChestsExternalAccess && hasActiveMEConnection())
            return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (GregTech_API.mAE2 && GT_Values.disableDigitalChestsExternalAccess && hasActiveMEConnection())
            return false;
        return aIndex == 0 && (mInventory[0] == null || GT_Utility.areStacksEqual(mInventory[0], aStack));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide != aFacing) return new ITexture[]{MACHINE_CASINGS[mTier][aColorIndex + 1]};
        return new ITexture[]{
                MACHINE_CASINGS[mTier][aColorIndex + 1],
                TextureFactory.of(OVERLAY_SCHEST),
                TextureFactory.builder().addIcon(OVERLAY_SCHEST_GLOW).glow().build()
        };
    }
}
