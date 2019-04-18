package gtPlusPlus.core.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningAttack extends EntityWitherSkull {

    public EntityLightningAttack(World p_i1793_1_)
    {
        super(p_i1793_1_);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntityLightningAttack(World p_i1794_1_, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_)
    {
        super(p_i1794_1_, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        this.setSize(0.3125F, 0.3125F);
    }

    @SideOnly(Side.CLIENT)
    public EntityLightningAttack(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_)
    {
        super(p_i1795_1_, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_);
        this.setSize(0.3125F, 0.3125F);
    }	


    /**
     * Called when this EntityFireball hits a block or entity.
     */
	protected void onImpact(MovingObjectPosition p_70227_1_) {
		
		if (!this.worldObj.isRemote) {
			if (p_70227_1_.entityHit != null) {
				if (this.shootingEntity != null) {
					if (p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F)
							&& !p_70227_1_.entityHit.isEntityAlive()) {
						this.shootingEntity.heal(0.5F);
					}
				} else {
					p_70227_1_.entityHit.attackEntityFrom(DamageSource.lava, 10.0F);
				}

				if (p_70227_1_.entityHit instanceof EntityLivingBase) {
					byte b0 = 0;

					if (this.worldObj.difficultySetting == EnumDifficulty.NORMAL) {
						b0 = 10;
					} else if (this.worldObj.difficultySetting == EnumDifficulty.HARD) {
						b0 = 40;
					}

					if (b0 > 0) {
						((EntityLivingBase) p_70227_1_.entityHit).addPotionEffect(new PotionEffect(Potion.poison.id, 20 * b0, 1));
						((EntityLivingBase) p_70227_1_.entityHit).addPotionEffect(new PotionEffect(Potion.confusion.id, 20 * b0, 1));
						((EntityLivingBase) p_70227_1_.entityHit).addPotionEffect(new PotionEffect(Potion.weakness.id, 20 * b0, 1));
					}
				}
			}

			this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false,
					this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
			this.setDead();
		}
	}


}