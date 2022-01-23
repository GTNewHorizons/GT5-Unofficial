package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.GT_Values.MOD_ID_DC;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;

public class GTPP_Propolis extends Item {
	
	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public GTPP_Propolis() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gtpp.propolis");
		GameRegistry.registerItem(this, "gtpp.propolis", CORE.MODID);
	}

	public ItemStack getStackForType(GTPP_PropolisType type) {
		return new ItemStack(this, 1, type.ordinal());
	}

	public ItemStack getStackForType(GTPP_PropolisType type, int count) {
		return new ItemStack(this, count, type.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (GTPP_PropolisType type : GTPP_PropolisType.values()) {
			if (type.showInList) {
				list.add(this.getStackForType(type));
			}
		}
	}


	@Override
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
		int meta = Math.max(0, Math.min(GTPP_PropolisType.values().length - 1, stack.getItemDamage()));
		return GTPP_PropolisType.values()[meta].getColours();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return GTPP_PropolisType.values()[stack.getItemDamage()].getName();
	}
	public void initPropolisRecipes() {
		ItemStack tPropolis;

		tPropolis = getStackForType(GTPP_PropolisType.End);
		addProcessHV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "end_powder", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Stardust);
		addProcessHV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "stardust", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Ectoplasma);
		addProcessEV(tPropolis, GT_ModHandler.getModItem(MOD_ID_DC, "item.EctoplasmaChip", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Arcaneshard);
		addProcessEV(tPropolis, GT_ModHandler.getModItem(MOD_ID_DC, "item.ArcaneShardChip", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Dragonessence);
		addProcessIV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "essence", 16, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Enderman);
		addProcessIV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "enderman_head", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Silverfish);
		addProcessEV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "silverfish_blood", 1, 0));
		tPropolis = getStackForType(GTPP_PropolisType.Endium);
		addProcessHV(tPropolis, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.HeeEndium, 1));
		tPropolis = getStackForType(GTPP_PropolisType.Fireessence);
		addProcessIV(tPropolis, GT_ModHandler.getModItem("HardcoreEnderExpansion", "essence", 16, 1));

		//addRecipe(tDrop, aOutput, aOutput2, aChance, aDuration, aEUt);
	}

	public void addProcessHV(ItemStack tPropolis, ItemStack aOutput2) {
		GT_Values.RA.addFluidExtractionRecipe(tPropolis, aOutput2, FluidRegistry.getFluidStack("endergoo",100), 5000, 50, 480);
	}
	public void addProcessEV(ItemStack tPropolis, ItemStack aOutput2) {
		GT_Values.RA.addFluidExtractionRecipe(tPropolis, aOutput2, FluidRegistry.getFluidStack("endergoo",200), 2500, 100, 1920);
	}
	public void addProcessIV(ItemStack tPropolis, ItemStack aOutput2) {
		GT_Values.RA.addFluidExtractionRecipe(tPropolis, aOutput2, FluidRegistry.getFluidStack("endergoo",300), 1500, 150, 7680);
	}
}
