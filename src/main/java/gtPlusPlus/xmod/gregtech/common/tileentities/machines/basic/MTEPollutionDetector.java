package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.pollution.Pollution;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEPollutionDetector extends MTETieredMachineBlock {

    int mCurrentPollution;
    int mAveragePollution;
    int[] mAveragePollutionArray = new int[10];
    private int mArrayPos = 0;
    private int mTickTimer = 0;
    private long mRedstoneLevel = 0;

    public MTEPollutionDetector(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
    }

    public MTEPollutionDetector(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Right click to check pollution levels.",
            "Configure with screwdriver to set redstone output amount.",
            "Does not use power.",
            GTPPCore.GT_Tooltip.get());
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return side == facing
            ? new ITexture[] { TextureFactory.of(TexturesGtBlock.Casing_Machine_Dimensional),
                TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_Frequency) }
            : new ITexture[] { TextureFactory.of(TexturesGtBlock.Casing_Machine_Dimensional),
                TextureFactory.of(Textures.GlobalIcons.VOID) };
    }

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

    /*
     * @Override public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final
     * byte aFacing, final int aColorIndex, final boolean aActive, final boolean aRedstone) { return
     * this.mTextures[(aActive ? 5 : 0) + (side == facing ? 0 : side == facing.getOpposite() ? 1 : side ==
     * ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1]; }
     */

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_2) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_2) };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEPollutionDetector(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mInventory.length);
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
        return 0;
    }

    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public long maxAmperesOut() {
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
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        this.showPollution(aPlayer.getEntityWorld(), aPlayer);
        return true;
    }

    private void showPollution(final World worldIn, final EntityPlayer playerIn) {
        if (!GTMod.proxy.mPollution) {
            GTUtility.sendChatToPlayer(playerIn, "This block is useless, Pollution is disabled.");
        } else {
            GTUtility.sendChatToPlayer(playerIn, "This chunk contains " + getCurrentChunkPollution() + " pollution.");
            GTUtility.sendChatToPlayer(playerIn, "Emit Redstone at pollution level: " + this.mRedstoneLevel);
        }
    }

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

    public int getCurrentChunkPollution() {
        return getCurrentChunkPollution(this.getBaseMetaTileEntity());
    }

    public int getCurrentChunkPollution(IGregTechTileEntity aBaseMetaTileEntity) {
        return Pollution.getPollution(aBaseMetaTileEntity);
    }

    @Override
    public String[] getInfoData() {
        return new String[] { this.getLocalName(),
            StatCollector
                .translateToLocalFormatted("gtpp.infodata.pollution_creator.pollution", this.mCurrentPollution),
            StatCollector
                .translateToLocalFormatted("gtpp.infodata.pollution_detector.pollution.avg", this.mAveragePollution),
            StatCollector
                .translateToLocalFormatted("gtpp.infodata.pollution_detector.redstone_level", this.mRedstoneLevel) };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        return GTValues.emptyIntArray;
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
    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {}

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setInteger("mCurrentPollution", this.mCurrentPollution);
        aNBT.setInteger("mAveragePollution", this.mAveragePollution);
        aNBT.setLong("mRedstoneLevel", this.mRedstoneLevel);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.mCurrentPollution = aNBT.getInteger("mCurrentPollution");
        this.mAveragePollution = aNBT.getInteger("mAveragePollution");
        this.mRedstoneLevel = aNBT.getLong("mRedstoneLevel");
    }

    @Override
    public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    public boolean allowCoverOnSide(final ForgeDirection side, final int aCoverID) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        // Only Calc server-side
        if (!this.getBaseMetaTileEntity()
            .isServerSide()) {
            return;
        }
        // Emit Redstone
        if (this.getCurrentChunkPollution() >= this.mRedstoneLevel) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                this.getBaseMetaTileEntity()
                    .setStrongOutputRedstoneSignal(side, (byte) 16);
            }
            this.markDirty();
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                this.getBaseMetaTileEntity()
                    .setStrongOutputRedstoneSignal(side, (byte) 0);
            }
            this.markDirty();
        }

        // Do Math for stats
        if (this.mTickTimer % 20 == 0) {
            mCurrentPollution = this.getCurrentChunkPollution();
            if (mArrayPos > mAveragePollutionArray.length - 1) {
                mArrayPos = 0;
            }
            mAveragePollutionArray[mArrayPos] = mCurrentPollution;
            mAveragePollution = getAveragePollutionOverLastTen();
            mArrayPos++;
        }
        this.mTickTimer++;
    }

    public int getAveragePollutionOverLastTen() {
        return MathUtils.getIntAverage(mAveragePollutionArray);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {

        if (side == this.getBaseMetaTileEntity()
            .getFrontFacing()) {
            final float[] tCoords = GTUtility.getClickedFacingCoords(side, aX, aY, aZ);
            switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + (2 * (byte) (int) (tCoords[1] * 2.0F)))) {
                case 0 -> this.mRedstoneLevel -= 5000;
                case 1 -> this.mRedstoneLevel += 5000;
                case 2 -> this.mRedstoneLevel -= 50000;
                case 3 -> this.mRedstoneLevel += 50000;
            }
            this.markDirty();
            GTUtility.sendChatToPlayer(aPlayer, "Emit Redstone at Pollution Level: " + this.mRedstoneLevel);
        }

        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        if (this.getCurrentChunkPollution() >= this.mRedstoneLevel) {
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public void onMachineBlockUpdate() {
        super.onMachineBlockUpdate();
    }
}
