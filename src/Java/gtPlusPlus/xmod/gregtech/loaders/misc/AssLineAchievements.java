package gtPlusPlus.xmod.gregtech.loaders.misc;

import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GT_Mod;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class AssLineAchievements {

	public static int assReg = -1;
	public static ConcurrentHashMap<String, Achievement> mAchievementMap;
	public static ConcurrentHashMap<String, Boolean> mIssuedAchievementMap;
	public static int adjX = 5;
	public static int adjY = 9;
	private static boolean active = true;

	public AssLineAchievements() {
		Logger.INFO(active ? "Loading custom achievement page for Assembly Line recipes."
				: "Achievements are disabled.");	
		Utils.registerEvent(this);
	}
	
	private static boolean ready = false;
	private static int recipeTotal = 0;
	private static int recipeCount = 0;
	private static void init() {		
		if (!ready) {
			active = GT_Mod.gregtechproxy.mAchievements;
			try {
				recipeTotal = ((GT_Recipe.GT_Recipe_Map) StaticFields59.mAssLineVisualMapNEI.get(null)).mRecipeList.size();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				recipeTotal = 0;
			}
			mAchievementMap = new ConcurrentHashMap<String, Achievement>();
			mIssuedAchievementMap = new ConcurrentHashMap<String, Boolean>();			
		}		
		
	}
	
	public static void registerAchievements() {		
		if (active && mAchievementMap.size() > 0) {
			AchievementPage.registerAchievementPage(new AchievementPage("GT Assembly Line",
					(Achievement[]) mAchievementMap.values().toArray(new Achievement[mAchievementMap.size()])));			
		}
		else if (active) {
			Logger.INFO("Unable to register custom achievement page for Assembly Line recipes.");
		}
	}

	public static Achievement registerAssAchievement(GT_Recipe recipe) {
		init();
		String aSafeUnlocalName;
		// Debugging
		if (recipe == null) {
			Logger.INFO(
					"Someone tried to register an achievement for an invalid recipe. Please report this to Alkalus.");
			return null;
		}
		if (recipe.getOutput(0) == null) {
			Logger.INFO(
					"Someone tried to register an achievement for a recipe with null output. Please report this to Alkalus.");
			return null;
		}
		ItemStack aStack = recipe.getOutput(0);
		try {
			aSafeUnlocalName = aStack.getUnlocalizedName();
		} catch (Throwable t) {
			aSafeUnlocalName = ItemUtils.getUnlocalizedItemName(aStack);
		}
		
		Achievement aYouDidSomethingInGT;
		if (mAchievementMap.get(aSafeUnlocalName) == null) {
			assReg++;
			recipeCount++;
			aYouDidSomethingInGT = registerAchievement(aSafeUnlocalName, -(11 + assReg % 5), ((assReg) / 5) - 8,
					recipe.getOutput(0), AchievementList.openInventory, false);
		}
		else {
			aYouDidSomethingInGT = null;
		}
		if (recipeCount >= recipeTotal) {
			Logger.INFO("Critical mass achieved. ["+recipeCount+"]");
			registerAchievements();
		}
		
		return aYouDidSomethingInGT;
	}

	public static Achievement registerAchievement(String textId, int x, int y, ItemStack icon,
			Achievement requirement, boolean special) {
		if (!GT_Mod.gregtechproxy.mAchievements) {
			return null;
		}
		Achievement achievement = new Achievement(textId, textId, adjX + x, adjY + y, icon, requirement);
		if (special) {
			achievement.setSpecial();
		}
		achievement.registerStat();
		if (CORE.DEVENV) {
			GT_Log.out.println("achievement." + textId + "=");
			GT_Log.out.println("achievement." + textId + ".desc=");
		}
		mAchievementMap.put(textId, achievement);
		return achievement;
	}

	public static void issueAchievement(EntityPlayer entityplayer, String textId) {
		if (entityplayer == null || !GT_Mod.gregtechproxy.mAchievements) {
			return;
		}
		
		entityplayer.triggerAchievement((StatBase) getAchievement(textId));
	}

	public static Achievement getAchievement(String textId) {
		if (mAchievementMap.containsKey(textId)) {
			Logger.INFO("Found Achivement: "+textId);
			return (Achievement) mAchievementMap.get(textId);
		}
		return null;
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack stack = event.item.getEntityItem();
		String aPickupUnlocalSafe = ItemUtils.getUnlocalizedItemName(stack);
		if (player == null || stack == null) {
			return;
		}
		Logger.INFO("Trying to check for achievements");
		// Debug scanner unlocks all AL recipes in creative
		if (player.capabilities.isCreativeMode && aPickupUnlocalSafe.equals("gt.metaitem.01.32761")) {
			for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.mRecipeList) {
				issueAchievement(player, recipe.getOutput(0).getUnlocalizedName());
				recipe.mHidden = false;
			}
		}
		for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.mRecipeList) {
			
			String aSafeUnlocalName;
			if (recipe.getOutput(0) == null) {
				Logger.INFO(
						"Someone tried to register an achievement for a recipe with null output. Please report this to Alkalus.");
				continue;
			}
			ItemStack aStack = recipe.getOutput(0);
			aSafeUnlocalName = ItemUtils.getUnlocalizedItemName(aStack);			
			if (aSafeUnlocalName.equals(aPickupUnlocalSafe)) {
				issueAchievement(player, aSafeUnlocalName);
				recipe.mHidden = false;
				Logger.INFO("FOUND: " + aSafeUnlocalName + " | " + aPickupUnlocalSafe);
			}
			else {
				//Logger.INFO(aSafeUnlocalName + " | " + aPickupUnlocalSafe);
			}
		}
	}

}
