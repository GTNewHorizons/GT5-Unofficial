package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_STEAM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.ISmartInputHatch;
import gtPlusPlus.core.lib.GTPPCore;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchCustomFluidBase extends MTEHatch implements ISmartInputHatch {

    public final Fluid mLockedFluid;
    public final int mFluidCapacity;
    protected FluidStack mLockedStack = null;
    protected String mTempMod = null;

    public MTEHatchCustomFluidBase(Fluid aFluid, int aAmount, final int aID, final String aName,
        final String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "Fluid Input for Multiblocks", "Capacity: " + formatNumber(aAmount) + "L" });
        this.mLockedFluid = aFluid;
        this.mFluidCapacity = aAmount;
    }

    public MTEHatchCustomFluidBase(Fluid aFluid, int aAmount, final String aName, final int aTier,
        final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
        this.mLockedFluid = aFluid;
        this.mFluidCapacity = aAmount;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        if (side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0) {
            FluidStack fs = GTUtility.getFluidForFilledItem(aStack, true);
            return fs != null && fs.getFluid() == this.mLockedFluid;
        }
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 1;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN),
                TextureFactory.of(FLUID_STEAM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN),
                TextureFactory.of(FLUID_STEAM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        // return true;
        return false;
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

    public void updateSlots() {
        if (mInventory[getInputSlot()] != null && mInventory[getInputSlot()].stackSize <= 0)
            mInventory[getInputSlot()] = null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if (aBaseMetaTileEntity.isServerSide()) {
            // Pushing on change lets a machine that stalled on running out of steam restart the instant steam returns.
            detectInventoryChange();
        }
    }

    @Override
    public int getCapacity() {
        return this.mFluidCapacity;
    }

    @Override
    public String[] getDescription() {
        if (mLockedStack == null && mLockedFluid != null) {
            mLockedStack = new FluidStack(mLockedFluid, 1);
        }
        int aFluidTemp = 0;
        if (mLockedFluid != null) {
            aFluidTemp = mLockedFluid.getTemperature();
            mTempMod = mLockedFluid.getName();
        }
        final boolean isSteam = mTempMod.equalsIgnoreCase("steam");

        EnumChatFormatting aColour = EnumChatFormatting.BLUE;
        if (aFluidTemp <= -3000) {
            aColour = EnumChatFormatting.DARK_PURPLE;
        } else if (aFluidTemp >= -2999 && aFluidTemp <= -500) {
            aColour = EnumChatFormatting.DARK_BLUE;
        } else if (aFluidTemp >= -499 && aFluidTemp <= -50) {
            aColour = EnumChatFormatting.BLUE;
        } else if (aFluidTemp >= 30 && aFluidTemp <= 300) {
            aColour = EnumChatFormatting.AQUA;
        } else if (aFluidTemp >= 301 && aFluidTemp <= 800) {
            aColour = EnumChatFormatting.YELLOW;
        } else if (aFluidTemp >= 801 && aFluidTemp <= 1500) {
            aColour = EnumChatFormatting.GOLD;
        } else if (aFluidTemp >= 1501) {
            aColour = EnumChatFormatting.RED;
        }
        final String fluid = aColour
            + (mLockedStack != null ? mLockedStack.getLocalizedName()
                : StatCollector.translateToLocal("gt.blockmachines.hatch.custom_fluid.empty"))
            + EnumChatFormatting.RESET;
        return ArrayUtils
            .addAll(
                GTSplit.splitLocalizedFormatted(
                    isSteam ? "gt.blockmachines.hatch.custom_fluid.steam.desc"
                        : "gt.blockmachines.hatch.custom_fluid.desc",
                    getCapacity(),
                    fluid),
                GTPPCore.GT_Tooltip.get());
    }

    @Override
    public boolean isFluidInputAllowed(final FluidStack aFluid) {
        return this.mLockedFluid.getName()
            .equals(
                aFluid.getFluid()
                    .getName());
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchCustomFluidBase(
            this.mLockedFluid,
            this.mFluidCapacity,
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures);
    }
}
