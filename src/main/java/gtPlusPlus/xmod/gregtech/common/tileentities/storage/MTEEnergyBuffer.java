package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEEnergyBuffer extends GTPPMetaTileEntity {

    protected byte aCurrentOutputAmperage = 4;

    public MTEEnergyBuffer(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
    }

    public MTEEnergyBuffer(final String aName, final int aTier, final String aDescription,
        final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, "Defaults 4A In/Out", "Change output Amperage with a screwdriver",
            "Now Portable!", GTPPCore.GT_Tooltip.get() };
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aCover) {
        if (side != this.getBaseMetaTileEntity()
            .getFrontFacing()) {
            return true;
        }
        return super.allowCoverOnSide(side, aCover);
    }

    /*
     * MACHINE_STEEL_SIDE
     */

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Screen_Logo) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Screen_Logo) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEEnergyBuffer(this.mName, this.mTier, this.mDescription, this.mTextures, this.mInventory.length);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return true;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(final ForgeDirection side) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[this.mTier] * 2;
    }

    @Override
    public long maxEUStore() {
        return V[this.mTier] * 250000;
    }

    @Override
    public long maxEUInput() {
        return V[this.mTier];
    }

    @Override
    public long maxEUOutput() {
        return V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return aCurrentOutputAmperage;
    }

    @Override
    public long maxAmperesOut() {
        return aCurrentOutputAmperage;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

    @Override
    public int getProgresstime() {
        return (int) this.getBaseMetaTileEntity()
            .getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) this.getBaseMetaTileEntity()
            .getUniversalEnergyCapacity();
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setByte("aCurrentOutputAmperage", aCurrentOutputAmperage);
        long aEU = this.getBaseMetaTileEntity()
            .getStoredEU();
        if (aEU > 0) {
            aNBT.setLong("aStoredEU", aEU);
            if (aNBT.hasKey("aStoredEU")) {
                Logger.WARNING("Set aStoredEU to NBT.");
            }
        }
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        aCurrentOutputAmperage = aNBT.getByte("aCurrentOutputAmperage");
        if (aNBT.hasKey("aStoredEU")) {
            this.setEUVar(aNBT.getLong("aStoredEU"));
        }
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        Logger.WARNING("Right Click on MTE by Player");
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }

        Logger.WARNING("MTE is Client-side");
        this.showEnergy(aPlayer.getEntityWorld(), aPlayer);
        return true;
    }

    protected void showEnergy(final World worldIn, final EntityPlayer playerIn) {
        final long tempStorage = this.getBaseMetaTileEntity()
            .getStoredEU();
        final double c = ((double) tempStorage / this.maxEUStore()) * 100;
        final double roundOff = Math.round(c * 100.00) / 100.00;
        PlayerUtils.messagePlayer(
            playerIn,
            "Energy: " + GTUtility.formatNumbers(tempStorage) + " EU at " + V[this.mTier] + "v (" + roundOff + "%)");
        PlayerUtils.messagePlayer(playerIn, "Amperage: " + GTUtility.formatNumbers(maxAmperesOut()) + "A");
    }
    // Utils.LOG_WARNING("Begin Show Energy");
    /*
     * //Utils.LOG_INFO("getProgresstime: "+tempStorage+"  maxProgresstime: "+maxEUStore()+"  C: "+c);
     * Utils.LOG_INFO("getProgressTime: "+getProgresstime()); Utils.LOG_INFO("maxProgressTime: "+maxProgresstime());
     * Utils.LOG_INFO("getMinimumStoredEU: "+getMinimumStoredEU()); Utils.LOG_INFO("maxEUStore: "+maxEUStore());
     */
    /*
     * final long d = (tempStorage * 100L) / maxEUStore();
     * Utils.LOG_INFO("getProgresstime: "+tempStorage+"  maxProgresstime: "+maxEUStore()+"  D: "+d); final double
     * roundOff2 = Math.round(d * 100.00) / 100.00; Utils.messagePlayer(playerIn, "Energy: " + tempStorage +
     * " EU at "+V[mTier]+"v ("+roundOff2+"%)"); Utils.LOG_WARNING("Making new instance of Guihandler"); GuiHandler
     * block = new GuiHandler(); Utils.LOG_WARNING("Guihandler.toString(): "+block.toString());
     * block.getClientGuiElement(1, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
     */

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        String cur = GTUtility.formatNumbers(
            this.getBaseMetaTileEntity()
                .getStoredEU());
        String max = GTUtility.formatNumbers(
            this.getBaseMetaTileEntity()
                .getEUCapacity());

        // Right-align current storage with maximum storage
        String fmt = String.format("%%%ds", max.length());
        cur = String.format(fmt, cur);

        return new String[] { cur + " EU stored", max + " EU capacity" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(final int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {}

    @Override
    public String getInventoryName() {
        return super.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        aNBT.setByte("aCurrentOutputAmperage", aCurrentOutputAmperage);
        long aEU = this.getBaseMetaTileEntity()
            .getStoredEU();
        if (aEU > 0) {
            aNBT.setLong("aStoredEU", aEU);
            if (aNBT.hasKey("aStoredEU")) {
                Logger.WARNING("Set aStoredEU to NBT.");
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        byte aTest = (byte) (aCurrentOutputAmperage + 1);
        if (aTest > 16 || aTest <= 0) {
            aTest = 1;
        }
        aCurrentOutputAmperage = aTest;
        PlayerUtils.messagePlayer(aPlayer, "Now handling " + aCurrentOutputAmperage + " Amps.");
    }
}
