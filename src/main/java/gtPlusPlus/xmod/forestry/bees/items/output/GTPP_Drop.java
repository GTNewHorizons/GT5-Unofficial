package gtPlusPlus.xmod.forestry.bees.items.output;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT.STANDALONE;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

public class GTPP_Drop extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public GTPP_Drop() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gtpp.drop");
		GameRegistry.registerItem(this, "gtpp.drop", CORE.MODID);
	}

	public ItemStack getStackForType(GTPP_DropType type) {
		return new ItemStack(this, 1, type.mID);
	}

	public ItemStack getStackForType(GTPP_DropType type, int count) {
		return new ItemStack(this, count, type.mID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (GTPP_DropType type : GTPP_DropType.values()) {
			if (type.mShowInList) {
				list.add(this.getStackForType(type));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getRenderPasses(int meta) {
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("forestry:honeyDrop.0");
		this.secondIcon = par1IconRegister.registerIcon("forestry:honeyDrop.1");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return (pass == 0) ? itemIcon : secondIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		int colour = GTPP_DropType.get(stack.getItemDamage()).getColours()[0];

		if (pass >= 1) {
			colour = GTPP_DropType.get(stack.getItemDamage()).getColours()[1];
		}

		return colour;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return GTPP_DropType.get(stack.getItemDamage()).getName();
	}
	public void initDropsRecipes() {
		ItemStack tDrop;

		tDrop = getStackForType(GTPP_DropType.DRAGONBLOOD);
		addProcessHV(tDrop, new FluidStack(STANDALONE.DRAGON_METAL.getFluid(), 4), GT_Values.NI, 1000);
	}

	public void addProcessLV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aEUt) {
		GT_Values.RA.addFluidExtractionRecipe(tDrop, aOutput2, aOutput, aChance, 32, aEUt);
	}
	public void addProcessLV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aDuration, int aEUt) {
		GT_Values.RA.addFluidExtractionRecipe(tDrop, aOutput2, aOutput, aChance, aDuration, aEUt);
	}
	public void addProcessMV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aEUt) {
		GT_Values.RA.addFluidExtractionRecipe(tDrop, aOutput2, aOutput, aChance, 128, aEUt);
	}
	public void addProcessHV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance) {
		GT_Values.RA.addFluidExtractionRecipe(tDrop, aOutput2, aOutput, aChance, 480, 480);
	}
}
