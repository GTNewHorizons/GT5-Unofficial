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

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class CoverVent extends CoverLegacyData {

    private final int mEfficiency;
    private final Fluid IC2_HOT_COOLANT = FluidRegistry.getFluid("ic2hotcoolant");
    private final Fluid IC2_COOLANT = GTModHandler.getIC2Coolant(0)
        .getFluid();

    public CoverVent(CoverContext context, int aEfficiency) {
        super(context);
        this.mEfficiency = aEfficiency;
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverSide == ForgeDirection.UNKNOWN) {
            coverData = 0;
            return;
        }
        if (coverable instanceof IFluidHandler fluidHandler) {
            coverData = doCoolFluid(coverSide, coverable, fluidHandler);
        }
        if ((coverable instanceof IMachineProgress machine)) {
            coverData = doProgressEfficiency(coverSide, machine, coverID);
        }
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 100;
    }

    protected int doProgressEfficiency(final ForgeDirection coverSide, final IMachineProgress coverable,
        final int coverData) {
        final int offsetX = coverable.getOffsetX(coverSide, 1);
        final int offsetY = coverable.getOffsetY(coverSide, 1);
        final int offsetZ = coverable.getOffsetZ(coverSide, 1);
        final World world = coverable.getWorld();
        if (coverable.hasThingsToDo() && coverData != coverable.getProgress()
            && !GTUtility.hasBlockHitBox(world, offsetX, offsetY, offsetZ)) {
            coverable.increaseProgress(this.mEfficiency);
        }
        return coverable.getProgress();
    }

    protected int doCoolFluid(final ForgeDirection coverSide, final ICoverable coverable, IFluidHandler fluidHandler) {
        final int offsetX = coverable.getOffsetX(coverSide, 1);
        final int offsetY = coverable.getOffsetY(coverSide, 1);
        final int offsetZ = coverable.getOffsetZ(coverSide, 1);
        final World world = coverable.getWorld();
        if (!fluidHandler.canDrain(ForgeDirection.UNKNOWN, IC2_HOT_COOLANT)) {
            return 0;
        }
        final int chances; // 10000 = 100%
        final Block blockAtSide = coverable.getBlockAtSide(coverSide);
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
