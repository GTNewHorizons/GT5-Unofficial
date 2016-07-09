package miscutil.core.xmod.ic2.item;

import ic2.api.item.IKineticRotor;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class RotorCustom extends IC2_ItemGradualInteger implements IKineticRotor
{
	private final int maxWindStrength;
	private final int minWindStrength;
	private final int radius;
	private final float efficiency;
	private final ResourceLocation renderTexture;
	private final boolean water;
	private final int durability;

	public RotorCustom(String internalName, int Radius, int durability, float efficiency, int minWindStrength, int maxWindStrength, ResourceLocation RenderTexture)
	{
		super(internalName, durability);

		setMaxStackSize(1);
		setMaxDamage(durability);

		this.radius = Radius;
		this.efficiency = efficiency;
		this.renderTexture = RenderTexture;
		this.minWindStrength = minWindStrength;
		this.maxWindStrength = maxWindStrength;
		this.durability = durability;
		this.water = (internalName != "itemwoodrotor");
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add(StatCollector.translateToLocalFormatted("ic2.itemrotor.wind.info", new Object[] { Integer.valueOf(this.minWindStrength), Integer.valueOf(this.maxWindStrength) }));
		
		/*IKineticRotor.GearboxType type = null;
		if ((Minecraft.getMinecraft().currentScreen != null) && ((Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator))) {
			type = IKineticRotor.GearboxType.WATER;
		} else if ((Minecraft.getMinecraft().currentScreen != null) && ((Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator))) {
			type = IKineticRotor.GearboxType.WIND;
		}
		if (type != null) {
			info.add(StatCollector.translateToLocal("ic2.itemrotor.fitsin." + isAcceptedType(itemStack, type)));
		}*/
	}

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

	@Override
	public boolean isAcceptedType(ItemStack stack, IKineticRotor.GearboxType type)
	{
		return true;
	}
}
