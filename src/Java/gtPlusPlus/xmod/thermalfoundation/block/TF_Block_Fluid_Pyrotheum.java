package gtPlusPlus.xmod.thermalfoundation.block;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class TF_Block_Fluid_Pyrotheum
  extends BlockFluidInteractive
{
  Random random = new Random();
  public static final int LEVELS = 5;
  public static final Material materialFluidPyrotheum = new MaterialLiquid(MapColor.tntColor);
  private static boolean effect = true;
  private static boolean enableSourceFall = true;
  
  public TF_Block_Fluid_Pyrotheum()
  {
    super(CORE.MODID, TF_Fluids.fluidPyrotheum, Material.lava, "pyrotheum");
    setQuantaPerBlock(5);
    setTickRate(10);
    
    setHardness(1000.0F);
    setLightOpacity(1);
    setParticleColor(1.0F, 0.7F, 0.15F);
  }
  
  public boolean preInit()
  {
    GameRegistry.registerBlock(this, "FluidPyrotheum");
    
    addInteraction(Blocks.cobblestone, Blocks.stone);
    addInteraction(Blocks.grass, Blocks.dirt);
    addInteraction(Blocks.sand, Blocks.glass);
    addInteraction(Blocks.water, Blocks.stone);
    addInteraction(Blocks.flowing_water, Blocks.stone);
    addInteraction(Blocks.clay, Blocks.hardened_clay);
    addInteraction(Blocks.ice, Blocks.stone);
    addInteraction(Blocks.snow, Blocks.air);
    addInteraction(Blocks.snow_layer, Blocks.air);
    for (int i = 0; i < 8; i++) {
      addInteraction(Blocks.stone_stairs, i, Blocks.stone_brick_stairs, i);
    }
    String str1 = "Fluid.Pyrotheum";
    String str2 = "Enable this for Fluid Pyrotheum to be worse than lava.";
    effect = true;
    
    str2 = "Enable this for Fluid Pyrotheum Source blocks to gradually fall downwards.";
    enableSourceFall = true;
    
    return true;
  }
  
  public void onEntityCollidedWithBlock(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Entity paramEntity)
  {
    if (!effect) {
      return;
    }
    if (ServerHelper.isClientWorld(paramWorld)) {
      return;
    }
    if (!(paramEntity instanceof EntityPlayer)) {
      if ((paramEntity instanceof EntityCreeper))
      {
        paramWorld.createExplosion(paramEntity, paramEntity.posX, paramEntity.posY, paramEntity.posZ, 6.0F, paramEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
        paramEntity.setDead();
      }
    }
  }
  
  public int getLightValue(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3)
  {
    return TF_Fluids.fluidPyrotheum.getLuminosity();
  }
  
  public int getFireSpreadSpeed(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    return effect ? 800 : 0;
  }
  
  public int getFlammability(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    return 0;
  }
  
  public boolean isFlammable(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    return (effect) && (paramForgeDirection.ordinal() > ForgeDirection.UP.ordinal()) && (paramIBlockAccess.getBlock(paramInt1, paramInt2 - 1, paramInt3) != this);
  }
  
  public boolean isFireSource(World paramWorld, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    return effect;
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
      if (((localBlock == this) && (i != 0)) || (localBlock.isFlammable(paramWorld, paramInt1, paramInt2 + this.densityDir, paramInt3, ForgeDirection.UP)))
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
      triggerInteractionEffects(paramWorld, paramInt1, paramInt2, paramInt3);
    }
    else if (localBlock.isFlammable(paramWorld, paramInt1, paramInt2, paramInt3, ForgeDirection.UP))
    {
      paramWorld.setBlock(paramInt1, paramInt2, paramInt3, Blocks.fire);
    }
    else if ((paramWorld.isSideSolid(paramInt1, paramInt2, paramInt3, ForgeDirection.UP)) && (paramWorld.isAirBlock(paramInt1, paramInt2 + 1, paramInt3)))
    {
      paramWorld.setBlock(paramInt1, paramInt2 + 1, paramInt3, Blocks.fire, 0, 3);
    }
  }
  
  protected void triggerInteractionEffects(World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.random.nextInt(16) == 0) {
      paramWorld.playSoundEffect(paramInt1 + 0.5F, paramInt2 + 0.5F, paramInt3 + 0.5F, "random.fizz", 0.5F, 2.2F + (paramWorld.rand.nextFloat() - paramWorld.rand.nextFloat()) * 0.8F);
    }
  }
}
