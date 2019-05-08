package gregtech.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.enums.GT_Values;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static gregtech.api.enums.GT_Values.MOD_ID;

public class ItemPropolis extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public ItemPropolis() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gt.propolis");
		GameRegistry.registerItem(this, "gt.propolis", MOD_ID);
	}

	public ItemStack getStackForType(PropolisType type) {
		return new ItemStack(this, 1, type.ordinal());
	}

	public ItemStack getStackForType(PropolisType type, int count) {
		return new ItemStack(this, count, type.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (PropolisType type : PropolisType.values()) {
			if (type.showInList) {
				list.add(this.getStackForType(type));
			}
		}
	}


	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("forestry:propolis.0");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		int meta = Math.max(0, Math.min(PropolisType.values().length - 1, stack.getItemDamage()));
		return PropolisType.values()[meta].getColours();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return PropolisType.values()[stack.getItemDamage()].getName();
	}
	public void initPropolisRecipes() {
		ItemStack tPropolis;

		tPropolis = getStackForType(PropolisType.Zoko);
		//addRecipe(tDrop, aOutput, aOutput2, aChance, aEUt);
	}

	public void addRecipe(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aEUt) {
		GT_Values.RA.addFluidExtractionRecipe(tDrop, aOutput2, aOutput, aChance, 40, aEUt);
	}

}
