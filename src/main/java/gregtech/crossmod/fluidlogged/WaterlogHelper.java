package gregtech.crossmod.fluidlogged;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import mega.fluidlogged.api.FLBlockAccess;
import mega.fluidlogged.api.world.WorldDriver;

public final class WaterlogHelper {

    private WaterlogHelper() {}

    public static void waterlog(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        FLBlockAccess fluidWorld = (FLBlockAccess) world;
        if (fluidWorld.fl$getFluid(x, y, z) == null
            && WorldDriver.getCanBeFluidLogged(block, meta, FluidRegistry.WATER)) {
            fluidWorld.fl$setFluid(x, y, z, FluidRegistry.WATER);
        }
    }
}
