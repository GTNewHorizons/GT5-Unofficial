package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_Cover_LiquidMeter
        extends GT_CoverBehavior {
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aTileEntity instanceof IFluidHandler)) {
            FluidTankInfo[] tTanks = ((IFluidHandler) aTileEntity).getTankInfo(ForgeDirection.UNKNOWN);
            long tMax = 0;
            long tUsed = 0;
            if (tTanks != null) {
                for (FluidTankInfo tTank : tTanks) {
                    if (tTank != null) {
                        tMax += tTank.capacity;
                        FluidStack tLiquid = tTank.fluid;
                        if (tLiquid != null) {
                            tUsed += tLiquid.amount;
                        }
                    }
                }
            }
            if(tUsed==0L)//nothing
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 15 : 0));
            else if(tUsed >= tMax)//full
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 0 : 15));
            else//1-14 range
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 14-((14*tUsed)/tMax) : 1+((14*tUsed)/tMax)) );
        } else {
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)0);
        }
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable == 0) {
            GT_Utility.sendChatToPlayer(aPlayer, trans("054", "Inverted"));
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, trans("055", "Normal"));
        }
        return aCoverVariable == 0 ? 1 : 0;
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }
}