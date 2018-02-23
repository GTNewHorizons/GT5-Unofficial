package gtPlusPlus.core.item.general;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemAirFilter extends Item {

	public IIcon[] icons = new IIcon[1];

	public ItemAirFilter() {
		super();
		this.setHasSubtypes(true);
		String unlocalizedName = "itemAirFilter";
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		this.icons[0] = reg.registerIcon(CORE.MODID + ":" + "itemAirFilter");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[0];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 2; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String itemName = tItem == null ? "Air Filter" : super.getItemStackDisplayName(tItem);
		String suffixName = "";
		if (tItem.getItemDamage() == 0){
			suffixName = " [Tier 1]";
		}
		else if (tItem.getItemDamage() == 1){
			suffixName = " [Tier 2]";
		}
		return (itemName+suffixName);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
		int meta = stack.getItemDamage();
		if (meta == 1){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(150,180,35);
		}
		return HEX_OxFFFFFF;
	}
	
	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Damage", 0);
		tagMain.setTag("AirFilter", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}
	
	public static final long getFilterDamage(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("AirFilter");
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
			aNBT = aNBT.getCompoundTag("AirFilter");
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
		double meta = stack.getItemDamage() == 0 ? 50 : 2500;
		double durabilitypercent = currentDamage / meta;		
        return  durabilitypercent;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"An Air filter for Atmospheric Reconditioning.");
		int maxDamage = (stack.getItemDamage() == 0 ? 50 : 2500);
		list.add(EnumChatFormatting.GRAY+""+(maxDamage-getFilterDamage(stack))+"/"+maxDamage+" uses left.");
		super.addInformation(stack, player, list, bool);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

}