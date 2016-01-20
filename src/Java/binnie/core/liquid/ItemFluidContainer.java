package binnie.core.liquid;

import binnie.Binnie;
import binnie.extratrees.alcohol.AlcoholEffect;
import binnie.extratrees.alcohol.drink.DrinkManager;
import binnie.extratrees.alcohol.drink.IDrinkLiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ItemFluidContainer
  extends ItemFood
{
  private FluidContainer container;
  public static int LiquidExtraBee = 64;
  public static int LiquidExtraTree = 128;
  public static int LiquidJuice = 256;
  public static int LiquidAlcohol = 384;
  public static int LiquidSpirit = 512;
  public static int LiquidLiqueuer = 640;
  public static int LiquidGenetics = 768;
  private static Map<Integer, String> idToFluid = new HashMap();
  private static Map<String, Integer> fluidToID = new HashMap();
  
  public static void registerFluid(IFluidType fluid, int id)
  {
    idToFluid.put(Integer.valueOf(id), fluid.getIdentifier().toLowerCase());
    fluidToID.put(fluid.getIdentifier().toLowerCase(), Integer.valueOf(id));
  }
  
  public ItemFluidContainer(FluidContainer container)
  {
    super(0, false);
    this.container = container;
    container.item = this;
    this.maxStackSize = container.getMaxStackSize();
    setHasSubtypes(true);
    setUnlocalizedName("container" + container.name());
    setCreativeTab(CreativeTabs.tabMaterials);
  }
  
  private FluidStack getLiquid(ItemStack stack)
  {
    String liquid = (String)idToFluid.get(Integer.valueOf(stack.getItemDamage()));
    return liquid == null ? null : Binnie.Liquid.getLiquidStack(liquid, 1000);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.container.updateIcons(register);
  }
  
  public String getItemStackDisplayName(ItemStack itemstack)
  {
    if (itemstack == null) {
      return "???";
    }
    FluidStack fluid = getLiquid(itemstack);
    if (fluid == null) {
      return "Missing Fluid";
    }
    return fluid.getFluid().getLocalizedName(fluid) + " " + this.container.getName();
  }
  
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList)
  {
    for (IFluidType liquid : Binnie.Liquid.fluids.values()) {
      if ((liquid.canPlaceIn(this.container)) && 
      
        (liquid.showInCreative(this.container))) {
        itemList.add(getContainer(liquid));
      }
    }
  }
  
  public ItemStack getContainer(IFluidType liquid)
  {
    int id = ((Integer)fluidToID.get(liquid.getIdentifier().toLowerCase())).intValue();
    ItemStack itemstack = new ItemStack(this, 1, id);
    return itemstack;
  }
  
  public IIcon getIcon(ItemStack itemstack, int j)
  {
    if (j > 0) {
      return this.container.getBottleIcon();
    }
    return this.container.getContentsIcon();
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack item, int pass)
  {
    FluidStack fluid = getLiquid(item);
    if (fluid == null) {
      return 16777215;
    }
    if ((pass == 0) && ((fluid.getFluid() instanceof BinnieFluid))) {
      return ((BinnieFluid)fluid.getFluid()).fluidType.getContainerColour();
    }
    return super.getColorFromItemStack(item, pass);
  }
  
  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }
  
  public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
  {
    player.getFoodStats().func_151686_a(this, stack);
    world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    onFoodEaten(stack, world, player);
    return this.container.getEmpty();
  }
  
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
  {
    if (!world.isRemote)
    {
      FluidStack fluid = getLiquid(stack);
      IDrinkLiquid liquid = DrinkManager.getLiquid(fluid);
      if (liquid != null) {
        AlcoholEffect.makeDrunk(player, liquid.getABV() * fluid.amount);
      }
    }
  }
  
  public int getMaxItemUseDuration(ItemStack stack)
  {
    return 32;
  }
  
  public EnumAction getItemUseAction(ItemStack stack)
  {
    return isDrinkable(stack) ? EnumAction.drink : EnumAction.none;
  }
  
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    if (isDrinkable(stack)) {
      player.setItemInUse(stack, getMaxItemUseDuration(stack));
    }
    return stack;
  }
  
  public int func_150905_g(ItemStack p_150905_1_)
  {
    return 0;
  }
  
  public float func_150906_h(ItemStack p_150906_1_)
  {
    return 0.0F;
  }
  
  private boolean isDrinkable(ItemStack stack)
  {
    FluidStack fluid = getLiquid(stack);
    IDrinkLiquid liquid = DrinkManager.getLiquid(fluid);
    return liquid == null ? false : liquid.isConsumable();
  }
}
