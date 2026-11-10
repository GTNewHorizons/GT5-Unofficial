package gregtech.crossmod.liquidlogged;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import com.gtnewhorizons.liquidlogged.api.LiquidLoggedAPI;

public final class WaterlogHelper {

    private WaterlogHelper() {}

    public static void waterlog(World world, int x, int y, int z) {
        LiquidLoggedAPI.setFluid(world, x, y, z, FluidRegistry.WATER);
    }

    /** Drops the stored fluid if the block no longer accepts it, e.g. after a cover is attached. */
    public static void revalidate(World world, int x, int y, int z) {
        LiquidLoggedAPI.revalidate(world, x, y, z);
    }
}
