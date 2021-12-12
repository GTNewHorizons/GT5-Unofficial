package gtPlusPlus.australia.entity.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityAustralianSpiderBase extends EntitySpider {

	public EntityAustralianSpiderBase(World p_i1732_1_) {
		super(p_i1732_1_);
		this.setSize(0.7F, 0.5F);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
	}

	public boolean attackEntityAsMob(Entity p_70652_1_) {
		if (super.attackEntityAsMob(p_70652_1_)) {
			if (p_70652_1_ instanceof EntityLivingBase) {
				byte b0 = 45;
				if (b0 > 0) {
					((EntityLivingBase) p_70652_1_).addPotionEffect(new PotionEffect(Potion.poison.id, b0 * 20, 0));
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		return p_110161_1_;
	}
}