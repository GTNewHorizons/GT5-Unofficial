package binnie.extrabees.genetics.effect;

import binnie.Binnie;
import binnie.core.liquid.ManagerLiquid;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.genetics.ExtraBeesFlowers;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cofh.api.energy.IEnergyReceiver;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IArmorApiarist;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IEffectData;
import forestry.apiculture.proxy.ProxyApiculture;
import forestry.plugins.PluginApiculture;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public enum ExtraBeesEffect
  implements IAlleleBeeEffect
{
  ECTOPLASM,  ACID,  SPAWN_ZOMBIE,  SPAWN_SKELETON,  SPAWN_CREEPER,  LIGHTNING,  RADIOACTIVE,  METEOR,  HUNGER,  FOOD,  BLINDNESS,  CONFUSION,  FIREWORKS,  FESTIVAL,  BIRTHDAY,  TELEPORT,  GRAVITY,  THIEF,  WITHER,  WATER,  SLOW,  BonemealSapling,  BonemealFruit,  BonemealMushroom,  Power;
  
  private ExtraBeesEffect()
  {
    this.uid = toString().toLowerCase();
    this.combinable = false;
    this.dominant = true;
  }
  
  String fx = "";
  public boolean combinable;
  public boolean dominant;
  public int id;
  private String uid;
  static List<Birthday> birthdays;
  
  public static void doInit()
  {
    BLINDNESS.setFX("blindness");
    FOOD.setFX("food");
    GRAVITY.setFX("gravity");
    THIEF.setFX("gravity");
    TELEPORT.setFX("gravity");
    LIGHTNING.setFX("lightning");
    METEOR.setFX("meteor");
    RADIOACTIVE.setFX("radioactive");
    WATER.setFX("water");
    WITHER.setFX("wither");
    for (ExtraBeesEffect effect : values()) {
      effect.register();
    }
  }
  
  private void setFX(String string)
  {
    this.fx = ("particles/" + string);
  }
  
  public void register()
  {
    AlleleManager.alleleRegistry.registerAllele(this);
  }
  
  public boolean isCombinable()
  {
    return this.combinable;
  }
  
  public IEffectData validateStorage(IEffectData storedData)
  {
    return storedData;
  }
  
  public String getName()
  {
    return ExtraBees.proxy.localise("effect." + name().toString().toLowerCase() + ".name");
  }
  
  public boolean isDominant()
  {
    return this.dominant;
  }
  
  public void spawnMob(World world, int x, int y, int z, String name)
  {
    if (anyPlayerInRange(world, x, y, z, 16))
    {
      double var1 = x + world.rand.nextFloat();
      double var3 = y + world.rand.nextFloat();
      double var5 = z + world.rand.nextFloat();
      world.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
      world.spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);
      EntityLiving var9 = (EntityLiving)EntityList.createEntityByName(name, world);
      if (var9 == null) {
        return;
      }
      int var10 = world.getEntitiesWithinAABB(var9.getClass(), AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(8.0D, 4.0D, 8.0D)).size();
      if (var10 >= 6) {
        return;
      }
      if (var9 != null)
      {
        double var11 = x + (world.rand.nextDouble() - world.rand.nextDouble()) * 4.0D;
        

        double var13 = y + world.rand.nextInt(3) - 1;
        double var15 = z + (world.rand.nextDouble() - world.rand.nextDouble()) * 4.0D;
        

        var9.setLocationAndAngles(var11, var13, var15, world.rand.nextFloat() * 360.0F, 0.0F);
        if (var9.getCanSpawnHere())
        {
          world.spawnEntityInWorld(var9);
          world.playAuxSFX(2004, x, y, z, 0);
          var9.spawnExplosionParticle();
        }
      }
    }
  }
  
  private boolean anyPlayerInRange(World world, int x, int y, int z, int distance)
  {
    return world.getClosestPlayer(x + 0.5D, y + 0.5D, z + 0.5D, distance) != null;
  }
  
  public static void doAcid(World world, int x, int y, int z)
  {
    Block block = world.getBlock(x, y, z);
    if ((block == Blocks.cobblestone) || (block == Blocks.stone)) {
      world.setBlock(x, y, z, Blocks.gravel, 0, 0);
    } else if (((block == Blocks.dirt ? 1 : 0) | (block == Blocks.grass ? 1 : 0)) != 0) {
      world.setBlock(x, y, z, Blocks.sand, 0, 0);
    }
  }
  
  public String getUID()
  {
    return "extrabees.effect." + this.uid;
  }
  
  public IEffectData doEffect(IBeeGenome genome, IEffectData storedData, IBeeHousing housing)
  {
    World world = housing.getWorld();
    int xHouse = housing.getXCoord();
    int yHouse = housing.getYCoord();
    int zHouse = housing.getZCoord();
    
    int[] area = getModifiedArea(genome, housing);
    
    int xd = 1 + area[0] / 2;
    int yd = 1 + area[1] / 2;
    int zd = 1 + area[2] / 2;
    int x1 = xHouse - xd + world.rand.nextInt(2 * xd + 1);
    int y1 = yHouse - yd + world.rand.nextInt(2 * yd + 1);
    int z1 = zHouse - zd + world.rand.nextInt(2 * zd + 1);
    switch (1.$SwitchMap$binnie$extrabees$genetics$effect$ExtraBeesEffect[ordinal()])
    {
    case 4: 
      if (world.rand.nextInt(100) < 4)
      {
        if ((world.isAirBlock(x1, y1, z1)) && ((world.isBlockNormalCubeDefault(x1, y1 - 1, z1, false)) || (world.getBlock(x1, y1 - 1, z1) == ExtraBees.ectoplasm))) {
          world.setBlock(x1, y1, z1, ExtraBees.ectoplasm, 0, 0);
        }
        return null;
      }
      break;
    case 5: 
      if (world.rand.nextInt(100) < 6) {
        doAcid(world, x1, y1, z1);
      }
      break;
    case 6: 
      if (world.rand.nextInt(200) < 2) {
        spawnMob(world, x1, y1, z1, "Zombie");
      }
      break;
    case 7: 
      if (world.rand.nextInt(200) < 2) {
        spawnMob(world, x1, y1, z1, "Skeleton");
      }
      break;
    case 8: 
      if (world.rand.nextInt(200) < 2) {
        spawnMob(world, x1, y1, z1, "Creeper");
      }
      break;
    case 9: 
      if (world.rand.nextInt(100) < 1) {
        if ((world.canBlockSeeTheSky(x1, y1, z1)) && 
          ((world instanceof WorldServer))) {
          ((WorldServer)world).addWeatherEffect(new EntityBeeLightning(world, x1, y1, z1));
        }
      }
      break;
    case 10: 
      if (world.rand.nextInt(100) < 1) {
        if (world.canBlockSeeTheSky(x1, y1, z1)) {
          ((WorldServer)world).spawnEntityInWorld(new EntitySmallFireball(world, x1, y1 + 64, z1, 0.0D, -0.6D, 0.0D));
        }
      }
      break;
    case 11: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing))
      {
        int damage = 4;
        if ((entity instanceof EntityPlayer))
        {
          int count = wearsItems((EntityPlayer)entity);
          if (count > 3) {
            continue;
          }
          if (count > 2) {
            damage = 1;
          } else if (count > 1) {
            damage = 2;
          } else if (count > 0) {
            damage = 3;
          }
        }
        entity.attackEntityFrom(DamageSource.generic, damage);
      }
      break;
    case 12: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing)) {
        if ((entity instanceof EntityPlayer))
        {
          EntityPlayer player = (EntityPlayer)entity;
          player.getFoodStats().addStats(2, 0.2F);
        }
      }
      break;
    case 13: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing)) {
        if ((entity instanceof EntityPlayer))
        {
          EntityPlayer player = (EntityPlayer)entity;
          if (world.rand.nextInt(4) >= wearsItems(player))
          {
            player.getFoodStats().addExhaustion(4.0F);
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 100));
          }
        }
      }
      break;
    case 14: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing)) {
        if ((entity instanceof EntityPlayer))
        {
          EntityPlayer player = (EntityPlayer)entity;
          if (world.rand.nextInt(4) >= wearsItems(player)) {
            player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200));
          }
        }
      }
      break;
    case 15: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing)) {
        if ((entity instanceof EntityPlayer))
        {
          EntityPlayer player = (EntityPlayer)entity;
          if (world.rand.nextInt(4) >= wearsItems(player)) {
            player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200));
          }
        }
      }
      break;
    case 16: 
      for (EntityLivingBase entity : getEntities(EntityLivingBase.class, genome, housing)) {
        if ((entity instanceof EntityPlayer))
        {
          EntityPlayer player = (EntityPlayer)entity;
          if (world.rand.nextInt(4) >= wearsItems(player)) {
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200));
          }
        }
      }
      break;
    case 1: 
    case 2: 
    case 3: 
      if (world.rand.nextInt(this == FIREWORKS ? 8 : 12) < 1)
      {
        FireworkCreator.Firework firework = new FireworkCreator.Firework();
        switch (1.$SwitchMap$binnie$extrabees$genetics$effect$ExtraBeesEffect[ordinal()])
        {
        case 1: 
          firework.setShape(FireworkCreator.Shape.Star);
          firework.addColor(16768256);
          for (Birthday birthday : birthdays) {
            if (birthday.isToday())
            {
              firework.addColor(16711680);
              firework.addColor(65280);
              firework.addColor(255);
              firework.setTrail();
              break;
            }
          }
          break;
        case 2: 
          break;
        case 3: 
          firework.setShape(FireworkCreator.Shape.Ball);
          firework.addColor(genome.getPrimary().getIconColour(0));
          firework.addColor(genome.getPrimary().getIconColour(0));
          firework.addColor(genome.getPrimary().getIconColour(1));
          firework.addColor(genome.getSecondary().getIconColour(0));
          firework.addColor(genome.getSecondary().getIconColour(0));
          firework.addColor(genome.getPrimary().getIconColour(1));
          firework.setTrail();
          break;
        }
        EntityFireworkRocket var11 = new EntityFireworkRocket(world, x1, y1, z1, firework.getFirework());
        if (world.canBlockSeeTheSky(x1, y1, z1)) {
          ((WorldServer)world).spawnEntityInWorld(var11);
        }
      }
      break;
    case 17: 
      List<Entity> entities2 = getEntities(Entity.class, genome, housing);
      for (Entity entity : entities2)
      {
        float entityStrength = 1.0F;
        if ((entity instanceof EntityPlayer)) {
          entityStrength *= 100.0F;
        }
        double dx = x1 - entity.posX;
        double dy = y1 - entity.posY;
        double dz = z1 - entity.posZ;
        if (dx * dx + dy * dy + dz * dz < 2.0D) {
          return null;
        }
        double strength = 0.5D / (dx * dx + dy * dy + dz * dz) * entityStrength;
        entity.addVelocity(dx * strength, dy * strength, dz * strength);
      }
      break;
    case 18: 
      List<EntityPlayer> entities3 = getEntities(EntityPlayer.class, genome, housing);
      for (EntityPlayer entity : entities3)
      {
        double dx = x1 - entity.posX;
        double dy = y1 - entity.posY;
        double dz = z1 - entity.posZ;
        if (dx * dx + dy * dy + dz * dz < 2.0D) {
          return null;
        }
        double strength = 0.5D / (dx * dx + dy * dy + dz * dz);
        entity.addVelocity(-dx * strength, -dy * strength, -dz * strength);
      }
      break;
    case 19: 
      if (world.rand.nextInt(80) > 1) {
        return null;
      }
      List<Entity> entities = getEntities(Entity.class, genome, housing);
      if (entities.size() == 0) {
        return null;
      }
      Entity entity = (Entity)entities.get(world.rand.nextInt(entities.size()));
      if (!(entity instanceof EntityLiving)) {
        return null;
      }
      float jumpDist = 5.0F;
      if (y1 < 4) {
        y1 = 4;
      }
      if ((!world.isAirBlock(x1, y1, z1)) || (!world.isAirBlock(x1, y1 + 1, z1))) {
        return null;
      }
      ((EntityLiving)entity).setPositionAndUpdate(x1, y1, z1);
      



      ((EntityLiving)entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 160, 10));
      

      break;
    case 20: 
      if (world.rand.nextInt(120) > 1) {
        return null;
      }
      TileEntity tile = world.getTileEntity(x1, y1, z1);
      if ((tile instanceof IFluidHandler)) {
        ((IFluidHandler)tile).fill(ForgeDirection.UP, Binnie.Liquid.getLiquidStack("water", 100), true);
      }
      break;
    case 21: 
      if (world.rand.nextInt(20) > 1) {
        return null;
      }
      if (ExtraBeesFlowers.Sapling.isAcceptedFlower(world, null, x1, y1, z1)) {
        ItemDye.applyBonemeal(new ItemStack(Blocks.dirt, 1), world, x1, y1, z1, null);
      }
      break;
    case 22: 
      if (world.rand.nextInt(20) > 1) {
        return null;
      }
      if (ExtraBeesFlowers.Fruit.isAcceptedFlower(world, null, x1, y1, z1)) {
        ItemDye.applyBonemeal(new ItemStack(Blocks.dirt, 1), world, x1, y1, z1, null);
      }
      break;
    case 23: 
      if (world.rand.nextInt(20) > 1) {
        return null;
      }
      if ((world.getBlock(x1, y1, z1) == Blocks.brown_mushroom) || (world.getBlock(x1, y1, z1) == Blocks.red_mushroom)) {
        ItemDye.applyBonemeal(new ItemStack(Blocks.dirt, 1), world, x1, y1, z1, null);
      }
      break;
    case 24: 
      TileEntity tile2 = world.getTileEntity(x1, y1, z1);
      if ((tile2 instanceof IEnergyReceiver)) {
        ((IEnergyReceiver)tile2).receiveEnergy(ForgeDirection.getOrientation(0), 5, true);
      }
      break;
    case 25: 
      break;
    }
    return null;
  }
  
  protected int[] getModifiedArea(IBeeGenome genome, IBeeHousing housing)
  {
    int[] area = genome.getTerritory(); int 
      tmp9_8 = 0; int[] tmp9_7 = area;tmp9_7[tmp9_8] = ((int)(tmp9_7[tmp9_8] * (housing.getTerritoryModifier(genome, 1.0F) * 3.0F))); int 
      tmp29_28 = 1; int[] tmp29_27 = area;tmp29_27[tmp29_28] = ((int)(tmp29_27[tmp29_28] * (housing.getTerritoryModifier(genome, 1.0F) * 3.0F))); int 
      tmp49_48 = 2; int[] tmp49_47 = area;tmp49_47[tmp49_48] = ((int)(tmp49_47[tmp49_48] * (housing.getTerritoryModifier(genome, 1.0F) * 3.0F)));
    if (area[0] < 1) {
      area[0] = 1;
    }
    if (area[1] < 1) {
      area[1] = 1;
    }
    if (area[2] < 1) {
      area[2] = 1;
    }
    return area;
  }
  
  public IEffectData doFX(IBeeGenome genome, IEffectData storedData, IBeeHousing housing)
  {
    int[] area = genome.getTerritory(); int 
      tmp11_10 = 0; int[] tmp11_8 = area;tmp11_8[tmp11_10] = ((int)(tmp11_8[tmp11_10] * housing.getTerritoryModifier(genome, 1.0F))); int 
      tmp28_27 = 1; int[] tmp28_25 = area;tmp28_25[tmp28_27] = ((int)(tmp28_25[tmp28_27] * housing.getTerritoryModifier(genome, 1.0F))); int 
      tmp45_44 = 2; int[] tmp45_42 = area;tmp45_42[tmp45_44] = ((int)(tmp45_42[tmp45_44] * housing.getTerritoryModifier(genome, 1.0F)));
    if (area[0] < 1) {
      area[0] = 1;
    }
    if (area[1] < 1) {
      area[1] = 1;
    }
    if (area[2] < 1) {
      area[2] = 1;
    }
    PluginApiculture.proxy.addBeeHiveFX("particles/swarm_bee", housing.getWorld(), housing.getXCoord(), housing.getYCoord(), housing.getZCoord(), genome.getPrimary().getIconColour(0), area[0], area[1], area[2]);
    
    return storedData;
  }
  
  public String getFX()
  {
    return this.fx;
  }
  
  public <T extends Entity> List<T> getEntities(Class<T> eClass, IBeeGenome genome, IBeeHousing housing)
  {
    int[] area = genome.getTerritory();
    int[] offset = { -Math.round(area[0] / 2), -Math.round(area[1] / 2), -Math.round(area[2] / 2) };
    

    int[] min = { housing.getXCoord() + offset[0], housing.getYCoord() + offset[1], housing.getZCoord() + offset[2] };
    int[] max = { housing.getXCoord() + offset[0] + area[0], housing.getYCoord() + offset[1] + area[1], housing.getZCoord() + offset[2] + area[2] };
    
    AxisAlignedBB box = AxisAlignedBB.getBoundingBox(min[0], min[1], min[2], max[0], max[1], max[2]);
    return housing.getWorld().getEntitiesWithinAABB(eClass, box);
  }
  
  public static boolean wearsHelmet(EntityPlayer player)
  {
    ItemStack armorItem = player.inventory.armorInventory[3];
    return (armorItem != null) && ((armorItem.getItem() instanceof IArmorApiarist));
  }
  
  public static boolean wearsChest(EntityPlayer player)
  {
    ItemStack armorItem = player.inventory.armorInventory[2];
    return (armorItem != null) && ((armorItem.getItem() instanceof IArmorApiarist));
  }
  
  public static boolean wearsLegs(EntityPlayer player)
  {
    ItemStack armorItem = player.inventory.armorInventory[1];
    return (armorItem != null) && ((armorItem.getItem() instanceof IArmorApiarist));
  }
  
  public static boolean wearsBoots(EntityPlayer player)
  {
    ItemStack armorItem = player.inventory.armorInventory[0];
    return (armorItem != null) && ((armorItem.getItem() instanceof IArmorApiarist));
  }
  
  public static int wearsItems(EntityPlayer player)
  {
    int count = 0;
    if (wearsHelmet(player)) {
      count++;
    }
    if (wearsChest(player)) {
      count++;
    }
    if (wearsLegs(player)) {
      count++;
    }
    if (wearsBoots(player)) {
      count++;
    }
    return count;
  }
  
  public static class Birthday
  {
    int day;
    int month;
    String name;
    
    public boolean isToday()
    {
      return (Calendar.getInstance().get(5) == this.month) && (Calendar.getInstance().get(2) == this.day);
    }
    
    public String getName()
    {
      return this.name;
    }
    
    private Birthday(int day, int month, String name)
    {
      this.day = day;
      this.month = (month + 1);
      this.name = name;
    }
  }
  
  static
  {
    birthdays = new ArrayList();
    

    birthdays.add(new Birthday(3, 10, "Binnie", null));
  }
  
  public String getUnlocalizedName()
  {
    return getUID();
  }
}
