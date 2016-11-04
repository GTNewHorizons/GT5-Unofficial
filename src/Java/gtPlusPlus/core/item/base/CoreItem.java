package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;

public class CoreItem extends Item {

	private final EnumRarity			rarity;
	private final EnumChatFormatting	descColour;
	private final String				itemDescription;
	private final boolean				hasEffect;

	// 0
	/*
	 * Name, Tab - 64 Stack, 0 Dmg
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab) {
		this(unlocalizedName, creativeTab, 64, 0); // Calls 3
	}

	// 1
	/*
	 * Name, Tab, Stack - 0 Dmg
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize) {
		this(unlocalizedName, creativeTab, stackSize, 0); // Calls 3
	}

	// 3
	/*
	 * Name, Tab, Stack, Dmg - Description
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize,
			final int maxDmg) {
		this(unlocalizedName, creativeTab, stackSize, maxDmg, ""); // Calls 4
	}

	// 4 //Not Rare + basic tooltip
	/*
	 * Name, Tab, Stack, Dmg, Description
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
			final String description) {
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, EnumChatFormatting.GRAY,
				false); // Calls 4.5
	}

	// 4.5
	/*
	 * Name, Tab, Stack, Dmg, Description, Text Colour - Common
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
			final String description, final EnumChatFormatting colour) {
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, colour, false); // Calls
																												// 5
	}

	// 4.75
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity - Gray text
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
			final String description, final EnumRarity rarity) {
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, rarity, EnumChatFormatting.GRAY, false); // Calls
																													// 5
	}

	// 5
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg,
			final String description, final EnumRarity regRarity, final EnumChatFormatting colour,
			final boolean Effect) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(stackSize);
		this.setMaxDamage(maxDmg);
		this.rarity = regRarity;
		this.itemDescription = description;
		this.descColour = colour;
		this.hasEffect = Effect;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	// 2
	/*
	 * Name, Tab, Stack, Description - 0 Dmg
	 */
	public CoreItem(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize,
			final String description) {
		this(unlocalizedName, creativeTab, stackSize, 0, description); // Calls
																		// 4
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(this.descColour + this.itemDescription);
		// super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		return this.rarity;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return this.hasEffect;
	}
}