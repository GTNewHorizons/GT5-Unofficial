package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.crafting.ICraftingIconProvider;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Handles texture changes internally. No special calls are necessary other than updateTexture in add***ToMachineList.
 */
public abstract class MTEHatch extends MTEBasicTank implements ICraftingIconProvider {

    public enum ConnectionType {
        CABLE,
        WIRELESS,
        LASER,
        BIO
    }

    private int texturePage = 0;
    private int textureIndex = 0;

    private ItemStack ae2CraftingIcon;

    public MTEHatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatch(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return (aTier + 1) * (aTier + 1);
    }

    private int getOffsetTier() {
        return mTier < 4 ? 0 : mTier - 1;
    }

    public int getOffsetX() {
        return getOffsetTier() * 2;
    }

    public int getOffsetY() {
        return getOffsetTier() * 16;
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

        try {
            ITexture background;

            if (texturePage > 0 || textureIndex > 0) {
                background = Textures.BlockIcons.casingTexturePages[texturePage][textureIndex];
            } else {
                background = Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1];
            }

            if (side != aFacing) {
                return new ITexture[] { background };
            } else {
                if (aActive) {
                    return getTexturesActive(background);
                } else {
                    return getTexturesInactive(background);
                }
            }
        } catch (NullPointerException npe) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[0][0] };
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("texturePage", texturePage);
        aNBT.setInteger("textureIndex", textureIndex);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        texturePage = aNBT.getInteger("texturePage");
        textureIndex = aNBT.getInteger("textureIndex");

        updateTexture(texturePage << 7 | textureIndex);
    }

    /**
     * Sets texture with page and index, called on add to machine list
     *
     * @param id (page<<7)+index of the texture
     */
    public final void updateTexture(int id) {
        int newTexturePage = id >> 7;
        int newTextureIndex = id & 127;
        if (newTexturePage == texturePage && newTextureIndex == textureIndex) return;
        texturePage = newTexturePage;
        textureIndex = newTextureIndex;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base.isServerSide()) {
            base.issueTileUpdate();
        } else {
            base.issueTextureUpdate();
        }
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = new NBTTagCompound();

        data.setInteger("texturePage", texturePage);
        data.setInteger("textureIndex", textureIndex);

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        texturePage = data.getInteger("texturePage");
        textureIndex = data.getInteger("textureIndex");
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
     * Some multiblocks restrict hatches by tier. This method allows hatches to specify custom tier used for structure
     * check, while keeping {@link #mTier} for other uses.
     *
     * @return Tier used for multiblock structure
     */
    public byte getTierForStructure() {
        return mTier;
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

    public int getCircuitSlot() {
        return -1;
    }
}
