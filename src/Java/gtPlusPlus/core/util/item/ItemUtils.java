package gtPlusPlus.core.util.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BasicSpawnEgg;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemCentidust;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemDecidust;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.wrapper.var;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils {

	public static void addItemToOreDictionary(final ItemStack stack, final String oreDictName) {
		try {
			GT_OreDictUnificator.registerOre(oreDictName, stack);
		}
		catch (final NullPointerException e) {
			Utils.LOG_ERROR(stack.getDisplayName() + " not registered. [NULL]");
		}
	}

	public static BaseItemCentidust generateCentidust(final Material material) {
		if (material.getDust(1) != null && MaterialUtils.hasValidRGBA(material.getRGBA())) {
			final BaseItemCentidust Centidust = new BaseItemCentidust(material);
			return Centidust;
		}
		return null;
	}

	public static BaseItemCentidust generateCentidust(final Materials material) {
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null) {
			final Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null) {
				ItemUtils.generateCentidust(placeholder);
			}
		}
		return null;
	}

	public static BaseItemDecidust generateDecidust(final Material material) {
		if (material.getDust(1) != null && MaterialUtils.hasValidRGBA(material.getRGBA())) {
			final BaseItemDecidust Decidust = new BaseItemDecidust(material);
			return Decidust;
		}
		return null;
	}

	public static BaseItemDecidust generateDecidust(final Materials material) {
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null) {
			final Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null) {
				ItemUtils.generateDecidust(placeholder);
			}
		}
		return null;
	}

	public static Item[] generateDusts(final String unlocalizedName, final String materialName, final int materialTier,
			final Material matInfo, final int Colour) {
		final int radioactive = ItemUtils.getRadioactivityLevel(materialName);
		final Item[] output = {
				new BaseItemDust("itemDust" + unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier,
						radioactive),
				new BaseItemDust("itemDustSmall" + unlocalizedName, materialName, matInfo, Colour, "Small",
						materialTier, radioactive),
				new BaseItemDust("itemDustTiny" + unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier,
						radioactive)
		};
		return output;
	}

	public static MultiPickaxeBase generateMultiPick(final boolean GT_Durability, final Materials material) {
		final ToolMaterial customMaterial = Utils.generateMaterialFromGT(material);
		Utils.LOG_WARNING("Generating a Multi-Pick out of " + material.name());
		short[] rgb;
		rgb = material.getRGBA();
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for " + material.name() + " is " + dur);
		if (GT_Durability) {
			dur = material.mDurability * 100;
			Utils.LOG_WARNING("Using gregtech durability value, " + material.name() + " is now " + dur + ".");
		}
		else if (dur <= 0) {
			dur = material.mDurability;
			Utils.LOG_WARNING("Determined durability too low, " + material.name() + " is now " + dur
					+ " based on the GT material durability.");
		}

		if (dur <= 0) {
			Utils.LOG_WARNING("Still too low, " + material.name() + " will now go unused.");
			return null;
		}

		final MultiPickaxeBase MP_Redstone = new MultiPickaxeBase(material.name() + " Multipick", customMaterial, dur,
				Utils.rgbtoHexValue(rgb[0], rgb[1], rgb[2]));

		if (MP_Redstone.isValid) {
			return MP_Redstone;
		}
		return null;

	}

	public static MultiSpadeBase generateMultiShovel(final boolean GT_Durability, final Materials material) {
		final ToolMaterial customMaterial = Utils.generateMaterialFromGT(material);
		Utils.LOG_WARNING("Generating a Multi-Shovel out of " + material.name());
		short[] rgb;
		rgb = material.getRGBA();
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for " + material.name() + " is " + dur);
		if (GT_Durability) {
			dur = material.mDurability * 100;
			Utils.LOG_WARNING("Using gregtech durability value, " + material.name() + " is now " + dur + ".");
		}
		else if (dur <= 0) {
			dur = material.mDurability;
			Utils.LOG_WARNING("Determined durability too low, " + material.name() + " is now " + dur
					+ " based on the GT material durability.");
		}

		if (dur <= 0) {
			Utils.LOG_WARNING("Still too low, " + material.name() + " will now go unused.");
			return null;
		}

		final MultiSpadeBase MP_Redstone = new MultiSpadeBase(material.name() + " Multishovel", customMaterial, dur,
				Utils.rgbtoHexValue(rgb[0], rgb[1], rgb[2]));

		if (MP_Redstone.isValid) {
			return MP_Redstone;
		}
		return null;

	}

	public static void generateSpawnEgg(final String entityModID, final String parSpawnName, final int colourEgg,
			final int colourOverlay) {
		final Item itemSpawnEgg = new BasicSpawnEgg(entityModID, parSpawnName, colourEgg, colourOverlay)
				.setUnlocalizedName("spawn_egg_" + parSpawnName.toLowerCase())
				.setTextureName(CORE.MODID + ":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawnEgg" + parSpawnName);
	}

	public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName,
			final int Colour) {
		final Item[] output = {
				new BaseItemDustUnique("itemDust" + unlocalizedName, materialName, Colour, "Dust"),
				new BaseItemDustUnique("itemDustSmall" + unlocalizedName, materialName, Colour, "Small"),
				new BaseItemDustUnique("itemDustTiny" + unlocalizedName, materialName, Colour, "Tiny")
		};
		return output;
	}

	public static String getArrayStackNames(final ItemStack[] aStack) {
		String itemNames = "Item Array: ";
		for (final ItemStack alph : aStack) {

			if (alph != null) {
				final String temp = itemNames;
				itemNames = temp + ", " + alph.getDisplayName() + " x" + alph.stackSize;
			}
			else {
				final String temp = itemNames;
				itemNames = temp + ", " + "null" + " x" + "0";
			}
		}
		return itemNames;

	}

	public static String[] getArrayStackNamesAsArray(final ItemStack[] aStack) {
		final String[] itemNames = {};
		int arpos = 0;
		for (final ItemStack alph : aStack) {
			itemNames[arpos] = alph.getDisplayName();
			arpos++;
		}
		return itemNames;

	}

	public static ItemStack getCorrectStacktype(final Object item_Input, final int stackSize) {
		if (item_Input instanceof String) {
			return ItemUtils.getCorrectStacktype(item_Input, stackSize);
		}
		else if (item_Input instanceof ItemStack) {
			return (ItemStack) item_Input;
		}
		if (item_Input instanceof var) {
			return ((var) item_Input).getStack(stackSize);
		}
		return null;
	}

	public static ItemStack getCorrectStacktype(final String fqrn, final int stackSize) {
		final String oreDict = "ore:";
		ItemStack temp;
		if (fqrn.toLowerCase().contains(oreDict.toLowerCase())) {
			final String sanitizedName = fqrn.replace(oreDict, "");
			temp = ItemUtils.getItemStack(sanitizedName, stackSize);
			return temp;
		}
		final String[] fqrnSplit = fqrn.split(":");
		if (fqrnSplit[2] == null) {
			fqrnSplit[2] = "0";
		}
		temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, fqrnSplit[1], Integer.parseInt(fqrnSplit[2]),
				stackSize);
		return temp;
	}

	// TODO
	/*
	 * public static FluidStack getFluidStack(Materials m, int Size) // fqrn =
	 * fully qualified resource name { String[] fqrnSplit = fqrn.split(":");
	 * 
	 * FluidStack x = (FluidStack) "Materials."+m+".getFluid"(Size);
	 * 
	 * return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size); }
	 */

	public static String getFluidArrayStackNames(final FluidStack[] aStack) {
		String itemNames = "Fluid Array: ";
		for (final FluidStack alph : aStack) {
			final String temp = itemNames;
			itemNames = temp + ", " + alph.getFluid().getName() + " x" + alph.amount;
		}
		return itemNames;

	}

	public static ItemStack getGregtechCircuit(final int Meta) {
		return ItemUtils.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.integrated_circuit", "Gregtech Circuit",
				Meta, 0);
	}

	public static ItemStack getIC2Cell(final int meta) {
		final ItemStack temp = GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, meta);
		return temp != null ? temp : null;
	}

	public static ItemStack getIC2Cell(final String S) {
		final ItemStack moreTemp = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + S, 1);

		if (moreTemp == null) {
			final int cellID = 0;
			final ItemStack temp = GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, cellID);
			return temp != null ? temp : null;
		}

		return moreTemp;
	}

	public static Item getItem(final String fqrn) // fqrn = fully qualified
													// resource name
	{
		final String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItem(fqrnSplit[0], fqrnSplit[1]);
	}

	public static void getItemForOreDict(final String FQRN, final String oreDictName, final String itemName,
			final int meta) {
		try {
			Item em = null;
			final Item em1 = ItemUtils.getItem(FQRN);
			Utils.LOG_WARNING("Found: " + em1.getUnlocalizedName() + ":" + meta);
			if (em1 != null) {
				em = em1;
			}
			if (em != null) {

				final ItemStack metaStack = new ItemStack(em, 1, meta);
				GT_OreDictUnificator.registerOre(oreDictName, metaStack);

				/*
				 * ItemStack itemStackWithMeta = new ItemStack(em,1,meta);
				 * GT_OreDictUnificator.registerOre(oreDictName, new
				 * ItemStack(itemStackWithMeta.getItem()));
				 */
			}
		}
		catch (final NullPointerException e) {
			Utils.LOG_ERROR(itemName + " not found. [NULL]");
		}
	}

	public static ItemStack getItemStack(final String fqrn, final int Size) // fqrn
																			// =
																			// fully
																			// qualified
																			// resource
																			// name
	{
		final String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
	}

	public static ItemStack getItemStackOfAmountFromOreDict(final String oredictName, final int amount) {
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		if (!oreDictList.isEmpty()) {
			final ItemStack returnValue = oreDictList.get(0).copy();
			returnValue.stackSize = amount;
			return returnValue;
		}
		return ItemUtils.getSimpleStack(ModItems.AAA_Broken, amount);
	}

	public static ItemStack getItemStackOfAmountFromOreDictNoBroken(final String oredictName, final int amount) {
		final ItemStack returnValue = ItemUtils.getItemStackOfAmountFromOreDict(oredictName, amount);

		if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass()
				|| returnValue.getItem() != ModItems.AAA_Broken) {
			return returnValue;
		}
		Utils.LOG_INFO(oredictName + " was not valid.");
		return null;
	}

	public static ItemStack getItemStackOfAmountFromOreDictNoBrokenExcluding(final String excludeModName,
			final String oredictName, final int amount) {
		ItemStack returnValue = ItemUtils.getItemStackOfAmountFromOreDict(oredictName, amount);

		if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass()
				|| returnValue.getItem() != ModItems.AAA_Broken) {
			if (returnValue.getClass().toString().toLowerCase().contains(excludeModName.toLowerCase())) {
				final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
				if (!oreDictList.isEmpty()) {
					returnValue = oreDictList.get(1).copy();
					returnValue.stackSize = amount;
					return returnValue;
				}
			}
			else {
				final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
				if (!oreDictList.isEmpty()) {
					returnValue = oreDictList.get(1).copy();
					returnValue.stackSize = amount;
					return returnValue;
				}
			}
			return returnValue;
		}
		Utils.LOG_INFO(oredictName + " was not valid.");
		return null;
	}

	@SuppressWarnings("unused")
	public static ItemStack getItemStackWithMeta(final boolean MOD, final String FQRN, final String itemName,
			final int meta, final int itemstackSize) {
		if (MOD) {
			try {
				Item em = null;
				final Item em1 = ItemUtils.getItem(FQRN);
				Utils.LOG_WARNING("Found: " + em1.getUnlocalizedName() + ":" + meta);
				if (em1 != null) {
					if (null == em) {
						em = em1;
					}
					if (em != null) {
						final ItemStack metaStack = new ItemStack(em, itemstackSize, meta);
						return metaStack;
					}
				}
				return null;
			}
			catch (final NullPointerException e) {
				Utils.LOG_ERROR(itemName + " not found. [NULL]");
				return null;
			}
		}
		return null;
	}

	public static int getRadioactivityLevel(final String materialName) {
		int sRadiation = 0;
		if (materialName.toLowerCase().contains("uranium")) {
			sRadiation = 2;
		}
		else if (materialName.toLowerCase().contains("plutonium")) {
			sRadiation = 4;
		}
		else if (materialName.toLowerCase().contains("thorium")) {
			sRadiation = 1;
		}
		return sRadiation;
	}

	public static ItemStack getSimpleStack(final Item x) {
		return ItemUtils.getSimpleStack(x, 1);
	}

	public static ItemStack getSimpleStack(final Item x, final int i) {
		try {
			final ItemStack r = new ItemStack(x, i);
			return r;
		}
		catch (final Throwable e) {
			return null;
		}
	}

	public static ItemStack getSimpleStack(final ItemStack x, final int i) {
		try {
			final ItemStack r = x.copy();
			r.stackSize = i;
			return r;
		}
		catch (final Throwable e) {
			return null;
		}
	}

	public static boolean isRadioactive(final String materialName) {
		int sRadiation = 0;
		if (materialName.toLowerCase().contains("uranium")) {
			sRadiation = 2;
		}
		else if (materialName.toLowerCase().contains("plutonium")) {
			sRadiation = 4;
		}
		else if (materialName.toLowerCase().contains("thorium")) {
			sRadiation = 1;
		}
		if (sRadiation >= 1) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static ItemStack simpleMetaStack(final Item item, final int meta, final int itemstackSize) {
		try {
			Item em = item;
			final Item em1 = item;
			Utils.LOG_WARNING("Found: " + em1.getUnlocalizedName() + ":" + meta);
			if (em1 != null) {
				if (null == em) {
					em = em1;
				}
				if (em != null) {
					final ItemStack metaStack = new ItemStack(em, itemstackSize, meta);
					return metaStack;
				}
			}
			return null;
		}
		catch (final NullPointerException e) {
			Utils.LOG_ERROR(item.getUnlocalizedName() + " not found. [NULL]");
			return null;
		}
	}

	@SuppressWarnings("unused")
	public static ItemStack simpleMetaStack(final String FQRN, final int meta, final int itemstackSize) {
		try {
			Item em = null;
			final Item em1 = ItemUtils.getItem(FQRN);
			Utils.LOG_WARNING("Found: " + em1.getUnlocalizedName() + ":" + meta);
			if (em1 != null) {
				if (null == em) {
					em = em1;
				}
				if (em != null) {
					final ItemStack metaStack = new ItemStack(em, itemstackSize, meta);
					return metaStack;
				}
			}
			return null;
		}
		catch (final NullPointerException e) {
			Utils.LOG_ERROR(FQRN + " not found. [NULL]");
			return null;
		}
	}

	public static ItemStack[] validItemsForOreDict(final String oredictName) {
		final List<?> validNames = MaterialUtils.oreDictValuesForEntry(oredictName);
		final ItemStack[] inputs = null;
		for (int i = 0; i < validNames.size(); i++) {
			inputs[i] = (ItemStack) validNames.get(i);
		}
		return inputs;
	}

}
