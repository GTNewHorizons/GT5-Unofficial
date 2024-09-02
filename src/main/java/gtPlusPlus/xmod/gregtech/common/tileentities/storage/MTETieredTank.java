package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;

public class MTETieredTank extends MTEBasicTank {

    public MTETieredTank(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            "Stores " + GTUtility.formatNumbers(((int) (Math.pow(2, aTier) * 32000))) + "L of fluid");
    }

    public MTETieredTank(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        List<String> description = new ArrayList<>(Arrays.asList(this.mDescriptionArray));
        description.add("A portable tank.");
        if (this.mFluid != null) {
            description.add("Fluid: " + mFluid.getLocalizedName() + " " + mFluid.amount + "L");
        }
        description.add(GTPPCore.GT_Tooltip.get());
        return description.toArray(new String[0]);
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return side == ForgeDirection.UP
            ? new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_TOP_FLUIDTANK) }
            : new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_FLUIDTANK) };
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("mFluid")) {
            final FluidStack tContents = FluidStack
                .loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("mFluid"));
            if (tContents != null && tContents.amount > 0) {
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "TileEntity_TANK_INFO",
                        "Contains Fluid: ",
                        !GregTechAPI.sPostloadFinished) + EnumChatFormatting.YELLOW
                        + tContents.getLocalizedName()
                        + EnumChatFormatting.GRAY);
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "TileEntity_TANK_AMOUNT",
                        "Fluid Amount: ",
                        !GregTechAPI.sPostloadFinished) + EnumChatFormatting.GREEN
                        + GTUtility.formatNumbers(tContents.amount)
                        + " L"
                        + EnumChatFormatting.GRAY);
            }
        }
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public final byte getUpdateData() {
        return 0x00;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public String[] getInfoData() {

        if (this.mFluid == null) {
            return new String[] { GTValues.VOLTAGE_NAMES[this.mTier] + " Fluid Tank", "Stored Fluid:", "No Fluid",
                0 + "L", this.getCapacity() + "L" };
        }
        return new String[] { GTValues.VOLTAGE_NAMES[this.mTier] + " Fluid Tank", "Stored Fluid:",
            this.mFluid.getLocalizedName(), this.mFluid.amount + "L", this.getCapacity() + "L" };
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTETieredTank(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return (int) (Math.pow(2, this.mTier) * 32000);
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (mFluid != null) {
            Logger.WARNING("Setting item fluid nbt");
            aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
            if (aNBT.hasKey("mFluid")) {
                Logger.WARNING("Set mFluid to NBT.");
            }
        }
    }
}
