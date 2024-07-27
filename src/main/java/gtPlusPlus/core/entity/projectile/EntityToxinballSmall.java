package gtPlusPlus.core.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityToxinballSmall extends EntityToxinball {

    public EntityToxinballSmall(World world, EntityLivingBase entity, double x, double y, double z) {
        super(world, entity, x, y, z);
        this.setSize(0.3125F, 0.3125F);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition MoP) {
        if (!this.worldObj.isRemote) {
            if (MoP.entityHit != null) {
                if (!MoP.entityHit.isImmuneToFire() && MoP.entityHit
                    .attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F)) {
                    MoP.entityHit.setFire(5);
                }
            } else {
                int i = MoP.blockX;
                int j = MoP.blockY;
                int k = MoP.blockZ;

                switch (MoP.sideHit) {
                    case 0 -> --j;
                    case 1 -> ++j;
                    case 2 -> --k;
                    case 3 -> ++k;
                    case 4 -> --i;
                    case 5 -> ++i;
                }

                if (this.worldObj.isAirBlock(i, j, k)) {
                    this.worldObj.setBlock(i, j, k, Blocks.fire);
                }
            }

            this.setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }
}
