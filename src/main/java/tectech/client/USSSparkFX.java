package tectech.client;

import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.entity.fx.WeightlessParticleFX;

/**
 * Self-lit variant of the weightless FX — the space around the star carries no world light, and the base
 * {@code getBrightnessForRender} samples the world's light at the particle's position (0 in the void → black
 * particles, see the firework-spark pattern).
 */
public class USSSparkFX extends WeightlessParticleFX {

    public USSSparkFX(World world, double x, double y, double z, double mx, double my, double mz) {
        super(world, x, y, z, mx, my, mz);
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return 15728880; // full sky + full block light
    }

    @Override
    public float getBrightness(float partialTick) {
        return 1.0F;
    }
}
