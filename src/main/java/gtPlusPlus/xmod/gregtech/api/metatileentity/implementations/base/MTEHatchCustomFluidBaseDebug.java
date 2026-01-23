package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;

// An extension of MTEHatchCustomFluidBase, it caches 2G of the supplied fluid and refills its buffer every 30 seconds
public class MTEHatchCustomFluidBaseDebug extends MTEHatchCustomFluidBase {

    public MTEHatchCustomFluidBaseDebug(Fluid aFluid, int aID, String aName, String aNameRegional, int aTier) {
        super(aFluid, MAX_CAPACITY, aID, aName, aNameRegional, aTier);
    }

    public MTEHatchCustomFluidBaseDebug(Fluid aFluid, final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aFluid, MAX_CAPACITY, aName, aTier, aDescription, aTextures);

    }

    private static final int MAX_CAPACITY = 200000000;

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        if (baseMetaTileEntity.isServerSide()) refillFluid();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // refill entirely every 30 seconds
            if (aTick % (30 * SECONDS) == 0L) {
                refillFluid();
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    // sets the cached fluid stack to 2G of the locked fluid (passed in through constructor)
    private void refillFluid() {
        mFluid = new FluidStack(mLockedFluid, MAX_CAPACITY);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCustomFluidBaseDebug(
            this.mLockedFluid,
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public int getCapacity() {
        return MAX_CAPACITY;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public String[] getDescription() {
        if (mLockedStack == null && mLockedFluid != null) {
            mLockedStack = new FluidStack(mLockedFluid, 1);
        }
        int aFluidTemp = 0;
        boolean isSteam = false;
        if (mLockedFluid != null) {
            aFluidTemp = mLockedFluid.getTemperature();
            mTempMod = mLockedFluid.getName();
        }
        if (mLockedStack.isFluidEqual(Materials.Steam.getGas(1))) {
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
        String aFluidName = EnumChatFormatting.ITALIC + "Who needs this much "
            + aColour
            + EnumChatFormatting.ITALIC
            + (mLockedStack != null ? mLockedStack.getLocalizedName() : "nothing :( ")
            + EnumChatFormatting.RESET
            + EnumChatFormatting.ITALIC
            + "???";
        return new String[] { "Debug Fluid Input for " + (isSteam ? "Steam " : "") + "Multiblocks",
            "Replenishes itself every 30 seconds",
            "Capacity: " + EnumChatFormatting.WHITE + formatNumber(getCapacity()) + "L", aFluidName,
            "Author: " + GREEN + BOLD + "Chrom" };
    }

}
