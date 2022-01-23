package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.GT_Values.L;
import static gregtech.api.enums.GT_Values.NF;
import static gregtech.api.enums.GT_Values.NI;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.GT_Values.V;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GTPP_Comb extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public GTPP_Comb() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gtpp.comb");
		GameRegistry.registerItem(this, "gtpp.comb", CORE.MODID);
	}

	public ItemStack getStackForType(GTPP_CombType type) {
		return new ItemStack(this, 1, type.mID);
	}

	public ItemStack getStackForType(GTPP_CombType type, int count) {
		return new ItemStack(this, count, type.mID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (GTPP_CombType type : GTPP_CombType.values()) {
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
		int colour = GTPP_CombType.get(stack.getItemDamage()).getColours()[0];

		if (pass >= 1) {
			colour = GTPP_CombType.get(stack.getItemDamage()).getColours()[1];
		}

		return colour;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return GTPP_CombType.get(stack.getItemDamage()).getName();
	}

	public void initCombsRecipes() {
		addCentrifugeToItemStack(GTPP_CombType.DRAGONBLOOD, new ItemStack[]{GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.DRAGONBLOOD), GTPP_Bees.drop.getStackForType(GTPP_DropType.DRAGONBLOOD)}, new int[]{30
				* 100, (int) (7.5 * 100), 20 * 100}, Voltage.IV);
	}

	/**
	 * Currently use for STEEL, GOLD, MOLYBDENUM, PLUTONIUM
	 **/
	public void addChemicalProcess(GTPP_CombType comb, Materials aInMaterial, Materials aOutMaterial, Voltage volt) {
		if (GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4) == NI)
			return;
		RA.addChemicalRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), volt.getComplexChemical(), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), NI, volt.getComplexTime(), volt.getChemicalEnergy(), volt.compareTo(Voltage.IV) > 0);
	}

	/**
	 * Currently only used for GTPP_CombType.MOLYBDENUM
	 * 
	 * @param circuitNumber
	 *            should not conflict with addProcessGT
	 *
	 **/
	public void addAutoclaveProcess(GTPP_CombType comb, Materials aMaterial, Voltage volt, int circuitNumber) {
		if (GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4) == NI)
			return;
		RA.addAutoclaveRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_Utility.getIntegratedCircuit(circuitNumber), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()
				+ volt.getUUAmplifier())
				/ 10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 10000, (int) (aMaterial.getMass() * 128), volt.getAutoClaveEnergy(), volt.compareTo(Voltage.HV) > 0);
	}

	/**
	 * this only adds Chemical and AutoClave process. If you need Centrifuge
	 * recipe. use addCentrifugeToMaterial or addCentrifugeToItemStack
	 * 
	 * @param volt
	 *            This determine the required Tier of process for this recipes.
	 *            This decide the required aEU/t, progress time, required
	 *            additional UU-Matter, requirement of cleanRoom, needed fluid
	 *            stack for Chemical.
	 * @param aMaterial
	 *            result of Material that should be generated by this process.
	 **/
	public void addProcessGT(GTPP_CombType comb, Materials[] aMaterial, Voltage volt) {
		ItemStack tComb = getStackForType(comb);
		for (int i = 0; i < aMaterial.length; i++) {
			if (GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4) != NI) {
				RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial[i], 1), volt.getComplexChemical(), aMaterial[i].mOreByProducts.isEmpty() ? null : aMaterial[i].mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4), NI, volt.getComplexTime(), volt.getChemicalEnergy(), volt.compareTo(Voltage.IV) > 0);
				RA.addAutoclaveRecipe(GT_Utility.copyAmount(9, tComb), GT_Utility.getIntegratedCircuit(i + 1), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial[i].getMass() + volt.getUUAmplifier())
						/ 10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4), 10000, (int) (aMaterial[i].getMass()
								* 128), volt.getAutoClaveEnergy(), volt.compareTo(Voltage.HV) > 0);
			}
		}
	}

	/**
	 * this method only adds Centrifuge based on Material. If volt is lower than
	 * MV than it will also adds forestry centrifuge recipe.
	 * 
	 * @param comb
	 *            BeeComb
	 * @param aMaterial
	 *            resulting Material of processing. can be more than 6. but over
	 *            6 will be ignored in Gregtech Centrifuge.
	 * @param chance
	 *            chance to get result, 10000 == 100%
	 * @param volt
	 *            required Voltage Tier for this recipe, this also affect the
	 *            duration, amount of UU-Matter, and needed liquid type and
	 *            amount for chemical reactor
	 * @param stackSize
	 *            This parameter can be null, in that case stack size will be
	 *            just 1. This handle the stackSize of the resulting Item, and
	 *            Also the Type of Item. if this value is multiple of 9, than
	 *            related Material output will be dust, if this value is
	 *            multiple of 4 than output will be Small dust, else the output
	 *            will be Tiny dust
	 * @param beeWax
	 *            if this is null, than the comb will product default Bee wax.
	 *            But if aMaterial is more than 5, beeWax will be ignored in
	 *            Gregtech Centrifuge.
	 * @param waxChance
	 *            have same format like "chance"
	 **/
	public void addCentrifugeToMaterial(GTPP_CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, ItemStack beeWax, int waxChance) {
		addCentrifugeToMaterial(comb, aMaterial, chance, stackSize, volt, volt.getSimpleTime(), beeWax, waxChance);
	}
	public void addCentrifugeToMaterial(GTPP_CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, int duration, ItemStack beeWax, int waxChance) {
		ItemStack[] aOutPut = new ItemStack[aMaterial.length + 1];
		stackSize = Arrays.copyOf(stackSize, aMaterial.length);
		chance = Arrays.copyOf(chance, aOutPut.length);
		chance[chance.length - 1] = waxChance;
		for (int i = 0; i < (aMaterial.length); i++) {
			if (chance[i] == 0) {
				continue;
			}
			if (Math.max(1, stackSize[i]) % 9 == 0) {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial[i], (Math.max(1, stackSize[i]) / 9));
			}
			else if (Math.max(1, stackSize[i]) % 4 == 0) {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial[i], (Math.max(1, stackSize[i]) / 4));
			}
			else {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial[i], Math.max(1, stackSize[i]));
			}
		}
		if (beeWax != NI) {
			aOutPut[aOutPut.length - 1] = beeWax;
		}
		else {
			aOutPut[aOutPut.length - 1] = ItemList.FR_Wax.get(1);
		}

		addCentrifugeToItemStack(comb, aOutPut, chance, volt, duration);
	}

	/**
	 * @param volt
	 *            required Tier of system. If it's lower than MV, it will also
	 *            add forestry centrifuge.
	 * @param aItem
	 *            can be more than 6. but Over 6 will be ignored in Gregtech
	 *            Centrifuge.
	 **/
	public void addCentrifugeToItemStack(GTPP_CombType comb, ItemStack[] aItem, int[] chance, Voltage volt) {
		addCentrifugeToItemStack(comb, aItem, chance, volt, volt.getSimpleTime());
	}
	public void addCentrifugeToItemStack(GTPP_CombType comb, ItemStack[] aItem, int[] chance, Voltage volt, int duration) {
		ItemStack tComb = getStackForType(comb);
		Builder<ItemStack, Float> Product = new ImmutableMap.Builder<ItemStack, Float>();
		for (int i = 0; i < aItem.length; i++) {
			if (aItem[i] == NI) {
				continue;
			}
			Product.put(aItem[i], chance[i] / 10000.0f);
		}

		if (volt.compareTo(Voltage.MV) < 0 || !GT_Mod.gregtechproxy.mNerfedCombs) {
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, Product.build());
		}

		aItem = Arrays.copyOf(aItem, 6);
		if (aItem.length > 6) {
			chance = Arrays.copyOf(chance, 6);
		}

		RA.addCentrifugeRecipe(tComb, NI, NF, NF, aItem[0], aItem[1], aItem[2], aItem[3], aItem[4], aItem[5], chance, duration, volt.getSimpleEnergy());
	}

	enum Voltage {
		ULV, LV, MV, HV, EV, IV, LUV, ZPM, UV, UHV, UEV, UIV, UMV, UXV, OpV, MAX;
		public int getVoltage() {
			return (int) V[this.ordinal()];
		}
		/**
		 * @return aEU/t needed for chemical and autoclave process related to
		 *         the Tier
		 **/
		public int getChemicalEnergy() {
			return this.getVoltage() * 3 / 4;
		}
		public int getAutoClaveEnergy() {
			return (int) ((this.getVoltage() * 3 / 4) * (Math.max(1, Math.pow(2, 5 - this.ordinal()))));
		}
		/**
		 * @return FluidStack needed for chemical process related to the Tier
		 **/
		public FluidStack getComplexChemical() {
			if (this.compareTo(Voltage.MV) < 0) {
				return Materials.Water.getFluid((this.compareTo(Voltage.ULV) > 0) ? 1000 : 500);
			}
			else if (this.compareTo(Voltage.HV) < 0) {
				return GT_ModHandler.getDistilledWater(1000L);
			}
			else if (this.compareTo(Voltage.LUV) < 0) {
				return Materials.Mercury.getFluid((long) (Math.pow(2, this.compareTo(Voltage.HV)) * L));
			}
			else if (this.compareTo(Voltage.UHV) < 0) {
				return FluidRegistry.getFluidStack("mutagen", (int) (Math.pow(2, this.compareTo(Voltage.LUV)) * L));
			}
			else {
				return NF;
			}
		}
		/**
		 * @return additional required UU-Matter amount for Autoclave process
		 *         related to the Tier
		 **/
		public int getUUAmplifier() {
			return 9 * ((this.compareTo(Voltage.MV) < 0) ? 1 : this.compareTo(Voltage.MV));
		}
		/** @return duration needed for Chemical process related to the Tier **/
		public int getComplexTime() {
			return 64 + this.ordinal() * 32;
		}
		/**
		 * @return duration needed for Centrifuge process related to the Tier
		 **/
		public int getSimpleTime() {
			if (!GT_Mod.gregtechproxy.mNerfedCombs) {
				return 96 + this.ordinal() * 32;
			}
			else {
				// ULV, LV needs 128ticks, MV need 256 ticks, HV need 384 ticks,
				// EV need 512 ticks, IV need 640 ticks
				return 128 * (Math.max(1, this.ordinal()));
			}
		}
		/** @return aEU/t needed for Centrifuge process related to the Tier **/
		public int getSimpleEnergy() {
			if (this == Voltage.ULV) {
				return 5;
			}
			else {
				return (int) (this.getVoltage() / 16) * 15;
			}
		}
	}
}
