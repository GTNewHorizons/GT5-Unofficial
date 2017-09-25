package gtPlusPlus.core.entity.projectile;

import gregtech.common.GT_Pollution;
import gtPlusPlus.core.util.PollutionUtils;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
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
		int xBlock = object.blockX;
		int yBlock = object.blockY;
		int zBlock = object.blockZ;	
		if (object.entityHit != null) {
			byte b0 = 6;
			object.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) b0);
			EntityUtils.setEntityOnFire(object.entityHit, 10);
			object.entityHit.fireResistance = 0;
			ravage(new BlockPos(xBlock, yBlock, zBlock));	
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
			this.worldObj.spawnParticle(mParticleType, this.posX+MathUtils.randDouble(0, 2), this.posY+MathUtils.randDouble(0, 2), this.posZ+MathUtils.randDouble(0, 2), 0.0D, 0.0D, 0.0D);
		}

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
	
	private boolean ravage(BlockPos blockpos){
		
		for (int i=(blockpos.xPos-1);i<(blockpos.xPos+1);i++){
			for (int j=(blockpos.yPos-1);j<(blockpos.yPos+1);j++){
				for (int h=(blockpos.zPos-1);h<(blockpos.zPos+1);h++){
					
					Block mBlockhit = worldObj.getBlock(i, j, h);
					this.worldObj.spawnParticle("flame", this.posX+MathUtils.randDouble(0, 2), this.posY+MathUtils.randDouble(0, 2), this.posZ+MathUtils.randDouble(0, 2), 0.0D, 0.0D, 0.0D);
					this.worldObj.spawnParticle("largesmoke", this.posX+MathUtils.randDouble(0, 2), this.posY+MathUtils.randDouble(0, 2), this.posZ+MathUtils.randDouble(0, 2), 0.0D, 0.0D, 0.0D);
					
					int mPol = 500000000;
					
					GT_Pollution.addPollution(worldObj.getChunkFromBlockCoords(blockpos.xPos, blockpos.zPos), mPol);
					
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
		
		
		return true;
	}
	
	
}