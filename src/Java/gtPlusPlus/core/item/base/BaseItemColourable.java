package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.lib.CORE;

public class BaseItemColourable extends Item
{

	private final EnumRarity rarity;
	private final EnumChatFormatting descColour;
	private final String itemDescription;
	protected String itemName;
	private final boolean hasEffect;
	public final int componentColour;

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.componentColour;
	}
	
	//5
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
	 */
	public BaseItemColourable(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg, final String description, final EnumRarity regRarity, final EnumChatFormatting colour, final boolean Effect, int rgb)
	{
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(stackSize);
		this.setMaxDamage(maxDmg);
		this.rarity = regRarity;
		this.itemDescription = description;
		this.descColour = colour;
		this.hasEffect = Effect;
		this.componentColour = rgb;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	//6
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
	 */
	public BaseItemColourable(final String unlocalizedName, final String displayName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg, final String description, final EnumRarity regRarity, final EnumChatFormatting colour, final boolean Effect, int rgb)
	{
		this.setUnlocalizedName(unlocalizedName);
		this.itemName = displayName;
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(stackSize);
		this.setMaxDamage(maxDmg);
		this.rarity = regRarity;
		this.itemDescription = description;
		this.descColour = colour;
		this.hasEffect = Effect;
		this.componentColour = rgb;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(this.descColour+this.itemDescription);
		//super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return this.rarity;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return this.hasEffect;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		if ((this.itemName == null) || this.itemName.equals("")) {
			return super.getItemStackDisplayName(tItem);
		}
		return this.itemName;
	}
}