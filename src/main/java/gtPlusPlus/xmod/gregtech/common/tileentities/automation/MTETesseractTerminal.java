package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.tesseract.TesseractHelper;

public class MTETesseractTerminal extends MTEBasicTank {

    public int mFrequency = 0;
    public UUID mOwner;
    public boolean mDidWork = false;
    public static boolean sInterDimensionalTesseractAllowed = true;
    private static int TESSERACT_ENERGY_COST = 128;
    private static int TESSERACT_ENERGY_COST_DIMENSIONAL = 512;

    public MTETesseractTerminal(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, "");
    }

    public MTETesseractTerminal(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTETesseractTerminal(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
    public long maxEUStore() {
        return (long) TESSERACT_ENERGY_COST_DIMENSIONAL * 8 * 32;
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
                    .isEmpty()) {
                if (this.getBaseMetaTileEntity()
                    .getOwnerName()
                    .equalsIgnoreCase(aPlayer.getDisplayName())) {
                    this.mOwner = GTMod.proxy.getPlayersUUID(
                        this.getBaseMetaTileEntity()
                            .getOwnerName());
                }
            }
        }

        if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) == 0) {
            if (side == this.getBaseMetaTileEntity()
                .getFrontFacing()) {
                final float[] tCoords = GTUtility.getClickedFacingCoords(side, aX, aY, aZ);
                switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + (2 * (byte) (int) (tCoords[1] * 2.0F)))) {
                    case 0:
                        // Utils.LOG_WARNING("Freq. -1 | " + this.mFrequency);
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency -= 1;
                        break;
                    case 1:
                        // Utils.LOG_WARNING("Freq. +1 | " + this.mFrequency);
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency += 1;
                    default:
                        // Utils.LOG_WARNING("Did not click the correct place.");
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        break;
                }
                GTUtility.sendChatToPlayer(aPlayer, "Frequency: " + this.mFrequency);
                if (this.getTesseract(this.mFrequency, false) != null) {
                    GTUtility.sendChatToPlayer(aPlayer, EnumChatFormatting.GREEN + " (Connected)");
                }
            }
        } else if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) != 0) {
                GTUtility.sendChatToPlayer(aPlayer, "This is not your Tesseract Terminal to configure.");
            }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(final ForgeDirection side, final EntityPlayer aPlayer, final float aX,
        final float aY, final float aZ, ItemStack aTool) {
        if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) == 0) {
            if (side == this.getBaseMetaTileEntity()
                .getFrontFacing()) {
                final float[] tCoords = GTUtility.getClickedFacingCoords(side, aX, aY, aZ);
                switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + (2 * (byte) (int) (tCoords[1] * 2.0F)))) {
                    case 0 -> {
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency -= 64;
                    }
                    case 1 -> {
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency += 64;
                    }
                    case 2 -> {
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency -= 512;
                    }
                    case 3 -> {
                        try {
                            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                                .remove(this.mFrequency);
                        } catch (Exception t) {}
                        this.mFrequency += 512;
                    }
                }
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    "Frequency: " + this.mFrequency
                        + (this.getTesseract(this.mFrequency, false) == null ? ""
                            : EnumChatFormatting.GREEN + " (Connected)"));
            }
        } else if (aPlayer.getUniqueID()
            .compareTo(this.mOwner) != 0) {
                GTUtility.sendChatToPlayer(aPlayer, "This is not your Tesseract Terminal to configure.");
            }
    }

    public boolean allowCoverOnSide(final ForgeDirection side, final int aCoverID) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    public MTETesseractGenerator getTesseract(final int aFrequency, final boolean aWorkIrrelevant) {
        final MTETesseractGenerator rTesseract = TesseractHelper
            .getGeneratorByFrequency(GTMod.proxy.getPlayerMP(mOwner), aFrequency);
        if (rTesseract == null) {
            return null;
        }
        if (!TesseractHelper.isGeneratorOwnedByPlayer(GTMod.proxy.getPlayerMP(mOwner), rTesseract)) {
            return null;
        }
        if (rTesseract.mFrequency != aFrequency) {
            TesseractHelper.setTerminalOwnershipByPlayer(GTMod.proxy.getPlayerMP(mOwner), aFrequency, null);
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
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity != null) && (this.getBaseMetaTileEntity()
            .isAllowedToWork()) && (tTileEntity.isSendingInformation())) {
            return tTileEntity.getInfoData();
        }
        return new String[] { StatCollector.translateToLocal("gtpp.infodata.tesseract_generator.name"),
            StatCollector.translateToLocalFormatted("gtpp.infodata.tesseract_generator.frequency", this.mFrequency),
            this.getTesseract(this.mFrequency, false) != null
                ? StatCollector.translateToLocal("gtpp.infodata.tesseract_generator.status.active")
                : StatCollector.translateToLocal("gtpp.infodata.tesseract_generator.status.inactive") };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean isDigitalChest() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.isDigitalChest();
    }

    @Override
    public ItemStack[] getStoredItemData() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.getStoredItemData();
    }

    @Override
    public void setItemCount(final int aCount) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return;
        }
        tTileEntity.setItemCount(aCount);
    }

    @Override
    public int getMaxItemCount() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getMaxItemCount();
    }

    @Override
    public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.isItemValidForSlot(aIndex, aStack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int ordinalSide) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return GTValues.emptyIntArray;
        }
        return tTileEntity.getAccessibleSlotsFromSide(ordinalSide);
    }

    @Override
    public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int ordinalSide) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int ordinalSide) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canExtractItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public int getSizeInventory() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(final int aIndex) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.getStackInSlot(aIndex);
    }

    @Override
    public void setInventorySlotContents(final int aIndex, final ItemStack aStack) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return;
        }
        tTileEntity.setInventorySlotContents(aIndex, aStack);
    }

    @Override
    public ItemStack decrStackSize(final int aIndex, final int aAmount) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.decrStackSize(aIndex, aAmount);
    }

    @Override
    public String getInventoryName() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return "";
        }
        return tTileEntity.getInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.getInventoryStackLimit();
    }

    @Override
    public boolean canFill(final ForgeDirection aSide, final Fluid aFluid) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canFill(aSide, aFluid);
    }

    @Override
    public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return false;
        }
        return tTileEntity.canDrain(aSide, aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection aSide) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return GTValues.emptyFluidTankInfo;
        }
        return tTileEntity.getTankInfo(aSide);
    }

    @Override
    public int fill(final ForgeDirection aDirection, final FluidStack aFluid, final boolean doFill) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return 0;
        }
        return tTileEntity.fill(aDirection, aFluid, doFill);
    }

    @Override
    public FluidStack drain(final ForgeDirection aDirection, final int maxDrain, final boolean doDrain) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
        if ((tTileEntity == null) || (!this.getBaseMetaTileEntity()
            .isAllowedToWork())) {
            return null;
        }
        return tTileEntity.drain(aDirection, maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain) {
        final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, false);
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
            if (GTMod.proxy.getPlayersUUID(
                this.getBaseMetaTileEntity()
                    .getOwnerName())
                != null) {
                if (this.mOwner == null) {
                    this.mOwner = GTMod.proxy.getPlayersUUID(
                        this.getBaseMetaTileEntity()
                            .getOwnerName());
                }
            }
            final MTETesseractGenerator tTileEntity = this.getTesseract(this.mFrequency, true);
            if (tTileEntity != null) {
                tTileEntity.addEnergyConsumption(this);
                if ((!this.mDidWork) && (this.getTesseract(this.mFrequency, false) != null)) {
                    this.mDidWork = true;
                    this.getBaseMetaTileEntity()
                        .issueBlockUpdate();
                    this.getBaseMetaTileEntity()
                        .decreaseStoredEnergyUnits(128, false);
                }
            } else if (this.mDidWork) {
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
            GTPPCore.GT_Tooltip.get());
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
            ? new ITexture[] { TextureFactory.of(TexturesGtBlock.Casing_Machine_Dimensional),
                TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_Frequency) }
            : new ITexture[] { TextureFactory.of(TexturesGtBlock.Casing_Machine_Dimensional),
                TextureFactory.of(Textures.GlobalIcons.VOID) };
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
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (this.getBaseMetaTileEntity()
            .getOwnerName() != null
            && !this.getBaseMetaTileEntity()
                .getOwnerName()
                .isEmpty()) {
            this.mOwner = GTMod.proxy.getPlayersUUID(
                this.getBaseMetaTileEntity()
                    .getOwnerName());
        }
        super.onCreated(aStack, aWorld, aPlayer);
    }

    @Override
    public void onRemoval() {
        try {
            GTPPCore.sTesseractTerminalOwnershipMap.get(mOwner)
                .remove(this.mFrequency);
        } catch (Exception t) {}
        super.onRemoval();
    }
}
