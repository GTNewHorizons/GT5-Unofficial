package gregtech.common.blocks.rubbertree;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;

@SideOnly(Side.CLIENT)
public final class RubberTreeEffects {

    private RubberTreeEffects() {}

    public static void spawnResinRefillParticles(World world, int x, int y, int z, int side) {
        if (!(world instanceof WorldServer)) {
            return;
        }

        WorldServer worldServer = (WorldServer) world;
        Random rand = world.rand;
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        // Main burst on the resin face
        for (int i = 0; i < 18; i++) {
            double px = x + 0.5D;
            double py = y + 0.5D;
            double pz = z + 0.5D;

            double along = (rand.nextDouble() - 0.5D) * 0.70D;
            double up = rand.nextDouble() * 0.75D - 0.10D;
            double outward = 0.501D + rand.nextDouble() * 0.08D;

            if (dir.offsetX != 0) {
                px += dir.offsetX * outward;
                py += up;
                pz += along;
            } else if (dir.offsetZ != 0) {
                pz += dir.offsetZ * outward;
                py += up;
                px += along;
            } else {
                px += along;
                py += up;
                pz += (rand.nextDouble() - 0.5D) * 0.70D;
            }

            // IMPORTANT:
            // count = 0 for "reddust" => the following 3 values are RGB
            worldServer.func_147487_a("reddust", px, py, pz, 0, 0.90D, 0.68D, 0.06D, 1.0D);
        }

        // A small secondary halo around the block
        for (int i = 0; i < 8; i++) {
            double px = x + 0.2D + rand.nextDouble() * 0.6D;
            double py = y + 0.2D + rand.nextDouble() * 0.8D;
            double pz = z + 0.2D + rand.nextDouble() * 0.6D;

            worldServer.func_147487_a("reddust", px, py, pz, 0, 0.95D, 0.78D, 0.10D, 1.0D);
        }
    }

    public static void playResinHarvestSound(World world, int x, int y, int z) {
        GTUtility.sendSoundToPlayers(
            world,
            "gregtech:resin.harvest",
            0.55F,
            0.96F + world.rand.nextFloat() * 0.10F,
            x + 0.5D,
            y + 0.5D,
            z + 0.5D);
    }
}
