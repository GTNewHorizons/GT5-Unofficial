package gtPlusPlus.xmod.thaumcraft.util;

import static gtPlusPlus.xmod.thaumcraft.HANDLER_Thaumcraft.sItemsToGetAspects;
import static gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectCompat.getAspectList_Ex;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.thaumcraft.HANDLER_Thaumcraft;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_Aspects;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_Aspects.TC_AspectStack_Ex;
import gtPlusPlus.xmod.thaumcraft.objects.ResearchNoteDataWrapper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.api.research.*;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.ItemResearchNotes;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexUtils;

public class ThaumcraftUtils {

	public static boolean addAspectToItem(ItemStack item, Aspect aspect, int amount) {
		return addAspectToItem(item, getEnumAspect(aspect.getName()), amount);
	}

	/*public static boolean addAspectToItem(ItemStack item, Aspect[] aspects, int amount) {
		return addAspectToItem(item, getEnumAspect(aspect.getName()), amount);
	}*/

	public static boolean addAspectToItem(ItemStack item, GTPP_Aspects aspect, int amount) {
		return addAspectToItem(item, new GTPP_Aspects[] {aspect}, new Integer[] {amount});

	}

	public static boolean addAspectToItem(ItemStack item, GTPP_Aspects[] aspect, Integer[] amounts) {
		TC_AspectStack_Ex[] aspects = new TC_AspectStack_Ex[aspect.length];
		for (int g=0;g<aspect.length;g++) {
			if (amounts[g] != null && amounts[g] > 0) {
				aspects[g] = new TC_AspectStack_Ex(aspect[g], amounts[g]);
			}
		}		
		Pair<ItemStack, TC_AspectStack_Ex[]> k = new Pair<ItemStack, TC_AspectStack_Ex[]>(item, aspects);
		int mSizeA = sItemsToGetAspects.size();
		sItemsToGetAspects.put(k);
		if (sItemsToGetAspects.size() > mSizeA) {
			Logger.MATERIALS("[Aspect] Successfully queued an ItemStack for Aspect addition.");
			return true;
		}
		Logger.MATERIALS("[Aspect] Failed to queue an ItemStack for Aspect addition.");
		//Logger.INFO("[Aspect] ");
		return false;
	}


	public static Aspect getAspect(String name) {
		GTPP_Aspects r = getAspectEnum(name);
		return (r == null ? null : r.mAspect);
	}

	public static GTPP_Aspects getEnumAspect(String name) {
		GTPP_Aspects r = getAspectEnum(name);
		return (r == null ? null : r);
	}

	private static GTPP_Aspects getAspectEnum(String name) {
		GTPP_Aspects h = null;
		for (GTPP_Aspects f : GTPP_Aspects.values()) {
			if (f.mAspect.getName().toLowerCase().contains(name.toLowerCase())) {
				h = f;
			}
		}
		return h;		
	}

