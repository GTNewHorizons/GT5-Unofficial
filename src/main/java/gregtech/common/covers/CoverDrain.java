package gregtech.common.covers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;

public class CoverDrain extends CoverBehavior {

    public CoverDrain(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if ((aCoverVariable % 3 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork()) {
                return aCoverVariable;
            }
        }
        if (side != ForgeDirection.UNKNOWN) {
            final Block tBlock = aTileEntity.getBlockAtSide(side);
            if ((aCoverVariable < 3) && ((aTileEntity instanceof IFluidHandler))) {
                if ((side == ForgeDirection.UP) && (aTileEntity.getWorld()
                    .isRaining())
                    && (aTileEntity.getWorld()
                        .getPrecipitationHeight(aTileEntity.getXCoord(), aTileEntity.getZCoord()) - 2
                        < aTileEntity.getYCoord())) {
                    int tAmount = (int) (aTileEntity.getBiome().rainfall * 10.0F);
                    if (tAmount > 0) {
                        ((IFluidHandler) aTileEntity).fill(
                            side,
                            Materials.Water.getFluid(
                                aTileEntity.getWorld()
                                    .isThundering() ? tAmount * 2L : tAmount),
                            true);
                    }
                }
                FluidStack tLiquid = null;
                if (tBlock != null) {
                    if (((tBlock == Blocks.water) || (tBlock == Blocks.flowing_water))
                        && (aTileEntity.getMetaIDAtSide(side) == 0)) {
                        tLiquid = Materials.Water.getFluid(1000L);
                    } else if (((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava))
                        && (aTileEntity.getMetaIDAtSide(side) == 0)) {
                            tLiquid = Materials.Lava.getFluid(1000L);
                        } else if ((tBlock instanceof IFluidBlock)) {
                            tLiquid = ((IFluidBlock) tBlock).drain(
                                aTileEntity.getWorld(),
                                aTileEntity.getOffsetX(side, 1),
                                aTileEntity.getOffsetY(side, 1),
                                aTileEntity.getOffsetZ(side, 1),
                                false);
                        }
                    if ((tLiquid != null) && (tLiquid.getFluid() != null)
                        && ((side.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 // Horizontal
                            || ((side == ForgeDirection.DOWN) && (tLiquid.getFluid()
                                .getDensity() <= 0))
                            || ((side == ForgeDirection.UP) && (tLiquid.getFluid()
                                .getDensity() >= 0)))
                        && (((IFluidHandler) aTileEntity).fill(side, tLiquid, false) == tLiquid.amount)) {
                        ((IFluidHandler) aTileEntity).fill(side, tLiquid, true);
                        aTileEntity.getWorld()
                            .setBlockToAir(
                                aTileEntity.getXCoord() + side.offsetX,
                                aTileEntity.getYCoord() + side.offsetY,
                                aTileEntity.getZCoord() + side.offsetZ);
                    }
                }
            }
            if ((aCoverVariable >= 3) && (tBlock != null)
                && ((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava)
                    || (tBlock == Blocks.water)
                    || (tBlock == Blocks.flowing_water)
                    || ((tBlock instanceof IFluidBlock)))) {
                aTileEntity.getWorld()
                    .setBlock(
                        aTileEntity.getOffsetX(side, 1),
                        aTileEntity.getOffsetY(side, 1),
                        aTileEntity.getOffsetZ(side, 1),
                        Blocks.air,
                        0,
                        0);
            }
        }
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (aCoverVariable < 0) {
            aCoverVariable = 5;
        }
        switch (aCoverVariable) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("022", "Import"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("023", "Import (conditional)"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("024", "Import (invert cond)"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("025", "Keep Liquids Away"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("026", "Keep Liquids Away (conditional)"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("027", "Keep Liquids Away (invert cond)"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return ((IMachineProgress) aTileEntity).isAllowedToWork() == aCoverVariable < 2;
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable < 3 ? 50 : 1;
    }
}
