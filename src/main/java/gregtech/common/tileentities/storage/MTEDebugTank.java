package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK_GLOW;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;

public class MTEDebugTank extends MTEQuantumTank {

    public MTEDebugTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEDebugTank(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(translateToLocal("GT5U.machines.digitaltank.debugtooltip"));
        super.addAdditionalTooltipInformation(stack, tooltip);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugTank(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            FluidStack drainableStack = getDrainableStack();
            if (drainableStack == null) return;
            drainableStack.amount = Integer.MAX_VALUE;
            if (mOutputFluid && (aTick % 20 == 0)) {
                IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
                if (tTank != null) {
                    FluidStack tDrained = drain(Integer.MAX_VALUE, false);
                    if (tDrained != null) {
                        int tFilledAmount = tTank.fill(aBaseMetaTileEntity.getBackFacing(), tDrained, false);
                        if (tFilledAmount > 0)
                            tTank.fill(aBaseMetaTileEntity.getBackFacing(), drain(tFilledAmount, true), true);
                    }
                }
            }
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != ForgeDirection.UP) {
            if (sideDirection == baseMetaTileEntity.getFrontFacing()) {
                return new ITexture[] { MACHINE_CASINGS[14][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE) };
            } else return new ITexture[] { MACHINE_CASINGS[14][colorIndex + 1] };
        }
        return new ITexture[] { MACHINE_CASINGS[14][colorIndex + 1], TextureFactory.of(OVERLAY_QTANK),
            TextureFactory.builder()
                .addIcon(OVERLAY_QTANK_GLOW)
                .glow()
                .build() };
    }

    @Override
    public int getRealCapacity() {
        return Integer.MAX_VALUE;
    }
}
