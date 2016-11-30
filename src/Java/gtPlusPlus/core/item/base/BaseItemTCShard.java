package gtPlusPlus.core.item.base;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemTCShard extends Item{

	public final String unlocalName;
	public final String displayName;
	public final String descriptionString;
	public final String descriptionString2;
	public final int itemColour;
	
	public BaseItemTCShard(String DisplayName, int colour) {
		this(DisplayName, colour, "");
	}
	
	public BaseItemTCShard(String DisplayName, int colour, String Description) {
		this(DisplayName, colour, "", Description);
	}

	public BaseItemTCShard(String DisplayName, int colour, String Description, String Description2) {
		this.unlocalName = "item"+Utils.sanitizeString(DisplayName);
		this.displayName = DisplayName;
		this.itemColour = colour;
		this.descriptionString = Description;
		this.descriptionString2 = Description2;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemShard");
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre("shard"+DisplayName, ItemUtils.getSimpleStack(this));
		GT_OreDictUnificator.registerOre("gemInfused"+DisplayName, ItemUtils.getSimpleStack(this));
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return (this.displayName+" Shard");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (descriptionString != "" || !descriptionString.equals("")){
			list.add(EnumChatFormatting.GRAY+descriptionString);
		}
		if (descriptionString2 != "" || !descriptionString2.equals("")){
			list.add(EnumChatFormatting.GRAY+descriptionString2);
		}
	}


	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return itemColour;
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {

	}


}


