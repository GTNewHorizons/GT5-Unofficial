package gtPlusPlus.core.item.base.ingots;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemIngot extends Item {

	protected int		colour;
	protected String	materialName;
	protected String	unlocalName;

	protected final int sRadiation;

	public BaseItemIngot(final String unlocalizedName, final String materialName, final int colour,
			final int sRadioactivity) {
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemIngot");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);
		String temp = "";
		if (this.unlocalName.contains("itemIngot")) {
			temp = this.unlocalName.replace("itemI", "i");
		}
		else if (this.unlocalName.contains("itemHotIngot")) {
			temp = this.unlocalName.replace("itemHotIngot", "ingotHot");
		}
		if (temp != null && temp != "") {
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}
		this.generateCompressorRecipe();
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.materialName != null && this.materialName != "" && !this.materialName.equals("")
				&& !this.unlocalName.contains("HotIngot")) {
			list.add(EnumChatFormatting.GRAY + "A solid ingot of " + this.materialName + ".");
		}
		else if (this.materialName != null && this.materialName != "" && !this.materialName.equals("")
				&& this.unlocalName.toLowerCase().contains("ingothot")) {
			list.add(EnumChatFormatting.GRAY + "Warning: " + EnumChatFormatting.RED + "Very hot! "
					+ EnumChatFormatting.GRAY + " Avoid direct handling..");
		}
		if (this.sRadiation > 0) {
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	private void generateCompressorRecipe() {
		if (this.unlocalName.contains("itemIngot")) {
			final ItemStack tempStack = ItemUtils.getSimpleStack(this, 9);
			ItemStack tempOutput = null;
			String temp = this.getUnlocalizedName().replace("item.itemIngot", "block");
			Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: " + this.getUnlocalizedName());
			if (this.getUnlocalizedName().contains("item.")) {
				temp = this.getUnlocalizedName().replace("item.", "");
				Utils.LOG_WARNING("Generating OreDict Name: " + temp);
			}
			temp = temp.replace("itemIngot", "block");
			Utils.LOG_WARNING("Generating OreDict Name: " + temp);
			if (temp != null && temp != "") {
				tempOutput = ItemUtils.getItemStackOfAmountFromOreDict(temp, 1);
				if (tempOutput != null) {
					GT_ModHandler.addCompressionRecipe(tempStack, tempOutput);
				}

			}
		}
		else if (this.unlocalName.contains("itemHotIngot")) {
			return;
		}

	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0) {
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return this.materialName + " Ingot";
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
			final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(this.sRadiation, world, entityHolding);
	}
}
