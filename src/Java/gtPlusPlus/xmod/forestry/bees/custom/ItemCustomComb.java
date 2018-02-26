package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemCustomComb extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public ItemCustomComb() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gtpp.comb");
		GameRegistry.registerItem(this, "gtpp.comb", CORE.MODID);
	}

	public ItemStack getStackForType(CustomCombs type) {
		return new ItemStack(this, 1, type.ordinal());
	}

	public ItemStack getStackForType(CustomCombs type, int count) {
		return new ItemStack(this, count, type.ordinal());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (CustomCombs type : CustomCombs.values()) {
			if (type.showInList) {
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
		this.itemIcon = par1IconRegister.registerIcon("forestry:beeCombs.0");
		this.secondIcon = par1IconRegister.registerIcon("forestry:beeCombs.1");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return (pass == 0) ? itemIcon : secondIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		int meta = Math.max(0, Math.min(CustomCombs.values().length - 1, stack.getItemDamage()));
		int colour = CustomCombs.values()[meta].getColours()[0];

		if (pass >= 1) {
			colour = CustomCombs.values()[meta].getColours()[1];
		}

		return colour;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return CustomCombs.values()[stack.getItemDamage()].getName();
	}

	public void initCombsRecipes() {
		ItemStack tComb;
	    
		tComb = getStackForType(CustomCombs.SILICON);
		addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Silicon, 1), 30);
		//addProcess(tComb, Materials.Silver, 100);
		//addProcess(tComb, Materials.Galena, 100);

		//Rubbers
		tComb = getStackForType(CustomCombs.RUBBER);
		addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Rubber, 1), 30);
		tComb = getStackForType(CustomCombs.PLASTIC);
		addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plastic, 1), 20);
		tComb = getStackForType(CustomCombs.PTFE);
		addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, GTPP_Bees.PTFE, 1), 10);
		tComb = getStackForType(CustomCombs.PBS);
		addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, GTPP_Bees.PBS, 1), 5);
		
		
		//Fuels
		tComb = getStackForType(CustomCombs.BIOMASS);
		addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropBiomassBlob), 5);
		tComb = getStackForType(CustomCombs.PBS);
		addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropEthanolBlob), 5);

		//Misc Materials
		tComb = getStackForType(CustomCombs.FORCE);
		addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropForceGem), 5);
		tComb = getStackForType(CustomCombs.FLUORINE);
		addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropFluorineBlob), 5);
		tComb = getStackForType(CustomCombs.NIKOLITE);
		addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropNikoliteDust), 5);
		
	}
	public void addSpecialCent(ItemStack tComb, ItemStack aOutput, int chance){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
	}
	
	public void addSpecialCent(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), aOutput2, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000, chance2 * 100 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f,aOutput2,chance2 * 0.01f));
	}
	
	public void addSpecialCent(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2, ItemStack aOutput3, int chance3){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), aOutput2, aOutput3, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000, chance2 * 100, chance3*100 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f,aOutput2,chance2 * 0.01f,aOutput3,chance3*0.01f));
	}
	
	public void addProcess(ItemStack tComb, Materials aMaterial, int chance){
		if(tryGetNerfBoolean()){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 128, 5);
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1), chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
		}
	}
	
	public void addProcess(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(tryGetNerfBoolean()){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), Materials.Water.getFluid(1000), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 96);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 384);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 128, 5);
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1), chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
		}
	}
	
	private static boolean tryGetNerfBoolean(){
		try {
			Class mProxy = Class.forName("gregtech.GT_Mod.gregtechproxy");
			Field mNerf = FieldUtils.getDeclaredField(mProxy, "mNerfedCombs", true);
			boolean returnValue = (boolean) mNerf.get(GT_Mod.gregtechproxy);
			return returnValue;
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			return false;
		}		
	}
	
}