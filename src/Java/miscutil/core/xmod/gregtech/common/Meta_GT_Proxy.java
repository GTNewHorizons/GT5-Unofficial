package miscutil.core.xmod.gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy.OreDictEventContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.core.xmod.gregtech.api.objects.GregtechFluid;
import miscutil.core.xmod.gregtech.api.util.GregtechOreDictUnificator;
import miscutil.core.xmod.gregtech.api.util.GregtechRecipeRegistrator;
import miscutil.core.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Meta_GT_Proxy {

	//Silly Vars
	private static final Collection<String> mIgnoredItems = new HashSet(Arrays.asList(new String[]{"itemGhastTear", "itemFlint", "itemClay", "itemBucketSaltWater",
			"itemBucketFreshWater", "itemBucketWater", "itemRock", "itemReed", "itemArrow", "itemSaw", "itemKnife", "itemHammer", "itemChisel", "itemRubber",
			"itemEssence", "itemIlluminatedPanel", "itemSkull", "itemRawRubber", "itemBacon", "itemJetpackAccelerator", "itemLazurite", "itemIridium",
			"itemTear", "itemClaw", "itemFertilizer", "itemTar", "itemSlimeball", "itemCoke", "itemBeeswax", "itemBeeQueen", "itemForcicium", "itemForcillium",
			"itemRoyalJelly", "itemHoneydew", "itemHoney", "itemPollen", "itemReedTypha", "itemSulfuricAcid", "itemPotash", "itemCompressedCarbon",
			"itemBitumen", "itemBioFuel", "itemCokeSugar", "itemCokeCactus", "itemCharcoalSugar", "itemCharcoalCactus", "itemSludge", "itemEnrichedAlloy",
			"itemQuicksilver", "itemMercury", "itemOsmium", "itemUltimateCircuit", "itemEnergizedStar", "itemAntimatterMolecule", "itemAntimatterGlob",
			"itemCoal", "itemBoat", "itemHerbalMedicineCake", "itemCakeSponge", "itemFishandPumpkinCakeSponge", "itemSoulCleaver", "itemInstantCake",
			"itemWhippingCream", "itemGlisteningWhippingCream", "itemCleaver", "itemHerbalMedicineWhippingCream", "itemStrangeWhippingCream",
			"itemBlazeCleaver", "itemBakedCakeSponge", "itemMagmaCake", "itemGlisteningCake", "itemOgreCleaver", "itemFishandPumpkinCake",
			"itemMagmaWhippingCream", "itemMultimeter", "itemSuperconductor"}));
	private static final Collection<String> mIgnoredNames = new HashSet(Arrays.asList(new String[]{"grubBee", "chainLink", "candyCane", "bRedString", "bVial",
			"bFlask", "anorthositeSmooth", "migmatiteSmooth", "slateSmooth", "travertineSmooth", "limestoneSmooth", "orthogneissSmooth", "marbleSmooth",
			"honeyDrop", "lumpClay", "honeyEqualssugar", "flourEqualswheat", "bluestoneInsulated", "blockWaterstone", "blockSand", "blockTorch",
			"blockPumpkin", "blockClothRock", "blockStainedHardenedClay", "blockQuartzPillar", "blockQuartzChiselled", "blockSpawner", "blockCloth", "mobHead",
			"mobEgg", "enderFlower", "enderChest", "clayHardened", "dayGemMaterial", "nightGemMaterial", "snowLayer", "bPlaceholder", "hardenedClay",
			"eternalLifeEssence", "sandstone", "wheatRice", "transdimBlock", "bambooBasket", "lexicaBotania", "livingwoodTwig", "redstoneCrystal",
			"pestleAndMortar", "glowstone", "whiteStone", "stoneSlab", "transdimBlock", "clayBowl", "clayPlate", "ceramicBowl", "ceramicPlate", "ovenRack",
			"clayCup", "ceramicCup", "batteryBox", "transmutationStone", "torchRedstoneActive", "coal", "charcoal", "cloth", "cobblestoneSlab",
			"stoneBrickSlab", "cobblestoneWall", "stoneBrickWall", "cobblestoneStair", "stoneBrickStair", "blockCloud", "blockDirt", "blockTyrian",
			"blockCarpet", "blockFft", "blockLavastone", "blockHolystone", "blockConcrete", "sunnariumPart", "brSmallMachineCyaniteProcessor", "meteoriteCoal",
			"blockCobble", "pressOreProcessor", "crusherOreProcessor", "grinderOreProcessor", "blockRubber", "blockHoney", "blockHoneydew", "blockPeat",
			"blockRadioactive", "blockSlime", "blockCocoa", "blockSugarCane", "blockLeather", "blockClayBrick", "solarPanelHV", "cableRedNet", "stoneBowl",
			"crafterWood", "taintedSoil", "brickXyEngineering", "breederUranium", "wireMill", "chunkLazurite", "aluminumNatural", "aluminiumNatural",
			"naturalAluminum", "naturalAluminium", "antimatterMilligram", "antimatterGram", "strangeMatter", "coalGenerator", "electricFurnace",
			"unfinishedTank", "valvePart", "aquaRegia", "leatherSeal", "leatherSlimeSeal", "hambone", "slimeball", "clay", "enrichedUranium", "camoPaste",
			"antiBlock", "burntQuartz", "salmonRaw", "blockHopper", "blockEnderObsidian", "blockIcestone", "blockMagicWood", "blockEnderCore", "blockHeeEndium",
			"oreHeeEndPowder", "oreHeeStardust", "oreHeeIgneousRock", "oreHeeInstabilityOrb", "crystalPureFluix", "shardNether", "gemFluorite",
			"stickObsidian", "caveCrystal", "shardCrystal", "dyeCrystal","shardFire","shardWater","shardAir","shardEarth","ingotRefinedIron","blockMarble","ingotUnstable"}));
	private static final Collection<String> mInvalidNames = new HashSet(Arrays.asList(new String[]{"diamondShard", "redstoneRoot", "obsidianStick", "bloodstoneOre",
			"universalCable", "bronzeTube", "ironTube", "netherTube", "obbyTube", "infiniteBattery", "eliteBattery", "advancedBattery", "10kEUStore",
			"blueDye", "MonazitOre", "quartzCrystal", "whiteLuminiteCrystal", "darkStoneIngot", "invisiumIngot", "demoniteOrb", "enderGem", "starconiumGem",
			"osmoniumIngot", "tapaziteGem", "zectiumIngot", "foolsRubyGem", "rubyGem", "meteoriteGem", "adamiteShard", "sapphireGem", "copperIngot",
			"ironStick", "goldStick", "diamondStick", "reinforcedStick", "draconicStick", "emeraldStick", "copperStick", "tinStick", "silverStick",
			"bronzeStick", "steelStick", "leadStick", "manyullynStick", "arditeStick", "cobaltStick", "aluminiumStick", "alumiteStick", "oilsandsOre",
			"copperWire", "superconductorWire", "sulfuricAcid", "conveyorBelt", "ironWire", "aluminumWire", "aluminiumWire", "silverWire", "tinWire",
			"dustSiliconSmall", "AluminumOre", "plateHeavyT2", "blockWool", "alloyPlateEnergizedHardened", "gasWood", "alloyPlateEnergized", "SilverOre",
			"LeadOre", "TinOre", "CopperOre", "silverOre", "leadOre", "tinOre", "copperOre", "bauxiteOre", "HSLivingmetalIngot", "oilMoving", "oilStill",
			"oilBucket", "petroleumOre", "dieselFuel", "diamondNugget", "planks", "wood", "stick", "sticks", "naquadah", "obsidianRod", "stoneRod",
			"thaumiumRod", "steelRod", "netherrackRod", "woodRod", "ironRod", "cactusRod", "flintRod", "copperRod", "cobaltRod", "alumiteRod", "blueslimeRod",
			"arditeRod", "manyullynRod", "bronzeRod", "boneRod", "slimeRod", "redalloyBundled", "bluestoneBundled", "infusedteslatiteInsulated",
			"redalloyInsulated", "infusedteslatiteBundled"}));
	public static boolean mOreDictActivated = false;
	public static boolean mSortToTheEnd = true;
	public final static HashSet<ItemStack> mRegisteredOres = new HashSet(10000);
	public final static Collection<GregtechOreDictEventContainer> mEvents = new HashSet();

	public Meta_GT_Proxy() {	
		Utils.LOG_INFO("GT_PROXY - initialized.");
		for (String tOreName : OreDictionary.getOreNames()) {
			ItemStack tOreStack;
			for (Iterator i$ = OreDictionary.getOres(tOreName).iterator(); i$.hasNext(); registerOre(new OreDictionary.OreRegisterEvent(tOreName, tOreStack))) {
				tOreStack = (ItemStack) i$.next();
			}
		}
	}

	public static Fluid addFluid(String aName, String aLocalized, GT_Materials aMaterial, int aState, int aTemperatureK) {
		return addFluid(aName, aLocalized, aMaterial, aState, aTemperatureK, null, null, 0);
	}

	public static Fluid addFluid(String aName, String aLocalized, GT_Materials aMaterial, int aState, int aTemperatureK, ItemStack aFullContainer,
			ItemStack aEmptyContainer, int aFluidAmount) {
		return addFluid(aName, aName.toLowerCase(), aLocalized, aMaterial, null, aState, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount);
	}

	public static Fluid addFluid(String aName, String aTexture, String aLocalized, GT_Materials aMaterial, short[] aRGBa, int aState, int aTemperatureK,
			ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount) {
		aName = aName.toLowerCase();
		Fluid rFluid = new GregtechFluid(aName, aTexture, aRGBa != null ? aRGBa : Dyes._NULL.getRGBA());
		GT_LanguageManager.addStringLocalization(rFluid.getUnlocalizedName(), aLocalized == null ? aName : aLocalized);
		if (FluidRegistry.registerFluid(rFluid)) {
			switch (aState) {
			case 0:
				rFluid.setGaseous(false);
				rFluid.setViscosity(10000);
				break;
			case 1:
			case 4:
				rFluid.setGaseous(false);
				rFluid.setViscosity(1000);
				break;
			case 2:
				rFluid.setGaseous(true);
				rFluid.setDensity(-100);
				rFluid.setViscosity(200);
				break;
			case 3:
				rFluid.setGaseous(true);
				rFluid.setDensity(55536);
				rFluid.setViscosity(10);
				rFluid.setLuminosity(15);
			}
		} else {
			rFluid = FluidRegistry.getFluid(aName);
		}
		if (rFluid.getTemperature() == new Fluid("test").getTemperature()) {
			rFluid.setTemperature(aTemperatureK);
		}
		if (aMaterial != null) {
			switch (aState) {
			case 0:
				aMaterial.mSolid = rFluid;
				break;
			case 1:
				aMaterial.mFluid = rFluid;
				break;
			case 2:
				aMaterial.mGas = rFluid;
				break;
			case 3:
				aMaterial.mPlasma = rFluid;
				break;
			case 4:
				aMaterial.mStandardMoltenFluid = rFluid;
			}
		}
		if ((aFullContainer != null) && (aEmptyContainer != null)
				&& (!FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer))) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, GT_Utility.getContainerItem(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}
		return rFluid;
	}

	@SubscribeEvent
	public static void registerOre(OreDictionary.OreRegisterEvent aEvent) {
		ModContainer tContainer = Loader.instance().activeModContainer();
		String aMod = tContainer == null ? "UNKNOWN" : tContainer.getModId();
		String aOriginalMod = aMod;
		if (GregtechOreDictUnificator.isRegisteringOres()) {
			aMod = CORE.MODID;
		} else if (aMod.equals(CORE.MODID)) {
			aMod = "UNKNOWN";
		}
		if ((aEvent == null) || (aEvent.Ore == null) || (aEvent.Ore.getItem() == null) || (aEvent.Name == null) || (aEvent.Name.isEmpty())
				|| (aEvent.Name.replaceAll("_", "").length() - aEvent.Name.length() == 9)) {
			if (aOriginalMod.equals(CORE.MODID)) {
				aOriginalMod = "UNKNOWN";
			}
			GT_Log.ore
			.println(aOriginalMod
					+ " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
			throw new IllegalArgumentException(
					aOriginalMod
					+ " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
		}
		try {
			aEvent.Ore.stackSize = 1;

			String tModToName = aMod + " -> " + aEvent.Name;
			if ((mOreDictActivated) || (GregTech_API.sPostloadStarted) || ((mSortToTheEnd) && (GregTech_API.sLoadFinished))) {
				tModToName = aOriginalMod + " --Late--> " + aEvent.Name;
			}
			if (((aEvent.Ore.getItem() instanceof ItemBlock)) || (GT_Utility.getBlockFromStack(aEvent.Ore) != Blocks.air)) {
				GregtechOreDictUnificator.addToBlacklist(aEvent.Ore);
			}
			mRegisteredOres.add(aEvent.Ore);			
			if (mIgnoredNames.contains(aEvent.Name)) {
				GT_Log.ore.println(tModToName + " is getting ignored via hardcode.");
				return;
			}
			if ((aEvent.Name.contains("|")) || (aEvent.Name.contains("*")) || (aEvent.Name.contains(":")) || (aEvent.Name.contains("."))
					|| (aEvent.Name.contains("$"))) {
				GT_Log.ore.println(tModToName + " is using a private Prefix and is therefor getting ignored properly.");
				return;
			}
			if (aEvent.Name.contains(" ")) {
				GT_Log.ore.println(tModToName + " is getting re-registered because the OreDict Name containing invalid spaces.");
				GregtechOreDictUnificator.registerOre(aEvent.Name.replaceAll(" ", ""), GT_Utility.copyAmount(1L, new Object[]{aEvent.Ore}));
				aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
				return;
			}
			if (mInvalidNames.contains(aEvent.Name)) {
				GT_Log.ore.println(tModToName + " is wrongly registered and therefor getting ignored.");

				return;
			}
			GregtechOrePrefixes aPrefix = GregtechOrePrefixes.getOrePrefix(aEvent.Name);
			GT_Materials aMaterial = GT_Materials._NULL;			
			if (aPrefix == null) {
				if (aEvent.Name.toLowerCase().equals(aEvent.Name)) {
					GT_Log.ore.println(tModToName + " is invalid due to being solely lowercased.");
					return;
				}
				if (aEvent.Name.toUpperCase().equals(aEvent.Name)) {
					GT_Log.ore.println(tModToName + " is invalid due to being solely uppercased.");
					return;
				}
				if (Character.isUpperCase(aEvent.Name.charAt(0))) {
					GT_Log.ore.println(tModToName + " is invalid due to the first character being uppercased.");
				}
			} else {
				if (aPrefix.mDontUnificateActively) {
					GregtechOreDictUnificator.addToBlacklist(aEvent.Ore);
				}
				if (aPrefix != aPrefix.mPrefixInto) {
					String tNewName = aEvent.Name.replaceFirst(aPrefix.toString(), aPrefix.mPrefixInto.toString());
					if (!GregtechOreDictUnificator.isRegisteringOres()) {
						GT_Log.ore.println(tModToName + " uses a depricated Prefix, and is getting re-registered as " + tNewName);
					}
					GregtechOreDictUnificator.registerOre(tNewName, aEvent.Ore);
					return;
				}
				String tName = aEvent.Name.replaceFirst(aPrefix.toString(), "");
				if (tName.length() > 0) {
					char firstChar = tName.charAt(0);
					if (Character.isUpperCase(firstChar) || Character.isLowerCase(firstChar) || firstChar == '_') {
						if (aPrefix.mIsMaterialBased) {
							aMaterial = GT_Materials.get(tName);
							if (aMaterial != aMaterial.mMaterialInto) {
								GregtechOreDictUnificator.registerOre(aPrefix, aMaterial.mMaterialInto, aEvent.Ore);
								if (!GregtechOreDictUnificator.isRegisteringOres()) {
									GT_Log.ore.println(tModToName + " uses a deprecated Material and is getting re-registered as "
											+ aPrefix.get(aMaterial.mMaterialInto));
								}
								return;
							}
							if (!aPrefix.isIgnored(aMaterial)) {
								aPrefix.add(GT_Utility.copyAmount(1L, new Object[]{aEvent.Ore}));
							}
							if (aMaterial != GT_Materials._NULL) {
								GT_Materials tReRegisteredMaterial;
								for (Iterator i$ = aMaterial.mOreReRegistrations.iterator(); i$.hasNext(); GregtechOreDictUnificator.registerOre(aPrefix,
										tReRegisteredMaterial, aEvent.Ore)) {
									tReRegisteredMaterial = (GT_Materials) i$.next();
								}
								aMaterial.add(GT_Utility.copyAmount(1L, new Object[]{aEvent.Ore}));								
								switch (aPrefix) {								
								case gem:								
									break;
								case plate:
									break;								
								case stick:
									if (!GregtechRecipeRegistrator.sRodMaterialList.contains(aMaterial)) {
										GregtechRecipeRegistrator.sRodMaterialList.add(aMaterial);
									}
									break;
								case dust:
									break;
								case ingot:								
									break;
								}
								if (aPrefix.mIsUnificatable && !aMaterial.mUnificatable) {
									return;
								}
							} else {
								for (Dyes tDye : Dyes.VALUES) {
									if (aEvent.Name.endsWith(tDye.name().replaceFirst("dye", ""))) {
										GregtechOreDictUnificator.addToBlacklist(aEvent.Ore);
										GT_Log.ore.println(tModToName + " Oh man, why the fuck would anyone need a OreDictified Color for this, that is even too much for GregTech... do not report this, this is just a random Comment about how ridiculous this is.");
										return;
									}
								}
								//								System.out.println("Material Name: "+aEvent.Name+ " !!!Unknown Material detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just an Information, which you should pass to me.");
								//								GT_Log.ore.println(tModToName + " uses an unknown Material. Report this to GregTech.");
								return;
							}
						} else {
							aPrefix.add(GT_Utility.copyAmount(1L, new Object[]{aEvent.Ore}));
						}
					}
				} else if (aPrefix.mIsSelfReferencing) {
					aPrefix.add(GT_Utility.copyAmount(1L, new Object[]{aEvent.Ore}));
				} else {
					GT_Log.ore.println(tModToName + " uses a Prefix as full OreDict Name, and is therefor invalid.");
					aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
					return;
				}
			}
			GT_Log.ore.println(tModToName);
			GregtechOreDictEventContainer tOre = new GregtechOreDictEventContainer(aEvent, aPrefix, aMaterial, aMod);
			if ((!mOreDictActivated) || (!GregTech_API.sUnificationEntriesRegistered)) {
				mEvents.add(tOre);
			} else {
				mEvents.clear();
			}
			if (mOreDictActivated) {
				registerRecipes(tOre);
			}
		} catch (Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
	}

	private static final void registerRecipes(GregtechOreDictEventContainer tOre) {
		if ((tOre.mEvent.Ore == null) || (tOre.mEvent.Ore.getItem() == null)) {
			return;
		}
		if (tOre.mEvent.Ore.stackSize != 1) {
			tOre.mEvent.Ore.stackSize = 1;
		}
		if (tOre.mPrefix != null) {
			if (!tOre.mPrefix.isIgnored(tOre.mMaterial)) {
				tOre.mPrefix.processOre((GT_Materials) (tOre.mMaterial == null ? GT_Materials._NULL : tOre.mMaterial), tOre.mEvent.Name, tOre.mModID,
						GT_Utility.copyAmount(1L, new Object[]{tOre.mEvent.Ore}));
			}
		} else {
			//			System.out.println("Thingy Name: "+ aOre.mEvent.Name+ " !!!Unknown 'Thingy' detected!!! This Object seems to probably not follow a valid OreDictionary Convention, or I missed a Convention. Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just an Information, which you should pass to me.");
		}
	}

	private static final void registerRecipes(OreDictEventContainer aOre) {
		if ((aOre.mEvent.Ore == null) || (aOre.mEvent.Ore.getItem() == null)) {
			return;
		}
		if (aOre.mEvent.Ore.stackSize != 1) {
			aOre.mEvent.Ore.stackSize = 1;
		}
		if (aOre.mPrefix != null) {
			if (!aOre.mPrefix.isIgnored(aOre.mMaterial)) {
				aOre.mPrefix.processOre(aOre.mMaterial == null ? Materials._NULL : aOre.mMaterial, aOre.mEvent.Name, aOre.mModID,
						GT_Utility.copyAmount(1L, new Object[]{aOre.mEvent.Ore}));
			}
		} else {
			//			System.out.println("Thingy Name: "+ aOre.mEvent.Name+ " !!!Unknown 'Thingy' detected!!! This Object seems to probably not follow a valid OreDictionary Convention, or I missed a Convention. Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just an Information, which you should pass to me.");
		}
	}

	public static void registerUnificationEntries() {
		GregTech_API.sUnification.mConfig.save();
		GregTech_API.sUnification.mConfig.load();
		GregtechOreDictUnificator.resetUnificationEntries();
		for (GregtechOreDictEventContainer tOre : mEvents) {
			if ((!(tOre.mEvent.Ore.getItem() instanceof MetaGeneratedGregtechItems)) && (tOre.mPrefix != null) && (tOre.mPrefix.mIsUnificatable)
					&& (tOre.mMaterial != null)) {
				if (GregtechOreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
					GregtechOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
				} else {
					GregtechOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
					GregtechOreDictUnificator.set(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, (tOre.mModID != null) && (GregTech_API.sUnification.get(ConfigCategories.specialunificationtargets + "." + tOre.mModID, tOre.mEvent.Name, false)), true);
				}
			}
		}
		for (GregtechOreDictEventContainer tOre : mEvents) {
			if (((tOre.mEvent.Ore.getItem() instanceof MetaGeneratedGregtechItems)) && (tOre.mPrefix != null) && (tOre.mPrefix.mIsUnificatable)
					&& (tOre.mMaterial != null)) {
				if (GregtechOreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
					GregtechOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
				} else {
					GregtechOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
					GregtechOreDictUnificator.set(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, (tOre.mModID != null) &&
							(GregTech_API.sUnification.get(ConfigCategories.specialunificationtargets + "." + tOre.mModID, tOre.mEvent.Name, false)), true);
				}
			}
		}
		GregTech_API.sUnificationEntriesRegistered = true;
		GregTech_API.sUnification.mConfig.save();
		GT_Recipe.reInit();
	}

	public static void activateOreDictHandler() {
		mOreDictActivated = true;
		GregtechOreDictEventContainer tEvent;
		for (Iterator i$ = mEvents.iterator(); i$.hasNext(); registerRecipes(tEvent)) {
			tEvent = (GregtechOreDictEventContainer) i$.next();
		}
	}


	//Dunno
	public static class GregtechOreDictEventContainer {
		public final OreDictionary.OreRegisterEvent mEvent;
		public final GregtechOrePrefixes mPrefix;
		public final GT_Materials mMaterial;
		public final String mModID;

		public GregtechOreDictEventContainer(OreDictionary.OreRegisterEvent aEvent, GregtechOrePrefixes aPrefix, GT_Materials aMaterial, String aModID) {
			this.mEvent = aEvent;
			this.mPrefix = aPrefix;
			this.mMaterial = aMaterial;
			this.mModID = ((aModID == null) || (aModID.equals("UNKNOWN")) ? null : aModID);
		}
	}

	public static boolean areWeUsingGregtech5uExperimental(){
		int version = GregTech_API.VERSION;
		if (version == 508){
			return false;
		}
		else if (version == 509){
			return true;
		}
		else {
			return false;
		}
	}

}
