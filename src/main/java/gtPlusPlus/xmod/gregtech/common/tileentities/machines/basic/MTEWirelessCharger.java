package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;

public class MTEWirelessCharger extends MTETieredMachineBlock {

    private static final int MODE_LONG_RANGE = 0;
    private static final int MODE_LOCAL = 1;
    private static final int MODE_MIXED = 2;

    private int mCurrentDimension = 0;
    public int mMode = MODE_LONG_RANGE;
    public boolean mLocked = true;

    public MTEWirelessCharger(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
    }

    public MTEWirelessCharger(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Can be locked to the owner by sneaking with a screwdriver",
            "Can also be locked with a lock upgrade",
            "",
            "3 Modes, Long-Range, Local and Mixed.",
            "Long-Range: Can supply 2A of power to a single player up to " + this.getLongRange(false) + "m away.",
            "Local: Can supply several Amps to each player within " + this.getLocalRange(false) + "m.",
            "Mixed: Provides both 2A of long range and 1A per player locally.",
            "Mixed mode is more conservative of power and as a result only",
            "Gets half the distances each singular mode gets.",
            GTPPCore.GT_Tooltip.get());
    }

    public int getTier() {
        return this.mTier;
    }

    public int getMode() {
        return this.mMode;
    }

    public int getDimensionID() {
        return this.mCurrentDimension;
    }

    public Map<String, UUID> getLocalMap() {
        return this.mLocalChargingMap;
    }

    public Map<String, UUID> getLongRangeMap() {
        return this.mWirelessChargingMap;
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {

        if (aPlayer.isSneaking()) {
            mLocked = !mLocked;
            PlayerUtils.messagePlayer(aPlayer, mLocked ? "Locked to owner." : "Unlocked.");
            return;
        }

        mWirelessChargingMap.clear();
        mLocalChargingMap.clear();
        if (!this.getBaseMetaTileEntity()
            .getWorld().playerEntities.isEmpty()) {
            for (EntityPlayer player : this.getBaseMetaTileEntity()
                .getWorld().playerEntities) {
                ChargingHelper.removeValidPlayer(player, this);
            }
        }

        if (this.mMode >= MODE_MIXED) {
            this.mMode = MODE_LONG_RANGE;
        } else {
            this.mMode++;
        }
        if (this.mMode == MODE_LONG_RANGE) {
            PlayerUtils.messagePlayer(aPlayer, "Now in Long-Range Charge Mode.");
        } else if (this.mMode == MODE_LOCAL) {
            PlayerUtils.messagePlayer(aPlayer, "Now in Local Charge Mode.");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Now in Mixed Charge Mode.");
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEWirelessCharger(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mInventory.length);
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
        return false;
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
    public long maxEUStore() {
        return GTValues.V[this.mTier] * 128;
    }

    @Override
    public int getCapacity() {
        return (int) (GTValues.V[this.mTier] * 32);
    }

    @Override
    public long maxEUInput() {
        return GTValues.V[this.mTier];
    }

    @Override
    public long maxEUOutput() {
        return 0;
    }

    @Override
    public long maxAmperesIn() {
        if (this.mMode == MODE_LONG_RANGE) {
            return 2;
        } else if (this.mMode == MODE_LOCAL) {
            return this.mLocalChargingMap.size() * 8;
        } else {
            return ((this.mLocalChargingMap.size() * 4) + this.mWirelessChargingMap.size());
        }
    }

    @Override
    public long maxAmperesOut() {
        return 0;
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
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        aBaseMetaTileEntity.isClientSide();
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

    @Override
    public String[] getInfoData() {
        return new String[] { this.getLocalName() };
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
        return null;
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
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setBoolean("mLocked", this.mLocked);
        aNBT.setInteger("mMode", this.mMode);
        aNBT.setInteger("mCurrentDimension", this.mCurrentDimension);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.mLocked = aNBT.getBoolean("mLocked");
        this.mMode = aNBT.getInteger("mMode");
        this.mCurrentDimension = aNBT.getInteger("mCurrentDimension");
    }

    @Override
    public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    private final Map<String, UUID> mWirelessChargingMap = new HashMap<>();
    private final Map<String, UUID> mLocalChargingMap = new HashMap<>();

    private boolean isValidPlayer(EntityPlayer aPlayer) {
        BaseMetaTileEntity aTile = (BaseMetaTileEntity) this.getBaseMetaTileEntity();
        if (mLocked || (aTile != null && aTile.privateAccess())) {
            return aPlayer.getUniqueID()
                .equals(getBaseMetaTileEntity().getOwnerUuid());
        }
        return true;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!this.getBaseMetaTileEntity()
            .isServerSide()) {
            return;
        }

        if (this.mCurrentDimension != aBaseMetaTileEntity.getWorld().provider.dimensionId) {
            this.mCurrentDimension = aBaseMetaTileEntity.getWorld().provider.dimensionId;
        }

        if (aTick % 20 == 0) {
            boolean mHasBeenMapped = this.equals(ChargingHelper.getEntry(getTileEntityPosition()));
            if (!mHasBeenMapped) {
                mHasBeenMapped = ChargingHelper.addEntry(getTileEntityPosition(), this);
            }

            if (mHasBeenMapped && !aBaseMetaTileEntity.getWorld().playerEntities.isEmpty()) {
                for (EntityPlayer player : aBaseMetaTileEntity.getWorld().playerEntities) {
                    if (this.mMode == MODE_LOCAL || this.mMode == MODE_MIXED) {
                        if (getDistanceBetweenTwoPositions(getTileEntityPosition(), getPositionOfEntity(player))
                            < this.getLocalRange(this.mMode == MODE_MIXED)) {
                            if (isValidPlayer(player) && !mLocalChargingMap.containsKey(player.getDisplayName())) {
                                mLocalChargingMap.put(player.getDisplayName(), player.getPersistentID());
                                ChargingHelper.addValidPlayer(player, this);
                            }
                        } else {
                            if (mLocalChargingMap.containsKey(player.getDisplayName())) {
                                if (mLocalChargingMap.remove(player.getDisplayName()) != null) {
                                    ChargingHelper.removeValidPlayer(player, this);
                                }
                            }
                        }
                    }
                    if (this.mMode == MODE_LONG_RANGE || this.mMode == MODE_MIXED) {
                        int tempRange = getLongRange(this.mMode == MODE_MIXED);
                        if (getDistanceBetweenTwoPositions(getTileEntityPosition(), getPositionOfEntity(player))
                            <= tempRange) {
                            if (!mWirelessChargingMap.containsKey(player.getDisplayName())) {
                                if (isValidPlayer(player)) {
                                    mWirelessChargingMap.put(player.getDisplayName(), player.getPersistentID());
                                    ChargingHelper.addValidPlayer(player, this);
                                    PlayerUtils.messagePlayer(
                                        player,
                                        "You have entered charging range. [" + tempRange + "m - Long-Range].");
                                }
                            }
                        } else {
                            if (mWirelessChargingMap.containsKey(player.getDisplayName())) {
                                if (mWirelessChargingMap.remove(player.getDisplayName()) != null) {
                                    PlayerUtils.messagePlayer(
                                        player,
                                        "You have left charging range. [" + tempRange + "m - Long Range].");
                                    ChargingHelper.removeValidPlayer(player, this);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public BlockPos getTileEntityPosition() {
        return new BlockPos(
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            this.getBaseMetaTileEntity()
                .getWorld());
    }

    public BlockPos getPositionOfEntity(Entity mEntity) {
        if (mEntity == null) {
            return null;
        }
        return EntityUtils.findBlockPosUnderEntity(mEntity);
    }

    public double getDistanceBetweenTwoPositions(BlockPos objectA, BlockPos objectB) {
        if (objectA == null || objectB == null) {
            return 0f;
        }

        return Math.sqrt(
            (objectB.xPos - objectA.xPos) * (objectB.xPos - objectA.xPos)
                + (objectB.yPos - objectA.yPos) * (objectB.yPos - objectA.yPos)
                + (objectB.zPos - objectA.zPos) * (objectB.zPos - objectA.zPos));
    }

    @Override
    public void onRemoval() {

        ChargingHelper.removeEntry(getTileEntityPosition(), this);

        mWirelessChargingMap.clear();
        mLocalChargingMap.clear();
        if (!this.getBaseMetaTileEntity()
            .getWorld().playerEntities.isEmpty()) {
            for (EntityPlayer player : this.getBaseMetaTileEntity()
                .getWorld().playerEntities) {
                ChargingHelper.removeValidPlayer(player, this);
            }
        }

        super.onRemoval();
    }

    private int getLongRange(boolean mixed) {
        return (int) GTValues.V[this.mTier] * (mixed ? 2 : 4);
    }

    private int getLocalRange(boolean mixed) {
        return this.mTier * (mixed ? 10 : 20);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {

        if (this.mMode == MODE_MIXED) {
            PlayerUtils.messagePlayer(
                aPlayer,
                "Mixed Mode | Local: " + this.getLocalRange(true) + "m | Long: " + this.getLongRange(true) + "m");
            PlayerUtils.messagePlayer(aPlayer, "Players with access:");
            for (String name : this.getLocalMap()
                .keySet()) {
                PlayerUtils.messagePlayer(aPlayer, "Local: " + name);
            }
            for (String name : this.getLongRangeMap()
                .keySet()) {
                PlayerUtils.messagePlayer(aPlayer, "Long: " + name);
            }
        } else if (this.mMode == MODE_LOCAL) {
            PlayerUtils.messagePlayer(aPlayer, "Local Mode: " + this.getLocalRange(false) + "m");
            PlayerUtils.messagePlayer(aPlayer, "Players with access:");
            for (String name : this.getLocalMap()
                .keySet()) {
                PlayerUtils.messagePlayer(aPlayer, name);
            }

        } else {
            PlayerUtils.messagePlayer(aPlayer, "Long-range Mode: " + this.getLongRange(false) + "m");
            PlayerUtils.messagePlayer(aPlayer, "Players with access:");
            for (String name : this.getLongRangeMap()
                .keySet()) {
                PlayerUtils.messagePlayer(aPlayer, name);
            }
        }

        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public void onServerStart() {
        mWirelessChargingMap.clear();
        mLocalChargingMap.clear();
        super.onServerStart();
    }

    @Override
    public void onExplosion() {
        ChargingHelper.removeEntry(getTileEntityPosition(), this);
        super.onExplosion();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        ChargingHelper.removeEntry(getTileEntityPosition(), this);
        super.doExplosion(aExplosionPower);
    }
}
