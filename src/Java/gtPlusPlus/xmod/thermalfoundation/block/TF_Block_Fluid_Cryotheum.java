package gtPlusPlus.xmod.thermalfoundation.block;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.DamageHelper;
import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class TF_Block_Fluid_Cryotheum
  extends BlockFluidInteractive
{
  Random random = new Random();
  public static final int LEVELS = 5;
  public static final Material materialFluidCryotheum = new MaterialLiquid(MapColor.iceColor);
  private static boolean enableSourceFall = true;
  private static boolean effect = true;
  
  public TF_Block_Fluid_Cryotheum()
  {
    super(CORE.MODID, TF_Fluids.fluidCryotheum, materialFluidCryotheum, "cryotheum");
    setQuantaPerBlock(5);
    setTickRate(15);
    
    setHardness(1000.0F);
    setLightOpacity(1);
    setParticleColor(0.15F, 0.7F, 1.0F);
  }
  
  public boolean preInit()
  {
    GameRegistry.registerBlock(this, "FluidCryotheum");
    
    addInteraction(Blocks.grass, Blocks.dirt);
    addInteraction(Blocks.water, 0, Blocks.ice);
    addInteraction(Blocks.water, Blocks.snow);
    addInteraction(Blocks.flowing_water, 0, Blocks.ice);
    addInteraction(Blocks.flowing_water, Blocks.snow);
    addInteraction(Blocks.lava, 0, Blocks.obsidian);
    addInteraction(Blocks.lava, Blocks.stone);
    addInteraction(Blocks.flowing_lava, 0, Blocks.obsidian);
    addInteraction(Blocks.flowing_lava, Blocks.stone);
    addInteraction(Blocks.leaves, Blocks.air);
    addInteraction(Blocks.tallgrass, Blocks.air);
    addInteraction(Blocks.fire, Blocks.air);
    //addInteraction(TFBlocks.blockFluidGlowstone, 0, Blocks.glowstone);
    
    String str1 = "Fluid.Cryotheum";
    String str2 = "Enable this for Fluid Cryotheum to be worse than lava, except cold.";
    effect = true;
    
    str2 = "Enable this for Fluid Cryotheum Source blocks to gradually fall downwards.";
    enableSourceFall = true;
    
    return true;
  }
  
  public void onEntityCollidedWithBlock(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Entity paramEntity)
  {
    paramEntity.extinguish();
    if (!effect) {
      return;
    }
    if ((paramEntity.motionY < -0.05D) || (paramEntity.motionY > 0.05D)) {
      paramEntity.motionY *= 0.05D;
    }
    if ((paramEntity.motionZ < -0.05D) || (paramEntity.motionZ > 0.05D)) {
      paramEntity.motionZ *= 0.05D;
    }
    if ((paramEntity.motionX < -0.05D) || (paramEntity.motionX > 0.05D)) {
      paramEntity.motionX *= 0.05D;
    }
    if (ServerHelper.isClientWorld(paramWorld)) {
      return;
    }
    if (paramWorld.getTotalWorldTime() % 8L != 0L) {
      return;
    }
    if (((paramEntity instanceof EntityZombie)) || ((paramEntity instanceof EntityCreeper)))
    {
      EntitySnowman localEntitySnowman = new EntitySnowman(paramWorld);
      localEntitySnowman.setLocationAndAngles(paramEntity.posX, paramEntity.posY, paramEntity.posZ, paramEntity.rotationYaw, paramEntity.rotationPitch);
      paramWorld.spawnEntityInWorld(localEntitySnowman);
      
      paramEntity.setDead();
    }
    else if (/*((paramEntity instanceof EntityBlizz)) ||*/((paramEntity instanceof EntitySnowman)))
    {
      ((EntityLivingBase)paramEntity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 120, 0));
      ((EntityLivingBase)paramEntity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 120, 0));
    }
    else if ((paramEntity instanceof EntityBlaze))
    {
      paramEntity.attackEntityFrom(DamageHelper.cryotheum, 10.0F);
    }
    else
    {
      boolean bool = paramEntity.velocityChanged;
      paramEntity.attackEntityFrom(DamageHelper.cryotheum, 2.0F);
      paramEntity.velocityChanged = bool;
    }
  }
  
  public int getLightValue(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3)
  {
    return TF_Fluids.fluidCryotheum.getLuminosity();
  }
  
  public void updateTick(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Random paramRandom)
  {
    if (effect) {
      checkForInteraction(paramWorld, paramInt1, paramInt2, paramInt3);
    }
    if ((enableSourceFall) && (paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3) == 0))
    {
      Block localBlock = paramWorld.getBlock(paramInt1, paramInt2 + this.densityDir, paramInt3);
      int i = paramWorld.getBlockMetadata(paramInt1, paramInt2 + this.densityDir, paramInt3);
      if ((localBlock == this) && (i != 0))
      {
        paramWorld.setBlock(paramInt1, paramInt2 + this.densityDir, paramInt3, this, 0, 3);
        paramWorld.setBlockToAir(paramInt1, paramInt2, paramInt3);
        return;
      }
    }
    super.updateTick(paramWorld, paramInt1, paramInt2, paramInt3, paramRandom);
  }
  
  protected void checkForInteraction(World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramWorld.getBlock(paramInt1, paramInt2, paramInt3) != this) {
      return;
    }
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    for (int m = 0; m < 6; m++)
    {
      i = paramInt1 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][0];
      j = paramInt2 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][1];
      k = paramInt3 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][2];
      
      interactWithBlock(paramWorld, i, j, k);
    }
    interactWithBlock(paramWorld, paramInt1 - 1, paramInt2, paramInt3 - 1);
    interactWithBlock(paramWorld, paramInt1 - 1, paramInt2, paramInt3 + 1);
    interactWithBlock(paramWorld, paramInt1 + 1, paramInt2, paramInt3 - 1);
    interactWithBlock(paramWorld, paramInt1 + 1, paramInt2, paramInt3 + 1);
  }
  
  protected void interactWithBlock(World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    Block localBlock = paramWorld.getBlock(paramInt1, paramInt2, paramInt3);
    if ((localBlock == Blocks.air) || (localBlock == this)) {
      return;
    }
    int i = paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3);
    if (hasInteraction(localBlock, i))
    {
      BlockWrapper localBlockWrapper = getInteraction(localBlock, i);
      paramWorld.setBlock(paramInt1, paramInt2, paramInt3, localBlockWrapper.block, localBlockWrapper.metadata, 3);
    }
    else if ((paramWorld.isSideSolid(paramInt1, paramInt2, paramInt3, ForgeDirection.UP)) && (paramWorld.isAirBlock(paramInt1, paramInt2 + 1, paramInt3)))
    {
      paramWorld.setBlock(paramInt1, paramInt2 + 1, paramInt3, Blocks.snow_layer, 0, 3);
    }
  }
  
  protected void triggerInteractionEffects(World paramWorld, int paramInt1, int paramInt2, int paramInt3) {}
}
