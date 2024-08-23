package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class GT_MetaTileEntity_Hatch_CustomFluidBase extends GT_MetaTileEntity_Hatch {

    public final Fluid mLockedFluid;
    public final int mFluidCapacity;
    protected FluidStack mLockedStack = null;
    protected String mTempMod = null;

    public GT_MetaTileEntity_Hatch_CustomFluidBase(Fluid aFluid, int aAmount, final int aID, final String aName,
        final String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "Fluid Input for Multiblocks", "Capacity: " + GT_Utility.formatNumbers(aAmount) + "L" });
        this.mLockedFluid = aFluid;
        this.mFluidCapacity = aAmount;
    }

    public GT_MetaTileEntity_Hatch_CustomFluidBase(Fluid aFluid, int aAmount, final String aName, final int aTier,
        final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription[0], aTextures);
        this.mLockedFluid = aFluid;
        this.mFluidCapacity = aAmount;
    }

    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        if (side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0) {
            FluidStack fs = GT_Utility.getFluidForFilledItem(aStack, true);
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
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(FLUID_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(FLUID_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    public void updateSlots() {
        if (mInventory[getInputSlot()] != null && mInventory[getInputSlot()].stackSize <= 0)
            mInventory[getInputSlot()] = null;
    }

    @Override
    public int getTankPressure() {
        return -100;
    }

    @Override
    public int getCapacity() {
        return this.mFluidCapacity;
    }

    @Override
    public String[] getDescription() {
        if (mLockedStack == null) {
            mLockedStack = FluidUtils.getFluidStack(mLockedFluid, 1);
        }
        int aFluidTemp = 0;
        boolean isSteam = false;
        if (mLockedFluid != null) {
            aFluidTemp = mLockedFluid.getTemperature();
            mTempMod = mLockedFluid.getName();
        }
        if (mTempMod.equalsIgnoreCase("steam")) {
            isSteam = true;
        }

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
        String aFluidName = "Accepted Fluid: " + aColour
            + (mLockedStack != null ? mLockedStack.getLocalizedName() : "Empty")
            + EnumChatFormatting.RESET;
        return new String[] { "Fluid Input for " + (isSteam ? "Steam " : "") + "Multiblocks",
            "Capacity: " + getCapacity() + "L", aFluidName, CORE.GT_Tooltip.get() };
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
        return new GT_MetaTileEntity_Hatch_CustomFluidBase(
            this.mLockedFluid,
            this.mFluidCapacity,
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    protected FluidSlotWidget createFluidSlot() {
        return super.createFluidSlot().setFilter(f -> f == mLockedFluid);
    }
}
