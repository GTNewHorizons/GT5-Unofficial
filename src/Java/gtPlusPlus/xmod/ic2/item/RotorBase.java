package gtPlusPlus.xmod.ic2.item;

import java.util.List;

import ic2.api.item.IKineticRotor;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.init.InternalName;
import ic2.core.item.resources.ItemWindRotor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class RotorBase extends ItemWindRotor {

	private final int				maxWindStrength;
	private final int				minWindStrength;
	private final int				radius;
	private final float				efficiency;
	private final ResourceLocation	renderTexture;
	private final boolean			water;

	public RotorBase(final InternalName internalName, final int Radius, final int durability, final float efficiency,
			final int minWindStrength, final int maxWindStrength, final ResourceLocation RenderTexture) {
		super(internalName, Radius, durability, efficiency, minWindStrength, maxWindStrength, RenderTexture);

		this.setMaxStackSize(1);
		this.setMaxDamage(durability);

		this.radius = Radius;
		this.efficiency = efficiency;
		this.renderTexture = RenderTexture;
		this.minWindStrength = minWindStrength;
		this.maxWindStrength = maxWindStrength;
		this.water = internalName != InternalName.itemwoodrotor;
	}

	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List info, final boolean b) {
		info.add(StatCollector.translateToLocalFormatted("ic2.itemrotor.wind.info", new Object[] {
				Integer.valueOf(this.minWindStrength), Integer.valueOf(this.maxWindStrength)
		}));
		IKineticRotor.GearboxType type = null;
		if (Minecraft.getMinecraft().currentScreen != null
				&& Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator) {
			type = IKineticRotor.GearboxType.WATER;
		}
		else if (Minecraft.getMinecraft().currentScreen != null
				&& Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator) {
			type = IKineticRotor.GearboxType.WIND;
		}
		if (type != null) {
			// info.add(StatCollector.translateToLocal("ic2.itemrotor.fitsin." +
			// isAcceptedType(itemStack, type)));
		}
	}

	@Override
	public int getDiameter(final ItemStack stack) {
		return this.radius;
	}

	@Override
	public float getEfficiency(final ItemStack stack) {
		return this.efficiency;
	}

	@Override
	public int getMaxWindStrength(final ItemStack stack) {
		return this.maxWindStrength;
	}

	@Override
	public int getMinWindStrength(final ItemStack stack) {
		return this.minWindStrength;
	}

	@Override
	public ResourceLocation getRotorRenderTexture(final ItemStack stack) {
		return this.renderTexture;
	}

	@Override
	public String getTextureFolder() {
		return "rotors";
	}

	@Override
	public boolean isAcceptedType(final ItemStack stack, final IKineticRotor.GearboxType type) {
		return type == IKineticRotor.GearboxType.WIND || this.water;
	}
}
