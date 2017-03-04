package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemTCShard extends Item{

	public final String unlocalName;
	public final String displayName;
	public final String descriptionString;
	public final String descriptionString2;
	public final int itemColour;

	public BaseItemTCShard(final String DisplayName, final int colour) {
		this(DisplayName, colour, "");
	}

	public BaseItemTCShard(final String DisplayName, final int colour, final String Description) {
		this(DisplayName, colour, "", Description);
	}

	public BaseItemTCShard(final String DisplayName, final int colour, final String Description, final String Description2) {
		this.unlocalName = "item"+Utils.sanitizeString(DisplayName);
		this.displayName = DisplayName;
		this.itemColour = colour;
		this.descriptionString = Description;
		this.descriptionString2 = Description2;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemShard");
		GameRegistry.registerItem(this, this.unlocalName);
		GT_OreDictUnificator.registerOre("shard"+DisplayName, ItemUtils.getSimpleStack(this));
		GT_OreDictUnificator.registerOre("gemInfused"+DisplayName, ItemUtils.getSimpleStack(this));
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return (this.displayName+" Shard");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if ((this.descriptionString != "") || !this.descriptionString.equals("")){
			list.add(EnumChatFormatting.GRAY+this.descriptionString);
		}
		if ((this.descriptionString2 != "") || !this.descriptionString2.equals("")){
			list.add(EnumChatFormatting.GRAY+this.descriptionString2);
		}
	}


	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.itemColour;
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {

	}


}


