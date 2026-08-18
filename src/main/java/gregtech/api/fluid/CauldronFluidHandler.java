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

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;

/**
 * A fake {@link IFluidHandler} that allows GT blocks and pipes to fill a vanilla cauldron. Does not allow draining.
 */
public class CauldronFluidHandler implements IFluidHandler {

    public final static int ORIGINAL_METADATA_MASK = 0x3;
    public final static int PARTIAL_FILL_MASK = 0x7FC;
    public final static int CLOBBER_PARTIAL_FILL_MASK = 0x800;

    private static final int MB_PER_LEVEL = 333;
    private static final Long2IntMap PARTIAL_AMOUNTS = new Long2IntLinkedOpenHashMap();
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

        final long coordinates = CoordinatePacker.pack(x, y, z);
        final int metadata = world.getBlockMetadata(x, y, z);
        final int partialAmount = PARTIAL_AMOUNTS.getOrDefault(coordinates, 0);
        PARTIAL_AMOUNTS.remove(coordinates);

        if (metadata == 3) {
            return 0;
        }

        final int initialFill = metadata * MB_PER_LEVEL + partialAmount;

        final int amountToDrain;
        int newMetadata = Math.min(3, (initialFill + resource.amount) / MB_PER_LEVEL);

        if (newMetadata == 3) {
            amountToDrain = 3 * MB_PER_LEVEL - initialFill;
            newMetadata |= CLOBBER_PARTIAL_FILL_MASK;
        } else {
            newMetadata = newMetadata | ((initialFill + resource.amount) % MB_PER_LEVEL << 2);
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
        return fluid == FluidRegistry.WATER && world.getBlockMetadata(x, y, z) == 0;
    }

    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        if (world.getBlockMetadata(x, y, z) == 3) {
            return FULL_FAKE_TANK;
        }

        return EMPTY_FAKE_TANK;
    }

    public static void setLastPartialFill(int x, int y, int z, int partialFill) {
        PARTIAL_AMOUNTS.put(CoordinatePacker.pack(x, y, z), partialFill);
    }
}
