package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import gtPlusPlus.core.lib.CORE;

public class ItemControlCore extends Item {

	public IIcon[] icons = new IIcon[10];

	public ItemControlCore() {
		super();
		this.setHasSubtypes(true);
		String unlocalizedName = "itemControlCore";
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(GregTech_API.TAB_GREGTECH);
		//this.setCreativeTab(AddToCreativeTab.tabMisc);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		this.icons[0] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_0");
		this.icons[1] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_1");
		this.icons[2] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_2");
		this.icons[3] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_3");
		this.icons[4] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_4");
		this.icons[5] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_5");
		this.icons[6] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_6");
		this.icons[7] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_7");
		this.icons[8] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_8");
		this.icons[9] = reg.registerIcon(CORE.MODID + ":" + "controlcore/Core_9");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 10; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GRAY+"Allows a Multiblock to function upto "+GT_Values.VN[stack.getItemDamage()]+"");
		list.add(EnumChatFormatting.GRAY+"Required Tier is determined by the sum of the eu/t of all Energy Inputs");
		list.add(EnumChatFormatting.GRAY+"Lower tiers may be used to underclock, which is useful in some situations");
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String aReturnValue = super.getItemStackDisplayName(tItem);
		if (tItem != null) {
			try {
			if (aReturnValue != null) {
				if (aReturnValue.toLowerCase().contains(".name")) {
					aReturnValue = "Control Core";
				}
				else {
					return aReturnValue;
				}
			}
			}
			catch (Throwable t) {}
		}
		if (aReturnValue == null || !aReturnValue.toLowerCase().contains("control core") || aReturnValue.length() <= 0) {
			aReturnValue =  "Error";
		}		
		String suffixName = "";
		if (tItem.getItemDamage() == 0){
			suffixName = " [ULV]";
		}
		else if (tItem.getItemDamage() == 1){
			suffixName = " [LV]";
		}
		else if (tItem.getItemDamage() == 2){
			suffixName = " [MV]";
		}
		else if (tItem.getItemDamage() == 3){
			suffixName = " [HV]";
		}
		else if (tItem.getItemDamage() == 4){
			suffixName = " [EV]";
		}
		else if (tItem.getItemDamage() == 5){
			suffixName = " [IV]";
		}
		else if (tItem.getItemDamage() == 6){
			suffixName = " [LuV]";
		}
		else if (tItem.getItemDamage() == 7){
			suffixName = " [ZPM]";
		}
		else if (tItem.getItemDamage() == 8){
			suffixName = " [UV]";
		}
		else if (tItem.getItemDamage() == 9){
			suffixName = " [MAX]";
		}
		return (aReturnValue+suffixName);		
	}

}