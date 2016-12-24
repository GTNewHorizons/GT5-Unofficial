package gtPlusPlus.core.item.base;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreItem extends Item
{

	private final EnumRarity rarity;
	private final EnumChatFormatting descColour;
	private final String itemDescription;
	private String itemName;
	private final boolean hasEffect;

	//Replace Item - What does this item turn into when held.
	private final ItemStack turnsInto;

	//0
	/*
	 * Name, Tab - 64 Stack, 0 Dmg
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab)
	{
		this(unlocalizedName, creativeTab, 64, 0); //Calls 3
	}

	//0
	/*
	 * Name, Tab - 64 Stack, 0 Dmg
	 */
	public CoreItem(String unlocalizedName, String displayName, CreativeTabs creativeTab)
	{
		this(unlocalizedName, creativeTab, 64, 0); //Calls 3
		itemName = displayName;
	}

	//0.1
		/*
		 * Name, Tab - 64 Stack, 0 Dmg
		 */
		public CoreItem(String unlocalizedName, CreativeTabs creativeTab, ItemStack OverrideItem)
		{
			this(unlocalizedName, creativeTab, 64, 0, "This item will be replaced by another when helf by a player, it is old and should not be used in recipes.", EnumRarity.uncommon, EnumChatFormatting.UNDERLINE, false, OverrideItem); //Calls 5
		}
	//0.1
	/*
	 * Name, Tab - 64 Stack, 0 Dmg
	 */
	public CoreItem(String unlocalizedName, String displayName, CreativeTabs creativeTab, ItemStack OverrideItem)
	{
		this(unlocalizedName, creativeTab, 64, 0, "This item will be replaced by another when helf by a player, it is old and should not be used in recipes.", EnumRarity.uncommon, EnumChatFormatting.UNDERLINE, false, OverrideItem); //Calls 5
		itemName = displayName;
	}

	//1
	/*
	 * Name, Tab, Stack - 0 Dmg
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize)
	{
		this(unlocalizedName, creativeTab, stackSize, 0); //Calls 3
	}
	//2
	/*
	 * Name, Tab, Stack, Description - 0 Dmg
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, String description)
	{
		this(unlocalizedName, creativeTab, stackSize, 0, description); //Calls 4
	}	
	//3
	/*
	 * Name, Tab, Stack, Dmg - Description
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg)
	{
		this(unlocalizedName, creativeTab, stackSize, maxDmg, ""); //Calls 4
	}
	//4 //Not Rare + basic tooltip
	/*
	 * Name, Tab, Stack, Dmg, Description
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String description)
	{
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, EnumChatFormatting.GRAY, false, null); //Calls 4.5
	}
	//4.5
	/*
	 * Name, Tab, Stack, Dmg, Description, Text Colour - Common
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String description, EnumChatFormatting colour)
	{
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, EnumRarity.common, colour, false, null); //Calls 5
	}

	//4.75
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity - Gray text
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String description, EnumRarity rarity)
	{
		this(unlocalizedName, creativeTab, stackSize, maxDmg, description, rarity, EnumChatFormatting.GRAY, false, null); //Calls 5
	}

	//5	
	/*
	 * Name, Tab, Stack, Dmg, Description, Rarity, Text Colour, Effect
	 */
	public CoreItem(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg, String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, ItemStack OverrideItem)
	{
		setUnlocalizedName(unlocalizedName);
		setTextureName(CORE.MODID + ":" + unlocalizedName);
		setCreativeTab(creativeTab);
		setMaxStackSize(stackSize);
		setMaxDamage(maxDmg);
		this.rarity = regRarity;
		this.itemDescription = description;
		this.descColour = colour;
		this.hasEffect = Effect;
		this.turnsInto = OverrideItem;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(descColour+itemDescription);
		//super.addInformation(stack, aPlayer, list, bool);
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return rarity;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return hasEffect;
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		if (turnsInto != null){
			if (entityHolding instanceof EntityPlayer){

				Utils.LOG_INFO("Replacing "+iStack.getDisplayName()+" with "+turnsInto.getDisplayName()+".");
				ItemStack tempTransform = turnsInto;
				if (iStack.stackSize == 64){
					tempTransform.stackSize=64;
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
					for (int l=0;l<64;l++){
						((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
					}

				}
				else {
					tempTransform.stackSize=1;
					((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
					((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack tItem) {
		if (itemName == null || itemName.equals("") || itemName == "")
		return super.getItemStackDisplayName(tItem);
		return itemName;
	}
}