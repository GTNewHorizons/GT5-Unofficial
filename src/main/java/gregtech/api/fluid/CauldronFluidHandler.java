package gregtech.api.fluid;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.NotImplementedException;

/**
 * A fake {@link IFluidHandler} that allows GT blocks and pipes to fill a vanilla cauldron. Does not allow draining.
 */
public class CauldronFluidHandler implements IFluidHandler {

    // spotless:off
    /* Endless IDs expands block metadata to 16 bits. The new cauldron metadata:

       Clobber |   Partial Fill  | Original Cauldron Data
             /¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯\
         +-+-+-|-+-+-+-+-+-+-+-+-|-+-+-+-|
         |F|E|D|C|B|A|9|8|7|6|5|4|3|2|1|0|
         |-+-+-+-+-+-+-+-+-+-+-+-|-+-+-+-|
         \_______________________|_______/
           Endless IDs Addition   Vanilla
     */
    //spotless:on
    public final static int ORIGINAL_METADATA_MASK = 0xF;
    public final static int PARTIAL_FILL_MASK = 0x1FF0;
    public final static int CLOBBER_PARTIAL_FILL_MASK = 0x2000;
    public final static int PARTIAL_FILL_BIT_SHIFT = 4;

    // Must be below 512, or it won't fit into the allotted metadata space.
    private static final int MB_PER_LEVEL = 333;
    private static final ThreadLocal<Integer> PARTIAL_AMOUNT = ThreadLocal.withInitial(() -> 0);
    private static final FluidTankInfo[] EMPTY_FAKE_TANK = {
        new FluidTankInfo(new FluidStack(FluidRegistry.WATER, 0), MB_PER_LEVEL * 3) };
    private static final FluidTankInfo[] FULL_FAKE_TANK = {
        new FluidTankInfo(new FluidStack(FluidRegistry.WATER, MB_PER_LEVEL * 3), MB_PER_LEVEL * 3) };

    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public CauldronFluidHandler(final World world, final int x, final int y, final int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        if (world.getBlock(x, y, z) != Blocks.cauldron) {
            throw new AssertionError(
                "Tried to set up a cauldron fluid handler on a block that is not a vanilla cauldron.");
        }
    }

    @Override
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (resource.getFluid() != FluidRegistry.WATER) {
            return 0;
        }

        final int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & ORIGINAL_METADATA_MASK) == 3) {
            return 0;
        }

        final int initialFill = metadata * MB_PER_LEVEL + PARTIAL_AMOUNT.get();
        PARTIAL_AMOUNT.set(0);
        final int amountToDrain;
        int newMetadata = Math.min(3, (initialFill + resource.amount) / MB_PER_LEVEL);

        if (newMetadata == 3) {
            amountToDrain = 3 * MB_PER_LEVEL - initialFill;
            newMetadata |= CLOBBER_PARTIAL_FILL_MASK;
        } else {
            newMetadata |= ((initialFill + resource.amount) % MB_PER_LEVEL << PARTIAL_FILL_BIT_SHIFT);
            amountToDrain = resource.amount;
        }

        if (doFill) {
            world.setBlockMetadataWithNotify(x, y, z, newMetadata, 3);
        }

        return amountToDrain;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        throw new NotImplementedException("Cannot drain from a cauldron.");
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        throw new NotImplementedException("Cannot drain from a cauldron.");
    }

    @Override
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return fluid == FluidRegistry.WATER && world.getBlockMetadata(x, y, z) < 3;
    }

    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return world.getBlockMetadata(x, y, z) == 3 ? FULL_FAKE_TANK : EMPTY_FAKE_TANK;

    }

    public static void setLastPartialFill(final int partialFill) {
        PARTIAL_AMOUNT.set(partialFill);
    }
}
