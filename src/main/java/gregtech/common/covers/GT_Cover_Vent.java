package gregtech.common.covers;

import static gregtech.api.enums.GT_Values.SIDE_UNKNOWN;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;

public class GT_Cover_Vent extends GT_CoverBehavior {

    private final int mEfficiency;
    private final Fluid IC2_HOT_COOLANT = FluidRegistry.getFluid("ic2hotcoolant");
    private final Fluid IC2_COOLANT = FluidRegistry.getFluid("ic2coolant");

    public GT_Cover_Vent(int aEfficiency) {
        this.mEfficiency = aEfficiency;
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            long aTimer) {
        if (aSide == SIDE_UNKNOWN) return 0;
        int ret = 0;
        if (aTileEntity instanceof IFluidHandler) {
            ret = doCoolFluid(aSide, aTileEntity);
        }
        if ((aTileEntity instanceof IMachineProgress)) {
            ret = doProgressEfficiency(aSide, (IMachineProgress) aTileEntity, aCoverID);
        }
        return ret;
    }

    @Override
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 100;
    }

    protected int doProgressEfficiency(final byte aSide, final IMachineProgress aTileEntity, final int aCoverVariable) {
        final int offsetX = aTileEntity.getOffsetX(aSide, 1);
        final int offsetY = aTileEntity.getOffsetY(aSide, 1);
        final int offsetZ = aTileEntity.getOffsetZ(aSide, 1);
        final World world = aTileEntity.getWorld();
        if (aTileEntity.hasThingsToDo() && aCoverVariable != aTileEntity.getProgress()
                && !GT_Utility.hasBlockHitBox(world, offsetX, offsetY, offsetZ)) {
            aTileEntity.increaseProgress(this.mEfficiency);
        }
        return aTileEntity.getProgress();
    }

    protected int doCoolFluid(final byte aSide, final ICoverable aTileEntity) {
        final int offsetX = aTileEntity.getOffsetX(aSide, 1);
        final int offsetY = aTileEntity.getOffsetY(aSide, 1);
        final int offsetZ = aTileEntity.getOffsetZ(aSide, 1);
        final World world = aTileEntity.getWorld();
        final IFluidHandler fluidHandler = (IFluidHandler) aTileEntity;
        if (!fluidHandler.canDrain(ForgeDirection.UNKNOWN, IC2_HOT_COOLANT)) {
            return 0;
        }
        final int chances; // 10000 = 100%
        final Block blockAtSide = aTileEntity.getBlockAtSide(aSide);
        if (blockAtSide == null) {
            return 0;
        }
        if (blockAtSide == Blocks.water || blockAtSide == Blocks.flowing_water) {
            chances = switch (mEfficiency) {
                case 3 -> 10000; // 100% chances for Diamond reactor vent with water
                case 2 -> 5000; // 50% chances for Gold and Overclocked reactor vents with water
                default -> 2500; // 25% chances for Basic reactor vent with water
            };
        } else if (blockAtSide.isAir(world, offsetX, offsetY, offsetZ)) {
            switch (mEfficiency) {
                case 3 -> chances = 2500; // 25% chances for Diamond reactor vent with air
                case 2 -> chances = 1250; // 12.5% chances for Gold and Overclocked reactor vents with air
                default -> {
                    return 0; // Basic reactor vent cannot be used with air
                }
            }
        } else {
            return 0; // Vent cover need water or air
        }
        if (chances > XSTR_INSTANCE.nextInt(10000)) {
            final FluidStack hotFluidStack = fluidHandler.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true);
            final FluidStack coldFluidStack = new FluidStack(IC2_COOLANT, hotFluidStack.amount);
            fluidHandler.fill(ForgeDirection.UNKNOWN, coldFluidStack, true);
            return hotFluidStack.amount;
        }
        return 0;
    }
}
