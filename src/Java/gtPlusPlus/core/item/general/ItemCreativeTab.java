package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemCreativeTab extends Item {
	
	public IIcon[] icons = new IIcon[10];

	public ItemCreativeTab() {
		super();
		this.setHasSubtypes(true);
		String unlocalizedName = "itemCreativeTabs";
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(GregTech_API.TAB_GREGTECH);
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

}
