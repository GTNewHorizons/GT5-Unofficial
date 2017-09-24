package gtPlusPlus.core.entity.projectile;

import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySulfuricAcidPotion extends EntityThrowable {

	public EntitySulfuricAcidPotion(World world) {
		super(world);
	}

	public EntitySulfuricAcidPotion(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntitySulfuricAcidPotion(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition object) {
		if (object.entityHit != null) {
			byte b0 = 6;
			object.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) b0);
			EntityUtils.setEntityOnFire(object.entityHit, 10);
		}

		String mParticleType = "reddust";
		int e=0;
		for (int i = 0; i < 24; ++i) {
			if ((e = MathUtils.randInt(0, 5)) <= 1){
				if (e==0)
				mParticleType = "largesmoke";
				if (e==1)
				mParticleType = "flame";
			}
			this.worldObj.spawnParticle(mParticleType, this.posX+MathUtils.randDouble(0, 2), this.posY+MathUtils.randDouble(0, 2), this.posZ+MathUtils.randDouble(0, 2), 0.0D, 0.0D, 0.0D);
		}

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
}