	public static Object addResearch(String aResearch, String aName, String aText, String[] aParentResearches, String aCategory, ItemStack aIcon, int aComplexity, int aType, int aX, int aY, List<TC_AspectStack_Ex> aAspects, ItemStack[] aResearchTriggers, Object[] aPages) {
		if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.researches, aResearch, true)) {
			return null;
		}
		ResearchCategoryList tCategory = ResearchCategories.getResearchList(aCategory);
		if (tCategory == null) {
			return null;
		}
		for (Iterator<ResearchItem> i$ = tCategory.research.values().iterator(); i$.hasNext(); ) {
			ResearchItem tResearch = (ResearchItem) i$.next();
			if ((tResearch.displayColumn == aX) && (tResearch.displayRow == aY)) {
				aX += (aX > 0 ? 5 : -5);
				aY += (aY > 0 ? 5 : -5);
			}
		}
		ResearchItem rResearch = new ResearchItem(aResearch, aCategory, getAspectList_Ex(aAspects), aX, aY, aComplexity, aIcon);
		ArrayList<ResearchPage> tPages = new ArrayList<ResearchPage>(aPages.length);
		GT_LanguageManager.addStringLocalization("tc.research_name." + aResearch, aName);
		GT_LanguageManager.addStringLocalization("tc.research_text." + aResearch, "[GT++] " + aText);
		for (Object tPage : aPages) {
			if ((tPage instanceof String)) {
				tPages.add(new ResearchPage((String) tPage));
			} else if ((tPage instanceof IRecipe)) {
				tPages.add(new ResearchPage((IRecipe) tPage));
			} else if ((tPage instanceof IArcaneRecipe)) {
				tPages.add(new ResearchPage((IArcaneRecipe) tPage));
			} else if ((tPage instanceof CrucibleRecipe)) {
				tPages.add(new ResearchPage((CrucibleRecipe) tPage));
			} else if ((tPage instanceof InfusionRecipe)) {
				tPages.add(new ResearchPage((InfusionRecipe) tPage));
			} else if ((tPage instanceof InfusionEnchantmentRecipe)) {
				tPages.add(new ResearchPage((InfusionEnchantmentRecipe) tPage));
			}
		}
		if ((aType & 0x40) != 0) {
			rResearch.setAutoUnlock();
		}
		if ((aType & 0x1) != 0) {
			rResearch.setSecondary();
		}
		if ((aType & 0x20) != 0) {
			rResearch.setSpecial();
		}
		if ((aType & 0x8) != 0) {
			rResearch.setVirtual();
		}
		if ((aType & 0x4) != 0) {
			rResearch.setHidden();
		}
		if ((aType & 0x10) != 0) {
			rResearch.setRound();
		}
		if ((aType & 0x2) != 0) {
			rResearch.setStub();
		}
		if (aParentResearches != null) {
			ArrayList<String> tParentResearches = new ArrayList<String>();
			for (String tParent : aParentResearches) {
				if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.researches, aResearch, true)) {
					tParentResearches.add(tParent);
				}
			}
			if (tParentResearches.size() > 0) {
				rResearch.setParents((String[]) tParentResearches.toArray(new String[tParentResearches.size()]));
				rResearch.setConcealed();
			}
		}
		if (aResearchTriggers != null) {
			rResearch.setItemTriggers(aResearchTriggers);
			rResearch.setHidden();
		}
		rResearch.setPages((ResearchPage[]) tPages.toArray(new ResearchPage[tPages.size()]));
		return rResearch.registerResearchItem();
	}


	public static Object addCrucibleRecipe(final String aResearch, final Object aInput, final ItemStack aOutput,
			final List<TC_AspectStack_Ex> aAspects) {
		if (GT_Utility.isStringInvalid((Object) aResearch) || aInput == null || aOutput == null || aAspects == null
				|| aAspects.isEmpty()) {
			return null;
		}
		return ThaumcraftApi.addCrucibleRecipe(aResearch, GT_Utility.copy(new Object[]{aOutput}),
				(aInput instanceof ItemStack || aInput instanceof ArrayList) ? aInput : aInput.toString(),
						getAspectList_Ex(aAspects));
	}

	public static Object addInfusionRecipe(final String aResearch, final ItemStack aMainInput, final ItemStack[] aSideInputs,
			final ItemStack aOutput, final int aInstability, final List<TC_AspectStack_Ex> aAspects) {
		if (GT_Utility.isStringInvalid((Object) aResearch) || aMainInput == null || aSideInputs == null
				|| aOutput == null || aAspects == null || aAspects.isEmpty()) {
			return null;
		}
		return ThaumcraftApi.addInfusionCraftingRecipe(aResearch, (Object) GT_Utility.copy(new Object[]{aOutput}),
				aInstability, getAspectList_Ex(aAspects), aMainInput, aSideInputs);
	}

	public static boolean registerThaumcraftAspectsToItem(final ItemStack aExampleStack,
			final List<TC_AspectStack_Ex> aAspects, final String aOreDict) {
		if (aAspects.isEmpty()) {
			return false;
		}
		ThaumcraftApi.registerObjectTag(aOreDict, getAspectList_Ex(aAspects));
		return true;
	}

	public static boolean registerThaumcraftAspectsToItem(final ItemStack aStack,
			final List<TC_AspectStack_Ex> aAspects, final boolean aAdditive) {
		try {
			if (aAspects.isEmpty()) {
				return false;
			}
			AspectList h = getAspectList_Ex(aAspects);
			if (aAdditive && (h != null && h.size() > 0)) {
				ThaumcraftApi.registerComplexObjectTag(aStack, getAspectList_Ex(aAspects));
				return true;
			}
			else {
				Logger.MATERIALS("[Aspect] Failed adding aspects to "+aStack.getDisplayName()+".");
			}
			final AspectList tAlreadyRegisteredAspects = ThaumcraftApiHelper.getObjectAspects(aStack);
			if (tAlreadyRegisteredAspects == null || tAlreadyRegisteredAspects.size() <= 0) {
				ThaumcraftApi.registerObjectTag(aStack, getAspectList_Ex(aAspects));
			}
			return true;
		}
		catch (Throwable t) {
			Logger.MATERIALS("[Aspect] Failed adding aspects to "+aStack.getDisplayName()+".");
			t.printStackTrace();
			return false;
		}
	}

	public static boolean registerPortholeBlacklistedBlock(final Block aBlock) {
		ThaumcraftApi.portableHoleBlackList.add(aBlock);
		return true;
	}

	public static TC_AspectStack_Ex convertAspectStack(TC_AspectStack gtType) {
		TC_AspectStack_Ex g = null;
		if (gtType == null) {
			return null;
		}
		else {
			String oldName = gtType.mAspect.name().toLowerCase();
			long oldAmount = gtType.mAmount;				
			for (GTPP_Aspects r : GTPP_Aspects.values()) {
				if (r.mAspect.getName().toLowerCase().contains(oldName)) {
					g = new TC_AspectStack_Ex(r, oldAmount);
					break;
				}
			}			
		}		
		return g;
	}

	public static List<TC_AspectStack_Ex> convertAspectStack(List<TC_AspectStack> p5) {
		List<TC_AspectStack_Ex> list = new ArrayList<TC_AspectStack_Ex>();
		for (TC_AspectStack h : p5) {
			list.add(convertAspectStack(h));
		}		
		return list;		
	}
	
	public static void updateResearchNote(ItemStack a, ResearchNoteData b) {
		updateResearchNote(a, new ResearchNoteDataWrapper(b));
	}

	public static void updateResearchNote(ItemStack a, ResearchNoteDataWrapper b) {
		ResearchManager.updateData(a, b);
	}

	public static boolean isItemResearchNotes(ItemStack aStack) {
		if (aStack != null && aStack.getItem() == HANDLER_Thaumcraft.mResearchNotes && HANDLER_Thaumcraft.mResearchNotes.getClass().isInstance(aStack.getItem())) {
			return true;
		}
		return false;
	}

	public static boolean isItemStackValidResearchNotes(ItemStack aStack) {
		if (isItemResearchNotes(aStack) && aStack.getItemDamage() < 64) {
			return true;
		}
		return false;
	}

	public static ResearchNoteDataWrapper gatherResults(ItemStack note) {
		ResearchNoteDataWrapper research = null;
		if (isItemResearchNotes(note)) {
			research = new ResearchNoteDataWrapper(ResearchManager.getData(note));
		}
		return research;
	}

	public static void placeAspectIntoResearchNote(ItemStack note, World aWorld, final int q, final int r, final Aspect aspect) {
		ResearchNoteDataWrapper data = gatherResults(note);
		String mGTPP = CORE.gameProfile.getName();
		EntityPlayer player = CORE.getFakePlayer(aWorld);

		if (isItemResearchNotes(note) && data != null && note.getItemDamage() < 64) {
			final boolean r2 = ResearchManager.isResearchComplete(mGTPP, "RESEARCHER1");
			final boolean r3 = ResearchManager.isResearchComplete(mGTPP, "RESEARCHER2");
			final HexUtils.Hex hex = new HexUtils.Hex(q, r);
			ResearchManager.HexEntry he = null;
			if (aspect != null) {
				he = new ResearchManager.HexEntry(aspect, 2);
				if (r3 && aWorld.rand.nextFloat() < 0.1f) {
					aWorld.playSoundAtEntity((Entity) player, "random.orb", 0.2f, 0.9f + player.worldObj.rand.nextFloat() * 0.2f);
				}
			} else {
				final float f = aWorld.rand.nextFloat();
				if (data.hexEntries.get(hex.toString()).aspect != null
						&& ((r2 && f < 0.25f) || (r3 && f < 0.5f))) {
					aWorld.playSoundAtEntity((Entity) player, "random.orb", 0.2f,
							0.9f + player.worldObj.rand.nextFloat() * 0.2f);
					ResearchManager.scheduleSave(player);
				}
				he = new ResearchManager.HexEntry((Aspect) null, 0);
			}
			data.hexEntries.put(hex.toString(), he);
			data.hexes.put(hex.toString(), hex);
			updateResearchNote(note, data);
			if (!aWorld.isRemote && ResearchManager.checkResearchCompletion(note, data,	player.getCommandSenderName())) {
				note.setItemDamage(64);
			}
		}
	}

	public static void completeResearchNote(World aWorld, ItemStack aStack) {
		if (!aWorld.isRemote) {
			if (isItemResearchNotes(aStack)) {
				aStack.setItemDamage(64);
			}
		}
	}
}
