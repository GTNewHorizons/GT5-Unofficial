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
import gregtech.api.util.ISerializableObject.LegacyCoverData;

public class CoverDrain extends CoverBehavior {

    public CoverDrain(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        if ((coverDataValue % 3 > 1) && ((coverable instanceof IMachineProgress))) {
            if (((IMachineProgress) coverable).isAllowedToWork()) {
                return coverData;
            }
        }
        if (coverSide != ForgeDirection.UNKNOWN) {
            final Block tBlock = coverable.getBlockAtSide(coverSide);
            if ((coverDataValue < 3) && ((coverable instanceof IFluidHandler))) {
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
                        tLiquid = Materials.Water.getFluid(1000L);
                    } else if (((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava))
                        && (coverable.getMetaIDAtSide(coverSide) == 0)) {
                            tLiquid = Materials.Lava.getFluid(1000L);
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
            if ((coverDataValue >= 3) && (tBlock != null)
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
        return LegacyCoverData.of(coverDataValue);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (coverDataValue < 0) {
            coverDataValue = 5;
        }
        switch (coverDataValue) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("022", "Import"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("023", "Import (conditional)"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("024", "Import (invert cond)"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("025", "Keep Liquids Away"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("026", "Keep Liquids Away (conditional)"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("027", "Keep Liquids Away (invert cond)"));
        }
        coverData.set(coverDataValue);
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return (coveredTile.get() instanceof IMachineProgress machine && machine.isAllowedToWork())
            == (coverData.get() < 2);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return coverData.get() < 3 ? 50 : 1;
    }
}
