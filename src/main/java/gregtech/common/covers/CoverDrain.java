package gregtech.common.covers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.covers.CoverContext;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;

public class CoverDrain extends CoverLegacyData {

    public CoverDrain(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if ((this.coverData % 3 > 1) && ((coverable instanceof IMachineProgress))) {
            if (((IMachineProgress) coverable).isAllowedToWork()) {
                return;
            }
        }
        if (coverSide != ForgeDirection.UNKNOWN) {
            final Block tBlock = coverable.getBlockAtSide(coverSide);
            if ((this.coverData < 3) && ((coverable instanceof IFluidHandler))) {
                if ((coverSide == ForgeDirection.UP) && (coverable.getWorld()
                    .isRaining())
                    && (coverable.getWorld()
                        .getPrecipitationHeight(coverable.getXCoord(), coverable.getZCoord()) - 2
                        < coverable.getYCoord())) {
                    int tAmount = (int) (coverable.getBiome().rainfall * 10.0F);
                    if (tAmount > 0) {
                        ((IFluidHandler) coverable).fill(
                            coverSide,
                            Materials.Water.getFluid(
                                coverable.getWorld()
                                    .isThundering() ? tAmount * 2L : tAmount),
                            true);
                    }
                }
                FluidStack tLiquid = null;
                if (tBlock != null) {
                    if (((tBlock == Blocks.water) || (tBlock == Blocks.flowing_water))
                        && (coverable.getMetaIDAtSide(coverSide) == 0)) {
                        tLiquid = Materials.Water.getFluid(1_000);
                    } else if (((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava))
                        && (coverable.getMetaIDAtSide(coverSide) == 0)) {
                            tLiquid = Materials.Lava.getFluid(1_000);
                        } else if ((tBlock instanceof IFluidBlock)) {
                            tLiquid = ((IFluidBlock) tBlock).drain(
                                coverable.getWorld(),
                                coverable.getOffsetX(coverSide, 1),
                                coverable.getOffsetY(coverSide, 1),
                                coverable.getOffsetZ(coverSide, 1),
                                false);
                        }
                    if ((tLiquid != null) && (tLiquid.getFluid() != null)
                        && ((coverSide.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 // Horizontal
                            || ((coverSide == ForgeDirection.DOWN) && (tLiquid.getFluid()
                                .getDensity() <= 0))
                            || ((coverSide == ForgeDirection.UP) && (tLiquid.getFluid()
                                .getDensity() >= 0)))
                        && (((IFluidHandler) coverable).fill(coverSide, tLiquid, false) == tLiquid.amount)) {
                        ((IFluidHandler) coverable).fill(coverSide, tLiquid, true);
                        coverable.getWorld()
                            .setBlockToAir(
                                coverable.getXCoord() + coverSide.offsetX,
                                coverable.getYCoord() + coverSide.offsetY,
                                coverable.getZCoord() + coverSide.offsetZ);
                    }
                }
            }
            if ((this.coverData >= 3) && (tBlock != null)
                && ((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava)
                    || (tBlock == Blocks.water)
                    || (tBlock == Blocks.flowing_water)
                    || ((tBlock instanceof IFluidBlock)))) {
                coverable.getWorld()
                    .setBlock(
                        coverable.getOffsetX(coverSide, 1),
                        coverable.getOffsetY(coverSide, 1),
                        coverable.getOffsetZ(coverSide, 1),
                        Blocks.air,
                        0,
                        0);
            }
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (this.coverData < 0) {
            this.coverData = 5;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import");
            case 1 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_cond");
            case 2 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_invert_cond");
            case 3 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.no_fluid");
            case 4 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.no_fluid_cond");
            case 5 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.no_fluid_invert_cond");
        }
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return (coveredTile.get() instanceof IMachineProgress machine && machine.isAllowedToWork()) == (coverData < 2);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return coverData < 3 ? 50 : 1;
    }
}
