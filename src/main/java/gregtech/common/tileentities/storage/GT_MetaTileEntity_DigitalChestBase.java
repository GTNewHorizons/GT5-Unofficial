package gregtech.common.tileentities.storage;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_QuantumChest;
import gregtech.common.gui.GT_GUIContainer_QuantumChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

@Optional.Interface(iface = "appeng.api.storage.IMEInventory", modid = "appliedenergistics2")
public abstract class GT_MetaTileEntity_DigitalChestBase extends GT_MetaTileEntity_TieredMachineBlock implements appeng.api.storage.IMEInventory<appeng.api.storage.data.IAEItemStack> {
    public GT_MetaTileEntity_DigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, "This Chest stores " + CommonSizeCompute(aTier) + " Blocks Use a screwdriver to enable voiding items on overflow");
    }

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_DigitalChestBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
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
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }
    
    protected boolean mVoidOverflow = false;

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mVoidOverflow = !mVoidOverflow;
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal(mVoidOverflow ? "GT5U.machines.voidoveflow.enabled" : "GT5U.machines.voidoveflow.disabled"));
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
            if ((mInventory[0] != null) && ((count < getMaxItemCount())|| mVoidOverflow ) && GT_Utility.areStacksEqual(mInventory[0], stack)) {
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
        }
    }

    abstract protected int getItemCount();
    abstract public void setItemCount(int aCount);
    abstract protected ItemStack getItemStack();
    abstract protected void setItemStack(ItemStack s);

    @Override
    public int getProgresstime() {
        return getItemCount() + (mInventory[0] == null ? 0 : mInventory[0].stackSize) + (mInventory[1] == null ? 0 : mInventory[1].stackSize);
    }

    @Override
    public int maxProgresstime() {
        return getMaxItemCount();
    }

    protected static int CommonSizeCompute(int tier){
        switch(tier){
            case 1:
                return    4000000;
            case 2:
                return    8000000;
            case 3:
                return   16000000;
            case 4:
                return   32000000;
            case 5:
                return   64000000;
            case 6:
                return  128000000;
            case 7:
                return  256000000;
            case 8:
                return  512000000;
            case 9:
                return 1024000000;
            case 10:
                return 2147483640;
            default:
                return 0;
        }
    }

    @Override
    public int getMaxItemCount() {
        return CommonSizeCompute(mTier);
    }
    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }
    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aIndex == 0 && (mInventory[0] == null || GT_Utility.areStacksEqual(mInventory[0], aStack));
    }

    abstract protected String chestName();

    @Optional.Method(modid = "appliedenergistics2")
    public static void registerAEIntegration() {
        appeng.api.AEApi.instance().registries().externalStorage().addExternalStorageInterface(new AE2DigitalChestHandler());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aBaseMetaTileEntity.getFrontFacing() == 0 && aSide == 4) {
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_QCHEST)};
        }
        return aSide == aBaseMetaTileEntity.getFrontFacing() ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_QCHEST)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};//aSide != aFacing ? mMachineBlock != 0 ? new ITexture[] {Textures.BlockIcons.CASING_BLOCKS[mMachineBlock]} : new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex+1]} : mMachineBlock != 0 ? aActive ? getTexturesActive(Textures.BlockIcons.CASING_BLOCKS[mMachineBlock]) : getTexturesInactive(Textures.BlockIcons.CASING_BLOCKS[mMachineBlock]) : aActive ? getTexturesActive(Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex+1]) : getTexturesInactive(Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex+1]);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public String[] getInfoData() {

        if (getItemStack() == null) {
            return new String[]{
                    EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET,
                    "Stored Items:",
                    EnumChatFormatting.GOLD + "No Items" + EnumChatFormatting.RESET,
                    EnumChatFormatting.GREEN + "0" + EnumChatFormatting.RESET + " " +
                            EnumChatFormatting.YELLOW + getMaxItemCount() + EnumChatFormatting.RESET
            };
        }
        return new String[]{
                EnumChatFormatting.BLUE + chestName() + EnumChatFormatting.RESET,
                "Stored Items:",
                EnumChatFormatting.GOLD + getItemStack().getDisplayName() + EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + Integer.toString(getItemCount()) + EnumChatFormatting.RESET + " " +
                        EnumChatFormatting.YELLOW + getMaxItemCount() + EnumChatFormatting.RESET
        };
    }

    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IAEItemStack injectItems(final appeng.api.storage.data.IAEItemStack input, final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        final ItemStack inputStack = input.getItemStack();
        if (inputStack == null)
            return null;
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            if (GT_Utility.areStacksEqual(storedStack, inputStack)) {
                if (input.getStackSize() + getItemCount() > getMaxItemCount())
                {
                    if (mVoidOverflow)
                    {
                        if (mode != appeng.api.config.Actionable.SIMULATE)
                            setItemCount(getMaxItemCount());
                        return null;
                    }
                    else
                    {
                        return createOverflowStack(input.getStackSize() + getItemCount(), mode);
                    }
                }
                else {
                    if (mode != appeng.api.config.Actionable.SIMULATE)
                        setItemCount(getItemCount() + (int) input.getStackSize());
                }
                return null;
            } else
                return input;
        } else {
            if (mode != appeng.api.config.Actionable.SIMULATE)
                setItemStack(inputStack.copy());
            if (input.getStackSize() > getMaxItemCount())
                return createOverflowStack(input.getStackSize(), mode);
            else if (mode != appeng.api.config.Actionable.SIMULATE)
                setItemCount((int) input.getStackSize());
            return null;
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private appeng.api.storage.data.IAEItemStack createOverflowStack(long size, appeng.api.config.Actionable mode) {
        final appeng.api.storage.data.IAEItemStack overflow = appeng.util.item.AEItemStack.create(getItemStack());
        overflow.setStackSize(size - getMaxItemCount());
        if (mode != appeng.api.config.Actionable.SIMULATE)
            setItemCount(getMaxItemCount());
        return overflow;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IAEItemStack extractItems(final appeng.api.storage.data.IAEItemStack request, final appeng.api.config.Actionable mode, final appeng.api.networking.security.BaseActionSource src) {
        if (request.equals(getItemStack())) {
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

    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.StorageChannel getChannel() {
        return appeng.api.storage.StorageChannel.ITEMS;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> getAvailableItems(final appeng.api.storage.data.IItemList<appeng.api.storage.data.IAEItemStack> out) {
        ItemStack storedStack = getItemStack();
        if (storedStack != null) {
            appeng.util.item.AEItemStack s = appeng.util.item.AEItemStack.create(storedStack);
            s.setStackSize(getItemCount());
            out.add(s);
        }
        return out;
    }
}
