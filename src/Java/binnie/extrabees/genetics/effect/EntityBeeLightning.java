package binnie.extrabees.genetics.effect;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBeeLightning
  extends EntityLightningBolt
{
  int lightningState = 2;
  int boltLivingTime;
  
  public EntityBeeLightning(World par1World, double par2, double par4, double par6)
  {
    super(par1World, par2, par4, par6);
    this.boltLivingTime = (this.rand.nextInt(3) + 1);
  }
  
  public void onUpdate()
  {
    onEntityUpdate();
    
    this.lightningState -= 1;
    if (this.lightningState < 0) {
      if (this.boltLivingTime == 0)
      {
        setDead();
      }
      else if (this.lightningState < -this.rand.nextInt(10))
      {
        this.boltLivingTime -= 1;
        this.lightningState = 1;
        this.boltVertex = this.rand.nextLong();
        if ((!this.worldObj.isRemote) && (this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)))
        {
          int i = MathHelper.floor_double(this.posX);
          int j = MathHelper.floor_double(this.posY);
          int k = MathHelper.floor_double(this.posZ);
          if ((this.worldObj.getBlock(i, j, k) == null) && (Blocks.fire.canPlaceBlockAt(this.worldObj, i, j, k))) {
            this.worldObj.setBlock(i, j, k, Blocks.fire);
          }
        }
      }
    }
    if (this.lightningState >= 0) {
      if (this.worldObj.isRemote)
      {
        this.worldObj.lastLightningBolt = 2;
      }
      else
      {
        double d0 = 3.0D;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));
        for (int l = 0; l < list.size(); l++)
        {
          Entity entity = (Entity)list.get(l);
          entity.onStruckByLightning(this);
        }
      }
    }
  }
}
