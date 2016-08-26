package miscutil.xmod.ic2.item;

import ic2.api.item.IKineticRotor;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class RotorIridium extends RotorBase{

	private final int maxWindStrength;
	private final int minWindStrength;
	private final int radius;
	private final float efficiency;
	private final ResourceLocation renderTexture;
	private final boolean water;	

	public RotorIridium(InternalName internalName, int Radius, int durability, float efficiency, int minWindStrength, int maxWindStrength, ResourceLocation RenderTexture)
	{
		super(internalName, Radius, durability, efficiency, minWindStrength, maxWindStrength, RenderTexture);

		
		
		setMaxStackSize(1);
		setMaxDamage(Integer.MAX_VALUE);

		this.radius = Radius;
		this.efficiency = efficiency;
		this.renderTexture = RenderTexture;
		this.minWindStrength = minWindStrength;
		this.maxWindStrength = maxWindStrength;
		this.water = (internalName != InternalName.itemwoodrotor);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add(StatCollector.translateToLocalFormatted("ic2.itemrotor.wind.info", new Object[] { Integer.valueOf(this.minWindStrength), Integer.valueOf(this.maxWindStrength) }));
		IKineticRotor.GearboxType type = null;
		if ((Minecraft.getMinecraft().currentScreen != null) && ((Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator))) {
			type = IKineticRotor.GearboxType.WATER;
		} else if ((Minecraft.getMinecraft().currentScreen != null) && ((Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator))) {
			type = IKineticRotor.GearboxType.WIND;
		}
		if (type != null) {
			//info.add(StatCollector.translateToLocal("ic2.itemrotor.fitsin." + isAcceptedType(itemStack, type)));
		}
	}

	@Override
	public String getTextureFolder()
	  {
	    return "rotors";
	  }
	  
	  @Override
	public int getDiameter(ItemStack stack)
	  {
	    return this.radius;
	  }
	  
	  @Override
	public ResourceLocation getRotorRenderTexture(ItemStack stack)
	  {
	    return this.renderTexture;
	  }
	  
	  @Override
	public float getEfficiency(ItemStack stack)
	  {
	    return this.efficiency;
	  }
	  
	  @Override
	public int getMinWindStrength(ItemStack stack)
	  {
	    return this.minWindStrength;
	  }
	  
	  @Override
	public int getMaxWindStrength(ItemStack stack)
	  {
	    return this.maxWindStrength;
	  }
	  
	  public boolean isAcceptedType(ItemStack stack, IKineticRotor.GearboxType type)
	  {
	    return (type == IKineticRotor.GearboxType.WIND) || (this.water);
	  }
	
	    
	    @Override
	  public void setCustomDamage(ItemStack stack, int damage)
	    {
	      NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
	      nbt.setInteger("advDmg", 0);
	      
	      int maxStackDamage = stack.getMaxDamage();
	      if (maxStackDamage > 2) {
	        //stack.setItemDamage(1 + (int)Util.map(damage, this.maxDmg, maxStackDamage - 2));
	      }
	    }
	    
	    @Override
	  public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src)
	    {
	      setCustomDamage(stack, getCustomDamage(stack) + damage);
	      return true;
	    }
	}
	
