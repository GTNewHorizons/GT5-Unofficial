package gregtech.common.blocks.rubbertree;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

@SideOnly(Side.CLIENT)
public final class RubberTreeClientEffects {

    private RubberTreeClientEffects() {}

    public static void spawnResinRefillParticles(World world, int x, int y, int z, int side) {
        if (world == null) {
            return;
        }

        Random rand = world.rand;
        ForgeDirection dir = ForgeDirection.getOrientation(side);

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

            double red = 0.90D + rand.nextDouble() * 0.10D;
            double green = 0.68D + rand.nextDouble() * 0.12D;
            double blue = 0.06D + rand.nextDouble() * 0.05D;

            world.spawnParticle("reddust", px, py, pz, red, green, blue);
        }

        for (int i = 0; i < 8; i++) {
            double px = x + 0.2D + rand.nextDouble() * 0.6D;
            double py = y + 0.2D + rand.nextDouble() * 0.8D;
            double pz = z + 0.2D + rand.nextDouble() * 0.6D;

            double red = 0.95D;
            double green = 0.78D;
            double blue = 0.10D;

            world.spawnParticle("reddust", px, py, pz, red, green, blue);
        }
    }
}
