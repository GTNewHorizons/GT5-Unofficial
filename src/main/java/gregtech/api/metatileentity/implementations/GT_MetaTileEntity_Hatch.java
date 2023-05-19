package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.crafting.ICraftingIconProvider;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Handles texture changes internally. No special calls are necessary other than updateTexture in add***ToMachineList.
 */
public abstract class GT_MetaTileEntity_Hatch extends GT_MetaTileEntity_BasicTank implements ICraftingIconProvider {

    public enum ConnectionType {
        CABLE,
        WIRELESS,
        LASER
    }

    /**
     * Uses new texture changing methods to avoid limitations of byte as texture index...
     */
    @Deprecated
    public byte mMachineBlock = 0;

    private byte mTexturePage = 0;
    private byte actualTexture = 0;

    private ItemStack ae2CraftingIcon;

    public GT_MetaTileEntity_Hatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        int texturePointer = (byte) (actualTexture & 0x7F); // just to be sure, from my testing the 8th bit cannot be
                                                            // set clientside
        int textureIndex = texturePointer | (mTexturePage << 7); // Shift seven since one page is 128 textures!
        try {
            if (side != aFacing) {
                if (textureIndex > 0)
                    return new ITexture[] { Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer] };
                else return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1] };
            } else {
                if (textureIndex > 0) {
                    if (aActive)
                        return getTexturesActive(Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer]);
                    else return getTexturesInactive(
                        Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer]);
                } else {
                    if (aActive) return getTexturesActive(Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1]);
                    else return getTexturesInactive(Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1]);
                }
            }
        } catch (NullPointerException npe) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[0][0] };
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mMachineBlock", actualTexture);
        aNBT.setByte("mTexturePage", mTexturePage);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        actualTexture = aNBT.getByte("mMachineBlock");
        mTexturePage = aNBT.getByte("mTexturePage");

        if (mTexturePage != 0 && GT_Values.GT.isServerSide()) actualTexture |= 0x80; // <- lets just hope no one needs
                                                                                     // the correct value for that on
                                                                                     // server
        mMachineBlock = actualTexture;
    }

    /**
     * Sets texture with page and index, called on add to machine list
     *
     * @param id (page<<7)+index of the texture
     */
    public final void updateTexture(int id) {
        onValueUpdate((byte) id);
        onTexturePageUpdate((byte) (id >> 7));
    }

    /**
     * Sets the icon for the owning multiblock used for AE2 crafting display of attached interfaces, called on add to
     * machine list
     */
    public final void updateCraftingIcon(ItemStack icon) {
        this.ae2CraftingIcon = icon;
    }

    @Override
    public ItemStack getMachineCraftingIcon() {
        return this.ae2CraftingIcon;
    }

    /**
     * Sets texture with page and index, rather unusable, but kept FFS
     *
     * @param page  page of texure
     * @param index index of texure
     */
    @Deprecated
    public final void updateTexture(byte page, byte index) {
        onValueUpdate(index);
        onTexturePageUpdate(page);
    }

    @Override
    public final void onValueUpdate(byte aValue) {
        actualTexture = (byte) (aValue & 0x7F);
        mMachineBlock = actualTexture;
        mTexturePage = 0;
    }

    /**
     * Get the maximum amount of amperes to work with, which excludes the additional amps in for loss
     *
     * @return Working amps
     */
    public long maxWorkingAmperesIn() {
        return maxAmperesIn();
    }

    /**
     * Get the type of connection this hatch allows
     *
     * @return Connection type
     */
    public ConnectionType getConnectionType() {
        return ConnectionType.CABLE;
    }

    @Override
    public final byte getUpdateData() {
        return (byte) (actualTexture & 0x7F);
    }

    public final void onTexturePageUpdate(byte aValue) {
        mTexturePage = (byte) (aValue & 0x7F);
        if (mTexturePage != 0 && getBaseMetaTileEntity().isServerSide()) { // just to be sure
            mMachineBlock |= 0x80; // <- lets just hope no one needs the correct value for that on server
            actualTexture = mMachineBlock;
        }
        // set last bit to allow working of the page reset-er to 0 in rare case when texture id is the same but page
        // changes to 0
    }

    public final byte getTexturePage() {
        return (byte) (mTexturePage & 0x7F);
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) { // in that method since it is usually
                                                                                 // not overriden, especially for
                                                                                 // hatches.
        if (actualTexture != mMachineBlock) { // revert to page 0 on edition of the field - old code way
            actualTexture = (byte) (mMachineBlock & 0x7F);
            mMachineBlock = actualTexture; // clear last bit in mMachineBlock since now we are at page 0 after the
                                           // direct field
            // change
            mTexturePage = 0; // assuming old code only supports page 0
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    // To change to other page -> use the setter method -> updateTexture

    public int getCircuitSlot() {
        return -1;
    }
}
