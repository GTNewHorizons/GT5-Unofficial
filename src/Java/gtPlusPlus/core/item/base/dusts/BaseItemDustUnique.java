package gtPlusPlus.core.item.base.dusts;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
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

public class BaseItemDustUnique extends Item {

	protected int		colour;
	protected String	materialName;
	protected String	pileType;
	String				name	= "";

	protected final int sRadiation;

	public BaseItemDustUnique(final String unlocalizedName, final String materialName, final int colour,
			final String pileSize) {
		this.setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);
		if (pileSize == "dust" || pileSize == "Dust") {
			this.setTextureName(CORE.MODID + ":" + "dust");
		}
		else {
			this.setTextureName(CORE.MODID + ":" + "dust" + pileSize);
		}
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.colour = colour;
		this.materialName = materialName;
		this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
		GameRegistry.registerItem(this, unlocalizedName);

		String temp = "";
		Utils.LOG_WARNING("Unlocalized name for OreDict nameGen: " + this.getUnlocalizedName());
		if (this.getUnlocalizedName().contains("item.")) {
			temp = this.getUnlocalizedName().replace("item.", "");
			Utils.LOG_WARNING("Generating OreDict Name: " + temp);
		}
		else {
			temp = this.getUnlocalizedName();
		}
		if (temp.contains("DustTiny")) {
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: " + temp);
		}
		else if (temp.contains("DustSmall")) {
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: " + temp);
		}
		else {
			temp = temp.replace("itemD", "d");
			Utils.LOG_WARNING("Generating OreDict Name: " + temp);
		}
		if (temp != null && temp != "") {
			GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
		}
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		// if (pileType != null && materialName != null && pileType != "" &&
		// materialName != "" && !pileType.equals("") &&
		// !materialName.equals("")){
		if (this.getUnlocalizedName().contains("DustTiny")) {
			list.add(EnumChatFormatting.GRAY + "A tiny pile of " + this.materialName + " dust.");
		}
		else if (this.getUnlocalizedName().contains("DustSmall")) {
			list.add(EnumChatFormatting.GRAY + "A small pile of " + this.materialName + " dust.");
		}
		else {
			list.add(EnumChatFormatting.GRAY + "A pile of " + this.materialName + " dust.");
		}
		if (this.sRadiation > 0) {
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		// }
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0) {
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {

		if (this.getUnlocalizedName().contains("DustTiny")) {
			this.name = "Tiny Pile of " + this.materialName + " Dust";
		}
		else if (this.getUnlocalizedName().contains("DustSmall")) {
			this.name = "Small Pile of " + this.materialName + " Dust";
		}
		else {
			this.name = this.materialName + " Dust";
		}
		return this.name;
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
