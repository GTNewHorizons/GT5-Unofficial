package gtPlusPlus.core.item.general;

import java.util.List;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemLavaFilter extends CoreItem {

	public IIcon[] mIcon = new IIcon[1];
	
	public ItemLavaFilter() {
		super("itemLavaFilter", AddToCreativeTab.tabMachines, 1, 100, "Lava Filter", EnumRarity.common,
				EnumChatFormatting.BLACK, false, null);
		setGregtechItemList();
	}

	private final boolean setGregtechItemList() {
		ItemList.Component_LavaFilter.set(this);
		return ItemList.Component_LavaFilter.get(1) != null ? true : false;
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		this.mIcon[0] = reg.registerIcon(CORE.MODID + ":" + "itemLavaFilter");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.mIcon[0];
	}

	@Override
	public String getItemStackDisplayName(ItemStack tItem) {
		return "Lava Filter";
	}
	
	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Damage", 0);
		tagMain.setTag("LavaFilter", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}
	
	public static final long getFilterDamage(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("LavaFilter");
			if (aNBT != null) {
				return aNBT.getLong("Damage");
			}
		}
		else {
		createNBT(aStack);
		}
		return 0L;
	}

	public static final boolean setFilterDamage(final ItemStack aStack, final long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("LavaFilter");
			if (aNBT != null) {
				aNBT.setLong("Damage", aDamage);
				return true;
			}
		}
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getTagCompound() == null){
			createNBT(stack);
        }
		double currentDamage = getFilterDamage(stack);
		double durabilitypercent = currentDamage / 100;		
        return  durabilitypercent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"Filters Lava within a Thermal Boiler.");
		EnumChatFormatting durability = EnumChatFormatting.GRAY;
		if (100-getFilterDamage(stack) > 80){
			durability = EnumChatFormatting.GRAY;
		}
		else if (100-getFilterDamage(stack) > 60){
			durability = EnumChatFormatting.GREEN;
		}
		else if (100-getFilterDamage(stack) > 40){
			durability = EnumChatFormatting.YELLOW;
		}
		else if (100-getFilterDamage(stack) > 20){
			durability = EnumChatFormatting.GOLD;
		}
		else if (100-getFilterDamage(stack) > 0){
			durability = EnumChatFormatting.RED;
		}
		list.add(durability+""+(100-getFilterDamage(stack))+EnumChatFormatting.GRAY+" / "+100);
		super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}


}
