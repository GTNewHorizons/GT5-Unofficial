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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

public class BaseItemDamageable extends Item {

	private final EnumRarity rarity;
	private final String itemDescription;
	protected String itemName;
	private final boolean hasEffect;

	public BaseItemDamageable(final String unlocalizedName, final CreativeTabs creativeTab, final int stackSize, final int maxDmg, final String description, final EnumRarity regRarity, final EnumChatFormatting colour, final boolean Effect, final ItemStack OverrideItem)
	{
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(1);
		this.setMaxDamage(251);
		this.setNoRepair();
		this.rarity = regRarity;
		this.itemDescription = description;
		this.hasEffect = Effect;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	public String getItemDescription(){
		return this.itemDescription;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		int dmg = (int) getItemDamage(stack);
		if (dmg <= 3){
			list.add(EnumChatFormatting.GRAY+this.itemDescription);
		}
		if (dmg > 3 && dmg <= 25){
			list.add(EnumChatFormatting.GRAY+"You have discovered that smashing this against valuable stones has some function..");
		}
		else if (dmg > 0){
			int maxDamage = 250;
			list.add(EnumChatFormatting.GRAY+""+(maxDamage-getItemDamage(stack))+"/"+maxDamage+" gems remaining.");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		int dmg = (int) getItemDamage(par1ItemStack);
		if (dmg > 200){
			return EnumRarity.epic;
		}
		return this.rarity;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		int dmg = (int) getItemDamage(par1ItemStack);
		if (dmg > 200){
			return true;
		}
		return this.hasEffect;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		if ((this.itemName == null) || this.itemName.equals("")) {
			return super.getItemStackDisplayName(tItem);
		}
		return this.itemName;
	}

	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Value", 0);
		tagMain.setTag("Damage", tagNBT);
		rStack.setTagCompound(tagMain);		
		return true;
	}

	public static final long getItemDamage(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("Damage");
			if (aNBT != null) {
				return aNBT.getLong("Value");
			}
		}
		else {
			createNBT(aStack);
		}
		return 0L;
	}

	public static final boolean setItemDamage(final ItemStack aStack, final long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("Damage");
			if (aNBT != null) {
				aNBT.setLong("Value", aDamage);
				return true;
			}
		}
		else {
			createNBT(aStack);
		}
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getTagCompound() == null){
			createNBT(stack);
		}
		double currentDamage = getItemDamage(stack);
		double durabilitypercent = currentDamage / 100;	
		double inverse = (100-durabilitypercent);
		return  durabilitypercent;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		int dmg = (int) getItemDamage(stack);
		if (dmg <= 20){
			return false;			
		}
		else {
			return true;			
		}
	}

	public static ItemStack damageItem(ItemStack item){
		if (item != null){
			long currentUse = BaseItemDamageable.getItemDamage(item);
			if (currentUse >= 0 && currentUse <= 250){			
				BaseItemDamageable.setItemDamage(item, currentUse+1);
				return item;				
			}	
			else {	
				return item;	
			}	
		}
		return null;
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack) {
		Logger.INFO("Does Leave Table? "+stack.getDisplayName());
		return true;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public boolean hasContainerItem() {
		return true;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		Logger.INFO("hasContainerItem? "+stack.getDisplayName());
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack stack = itemStack.copy();
		//stack.setItemDamage(stack.getItemDamage() + 1);
		damageItem(stack);
		stack.stackSize = 1;
		return stack;
	}

	@Override
	public int getDamage(ItemStack stack) {
		return (int) getItemDamage(stack);
	}

}



