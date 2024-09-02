package gregtech.common.covers;

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
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;

public class CoverVent extends CoverBehavior {

    private final int mEfficiency;
    private final Fluid IC2_HOT_COOLANT = FluidRegistry.getFluid("ic2hotcoolant");
    private final Fluid IC2_COOLANT = FluidRegistry.getFluid("ic2coolant");

    public CoverVent(int aEfficiency) {
        this.mEfficiency = aEfficiency;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (side == ForgeDirection.UNKNOWN) return 0;
        int ret = 0;
        if (aTileEntity instanceof IFluidHandler) {
            ret = doCoolFluid(side, aTileEntity);
        }
        if ((aTileEntity instanceof IMachineProgress)) {
            ret = doProgressEfficiency(side, (IMachineProgress) aTileEntity, aCoverID);
        }
        return ret;
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 100;
    }

    protected int doProgressEfficiency(final ForgeDirection side, final IMachineProgress aTileEntity,
        final int aCoverVariable) {
        final int offsetX = aTileEntity.getOffsetX(side, 1);
        final int offsetY = aTileEntity.getOffsetY(side, 1);
        final int offsetZ = aTileEntity.getOffsetZ(side, 1);
        final World world = aTileEntity.getWorld();
        if (aTileEntity.hasThingsToDo() && aCoverVariable != aTileEntity.getProgress()
            && !GTUtility.hasBlockHitBox(world, offsetX, offsetY, offsetZ)) {
            aTileEntity.increaseProgress(this.mEfficiency);
        }
        return aTileEntity.getProgress();
    }

    protected int doCoolFluid(final ForgeDirection side, final ICoverable aTileEntity) {
        final int offsetX = aTileEntity.getOffsetX(side, 1);
        final int offsetY = aTileEntity.getOffsetY(side, 1);
        final int offsetZ = aTileEntity.getOffsetZ(side, 1);
        final World world = aTileEntity.getWorld();
        final IFluidHandler fluidHandler = (IFluidHandler) aTileEntity;
        if (!fluidHandler.canDrain(ForgeDirection.UNKNOWN, IC2_HOT_COOLANT)) {
            return 0;
        }
        final int chances; // 10000 = 100%
        final Block blockAtSide = aTileEntity.getBlockAtSide(side);
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
