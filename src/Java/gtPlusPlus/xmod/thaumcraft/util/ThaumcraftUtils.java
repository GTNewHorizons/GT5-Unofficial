package gtPlusPlus.xmod.thaumcraft.util;

import static gtPlusPlus.xmod.thaumcraft.HANDLER_Thaumcraft.sItemsToGetAspects;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.thaumcraft.HANDLER_Thaumcraft;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectStack;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_AspectList_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_Aspect_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_ResearchCategories_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_ResearchCategoryList_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_ResearchItem_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_ResearchNoteData_Wrapper;
import gtPlusPlus.xmod.thaumcraft.aspect.TC_ResearchPage_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_CrucibleRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_IArcaneRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_InfusionEnchantmentRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_InfusionRecipe_Wrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ThaumcraftUtils {


	private static Class mClass_Aspect;
	private static Field mField_Aspects;

	public static boolean addAspectToItem(ItemStack item, TC_Aspect_Wrapper aspect, int amount) {
		return addAspectToItem(item, new TC_Aspect_Wrapper[] {aspect}, new Integer[] {amount});

	}

	public static boolean addAspectToItem(ItemStack item, TC_Aspect_Wrapper[] aspect, Integer[] amounts) {
		GTPP_AspectStack[] aspects = new GTPP_AspectStack[aspect.length];
		for (int g=0;g<aspect.length;g++) {
			if (amounts[g] != null && amounts[g] > 0) {
				//aspects[g] = new GTPP_AspectStack(aspect[g], amounts[g]);
			}
		}		
		Pair<ItemStack, GTPP_AspectStack[]> k = new Pair<ItemStack, GTPP_AspectStack[]>(item, aspects);
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


	public static TC_Aspect_Wrapper getAspect(String name) {
		return TC_Aspect_Wrapper.getAspect(name);
	}

	public static TC_Aspects getEnumAspect(String name) {
		TC_Aspect_Wrapper r = getAspect(name);
		return r.mGtEnumField;
	}
	
	

	public static Object addResearch(String aResearch, String aName, String aText, String[] aParentResearches, String aCategory, ItemStack aIcon, int aComplexity, int aType, int aX, int aY, List<GTPP_AspectStack> aAspects, ItemStack[] aResearchTriggers, Object[] aPages) {
		if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.researches, aResearch, true)) {
			return null;
		}
		TC_ResearchCategoryList_Wrapper tCategory = TC_ResearchCategories_Wrapper.getResearchList(aCategory);
		if (tCategory == null) {
			return null;
		}
		for (Iterator<TC_ResearchItem_Wrapper> i$ = tCategory.research.values().iterator(); i$.hasNext(); ) {
			TC_ResearchItem_Wrapper tResearch = (TC_ResearchItem_Wrapper) i$.next();
			if ((tResearch.displayColumn == aX) && (tResearch.displayRow == aY)) {
				aX += (aX > 0 ? 5 : -5);
				aY += (aY > 0 ? 5 : -5);
			}
		}
		TC_ResearchItem_Wrapper rResearch = new TC_ResearchItem_Wrapper(aResearch, aCategory, getAspectList_Ex(aAspects), aX, aY, aComplexity, aIcon);
		ArrayList<Object> tPages = new ArrayList<Object>(aPages.length);
		GT_LanguageManager.addStringLocalization("tc.research_name." + aResearch, aName);
		GT_LanguageManager.addStringLocalization("tc.research_text." + aResearch, "[GT++] " + aText);
		for (Object tPage : aPages) {
			if ((tPage instanceof String)) {
				tPages.add(new TC_ResearchPage_Wrapper((String) tPage));
			} else if ((tPage instanceof IRecipe)) {
				tPages.add(new TC_ResearchPage_Wrapper((IRecipe) tPage));
			} 
			else if ((tPage instanceof TC_IArcaneRecipe_Wrapper)) {
				tPages.add(new TC_ResearchPage_Wrapper((TC_IArcaneRecipe_Wrapper) tPage));
			} else if ((tPage instanceof TC_CrucibleRecipe_Wrapper)) {
				tPages.add(new TC_ResearchPage_Wrapper((TC_CrucibleRecipe_Wrapper) tPage));
			} else if ((tPage instanceof TC_InfusionRecipe_Wrapper)) {
				tPages.add(new TC_ResearchPage_Wrapper((TC_InfusionRecipe_Wrapper) tPage));
			} else if ((tPage instanceof TC_InfusionEnchantmentRecipe_Wrapper)) {
				tPages.add(new TC_ResearchPage_Wrapper((TC_InfusionEnchantmentRecipe_Wrapper) tPage));
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
		rResearch.setPages((TC_ResearchPage_Wrapper[]) tPages.toArray(new TC_ResearchPage_Wrapper[tPages.size()]));
		return rResearch.registerResearchItem();
	}


	public static Object addCrucibleRecipe(final String aResearch, final Object aInput, final ItemStack aOutput,
			final List<GTPP_AspectStack> aAspects) {
		if (GT_Utility.isStringInvalid((Object) aResearch) || aInput == null || aOutput == null || aAspects == null
				|| aAspects.isEmpty()) {
			return null;
		}
		return addCrucibleRecipe(aResearch, GT_Utility.copy(new Object[]{aOutput}),
				(aInput instanceof ItemStack || aInput instanceof ArrayList) ? aInput : aInput.toString(),
						getAspectList_Ex(aAspects));
	}

	public static Object addInfusionRecipe(final String aResearch, final ItemStack aMainInput, final ItemStack[] aSideInputs,
			final ItemStack aOutput, final int aInstability, final List<GTPP_AspectStack> aAspects) {
		if (GT_Utility.isStringInvalid((Object) aResearch) || aMainInput == null || aSideInputs == null
				|| aOutput == null || aAspects == null || aAspects.isEmpty()) {
			return null;
		}
		return addInfusionCraftingRecipe(aResearch, (Object) GT_Utility.copy(new Object[]{aOutput}),
				aInstability, getAspectList_Ex(aAspects), aMainInput, aSideInputs);
	}

	public static boolean registerThaumcraftAspectsToItem(final ItemStack aExampleStack,
			final List<GTPP_AspectStack> aAspects, final String aOreDict) {
		if (aAspects.isEmpty()) {
			return false;
		}
		registerObjectTag(aOreDict, getAspectList_Ex(aAspects));
		return true;
	}

	public static boolean registerThaumcraftAspectsToItem(final ItemStack aStack,
			final List<GTPP_AspectStack> aAspects, final boolean aAdditive) {
		try {
			if (aAspects.isEmpty()) {
				return false;
			}
			TC_AspectList_Wrapper h = getAspectList_Ex(aAspects);
			if (aAdditive && (h != null && h.size() > 0)) {
				registerComplexObjectTag(aStack, getAspectList_Ex(aAspects));
				return true;
			}
			else {
				Logger.MATERIALS("[Aspect] Failed adding aspects to "+aStack.getDisplayName()+".");
			}
			final TC_AspectList_Wrapper tAlreadyRegisteredAspects = getObjectAspects(aStack);
			if (tAlreadyRegisteredAspects == null || tAlreadyRegisteredAspects.size() <= 0) {
				registerObjectTag(aStack, getAspectList_Ex(aAspects));
			}
			return true;
		}
		catch (Throwable t) {
			Logger.MATERIALS("[Aspect] Failed adding aspects to "+aStack.getDisplayName()+".");
			t.printStackTrace();
			return false;
		}
	}
	

	private static final Class mClass_ThaumcraftApi;
	private static final Class mClass_ThaumcraftApiHelper;
	private static final Class mClass_AspectList;
	private static final Class mClass_ResearchManager;
	private static final Method mMethod_registerObjectTag1;
	private static final Method mMethod_registerObjectTag2;
	private static final Method mMethod_registerComplexObjectTag;
	private static final Method mMethod_addInfusionCraftingRecipe;
	private static final Method mMethod_addCrucibleRecipe;
	private static final Method mMethod_getObjectAspects;
	private static final Method mMethod_updateData;	
	private static final Method mMethod_getData;	

	private static final Field mField_PortholeBlacklist;
	static {
		/*
		 * Classes
		 */
		mClass_ThaumcraftApi = ReflectionUtils.getClass("thaumcraft.api.ThaumcraftApi");
		mClass_ThaumcraftApiHelper = ReflectionUtils.getClass("thaumcraft.api.ThaumcraftApiHelper");
		mClass_AspectList = ReflectionUtils.getClass("thaumcraft.api.aspects.AspectList");
		mClass_ResearchManager = ReflectionUtils.getClass("thaumcraft.common.lib.research.ResearchManager");

		/*
		 * Methods
		 */
		mMethod_registerObjectTag1 = ReflectionUtils.getMethod(mClass_ThaumcraftApi, "registerObjectTag",
				ItemStack.class, mClass_AspectList);

		mMethod_registerObjectTag2 = ReflectionUtils.getMethod(mClass_ThaumcraftApi, "registerObjectTag", String.class,
				mClass_AspectList);

		mMethod_registerComplexObjectTag = ReflectionUtils.getMethod(mClass_ThaumcraftApi, "registerComplexObjectTag",
				ItemStack.class, mClass_AspectList);

		mMethod_addInfusionCraftingRecipe = ReflectionUtils.getMethod(mClass_ThaumcraftApi, "addInfusionCraftingRecipe",
				String.class, Object.class, int.class, mClass_AspectList, ItemStack.class, ItemStack[].class);

		mMethod_addCrucibleRecipe = ReflectionUtils.getMethod(mClass_ThaumcraftApi, "addCrucibleRecipe", String.class,
				ItemStack.class, Object.class, mClass_AspectList);

		
		mMethod_getObjectAspects = ReflectionUtils.getMethod(mClass_ThaumcraftApiHelper, "getObjectAspects", ItemStack.class);
		
		
		mMethod_updateData = ReflectionUtils.getMethod(mClass_ResearchManager, "updateData", ItemStack.class, ReflectionUtils.getClass("thaumcraft.common.lib.research.ResearchNoteData"));
		mMethod_getData = ReflectionUtils.getMethod(mClass_ResearchManager, "getData", ItemStack.class);
		
		/*
		 * Fields
		 */
		mField_PortholeBlacklist = ReflectionUtils.getField(mClass_ThaumcraftApi, "portableHoleBlackList");

	}
	
	public static void registerObjectTag(ItemStack aStack, TC_AspectList_Wrapper aAspectList) {
		try {
			mMethod_registerObjectTag1.invoke(null, aStack, aAspectList.getVanillaAspectList());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void registerObjectTag(String aOreDict, TC_AspectList_Wrapper aAspectList) {
		try {
			mMethod_registerObjectTag2.invoke(null, aOreDict, aAspectList.getVanillaAspectList());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void registerComplexObjectTag(ItemStack aStack, TC_AspectList_Wrapper aAspectList) {
		try {
			mMethod_registerComplexObjectTag.invoke(null, aStack, aAspectList.getVanillaAspectList());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	public static TC_AspectList_Wrapper getObjectAspects(ItemStack aStack) {
		try {
			return new TC_AspectList_Wrapper(mMethod_getObjectAspects.invoke(null, aStack));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public static Object addCrucibleRecipe(String aResearch, ItemStack copy, Object aOutput,
			TC_AspectList_Wrapper aAspectList) {		
		try {
			return mMethod_addCrucibleRecipe.invoke(null, aResearch, copy, aOutput, aAspectList);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object addInfusionCraftingRecipe(String aResearch, Object copy, int aInstability,
			TC_AspectList_Wrapper aAspectList, ItemStack aMainInput, ItemStack[] aSideInputs) {		
		try {
			return mMethod_addInfusionCraftingRecipe.invoke(null, aResearch, copy, aInstability, aAspectList, aMainInput, aSideInputs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}	
	

	public static boolean registerPortholeBlacklistedBlock(final Block aBlock) {		
		try {
			((ArrayList<Block>) mField_PortholeBlacklist.get(null)).add(aBlock);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}

	
	public static String getTagFromAspectObject(Object aAspect) {
		try {
			Field aTagF = ReflectionUtils.getField(mClass_Aspect, "tag");		
			if (aTagF == null) {
				return null;
			}		
			String aTafB = (String) aTagF.get(aAspect);
			if (aTafB == null) {
				return null;
			}
			String aTag = aTafB.toLowerCase();	
			return aTag;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}
	

	public static void updateResearchNote(ItemStack a, TC_ResearchNoteData_Wrapper b) {
		//updateData(a, b.getResearchNoteData());		
		try {
			mMethod_updateData.invoke(a, b.getResearchNoteData());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	public static Object getResearchNoteData(ItemStack a) {
		//getData(a);		
		try {
			return mMethod_getData.invoke(a);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}	
		return null;
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

	public static TC_ResearchNoteData_Wrapper gatherResults(ItemStack note) {
		TC_ResearchNoteData_Wrapper research = null;
		if (isItemResearchNotes(note)) {
			research = new TC_ResearchNoteData_Wrapper(getResearchNoteData(note));
		}
		return research;
	}

	public static void placeAspectIntoResearchNote(ItemStack note, World aWorld, final int q, final int r, final TC_Aspect_Wrapper aspect) {
		/*TC_ResearchNoteData_Wrapper data = gatherResults(note);
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
		}*/
	}

	public static void completeResearchNote(World aWorld, ItemStack aStack) {
		if (!aWorld.isRemote) {
			if (isItemResearchNotes(aStack)) {
				aStack.setItemDamage(64);
			}
		}
	}
	
	public static synchronized final TC_AspectList_Wrapper getAspectList_Ex(final List<GTPP_AspectStack> aAspects) {
		final TC_AspectList_Wrapper rAspects = new TC_AspectList_Wrapper();
		for (final GTPP_AspectStack tAspect : aAspects) {
			rAspects.add(tAspect.mAspect, tAspect.mAmount);
		}
		return rAspects;
	}

	public static void addResearch(TC_ResearchItem_Wrapper tc_ResearchItem_Wrapper) {
		// TODO Auto-generated method stub
		
	}
}
