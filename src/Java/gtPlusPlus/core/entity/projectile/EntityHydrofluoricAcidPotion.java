package gtPlusPlus.core.entity.projectile;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityHydrofluoricAcidPotion extends EntityThrowable {

	public EntityHydrofluoricAcidPotion(World world) {
		super(world);
	}

	public EntityHydrofluoricAcidPotion(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntityHydrofluoricAcidPotion(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition object) {
		int xBlock = object.blockX;
		int yBlock = object.blockY;
		int zBlock = object.blockZ;	
		if (object.entityHit != null) {
			byte b0 = 6;
			if (!GT_Utility.isWearingFullRadioHazmat((EntityLivingBase) object.entityHit)){
				object.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), b0);
				EntityUtils.setEntityOnFire(object.entityHit, 5);

				if (object.entityHit instanceof EntityPlayer){
					EntityPlayer mPlayer = (EntityPlayer) object.entityHit;
					mPlayer.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 200, 1));
					mPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 300, 2));
					mPlayer.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 250, 2));					 
				}
				
				object.entityHit.fireResistance = 0;
				ravage(EntityUtils.findBlockPosUnderEntity(object.entityHit));
				
			}
		}
		if (object.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){			
			ravage(new BlockPos(xBlock, yBlock, zBlock));			
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
			this.worldObj.spawnParticle(mParticleType, this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);
		}

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}

	private boolean ravage(BlockPos blockpos){

		int radius = 2;		

		for (int i=(blockpos.xPos-radius);i<(blockpos.xPos+radius);i++){
			for (int j=(blockpos.yPos-radius);j<(blockpos.yPos+radius);j++){
				for (int h=(blockpos.zPos-radius);h<(blockpos.zPos+radius);h++){

					int mChance = MathUtils.randInt(1, 10);
					if (mChance <= 3){
						Block mBlockhit = worldObj.getBlock(i, j, h);
						this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(-2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);
						this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(2, 2), this.posY+MathUtils.randDouble(-2, 2), this.posZ+MathUtils.randDouble(-2, 2), 0.0D, 0.0D, 0.0D);

						//GT_Pollution.addPollution(worldObj.getChunkFromBlockCoords(blockpos.xPos, blockpos.zPos), mPol);

						if (mBlockhit == Blocks.grass || mBlockhit == Blocks.mycelium){
							worldObj.setBlock(i, j+1, h, Blocks.fire);
							worldObj.setBlock(i, j, h, Blocks.dirt);
						}
						else if (mBlockhit == Blocks.leaves || mBlockhit == Blocks.leaves2){
							worldObj.setBlock(i, j, h, Blocks.fire);
						}
						else if (mBlockhit == Blocks.tallgrass){
							worldObj.setBlock(i, j, h, Blocks.fire);
							if (worldObj.getBlock(i, j-1, h) == Blocks.grass){
								worldObj.setBlock(i, j-1, h, Blocks.dirt);					
							}
						}
						else if (mBlockhit == Blocks.carrots || mBlockhit == Blocks.melon_block || mBlockhit == Blocks.pumpkin || mBlockhit == Blocks.potatoes){
							worldObj.setBlock(i, j+1, h, Blocks.fire);
							worldObj.setBlock(i, j, h, Blocks.dirt);
						}
						else if (mBlockhit == Blocks.air){
							worldObj.setBlock(i, j, h, Blocks.fire);
						}	
					}
				}	
			}
		}


		return true;
	}


}