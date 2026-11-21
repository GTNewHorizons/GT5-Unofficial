package gtPlusPlus.core.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntityToxinball extends EntityFireball {

    public EntityToxinball(World world) {
        super(world);
    }

    public EntityToxinball(World world, EntityLivingBase shooter, double x, double y, double z) {
        super(world, shooter, x, y, z);
    }

    public EntityToxinball(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double range = this.boundingBox.getAverageEdgeLength() * 4.0D * 64.0D;
        return distance < range * range;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }
}
