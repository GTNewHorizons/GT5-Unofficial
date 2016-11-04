package gtPlusPlus.core.util.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class EntityUtils {

	public static void setEntityOnFire(Entity entity, int length){
		entity.setFire(length);
	}

	public static int getFacingDirection(Entity entity){
		int d = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360) + 0.50) & 3;
		return d;
	}

	@Deprecated
	public static Block findBlockUnderEntityNonBoundingBox(Entity parEntity){
		int blockX = MathHelper.floor_double(parEntity.posX);
		int blockY = MathHelper.floor_double(parEntity.posY-0.2D - (double)parEntity.yOffset);
		int blockZ = MathHelper.floor_double(parEntity.posZ);
		return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
	}

	public static Block findBlockUnderEntity(Entity parEntity){
		int blockX = MathHelper.floor_double(parEntity.posX);
		int blockY = MathHelper.floor_double(parEntity.boundingBox.minY)-1;
		int blockZ = MathHelper.floor_double(parEntity.posZ);
		return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
	}

	//TODO
	public static void registerEntityToBiomeSpawns(Class<EntityLiving> classy, EnumCreatureType EntityType, BiomeGenBase baseBiomeGen){
		EntityRegistry.addSpawn(classy, 6, 1, 5, EntityType, baseBiomeGen); //change the values to vary the spawn rarity, biome, etc. 
	}

	public static boolean applyRadiationDamageToEntity(int damage, World world, Entity entityHolding){
		if (!world.isRemote){				
			if (damage > 0 && (entityHolding instanceof EntityLivingBase)) {
				EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
				if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
					int duration;
					if (entityLiving.getActivePotionEffect(IC2Potion.radiation) != null){
						//Utils.LOG_INFO("t");
						duration = (damage*5)+entityLiving.getActivePotionEffect(IC2Potion.radiation).getDuration();
					}
					else {
						//Utils.LOG_INFO("f");
						duration = damage*30;
					}					
					IC2Potion.radiation.applyTo(entityLiving, duration, damage * 15);
				}
			}
			return true;
		}
		return false;		
	}

}
