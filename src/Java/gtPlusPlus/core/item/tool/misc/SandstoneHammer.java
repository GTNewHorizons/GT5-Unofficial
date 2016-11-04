package gtPlusPlus.core.item.tool.misc;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class SandstoneHammer extends BaseItemWithDamageValue {

	public SandstoneHammer(final String unlocalizedName) {
		super(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(2500);
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GRAY + "Allows you to craft sand from cobble, or sandstone from sand.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(final ItemStack itemStack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.item.Item#getColorFromItemStack(net.minecraft.item.
	 * ItemStack, int)
	 */
	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		// Figure Out Damage

		return super.getColorFromItemStack(stack, HEX_OxFFFFFF);
	}

	@Override
	public ItemStack getContainerItem(final ItemStack itemStack) {
		itemStack.setItemDamage(itemStack.getItemDamage() + 8);

		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		return EnumRarity.uncommon;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public boolean hasContainerItem(final ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return false;
	}

}
