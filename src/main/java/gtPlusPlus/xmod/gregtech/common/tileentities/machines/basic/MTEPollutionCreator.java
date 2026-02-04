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
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEPollutionCreator extends MTETieredMachineBlock {

    int mCurrentPollution;
    int mAveragePollution;
    int[] mAveragePollutionArray = new int[10];
    private int mArrayPos = 0;
    private int mTickTimer = 0;

    public MTEPollutionCreator(Args args) {
        super(args);
    }

    @Deprecated
    public MTEPollutionCreator(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
    }

    @Deprecated
    public MTEPollutionCreator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils
            .addAll(this.mDescriptionArray, "A useful debug machine to create pollution.", GTPPCore.GT_Tooltip.get());
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

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_2) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_2) };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier + 3][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (pollutionMultiplier > 99) {
            pollutionMultiplier = 1;
        } else {
            pollutionMultiplier++;
        }
        GTUtility.sendChatToPlayer(aPlayer, "Pollution Mutliplier is now " + pollutionMultiplier + ".");
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEPollutionCreator(getPrototype());
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

    public int pollutionMultiplier = 1;

    private void showPollution(final World worldIn, final EntityPlayer playerIn) {
        if (!GTMod.proxy.mPollution) {
            GTUtility.sendChatToPlayer(playerIn, "This block is useless, Pollution is disabled.");
        } else {
            addPollution();
            GTUtility
                .sendChatToPlayer(playerIn, "This chunk now contains " + getCurrentChunkPollution() + " pollution.");
            // GTUtility.sendChatToPlayer(playerIn, "Average over last ten minutes: "+getAveragePollutionOverLastTen()+"
            // pollution.");
        }
    }

    private boolean addPollution() {
        Pollution.addPollution(getBaseMetaTileEntity(), 100000 * pollutionMultiplier);
        return true;
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
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.pollution_creator.pollution.avg",
                getAveragePollutionOverLastTen()) };
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
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.mCurrentPollution = aNBT.getInteger("mCurrentPollution");
        this.mAveragePollution = aNBT.getInteger("mAveragePollution");
    }

    @Override
    public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            if (this.mCurrentPollution == 0) {
                this.mCurrentPollution = getCurrentChunkPollution();
            }
            if (this.mArrayPos < 0 || this.mArrayPos > 9) {
                this.mArrayPos = 0;
            }
            this.mTickTimer = 0;
        }
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            // TickTimer - 20 times a second
            this.mTickTimer++;
            if (mTickTimer % 20 == 0) {
                this.mCurrentPollution = getCurrentChunkPollution();
            }
        }
    }

    public int getAveragePollutionOverLastTen() {
        int counter = 0;
        int total = 0;

        for (int j : this.mAveragePollutionArray) {
            if (j != 0) {
                total += j;
                counter++;
            }
        }
        int returnValue = 0;
        if (total > 0 && counter > 0) {
            returnValue = (total / counter);
            this.mAveragePollution = returnValue;
        } else {
            returnValue = getCurrentChunkPollution();
        }
        // Logger.INFO("| DEBUG: "+returnValue +" | ArrayPos:"+this.mArrayPos+" | Counter:"+counter+" | Total:"+total+"
        // |");
        return returnValue;
    }
}
