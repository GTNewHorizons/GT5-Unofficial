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

public class SandstoneHammer extends BaseItemWithDamageValue{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getColorFromItemStack(net.minecraft.item.ItemStack, int)
	 */
	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		//Figure Out Damage


		return super.getColorFromItemStack(stack, HEX_OxFFFFFF);
	}

	public SandstoneHammer(String unlocalizedName) {
		super(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(2500);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"Allows you to craft sand from cobble, or sandstone from sand.");
		super.addInformation(stack, aPlayer, list, bool);
	}	

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack)
	{
		return true;
	}
	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		itemStack.setItemDamage(itemStack.getItemDamage() + 8);

		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return false;
	}


}
