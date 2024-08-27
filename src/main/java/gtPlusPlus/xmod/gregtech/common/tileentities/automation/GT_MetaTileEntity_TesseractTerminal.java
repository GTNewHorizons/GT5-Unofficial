package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.tesseract.TesseractHelper;

public class GT_MetaTileEntity_TesseractTerminal extends GT_MetaTileEntity_BasicTank {

    public int mFrequency = 0;
    public UUID mOwner;
    public boolean mDidWork = false;
    public static boolean sInterDimensionalTesseractAllowed = true;
    private static int TESSERACT_ENERGY_COST = 128;
    private static int TESSERACT_ENERGY_COST_DIMENSIONAL = 512;

    public GT_MetaTileEntity_TesseractTerminal(final int aID, final String aName, final String aNameRegional,
        final int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, "");
    }

    public GT_MetaTileEntity_TesseractTerminal(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TesseractTerminal(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isTransformerUpgradable() {
        return false;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getBackFacing();
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return (this.getBaseMetaTileEntity()
            .getEUCapacity() / 100);
    }

    @Override
    public long maxEUInput() {
        return TESSERACT_ENERGY_COST_DIMENSIONAL;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public long maxEUStore() {
        return TESSERACT_ENERGY_COST_DIMENSIONAL * 8 * 32;
    }

    @Override
    public long maxSteamStore() {
        return this.maxEUStore();
    }

    @Override
    public boolean ownerControl() {
        return true;
    }

    @Override
    public int getProgresstime() {
        return this.getTesseract(this.mFrequency, false) != null ? 999 : 0;
    }

    @Override
    public int maxProgresstime() {
        return 1000;
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setInteger("mFrequency", this.mFrequency);
        aNBT.setString("mOwner", mOwner.toString());
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.mFrequency = aNBT.getInteger("mFrequency");
        this.mOwner = UUID.fromString(aNBT.getString("mOnwer"));
    }

    @Override
    public void onConfigLoad() {
        sInterDimensionalTesseractAllowed = true;
        TESSERACT_ENERGY_COST = 512;
        TESSERACT_ENERGY_COST_DIMENSIONAL = 2048;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer,
        final ForgeDirection side, final float aX, final float aY, final float aZ) {

        if (this.mOwner == null) {
            if (this.getBaseMetaTileEntity()
                .getOwnerName() != null
                && !this.getBaseMetaTileEntity()
                    .getOwnerName()
                    .equals("")) {
                if (this.getBaseMetaTileEntity()
                    .getOwnerName()
                    .toLowerCase()
                    .equals(
                        aPlayer.getDisplayName()
                            .toLowerCase())) {
                    this.mOwner = PlayerUtils.getPlayersUUIDByName(
                        this.getBaseMetaTileEntity()
                            .getOwnerName());
                }
            }
        }

        if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) == 0) {
            if (side == this.getBaseMetaTileEntity()
                .getFrontFacing()) {
                final float[] tCoords = GT_Utility.getClickedFacingCoords(side, aX, aY, aZ);
                switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + (2 * (byte) (int) (tCoords[1] * 2.0F)))) {
                    case 0:
                        // Utils.LOG_WARNING("Freq. -1 | " + this.mFrequency);
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency -= 1;
                        break;
                    case 1:
                        // Utils.LOG_WARNING("Freq. +1 | " + this.mFrequency);
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency += 1;
                    default:
                        // Utils.LOG_WARNING("Did not click the correct place.");
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        break;
                }
                PlayerUtils.messagePlayer(aPlayer, "Frequency: " + this.mFrequency);
                if (this.getTesseract(this.mFrequency, false) != null) {
                    PlayerUtils.messagePlayer(
                        aPlayer,
                        new StringBuilder().append(EnumChatFormatting.GREEN)
                            .append(" (Connected)")
                            .toString());
                }
            }
        } else if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) != 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "This is not your Tesseract Terminal to configure.");
            }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(final ForgeDirection side, final EntityPlayer aPlayer, final float aX,
        final float aY, final float aZ) {
        if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) == 0) {
            if (side == this.getBaseMetaTileEntity()
                .getFrontFacing()) {
                final float[] tCoords = GT_Utility.getClickedFacingCoords(side, aX, aY, aZ);
                switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + (2 * (byte) (int) (tCoords[1] * 2.0F)))) {
                    case 0 -> {
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency -= 64;
                    }
                    case 1 -> {
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency += 64;
                    }
                    case 2 -> {
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency -= 512;
                    }
                    case 3 -> {
                        try {
                            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Throwable t) {}
                        this.mFrequency += 512;
                    }
                }
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    "Frequency: " + this.mFrequency
                        + (this.getTesseract(this.mFrequency, false) == null ? ""
                            : new StringBuilder().append(EnumChatFormatting.GREEN)
                                .append(" (Connected)")
                                .toString()));
            }
        } else if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) != 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "This is not your Tesseract Terminal to configure.");
            }
    }

    public boolean allowCoverOnSide(final ForgeDirection side, final int aCoverID) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    public GT_MetaTileEntity_TesseractGenerator getTesseract(final int aFrequency, final boolean aWorkIrrelevant) {
        final GT_MetaTileEntity_TesseractGenerator rTesseract = TesseractHelper
            .getGeneratorByFrequency(PlayerUtils.getPlayerOnServerFromUUID(mOwner), aFrequency);
        if (rTesseract == null) {
            return null;
        }
        if (!TesseractHelper.isGeneratorOwnedByPlayer(PlayerUtils.getPlayerOnServerFromUUID(mOwner), rTesseract)) {
            return null;
        }
        if (rTesseract.mFrequency != aFrequency) {
            TesseractHelper.setTerminalOwnershipByPlayer(
                PlayerUtils.getPlayerOnServerFromUUID(mOwner),
                Integer.valueOf(aFrequency),
                null);
            return null;
        }
        if (!rTesseract.isValidTesseractGenerator(
            this.getBaseMetaTileEntity()
                .getOwnerName(),
            aWorkIrrelevant)) {
            return null;
        }
        if ((!sInterDimensionalTesseractAllowed) && (rTesseract.getBaseMetaTileEntity()
            .getWorld()
            != this.getBaseMetaTileEntity()
                .getWorld())) {
            return null;
        }
        return rTesseract;
    }

    @Override
    public String[] getInfoData() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity != null) && (this.getBaseMetaTileEntity()
            .isAllowedToWork()) && (tTileEntity.isSendingInformation())) {
            return tTileEntity.getInfoData();
        }
        return new String[] { "Tesseract Generator", "Freqency:", "" + this.mFrequency,
            this.getTesseract(this.mFrequency, false) != null ? "Active" : "Inactive" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean isDigitalChest() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.isDigitalChest();
    }

    @Override
    public ItemStack[] getStoredItemData() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.getStoredItemData();
    }

    @Override
    public void setItemCount(final int aCount) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return;
        }
        tTileEntity.setItemCount(aCount);
    }

    @Override
    public int getMaxItemCount() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getMaxItemCount();
    }

    @Override
    public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.isItemValidForSlot(aIndex, aStack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int ordinalSide) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return new int[0];
        }
        return tTileEntity.getAccessibleSlotsFromSide(ordinalSide);
    }

    @Override
    public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int ordinalSide) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int ordinalSide) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canExtractItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public int getSizeInventory() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(final int aIndex) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.getStackInSlot(aIndex);
    }

    @Override
    public void setInventorySlotContents(final int aIndex, final ItemStack aStack) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return;
        }
        tTileEntity.setInventorySlotContents(aIndex, aStack);
    }

    @Override
    public ItemStack decrStackSize(final int aIndex, final int aAmount) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.decrStackSize(aIndex, aAmount);
    }

    @Override
    public String getInventoryName() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return "";
        }
        return tTileEntity.getInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getInventoryStackLimit();
    }

    @Override
    public boolean canFill(final ForgeDirection aSide, final Fluid aFluid) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canFill(aSide, aFluid);
    }

    @Override
    public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canDrain(aSide, aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection aSide) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return new FluidTankInfo[0];
        }
        return tTileEntity.getTankInfo(aSide);
    }

    @Override
    public int fill_default(final ForgeDirection aDirection, final FluidStack aFluid, final boolean doFill) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.fill(aDirection, aFluid, doFill);
    }

    @Override
    public FluidStack drain(final ForgeDirection aDirection, final int maxDrain, final boolean doDrain) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.drain(aDirection, maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain) {
        final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.drain(aSide, aFluid, doDrain);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((this.getBaseMetaTileEntity()
            .isServerSide())
            && (this.getBaseMetaTileEntity()
                .isAllowedToWork())) {
            // Set owner
            if (PlayerUtils.getPlayersUUIDByName(
                this.getBaseMetaTileEntity()
                    .getOwnerName())
                != null) {
                if (this.mOwner == null) {
                    this.mOwner = PlayerUtils.getPlayersUUIDByName(
                        this.getBaseMetaTileEntity()
                            .getOwnerName());
                }
            }
            final GT_MetaTileEntity_TesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, true);
            if (tTileEntity != null) {
                tTileEntity.addEnergyConsumption(this);
                if ((!this.mDidWork) && (this.getTesseract(this.mFrequency, false) != null)) {
                    this.mDidWork = true;
                    this.getBaseMetaTileEntity()
                        .issueBlockUpdate();
                    this.getBaseMetaTileEntity()
                        .decreaseStoredEnergyUnits(128, false);
                }
            } else if (this.mDidWork == true) {
                this.mDidWork = false;
                this.getBaseMetaTileEntity()
                    .issueBlockUpdate();
            }
        }
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Accesses Tesseract Generators remotely",
            "Connect with pipes to extract items or fluids",
            "Outputs from the back face",
            "Consumes " + TESSERACT_ENERGY_COST + "EU/t for same dimension transfers",
            "Consumes " + TESSERACT_ENERGY_COST_DIMENSIONAL + "EU/t for cross dimensional transfers",
            CORE.GT_Tooltip.get());
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
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return side == facing
            ? new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional),
                new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_Frequency) }
            : new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Dimensional),
                new GT_RenderedTexture(Textures.BlockIcons.VOID) };
    }

    // To-Do?
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
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (this.getBaseMetaTileEntity()
            .getOwnerName() != null
            && !this.getBaseMetaTileEntity()
                .getOwnerName()
                .equals("")) {
            this.mOwner = PlayerUtils.getPlayersUUIDByName(
                this.getBaseMetaTileEntity()
                    .getOwnerName());
        }
        super.onCreated(aStack, aWorld, aPlayer);
    }

    @Override
    public void onRemoval() {
        try {
            CORE.sTesseractTerminalOwnershipMap.get(mOwner)
                .remove(this.mFrequency);
        } catch (Throwable t) {}
        super.onRemoval();
    }
}
